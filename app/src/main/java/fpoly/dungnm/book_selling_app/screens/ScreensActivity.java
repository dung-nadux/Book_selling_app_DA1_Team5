package fpoly.dungnm.book_selling_app.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.login.LoginActivity;
import fpoly.dungnm.book_selling_app.pages.search.SearchActivity;
import fpoly.dungnm.book_selling_app.screens.fragment.AnalytistFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.CartFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.HomeFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.OrderFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.ProductFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.ProfileFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.SettingsFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.UserFragment;

public class ScreensActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageView imgSearch;
    EditText edSearchClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screens);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        imgSearch = findViewById(R.id.imgSearch);
        edSearchClick = findViewById(R.id.edSearchClick);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);  // Tắt hiển thị title

        // phân quyền
        Intent intent = getIntent();
        String role = intent.getStringExtra("role");

        if ("0".equals(role)) {
            navigationView.getMenu().findItem(R.id.nav_product).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_alalytics).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_user).setVisible(false);
        }else {
            navigationView.getMenu().findItem(R.id.nav_product).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_alalytics).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_user).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_cart).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_order).setVisible(false);
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Đặt sự kiện cho NavigationView
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frContent, new HomeFragment()).commit();
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frContent);
            if (currentFragment instanceof HomeFragment ) {
                toolbar.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        });

        edSearchClick.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = null;
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            imgSearch.setVisibility(View.VISIBLE); // Show SearchView
        } else {
            // Other fragments
            if(id ==R.id.nav_cart){
                fragment = new CartFragment();
            }
            else if (id == R.id.nav_order) {
                fragment = new OrderFragment();
            } else if (id == R.id.nav_profile) {
                toolbar.setVisibility(View.GONE);
                fragment = new ProfileFragment();
            } else if (id == R.id.nav_user) {
                fragment = new UserFragment();
            } else if (id == R.id.nav_product) {
                fragment = new ProductFragment();
            } else if (id == R.id.nav_alalytics) {
                fragment = new AnalytistFragment();
            }else if (id == R.id.nav_setting) {
                toolbar.setVisibility(View.VISIBLE);
                fragment = new SettingsFragment();
            }
            else if (id == R.id.nav_logout) {
                startActivity(new Intent(this, LoginActivity.class));
            }
            imgSearch.setVisibility(View.GONE); // Hide SearchView

        }

        // Đóng ngăn kéo sau khi chọn mục
        transaction.replace(R.id.frContent, fragment);
        transaction.addToBackStack("HomeFragment");
        transaction.commit();
        drawerLayout.closeDrawers();
        return true;
    }


     @Override
     public void onBackPressed() {
         if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
             drawerLayout.closeDrawer(GravityCompat.START);
         } else {
             super.onBackPressed();
         }
     }

}