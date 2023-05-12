package ncodedev.coffeebase.ui.utility;

import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import com.google.android.material.textfield.TextInputLayout;
import ncodedev.coffeebase.R;

import java.util.List;
import java.util.Objects;

public class CoffeeActivityViewAdjuster {
    private static boolean isPrevHidden = false;
    public static void hideBlankTextViewsAndAdjustConstraints(List<TextInputLayout> inputLayouts, int lastPresentElementId) {

        for (TextInputLayout layout:inputLayouts) {
            if (isPrevHidden) {
                adjustConstraints(layout, lastPresentElementId);
            }

            String layoutText = Objects.requireNonNull(layout.getEditText()).getText().toString();
            if (isBlankOrDefault(layoutText)) {
                hideBlank(layout);
            } else {
                lastPresentElementId = layout.getId();
                isPrevHidden = false;
            }
        }
    }

    private static void hideBlank(TextInputLayout layout) {
        layout.setVisibility(View.GONE);
        isPrevHidden = true;
    }

    private static void adjustConstraints(TextInputLayout layout, int lastPresentElementId) {
        LayoutParams layoutParams = (LayoutParams) layout.getLayoutParams();
        layoutParams.topToBottom = lastPresentElementId;
        layout.setLayoutParams(layoutParams);
    }

    private static boolean isBlankOrDefault(String text) {
        return text.isEmpty() ||
                text.equalsIgnoreCase(String.valueOf(R.string.continent)) ||
                text.equalsIgnoreCase(String.valueOf(R.string.roast_profile));
    }
}
