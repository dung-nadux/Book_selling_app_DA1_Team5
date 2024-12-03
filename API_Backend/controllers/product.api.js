const Product = require('../models/products'); // Import model Product

// Thêm sản phẩm mới
exports.addProduct = async (req, res) => {
    try {
        const newProduct = new Product(req.body);
        const savedProduct = await newProduct.save();

        if (savedProduct) {
            return res.status(200).json({
                status: 200,
                msg: "Thêm sản phẩm thành công",
                data: savedProduct
            });
        }
        return res.json({
            status: 400,
            msg: "Thêm sản phẩm thất bại",
            data: null
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Cập nhật sản phẩm
exports.updateProduct = async (req, res) => {
    try {
        const { id } = req.params; // Lấy ID sản phẩm từ URL
        const updatedProduct = await Product.findByIdAndUpdate(id, req.body, { new: true });

        if (updatedProduct) {
            return res.status(200).json({
                status: 200,
                msg: "Cập nhật sản phẩm thành công",
                data: updatedProduct
            });
        } else {
            return res.json({
                status: 404,
                msg: "Không tìm thấy sản phẩm với ID này"
            });
        }
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Xóa sản phẩm
exports.deleteProduct = async (req, res) => {
    try {
        const { id } = req.params; // Lấy ID sản phẩm từ URL
        const deletedProduct = await Product.findByIdAndDelete(id);

        if (deletedProduct) {
            return res.status(200).json({
                status: 200,
                msg: "Xóa sản phẩm thành công",
                data: deletedProduct
            });
        } else {
            return res.json({
                status: 404,
                msg: "Không tìm thấy sản phẩm với ID này"
            });
        }
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Lấy danh sách tất cả sản phẩm
exports.getListProduct = async (req, res) => {
    try {
        const products = await Product.find().sort({ createdAt: -1 });

        return res.status(200).json({
            status: 200,
            msg: "Lấy danh sách sản phẩm thành công",
            data: products
        });
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Lấy sản phẩm theo tên
exports.getByName = async (req, res) => {
    try {
        const { name } = req.query; // Lấy tên sản phẩm từ query
        const products = await Product.find({ productname: { $regex: name, $options: 'i' } });

        if (products.length > 0) {
            return res.status(200).json({
                status: 200,
                msg: "Tìm sản phẩm theo tên thành công",
                data: products
            });
        } else {
            return res.json({
                status: 404,
                msg: "Không tìm thấy sản phẩm nào với tên này"
            });
        }
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};

// Lấy sản phẩm theo ID
exports.getById = async (req, res) => {
    try {
        const { id } = req.params; // Lấy ID sản phẩm từ URL
        const product = await Product.findById(id);

        if (product) {
            return res.status(200).json({
                status: 200,
                msg: "Tìm sản phẩm theo ID thành công",
                data: product
            });
        } else {
            return res.json({
                status: 404,
                msg: "Không tìm thấy sản phẩm với ID này"
            });
        }
    } catch (error) {
        return res.status(500).json({ status: 500, msg: error.message });
    }
};
