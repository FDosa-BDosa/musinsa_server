-- 상품 이미지 데이터 추가 스크립트
-- 사용법: mysql -u root -p musinsa < insert_product_images.sql

-- 상품 ID 1번에 이미지 추가 (썸네일 1개 + 일반 이미지 2개)
INSERT INTO image (is_thumbnail, created_at, event_id, product_id, updated_at, image_url)
VALUES
    -- 썸네일 이미지 (각 상품당 정확히 1개만!)
    (1, NOW(), NULL, 1, NOW(), 'https://postfiles.pstatic.net/MjAxODExMTVfMjA2/MDAxNTQyMjYwNDQ1MzM2._wBPUukRo3BX0MeyYHsXnGtA56xEoLiqU-chNVxuW80g.Px8xllz9PThy9qn-kI3HddpPArBhBQby8gHLUps0AYMg.JPEG.photodynamic/tip-1-shoe-photography.jpg?type=w966'),
    -- 일반 이미지
    (0, NOW(), NULL, 1, NOW(), 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800'),
    (0, NOW(), NULL, 1, NOW(), 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=800');

-- 상품 ID 2번에 이미지 추가
INSERT INTO image (is_thumbnail, created_at, event_id, product_id, updated_at, image_url)
VALUES
    (1, NOW(), NULL, 2, NOW(), 'https://postfiles.pstatic.net/MjAxODExMTVfMjA2/MDAxNTQyMjYwNDQ1MzM2._wBPUukRo3BX0MeyYHsXnGtA56xEoLiqU-chNVxuW80g.Px8xllz9PThy9qn-kI3HddpPArBhBQby8gHLUps0AYMg.JPEG.photodynamic/tip-1-shoe-photography.jpg?type=w966'),
    (0, NOW(), NULL, 2, NOW(), 'https://images.unsplash.com/photo-1560769629-975ec94e6a86?w=800');

-- 상품 ID 3번에 이미지 추가
INSERT INTO image (is_thumbnail, created_at, event_id, product_id, updated_at, image_url)
VALUES
    (1, NOW(), NULL, 3, NOW(), 'https://postfiles.pstatic.net/MjAxODExMTVfMjA2/MDAxNTQyMjYwNDQ1MzM2._wBPUukRo3BX0MeyYHsXnGtA56xEoLiqU-chNVxuW80g.Px8xllz9PThy9qn-kI3HddpPArBhBQby8gHLUps0AYMg.JPEG.photodynamic/tip-1-shoe-photography.jpg?type=w966'),
    (0, NOW(), NULL, 3, NOW(), 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=800');

-- 상품 ID 4번에 이미지 추가
INSERT INTO image (is_thumbnail, created_at, event_id, product_id, updated_at, image_url)
VALUES
    (1, NOW(), NULL, 4, NOW(), 'https://postfiles.pstatic.net/MjAxODExMTVfMjA2/MDAxNTQyMjYwNDQ1MzM2._wBPUukRo3BX0MeyYHsXnGtA56xEoLiqU-chNVxuW80g.Px8xllz9PThy9qn-kI3HddpPArBhBQby8gHLUps0AYMg.JPEG.photodynamic/tip-1-shoe-photography.jpg?type=w966'),
    (0, NOW(), NULL, 4, NOW(), 'https://images.unsplash.com/photo-1600185365926-3a2ce3cdb9eb?w=800');

-- 상품 ID 5번에 이미지 추가
INSERT INTO image (is_thumbnail, created_at, event_id, product_id, updated_at, image_url)
VALUES
    (1, NOW(), NULL, 5, NOW(), 'https://postfiles.pstatic.net/MjAxODExMTVfMjA2/MDAxNTQyMjYwNDQ1MzM2._wBPUukRo3BX0MeyYHsXnGtA56xEoLiqU-chNVxuW80g.Px8xllz9PThy9qn-kI3HddpPArBhBQby8gHLUps0AYMg.JPEG.photodynamic/tip-1-shoe-photography.jpg?type=w966'),
    (0, NOW(), NULL, 5, NOW(), 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=800'),
    (0, NOW(), NULL, 5, NOW(), 'https://images.unsplash.com/photo-1551107696-a4b0c5a0d9a2?w=800');

-- 추가된 이미지 확인
SELECT p.product_id, p.product_name, i.image_id, i.is_thumbnail, i.image_url
FROM product p
LEFT JOIN image i ON p.product_id = i.product_id
ORDER BY p.product_id, i.is_thumbnail DESC, i.image_id;
