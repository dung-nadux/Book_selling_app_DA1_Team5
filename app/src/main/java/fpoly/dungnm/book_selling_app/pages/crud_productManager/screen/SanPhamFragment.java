package fpoly.dungnm.book_selling_app.pages.crud_productManager.screen;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import fpoly.dungnm.book_selling_app.DAO.ProductDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.adapter.AdapterProducts;
import fpoly.dungnm.book_selling_app.models.ModelProducts;

public class SanPhamFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText edTitle, edAuthor, edPrice, edDescription, edCategory;
    FloatingActionButton fabAdd;
    private Uri imageUri;
    RecyclerView recyclerView;
    AdapterProducts adapter;
    ArrayList<ModelProducts> listProducts = new ArrayList<>();
    ImageView ivImage;
    ProductDAO productDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_san_pham, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabAdd = view.findViewById(R.id.fabAdd);
        recyclerView = view.findViewById(R.id.recyclerView);

        productDAO = new ProductDAO(getContext());
        listProducts = productDAO.getAllProducts();

        // Thiết lập RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new AdapterProducts(getContext(), listProducts);

        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            // Gọi phương thức để mở dialog
            showAddProductDialog();
        });

        adapter.setOnItemClickListener(new AdapterProducts.OnItemClickListener() {

            @Override
            public void deleteItem(String id) {
                // Xác nhận xóa và cập nhật lại danh sách
                showDeleteConfirmationDialog(id);
            }

            @Override
            public void updateItem(String id, String image, String title, String author, String price, String description, String category) {
                Log.e("=========//////////", image );
                showAlertDialogUpdate(id, image, title, author, price, description, category);
            }

        });
    }

    // Hàm hiển thị AlertDialog để thêm item
    private void showAddProductDialog() {
        // Tạo LayoutInflater để gắn layout vào dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_product_add, null);

        // Ánh xạ các View trong dialog
        edTitle = dialogView.findViewById(R.id.edTitle);
        edAuthor = dialogView.findViewById(R.id.edAuthor);
        edPrice = dialogView.findViewById(R.id.edPrice);
        edDescription = dialogView.findViewById(R.id.edDescription);
        edCategory = dialogView.findViewById(R.id.edCategory);
        ivImage = dialogView.findViewById(R.id.ivImage);

        // Tạo Dialog và thiết lập các thuộc tính
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView)
                .setTitle("Add Product")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lưu dữ liệu khi nhấn "Save"
                        saveProduct();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();

        // Xử lý sự kiện nhấn vào ImageView để chọn ảnh
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
    }

    // Mở bộ chọn ảnh
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Xử lý kết quả khi người dùng chọn ảnh
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ivImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // Lưu sản phẩm vào cơ sở dữ liệu
    private void saveProduct() {
        String title = edTitle.getText().toString();
        String author = edAuthor.getText().toString();
        int price = Integer.parseInt(edPrice.getText().toString());
        String description = edDescription.getText().toString();
        String category = edCategory.getText().toString();

        // Chuyển đổi ảnh thành byte[]
        byte[] imageByteArray = imageUriToByteArray(imageUri);

        // Tạo đối tượng ModelProducts
        ModelProducts product = new ModelProducts(imageByteArray, title, author, price, description, category);

        // Gọi phương thức insertProduct từ DAO để lưu sản phẩm vào cơ sở dữ liệu
        boolean isInserted = productDAO.insertProduct(product);
        if(isInserted){
            Log.d("đường dẫn ảnh", Arrays.toString(imageByteArray));
            Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            listProducts = productDAO.getAllProducts();
            adapter.setData(listProducts);
//            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
        }

        // Thực hiện lưu dữ liệu vào cơ sở dữ liệu hoặc thao tác khác ở đây
        // Ví dụ: Sử dụng DAO để lưu sản phẩm vào SQLite hoặc Firebase
        Log.d("AddProduct", "Product saved: " + title);
    }

    // Chuyển đổi ảnh thành byte[]
    private byte[] imageUriToByteArray(Uri uri) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }


    private void showAlertDialogUpdate(String productId, String image, String title, String author, String price, String description, String category) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.item_product_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sửa sản phẩm");
        builder.setView(dialogView);

        ImageView ivImage = dialogView.findViewById(R.id.ivImage);
        EditText edTitle = dialogView.findViewById(R.id.edTitle);
        EditText edAuthor = dialogView.findViewById(R.id.edAuthor);
        EditText edPrice = dialogView.findViewById(R.id.edPrice);
        EditText edDescription = dialogView.findViewById(R.id.edDescription);
        EditText edCategory = dialogView.findViewById(R.id.edCategory);

        // Điền thông tin hiện tại vào các EditText
        edTitle.setText(title);
        edAuthor.setText(author);
        edPrice.setText(price);
        edDescription.setText(description);
        edCategory.setText(category);

        // Hiển thị ảnh hiện tại nếu có
        if (image != null && !image.isEmpty()) {
            // Chuyển đổi chuỗi byte thành mảng byte[] và sau đó decode thành Bitmap
            String[] byteStrings = image.replaceAll("[\\[\\] ]", "").split(","); // Xóa dấu ngoặc và khoảng trắng
            byte[] imageBytes = new byte[byteStrings.length];
            for (int i = 0; i < byteStrings.length; i++) {
                imageBytes[i] = Byte.parseByte(byteStrings[i]);
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ivImage.setImageBitmap(bitmap);
        }

        ivImage.setOnClickListener(v -> openImagePickerForUpdate());

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String updatedTitle = edTitle.getText().toString();
            String updatedAuthor = edAuthor.getText().toString();
            String updatedPrice = edPrice.getText().toString();
            String updatedDescription = edDescription.getText().toString();
            String updatedCategory = edCategory.getText().toString();

            if (updatedTitle.isEmpty() || updatedAuthor.isEmpty() || updatedPrice.isEmpty() || updatedDescription.isEmpty() || updatedCategory.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            ModelProducts updatedProduct = new ModelProducts();
            updatedProduct.setId(Integer.parseInt(productId));
            updatedProduct.setTitle(updatedTitle);
            updatedProduct.setAuthor(updatedAuthor);
            updatedProduct.setPrice(Integer.parseInt(updatedPrice));
            updatedProduct.setDescription(updatedDescription);
            updatedProduct.setCategory(updatedCategory);

            // Kiểm tra xem có ảnh mới không
            if (imageUri != null) {
                updatedProduct.setImage(imageUriToByteArray(imageUri)); // Chuyển đổi ảnh mới thành byte[]
            } else {
                // Giữ nguyên ảnh cũ
                updatedProduct.setImage(image.getBytes());
            }

            boolean success = productDAO.updateProduct(updatedProduct);
            if (success) {
                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                listProducts = productDAO.getAllProducts();
                adapter.setData(listProducts);
            } else {
                Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Xóa sản phẩm khỏi cơ sở dữ liệu và cập nhật lại danh sách
    private void showDeleteConfirmationDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận xoá");
        builder.setMessage("Bạn có chắc chắn muốn xoá sản phẩm này?");

        builder.setPositiveButton("Có", (dialog, which) -> {
            boolean check = productDAO.deleteProduct(Integer.parseInt(id));
            if (check) {
                Toast.makeText(getContext(), "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                listProducts = productDAO.getAllProducts();
                adapter.setData(listProducts);
            } else {
                Toast.makeText(getContext(), "Xoá sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    private void openImagePickerForUpdate() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 102);
    }

}