package com.nuce.duantp.sunshine.enums;

public enum EnumResponseStatusCode {
    ADD_ACCOUNT_SUCCESS("Thêm tai khoản ngân hàng thành công!"),
    ACCOUNT_EXISTED("Tài khoản ngân hàng đã tồn tại!"),
    EDIT_USER_SUCCESS("Thay đổi thông tin thành công"),
    EDIT_USER_FAILED("Thay đổi thông tin thất bại!"),
    USER_NOT_FOUND("Không tìm thấy tài khoản!"),
    BAD_REQUEST("Có lỗi xảy ra!"),
    TIME_INVALID("Thời gian đặt bàn trong khoảng 8:00 - 22:59!"),
    ADD_FOOD_SUCCESS("Thêm món ăn thành công!"),
    ADD_FOOD_FAILED("Thêm món ăn thất bại!"),
    PAY_SUCCESS("Thanh toán thành công!"),
    PAY_FAILED("Tài khoản không đủ tiền!"),
    CANCEL_BOOKING_SUCCESS("Huỷ đặt bàn thành công!"),
    EMPLOYEE_CANCEL_BOOKING_SUCCESS("Đợi nhân viên xác nhận!"),
    INVALID_EMAIL_FORMAT("Định dạng email không hợp lệ!"),
    INVALID_NAME_FORMAT("Định dạng tên không hợp lệ!"),
    INVALID_PASSWORD_FORMAT("Định dạng password không hợp lệ!"),
    EMAIL_EXISTED("Email đã tồn tại!"),
    SUCCESS("Thành công!"),
    TABLE_OFF("Hết bàn!"),
    EMAIL_PASS_NOT_CORRECT("Email hoặc Password không đúng!"),
    TEST("Test");

    public final String label;
    private EnumResponseStatusCode(String label) {
        this.label = label;
    }
}
