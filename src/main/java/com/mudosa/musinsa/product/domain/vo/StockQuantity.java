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
public class StockQuantity {
    private Integer value;
    
    public StockQuantity(Integer value) {
        validate(value);
        this.value = value;
    }
    
    private void validate(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("재고 수량은 null일 수 없습니다.");
        }
        if (value < 0) {
            throw new IllegalArgumentException("재고 수량은 음수일 수 없습니다.");
        }
    }
    
    @Override
    public String toString() {
        return value.toString();
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
public class StockQuantity {
    private Integer value;
    
    public StockQuantity(Integer value) {
        validate(value);
        this.value = value;
    }
    
    private void validate(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("재고 수량은 null일 수 없습니다.");
        }
        if (value < 0) {
            throw new IllegalArgumentException("재고 수량은 음수일 수 없습니다.");
        }
    }
    
    @Override
    public String toString() {
<<<<<<< HEAD
        return value + "개";
>>>>>>> de5afbd (FDBD-43 ✨ feat[product]: 상품 재고 model + vo생성 및 상품 옵션에 재고 관계 추가.)
=======
        return value.toString();
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
    }
}