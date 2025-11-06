-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th1 04, 2025 lúc 03:09 PM
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
-- Cơ sở dữ liệu: `chatapp`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `AccountID` int(11) NOT NULL,
  `UserName` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `UserID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `account`
--

INSERT INTO `account` (`AccountID`, `UserName`, `Password`, `UserID`) VALUES
(1, 'ksskiet', '123', 1),
(2, 'binh', '123', 2),
(3, 'hung', '123', 3);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `group`
--

CREATE TABLE `group` (
  `GroupID` int(11) NOT NULL,
  `GroupName` varchar(255) DEFAULT NULL,
  `CreatorID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `group`
--

INSERT INTO `group` (`GroupID`, `GroupName`, `CreatorID`) VALUES
(1, 'Nhóm chat PBL4', 1),
(2, 'Nhóm chat riêng', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `groupmember`
--

CREATE TABLE `groupmember` (
  `GroupID` int(11) NOT NULL,
  `MemberID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `groupmember`
--

INSERT INTO `groupmember` (`GroupID`, `MemberID`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `groupmessage`
--

CREATE TABLE `groupmessage` (
  `GroupMessageID` int(11) NOT NULL,
  `GroupID` int(11) DEFAULT NULL,
  `SenderID` int(11) DEFAULT NULL,
  `MessageContent` mediumtext DEFAULT NULL,
  `TimeStamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message`
--

CREATE TABLE `message` (
  `MessageID` int(11) NOT NULL,
  `SenderID` int(11) DEFAULT NULL,
  `ReceiverID` int(11) DEFAULT NULL,
  `MessageContent` mediumtext DEFAULT NULL,
  `TimeStamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `message`
--

INSERT INTO `message` (`MessageID`, `SenderID`, `ReceiverID`, `MessageContent`, `TimeStamp`) VALUES
(1, 2, 1, 'hello', '2024-11-24 10:13:45'),
(2, 1, 2, 'hi', '2024-11-24 10:13:48'),
(3, 2, 1, 'hello', '2024-11-24 10:29:03'),
(4, 2, 1, 'hello', '2024-11-24 10:30:03'),
(5, 1, 2, 'hello', '2024-11-24 10:33:08'),
(6, 2, 1, 'hi', '2024-11-24 10:34:50'),
(7, 2, 1, '? sao day', '2024-11-24 10:54:25'),
(8, 1, 2, 'khong co gi', '2024-11-24 10:54:31'),
(9, 1, 2, 'tin nhan 2', '2024-11-24 12:27:43'),
(10, 2, 1, '?', '2024-11-24 12:28:38'),
(11, 1, 3, 'hello', '2024-11-24 12:45:57'),
(12, 1, 2, 'file:/C:/Users/ADMIN/Downloads/4.jpg', '2024-11-25 06:49:53'),
(13, 1, 2, 'file:/C:/Users/ADMIN/Downloads/3.png', '2024-11-25 07:04:04'),
(14, 1, 2, 'file%3A%2FC%3A%2FUsers%2FADMIN%2FDownloads%2F2.png', '2024-11-25 07:12:45'),
(15, 1, 2, '🎁', '2024-11-25 07:28:51'),
(16, 2, 3, 'hello3', '2024-11-26 08:51:11'),
(17, 3, 2, 'hhhhhh', '2024-11-26 08:51:18'),
(18, 2, 3, '😊', '2024-11-26 08:51:24'),
(26, 1, 2, 'da.txt', '2024-12-01 06:32:27'),
(27, 1, 2, 'da.txt', '2024-12-01 06:36:59'),
(28, 1, 2, 'da.txt', '2024-12-01 06:46:55'),
(29, 2, 1, 'message.txt', '2024-12-01 06:48:08'),
(30, 2, 1, 'file%3A%2FC%3A%2FUsers%2FADMIN%2FDownloads%2F111.png', '2024-12-01 14:41:04'),
(31, 1, 2, 'file%3A%2FD%3A%2FPBL4%2Ftest1%2Fsrc%2Fmain%2Fresources%2Fsrc%2Ftest1%2Fimages%2Fplace.jpg', '2024-12-01 16:08:14'),
(32, 2, 1, 'file%3A%2FD%3A%2FPBL4%2Ftest1%2Fsrc%2Fmain%2Fresources%2Fsrc%2Ftest1%2Fimages%2Fplace.jpg', '2024-12-01 16:08:48'),
(33, 2, 1, 'file%3A%2FD%3A%2FPBL4%2Ftest1%2Fsrc%2Fmain%2Fresources%2Fsrc%2Ftest1%2Fimages%2Fplace.jpg', '2024-12-01 16:09:29'),
(34, 1, 2, 'file%3A%2FD%3A%2FPBL4%2Ftest1%2Fsrc%2Fmain%2Fresources%2Fsrc%2Ftest1%2Fimages%2Fplace2.jpg', '2024-12-01 16:09:47'),
(35, 2, 1, '😁', '2024-12-01 16:09:52'),
(36, 2, 1, 'Hello kiệt', '2024-12-01 16:10:09'),
(37, 1, 2, 'Giao trinh.txt', '2024-12-01 16:10:34'),
(38, 1, 2, '😀', '2025-01-04 08:22:11'),
(39, 1, 3, 'hello nhóm riêng', '2025-01-04 12:17:26');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `UserID` int(11) NOT NULL,
  `FullName` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Image` varchar(255) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`UserID`, `FullName`, `Email`, `Image`, `Status`) VALUES
(1, 'Tấn Kiệt', 'ksskiet@gmail.com', NULL, 0),
(2, 'Quang Bình', 'nqbinh1702@gmail.com', NULL, 0),
(3, 'Việt Hưng', 'viethung@gmail.com', NULL, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `voicecall`
--

CREATE TABLE `voicecall` (
  `CallID` int(11) NOT NULL,
  `CallerID` int(11) DEFAULT NULL,
  `ReceiveID` int(11) DEFAULT NULL,
  `TimeStart` time DEFAULT NULL,
  `TimeEnd` time DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`AccountID`),
  ADD UNIQUE KEY `AccountID` (`AccountID`),
  ADD KEY `UserID` (`UserID`);

--
-- Chỉ mục cho bảng `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`GroupID`),
  ADD UNIQUE KEY `GroupID` (`GroupID`),
  ADD KEY `creator_fk` (`CreatorID`);

--
-- Chỉ mục cho bảng `groupmember`
--
ALTER TABLE `groupmember`
  ADD PRIMARY KEY (`GroupID`,`MemberID`),
  ADD KEY `MemberID` (`MemberID`);

--
-- Chỉ mục cho bảng `groupmessage`
--
ALTER TABLE `groupmessage`
  ADD PRIMARY KEY (`GroupMessageID`),
  ADD UNIQUE KEY `GroupMessageID` (`GroupMessageID`),
  ADD KEY `GroupID` (`GroupID`);

--
-- Chỉ mục cho bảng `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`MessageID`),
  ADD UNIQUE KEY `MessageID` (`MessageID`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `UserID` (`UserID`);

--
-- Chỉ mục cho bảng `voicecall`
--
ALTER TABLE `voicecall`
  ADD PRIMARY KEY (`CallID`),
  ADD UNIQUE KEY `CallID` (`CallID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `account`
--
ALTER TABLE `account`
  MODIFY `AccountID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `group`
--
ALTER TABLE `group`
  MODIFY `GroupID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `groupmember`
--
ALTER TABLE `groupmember`
  MODIFY `GroupID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `groupmessage`
--
ALTER TABLE `groupmessage`
  MODIFY `GroupMessageID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `message`
--
ALTER TABLE `message`
  MODIFY `MessageID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `voicecall`
--
ALTER TABLE `voicecall`
  MODIFY `CallID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `group`
--
ALTER TABLE `group`
  ADD CONSTRAINT `creator_fk` FOREIGN KEY (`CreatorID`) REFERENCES `user` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `groupmember`
--
ALTER TABLE `groupmember`
  ADD CONSTRAINT `groupmember_ibfk_1` FOREIGN KEY (`GroupID`) REFERENCES `group` (`GroupID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `groupmember_ibfk_2` FOREIGN KEY (`MemberID`) REFERENCES `user` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `groupmessage`
--
ALTER TABLE `groupmessage`
  ADD CONSTRAINT `groupmessage_ibfk_1` FOREIGN KEY (`GroupID`) REFERENCES `group` (`GroupID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
