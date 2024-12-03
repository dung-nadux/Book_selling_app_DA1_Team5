const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Users = new Schema({
    username: {type: String, unique: true, maxLength: 225},
    password: {type: String, maxLength: 255},
    fullname: {type: String},
    address: {type: String},
    phone: {type: String},
    balance: {type: Number, default: 0},
    status: {type: String, enum: ['Active', 'Banned'], default: 'Active'}
}, {
    timestamps: true
})

module.exports = mongoose.model('user', Users)