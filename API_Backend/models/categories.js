const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Categories = new Schema({
    catename: {type: String, unique: true, maxLength: 225}
}, {
    timestamps: true
})

module.exports = mongoose.model('category', Categories)