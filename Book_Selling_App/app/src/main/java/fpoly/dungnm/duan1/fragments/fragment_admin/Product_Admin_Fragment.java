package fpoly.dungnm.duan1.fragments.fragment_admin;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.ProductAdminAdapter;
import fpoly.dungnm.duan1.fragments.ProfileFragment;
import fpoly.dungnm.duan1.models.Product;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Product_Admin_Fragment extends Fragment {

    private ImageView btnProfileAdmin, btnAddProduct;
    private TextInputEditText edtimage_product, edtname_product, edtauthor_product, edtdescription_product, edtprice_product, edtstock_product;
    private RadioButton rdLife, rdStory, rdAcademic, rdConHang, rdHetHang, rdNgungBan;
    private LinearLayout radio_status_product;
    private RadioGroup rdgStatus;
    private TextView btnCancel, btnAdd;
    private RecyclerView rvProductAdmin;
    private ArrayList<Product> listProduct = new ArrayList<>();
    private ProductAdminAdapter adapter;
    private  HttpRequest request;

    public Product_Admin_Fragment() {
        // Required empty public constructor
    }

    public static Product_Admin_Fragment newInstance() {
        Product_Admin_Fragment fragment = new Product_Admin_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lắng nghe sự kiện nút Back
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog(); // Hiển thị hộp thoại xác nhận
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product__admin_, container, false);

        anhXa(view);
        request = new HttpRequest();

        loadData();
        adapter = new ProductAdminAdapter(getActivity(), listProduct);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvProductAdmin.setLayoutManager(manager);
        rvProductAdmin.setAdapter(adapter);
        
        btnAddProduct.setOnClickListener(v -> {
            dialogAddProduct();
        });
        adapter.setOnItemClickListener(new ProductAdminAdapter.OnItemClickListener() { // Gọi hàm onItemClick khi item được click
            @Override
            public void onItemClick(Product product) {
                dialogUpdateProduct(product);
            }

            @Override
            public void onItemLongClick(Product product) {
                dialogDeleteProduct(product);
            }
        });

        btnProfileAdmin.setOnClickListener(new View.OnClickListener() { // Chuyển sang ProfileFragment
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_admin, profileFragment) // Thay thế Fragment hiện tại
                        .addToBackStack(null) // Lưu lại trạng thái của HomeFragment
                        .commit();
            }
        });

        return view;
    }

    // Hàm hiển thị dialog xóa sản phẩm
    private void dialogDeleteProduct(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            request.callAPI().deleteProduct(product.getId()).enqueue(new Callback<ResponeData<Product>>() {
                @Override
                public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            listProduct.remove(listProduct.indexOf(product));
                            adapter.notifyItemRemoved(listProduct.indexOf(product));
                            Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else if (response.body().getStatus() == 4044) {
                            Toast.makeText(getActivity(), "Lỗi deleteProduct", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Lỗi kết nối deleteProduct", Toast.LENGTH_SHORT).show();
                        Log.e("er", response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Lỗi kết nối deleteProduct", Toast.LENGTH_SHORT).show();
                    Log.e("er", t.getMessage());
                }
            });
            dialog.dismiss();
        });
        builder.setNegativeButton("Không", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Hàm hiển thị dialog cập nhật sản phẩm
    private void dialogUpdateProduct(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.layout_dialog_add_product, null);
        builder.setView(view);
        anhXaDialog(view);
        AlertDialog dialog = builder.create();
        Toast.makeText(getContext(), "index"+listProduct.indexOf(product), Toast.LENGTH_SHORT).show();

        edtname_product.setText(product.getProductname());
        edtimage_product.setText(product.getImage());
        edtauthor_product.setText(product.getAuthor());
        edtdescription_product.setText(product.getDescription());
        edtprice_product.setText(String.valueOf((int) product.getPrice()));
        edtstock_product.setText(String.valueOf(product.getStock()));
        btnAdd.setText("Cập nhật");
        if (product.getCateID().equals("Đời sống")) {
            rdLife.setChecked(true);
        } else if (product.getCateID().equals("Truyện")) {
            rdStory.setChecked(true);
        } else if (product.getCateID().equals("Học thuật")) {
            rdAcademic.setChecked(true);
        }
        if (product.getStatus().equals("Còn hàng")) {
            rdConHang.setChecked(true);
        } else if (product.getStatus().equals("Hết hàng")) {
            rdHetHang.setChecked(true);
        } else if (product.getStatus().equals("Ngừng bán")) {
            rdNgungBan.setChecked(true);
        }

        //Nếu thay đổi status là nggừng bán hoặc hết hàng thì sẽ đưa số lượng về 0 và không cho nhập
        rdgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdNgungBan) {
                    edtstock_product.setText("0");
                    edtstock_product.setEnabled(false);
                } else if (checkedId == R.id.rdHetHang) {
                    edtstock_product.setText("0");
                    edtstock_product.setEnabled(false);
                } else {
                    edtstock_product.setText(product.getStock() + "");
                    edtstock_product.setEnabled(true);
                }
            }
        });

        btnAdd.setOnClickListener(v -> {
            updateProduct(dialog, product);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void updateProduct(AlertDialog dialog, Product product) {
        String image = edtimage_product.getText().toString();
        String name = edtname_product.getText().toString();
        String author = edtauthor_product.getText().toString();
        String description = edtdescription_product.getText().toString();
        String price = edtprice_product.getText().toString();
        String stock = edtstock_product.getText().toString();
        if (rdLife.isChecked()) {
            product.setCateID("Đời sống");
        } else if (rdStory.isChecked()) {
            product.setCateID("Truyện");
        } else if (rdAcademic.isChecked()) {
            product.setCateID("Học thuật");
        }
        if (rdConHang.isChecked()) {
            product.setStatus("Còn hàng");
        } else if (rdHetHang.isChecked()) {
            product.setStatus("Hết hàng");
        } else if (rdNgungBan.isChecked()) {
            product.setStatus("Ngừng bán");
        }

        if (validate(name, image, author, description, price, stock)) {
            int priceInt = Integer.parseInt(price);
            int stockInt = Integer.parseInt(stock);
            product.setProductname(name);
            product.setImage(image);
            product.setAuthor(author);
            product.setDescription(description);
            product.setPrice(priceInt);
            product.setStock(stockInt);

            request.callAPI().updateProduct(product.getId(), product).enqueue(new Callback<ResponeData<Product>>() {
                @Override
                public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Product updateProduct = response.body().getData();
                            listProduct.set(listProduct.indexOf(product), updateProduct);
                            adapter.notifyItemChanged(listProduct.indexOf(product));
                            Toast.makeText(getActivity(), "Update thành công", Toast.LENGTH_SHORT).show();
                            //loadData();
                            dialog.dismiss();
                        } else if (response.body().getStatus() == 404) {
                            Toast.makeText(getActivity(), "Lỗi updateProduct", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Lỗi kết nối udateProduct", Toast.LENGTH_SHORT).show();
                        Log.e("er", response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Lỗi kết nối updateProduct", Toast.LENGTH_SHORT).show();
                    Log.e("er", t.getMessage());
                }
            });
        }

    }

    // Hàm hiển thị dialog thêm sản phẩm
    private void dialogAddProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.layout_dialog_add_product, null);
        builder.setView(view);
        anhXaDialog(view);
        AlertDialog dialog = builder.create();

        radio_status_product.setVisibility(View.GONE);

        btnAdd.setOnClickListener(v -> {
            addProduct(dialog);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void addProduct(AlertDialog dialog) {
        String image = edtimage_product.getText().toString();
        String name = edtname_product.getText().toString();
        String author = edtauthor_product.getText().toString();
        String description = edtdescription_product.getText().toString();
        String price = edtprice_product.getText().toString();
        String stock = edtstock_product.getText().toString();
        String type = "";
        if (rdLife.isChecked()) {
            type = "Đời sống";
        } else if (rdStory.isChecked()) {
            type = "Truyện";
        } else if (rdAcademic.isChecked()) {
            type = "Học thuật";
        }
        String status = "Còn hàng";
        if (validate(name, image, author, description, price, stock)) {
            int priceInt = Integer.parseInt(price);
            int stockInt = Integer.parseInt(stock);
            Product product = new Product(name, image, author, description, priceInt, stockInt, status, type);

            request.callAPI().addProduct(product).enqueue(new Callback<ResponeData<Product>>() {
                @Override
                public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                            Product newProduct = response.body().getData(); // Lấy sản phẩm vừa thêm
                            listProduct.add(0, newProduct); // Thêm vào danh sách
                            adapter.notifyItemInserted(0); // Thông báo RecyclerView
                            //loadData();
                            dialog.dismiss();
                        } else if (response.body().getStatus() == 400) {
                            Toast.makeText(getActivity(), "Lỗi addProduct", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Lỗi kết nối addProduct", Toast.LENGTH_SHORT).show();
                        Log.e("er", response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Lỗi kết nối addProduct", Toast.LENGTH_SHORT).show();
                    Log.e("er", t.getMessage());
                }
            });
        }

    }

    // Hàm validate dữ liệu
    private boolean validate(String name, String image, String author, String description, String price, String stock) {
        if (name.isEmpty()) {
            edtimage_product.setError("Tên không được để trống");
            edtimage_product.requestFocus();
            return false;
        }
        if (image.isEmpty()) {
            edtimage_product.setError("Ảnh không được để trống");
            edtimage_product.requestFocus();
            return false;
        }
        if (author.isEmpty()) {
            edtauthor_product.setError("Tác giả không được để trống");
            edtauthor_product.requestFocus();
            return false;
        }
        if (description.isEmpty()) {
            edtdescription_product.setError("Mô tả không được để trống");
            edtdescription_product.requestFocus();
            return false;
        }
        if (price.isEmpty()) {
            edtprice_product.setError("Giá không được để trống");
            edtprice_product.requestFocus();
            return false;
        } else {
            try {
                Integer.parseInt(price);
            } catch (NumberFormatException e) {
                edtprice_product.setError("Giá phải là số");
                edtprice_product.requestFocus();
                return false;
            }
        }
        if (Integer.parseInt(price) <= 0) {
            edtprice_product.setError("Giá phải lớn hơn 0");
            edtprice_product.requestFocus();
            return false;
        }
        if (stock.isEmpty()) {
            edtstock_product.setError("Số lượng không được để trống");
            edtstock_product.requestFocus();
            return false;
        } else {
            try {
                Integer.parseInt(stock);
            } catch (NumberFormatException e) {
                edtstock_product.setError("Số lượng phải là số");
                edtstock_product.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void anhXaDialog(View view) {
        edtimage_product = view.findViewById(R.id.edtimage_product);
        edtname_product = view.findViewById(R.id.edtname_product);
        edtauthor_product = view.findViewById(R.id.edtauthor_product);
        edtdescription_product = view.findViewById(R.id.edtdescription_product);
        edtprice_product = view.findViewById(R.id.edtprice_product);
        rdLife = view.findViewById(R.id.rdPhone);
        rdStory = view.findViewById(R.id.rdLaptop);
        rdAcademic = view.findViewById(R.id.rdAccessory);
        edtstock_product = view.findViewById(R.id.edtstock_product);
        rdConHang = view.findViewById(R.id.rdConHang);
        rdHetHang = view.findViewById(R.id.rdHetHang);
        rdNgungBan = view.findViewById(R.id.rdNgungBan);
        btnCancel = view.findViewById(R.id.btnCancel_dialog_add_product);
        btnAdd = view.findViewById(R.id.btnOk_dialog_add_product);
        radio_status_product = view.findViewById(R.id.radio_status_product);
        rdgStatus = view.findViewById(R.id.rdgStatus_product);
    }

    private void anhXa(View view) {
        btnProfileAdmin = view.findViewById(R.id.btnProfileAdmin);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        rvProductAdmin = view.findViewById(R.id.rvProductAdmin);
    }

    // Hàm hiển thị hộp thoại xác nhận thoát
    private void showExitConfirmationDialog() {
        // Tạo hộp thoại xác nhận
        new AlertDialog.Builder(requireContext())
                .setTitle("Thoát ứng dụng")
                .setMessage("Bạn có chắc chắn muốn thoát ứng dụng không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Thoát ứng dụng
                    requireActivity().finish();
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    dialog.dismiss(); // Đóng hộp thoại
                })
                .show();
    }

    public void loadData() {
        request.callAPI().getListProductAll().enqueue(callbackGetAllProduct);
    }

    Callback<ResponeData<ArrayList<Product>>> callbackGetAllProduct = new Callback<ResponeData<ArrayList<Product>>>() {
        @Override
        public void onResponse(Call<ResponeData<ArrayList<Product>>> call, Response<ResponeData<ArrayList<Product>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listProduct.clear();
                    listProduct.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<ArrayList<Product>>> call, Throwable t) {
            Toast.makeText(getActivity(), "Lỗi kết nối getProduct", Toast.LENGTH_SHORT).show();
            Log.e("er", t.getMessage());
        }
    };

    Callback<ResponeData<Product>> callbackAddProduct = new Callback<ResponeData<Product>>() {
        @Override
        public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    loadData();
                } else if (response.body().getStatus() == 400) {
                    Toast.makeText(getActivity(), "Lỗi addProduct", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Lỗi kết nối addProduct", Toast.LENGTH_SHORT).show();
                Log.e("er", response.message());
            }
        }

        @Override
        public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
            Toast.makeText(getActivity(), "Lỗi kết nối addProduct", Toast.LENGTH_SHORT).show();
            Log.e("er", t.getMessage());
        }
    };
}