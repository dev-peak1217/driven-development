package study.drivendevelopment.ddd.domain.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

public class Order {

    // 1. 필드 생성
    private final UUID id;                // UUID
    private final String orderNumber;     // 주문번호
    private final int quantity;           // 주문 수량
    private final String shippingAddress; // 배송주소
    private final String phoneNumber;     // 핸드폰 번호
    private final long paymentAmount;     // 결제 금액 (소수점 절삭을 위해 long 타입)

    // 2. 생성자 및 팩토리 메소드
    // 적정 팩토리 메소드 패턴을 이용하여 create 메소드 생성
    private Order(int quantity, String shippingAddress, String phoneNumber, double paymentAmount) {
        // 검증 실행
        validateShippingAddress(shippingAddress);
        validatePhoneNumber(phoneNumber);

        // 내부 메소드를 통한 자체 생성
        this.id = generateUUID();
        this.orderNumber = generateOrderNumber();

        // 파라미터로 전달된 값 사용
        this.quantity = quantity;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.paymentAmount = validateAndProcessAmount(paymentAmount);
    }

    public static Order create(int quantity, String shippingAddress, String phoneNumber, double paymentAmount) {
        return new Order(quantity, shippingAddress, phoneNumber, paymentAmount);
    }

    // 3. 내부 생성 메소드
    // UUID class를 통한 randomUUID 생성
    private UUID generateUUID() {
        return UUID.randomUUID();
    }

    // 현재 시간을 YYmmddhhmmss 형식으로 주문 번호 생성
    private String generateOrderNumber() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
    }

    // 4. 검증 메소드
    // 배송주소 검증: null 또는 공백 불가
    private void validateShippingAddress(String shippingAddress) {
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("배송 주소는 필수이며 공백일 수 없습니다.");
        }
    }

    // 전화번호 검증: 3자리-4자리-4자리 숫자 형식
    private void validatePhoneNumber(String phoneNumber) {
        String regex = "^\\d{3}-\\d{4}-\\d{4}$";
        if (phoneNumber == null || !Pattern.matches(regex, phoneNumber)) {
            throw new IllegalArgumentException("전화번호는 000-0000-0000 형식이어야 합니다.");
        }
    }

    // 결제금액 검증: 0 또는 1 이상의 값, 음수 불가, 소수점 절삭
    private long validateAndProcessAmount(double paymentAmount) {
        if (paymentAmount < 0) {
            throw new IllegalArgumentException("결제 금액은 음수일 수 없습니다.");
        }
        return (long) paymentAmount; // 소수점 이하 절삭
    }

    // Getter 메소드 (조회용)
    public UUID getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public int getQuantity() { return quantity; }
    public String getShippingAddress() { return shippingAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public long getPaymentAmount() { return paymentAmount; }
}
