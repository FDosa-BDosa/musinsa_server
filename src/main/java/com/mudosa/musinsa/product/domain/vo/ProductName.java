package com.mudosa.musinsa.product.domain.vo;

<<<<<<< HEAD
<<<<<<< HEAD
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductName {
    private String value;
    
    private static final int MAX_LENGTH = 100;
    
    public ProductName(String value) {
        validate(value);
        this.value = value;
    }
    
    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("상품명은 비어있을 수 없습니다.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("상품명은 " + MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
    }
    
    @Override
    public String toString() {
=======
import jakarta.persistence.Column;
=======
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductName {
    private String value;
    
    private static final int MAX_LENGTH = 100;
    
    public ProductName(String value) {
        validate(value);
        this.value = value;
    }
    
    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("상품명은 비어있을 수 없습니다.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("상품명은 " + MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
    }
<<<<<<< HEAD

    public static ProductName of(String value) {
        return new ProductName(value);
    }

    public String getValue() {
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
=======
    
    @Override
    public String toString() {
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
        return value;
    }
}