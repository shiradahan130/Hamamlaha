package com.example.hamamlaha.utils;

import android.util.Patterns;

import androidx.annotation.Nullable;

/// מחלקת וולידציה לבדיקת קלט מהמשתמש
/// מכילה מתודות סטטיות לבדיקת תקינות קלט,
/// כגון אימייל, סיסמה, טלפון, שם וכו׳
public class Validator {

    /// בדיקה האם האימייל תקין
    /// @param email האימייל לבדיקה
    /// @return true אם האימייל תקין, false אחרת
    /// @see Patterns#EMAIL_ADDRESS
    public static boolean isEmailValid(@Nullable String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /// בדיקה האם הסיסמה תקינה
    /// @param password הסיסמה לבדיקה
    /// @return true אם הסיסמה תקינה (לפחות 6 תווים), false אחרת
    public static boolean isPasswordValid(@Nullable String password) {
        return password != null && password.length() >= 6;
    }

    /// בדיקה האם מספר הטלפון תקין
    /// @param phone מספר הטלפון לבדיקה
    /// @return true אם מספר הטלפון תקין, false אחרת
    /// @see Patterns#PHONE
    public static boolean isPhoneValid(@Nullable String phone) {
        return phone != null && phone.length() >= 10 && Patterns.PHONE.matcher(phone).matches();
    }

    /// בדיקה האם השם תקין
    /// @param name השם לבדיקה
    /// @return true אם השם תקין (לפחות 2 תווים), false אחרת
    public static boolean isNameValid(@Nullable String name) {
        return name != null && name.length() >= 2;
    }
}