package ncodedev.coffeebase.ui.utility;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.utils.PermissionsUtils;
import ncodedev.coffeebase.model.security.User;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ncodedev.coffeebase.utils.ToastUtils.showToast;

public class ImageHelper {

    private static ImageHelper instance;
    private ActivityResultLauncher<Intent> mGetPhotoImage;
    private ActivityResultLauncher<Intent> mGetGalleryImage;
    private boolean isReadPermissionGranted = false;
    private boolean isWritePermissionGranted = false;
    private ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private Dialog imageDialog;
    private Uri imageUri;

    public static ImageHelper getInstance() {
        if (instance == null) {
            return new ImageHelper();
        }
        return instance;
    }

    public void picassoSetImage(String imageUri, ImageView imageView) {
        Picasso.get()
                .load(imageUri)
                .into(imageView);
    }

    public void picassoSetImage(String imageUri, ImageView imageView, int placeHolder) {
        Picasso.get()
                .load(imageUri)
                .placeholder(placeHolder)
                .into(imageView);
    }

    public void showAddImageDialog(Activity activity) {
        imageDialog = new Dialog(activity);
        imageDialog.setContentView(R.layout.imagedialog);
        imageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton btnPhotoCamera = imageDialog.findViewById(R.id.btnPhotoCamera);
        ImageButton btnPhotoLibrary = imageDialog.findViewById(R.id.btnPhotoLibrary);
        btnPhotoLibrary.setOnClickListener(view -> {
            mGetGalleryImage.launch(new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
            imageDialog.hide();
        });
        btnPhotoCamera.setOnClickListener(view -> {
            mGetPhotoImage.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            imageDialog.hide();
        });
        imageDialog.show();
    }

    public void getPermissions(AppCompatActivity activity) {
        mPermissionResultLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    if (result.get(Manifest.permission.READ_EXTERNAL_STORAGE) != null) {
                        isReadPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE));
                    }
                    if (result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != null) {
                        isWritePermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                    }
                });
    }

    public void getCoffeeGalleryImage(AppCompatActivity activity, ImageView imgCoffee) {
        mGetGalleryImage = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();

                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        activity.getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                        picassoSetImage(imageUri.toString(), imgCoffee);
                        imgCoffee.setTag(imageUri);
                        imageDialog.hide();
                    } else {
                        showToast(activity, "Permission not granted!");
                    }
                });
    }

    public void getCoffeePhotoImage(AppCompatActivity activity, ImageView imgCoffee) {
        mGetPhotoImage = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");

                        if (isWritePermissionGranted) {
                            if (saveImageToExternalStorage(UUID.randomUUID().toString(), bitmap, activity)) {
                                picassoSetImage(imageUri.toString(), imgCoffee);
                                imgCoffee.setTag(imageUri);
                                imageDialog.hide();
                            }
                        } else {
                            showToast(activity, "Permission not granted!");
                        }
                    }
                });
        requestPermission(activity);
    }

    private void requestPermission(Activity activity) {
        boolean minSDK = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        isReadPermissionGranted = PermissionsUtils.checkReadStoragePermission(activity);
        isWritePermissionGranted = PermissionsUtils.checkWriteStoragePermission(activity);

        isWritePermissionGranted = isWritePermissionGranted || minSDK;

        List<String> permissionRequest = new ArrayList<>();
        if (!isReadPermissionGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!isWritePermissionGranted) {
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionRequest.isEmpty()) {
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }

    private boolean saveImageToExternalStorage(String imgName, Bitmap bmp, Activity activity) {
        Uri imageCollection;
        ContentResolver resolver = activity.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        imageUri = resolver.insert(imageCollection, contentValues);

        try {
            OutputStream outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream);
            return true;
        } catch (Exception e) {
            showToast(activity, "Image not saved!");
        }
        return false;
    }

    public void setPicassoInstance(Context context) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + User.getInstance().getToken())
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();

        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException e) {
            Log.d("ImageHelper", "Picasso instance already set, ignoring");
        }
    }
}
