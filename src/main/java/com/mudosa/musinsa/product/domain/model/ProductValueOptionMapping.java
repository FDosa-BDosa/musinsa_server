package com.mudosa.musinsa.product.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
<<<<<<< HEAD
<<<<<<< HEAD
import lombok.Builder;
=======
import lombok.EqualsAndHashCode;
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
=======
import lombok.Builder;
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
<<<<<<< HEAD
<<<<<<< HEAD
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_value_option_mapping")
public class ProductValueOptionMapping {
    
    @EmbeddedId
    private ProductValueOptionMappingId id;
    
    @MapsId("productOptionId")
=======
@Table(name = "product_value_option_mapping", 
       indexes = {
           @Index(name = "idx_map_prodopt", columnList = "product_option_id"),
           @Index(name = "idx_map_optval", columnList = "option_value_id")
       })
=======
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_value_option_mapping")
public class ProductValueOptionMapping {
    
    @EmbeddedId
    private ProductValueOptionMappingId id;
    
    @MapsId("productOptionId")
<<<<<<< HEAD
    @Column(name = "product_option_id", nullable = false)
    private Long productOptionId;

    @MapsId("optionValueId")
    @Column(name = "option_value_id", nullable = false)
    private Long optionValueId;

    // 연관관계 - ProductOption 연결
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;
<<<<<<< HEAD
    
    @MapsId("optionValueId")
=======

>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_value_id")
    private OptionValue optionValue;
<<<<<<< HEAD
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;
    
    @MapsId("optionValueId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_value_id")
    private OptionValue optionValue;
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
    
    @MapsId("optionNameId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_name_id")
    private OptionName optionName;
    
    @Builder
    public ProductValueOptionMapping(ProductOption productOption, OptionValue optionValue, OptionName optionName) {
<<<<<<< HEAD
        this.productOption = productOption;
        this.optionValue = optionValue;
        this.optionName = optionName;
        this.id = new ProductValueOptionMappingId(
            productOption.getProductOptionId(),
            optionValue.getOptionValueId(),
            optionName.getOptionNameId()
        );
    }
    
    // 도메인 로직: 수정
    public void modify(ProductOption productOption, OptionValue optionValue, OptionName optionName) {
        if (productOption != null) {
            this.productOption = productOption;
            this.id.productOptionId = productOption.getProductOptionId();
        }
        if (optionValue != null) {
            this.optionValue = optionValue;
            this.id.optionValueId = optionValue.getOptionValueId();
        }
        if (optionName != null) {
            this.optionName = optionName;
            this.id.optionNameId = optionName.getOptionNameId();
        }
    }
    
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class ProductValueOptionMappingId implements Serializable {
        
        @Column(name = "product_option_id")
        private Long productOptionId;
        
        @Column(name = "option_value_id")
        private Long optionValueId;
        
        @Column(name = "option_name_id")
        private Long optionNameId;
=======

    // 생성 메서드
    public static ProductValueOptionMapping create(Long productOptionId, Long optionValueId) {
        return new ProductValueOptionMapping(productOptionId, optionValueId);
    }

    // 비즈니스 메서드
    public void updateProductOption(Long productOptionId) {
        this.productOptionId = productOptionId;
        this.id = new ProductValueOptionMappingId(productOptionId, this.optionValueId);
    }

    public void updateOptionValue(Long optionValueId) {
        this.optionValueId = optionValueId;
        this.id = new ProductValueOptionMappingId(this.productOptionId, optionValueId);
    }

    // 연관관계 메서드
    public void setProductOption(ProductOption productOption) {
=======
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
        this.productOption = productOption;
        this.optionValue = optionValue;
        this.optionName = optionName;
        this.id = new ProductValueOptionMappingId(
            productOption.getProductOptionId(),
            optionValue.getOptionValueId(),
            optionName.getOptionNameId()
        );
    }
    
    // 도메인 로직: 수정
    public void modify(ProductOption productOption, OptionValue optionValue, OptionName optionName) {
        if (productOption != null) {
            this.productOption = productOption;
            this.id.productOptionId = productOption.getProductOptionId();
        }
        if (optionValue != null) {
            this.optionValue = optionValue;
            this.id.optionValueId = optionValue.getOptionValueId();
        }
        if (optionName != null) {
            this.optionName = optionName;
            this.id.optionNameId = optionName.getOptionNameId();
        }
    }
    
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class ProductValueOptionMappingId implements Serializable {
        
        @Column(name = "product_option_id")
        private Long productOptionId;
        
        @Column(name = "option_value_id")
        private Long optionValueId;
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
        
<<<<<<< HEAD
=======
        @Column(name = "option_name_id")
        private Long optionNameId;
        
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
        public ProductValueOptionMappingId(Long productOptionId, Long optionValueId, Long optionNameId) {
            this.productOptionId = productOptionId;
            this.optionValueId = optionValueId;
            this.optionNameId = optionNameId;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductValueOptionMappingId that = (ProductValueOptionMappingId) o;
<<<<<<< HEAD
<<<<<<< HEAD
            return Objects.equals(productOptionId, that.productOptionId) &&
                   Objects.equals(optionValueId, that.optionValueId) &&
                   Objects.equals(optionNameId, that.optionNameId);
=======
            return Objects.equals(productOptionId, that.productOptionId) && 
                   Objects.equals(optionValueId, that.optionValueId);
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
=======
            return Objects.equals(productOptionId, that.productOptionId) &&
                   Objects.equals(optionValueId, that.optionValueId) &&
                   Objects.equals(optionNameId, that.optionNameId);
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(productOptionId, optionValueId, optionNameId);
        }
    }
}