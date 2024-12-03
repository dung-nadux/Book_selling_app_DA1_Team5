const OrderDetail = require('../models/orderdetails');
const Cart = require('../models/carts');
const Order = require('../models/orders');


// thêm vào đơn hàng (lấy tù giỏ hàng)
exports.addToOrderDetail = async (req, res) => {
    try {
        const { orderId, username } = req.query;

        // Lấy sản phẩm từ giỏ hàng của user (lấy sản phẩm có status = 1)
        const cartItems = await Cart.find({ username, status: 1 }).populate('productID');

        if (cartItems.length === 0) {
            return res.json({
                status: 400,
                msg: "Giỏ hàng trống hoặc không có sản phẩm hợp lệ"
            });
        }
        var total = 0;
        // Lặp qua giỏ hàng để thêm vào OrderDetails
        const orderDetails = await Promise.all(
            cartItems.map(async (item) => {
                const newDetail = new OrderDetail({
                    orderId,
                    productId: item.productID._id,
                    quantity: item.quantity,
                    unitPrice: item.productID.price, // Giá hiện tại ca sản phẩm
                    amount: item.quantity * item.productID.price
                });

                total += item.quantity * item.productID.price
                await newDetail.save();
                return newDetail;
            })
        );

        const updateOrder = await Order.findByIdAndUpdate(orderId, {totalAmount: total}, { new: true });

        // Sau khi lưu tất cả orderDetails, lấy lại data với populate
        const populatedOrderDetails = await OrderDetail.find({ orderId })
            .populate('productId');

        return res.status(200).json({
            status: 200,
            msg: "Thêm sản phẩm từ giỏ hàng vào chi tiết đơn hàng thành công",
            data: populatedOrderDetails
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// xóa chi tiết đơn hàng
exports.deleteOrderDetail = async (req, res) => {
    try {
        const { orderId} = req.query;

        const deletedDetail = await OrderDetail.deleteMany({ orderId});

        if (deletedDetail.deletedCount === 0) {
            return res.status(404).json({
                status: 404,
                msg: "Chi tiết đơn hàng không tồn tại"
            });
        }

        return res.status(200).json({
            status: 200,
            msg: "Xóa chi tiết đơn hàng thành công"
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// xóa một chi tiết đơn hàng theo ID
exports.deleteOneOrderDetail = async (req, res) => {
    try {
        const { id } = req.query;

        // Lấy thông tin chi tiết đơn hàng với populate trước khi xóa
        const orderDetailToDelete = await OrderDetail.findById(id).populate('productId');
        
        if (!orderDetailToDelete) {
            return res.json({
                status: 404,
                msg: "Không tìm thấy chi tiết đơn hàng"
            });
        }

        // Thực hiện xóa
        const deletedDetail = await OrderDetail.findByIdAndDelete(id);

        // Cập nhật lại tổng tiền của đơn hàng
        const remainingDetails = await OrderDetail.find({ orderId: deletedDetail.orderId });
        const newTotal = remainingDetails.reduce((sum, detail) => sum + detail.amount, 0);
        
        await Order.findByIdAndUpdate(deletedDetail.orderId, { totalAmount: newTotal });

        return res.status(200).json({
            status: 200,
            msg: "Xóa chi tiết đơn hàng thành công",
            data: orderDetailToDelete // Trả về dữ liệu đã được populate
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// lấy danh sách chi tiết đơn hàng theo orderId
exports.getOrderDetailsByOrderId = async (req, res) => {
    try {
        const { orderId } = req.query;

        const orderDetails = await OrderDetail.find({ orderId })
            .populate('productId'); // populate để lấy thông tin sản phẩm

        if (!orderDetails || orderDetails.length === 0) {
            return res.json({
                status: 404,
                msg: "Không tìm thấy chi tiết đơn hàng nào"
            });
        }

        return res.status(200).json({
            status: 200,
            msg: "Lấy danh sách chi tiết đơn hàng thành công",
            data: orderDetails
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};



