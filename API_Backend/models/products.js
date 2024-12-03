const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Products = new Schema({
    productname: {type: String, unique: true, required: true, maxLength: 225},
    image: {type: String, maxLength: 255, default: 'default_image.png'},
    description: {type: String, default: '', maxLength: 1000},
    price: {type: Number, required: true, min: 0},
    cateID: {type: String, enum: ['Phone', 'Laptop', 'Accessory'], default: 'Phone'}, 
    stock: {type: Number, required: true, min: 0, default: 0}, // Số lượng sản phẩm trong kho
    status: {type: String, required: true, enum: ['Còn hàng', 'Hết hàng', 'Ngừng bán'], default: 'Còn hàng'}
}, {
    timestamps: true
});

module.exports = mongoose.model('product', Products);
