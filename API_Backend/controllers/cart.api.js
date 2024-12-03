const Cart = require('../models/carts');
const Product = require('../models/products');
const User = require('../models/users');

// Thêm vào giỏ hàng
exports.addProductToCartByUsername = async (req, res) => {
    try {
        const { username, productID, quantity } = req.body;

        // Lấy thông tin sản phẩm từ bảng Product
        const currentProduct = await Product.findById(productID);
        if (!currentProduct) {
            return res.json({
                status: 404,
                msg: "Sản phẩm không tồn tại"
            });
        }

        const priceProduct = currentProduct.price; // Giá sản phẩm lấy từ bảng Product

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        const existingCart = await Cart.findOne({ username, productID });
        if (existingCart) {
            // Nếu đã tồn tại: cập nhật số lượng và tổng giá
            existingCart.quantity += quantity;
            existingCart.amount = existingCart.quantity * priceProduct;

            const updatedCart = await existingCart.save();
            const populatedCart = await updatedCart.populate('productID');

            return res.status(200).json({
                status: 200,
                msg: "Cập nhật sản phẩm trong giỏ hàng thành công",
                data: populatedCart
            });
        }

        // Nếu chưa tồn tại: thêm mới sản phẩm vào giỏ hàng
        const newCart = new Cart({
            username,
            productID,
            quantity,
            amount: priceProduct * quantity
        });

        const savedCart = await newCart.save();
        const populatedSavedCart = await savedCart.populate('productID');

        return res.status(200).json({
            status: 200,
            msg: "Thêm sản phẩm vào giỏ hàng thành công",
            data: populatedSavedCart
        });
    } catch (error) {
        return res.status(500).json({
            status: 500,
            msg: error.message
        });
    }
};

// Xóa sản phẩm khỏi giỏ hàng
exports.deleteProductFromCart = async (req, res) => {
    try {
        const {id} = req.query;

        const deletedCart = await Cart.findByIdAndDelete({_id: id});
        if (deletedCart) {
            let populateDeleteCart = await deletedCart.populate('productID');
            return res.status(200).json({
                status: 200,
                msg: "Xóa sản phẩm khỏi giỏ hàng thành công",
                data: populateDeleteCart
            });
        }

        return res.json({
            status: 404,
            msg: "Sản phẩm không tồn tại trong giỏ hàng"
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Cập nhật số lượng và giá của sản phẩm trong giỏ hàng
exports.updateQuantityAndAmount = async (req, res) => {
    try {
        const { username, productID, quantity, status } = req.body;

        const currentProduct = await Product.findById(productID);
        if (!currentProduct) {
            return res.json({
                status: 404,
                msg: "Sản phẩm không tồn tại"
            });
        }

        const priceProduct = currentProduct.price;
        const updatedCart = await Cart.findOneAndUpdate(
            { username, productID },
            { $set: { quantity, amount: priceProduct * quantity, status} },
            { new: true } // Trả về tài liệu sau khi cập nhật
        );

        if (updatedCart) {
            let populateUpdateCart = await updatedCart.populate('productID');
            return res.status(200).json({
                status: 200,
                msg: "Cập nhật số lượng và tổng giá thành công",
                data: populateUpdateCart
            });
        }

        return res.json({
            status: 404,
            msg: "Sản phẩm không tồn tại trong giỏ hàng"
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Lấy danh sách sảnh phẩm trong giỏ hàng
exports.getListProductFromCartByUsername = async (req, res) => {
    try {
        const { username } = req.params;

        const cartItems = await Cart.find({ username }).populate('productID');
        if (cartItems.length > 0) {
            return res.status(200).json({
                status: 200,
                msg: "Lấy danh sách sản phẩm từ giỏ hàng thành công",
                data: cartItems
            });
        }

        return res.json({
            status: 404,
            msg: "Không tìm thấy sản phẩm trong giỏ hàng"
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Xóa sản phẩm đã đặt hàng khỏi giỏ hàng
exports.deleteOrderedProductsFromCart = async (req, res) => {
    try {
        const { username } = req.query;
        
        // Lấy danh sách sản phẩm đã đặt hàng
        const orderedProducts = await Cart.find({
            username: username,
            status: 1
        }).populate('productID');

        // Tính tổng số tiền cần thanh toán
        const totalAmount = orderedProducts.reduce((total, item) => {
            return total + item.amount;
        }, 0);

        // Tìm và cập nhật balance của user
        const user = await User.findOne({ username });
        if (user.balance < totalAmount) {
            return res.status(400).json({
                status: 400,
                msg: "Số dư không đủ để thanh toán"
            });
        }

        // Trừ tiền từ balance
        await User.findOneAndUpdate(
            { username },
            { $inc: { balance: -totalAmount } }
        );

        // Cập nhật số lượng trong kho cho từng sản phẩm
        for (const cartItem of orderedProducts) {
            const updatedStock = cartItem.productID.stock - cartItem.quantity;
            
            // Cập nhật số lượng và trạng thái nếu hết hàng
            await Product.findByIdAndUpdate(
                cartItem.productID._id,
                {
                    $set: {
                        stock: updatedStock,
                        status: updatedStock <= 0 ? 'Hết hàng' : cartItem.productID.status
                    }
                }
            );
        }
        
        // Xóa sản phẩm khỏi giỏ hàng
        const result = await Cart.deleteMany({ 
            username: username,
            status: 1
        });

        if (result.deletedCount > 0) {
            return res.status(200).json({
                status: 200,
                msg: "Đã thanh toán và xóa sản phẩm khỏi giỏ hàng thành công",
                deletedCount: result.deletedCount,
                paidAmount: totalAmount
            });
        }

        return res.json({
            status: 404,
            msg: "Không tìm thấy sản phẩm đã đặt hàng trong giỏ hàng"
        });

    } catch (error) {
        return res.status(500).json({
            status: 500,
            msg: error.message
        });
    }
};


