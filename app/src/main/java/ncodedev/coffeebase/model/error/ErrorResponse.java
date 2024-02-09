package ncodedev.coffeebase.model.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class ErrorResponse {
        @Expose
        @SerializedName("timestamp")
        private LocalDateTime timestamp;
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

        public LocalDateTime getTimestamp() {
                return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
                this.timestamp = timestamp;
        }

        public int getHttpStatus() {
                return httpStatus;
        }

        public void setHttpStatus(int httpStatus) {
                this.httpStatus = httpStatus;
        }

        public String getError() {
                return error;
        }

        public void setError(String error) {
                this.error = error;
        }

        public String getMessage() {
                return message;
        }

        public void setMessage(String message) {
                this.message = message;
        }
}
