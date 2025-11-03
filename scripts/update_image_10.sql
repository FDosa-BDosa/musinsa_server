-- imageId 10번의 이미지 URL 수정 (상품 5번 썸네일)
-- 기존 URL이 작동하지 않아 Unsplash의 신발 이미지로 교체

UPDATE image
SET image_url = 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800',
    updated_at = NOW()
WHERE image_id = 10;

-- 수정 결과 확인
SELECT image_id, product_id, is_thumbnail, image_url
FROM image
WHERE product_id = 5
ORDER BY is_thumbnail DESC, image_id;
