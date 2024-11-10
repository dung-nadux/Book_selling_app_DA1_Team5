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
    public static final int FRAGMENT_HOME = 0;
//    public static final int FRAGMENT_NEWS = 1;
//    public static final int FRAGMENT_CONTACTS = 2;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private int currentFragment = FRAGMENT_HOME;
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

        // cách 1 muốn hiển thị phần home khi vừa chạy
        repalceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            if (currentFragment != 0) {
                repalceFragment(new HomeFragment());
                currentFragment = 0;
            }
        } else if (id == R.id.nav_cart) {
            if (currentFragment != 1) {
                repalceFragment(new CartFragment());
                currentFragment = 1;
            }
        } else if (id == R.id.nav_order) {
            if (currentFragment != 2) {
                repalceFragment(new OrderFragment());
                currentFragment = 2;
            }
        }else if (id == R.id.nav_profile) {
            if (currentFragment != 3) {
                repalceFragment(new ProfileFragment());
                currentFragment = 3;
            }
        }else if (id == R.id.nav_profile) {
            if (currentFragment != 4) {
                repalceFragment(new UserFragment());
                currentFragment = 4;
            }
        }else if (id == R.id.nav_profile) {
            if (currentFragment != 5) {
                repalceFragment(new ProductFragment());
                currentFragment = 5;
            }
        }else if (id == R.id.nav_profile) {
            if (currentFragment != 6) {
                repalceFragment(new AnalytistFragment());
                currentFragment = 6;
            }
        }
        // Đóng ngăn kéo sau khi chọn mục
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void repalceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frContent, fragment);
        transaction.commit();
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