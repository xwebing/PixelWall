package com.pixelwall.pixelwall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

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
            categories.add(new Category("城市", "city"));
            categories.add(new Category("抽象", "abstract"));
            categories.add(new Category("动物", "animals"));
            categories.add(new Category("建筑", "architecture"));
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
                    int imageId = 1000 + (currentPage - 1) * 9 + i;
                    // 使用静态随机图片，seed为imageId
                    @SuppressLint("DefaultLocale") String imageUrl = String.format("https://picsum.photos/seed/%d/800/600", imageId);
                    newWallpapers.add(new Wallpaper(String.valueOf(imageId), imageUrl, 800, 600));
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