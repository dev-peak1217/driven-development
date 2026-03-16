package study.drivendevelopment.tdd.domain.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

public class Order {

    private final UUID id;
    private final String orderNumber;
    private final String shippingAddress;
    private final String phoneNumber;
    private final long paymentAmount;

    private Order(String shippingAddress, String phoneNumber, double paymentAmount) {
        validateShippingAddress(shippingAddress);
        validatePhoneNumber(phoneNumber);

        this.id = generateUUID();
        this.orderNumber = generateOrderNumber();
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.paymentAmount = validateAndProcessAmount(paymentAmount);
    }

    public static Order create(String shippingAddress, String phoneNumber, double paymentAmount) {
        return new Order(shippingAddress, phoneNumber, paymentAmount);
    }

    public UUID getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getPaymentAmount() {
        return paymentAmount;
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }

    private String generateOrderNumber() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
    }

    private void validateShippingAddress(String shippingAddress) {
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("배송 주소는 필수이며 공백일 수 없습니다.");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        String regex = "^\\d{3}-\\d{4}-\\d{4}$";
        if (phoneNumber == null || !Pattern.matches(regex, phoneNumber)) {
            throw new IllegalArgumentException("전화번호는 000-0000-0000 형식이어야 합니다.");
        }
    }

    private long validateAndProcessAmount(double paymentAmount) {
        if (paymentAmount < 0) {
            throw new IllegalArgumentException("결제 금액은 음수일 수 없습니다.");
        }
        return (long) paymentAmount;
    }
}
