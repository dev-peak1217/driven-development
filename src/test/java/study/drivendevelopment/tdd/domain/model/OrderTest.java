package study.drivendevelopment.tdd.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @Test
    @DisplayName("1. 주문 클래스의 기본 생성자 호출 테스트")
    public void 주문_클래스_기본_생성자_호출_테스트() {
        // Given
        String shippingAddress = "서울시 강남구";
        String phoneNumber = "010-1234-5678";
        double paymentAmount = 10000.0;

        // When
        Order order = Order.create(shippingAddress, phoneNumber, paymentAmount);

        // Then
        assertThat(order).isNotNull();
    }

    @Test
    @DisplayName("2. 정적 팩토리 메소드를 통한 생성자 반환 기능 테스트")
    public void 정적_팩토리_메소드_create_테스트() {
        // Given
        String shippingAddress = "서울시 강남구";
        String phoneNumber = "010-1234-5678";
        double paymentAmount = 10000.0;

        // When
        Order order = Order.create(shippingAddress, phoneNumber, paymentAmount);

        // Then
        assertThat(order).isNotNull();
    }

    @Test
    @DisplayName("3. UUID 생성 기능 테스트")
    public void UUID_생성_기능_테스트() {
        // Given
        String shippingAddress = "서울시 강남구";
        String phoneNumber = "010-1234-5678";
        double paymentAmount = 10000.0;

        // When
        Order order = Order.create(shippingAddress, phoneNumber, paymentAmount);

        // Then
        assertThat(order.getId()).isNotNull();
        assertThat(order.getId()).isInstanceOf(UUID.class);
    }

    @Test
    @DisplayName("4. 주문번호 생성 기능 테스트 - YYmmddhhmmss 형식")
    public void 주문번호_생성_기능_테스트() {
        // Given
        String shippingAddress = "서울시 강남구";
        String phoneNumber = "010-1234-5678";
        double paymentAmount = 10000.0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String expectedPattern = LocalDateTime.now().format(formatter);

        // When
        Order order = Order.create(shippingAddress, phoneNumber, paymentAmount);
        String orderNumber = order.getOrderNumber();

        // Then
        assertThat(orderNumber).isNotNull();
        assertThat(orderNumber).hasSize(12);
        assertThat(orderNumber).matches("\\d{12}");
        assertThat(orderNumber).startsWith(expectedPattern.substring(0, 8));
    }

    @Test
    @DisplayName("5-1. 배송 주소 검증 성공 테스트")
    public void 배송_주소_검증_성공_테스트() {
        // Given
        String validAddress = "서울시 강남구";
        String phoneNumber = "010-1234-5678";
        double paymentAmount = 10000.0;

        // When
        Order order = Order.create(validAddress, phoneNumber, paymentAmount);

        // Then
        assertThat(order.getShippingAddress()).isEqualTo(validAddress);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("5-2. 배송 주소 null 또는 공백 예외 테스트")
    public void 배송_주소_null_또는_공백_예외_테스트(String invalidAddress) {
        // Given
        String phoneNumber = "010-1234-5678";
        double paymentAmount = 10000.0;

        // When & Then
        assertThatThrownBy(() -> Order.create(invalidAddress, phoneNumber, paymentAmount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"010-1234-5678", "011-9876-5432", "019-0000-1111"})
    @DisplayName("6-1. 전화번호 검증 성공 테스트")
    public void 전화번호_검증_성공_테스트(String validPhoneNumber) {
        // Given
        String shippingAddress = "서울시 강남구";
        double paymentAmount = 10000.0;

        // When
        Order order = Order.create(shippingAddress, validPhoneNumber, paymentAmount);

        // Then
        assertThat(order.getPhoneNumber()).isEqualTo(validPhoneNumber);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "01012345678",       // 하이픈 없음
            "010-12345-678",     // 잘못된 형식
            "010-123-5678",      // 3자리가 아님
            "010-1234-567",      // 4자리가 아님
            "010-abcd-5678",     // 문자 포함
            "010-1234-567a",     // 문자 포함
            "0101234-5678"       // 잘못된 형식
    })
    @DisplayName("6-2. 전화번호 검증 예외 테스트")
    public void 전화번호_검증_예외_테스트(String invalidPhoneNumber) {
        // Given
        String shippingAddress = "서울시 강남구";
        double paymentAmount = 10000.0;

        // When & Then
        assertThatThrownBy(() -> Order.create(shippingAddress, invalidPhoneNumber, paymentAmount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 1000.0, 10000.5, 99999.9})
    @DisplayName("7-1. 결제 금액 검증 성공 테스트 - 0 이상의 값")
    public void 결제_금액_검증_성공_테스트(double validAmount) {
        // Given
        String shippingAddress = "서울시 강남구";
        String phoneNumber = "010-1234-5678";

        // When
        Order order = Order.create(shippingAddress, phoneNumber, validAmount);

        // Then
        assertThat(order.getPaymentAmount()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("7-2. 결제 금액 소수점 첫째자리 내림 처리 테스트")
    public void 결제_금액_소수점_내림_처리_테스트() {
        // Given
        String shippingAddress = "서울시 강남구";
        String phoneNumber = "010-1234-5678";
        double amount = 1234.99;

        // When
        Order order = Order.create(shippingAddress, phoneNumber, amount);

        // Then
        assertThat(order.getPaymentAmount()).isEqualTo(1234L);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -100.0, -0.1})
    @DisplayName("7-3. 결제 금액 음수 예외 테스트")
    public void 결제_금액_음수_예외_테스트(double invalidAmount) {
        // Given
        String shippingAddress = "서울시 강남구";
        String phoneNumber = "010-1234-5678";

        // When & Then
        assertThatThrownBy(() -> Order.create(shippingAddress, phoneNumber, invalidAmount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("8. 필드 생성 테스트 - 모든 필드가 올바르게 초기화되는지 확인")
    public void 필드_생성_테스트() {
        // Given
        String shippingAddress = "서울시 강남구";
        String phoneNumber = "010-1234-5678";
        double paymentAmount = 10000.99;

        // When
        Order order = Order.create(shippingAddress, phoneNumber, paymentAmount);

        // Then
        assertThat(order).isNotNull();
        assertThat(order.getId()).isNotNull();
        assertThat(order.getId()).isInstanceOf(UUID.class);
        assertThat(order.getOrderNumber()).isNotNull();
        assertThat(order.getOrderNumber()).hasSize(12);
        assertThat(order.getShippingAddress()).isEqualTo(shippingAddress);
        assertThat(order.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(order.getPaymentAmount()).isEqualTo(10000L);
    }
}
