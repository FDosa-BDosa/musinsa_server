package com.mudosa.musinsa.product.domain.model;

import com.mudosa.musinsa.common.domain.model.BaseEntity;
<<<<<<< HEAD
<<<<<<< HEAD
import com.mudosa.musinsa.product.domain.vo.OptionValueVo;
=======
import com.mudosa.musinsa.product.domain.vo.OptionValueContent;
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
=======
import com.mudosa.musinsa.product.domain.vo.OptionValueVo;
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
<<<<<<< HEAD
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
=======
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "option_value")
public class OptionValue extends BaseEntity {
<<<<<<< HEAD

>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
=======
    
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_value_id")
    private Long optionValueId;
<<<<<<< HEAD
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
=======
    
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
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
<<<<<<< HEAD
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
=======
    
    // 도메인 로직: 수정
    public void modify(OptionValueVo optionValue) {
        if (optionValue != null) this.optionValue = optionValue;
    }
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
}