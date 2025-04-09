package com.pixelwall.myapp;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WallpaperDetailActivity extends AppCompatActivity {
    private ImageView detailWallpaperImage;
    private Button setWallpaperButton;
    private Button downloadButton;
    private String wallpaperUrl;
    private String wallpaperId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);

        wallpaperUrl = getIntent().getStringExtra("wallpaper_url");
        wallpaperId = getIntent().getStringExtra("wallpaper_id");

        initializeViews();
        setupToolbar();
        loadWallpaper();
        setupClickListeners();
    }

    private void initializeViews() {
        detailWallpaperImage = findViewById(R.id.detailWallpaperImage);
        setWallpaperButton = findViewById(R.id.setWallpaperButton);
        downloadButton = findViewById(R.id.downloadButton);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void loadWallpaper() {
        Glide.with(this)
                .load(wallpaperUrl)
                .into(detailWallpaperImage);
    }

    private void setupClickListeners() {
        setWallpaperButton.setOnClickListener(v -> setWallpaper());
        downloadButton.setOnClickListener(v -> downloadWallpaper());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setWallpaper() {
        new Thread(() -> {
            try {
                URL url = new URL(wallpaperUrl);
                InputStream inputStream = url.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                wallpaperManager.setBitmap(bitmap);

                runOnUiThread(() -> Toast.makeText(this, "壁纸设置成功", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "壁纸设置失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void downloadWallpaper() {
        new Thread(() -> {
            try {
                URL url = new URL(wallpaperUrl);
                InputStream inputStream = url.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                String fileName = "wallpaper_" + wallpaperId + ".jpg";
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(directory, fileName);

                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();

                runOnUiThread(() -> Toast.makeText(this, "壁纸已保存到下载目录", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
} 