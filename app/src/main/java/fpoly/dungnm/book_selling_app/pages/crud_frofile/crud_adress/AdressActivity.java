package fpoly.dungnm.book_selling_app.pages.crud_frofile.crud_adress;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import fpoly.dungnm.book_selling_app.R;

public class AdressActivity extends AppCompatActivity {
    RecyclerView rcvAdress;
    ImageView imgAddAdress;

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

        imgAddAdress.setOnClickListener(v -> {
            dialogAddAdress();
        });
    }

    private void dialogAddAdress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_adress_add, null);
        builder.setView(view);

        EditText edEnterAdress = view.findViewById(R.id.edEnterAdress);
        RadioGroup rgAdress = view.findViewById(R.id.rgAdress);

        // Đặt "Nhà riêng" làm mặc định
        rgAdress.check(R.id.radio_button_home);

        builder.setPositiveButton("Thêm" , (dialog, which) -> {
            String enterAdress = edEnterAdress.getText().toString();
            int idAdress = rgAdress.getCheckedRadioButtonId();

            // Kiểm tra xem người dùng đã chọn RadioButton nào
            if (idAdress != -1) { // Nếu không có lựa chọn nào, idAdress sẽ là -1
                RadioButton selectedRadioButton = view.findViewById(idAdress); // Lấy RadioButton được chọn
                String selectedText = selectedRadioButton.getText().toString(); // Lấy text từ RadioButton
                // Xử lý text hoặc lưu địa chỉ
                System.out.println("Địa chỉ: " + enterAdress + ", Loại: " + selectedText);
            } else {
                System.out.println("Vui lòng chọn loại địa chỉ.");
            }
        });

        builder.setNegativeButton("Hủy" , (dialog, which) -> {
            dialog.dismiss();
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}