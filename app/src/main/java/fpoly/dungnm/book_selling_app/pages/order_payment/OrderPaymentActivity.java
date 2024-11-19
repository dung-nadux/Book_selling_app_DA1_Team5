package fpoly.dungnm.book_selling_app.pages.order_payment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.DAO.CartDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.adapter.AdapterCart;
import fpoly.dungnm.book_selling_app.adapter.AdapterOrderPayment;
import fpoly.dungnm.book_selling_app.models.ModelProducts;

public class OrderPaymentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterOrderPayment adapter;
    ArrayList<ModelProducts> listCart = new ArrayList<>();
    ImageView imgBackCart;
    CartDAO cartDAO;
//    TextView tvSelectedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.rcvOrderpayment);
        imgBackCart = findViewById(R.id.imgBackOderpayment);

// Nhận danh sách sản phẩm từ Intent
        ArrayList<ModelProducts> selectedProducts = getIntent().getParcelableArrayListExtra("selectedProducts");

        if (selectedProducts != null && !selectedProducts.isEmpty()) {
            // Thiết lập RecyclerView
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(manager);

            adapter = new AdapterOrderPayment(this, selectedProducts); // Sử dụng danh sách đã chọn
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Không có sản phẩm nào được chọn!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng màn hình nếu không có sản phẩm nào
        }

        imgBackCart.setOnClickListener(v -> {
            onBackPressed();
        });

    }

}
