-- ====================================================================
-- 상품 옵션 데이터 추가 스크립트
-- Product ID: 5 (11월 DROP 오픈 스니커즈)
-- 목적: 사이즈 255, 260 옵션 추가
-- ====================================================================

-- 외래 키 체크 비활성화 (순환 참조 해결)
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Inventory 레코드 추가
-- inventory와 product_option은 양방향 1:1 관계이므로
-- 두 테이블을 동시에 삽입해야 합니다
INSERT INTO inventory (inventory_id, product_option_id, is_available, value, stock_quantity, created_at, updated_at)
VALUES
  (6, 12, 1, 0, 100, NOW(), NOW()),  -- 사이즈 255용 재고
  (7, 13, 1, 0, 120, NOW(), NOW());  -- 사이즈 260용 재고

-- 2. ProductOption 레코드 추가
INSERT INTO product_option (product_option_id, product_id, product_price, inventory_id, created_at, updated_at)
VALUES
  (12, 5, 99000.00, 6, NOW(), NOW()),  -- 사이즈 255 옵션 (재고 inventory_id=6)
  (13, 5, 99000.00, 7, NOW(), NOW());  -- 사이즈 260 옵션 (재고 inventory_id=7)

-- 외래 키 체크 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;

-- 3. ProductOptionValue 매핑 추가
-- 각 product_option을 실제 option_value와 연결
INSERT INTO product_option_value (product_option_id, option_value_id)
VALUES
  (12, 2),  -- product_option 12 → option_value 2 (사이즈: 255)
  (13, 3);  -- product_option 13 → option_value 3 (사이즈: 260)

-- ====================================================================
-- 검증 쿼리
-- ====================================================================

-- 상품 5번의 모든 옵션 확인
SELECT
    po.product_option_id,
    po.product_price,
    i.stock_quantity,
    ov.option_value AS size,
    on2.option_name
FROM product_option po
JOIN inventory i ON po.inventory_id = i.inventory_id
JOIN product_option_value pov ON po.product_option_id = pov.product_option_id
JOIN option_value ov ON pov.option_value_id = ov.option_value_id
JOIN option_name on2 ON ov.option_name_id = on2.option_name_id
WHERE po.product_id = 5
ORDER BY po.product_option_id;

-- 예상 결과:
-- product_option_id | product_price | stock_quantity | size | option_name
-- ------------------|---------------|----------------|------|-------------
-- 11                | 99000.00      | 150            | 250  | 사이즈
-- 12                | 99000.00      | 100            | 255  | 사이즈
-- 13                | 99000.00      | 120            | 260  | 사이즈
