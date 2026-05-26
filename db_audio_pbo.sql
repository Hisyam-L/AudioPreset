-- phpMyAdmin SQL Dump
-- version 5.2.3-2.fc44
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 26, 2026 at 01:46 AM
-- Server version: 11.8.6-MariaDB
-- PHP Version: 8.5.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_audio_pbo`
--

-- --------------------------------------------------------

--
-- Table structure for table `preset_audio`
--

CREATE TABLE `preset_audio` (
  `id` int(11) NOT NULL,
  `nama_preset` varchar(100) NOT NULL,
  `hz_115` float DEFAULT 0,
  `hz_250` float DEFAULT 0,
  `hz_450` float DEFAULT 0,
  `hz_13k` float DEFAULT 0,
  `tipe_perangkat` varchar(50) DEFAULT NULL,
  `detail_perangkat` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Dumping data for table `preset_audio`
--

INSERT INTO `preset_audio` (`id`, `nama_preset`, `hz_115`, `hz_250`, `hz_450`, `hz_13k`, `tipe_perangkat`, `detail_perangkat`) VALUES
(23, 'Mendat Mendut', 20, -20, -20, -20, 'IEM', 'Planar'),
(24, 'Kerasukan Hi_RES', -11, 11, -5, -11, 'IEM', 'Dynamic Driver'),
(25, 'Hi-res', 20, 11, -5, -11, 'IEM', 'Dynamic Driver');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `preset_audio`
--
ALTER TABLE `preset_audio`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `preset_audio`
--
ALTER TABLE `preset_audio`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
