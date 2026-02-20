package com.example.hamamlaha.models;

import com.example.hamamlaha.R;

public enum SalonCategory {

    HAIR("Hair", "שיער", R.drawable.hairicon),
    NAILS("Nails", "ציפורניים", R.drawable.nailsicon),
    EYEBROWS("Eyebrows", "גבות", R.drawable.eyebrowsicon),
    EYELASHES("Eyelashes", "ריסים", R.drawable.eyelashesicon),
    LASER("Laser", "לייזר", R.drawable.lashericon),
    PEDICUR("Pedicur", "פדיקור", R.drawable.pedicuricon);

    private final String englishName;
    private final String hebrewName;
    private final int iconResId;

    SalonCategory(String englishName, String hebrewName, int iconResId) {
        this.englishName = englishName;
        this.hebrewName = hebrewName;
        this.iconResId = iconResId;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getHebrewName() {
        return hebrewName;
    }

    public int getIconResId() {
        return iconResId;
    }

    // לשימוש כששומרים כ-String ב-Firebase
    public static SalonCategory fromString(String value) {
        return SalonCategory.valueOf(value);
    }
}
