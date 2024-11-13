package fpoly.dungnm.book_selling_app.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.pages.search.SearchActivity;
import fpoly.dungnm.book_selling_app.screens.fragment.AnalytistFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.HomeFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.OrderFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.ProductFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.ProfileFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.UserFragment;

public class ScreensActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screens);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        imgSearch = findViewById(R.id.imgSearch);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // Tắt hiển thị title

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frContent, new HomeFragment()).commit();

        imgSearch.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });
        // Thêm listener cho SearchView
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Xử lý khi người dùng nhấn tìm kiếm
//                // return false;
//
//                  // Mở SearchResultsActivity khi nhấn tìm kiếm
//                Intent intent = new Intent(ScreensActivity.this, SearchActivity.class);
//                intent.putExtra("search_query", query); // Truyền query tìm kiếm
//                startActivity(intent);
//                return true; // Trả về true để chỉ ra rằng sự kiện đã được xử lý
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Gọi phương thức tìm kiếm trong HomeFragment
//                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frContent);
//                if (fragment instanceof HomeFragment) {
//                    ((HomeFragment) fragment).filter(newText); // Gọi phương thức filter trong HomeFragment
//                }
//                return true;
//            }
//        });

        // Thêm listener cho biểu tượng tìm kiếm

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            imgSearch.setVisibility(View.VISIBLE); // Show SearchView
        } else {
            // Other fragments
            if (id == R.id.nav_order) {
                fragment = new OrderFragment();
            } else if (id == R.id.nav_profile) {
                fragment = new ProfileFragment();
            } else if (id == R.id.nav_user) {
                fragment = new UserFragment();
            } else if (id == R.id.nav_product) {
                fragment = new ProductFragment();
            } else if (id == R.id.nav_alalytics) {
                fragment = new AnalytistFragment();
            }
            imgSearch.setVisibility(View.GONE); // Hide SearchView
        }

        // Đóng ngăn kéo sau khi chọn mục
        getSupportFragmentManager().beginTransaction().replace(R.id.frContent, fragment).commit();
        drawerLayout.closeDrawers();
        return true;
    }


    // @Override
    // public void onBackPressed() {
    //     if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
    //         drawerLayout.closeDrawer(GravityCompat.START);
    //     } else {
    //         super.onBackPressed();
    //     }
    // }

}