package ncodedev.coffeebase.model.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
//        @Expose
//        @SerializedName("timestamp")
//        private LocalDateTime timestamp;
        @Expose
        @SerializedName("httpStatus")
        private int httpStatus;
        @Expose
        @SerializedName("error")
        private String error;
        @Expose
        @SerializedName("message")
        private String message;

        public ErrorResponse() {
        }

        public String getMessage() {
                return message;
        }
}
