var express = require('express');
var router = express.Router();

var api_user = require('../controllers/user.api');
var productController = require('../controllers/product.api');
var favouriteControler = require('../controllers/favourtie.api');
var cartController = require('../controllers/cart.api');
var orderController = require('../controllers/order.api');
var orderDetailController = require('../controllers/orderdetail.api');

// API user
router.get('/get_list_user', api_user.listUser);
router.post('/add_user', api_user.register);
router.get('/login', api_user.getUserByUsernameAndPassword);
router.put('/update_user', api_user.updateUser);

// API product
router.post('/add_product', productController.addProduct);
router.put('/update_product/:id', productController.updateProduct);
router.delete('/delete_product/:id', productController.deleteProduct);
router.get('/get_list_product', productController.getListProduct);
router.get('/get_product_by_name', productController.getByName);
router.get('/get_product_by_id/:id', productController.getById);

// API Favourite
router.post('/add_to_favourite', favouriteControler.addToFavourite);
router.delete('/delete_from_favourite/:id', favouriteControler.deleteFromFavourite);
router.get('/get_list_favourite_by_name', favouriteControler.getListByName);
router.get('/check_favourite', favouriteControler.checkFavourite);

// API Cart
router.post('/add_product_to_cart', cartController.addProductToCartByUsername);
router.delete('/delete_product_from_cart', cartController.deleteProductFromCart);
router.put('/update_cart_product', cartController.updateQuantityAndAmount);
router.get('/get_cart_products/:username', cartController.getListProductFromCartByUsername);
router.delete('/delete_ordered_products_from_cart', cartController.deleteOrderedProductsFromCart);

//API Order
router.post('/add_order', orderController.addOrder);
router.put('/update_order', orderController.updateOrder);
router.get('/get_order_by_status', orderController.getOrdersByStatus);
router.delete('/delete_order', orderController.deleteOrder);
router.get('/get_all_orders_by_status', orderController.getAllOrdersByStatus);
router.get('/best_selling_products', orderController.getBestSellingProducts);
router.get('/revenue_by_date', orderController.getRevenueByDateRange);
router.get('/top_spending_customers', orderController.getTopSpendingCustomers);

// API OrderDetail
router.post('/add_order_detail', orderDetailController.addToOrderDetail);
router.delete('/delete_order_detail', orderDetailController.deleteOrderDetail);
router.delete('/delete_one_detail', orderDetailController.deleteOneOrderDetail);
router.get('/get_order_details_by_order_id', orderDetailController.getOrderDetailsByOrderId);

module.exports = router;