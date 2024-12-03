const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Orders = new Schema({
    username: {type: String, required: true, ref: 'user'}, // FK từ bảng user
    status: {type: String, required: true, enum: ['Chờ xác nhận', 'Đã xác nhận', 'Đang giao hàng', 'Đã giao hàng', 'Đã hủy'], default: 'Chờ xác nhận'}, // Trạng thái đơn hàng
    orderDate: {type: Date, required: true, default: Date.now}, // Ngày đặt hàng
    totalAmount: {type: Number, required: true, min: 0}, // Tổng tiền đơn hàng
}, {
    timestamps: true
})

module.exports = mongoose.model('order', Orders)