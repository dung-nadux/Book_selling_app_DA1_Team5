package fpoly.dungnm.book_selling_app.pages.order_payment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpoly.dungnm.book_selling_app.DAO.CartDAO;
import fpoly.dungnm.book_selling_app.DAO.OrderDAO;
import fpoly.dungnm.book_selling_app.DAO.OrderDetailDAO;
import fpoly.dungnm.book_selling_app.DAO.WalletDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.adapter.AdapterAdderss;
import fpoly.dungnm.book_selling_app.adapter.AdapterCart;
import fpoly.dungnm.book_selling_app.adapter.AdapterOrderPayment;
import fpoly.dungnm.book_selling_app.models.ModelAddres;
import fpoly.dungnm.book_selling_app.models.ModelCart;
import fpoly.dungnm.book_selling_app.models.ModelOrder;
import fpoly.dungnm.book_selling_app.models.ModelOrderDetail;
import fpoly.dungnm.book_selling_app.models.ModelProducts;
import fpoly.dungnm.book_selling_app.models.ModelVoucher;
import fpoly.dungnm.book_selling_app.models.ModelWallet;
import fpoly.dungnm.book_selling_app.pages.crud_frofile.crud_adress.AdressActivity;
import fpoly.dungnm.book_selling_app.pages.crud_frofile.crud_voucher_user.VoucherActivity;
import fpoly.dungnm.book_selling_app.screens.ScreensActivity;

public class OrderPaymentActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_ADDRESS = 100;
    private static final int REQUEST_SELECT_VOUCHER = 101;
    RecyclerView recyclerView;
    AdapterOrderPayment adapter;
    ArrayList<ModelOrderDetail> listOrderDetails = new ArrayList<>();
    ImageView imgBackCart;
    CartDAO cartDAO;
    private OrderDAO orderDAO;
    private ModelOrder order;
    private ModelWallet wallet;
    private OrderDetailDAO orderDetailDAO;
    private WalletDAO walletDAO;
    private int userId;
    private double totalPrice;
    private NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
    private ModelAddres modelAddres;
    RelativeLayout rlAdress;
    TextView tvAddressText, tvWalletBalance, tv_voucher_discount, tv_voucher_shipping, tvMoneyProduct, tvMoneyShip, tvVoucherPrice, tvTotalMoney;
    private CardView chosse_voucher;
    private ModelVoucher voucherDiscount, voucherShipping;
    private RadioGroup radioGroupPayment;
    private RadioButton radioEWallet, radioCashOnDelivery;
    private Button btnPayOrderProduct;

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
        anhXa();
        SharedPreferences sharedPreferences = getSharedPreferences("CHECK_LOGIN", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        orderDAO = new OrderDAO(OrderPaymentActivity.this);
        orderDetailDAO = new OrderDetailDAO(OrderPaymentActivity.this);
        cartDAO = new CartDAO(OrderPaymentActivity.this);
        walletDAO = new WalletDAO(OrderPaymentActivity.this);

        order = new ModelOrder();
        order.setUserId(userId);
        order.setDiscountVoucherID(-1);
        order.setShippingVoucherID(-1);
        order.setStatus("Chờ xác nhận");
        order.setTotalPrice(0);
        order = orderDAO.insert(order);
        if (order != null) {
            orderDetailDAO.insertFromCart(userId, order.getId());
            listOrderDetails = orderDetailDAO.getAllOrderDetailByOrderID(order.getId());
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new AdapterOrderPayment(this, listOrderDetails); // Sử dụng danh sách đã chọn
        recyclerView.setAdapter(adapter);

        tinhTong();
        adapter.setOnChangeTotalPriceListener(price -> {
            totalPrice = price;
            tinhTong();
        });

        handlePaymentSelection();


        imgBackCart.setOnClickListener(v -> {
            finish();
        });

        rlAdress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdressActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_ADDRESS);
        });

        chosse_voucher.setOnClickListener(v -> {
            Intent intent = new Intent(this, VoucherActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_VOUCHER);
        });


        btnPayOrderProduct.setOnClickListener(v -> {
            handleOrderPayment();

        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (orderDAO.deleteOrder(order.getId())) {
                    Toast.makeText(OrderPaymentActivity.this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(OrderPaymentActivity.this, "Hủy đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void anhXa() {
        recyclerView = findViewById(R.id.rcvOrderpayment);
        imgBackCart = findViewById(R.id.imgBackOderpayment);
        rlAdress = findViewById(R.id.rlAdress);
        tvAddressText = findViewById(R.id.tvAddressText);
        chosse_voucher = findViewById(R.id.chosse_voucher);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        radioCashOnDelivery = findViewById(R.id.radioCashOnDelivery);
        radioEWallet = findViewById(R.id.radioEWallet);
        tvWalletBalance = findViewById(R.id.tvWalletBalance);
        btnPayOrderProduct = findViewById(R.id.btnPayOrderProduct);
        tv_voucher_discount = findViewById(R.id.tv_voucher_discount);
        tv_voucher_shipping = findViewById(R.id.tv_voucher_shipping);
        tvMoneyProduct = findViewById(R.id.tvMoneyProduct);
        tvMoneyShip = findViewById(R.id.tvMoneyShip);
        tvVoucherPrice = findViewById(R.id.tvVoucherPrice);
        tvTotalMoney = findViewById(R.id.tvTotalMoney);
    }


    private void handlePaymentSelection() {
        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioEWallet) {
                wallet = walletDAO.getWalletByUserId(userId);
                tvWalletBalance.setText("Số dư ví: " + formatter.format(wallet.getBalance()) + "đ");
                tvWalletBalance.setVisibility(View.VISIBLE);
            } else {
                tvWalletBalance.setVisibility(View.GONE);
            }
        });
    }

    private void tinhTong() {
        totalPrice = 0;
        double totalProduct = 0;
        for (ModelOrderDetail detail : listOrderDetails) {
            totalProduct += detail.getUnitPrice()* detail.getQuantity();
        }
        double shippingFee = 30000;
        double discount = 0;
        double shipping = 0;
        if (voucherDiscount != null) {
            discount =  totalProduct * (voucherDiscount.getDiscount()/100.0);
            Log.e("jjjjjj", "tinhTong: "+discount);
        }
        if (voucherShipping != null) {
            shipping = shippingFee * (voucherShipping.getDiscount()/100.0);
        }
        totalPrice = totalProduct + shippingFee - (discount + shipping);
        tvMoneyProduct.setText(formatter.format(totalProduct)+"đ");
        tvMoneyShip.setText(formatter.format(shippingFee) + "đ");
        tvVoucherPrice.setText("-" + formatter.format(discount + shipping) + "đ");
        tvTotalMoney.setText(formatter.format(totalPrice) + "đ");
    }

    private void handleOrderPayment() {
        btnPayOrderProduct.setOnClickListener(v -> {
            if (radioEWallet.isChecked()) {
                double currentWalletBalance = wallet.getBalance();

                if (currentWalletBalance < totalPrice) {
                    Toast.makeText(this, "Số dư ví không đủ!", Toast.LENGTH_LONG).show();
                } else {
                    double newBalance = currentWalletBalance - totalPrice;

                    ModelWallet wallet = new ModelWallet();
                    wallet.setUserId(userId);
                    wallet.setBalance(newBalance);

                    // Cập nhật số dư mới vào cơ sở dữ liệu
                    if (walletDAO.updateWallet(wallet)) {
                        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

//                        saveOrder();
                        Intent intent = new Intent(this, ScreensActivity.class);
                        intent.putExtra("role", "0"); // Truyền thông tin role nếu cần thiết
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
                    }


                }
            } else if (radioCashOnDelivery.isChecked()) {
                Toast.makeText(this, "Thanh toán khi nhận hàng!", Toast.LENGTH_SHORT).show();

//                saveOrder();
                Intent intent = new Intent(this, ScreensActivity.class);
                intent.putExtra("role", "0"); // Truyền thông tin role nếu cần thiết
                startActivity(intent);
            }
        });
    }

    private void saveOrder() {
        order.setDiscountVoucherID(voucherDiscount!=null?voucherDiscount.getId():0);
        order.setShippingVoucherID(voucherShipping!=null?voucherShipping.getId():0);
        order.setTotalPrice(totalPrice);
        if (orderDAO.updateOrder(order)) {
            Toast.makeText(this, "Đơn hàng đã được lưu!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lưu đơn hàng thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_ADDRESS && resultCode == RESULT_OK && data != null) {
            modelAddres = (ModelAddres) data.getSerializableExtra("address");
            tvAddressText.setText("Họ và tên: " + modelAddres.getFullName() + "\nSĐT: " + modelAddres.getPhone() + "\nĐịa chỉ: " + modelAddres.getAddress());
        } else if (requestCode == REQUEST_SELECT_VOUCHER && resultCode == RESULT_OK && data != null) {
            voucherDiscount = (ModelVoucher) data.getSerializableExtra("selectedVoucherDiscount");
            voucherShipping = (ModelVoucher) data.getSerializableExtra("selectedVoucherShipping");
            if (voucherDiscount != null) {
                Toast.makeText(this, "Đã chọn voucher", Toast.LENGTH_SHORT).show();
                tv_voucher_discount.setVisibility(View.VISIBLE);
                tv_voucher_discount.setText("Giảm giá: "+voucherDiscount.getDiscount());
                tinhTong();
            }
            if (voucherShipping != null) {
                tv_voucher_shipping.setVisibility(View.VISIBLE);
                tv_voucher_shipping.setText("Giảm phí vận chuyển: "+voucherShipping.getDiscount());
                tinhTong();
            }
        }
    }

}
