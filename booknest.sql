-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 27, 2025 at 12:46 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `booknest`
--

-- --------------------------------------------------------

--
-- Table structure for table `author`
--

CREATE TABLE `author` (
  `authorID` int(11) NOT NULL,
  `author_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `author`
--

INSERT INTO `author` (`authorID`, `author_name`) VALUES
(1, 'J.K. Rowling'),
(2, 'George R.R. Martin'),
(3, 'Stephen King'),
(4, 'Agatha Christie'),
(5, 'J.R.R. Tolkien'),
(6, 'Mark Twain'),
(7, 'Ernest Hemingway'),
(8, 'Jane Austen'),
(9, 'Charles Dickens'),
(10, 'Isaac Asimov');

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `bookID` int(11) NOT NULL,
  `book_title` varchar(200) NOT NULL,
  `isbn` varchar(17) NOT NULL,
  `publication_date` date NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `description` text NOT NULL,
  `stock_quantity` int(20) NOT NULL,
  `page_count` int(15) NOT NULL,
  `book_img_url` varchar(512) NOT NULL DEFAULT 'sampleurl',
  `publisherID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`bookID`, `book_title`, `isbn`, `publication_date`, `price`, `description`, `stock_quantity`, `page_count`, `book_img_url`, `publisherID`) VALUES
(39, 'Harry Potter and the Philosopher', '1234', '2025-05-27', 10.00, 'Magical Fantasy Book', 1, 500, 'resources/images/UploadedProfilePicture/philosopher\'s_stone.jpg', 1),
(42, 'Harry Potter and The Prisoner of Azkaban', '1234', '2025-05-29', 1000.00, 'Sirrus Black escapes from the prison of Azkaban.', 2, 750, 'resources/images/UploadedProfilePicture/azkaban.jpg', 8),
(43, 'A Song of Ice and Fire ', '1234', '2025-05-28', 1000.00, 'A Feast for Crows is the fourth of seven planned novels in the epic fantasy series A Song of Ice and Fire by American author George R. R. Martin. The novel was first published in the United Kingdom on October 17, 2005, with a United States edition following on November 8, 2005.', 5, 1200, 'resources/images/UploadedProfilePicture/a_feash_of_crows.jpg', 6),
(44, 'IT', '1234', '2025-05-29', 1000.00, 'Seven helpless and bullied children are forced to face their worst nightmares when Pennywise, a shape-shifting clown, reappears. The clown, an ancient evil torments children before feeding on them.', 10, 100, 'resources/images/UploadedProfilePicture/IT.jpg', 4),
(45, 'And Then There Were None', '123', '2025-05-22', 1000.00, 'Ten strangers accept an offer to spend some time on an isolated island. However, things get complicated when a few of them start to die and they realise that the killer is one among them.', 3, 1000, 'resources/images/UploadedProfilePicture/And_Then_There_Were_None_US.jpg', 5),
(46, 'Abc Murder', '2345', '2025-05-29', 1000.00, 'There’s a serial killer on the loose, working his way through the alphabet and the whole country is in a state of panic.\r\n\r\nA is for Mrs. Ascher in Andover, B is for Betty Barnard in Bexhill, C is for Sir Carmichael Clarke in Churston. With each murder, the killer is getting more confident—but leaving a trail of deliberate clues to taunt the proud Hercule Poirot might just prove to be the first, and fatal, mistake.', 11, 1000, 'resources/images/UploadedProfilePicture/abc_murders.jpg', 8),
(50, 'The Holy Bible 1', '1234', '2025-05-21', 100.00, 'hi', 12, 1000, 'resources/images/UploadedProfilePicture/bible.jpg', 6),
(52, 'The Godfather', '1234', '2025-05-20', 1000.00, 'The Godfather is a crime novel by Italian American author Mario Puzo. Originally published on 10 March 1969 by G. P. Putnam\'s Sons, the novel details the story of a fictional Mafia family in New York City and Long Island, headed by Vito Corleone: the Godfather', 50, 500, 'resources/images/UploadedProfilePicture/godfather.jpg', 6),
(56, 'The Fault in our stars', '1234', '2025-05-23', 1000.00, 'The Fault in Our Stars is a novel by John Green. It is his fourth solo novel, and sixth novel overall. It was published on January 10, 2012.', 12, 100, 'resources/images/UploadedProfilePicture/The_Fault_in_Our_Stars.jpg', 1),
(59, 'The brother Karamazov', '1234', '2025-05-29', 1000.00, 'The Brothers Karamazov is a murder mystery, a courtroom drama, and an exploration of erotic rivalry in a series of triangular love affairs involving Karamazov and his three sons - the impulsive and sensual Dmitri; the coldly rational Ivan; and the healthy young novice Alyosha. Through the gripping events of their story, Dostoevsky portrays the social and spiritual strivings in what was both a golden age and a tragic turning point in Russian history. ', 12, 1200, 'resources/images/UploadedProfilePicture/brotherkaramazov.jpg', 1),
(60, 'Fire and Blood v', '1234', '2025-05-24', 1000.01, 'Fire & Blood is a fantasy book by American writer George R. R. Martin and illustrated by Doug Wheatley. It tells the history of House Targaryen, the dynasty that ruled the Seven Kingdoms of Westeros in the backstory of his series A Song of Ice and Fire.', 10, 1000, 'resources/images/UploadedProfilePicture/fire_and_blood.jpg', 4),
(63, 'The hobbit', '123', '2025-05-22', 1200.00, 'The Hobbit is set in Middle-earth and follows home-loving Bilbo Baggins, the titular hobbit who joins the wizard Gandalf and the thirteen dwarves of Thorin\'s Company on a quest to reclaim the dwarves\' home and treasure from the dragon Smaug.', 13, 1200, 'resources/images/UploadedProfilePicture/hobbit.jpg', 9);

-- --------------------------------------------------------

--
-- Table structure for table `book_author`
--

CREATE TABLE `book_author` (
  `bookID` int(11) NOT NULL,
  `authorID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `book_author`
--

INSERT INTO `book_author` (`bookID`, `authorID`) VALUES
(42, 1),
(44, 3),
(45, 4),
(43, 2),
(39, 2),
(46, 4),
(52, 8),
(56, 1),
(59, 1),
(50, 4),
(60, 9),
(63, 7);

-- --------------------------------------------------------

--
-- Table structure for table `book_categories`
--

CREATE TABLE `book_categories` (
  `bookID` int(11) NOT NULL,
  `categoryID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `book_categories`
--

INSERT INTO `book_categories` (`bookID`, `categoryID`) VALUES
(42, 1),
(44, 6),
(45, 1),
(43, 1),
(39, 1),
(46, 1),
(52, 1),
(56, 1),
(59, 1),
(50, 1),
(60, 1),
(63, 1);

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `cartID` int(11) NOT NULL,
  `created_date` date NOT NULL,
  `updated_date` date NOT NULL,
  `userID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `cart_item`
--

CREATE TABLE `cart_item` (
  `cart_itemID` int(11) NOT NULL,
  `bookID` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1 COMMENT 'Number of this book in the cart',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Timestamp of last addition or quantity update',
  `userID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cart_item`
--

INSERT INTO `cart_item` (`cart_itemID`, `bookID`, `quantity`, `updated_at`, `userID`) VALUES
(52, 39, 1, '2025-05-19 17:12:51', 31);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `categoryID` int(11) NOT NULL,
  `category_name` varchar(50) NOT NULL,
  `category_description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`categoryID`, `category_name`, `category_description`) VALUES
(1, 'Fiction', 'Books that contain fictional stories'),
(2, 'Non-Fiction', 'Books based on real events and facts'),
(3, 'Science', 'Books related to scientific topics'),
(4, 'History', 'Books covering historical events and figures'),
(5, 'Biography', 'Books about the life of a person'),
(6, 'Fantasy', 'Books with fantastical elements and worlds'),
(7, 'Self-Help', 'Books to help with personal growth'),
(8, 'Technology', 'Books about technology and advancements'),
(9, 'Business', 'Books focused on business strategies and studies'),
(10, 'Education', 'Books used for educational purposes');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `orderID` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `order_date` date NOT NULL,
  `shipping_address` varchar(255) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `order_status` enum('completed','cancelled','in progress') NOT NULL DEFAULT 'in progress'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`orderID`, `userID`, `order_date`, `shipping_address`, `total_amount`, `order_status`) VALUES
(1, 24, '2025-05-01', '123 Main Street, Anytown, CA 12345', 199.99, 'completed'),
(2, 28, '2025-05-10', 'Custnoble, Nepal, Phone: 9841325984', 110.00, 'in progress'),
(3, 28, '2025-05-10', 'thimi, ktm, bagmati 123', 110.00, 'in progress'),
(4, 28, '2025-05-10', 'thimi, ktm, three 12344', 1130.00, 'completed'),
(5, 28, '2025-05-10', 'tinkune, ktm, bagmati 1', 4100.00, 'completed'),
(6, 28, '2025-05-10', 'tinkune, 132, sdf 12344', 3100.00, 'completed'),
(7, 28, '2025-05-10', 'Thimi, Ktm, Bagmati 1234', 5100.00, 'completed'),
(8, 28, '2025-05-11', 'Thimi, Ktm, Bagmati 1234', 2100.00, 'in progress'),
(9, 28, '2025-05-13', 'Koteshwor, Ktm, Bagmati 123', 3100.00, 'in progress'),
(10, 28, '2025-05-19', 'Thimi, B, Bagmati 123', 5100.00, 'in progress'),
(11, 28, '2025-05-19', 'KTm, Bhaktapur, B 123', 1100.00, 'completed'),
(12, 28, '2025-05-21', 'Ktm, Test, Bagmati 123', 1200.00, 'in progress'),
(13, 28, '2025-05-21', 'KTm, Bhaktapur, bagmati 123', 2100.00, 'in progress'),
(14, 28, '2025-05-21', 'KTm, Bhaktapur, bagmati 123', 2100.00, 'in progress');

-- --------------------------------------------------------

--
-- Table structure for table `order_book`
--

CREATE TABLE `order_book` (
  `bookID` int(11) NOT NULL,
  `orderID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_book`
--

INSERT INTO `order_book` (`bookID`, `orderID`) VALUES
(39, 2),
(39, 3),
(39, 4),
(43, 4),
(45, 5),
(44, 5),
(43, 5),
(42, 5),
(45, 6),
(43, 6),
(42, 6),
(46, 7),
(44, 7),
(42, 7),
(42, 8),
(44, 8),
(46, 9),
(43, 9),
(46, 10),
(43, 10),
(45, 10),
(44, 10),
(45, 11),
(50, 12),
(44, 12),
(56, 13),
(46, 13),
(42, 14),
(45, 14);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `paymentID` int(11) NOT NULL,
  `orderID` int(11) NOT NULL,
  `payment_date` date NOT NULL,
  `payment_amount` decimal(10,2) NOT NULL,
  `payment_method` varchar(20) NOT NULL,
  `payment_status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`paymentID`, `orderID`, `payment_date`, `payment_amount`, `payment_method`, `payment_status`) VALUES
(1, 2, '2025-05-10', 110.00, 'Credit Card', 'Completed'),
(2, 7, '2025-05-10', 5100.00, 'Cash on Delivery', 'Completed'),
(3, 8, '2025-05-11', 2100.00, 'Cash on Delivery', 'Completed'),
(4, 9, '2025-05-13', 3100.00, 'Cash on Delivery', 'Completed'),
(5, 10, '2025-05-19', 5100.00, 'Cash on Delivery', 'Completed'),
(6, 11, '2025-05-19', 1100.00, 'Cash on Delivery', 'Completed'),
(7, 14, '2025-05-21', 2100.00, 'Cash on Delivery', 'Completed');

-- --------------------------------------------------------

--
-- Table structure for table `publisher`
--

CREATE TABLE `publisher` (
  `publisherID` int(11) NOT NULL,
  `publisher_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `publisher`
--

INSERT INTO `publisher` (`publisherID`, `publisher_name`) VALUES
(1, 'Penguin Random House'),
(2, 'HarperCollins'),
(3, 'Simon & Schuster'),
(4, 'Hachette Book Group'),
(5, 'Macmillan Publishers'),
(6, 'Scholastic'),
(7, 'Cengage Learning'),
(8, 'Wiley'),
(9, 'Springer Nature'),
(10, 'Oxford University Press');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `roleID` int(11) NOT NULL,
  `user_role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`roleID`, `user_role`) VALUES
(1, 'User'),
(2, 'Admin');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userID` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone_number` varchar(10) NOT NULL,
  `address` varchar(50) NOT NULL,
  `user_img_url` varchar(512) NOT NULL DEFAULT 'sample',
  `roleID` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userID`, `first_name`, `last_name`, `user_name`, `password`, `email`, `phone_number`, `address`, `user_img_url`, `roleID`) VALUES
(24, 'Kritan', 'Nepal', 'Nova1', '8EtiYlc/TF394xt4CG57lA==:g3jEPjsYo+Ubc8xtbIlrFijjakqM8G2RcyfDQuNFm30=', 'kritan@gmail.com', '9812345678', 'Bhaktapur', 'resources/images/UploadedProfilePicture/insta.png', 1),
(27, 'Admin', 'Nepal', 'admin1', '6EPI49BFkoIe7nHll5i2dg==:uhlK6pkmhomQzY2pwSNTlozATs5Rzp9q2bW5D3dHPVE=', 'noblenepal1@gmail.com', '9812345670', 'thimi', 'resources/images/UploadedProfilePicture/coco.jpg', 2),
(28, 'Noble', 'Nepa', 'Custnoble', '3dlBIptyx0MJHvhopFjfmw==:/Rvs7yzDSWIBvnHgDCRevGVR+mCzuto+ONXut+Gi11g=', 'cust1@gmail.com', '9812345670', 'Bhaktapur', 'resources/images/UploadedProfilePicture/coco.jpg', 1),
(31, 'syam', 'prasad', 'syam23', 'atKMrqFCtu1wSbeoE1Gl0w==:akwhJl6b8Epr63seQ7Ox2aGKMlyUqbIMvuJrGKlpOa4=', 'syam@gmail.com', '9812345678', 'KTM', 'sample', 1),
(32, 'NewUser', 'Nepal', 'New1', '7de7WbFcFKvz9EroyKybiw==:3KVqoHp9TDiWrFbzpEjMX/wcbwIrvut11fW2kKEp+PU=', 'new@gmail.com', '9812345678', 'Bhaktapur', 'sample', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `author`
--
ALTER TABLE `author`
  ADD PRIMARY KEY (`authorID`);

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`bookID`),
  ADD KEY `book_publisher_fk` (`publisherID`);

--
-- Indexes for table `book_author`
--
ALTER TABLE `book_author`
  ADD KEY `bookID` (`bookID`),
  ADD KEY `authorID` (`authorID`);

--
-- Indexes for table `book_categories`
--
ALTER TABLE `book_categories`
  ADD KEY `bookID` (`bookID`),
  ADD KEY `categoryID` (`categoryID`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`cartID`),
  ADD UNIQUE KEY `userID` (`cartID`,`created_date`) USING BTREE,
  ADD KEY `user_cart_fk` (`userID`);

--
-- Indexes for table `cart_item`
--
ALTER TABLE `cart_item`
  ADD PRIMARY KEY (`cart_itemID`),
  ADD UNIQUE KEY `uq_user_book` (`userID`,`bookID`),
  ADD KEY `bookID` (`bookID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`categoryID`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`orderID`),
  ADD KEY `user_order_fk` (`userID`);

--
-- Indexes for table `order_book`
--
ALTER TABLE `order_book`
  ADD KEY `bookID` (`bookID`),
  ADD KEY `orderID` (`orderID`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`paymentID`),
  ADD KEY `payment_order_fk` (`orderID`);

--
-- Indexes for table `publisher`
--
ALTER TABLE `publisher`
  ADD PRIMARY KEY (`publisherID`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`roleID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`),
  ADD KEY `user_role_fk` (`roleID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `author`
--
ALTER TABLE `author`
  MODIFY `authorID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `bookID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `cartID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `cart_item`
--
ALTER TABLE `cart_item`
  MODIFY `cart_itemID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `categoryID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `orderID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `paymentID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `publisher`
--
ALTER TABLE `publisher`
  MODIFY `publisherID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `roleID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `book_publisher_fk` FOREIGN KEY (`publisherID`) REFERENCES `publisher` (`publisherID`);

--
-- Constraints for table `book_author`
--
ALTER TABLE `book_author`
  ADD CONSTRAINT `book_author_ibfk_1` FOREIGN KEY (`bookID`) REFERENCES `book` (`bookID`),
  ADD CONSTRAINT `book_author_ibfk_2` FOREIGN KEY (`authorID`) REFERENCES `author` (`authorID`);

--
-- Constraints for table `book_categories`
--
ALTER TABLE `book_categories`
  ADD CONSTRAINT `book_categories_ibfk_1` FOREIGN KEY (`bookID`) REFERENCES `book` (`bookID`),
  ADD CONSTRAINT `book_categories_ibfk_2` FOREIGN KEY (`categoryID`) REFERENCES `categories` (`categoryID`);

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `user_cart_fk` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`);

--
-- Constraints for table `cart_item`
--
ALTER TABLE `cart_item`
  ADD CONSTRAINT `cart_item_ibfk_1` FOREIGN KEY (`bookID`) REFERENCES `book` (`bookID`),
  ADD CONSTRAINT `cart_item_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `user_order_fk` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`);

--
-- Constraints for table `order_book`
--
ALTER TABLE `order_book`
  ADD CONSTRAINT `order_book_ibfk_1` FOREIGN KEY (`bookID`) REFERENCES `book` (`bookID`),
  ADD CONSTRAINT `order_book_ibfk_2` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderID`);

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_order_fk` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderID`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_role_fk` FOREIGN KEY (`roleID`) REFERENCES `role` (`roleID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
