const Users = require('../models/categories')

// Them loai san pham
exports.add_category = async (req, res) => {
    try {
        const data = req.body
        const newCate = new Users(data)
        const result = await newCate.save();
        if (result) {
            return res.json({
                "status": 200,
                "msg": "Thêm loại sản phẩm thành công",
                "data": result
            })
        } else {
            return res.json({
                "status": 400,
                "msg": "Thêm loại sản phẩm thất bại",
                "data": []
            })
        }
    } catch (error) {
        return res.status(500).json({msg: error.message});
    }
}