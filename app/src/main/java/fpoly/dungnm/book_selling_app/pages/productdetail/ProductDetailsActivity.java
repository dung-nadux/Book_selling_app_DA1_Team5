package fpoly.dungnm.book_selling_app.pages.productdetail;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fpoly.dungnm.book_selling_app.R;

public class ProductDetailsActivity extends AppCompatActivity {
ImageView imgBack1, book_image;
TextView book_title,book_price,book_description, tvCategory, tvAuthor;
Button btn_add_to_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgBack1 = findViewById(R.id.imgBack1);
        book_image = findViewById(R.id.book_image);
        book_title = findViewById(R.id.book_title);
        book_price = findViewById(R.id.book_price);
        book_description = findViewById(R.id.book_description);
        tvCategory = findViewById(R.id.tvCategory);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        tvAuthor = findViewById(R.id.tvAuthor);

        // Nhận dữ liệu từ Intent
        String productId = getIntent().getStringExtra("productId");
        String productTitle = getIntent().getStringExtra("productTitle");
        String productAuthor = getIntent().getStringExtra("productAuthor");
        String productPrice = getIntent().getStringExtra("productPrice");
        String productDescription = getIntent().getStringExtra("productDescription");
        String productCategory = getIntent().getStringExtra("productCategory");
        int productImageResId = getIntent().getIntExtra("productImageResId", 0);

        // Hiển thị dữ liệu lên giao diện
        book_title.setText(productTitle);
        tvAuthor.setText("Tác giả: "+ productAuthor);
        book_price.setText(productPrice);
        book_description.setText("Mô tả: "+productDescription);
        tvCategory.setText("Thể loại: "+productCategory);
        book_image.setImageResource(productImageResId);

        imgBack1.setOnClickListener(v -> finish());
    }
}