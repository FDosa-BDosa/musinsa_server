-- product 2번 이미지 수정 및 추가 스크립트

-- 1. imageId 4번 썸네일 URL 수정 (product 2번)
UPDATE image
SET image_url = 'https://images.unsplash.com/photo-1460353581641-37baddab0fa2?w=800',
    updated_at = NOW()
WHERE image_id = 4;

-- 2. product 2번에 추가 이미지 삽입 (일반 이미지 2개 추가)
INSERT INTO image (is_thumbnail, created_at, event_id, product_id, updated_at, image_url)
VALUES
    -- 추가 이미지 1: 스니커즈 측면
    (0, NOW(), NULL, 2, NOW(), 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=800'),
    -- 추가 이미지 2: 스니커즈 상세
    (0, NOW(), NULL, 2, NOW(), 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=800');

-- 수정 및 추가 결과 확인
SELECT image_id, product_id, is_thumbnail,
       SUBSTRING(image_url, 1, 60) as image_url_preview
FROM image
WHERE product_id = 2
ORDER BY is_thumbnail DESC, image_id;
