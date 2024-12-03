const Order = require('../models/orders');
const OrderDetail = require('../models/orderdetails');


// Tạo một đơn hàng mới có tổng tiền bằng 0
exports.addOrder = async (req, res) => {
    try {
        const { username } = req.query;

        // Tạo đơn hàng mới với tổng tiền mặc định là 0
        const newOrder = new Order({
            username,
            totalAmount: 0, // Tổng tiền ban đầu = 0
        });

        const savedOrder = await newOrder.save();

        return res.status(200).json({
            status: 200,
            msg: "Tạo đơn hàng thành công",
            data: savedOrder
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// update đơn hàng
exports.updateOrder = async (req, res) => {
    try {
        const { id, status} = req.query;

        const updatedOrder = await Order.findByIdAndUpdate(
            id,
            { status},
            { new: true }
        );

        if (!updatedOrder) {
            return res.json({ status: 404, msg: "Đơn hàng không tồn tại" });
        }

        return res.status(200).json({
            status: 200,
            msg: "Cập nhật đơn hàng thành công",
            data: updatedOrder
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// lấy danh sách đơn hàng theo trang thái
exports.getOrdersByStatus = async (req, res) => {
    try {
        const { username, status } = req.query;

        const orders = await Order.find({username, status }).sort({ createdAt: -1 });

        return res.status(200).json({
            status: 200,
            msg: "Lấy danh sách đơn hàng theo trạng thái thành công",
            data: orders
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Lấy tất cả đơn hàng theo trạng thái
exports.getAllOrdersByStatus = async (req, res) => {
    try {
        const { status } = req.query;
        
        const orders = await Order.find({ status }).sort({ createdAt: -1 });

        return res.status(200).json({
            status: 200,
            msg: "Lấy danh sách tất cả đơn hàng theo trạng thái thành công",
            data: orders
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// xóa đơn hàng
exports.deleteOrder = async (req, res) => {
    try {
        const { orderId } = req.query;

        const deletedOrder = await Order.findByIdAndDelete(orderId);

        if (!deletedOrder) {
            return res.json({ status: 404, msg: "Đơn hàng không tồn tại" });
        }

        // Xóa tất cả các orderDetail có trong đơn hàng
        const deletedDetail = await OrderDetail.deleteMany({ orderId});
        return res.status(200).json({
            status: 200,
            msg: "Xóa đơn hàng thành công"
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Thống kê sản phẩm bán chạy nhất
exports.getBestSellingProducts = async (req, res) => {
    try {
        const bestSellers = await OrderDetail.aggregate([
            {
                $group: {
                    _id: "$productId",
                    totalQuantity: { $sum: "$quantity" }
                }
            },
            {
                $lookup: {
                    from: "products",
                    localField: "_id",
                    foreignField: "_id",
                    as: "productInfo"
                }
            },
            { $unwind: "$productInfo" },
            { $sort: { totalQuantity: -1 } }
        ]);

        return res.status(200).json({
            status: 200,
            msg: "Lấy danh sách sản phẩm bán chạy thành công",
            data: bestSellers
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Thống kê doanh thu theo khoảng thời gian
exports.getRevenueByDateRange = async (req, res) => {
    try {
        const { startDate, endDate } = req.query;
        
        const revenue = await Order.aggregate([
            {
                $match: {
                    createdAt: {
                        $gte: new Date(startDate),
                        $lte: new Date(endDate)
                    },
                    status: "Đã giao hàng" // Chỉ tính các đơn hàng đã giao thành công
                }
            },
            {
                $group: {
                    _id: null,
                    totalRevenue: { $sum: "$totalAmount" },
                    orderCount: { $sum: 1 }
                }
            }
        ]);

        return res.status(200).json({
            status: 200,
            msg: "Thống kê doanh thu thành công",
            data: revenue[0] || { totalRevenue: 0, orderCount: 0 }
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Lấy danh sách khách hàng có tổng chi tiêu cao nhất
exports.getTopSpendingCustomers = async (req, res) => {
    try {
        const topCustomers = await Order.aggregate([
            {
                $match: {
                    status: "Đã giao hàng" // Chỉ tính các đơn hàng đã giao thành công
                }
            },
            {
                $group: {
                    _id: "$username",
                    totalSpent: { $sum: "$totalAmount" },
                    orderCount: { $sum: 1 }
                }
            },
            {
                $sort: { totalSpent: -1 }
            }
        ]);

        return res.status(200).json({
            status: 200,
            msg: "Lấy danh sách khách hàng chi tiêu cao nhất thành công",
            data: topCustomers
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};


