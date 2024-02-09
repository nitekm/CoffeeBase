package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.error.ErrorResponse;

public interface ResponseListener {
    void handleError(ErrorResponse errorResponse);
}
