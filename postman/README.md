# 📮 Hướng Dẫn Chạy Test API Tự Động Với Postman

Thư mục này chứa các tệp tin cấu hình tự động kiểm thử API cho dự án **YiYi Book** phục vụ cho môn học **Kiểm chứng phần mềm**.

## 📁 Các tệp cấu hình có sẵn:
1. `YiYiBook_Collection.json`: Bộ sưu tập các API request có sẵn test script tự động kiểm thử dữ liệu và kiểm tra logic (Đăng ký, Đăng nhập, Xem sách, Giỏ hàng, Đặt hàng).
2. `YiYiBook_Environment.json`: Môi trường chứa các biến `baseUrl` và tự động lưu `token` JWT sau khi đăng nhập thành công.

---

## 🚀 Hướng Dẫn Sử Dụng Trên Giao Diện Postman

### Bước 1: Nhập tệp vào Postman
1. Mở ứng dụng **Postman** trên máy tính.
2. Click vào nút **Import** ở góc trên cùng bên trái.
3. Kéo và thả cả hai tệp `YiYiBook_Collection.json` và `YiYiBook_Environment.json` vào.

### Bước 2: Chọn Môi trường (Environment)
1. Ở góc trên cùng bên phải của Postman, click vào menu thả xuống chọn môi trường (mặc định ban đầu có thể hiển thị là *No Environment*).
2. Chọn **YiYi Book Local Environment**.

### Bước 3: Chạy ứng dụng Backend (Cổng 8081)
* Hãy chắc chắn bạn đã bật Backend của dự án thành công (đang chạy tại `http://localhost:8081`).

### Bước 4: Chạy kiểm thử tự động
1. Click chuột phải vào Collection **YiYi Book REST API Tests** vừa nhập.
2. Chọn **Run Collection**.
3. Bạn có thể nhấn nút **Run YiYi Book...** màu cam. Postman sẽ tự động chạy tuần tự các API từ trên xuống dưới:
   * **Đăng ký tài khoản mới**
   * **Đăng nhập** (Tự động lấy Token trả về và lưu vào biến môi trường)
   * **Lấy danh sách sách nổi bật**
   * **Tìm kiếm sách**
   * **Thêm sách vào giỏ hàng** (Sử dụng Token vừa lưu)
   * **Xem giỏ hàng**
   * **Tạo đơn hàng** (Sử dụng Token vừa lưu)
4. Màn hình sẽ hiển thị bảng kết quả kiểm thử màu xanh lá cây đại diện cho các trường hợp kiểm thử đã Pass thành công.

---

## 💻 Hướng Dẫn Chạy Bằng Dòng Lệnh (Newman) - Để nộp bài

Giảng viên chấm bài rất thích việc chạy test tự động qua Command Line (CLI). Bạn có thể chạy trực tiếp bằng công cụ **Newman**:

1. Cài đặt Newman (yêu cầu máy có sẵn Node.js):
   ```bash
   npm install -g newman
   ```
2. Mở Terminal tại thư mục `postman/` và chạy lệnh sau:
   ```bash
   newman run YiYiBook_Collection.json -e YiYiBook_Environment.json
   ```
3. Newman sẽ thực thi kiểm thử và xuất ra một bảng báo cáo kết quả kiểm thử ngay trên cửa sổ terminal của bạn.


////