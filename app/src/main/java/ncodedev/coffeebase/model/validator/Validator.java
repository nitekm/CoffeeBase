package ncodedev.coffeebase.model.validator;

import android.text.TextUtils;
import android.widget.TextView;

public class Validator {
    @SuppressWarnings("ConstantConditions")
    public static boolean textNotBlank(TextView text, String errorMessage) {
        if (TextUtils.getTrimmedLength(text.getText().toString()) > 0) {
            return true;
        }
        text.setError(errorMessage);
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean numberFromTo(TextView numberToCheck, int min, int max, String errorMessage) {
        String numberAsString = numberToCheck.getText().toString();
        if (TextUtils.getTrimmedLength(numberAsString) > 0) {
            int number = Integer.parseInt(numberAsString);
            if (number < min || number > max) {
                numberToCheck.setError(errorMessage);
                return false;
            }
        }
        return true;
    }
}
