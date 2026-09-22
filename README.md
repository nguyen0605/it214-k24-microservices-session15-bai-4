# Báo cáo: Xây dựng Orchestrator Saga với State Machine

## 1. So sánh Choreography và Orchestration Saga
- **Choreography Saga:** Các service tự lắng nghe và phát sự kiện qua Message Broker (Kafka). Khi luồng nghiệp vụ ít bước, mô hình này rất linh hoạt. Tuy nhiên, khi hệ thống mở rộng, việc theo dõi luồng sự kiện trở nên khó khăn (hiện tượng "Event Spaghetti"), khó debug lỗi và không có cái nhìn tổng thể về trạng thái giao dịch.
- **Orchestration Saga với State Machine:** Có một "Nhạc trưởng" (Orchestrator) tập trung quản lý trạng thái, tuần tự hóa các bước và quyết định hành động tiếp theo dựa trên kết quả trả về. Giúp dễ dàng theo dõi, debug và kiểm soát quy trình nghiệp vụ phức tạp.

## 2. Mô tả Trạng thái, Sự kiện và Luồng chuyển đổi
### States (Trạng thái)
- `INITIATED`: Khởi tạo yêu cầu đặt vé.
- `PAYMENT_PENDING`: Đang xử lý thanh toán.
- `PAYMENT_COMPLETED`: Thanh toán thành công.
- `SEAT_RESERVING`: Đang giữ chỗ.
- `BOOKING_CONFIRMED`: Đặt vé hoàn tất.
- `CANCELLED`: Hủy bỏ và thực hiện bù trừ (compensation).

### Events (Sự kiện)
- `PROCESS_PAYMENT`, `PAYMENT_SUCCESS`, `PAYMENT_FAILED`
- `RESERVE_SEATS`, `RESERVATION_SUCCESS`, `RESERVATION_FAILED`

## 3. Retry Policy và Cơ chế bù trừ
- **Retry Policy:** Thử lại tối đa 3 lần với khoảng cách 2 giây giữa các lần khi gặp lỗi kết nối/timeout ở bước thanh toán.
- **Compensation:** Nếu bước giữ chỗ thất bại hoặc thanh toán lỗi sau 3 lần thử, hệ thống chuyển sang trạng thái `CANCELLED` và kích hoạt lệnh hoàn tiền (refund) nếu đã thanh toán.

## 4. Kết quả chạy thử
```text
[Orchestrator] State: INITIATED -> Event: PROCESS_PAYMENT -> New State: PAYMENT_PENDING
[Orchestrator] RetryPolicy: Activity 'processPayment' - Attempt 1/3
[Orchestrator] State: PAYMENT_PENDING -> Event: PAYMENT_SUCCESS -> New State: PAYMENT_COMPLETED
[Orchestrator] State: PAYMENT_COMPLETED -> Event: RESERVE_SEATS -> New State: SEAT_RESERVING
[Orchestrator] State: SEAT_RESERVING -> Event: RESERVATION_SUCCESS -> New State: BOOKING_CONFIRMED
[Orchestrator] Final State: BOOKING_CONFIRMED for booking CONCERT-2026-088
```