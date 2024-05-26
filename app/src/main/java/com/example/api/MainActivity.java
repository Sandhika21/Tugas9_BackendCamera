package com.example.api;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainActivityCallback {

    private MainActivityController controller = new MainActivityController(this, this);
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    private ImageView imageView;
    private Button takePictureButton;
    private Button uploadButton;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraActivityResultLauncher = getCameraActivityResultLauncher();

        imageView = findViewById(R.id.IV_camera_view);
        takePictureButton = findViewById(R.id.Btn_take_pict);
        uploadButton = findViewById(R.id.Btn_upload);

        takePictureButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Btn_take_pict) {
            try {
                // Take and save image
                Intent cameraActivityIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(cameraActivityIntent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else if (v.getId() == R.id.Btn_upload) {
            try {
                byte[] imageByteArray = controller.bitmapToByteArray(((BitmapDrawable) imageView.getDrawable()).getBitmap());
                controller.uploadImage(getString(R.string.BACKEND_URL), imageByteArray);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void updateImageView(Bitmap imageBitmap) {
        imageView.setImageBitmap(imageBitmap);
    }

    @Override
    public void showUploadButton() {
        uploadButton.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private ActivityResultLauncher<Intent> getCameraActivityResultLauncher() {
        try {
            return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    controller.saveImageFile(data);
                }
            });
        } catch (Exception ex) {
            throw ex;
        }
    }
}
