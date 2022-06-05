package com.ncode.coffeebase.utils;

import android.util.Log;
import retrofit2.Call;

public class Logger {
    public static void logCall (String TAG, Call call) {
        Log.d(TAG, "Call: " + call.request());
    }

    public static void logCallFail(String TAG, Call call) {
        Log.d(TAG, "Call failed: " + call.request());
    }
}
