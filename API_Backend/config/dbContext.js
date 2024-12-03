const mongoose = require("mongoose");
const mongoURI = "mongodb://127.0.0.1:27017/DuAn1";

const connect = async () => {
    try {
        await mongoose.connect(mongoURI, {
            useNewUrlParser: true,
            useUnifiedTopology: true,
        })
        .then(() => {
            console.log("Kết nối mongodb thành công");
        })
        .catch((err) => {
            console.log("Kết nối thất bại");
        });
    } catch (error) {
        console.log("Kết nối thất bại" + error);
    }
};
module.exports = { connect };