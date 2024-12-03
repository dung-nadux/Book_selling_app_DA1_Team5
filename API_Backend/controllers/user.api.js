const Users = require('../models/users')

// Danh sách User
exports.listUser = async (req, res) => {
    try {
        const data = await Users.find().sort({ createdAt: -1 });
        if (data) {
            return res.json({
                "status": 200,
                "msg": "Lấy danh sách user thành công",
                "data": data
            })
        } else {
            return res.json({
                "status": 400,
                "msg": "Lấy danh sách user thất bại",
                "data": []
            })
        }
    } catch (error) {
        return res.status(500).json({msg: error.message});
    }
}


// Hàm xử lý lấy user bằng username và password
exports.getUserByUsernameAndPassword = async (req, res) => {
    try {
        const { username, password } = req.query;

        if (!username?.trim() || !password?.trim()) {
            return res.json({
                status: 400,
                msg: "Username và password không được để trống",
                data: null
            });
        }

        // Tìm user trong database
        const user = await Users.findOne({ username, password });

        if (user) {
            return res.json({
                "status": 200,
                "msg": "Tìm thấy user",
                "data": user
            });
        } else {
            return res.json({
                "status": 404,
                "msg": "Không tìm thấy user hoặc sai thông tin đăng nhập",
                "data": null
            });
        }
    } catch (error) {
        // Xử lý lỗi server
        return res.status(500).json({msg: error.message});
    }
};

// Đăng ký
exports.register = async (req, res) => {
    try {
        const data = req.body
        const newUser = new Users(data)
        const result = await newUser.save();
        if (result) {
            return res.json({
                "status": 200,
                "msg": "Đăng ký thành công",
                "data": result
            })
        } else {
            return res.json({
                "status": 400,
                "msg": "Đăng ký thất bại",
                "data": []
            })
        }
    } catch (error) {
        return res.status(500).json({msg: error.message});
    }
}

// Hàm xử lý cập nhật thông tin user
exports.updateUser = async (req, res) => {
    try {
        const { username } = req.body;
        const data = req.body;

        if (!username) {
            return res.json({
                status: 400,
                msg: "Vui lòng cung cấp username để cập nhật thông tin",
                data: null
            });
        }

        // Xây dựng object chứa các trường cần cập nhật
        // const updateData = {};
        // if (req.body.fullname) updateData.fullname = req.body.fullname;
        // if (req.body.address) updateData.address = req.body.address;
        // if (req.body.phone) updateData.phone = req.body.phone;
        // if (req.body.password) updateData.password = req.body.password;

        // Cập nhật user trong database
        const updatedUser = await Users.findOneAndUpdate(
            { username: username }, // Điều kiện để tìm user
            data,             // Dữ liệu cần cập nhật
            { new: true }           // Tùy chọn trả về document sau khi cập nhật
        );

        if (updatedUser) {
            return res.json({
                status: 200,
                msg: "Cập nhật thông tin user thành công",
                data: updatedUser
            });
        } else {
            return res.json({
                status: 404,
                msg: "Không tìm thấy user với username đã cung cấp",
                data: null
            });
        }
    } catch (error) {
        // Xử lý lỗi server
        return res.status(500).json({
            status: 500,
            msg: "Đã xảy ra lỗi server",
            error: error.message
        });
    }
};