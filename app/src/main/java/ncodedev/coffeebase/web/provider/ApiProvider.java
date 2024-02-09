//package ncodedev.coffeebase.web.provider;
//
//import ncodedev.coffeebase.model.error.ErrorResponse;
//import ncodedev.coffeebase.web.api.Api;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//import retrofit2.Converter;
//
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//
//public abstract class ApiProvider {
////    protected handleErrorResponse();
//
//    public static ErrorResponse parseError(RetrofitApiCreator retrofitApiCreator, Response<?> response) {
//
//        Converter<ResponseBody, ErrorResponse> converter = retrofitApiCreator.getRetrofit()
//                .responseBodyConverter(ErrorResponse.class, new Annotation[0]);
//
//        ErrorResponse error;
//
//        try {
//            error = converter.convert(response.errorBody());
//        } catch (IOException e) {
//            return new ApiError();
//        }
//
//        return error;
//    }
//}
