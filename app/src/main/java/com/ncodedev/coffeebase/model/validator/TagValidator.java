package com.ncodedev.coffeebase.model.validator;

import android.text.TextUtils;
import android.widget.TextView;

public class TagValidator {

    public static boolean tagName(TextView text, String tagName, String errorMessage) {
        final String tagNameWithoutHash = tagName.replace('#', ' ');
        if (TextUtils.isEmpty(tagNameWithoutHash.trim())) {
            text.setError(errorMessage);
            return false;
        }
        return true;
    }
}
