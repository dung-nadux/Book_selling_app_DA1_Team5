const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Favorites = new Schema({
    username: {type: String, required: true, ref: 'user'}, // FK từ bảng user
    productID: {type: Schema.Types.ObjectId, required: true, ref: 'product'}
}, {
    timestamps: true
})

Favorites.index({username: 1, productID: 1}, {unique: true});

module.exports = mongoose.model('favourite', Favorites)