-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 24, 2025 at 10:03 AM
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
-- Database: `draft`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_milestone` (IN `student_id` INT, IN `username` VARCHAR(50), IN `milestone_date` DATE, IN `milestone_count` INT)   BEGIN
    INSERT INTO milestone (student_id, username, date, milestone_count)
    VALUES (student_id, username, milestone_date, milestone_count);
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `journal`
--

CREATE TABLE `journal` (
  `id` int(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `entry` longtext NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mentees`
--

CREATE TABLE `mentees` (
  `student_id` int(20) NOT NULL,
  `mentor_id` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mentees`
--

INSERT INTO `mentees` (`student_id`, `mentor_id`) VALUES
(1, 2),
(2, 4);

-- --------------------------------------------------------

--
-- Table structure for table `mentors`
--

CREATE TABLE `mentors` (
  `id` int(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` bigint(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mentors`
--

INSERT INTO `mentors` (`id`, `username`, `password`) VALUES
(1, 'mentor1', 9822048),
(2, 'mentor2', 9827142),
(3, 'mentor3', 9443442712),
(4, 'mentor4', 304216940),
(5, 'mentor5', 304211192);

-- --------------------------------------------------------

--
-- Table structure for table `mentor_1`
--

CREATE TABLE `mentor_1` (
  `sprintID` int(20) NOT NULL,
  `subject` varchar(30) NOT NULL,
  `sprint_name` varchar(30) NOT NULL,
  `duration` int(20) NOT NULL,
  `notes` longblob DEFAULT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mentor_2`
--

CREATE TABLE `mentor_2` (
  `sprintID` int(20) NOT NULL,
  `subject` varchar(30) NOT NULL,
  `sprint_name` varchar(30) NOT NULL,
  `duration` int(20) NOT NULL,
  `notes` longblob DEFAULT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mentor_2`
--

INSERT INTO `mentor_2` (`sprintID`, `subject`, `sprint_name`, `duration`, `notes`, `date`) VALUES
(1, 'java', 'oops', 30, 0x74686973206973206f6f7073206e6f7465732e2e0d0a4f4f50532072656665727320746f204f626a656374204f7269656e7465642050726f6772616d6d696e67, '2025-08-23');

-- --------------------------------------------------------

--
-- Table structure for table `mentor_3`
--

CREATE TABLE `mentor_3` (
  `sprintID` int(20) NOT NULL,
  `subject` varchar(30) NOT NULL,
  `sprint_name` varchar(30) NOT NULL,
  `duration` int(20) NOT NULL,
  `notes` longblob DEFAULT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mentor_4`
--

CREATE TABLE `mentor_4` (
  `sprintID` int(20) NOT NULL,
  `subject` varchar(30) NOT NULL,
  `sprint_name` varchar(30) NOT NULL,
  `duration` int(20) NOT NULL,
  `notes` longblob DEFAULT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mentor_5`
--

CREATE TABLE `mentor_5` (
  `sprintID` int(20) NOT NULL,
  `subject` varchar(30) NOT NULL,
  `sprint_name` varchar(30) NOT NULL,
  `duration` int(20) NOT NULL,
  `notes` longblob DEFAULT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `milestone`
--

CREATE TABLE `milestone` (
  `student_id` int(30) NOT NULL,
  `username` varchar(50) NOT NULL,
  `date` date NOT NULL,
  `milestone_count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `std_abc123`
--

CREATE TABLE `std_abc123` (
  `id` int(11) NOT NULL,
  `sprint_id` int(11) DEFAULT NULL,
  `subject` varchar(50) DEFAULT NULL,
  `sprint_name` varchar(100) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `focus_score` int(11) DEFAULT NULL,
  `distraction_reason` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `std_abc123`
--

INSERT INTO `std_abc123` (`id`, `sprint_id`, `subject`, `sprint_name`, `duration`, `start_time`, `focus_score`, `distraction_reason`, `date`) VALUES
(1, 1, 'java', 'oops', 30, '23:52:23', NULL, NULL, '2025-08-23');

-- --------------------------------------------------------

--
-- Table structure for table `std_jiya123`
--

CREATE TABLE `std_jiya123` (
  `id` int(11) NOT NULL,
  `sprint_id` int(11) DEFAULT NULL,
  `subject` varchar(50) DEFAULT NULL,
  `sprint_name` varchar(100) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `focus_score` int(11) DEFAULT NULL,
  `distraction_reason` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `student_id` int(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` int(30) NOT NULL,
  `mentor_id` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`student_id`, `username`, `password`, `mentor_id`) VALUES
(1, 'abc123', 304891, 2),
(2, 'jiya123', 9727246, 4);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `journal`
--
ALTER TABLE `journal`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mentees`
--
ALTER TABLE `mentees`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `mentors`
--
ALTER TABLE `mentors`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `mentor_1`
--
ALTER TABLE `mentor_1`
  ADD PRIMARY KEY (`sprintID`);

--
-- Indexes for table `mentor_2`
--
ALTER TABLE `mentor_2`
  ADD PRIMARY KEY (`sprintID`);

--
-- Indexes for table `mentor_3`
--
ALTER TABLE `mentor_3`
  ADD PRIMARY KEY (`sprintID`);

--
-- Indexes for table `mentor_4`
--
ALTER TABLE `mentor_4`
  ADD PRIMARY KEY (`sprintID`);

--
-- Indexes for table `mentor_5`
--
ALTER TABLE `mentor_5`
  ADD PRIMARY KEY (`sprintID`);

--
-- Indexes for table `milestone`
--
ALTER TABLE `milestone`
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `std_abc123`
--
ALTER TABLE `std_abc123`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `std_jiya123`
--
ALTER TABLE `std_jiya123`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`student_id`),
  ADD UNIQUE KEY `username ` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `journal`
--
ALTER TABLE `journal`
  MODIFY `id` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `mentees`
--
ALTER TABLE `mentees`
  MODIFY `student_id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `mentors`
--
ALTER TABLE `mentors`
  MODIFY `id` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `mentor_1`
--
ALTER TABLE `mentor_1`
  MODIFY `sprintID` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mentor_2`
--
ALTER TABLE `mentor_2`
  MODIFY `sprintID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `mentor_3`
--
ALTER TABLE `mentor_3`
  MODIFY `sprintID` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mentor_4`
--
ALTER TABLE `mentor_4`
  MODIFY `sprintID` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mentor_5`
--
ALTER TABLE `mentor_5`
  MODIFY `sprintID` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `std_abc123`
--
ALTER TABLE `std_abc123`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `std_jiya123`
--
ALTER TABLE `std_jiya123`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `student_id` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `milestone`
--
ALTER TABLE `milestone`
  ADD CONSTRAINT `milestone_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
