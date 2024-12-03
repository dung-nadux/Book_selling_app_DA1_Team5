const Favorite = require('../models/favourites');

// Thêm vào yêu thích
exports.addToFavourite = async (req, res) => {
    try {
        const { username, productID } = req.body;

        // Kiểm tra xem sản phẩm đã có trong danh sách yêu thích chưa
        const existingFavourite = await Favorite.findOne({ username, productID });
        if (existingFavourite) {
            const deletedFavourite = await Favorite.findByIdAndDelete(existingFavourite.id);
            let resultDelete = deletedFavourite.populate('productID')
            if (resultDelete) {
                return res.json({
                    status: 400,
                    msg: "Đã xóa sản phẩm khỏi yêu thích",
                    data: resultDelete
                });
            }
        }

        // Tạo mục yêu thích mới
        const newFavourite = new Favorite({ username, productID });
        const savedFavourite = await newFavourite.save();

        // Populate thông tin sản phẩm
        const populatedFavourite = await savedFavourite.populate('productID');

        return res.status(200).json({
            status: 200,
            msg: "Thêm sản phẩm vào yêu thích thành công",
            data: populatedFavourite
        });
    } catch (error) {
        return res.status(500).json({
            status: 500,
            msg: error.message
        });
    }
};


// Xóa khỏi yêu thích
exports.deleteFromFavourite = async (req, res) => {
    try {
        const { id } = req.params; // Lấy ID sản phẩm từ URL
        const deletedFavourite = await Favorite.findByIdAndDelete(id);

        if (deletedFavourite) {
            let resultDelete = deletedFavourite.populate('productID')
            return res.status(200).json({
                status: 200,
                msg: "Xóa sản phẩm thành công",
                data: resultDelete
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
}

// Lấy danh sách yêu thích theo username
exports.getListByName = async (req, res) => {
    try {
        const { username } = req.query; // Lấy tên sản phẩm từ query
        const favourite = await Favorite.find({ username: { $regex: username, $options: 'i' } }).populate('productID');

        if (favourite.length > 0) {
            return res.status(200).json({
                status: 200,
                msg: "Tìm sản phẩm theo tên thành công",
                data: favourite
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

// Check tồn tại của sản phẩm trong danh sách yêu thích
exports.checkFavourite = async (req, res) => {
    try {
        const { username, productID } = req.query;

        if (!username || !productID) {
            return res.json({
                "status": 400,
                "msg": "Vui lòng cung cấp username và productID",
                "data": null
            });
        }

        // Tìm favourite trong database
        const favourite = await Favorite.findOne({ username, productID }).populate('productID');

        if (favourite) {
            return res.json({
                "status": 200,
                "msg": "Tìm thấy user",
                "data": favourite
            });
        } else {
            return res.json({
                "status": 404,
                "msg": "Không tìm thấy favourite hoặc sai thông tin",
                "data": null
            });
        }
    } catch (error) {
        // Xử lý lỗi server
        return res.status(500).json({
            "status": 500,
            "msg": "Đã xảy ra lỗi server",
            "data": null,
            "error": error.message
        });
    }
};