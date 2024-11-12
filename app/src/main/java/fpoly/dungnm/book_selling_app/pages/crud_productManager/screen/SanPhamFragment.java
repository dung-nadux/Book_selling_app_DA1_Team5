package fpoly.dungnm.book_selling_app.pages.crud_productManager.screen;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import fpoly.dungnm.book_selling_app.DAO.ProductDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.adapter.AdapterProducts;
import fpoly.dungnm.book_selling_app.models.ModelProducts;

public class SanPhamFragment extends Fragment {
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
            showAlertDialogAdd();
        });
    }

    // Hàm hiển thị AlertDialog để thêm item
    // Hiển thị AlertDialog để thêm item
    private void showAlertDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_product_add, null);
        builder.setView(dialogView);

        ivImage = dialogView.findViewById(R.id.ivImage);
        EditText etTitle = dialogView.findViewById(R.id.edTitle);
        EditText etAuthor = dialogView.findViewById(R.id.edAuthor);
        EditText etPrice = dialogView.findViewById(R.id.edPrice);
        EditText etDescription = dialogView.findViewById(R.id.edDescription);
        EditText etCategory = dialogView.findViewById(R.id.edCategory);

        ivImage.setOnClickListener(v -> openImagePicker());

        builder.setTitle("Add New Item")
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String author = etAuthor.getText().toString();
                    String priceStr = etPrice.getText().toString();
                    String description = etDescription.getText().toString();
                    String category = etCategory.getText().toString();

                    if (title.isEmpty() || author.isEmpty() || priceStr.isEmpty() || description.isEmpty() || category.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ModelProducts product = new ModelProducts();
                    product.setTitle(title);
                    product.setAuthor(author);
                    product.setPrice(Integer.parseInt(priceStr));
                    product.setDescription(description);
                    product.setCategory(category);

                    if (imageUri != null) {
                        product.setImage(imageUri.toString()); // Store image URI as a string
                    }

                    // Thêm sản phẩm vào SQLite qua ProductDAO
                    productDAO.insertProduct(product);
                    listProducts.add(product);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivImage.setImageURI(imageUri);
        }
    }
}