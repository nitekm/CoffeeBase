package ncodedev.coffeebase.model.enums;

import ncodedev.coffeebase.R;

public enum ErrorMessage {
    NOT_PERMITTED_TO_MODIFY("User is not permitted to modify given item", R.string.not_permitted_to_modify),
    BREW_NOT_FOUND("Brew with given ID not found", R.string.brew_not_found),
    COFFEE_NOT_FOUND("Coffee with given ID not found", R.string.coffee_not_found),
    TAG_NOT_FOUND("Tag with given id not found", R.string.tag_not_found),
    NO_MATCHING_STRATEGY_FOR_BREW_ACTION("Couldn't process given action", R.string.no_matching_strategy_for_brew_action),
    USER_NOT_FOUND("No user found with given user ID", R.string.user_not_found),
    FILE_UNREADABLE("File is unreadable or does not exists", R.string.file_unreadable),
    COULD_NOT_LOAD_FILE("Could not load file", R.string.could_not_load_file),
    COULD_NOT_DELETE_FILE("Could not delete file", R.string.could_not_delete_file),
    INVALID_TOKEN("Token not valid or has expired", R.string.invalid_token),
    DELETE_FAILED("Delete operation failed", R.string.delete_failed),
    GENERIC_ERROR("", R.string.something_went_wrong);

    private final String message;
    private final int resId;
    ErrorMessage(String message, int resId) {
        this.message = message;
        this.resId = resId;
    }

    public static ErrorMessage fromMessage(String message) {
        for (ErrorMessage errorMessage : ErrorMessage.values()) {
            if (errorMessage.message.equalsIgnoreCase(message)) {
                return errorMessage;
            }
        }
        return GENERIC_ERROR;
    }

    public int getResId() {
        return this.resId;
    }
}
