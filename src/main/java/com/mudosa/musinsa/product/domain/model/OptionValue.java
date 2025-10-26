package com.mudosa.musinsa.product.domain.model;

import com.mudosa.musinsa.common.domain.model.BaseEntity;
<<<<<<< HEAD
import com.mudosa.musinsa.product.domain.vo.OptionValueVo;
=======
import com.mudosa.musinsa.product.domain.vo.OptionValueContent;
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
<<<<<<< HEAD
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "option_value")
public class OptionValue extends BaseEntity {
    
=======
@Table(name = "option_value", 
       indexes = {
           @Index(name = "idx_optval_option_name_id", columnList = "option_name_id")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uniq_option_value_name_val", columnNames = {"option_name_id", "option_value"})
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionValue extends BaseEntity {

>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_value_id")
    private Long optionValueId;
<<<<<<< HEAD
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_name_id", nullable = false, foreignKey = @ForeignKey(name = "fk_optionval_optnam"))
    private OptionName optionName;
    
    @Column(name = "option_value", nullable = false, length = 50)
    private OptionValueVo optionValue;
    
    @Builder
    public OptionValue(OptionName optionName, OptionValueVo optionValue) {
        this.optionName = optionName;
        this.optionValue = optionValue;
    }
    
    // 도메인 로직: 수정
    public void modify(OptionValueVo optionValue) {
        if (optionValue != null) this.optionValue = optionValue;
    }
=======

    @Column(name = "option_name_id", nullable = false)  // FK (OptionName)
    private Long optionNameId;

    @Embedded
    private OptionValueContent optionValue;

    // 연관관계 - OptionName 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_name_id", insertable = false, updatable = false)
    private OptionName optionName;

    @OneToMany(mappedBy = "optionValue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductValueOptionMapping> productValueOptionMappings = new ArrayList<>();

    // 생성 메서드
    public static OptionValue create(Long optionNameId, String optionValue) {
        return new OptionValue(optionNameId, OptionValueContent.of(optionValue));
    }

    // 비즈니스 메서드
    public void updateOptionValue(String optionValue) {
        this.optionValue = OptionValueContent.of(optionValue);
    }

    public void updateOptionName(Long optionNameId) {
        this.optionNameId = optionNameId;
    }

    // 연관관계 메서드 - ProductValueOptionMapping 연결
    public void addProductValueOptionMapping(ProductValueOptionMapping mapping) {
        productValueOptionMappings.add(mapping);
    }

    public void removeProductValueOptionMapping(ProductValueOptionMapping mapping) {
        productValueOptionMappings.remove(mapping);
    }

    // JPA를 위한 protected 생성자
    protected OptionValue(Long optionNameId, OptionValueContent optionValue) {
        this.optionNameId = optionNameId;
        this.optionValue = optionValue;
    }
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
}