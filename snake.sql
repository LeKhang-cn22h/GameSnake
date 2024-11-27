-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 27, 2024 lúc 04:07 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `snake`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `gamemode`
--

CREATE TABLE `gamemode` (
  `idGameMode` int(100) NOT NULL,
  `NameGameMode` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `gamemode`
--

INSERT INTO `gamemode` (`idGameMode`, `NameGameMode`) VALUES
(1, 'Cổ Điển'),
(2, 'Tự Do'),
(3, 'Chướng Ngại'),
(4, 'Thách Thức');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `questions`
--

CREATE TABLE `questions` (
  `id` int(11) NOT NULL,
  `question` text NOT NULL,
  `option1` varchar(255) NOT NULL,
  `option2` varchar(255) NOT NULL,
  `option3` varchar(255) NOT NULL,
  `option4` varchar(255) NOT NULL,
  `correct_option` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `questions`
--

INSERT INTO `questions` (`id`, `question`, `option1`, `option2`, `option3`, `option4`, `correct_option`) VALUES
(1, 'Thủ đô của Pháp là gì?', 'Berlin', 'Madrid', 'Paris', 'Rome', 3),
(2, 'Ai là tác giả của \"Romeo và Juliet\"?', 'Charles Dickens', 'William Shakespeare', 'Mark Twain', 'Jane Austen', 2),
(3, 'Hành tinh lớn nhất trong hệ mặt trời là gì?', 'Trái Đất', 'Sao Mộc', 'Sao Thổ', 'Sao Hỏa', 2),
(4, 'Nhiệt độ sôi của nước là bao nhiêu?', '100°C', '0°C', '50°C', '25°C', 1),
(5, 'Ai là người vẽ bức tranh Mona Lisa?', 'Vincent van Gogh', 'Pablo Picasso', 'Leonardo da Vinci', 'Claude Monet', 3),
(6, 'Ký hiệu hóa học của vàng là gì?', 'Au', 'Ag', 'Pb', 'Fe', 1),
(7, 'Có bao nhiêu châu lục trên thế giới?', '5', '6', '7', '8', 3),
(8, 'Động vật có vú lớn nhất thế giới là gì?', 'Voi', 'Cá voi xanh', 'Hươu cao cổ', 'Cá mập trắng', 2),
(9, 'Ai phát hiện ra penicillin?', 'Marie Curie', 'Alexander Fleming', 'Louis Pasteur', 'Thomas Edison', 2),
(10, 'Đơn vị tiền tệ của Nhật Bản là gì?', 'Yên', 'Won', 'Đô la', 'Euro', 1),
(11, 'Số nguyên tố nhỏ nhất là gì?', '0', '1', '2', '3', 3),
(12, 'Ai là người đầu tiên đặt chân lên mặt trăng?', 'Yuri Gagarin', 'Neil Armstrong', 'Buzz Aldrin', 'John Glenn', 2),
(13, 'Nguyên liệu chính để làm guacamole là gì?', 'Cà chua', 'Bơ', 'Hành', 'Ớt', 2),
(14, 'Chất tự nhiên cứng nhất trên Trái Đất là gì?', 'Vàng', 'Kim cương', 'Sắt', 'Thạch anh', 2),
(15, 'Ai được biết đến là \"Cha đẻ của Hình học\"?', 'Euclid', 'Pythagoras', 'Archimedes', 'Newton', 1),
(16, 'Đại dương lớn nhất trên Trái Đất là gì?', 'Đại Tây Dương', 'Ấn Độ Dương', 'Bắc Băng Dương', 'Thái Bình Dương', 4),
(17, 'Thủ đô của Úc là gì?', 'Sydney', 'Canberra', 'Melbourne', 'Brisbane', 2),
(18, 'Ai phát minh ra điện thoại?', 'Alexander Graham Bell', 'Thomas Edison', 'Nikola Tesla', 'Guglielmo Marconi', 1),
(19, 'Ngôn ngữ chính được nói ở Brazil là gì?', 'Tiếng Tây Ban Nha', 'Tiếng Bồ Đào Nha', 'Tiếng Anh', 'Tiếng Pháp', 2),
(20, 'Sa mạc lớn nhất thế giới là gì?', 'Sahara', 'Sa mạc Ả Rập', 'Sa mạc Gobi', 'Sa mạc Nam Cực', 1),
(21, 'Thủ đô của Ý là gì?', 'Venice', 'Florence', 'Rome', 'Milan', 3),
(22, 'Ai là tác giả của \"Cuộc Odyssey\"?', 'Homer', 'Virgil', 'Dante', 'Sophocles', 1),
(23, 'Nhiệt độ đóng băng của nước là bao nhiêu?', '0°C', '32°F', '100°F', '25°C', 1),
(24, 'Cơ quan lớn nhất trong cơ thể người là gì?', 'Da', 'Tim', 'Gan', 'Phổi', 1),
(25, 'Ai là người sáng lập Facebook?', 'Steve Jobs', 'Mark Zuckerberg', 'Bill Gates', 'Larry Page', 2),
(26, 'Hệ thống hành tinh của chúng ta có bao nhiêu hành tinh?', '8', '9', '7', '6', 1),
(27, 'Ngọn núi cao nhất thế giới là gì?', 'K2', 'Everest', 'Kilimanjaro', 'Mont Blanc', 2),
(28, 'Ai là nhà văn nổi tiếng với tác phẩm \"1984\"?', 'George Orwell', 'Aldous Huxley', 'Ray Bradbury', 'F. Scott Fitzgerald', 1),
(29, 'Màu sắc của lá cây là gì?', 'Đỏ', 'Xanh', 'Vàng', 'Nâu', 2),
(30, 'Ai là người phát minh ra bóng đèn?', 'Nikola Tesla', 'Thomas Edison', 'Albert Einstein', 'Isaac Newton', 2),
(31, 'Thời gian một năm có bao nhiêu tháng?', '10', '11', '12', '13', 3),
(32, 'Ngôn ngữ chính của Trung Quốc là gì?', 'Tiếng Anh', 'Tiếng Trung Quốc', 'Tiếng Tây Ban Nha', 'Tiếng Pháp', 2),
(33, 'Ai là người sáng lập Microsoft?', 'Steve Jobs', 'Bill Gates', 'Mark Zuckerberg', 'Larry Page', 2),
(34, 'Hành tinh nào gần Mặt Trời nhất?', 'Trái Đất', 'Sao Kim', 'Sao Hỏa', 'Sao Thủy', 4),
(35, 'Công thức hóa học của nước là gì?', 'H2O', 'CO2', 'O2', 'NaCl', 1),
(36, 'Ai là người đầu tiên phát hiện ra điện?', 'Benjamin Franklin', 'Thomas Edison', 'Nikola Tesla', 'Michael Faraday', 1),
(37, 'Số lượng châu lục trên thế giới là bao nhiêu?', '5', '6', '7', '8', 3),
(38, 'Ai là người sáng lập Apple?', 'Steve Jobs', 'Bill Gates', 'Mark Zuckerberg', 'Larry Page', 1),
(39, 'Thủ đô của Canada là gì?', 'Toronto', 'Vancouver', 'Ottawa', 'Montreal', 3),
(40, 'Ai là người phát minh ra máy bay?', 'Wright Brothers', 'Thomas Edison', 'Henry Ford', 'Charles Lindbergh', 1),
(41, 'Màu sắc của bầu trời vào ban ngày là gì?', 'Đỏ', 'Xanh', 'Vàng', 'Tím', 2),
(42, 'Ai là người sáng lập Google?', 'Larry Page', 'Sergey Brin', 'Steve Jobs', 'Bill Gates', 1),
(43, 'Hành tinh nào có vành đai?', 'Sao Hỏa', 'Sao Mộc', 'Sao Kim', 'Sao Thủy', 2),
(44, 'Ai là người phát minh ra internet?', 'Tim Berners-Lee', 'Al Gore', 'Bill Gates', 'Steve Jobs', 1),
(45, 'Thủ đô của Việt Nam là gì?', 'Hà Nội', 'TP.HCM', 'Đà Nẵng', 'Nha Trang', 1),
(46, 'Ai là người sáng lập Amazon?', 'Jeff Bezos', 'Elon Musk', 'Bill Gates', 'Larry Page', 1),
(47, 'Hành tinh nào được gọi là \"Hành tinh Đỏ\"?', 'Sao Kim', 'Sao Hỏa', 'Sao Mộc', 'Sao Thủy', 2),
(48, 'Ai là người phát minh ra máy tính?', 'Charles Babbage', 'Alan Turing', 'Bill Gates', 'Steve Jobs', 1),
(49, 'Thời gian một ngày có bao nhiêu giờ?', '12', '24', '36', '48', 2),
(50, 'Ai là người sáng lập Twitter?', 'Jack Dorsey', 'Mark Zuckerberg', 'Elon Musk', 'Bill Gates', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `score`
--

CREATE TABLE `score` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  `date_achieved` timestamp NOT NULL DEFAULT current_timestamp(),
  `idGameMode` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `score`
--

INSERT INTO `score` (`id`, `user_id`, `score`, `date_achieved`, `idGameMode`) VALUES
(13, 1, 50, '2024-11-12 17:00:00', 1),
(18, 1, 40, '2024-11-12 17:00:00', 1),
(19, 1, 60, '2024-11-12 17:00:00', 1),
(20, 1, 80, '2024-11-12 17:00:00', 1),
(21, 1, 90, '2024-11-12 17:00:00', 1),
(22, 1, 70, '2024-11-12 17:00:00', 1),
(24, 1, 50, '2024-11-16 17:00:00', 2),
(26, 1, 40, '2024-11-16 17:00:00', 1),
(27, 1, 40, '2024-11-16 17:00:00', 1),
(28, 1, 40, '2024-11-16 17:00:00', 1),
(29, 4, 80, '2024-11-25 17:00:00', 3),
(30, 4, 70, '2024-11-25 17:00:00', 4),
(36, 4, 80, '2024-11-25 17:00:00', 4),
(38, 4, 60, '2024-11-25 17:00:00', 4),
(42, 4, 80, '2024-11-25 17:00:00', 4),
(44, 4, 30, '2024-11-25 17:00:00', 4),
(45, 4, 30, '2024-11-25 17:00:00', 4),
(46, 4, 40, '2024-11-25 17:00:00', 4),
(47, 4, 70, '2024-11-25 17:00:00', 4),
(48, 4, 40, '2024-11-25 17:00:00', 4),
(49, 4, 60, '2024-11-25 17:00:00', 4),
(50, 4, 20, '2024-11-25 17:00:00', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, 'testuser', 'password123'),
(2, 'Nguyen Van A', '123456'),
(3, 'Nguyen Van B', '123456'),
(4, 'nobi', '123456');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `gamemode`
--
ALTER TABLE `gamemode`
  ADD PRIMARY KEY (`idGameMode`);

--
-- Chỉ mục cho bảng `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `score`
--
ALTER TABLE `score`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `FK_Score_GameMode` (`idGameMode`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `gamemode`
--
ALTER TABLE `gamemode`
  MODIFY `idGameMode` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `questions`
--
ALTER TABLE `questions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT cho bảng `score`
--
ALTER TABLE `score`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `score`
--
ALTER TABLE `score`
  ADD CONSTRAINT `FK_Score_GameMode` FOREIGN KEY (`idGameMode`) REFERENCES `gamemode` (`idGameMode`),
  ADD CONSTRAINT `score_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
