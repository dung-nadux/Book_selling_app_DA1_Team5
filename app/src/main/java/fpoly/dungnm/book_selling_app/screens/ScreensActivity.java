package fpoly.dungnm.book_selling_app.screens;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.screens.fragment.AnalytistFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.CartFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.HomeFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.OrderFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.ProductFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.ProfileFragment;
import fpoly.dungnm.book_selling_app.screens.fragment.UserFragment;

public class ScreensActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screens);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // Tắt hiển thị title

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frContent, new HomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_cart) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_order) {
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
        // Đóng ngăn kéo sau khi chọn mục
        getSupportFragmentManager().beginTransaction().replace(R.id.frContent, fragment).commit();
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