package ncodedev.coffeebase.model.error;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

public record ErrorResponse(
        @Expose
        LocalDateTime timestamp,
        @Expose
        int httpStatus,
        @Expose
        String error,
        @Expose
        String message
) {
}
