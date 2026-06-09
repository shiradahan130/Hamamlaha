package com.example.hamamlaha.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.hamamlaha.models.User;
import com.google.gson.Gson;

/// מחלקת עזר לפעולות שמירה מקומית
/// מכילה מתודות לשמירה ושליפה של נתונים מה־SharedPreferences
/// מכילה גם מתודות לניקוי והסרת נתונים מה־SharedPreferences
/// @see SharedPreferences
public class SharedPreferencesUtil {

    /// שם קובץ ה־SharedPreferences
    /// @see Context#getSharedPreferences(String, int)
    private static final String PREF_NAME = "com.example.hamamlaha.PREFERENCE_FILE_KEY";

    /// שמירת מחרוזת ב־SharedPreferences
    /// @param context הקונטקסט לשימוש
    /// @param key המפתח לשמירה
    /// @param value המחרוזת לשמירה
    /// @see SharedPreferences.Editor#putString(String, String)
    private static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /// שליפת מחרוזת מה־SharedPreferences
    /// @param context הקונטקסט לשימוש
    /// @param key המפתח לשליפה
    /// @param defaultValue ערך ברירת מחדל אם המפתח לא נמצא
    /// @return המחרוזת השמורה ב־SharedPreferences
    /// @see SharedPreferences#getString(String, String)
    private static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    /// שמירת מספר שלם ב־SharedPreferences
    /// @param context הקונטקסט לשימוש
    /// @param key המפתח לשמירה
    /// @param value המספר לשמירה
    /// @see SharedPreferences.Editor#putInt(String, int)
    private static void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /// שליפת מספר שלם מה־SharedPreferences
    /// @param context הקונטקסט לשימוש
    /// @param key המפתח לשליפה
    /// @param defaultValue ערך ברירת מחדל אם המפתח לא נמצא
    /// @return המספר השלם השמור ב־SharedPreferences
    /// @see SharedPreferences#getInt(String, int)
    private static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /// ניקוי כל הנתונים מה־SharedPreferences
    /// @param context הקונטקסט לשימוש
    /// @see SharedPreferences.Editor#clear()
    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /// הסרת מפתח ספציפי מה־SharedPreferences
    /// @param context הקונטקסט לשימוש
    /// @param key המפתח להסרה
    /// @see SharedPreferences.Editor#remove(String)
    private static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /// בדיקה האם מפתח קיים ב־SharedPreferences
    /// @param context הקונטקסט לשימוש
    /// @param key המפתח לבדיקה
    /// @return true אם המפתח קיים, false אחרת
    /// @see SharedPreferences#contains(String)
    private static boolean contains(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    private static <T> void saveObject(Context context, String key, T object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        saveString(context, key, json);
    }

    private static <T> T getObject(Context context, String key, Class<T> type) {
        String json = getString(context, key, null);
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /// שמירת אובייקט משתמש ב־SharedPreferences
    /// @param context הקונטקסט לשימוש
    /// @param user אובייקט המשתמש לשמירה
    /// @see User
    public static void saveUser(Context context, User user) {
        saveObject(context, "user", user);
    }

    /// שליפת אובייקט המשתמש מה־SharedPreferences
    /// @param context הקונטקסט לשימוש
    /// @return אובייקט המשתמש השמור, או null אם לא מחובר
    /// @see User
    /// @see #isUserLoggedIn(Context)
    public static User getUser(Context context) {
        if (!isUserLoggedIn(context)) {
            return null;
        }
        return getObject(context, "user", User.class);
    }

    /// התנתקות המשתמש על ידי מחיקת הנתונים מה־SharedPreferences
    /// @param context הקונטקסט לשימוש
    public static void signOutUser(Context context) {
        remove(context, "user");
    }

    /// בדיקה האם משתמש מחובר
    /// @param context הקונטקסט לשימוש
    /// @return true אם המשתמש מחובר, false אחרת
    /// @see #contains(Context, String)
    public static boolean isUserLoggedIn(Context context) {
        return contains(context, "user");
    }

    /// שליפת מזהה המשתמש המחובר
    /// @param context הקונטקסט לשימוש
    /// @return מזהה המשתמש, או null אם לא מחובר
    @Nullable
    public static String getUserId(Context context) {
        User user = getUser(context);
        if (user != null) {
            return user.getId();
        }
        return null;
    }
}