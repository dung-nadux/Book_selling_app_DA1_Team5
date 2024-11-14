package fpoly.dungnm.book_selling_app.pages.productdetail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fpoly.dungnm.book_selling_app.DAO.ProductDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.models.ModelProducts;

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
//        String productId = getIntent().getStringExtra("productId");
//        String productTitle = getIntent().getStringExtra("productTitle");
//        String productAuthor = getIntent().getStringExtra("productAuthor");
//        String productPrice = getIntent().getStringExtra("productPrice");
//        String productDescription = getIntent().getStringExtra("productDescription");
//        String productCategory = getIntent().getStringExtra("productCategory");
//        String productImageString = getIntent().getStringExtra("productImage");

        // Chuyển đổi chuỗi thành mảng byte
//        byte[] productImageBytes = null;
//        if (productImageString != null) {
//            String[] byteStrings = productImageString.replaceAll("[\\[\\] ]", "").split(","); // Xóa dấu ngoặc và khoảng trắng
//            productImageBytes = new byte[byteStrings.length];
//            for (int i = 0; i < byteStrings.length; i++) {
//                productImageBytes[i] = Byte.parseByte(byteStrings[i]);
//            }
//        }
//
//        // Hiển thị dữ liệu lên giao diện
//        book_title.setText(productTitle);
//        tvAuthor.setText("Tác giả: " + productAuthor);
//        book_price.setText(productPrice);
//        book_description.setText("Mô tả: " + productDescription);
//        tvCategory.setText("Thể loại: " + productCategory);
//
//        // Hiển thị hình ảnh nếu có
//        if (productImageBytes != null && productImageBytes.length > 0) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(productImageBytes, 0, productImageBytes.length);
//            book_image.setImageBitmap(bitmap);
//        } else {
//            book_image.setImageResource(R.drawable.ic_launcher_background); // Hình ảnh mặc định nếu không có
//        }

        imgBack1.setOnClickListener(v -> finish());

        // Nhận productId từ Intent
        int productId = getIntent().getIntExtra("productId", -1);
        Toast.makeText(this, "productId: " + productId, Toast.LENGTH_SHORT).show();

        // Kiểm tra productId và lấy dữ liệu từ CSDL
        if (productId != -1) {
            ProductDAO productDAO = new ProductDAO(this);
            ModelProducts product = productDAO.getProductById(productId);

            if (product != null) {
                // Hiển thị dữ liệu lên giao diện
                book_title.setText(product.getTitle());
                tvAuthor.setText("Tác giả: " + product.getAuthor());
                book_price.setText(String.valueOf(product.getPrice()));
                book_description.setText("Mô tả: " + product.getDescription());
                tvCategory.setText("Thể loại: " + product.getCategory());

                // Hiển thị hình ảnh nếu có
                byte[] productImageBytes = product.getImage();
                if (productImageBytes != null && productImageBytes.length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(productImageBytes, 0, productImageBytes.length);
                    book_image.setImageBitmap(bitmap);
                } else {
                    book_image.setImageResource(R.drawable.ic_launcher_background); // Hình ảnh mặc định nếu không có
                }
            }
        }
    }
}