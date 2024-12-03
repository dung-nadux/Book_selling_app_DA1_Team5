package fpoly.dungnm.book_selling_app.pages.crud_frofile.crud_voucher_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.DAO.VoucherDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.adapter.AdapterVoucher;
import fpoly.dungnm.book_selling_app.models.ModelVoucher;

public class VoucherActivity extends AppCompatActivity {

    private ImageView imgBackVoucher;
    private RecyclerView rv_discount_voucher, rv_shipping_voucher;
    private Button btn_add_voucher;
    private ArrayList<ModelVoucher> listDiscount, listShipping = new ArrayList<>();
    private AdapterVoucher adapterDiscount, adapterShipping;
    private VoucherDAO voucherDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voucher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgBackVoucher = findViewById(R.id.imgBackVoucher);
        rv_discount_voucher = findViewById(R.id.rv_discount_voucher);
        rv_shipping_voucher = findViewById(R.id.rv_shipping_voucher);
        btn_add_voucher = findViewById(R.id.btn_add_voucher);

        voucherDAO = new VoucherDAO(this);

        listDiscount = voucherDAO.getVoucherByType("discount");
        listShipping = voucherDAO.getVoucherByType("ship");
        LinearLayoutManager managerDiscount = new LinearLayoutManager(VoucherActivity.this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager managerShipping = new LinearLayoutManager(VoucherActivity.this, LinearLayoutManager.VERTICAL, false);


        rv_discount_voucher.setLayoutManager(managerDiscount);
        rv_shipping_voucher.setLayoutManager(managerShipping);
        adapterDiscount = new AdapterVoucher(this, listDiscount);
        adapterShipping = new AdapterVoucher(this, listShipping);
        rv_discount_voucher.setAdapter(adapterDiscount);
        rv_shipping_voucher.setAdapter(adapterShipping);

        Intent resultIntent = new Intent();
        adapterDiscount.setOnItemClickListener(modelVoucher -> {
            resultIntent.putExtra("selectedVoucherDiscount", modelVoucher);
            setResult(RESULT_OK, resultIntent);
        });

        adapterShipping.setOnItemClickListener(modelVoucher -> {
            resultIntent.putExtra("selectedVoucherShipping", modelVoucher);
            setResult(RESULT_OK, resultIntent);
        });

        btn_add_voucher.setOnClickListener(v -> {
            if (resultIntent.hasExtra("selectedVoucherDiscount") || resultIntent.hasExtra("selectedVoucherShipping")) {
                setResult(RESULT_OK, resultIntent); // Trả dữ liệu về
                finish(); // Đóng activity
            } else {
                Toast.makeText(this, "Vui lòng chọn ít nhất một voucher!", Toast.LENGTH_SHORT).show();
            }
        });


        imgBackVoucher.setOnClickListener(v -> {
            finish();
        });
    }
}