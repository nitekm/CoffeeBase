package ncodedev.coffeebase.model.enums;

import ncodedev.coffeebase.R;

public enum ErrorMessage {
    NOT_PERMITTED_TO_MODIFY("User is not permitted to modify given item", R.string.not_permitted_to_modify),
//    BREW_NOT_FOUND("Brew with given ID not found"),
    COFFEE_NOT_FOUND("Coffee with given ID not found", R.string.coffee_not_found);
//    TAG_NOT_FOUND("Tag with given id not found"),
//    NO_MATCHING_STRATEGY_FOR_BREW_ACTION("Couldn't process given action"),
//    USER_NOT_FOUND("No user found with given user ID"),
//    FILE_UNREADABLE("File is unreadable or does not exists"),
//    COULD_NOT_LOAD_FILE("Could not load file"),
//    COULD_NOT_DELETE_FILE("Could not delete file"),
//    INVALID_TOKEN("Token not valid or has expired"),
//    DELETE_FAILED("Delete operation failed");

    private final String message;
    private final int resId;
    ErrorMessage(String message, int resId) {
        this.message = message;
        this.resId = resId;
    }

    public static ErrorMessage fromMessage(String message) {
        for (ErrorMessage e : ErrorMessage.values()) {
            if (e.message.equalsIgnoreCase(message)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No enum found with message: " + message);
    }

    public int getResId() {
        return this.resId;
    }
}
