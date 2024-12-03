const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Carts = new Schema({
    username: {type: String, required: true, ref: 'user'}, // FK từ bảng user
    productID: {type: Schema.Types.ObjectId, ref: 'product'},
    quantity: {type: Number, min: 0},
    amount: {type: Number, min: 0},
    status: {type: Number, default: 0}
}, {
    timestamps: true
})

module.exports = mongoose.model('cart', Carts)