package com.example.hamamlaha.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hamamlaha.models.Appointment;
import com.example.hamamlaha.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/// מחלקת שירות לניהול כל התקשורת עם Firebase
/// מממשת את תבנית ה־Singleton — קיים רק עותק אחד של המחלקה בכל האפליקציה
/// מחולקת לשני אזורים: ניהול משתמשים וניהול תורים
public class DatabaseService {

    private static final String TAG = "DatabaseService";

    /// נתיבים במסד הנתונים
    private static final String USERS_PATH = "users",
            TOR_PATH = "appointment";

    /// ממשק Callback — מופעל כשהפעולה מסתיימת (הצלחה או כישלון)
    /// T הוא סוג הנתון המוחזר (User, Appointment, List וכו׳)
    public interface DatabaseCallback<T> {
        void onCompleted(T object);
        void onFailed(Exception e);
    }

    /// המופע היחיד של המחלקה (Singleton)
    private static DatabaseService instance;
    private final DatabaseReference databaseReference;

    /// constructor פרטי — מונע יצירת עותקים נוספים
    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://hamamlaha-59048-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference();
    }

    /// מחזיר את המופע היחיד של DatabaseService
    /// אם לא קיים — יוצר אחד חדש
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    /// כתיבת נתונים לנתיב מסוים ב־Firebase
    /// @param path הנתיב לכתיבה
    /// @param data הנתון לשמירה
    /// @param callback פעולה שתופעל בסיום (יכול להיות null)
    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        readData(path).setValue(data, (error, ref) -> {
            if (error != null) {
                if (callback == null) return;
                callback.onFailed(error.toException());
            } else {
                if (callback == null) return;
                callback.onCompleted(null);
            }
        });
    }

    /// מחיקת נתונים מנתיב מסוים ב־Firebase
    /// @param path הנתיב למחיקה
    /// @param callback פעולה שתופעל בסיום (יכול להיות null)
    private void deleteData(@NotNull final String path, @Nullable final DatabaseCallback<Void> callback) {
        readData(path).removeValue((error, ref) -> {
            if (error != null) {
                if (callback == null) return;
                callback.onFailed(error.toException());
            } else {
                if (callback == null) return;
                callback.onCompleted(null);
            }
        });
    }

    /// מחזיר reference לנתיב מסוים ב־Firebase
    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    /// שליפת אובייקט בודד מ־Firebase לפי נתיב וסוג
    /// @param path הנתיב לשליפה
    /// @param clazz סוג האובייקט המוחזר
    /// @param callback פעולה שתופעל כשהנתון מגיע
    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    /// שליפת רשימת אובייקטים מ־Firebase לפי נתיב וסוג
    /// ממיר את כל הילדים של הנתיב לרשימה
    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<List<T>> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> tList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Log.d("RAW_DATA", "raw snapshot: " + dataSnapshot.getValue());
                T t = dataSnapshot.getValue(clazz);
                tList.add(t);
            });
            callback.onCompleted(tList);
        });
    }

    /// יצירת מזהה ייחודי חדש בנתיב מסוים
    /// Firebase מייצר מזהה אוטומטי עם push()
    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    /// עדכון בטוח של נתון ב־Firebase באמצעות Transaction
    /// מונע קונפליקטים כשמספר משתמשים מעדכנים בו זמנית
    private <T> void runTransaction(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull UnaryOperator<T> function, @NotNull final DatabaseCallback<T> callback) {
        readData(path).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                T currentValue = currentData.getValue(clazz);
                if (currentValue == null) {
                    currentValue = function.apply(null);
                } else {
                    currentValue = function.apply(currentValue);
                }
                currentData.setValue(currentValue);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.e(TAG, "Transaction failed", error.toException());
                    callback.onFailed(error.toException());
                    return;
                }
                T result = currentData != null ? currentData.getValue(clazz) : null;
                callback.onCompleted(result);
            }
        });
    }

    // region אזור משתמשים

    /// יצירת מזהה ייחודי חדש למשתמש
    public String generateUserId() {
        return generateNewId(USERS_PATH);
    }

    /// שמירת משתמש חדש ב־Firebase
    /// הנתיב: users/{userId}
    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData(USERS_PATH + "/" + user.getId(), user, callback);
    }

    /// שליפת משתמש לפי מזהה
    /// הנתיב: users/{uid}
    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData(USERS_PATH + "/" + uid, User.class, callback);
    }

    /// שליפת כל המשתמשים מ־Firebase
    public void getUserList(@NotNull final DatabaseCallback<List<User>> callback) {
        getDataList(USERS_PATH, User.class, callback);
    }

    /// מחיקת משתמש לפי מזהה
    public void deleteUser(@NotNull final String uid, @Nullable final DatabaseCallback<Void> callback) {
        deleteData(USERS_PATH + "/" + uid, callback);
    }

    /// חיפוש משתמש לפי אימייל וסיסמה — משמש להתחברות
    /// שולף את כל המשתמשים ומחפש התאמה
    /// מחזיר null אם לא נמצא משתמש מתאים
    public void getUserByEmailAndPassword(@NotNull final String email, @NotNull final String password, @NotNull final DatabaseCallback<User> callback) {
        getUserList(new DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                Log.d("DB_SERVICE", "total users: " + users.size());
                for (User user : users) {
                    Log.d("DB_SERVICE", "checking user: " + user.getEmail() + " | admin: " + user.isAdmin());
                    if (Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), password)) {
                        Log.d("DB_SERVICE", "FOUND USER: " + user.toString());
                        callback.onCompleted(user);
                        return;
                    }
                }
                Log.d("DB_SERVICE", "USER NOT FOUND");
                callback.onCompleted(null);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("DB_SERVICE", "failed to get users", e);
                callback.onFailed(e);
            }
        });
    }

    /// בדיקה האם אימייל כבר קיים במסד הנתונים — משמש בהרשמה
    /// מחזיר true אם האימייל תפוס, false אם פנוי
    public void checkIfEmailExists(@NotNull final String email, @NotNull final DatabaseCallback<Boolean> callback) {
        getUserList(new DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                for (User user : users) {
                    if (Objects.equals(user.getEmail(), email)) {
                        callback.onCompleted(true);
                        return;
                    }
                }
                callback.onCompleted(false);
            }

            @Override
            public void onFailed(Exception e) {
                callback.onFailed(e);
            }
        });
    }

    /// עדכון פרטי משתמש קיים ב־Firebase
    /// משתמש ב־Transaction למניעת קונפליקטים
    public void updateUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        runTransaction(USERS_PATH + "/" + user.getId(), User.class, currentUser -> user, new DatabaseCallback<User>() {
            @Override
            public void onCompleted(User object) {
                if (callback != null) {
                    callback.onCompleted(null);
                }
            }

            @Override
            public void onFailed(Exception e) {
                if (callback != null) {
                    callback.onFailed(e);
                }
            }
        });
    }

    // endregion אזור משתמשים

    // region אזור תורים

    /// יצירת מזהה ייחודי חדש לתור
    public String generateAppointmentId() {
        return generateNewId(TOR_PATH);
    }

    /// שמירת תור חדש ב־Firebase (משמש גם לעדכון סטטוס)
    /// הנתיב: appointment/{appointmentId}
    public void createNewAppointment(@NotNull final Appointment appointment, @Nullable final DatabaseCallback<Void> callback) {
        writeData(TOR_PATH + "/" + appointment.getAppointmentId(), appointment, callback);
    }

    /// שליפת תור בודד לפי מזהה
    public void getAppointment(@NotNull final String aid, @NotNull final DatabaseCallback<Appointment> callback) {
        getData(TOR_PATH + "/" + aid, Appointment.class, callback);
    }

    /// שליפת כל התורים מ־Firebase
    public void getAppointmentList(@NotNull final DatabaseCallback<List<Appointment>> callback) {
        getDataList(TOR_PATH, Appointment.class, callback);
    }

    /// מחיקת תור לפי מזהה
    public void deleteAppointment(@NotNull final String aid, @Nullable final DatabaseCallback<Void> callback) {
        deleteData(TOR_PATH + "/" + aid, callback);
    }

    // endregion אזור תורים
}