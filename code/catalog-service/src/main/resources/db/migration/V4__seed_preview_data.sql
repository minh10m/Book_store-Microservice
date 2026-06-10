UPDATE products 
SET preview_text = 'Đây là nội dung đọc thử trang đầu tiên của sách. Ở chương một, tác giả mở đầu bằng một câu chuyện hấp dẫn dẫn dắt người đọc vào thế giới của nhân vật chính. "Trong một buổi chiều mùa thu đầy gió, khi những chiếc lá vàng rơi lả tả trên phố, tôi nhận ra cuộc đời mình sắp bước sang một trang hoàn toàn mới..."'
WHERE preview_text IS NULL;
