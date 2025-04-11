package com.pixelwall.pixelwall;

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

import androidx.appcompat.app.AlertDialog;
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

        // 设置Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回箭头
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // 设置返回箭头点击事件
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 获取传递的数据
        wallpaperUrl = getIntent().getStringExtra("wallpaper_url");
        wallpaperId = getIntent().getStringExtra("wallpaper_id");

        // 加载图片
        detailWallpaperImage = findViewById(R.id.wallpaperImage);
        Glide.with(this)
                .load(wallpaperUrl)
                .placeholder(R.drawable.placeholder) // 占位图
                .error(R.drawable.error) // 错误图
                .into(detailWallpaperImage);

        // 设置壁纸按钮
        setWallpaperButton = findViewById(R.id.setWallpaperButton);
        setWallpaperButton.setOnClickListener(v -> setWallpaper(wallpaperUrl));

        // 下载壁纸按钮
        downloadButton = findViewById(R.id.downloadWallpaperButton);
        downloadButton.setOnClickListener(v -> downloadWallpaper(wallpaperUrl));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setWallpaper(String imageUrl) {
        new Thread(() -> {
            try {
                // 下载图片并设置为壁纸
                Bitmap bitmap = Glide.with(this)
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get();

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                wallpaperManager.setBitmap(bitmap);

                runOnUiThread(() -> Toast.makeText(this, "壁纸设置成功", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "壁纸设置失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void downloadWallpaper(String imageUrl) {
        new AlertDialog.Builder(this)
                .setTitle("下载壁纸")
                .setMessage("是否下载此壁纸？")
                .setPositiveButton("是", (dialog, which) -> {
                    new Thread(() -> {
                        try {
                            // 下载图片
                            Bitmap bitmap = Glide.with(this)
                                    .asBitmap()
                                    .load(imageUrl)
                                    .submit()
                                    .get();

                            // 保存图片到指定目录
                            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Wallpapers");
                            if (!directory.exists()) {
                                directory.mkdirs();
                            }

                            File file = new File(directory, "wallpaper_" + System.currentTimeMillis() + ".jpg");
                            try (FileOutputStream out = new FileOutputStream(file)) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            }

                            runOnUiThread(() -> Toast.makeText(this, "壁纸下载成功：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show());
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(this, "壁纸下载失败", Toast.LENGTH_SHORT).show());
                        }
                    }).start();
                })
                .setNegativeButton("否", null)
                .show();
    }
} 