package com.pixelwall.pixelwall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pixelwall.pixelwall.adapter.CategoryAdapter;
import com.pixelwall.pixelwall.adapter.WallpaperAdapter;
import com.pixelwall.pixelwall.model.Category;
import com.pixelwall.pixelwall.model.Wallpaper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener, WallpaperAdapter.OnWallpaperClickListener {
    private RecyclerView categoryRecyclerView;
    private RecyclerView wallpaperRecyclerView;
    private CategoryAdapter categoryAdapter;
    private WallpaperAdapter wallpaperAdapter;
    private List<Category> categories;
    private int currentPage = 1;
    private boolean isLoading = false;
    private String currentCategoryId = "nature";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            initializeViews();
            setupToolbar();
            setupRecyclerViews();
            loadCategories();
            loadWallpapers();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "应用初始化失败" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        wallpaperRecyclerView = findViewById(R.id.wallpaperRecyclerView);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    private void setupRecyclerViews() {
        try {
            categoryAdapter = new CategoryAdapter(this);
            wallpaperAdapter = new WallpaperAdapter(this);

            categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            categoryRecyclerView.setAdapter(categoryAdapter);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            wallpaperRecyclerView.setLayoutManager(gridLayoutManager);
            wallpaperRecyclerView.setAdapter(wallpaperAdapter);

            wallpaperRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0) {
                            loadMoreWallpapers();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "列表初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCategories() {
        try {
            categories = new ArrayList<>();
            categories.add(new Category("自然", "nature"));
            categories.add(new Category("旅行", "travel"));
            categories.add(new Category("电影", "film"));
            categories.add(new Category("人物", "people"));
            categories.add(new Category("动物", "animals"));
            categories.add(new Category("时尚", "fashion-beauty"));
            categories.add(new Category("食品饮料", "food-drink"));
            categories.add(new Category("纹理图案", "textures-patterns"));
            categoryAdapter.setCategories(categories);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "类别加载失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadWallpapers() {
        try {
            currentPage = 1;
            wallpaperAdapter.setWallpapers(new ArrayList<>());
            loadMoreWallpapers();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "壁纸加载失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMoreWallpapers() {
        if (isLoading) return; // 如果正在加载，则直接返回
        isLoading = true;

        new Thread(() -> {
            try {
                List<Wallpaper> newWallpapers = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    // 调用Unsplash API获取随机图片
//                    String apiUrl = "https://api.unsplash.com/photos?client_id=hkzmgmjnKzYBWh2e5NC4WfijMDIwEYZulyF-E6DSk7s&query=" + currentCategoryId;
//                    URL url = new URL(apiUrl);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setRequestProperty("Authorization", "Client-ID " + "hkzmgmjnKzYBWh2e5NC4WfijMDIwEYZulyF-E6DSk7s");
//                    connection.connect();
//
//                    int responseCode = connection.getResponseCode();
//                    Log.d("MainActivity111", "API Response: " + responseCode);
//
//
//                    InputStream inputStream = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    StringBuilder response = new StringBuilder();
//
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    reader.close();
//                    inputStream.close();
//
//                    // 解析JSON响应
//                    JSONObject jsonObject = new JSONObject(response.toString());
//                    String imageUrl = jsonObject.getJSONObject("urls").getString("regular");
//                    String imageId = jsonObject.getString("id");
//                    Log.d("MainActivity", "API Response: " + response.toString());

                //    newWallpapers.add(new Wallpaper(imageId, imageUrl, 800, 600));
                    newWallpapers.add(new Wallpaper(
                        "id_" + i,
                        "https://picsum.photos/id/"+(1000 + i) + "/640/1136",
                        800,
                        600
                    ));
                    
                }

                runOnUiThread(() -> {
                    wallpaperAdapter.addWallpapers(newWallpapers);
                    currentPage++;
                    isLoading = false;
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "加载更多壁纸失败", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                });
            }
        }).start();
    }

    @Override
    public void onCategoryClick(Category category, int position) {
        try {
            currentCategoryId = category.getId();
            currentPage = 1;
            wallpaperAdapter.setWallpapers(new ArrayList<>());
            loadMoreWallpapers();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "切换类别失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWallpaperClick(Wallpaper wallpaper, int position) {
        try {
            Intent intent = new Intent(this, WallpaperDetailActivity.class);
            intent.putExtra("wallpaper_url", wallpaper.getImageUrl());
            intent.putExtra("wallpaper_id", wallpaper.getId());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "打开详情失败", Toast.LENGTH_SHORT).show();
        }
    }
} 