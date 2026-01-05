#  Dự án Kiểm thử Tự động (QLDA Automation Tests)

Dự án này chứa các kịch bản kiểm thử End-to-End (E2E) cho ứng dụng QLDA, sử dụng Selenium và TestNG.

---

## 📂 Tổng quan Hệ thống & Tài liệu (Project Resources)

Để thuận tiện cho việc theo dõi toàn bộ hệ sinh thái dự án, các liên kết quan trọng được tổng hợp tại đây:

| Thành phần | Liên kết | Ghi chú |
| :--- | :--- | :--- |
| **Source Code (Frontend)** | [GitHub - QLDA Frontend](https://github.com/phandinhphu/qlda-fe.git) | React.js |
| **Source Code (Backend)** | [GitHub - QLDA Backend](https://github.com/phandinhphu/qlda-be.git) | Node.js, Express, MongoDB |
| **Tài liệu đặc tả (SRS)** | [Google Drive - SRS Document](https://docs.google.com/document/d/1rnbpPP_nFh6-Hyz91euBZT-0uAFgycWn/edit?usp=sharing&ouid=114249575033992896291&rtpof=true&sd=true) | Phân tích yêu cầu hệ thống |
| **Quản lý dự án (Agile/Scrum)** | [Trello - Project Management](https://trello.com/invite/b/68cd192c739eed141784f7d4/ATTI64e2be3aa551d7295c7ab6331ee0cfeb515F83F8/qlduan) | Theo dõi tiến độ (Tasks/Sprints) |
| **Kịch bản kiểm thử (Manual)** | [Google Sheets - Test Case](https://docs.google.com/spreadsheets/d/1nQ_Ll6Bz5PkIvxCiugsY_g13kRUNLa8j2nEr1zVY1yY/edit?usp=sharing) | Test Cases chi tiết |
| **Quản lý lỗi (Bug Tracking)** | [Trello - Bug Report Board](https://trello.com/invite/b/69455693ff09e989731b0586/ATTI226dc524e8d3622a214b58841013eeb23DD6B97C/quản-li-bug-của-project-qlduan) | Danh sách bug và trạng thái fix |

---

## 🚀 Yêu cầu cài đặt (Prerequisites)

Để chạy dự án này, bạn cần cài đặt các phần mềm sau trên máy của mình:

1.  **JDK (Java Development Kit)**:
    * Phiên bản 11 hoặc 17 được khuyến nghị.
    * Để kiểm tra, gõ: `java -version`
2.  **Apache Maven**:
    * Dùng để quản lý thư viện và chạy test.
    * Để kiểm tra, gõ: `mvn -version`
3.  **Trình duyệt (ví dụ: Google Chrome)**:
    * Để Selenium có thể điều khiển và chạy test.

---

## 🔧 Cài đặt (Setup)

1.  **Clone dự án (Nếu chưa có)**:
    (Nếu bạn đã clone dự án `QLDA` rồi thì bỏ qua bước này).
    ```bash
    git clone [Link-den-repo-cua-ban]
    ```

2.  **Cài đặt thư viện (Dependencies)**:
    Mở terminal, `cd` vào thư mục `automation-tests` và chạy lệnh sau. Maven sẽ tự động tải tất cả các thư viện (Selenium, TestNG...) trong file `pom.xml` về.

    ```bash
    cd automation-tests
    mvn install
    ```

3.  **Cấu hình Môi trường (Configuration)**:
    Tất cả các cấu hình (URL, trình duyệt, tài khoản test) được quản lý trong file:
    `src/test/resources/config.properties`

    Hãy đảm bảo các giá trị này là chính xác cho môi trường local của bạn:

    ```properties
    # URL của ứng dụng React (frontend)
    BASE_URL = http://localhost:5173

    # Trình duyệt muốn chạy (CHROME, FIREFOX, EDGE)
    BROWSER = CHROME

    # Thông tin đăng nhập (ví dụ)
    TEST_USERNAME = admin
    TEST_PASSWORD = password123
    ```

---
## ⚡ Chạy Kiểm thử (Running Tests)

Sau khi cài đặt xong, bạn có thể chạy test bằng một trong các cách sau:

### Cách 1: Chạy bằng lệnh Maven (Khuyên dùng)

Cách chạy toàn bộ các bộ test (test suites) đã được định nghĩa trong `testng.xml`.

```bash
# Đảm bảo bạn đang ở trong thư mục automation-tests
mvn test
```

### Cách 2: Chạy bằng VS Code (Sử dụng "Test Runner for Java")

Để chạy test trực tiếp từ VS Code, hãy đảm bảo bạn đã cài đặt Extension Pack for Java.

⚠️ Quan trọng: KHÔNG sử dụng nút "Run" (▶) ở góc trên bên phải màn hình (đó là của extension Code Runner và sẽ gây lỗi).

Thay vào đó, hãy sử dụng các nút chạy test được tích hợp sẵn:

Mở file test (ví dụ: LoginTests.java).

Bạn sẽ thấy các tùy chọn Run | Debug xuất hiện:

Chạy tất cả test trong file: Nhấn Run ngay bên trên dòng public class LoginTests { ... }.

Chạy một test case cụ thể: Nhấn Run ngay bên trên phương thức có annotation @Test mà bạn muốn chạy (ví dụ: public void TC001...).

---

## 📊 Xem Báo cáo (Viewing Reports)

Sau khi chạy xong (bằng mvn test), TestNG và Maven Surefire sẽ tạo ra một báo cáo HTML chi tiết.

Bạn có thể tìm báo cáo tại: target/surefire-reports/index.html

(Hoặc emailable-report.html để có báo cáo đơn giản hơn).
