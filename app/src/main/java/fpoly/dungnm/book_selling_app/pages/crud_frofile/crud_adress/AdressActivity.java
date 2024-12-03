package fpoly.dungnm.book_selling_app.pages.crud_frofile.crud_adress;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.DAO.AddressDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.adapter.AdapterAdderss;
import fpoly.dungnm.book_selling_app.adapter.AdapterHomeProducts;
import fpoly.dungnm.book_selling_app.models.ModelAddres;

public class AdressActivity extends AppCompatActivity {
    RecyclerView rcvAdress;
    ImageView imgAddAdress;
    AddressDAO addressDAO;
    ArrayList<ModelAddres> list = new ArrayList<>();
    AdapterAdderss adapterAdderss ;
    ImageView imgBackProfile;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adress);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rcvAdress = findViewById(R.id.rcvAdress);
        imgAddAdress = findViewById(R.id.imgAddAdress);
        imgBackProfile = findViewById(R.id.imgBackProfile);

        SharedPreferences sharedPreferences = getSharedPreferences("CHECK_LOGIN", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);
        addressDAO = new AddressDAO(this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvAdress.setLayoutManager(manager);
        list = addressDAO.getAllAddresses(userId);
        adapterAdderss = new AdapterAdderss(this, list);
        rcvAdress.setAdapter(adapterAdderss);

        adapterAdderss.setOnItemClickListener(addres -> {
            Intent intent = new Intent();
            intent.putExtra("address", addres);
            Toast.makeText(this, "Đã chọn địa chỉ: "+addres.getAddress(), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, intent);
            finish();
        });

        imgAddAdress.setOnClickListener(v -> {
            dialogAddAdress();
        });
        imgBackProfile.setOnClickListener(v -> {
            finish();
        });
    }

    private void dialogAddAdress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_adress_add, null);
        builder.setView(view);

        EditText edEnterAdress = view.findViewById(R.id.edEnterAdress);
        EditText edFullName = view.findViewById(R.id.edFullName);
        EditText edPhone = view.findViewById(R.id.edPhone);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String enterAdress = edEnterAdress.getText().toString().trim();
            String fullName = edFullName.getText().toString().trim();
            String phoneStr = edPhone.getText().toString().trim();

            // Kiểm tra thông tin nhập
            if (enterAdress.isEmpty() || fullName.isEmpty() || phoneStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Chuyển đổi phone sang số nguyên
                int phone = Integer.parseInt(phoneStr);

                // Tạo đối tượng ModelAddres
                ModelAddres address = new ModelAddres(userId, fullName, phoneStr, enterAdress);

                // Thêm vào cơ sở dữ liệu
                boolean check = addressDAO.insertAddress(address);
                if (check) {
                    Toast.makeText(this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
                    list.clear();
                    list.addAll(addressDAO.getAllAddresses(userId));
                    adapterAdderss.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Thêm địa chỉ thất bại", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        Dialog dialog = builder.create();
        dialog.show();
    }

}