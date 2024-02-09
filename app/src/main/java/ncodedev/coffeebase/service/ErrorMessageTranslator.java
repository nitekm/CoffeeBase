package ncodedev.coffeebase.service;

import android.content.Context;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.enums.ErrorMessage;
import ncodedev.coffeebase.model.error.ErrorResponse;
import ncodedev.coffeebase.utils.ToastUtils;

public class ErrorMessageTranslator {
    public static void tranlateAndToastErrorMessage(Context context, ErrorResponse errorResponse) {
        var errorMessageEnum = ErrorMessage.fromMessage(errorResponse.getMessage());
        var fullErrorMessage = context.getString(R.string.error) + " " + context.getString(errorMessageEnum.getResId());
        ToastUtils.showToast(context, fullErrorMessage);
    }
}
