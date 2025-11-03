-- product 6번에 이미지 추가 스크립트
-- 할로윈 한정 스니커즈에 썸네일 1개 + 일반 이미지 2개 추가

INSERT INTO image (is_thumbnail, created_at, event_id, product_id, updated_at, image_url)
VALUES
    -- 썸네일 이미지
    (1, NOW(), NULL, 6, NOW(), 'https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=800'),
    -- 일반 이미지 1
    (0, NOW(), NULL, 6, NOW(), 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=800'),
    -- 일반 이미지 2
    (0, NOW(), NULL, 6, NOW(), 'https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?w=800');

-- 추가 결과 확인
SELECT image_id, product_id, is_thumbnail,
       SUBSTRING(image_url, 1, 60) as image_url_preview
FROM image
WHERE product_id = 6
ORDER BY is_thumbnail DESC, image_id;
