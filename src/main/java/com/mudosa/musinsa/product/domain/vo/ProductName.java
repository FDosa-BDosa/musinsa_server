package com.mudosa.musinsa.product.domain.vo;

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
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 상품명 Value Object
 * DDL: VARCHAR(100) NOT NULL
 */
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductName {

    @Column(name = "product_name", nullable = false, length = 100)
    private String value;

    private ProductName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("상품명은 100자를 초과할 수 없습니다.");
        }
        this.value = value.trim();
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }

    public String getValue() {
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
        return value;
    }
}