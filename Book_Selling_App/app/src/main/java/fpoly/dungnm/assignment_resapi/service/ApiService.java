package fpoly.dungnm.assignment_resapi.service;

import java.util.ArrayList;

import fpoly.dungnm.assignment_resapi.models.BestSelling;
import fpoly.dungnm.assignment_resapi.models.Cart;
import fpoly.dungnm.assignment_resapi.models.Favourite;
import fpoly.dungnm.assignment_resapi.models.Order;
import fpoly.dungnm.assignment_resapi.models.OrderDetail;
import fpoly.dungnm.assignment_resapi.models.Product;
import fpoly.dungnm.assignment_resapi.models.ResponeData;
import fpoly.dungnm.assignment_resapi.models.Revenue;
import fpoly.dungnm.assignment_resapi.models.User;
import fpoly.dungnm.assignment_resapi.models.UserBest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    public static String BASE_URL = "http://10.24.4.228:3000/api/";

    // User
    @POST("add_user")
    Call<ResponeData<User>> register(@Body User user);
    @GET("login")
    Call<ResponeData<User>> login(@Query("username") String username, @Query("password") String password);
    @GET("get_list_user")
    Call<ResponeData<ArrayList<User>>> getListUser();
    @PUT("update_user")
    Call<ResponeData<User>> updateUser(@Body User user);

    // Product
    @GET("get_list_product")
    Call<ResponeData<ArrayList<Product>>> getListProductAll();
    @POST("add_product")
    Call<ResponeData<Product>> addProduct(@Body Product product);
    @PUT("update_product/{id}")
    Call<ResponeData<Product>> updateProduct(@Path("id") String id, @Body Product product);
    @DELETE("delete_product/{id}")
    Call<ResponeData<Product>> deleteProduct(@Path("id") String id);
    @GET("get_product_by_name")
    Call<ResponeData<ArrayList<Product>>> getProductByName(@Query("name") String name);

    // Favourite
    @GET("get_list_favourite_by_name")
    Call<ResponeData<ArrayList<Favourite>>> getListFavourite(@Query("username") String username);
    @POST("add_to_favourite")
    Call<ResponeData<Favourite>> addFavourite(@Body Favourite favourite);
    @GET("check_favourite")
    Call<ResponeData<Favourite>> checkFavourite(@Query("username") String username, @Query("productID") String productID);
    @DELETE("delete_from_favourite/{id}")
    Call<ResponeData<Favourite>> deleteFavourite(@Path("id") String id);

    // Cart
    @GET("get_cart_products/{username}")
    Call<ResponeData<ArrayList<Cart>>> getListCart(@Path("username") String username);
    @DELETE("delete_product_from_cart")
    Call<ResponeData<Cart>> deleteProductFromCart(@Query("id") String id);
    @PUT("update_cart_product")
    Call<ResponeData<Cart>> updateCartProduct(@Body Cart cart);
    @POST("add_product_to_cart")
    Call<ResponeData<Cart>> addProductToCart(@Body Cart cart);
    @DELETE("delete_ordered_products_from_cart")
    Call<ResponeData<Cart>> deleteOrderedProductsFromCart(@Query("username") String username);

    // Order
    @GET("get_order_by_status")
    Call<ResponeData<ArrayList<Order>>> getListOrderByStatus(@Query("username") String username, @Query("status") String status);
    @POST("add_order")
    Call<ResponeData<Order>> addOrder(@Query("username") String username);
    @DELETE("delete_order")
    Call<ResponeData<Order>> deleteOrder(@Query("orderId") String orderId);
    @PUT("update_order")
    Call<ResponeData<Order>> updateOrder(@Query("id") String orderId, @Query("status") String status);
    @GET("get_all_orders_by_status")
    Call<ResponeData<ArrayList<Order>>> getAllOrdersByStatus(@Query("status") String status);
    @GET("best_selling_products")
    Call<ResponeData<ArrayList<BestSelling>>> getBestSellingProducts();
    @GET("top_spending_customers")
    Call<ResponeData<ArrayList<UserBest>>> getTopSpendingCustomers();
    @GET("revenue_by_date")
    Call<ResponeData<Revenue>> getRevenueByDate(@Query("startDate") String startDate, @Query("endDate") String endDate);

    // OrderDetail
    @POST("add_order_detail")
    Call<ResponeData<ArrayList<OrderDetail>>> addOrderDetail(@Query("username") String username, @Query("orderId") String orderId);
    @DELETE("delete_order_detail")
    Call<ResponeData<OrderDetail>> deleteOrderDetail(@Query("orderId") String id);
    @DELETE("delete_one_detail")
    Call<ResponeData<OrderDetail>> deleteOneDetail(@Query("id") String id);
    @GET("get_order_details_by_order_id")
    Call<ResponeData<ArrayList<OrderDetail>>> getOrderDetails(@Query("orderId") String orderId);

}
