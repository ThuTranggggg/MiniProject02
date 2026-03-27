package com.example.shoppingapp.utils;

import android.content.Context;

public final class ImageUtils {

    private ImageUtils() {
    }

    public static int getDrawableResId(Context context, String imageResName, int fallbackResId) {
        if (imageResName == null || imageResName.trim().isEmpty()) {
            return fallbackResId;
        }
        int resourceId = context.getResources().getIdentifier(
                imageResName,
                "drawable",
                context.getPackageName()
        );
        return resourceId != 0 ? resourceId : fallbackResId;
    }
}
