const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const OrderDetails = new Schema({
    orderId: {type: Schema.Types.ObjectId, required: true, ref: 'order'}, // FK đến bảng Order
    productId: {type: Schema.Types.ObjectId, required: true, ref: 'product'}, // FK đến bảng Product
    quantity: {type: Number, required: true, min: 0}, // Số lượng sản phẩm (>= 0)
    unitPrice: {type: Number, required: true, min: 0}, // Giá mỗi sản phẩm (>= 0)
    amount: {type: Number, required: true, min: 0}, // Thành tiền (>= 0)
}, {
    timestamps: true
});

// Thiết lập composite key cho OrderId + ProductId
OrderDetails.index({orderId: 1, productId: 1}, {unique: true});

// Hook tự động tính `amount` trước khi lưu
OrderDetails.pre('save', function(next) {
    this.amount = this.quantity * this.unitPrice;
    next();
});

module.exports = mongoose.model('orderdetail', OrderDetails);
