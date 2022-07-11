package com.disu.facecheck;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.disu.facecheck.databinding.ActivityMainBinding;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 09/07/2022 | 10119239 | DEA INESIA SRI UTAMI | IF6
 */

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clickListener();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    void clickListener() {
        binding.pilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bukaGallery();
            }
        });

        binding.cobaLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bukaGallery();
            }
        });

        binding.uploadGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadGambar();
            }
        });
    }

    void bukaGallery() {
        binding.umur.setVisibility(View.GONE);
        binding.gender.setVisibility(View.GONE);
        binding.cobaLagi.setVisibility(View.GONE);
        binding.pilihGambar.setVisibility(View.VISIBLE);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 10);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Context context = MainActivity.this;
                path = RealPathUtil.getRealPath(context, uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                binding.img.setImageBitmap(bitmap);
                binding.headline.setVisibility(View.GONE);
                binding.img.setVisibility(View.VISIBLE);
                binding.pilihGambar.setText("Ganti Gambar");
                binding.uploadGambar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void uploadGambar() {
        binding.progress.setVisibility(View.VISIBLE);

        File file = new File(path);
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        API api = RetrofitClient.getInstance().getAPI();
        Call<List<Gambar>> upload = api.uploadGambar(body);
        upload.enqueue(new Callback<List<Gambar>>() {
            @Override
            public void onResponse(Call<List<Gambar>> call, Response<List<Gambar>> response) {
                if (response.isSuccessful()) {
                    binding.progress.setVisibility(View.GONE);
                    binding.pilihGambar.setVisibility(View.GONE);
                    binding.uploadGambar.setVisibility(View.GONE);
                    binding.umur.setVisibility(View.VISIBLE);
                    binding.gender.setVisibility(View.VISIBLE);
                    binding.cobaLagi.setVisibility(View.VISIBLE);

                    String umur = response.body().get(0).getYears();
                    String gender = response.body().get(0).getGender();

                    binding.umur.setText("Umur : " + umur);
                    binding.gender.setText("Gender : " + gender);
                }
            }

            @Override
            public void onFailure(Call<List<Gambar>> call, Throwable t) {
                Log.d(TAG, "onFailure something went wrong : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
            }
        });
    }
}