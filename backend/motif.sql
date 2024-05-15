-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: motif
-- ------------------------------------------------------
-- Server version	8.0.36-0ubuntu0.23.10.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `notesheets`
--

DROP TABLE IF EXISTS `notesheets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notesheets` (
  `sheetID` int NOT NULL AUTO_INCREMENT,
  `UID` int(10) unsigned zerofill DEFAULT NULL,
  `notes` text,
  PRIMARY KEY (`sheetID`),
  KEY `UID` (`UID`),
  CONSTRAINT `notesheets_ibfk_1` FOREIGN KEY (`UID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notesheets`
--

LOCK TABLES `notesheets` WRITE;
/*!40000 ALTER TABLE `notesheets` DISABLE KEYS */;
INSERT INTO `notesheets` VALUES (2,0000000001,'NOTES NOTES TIME NOTES'),(3,0000000001,'ANOTHER NOTESHEET'),(4,0000000001,'hello'),(5,0000000001,'oh baby, a triple!'),(34,0000000097,'Hi'),(35,0000000097,'Hi'),(36,0000000097,'hello'),(37,0000000097,'hello'),(38,0000000097,'jshssdsdddxvgdddw'),(39,0000000097,'jshssdsdddxvgdddw'),(40,0000000097,'jdjdhhdvssdfgddfdefffdddddddfddfrdddd'),(41,0000000097,'jdjdhhdvssdfgddfdefffdddddddfddfrdddd'),(46,0000000097,'I am fine'),(47,0000000097,'I am fine'),(48,0000000097,'D scale D E F'),(49,0000000097,'D scale D E F'),(50,0000000099,'Test'),(51,0000000099,'Db Bb'),(52,0000000097,'Hi'),(53,0000000097,'Hi'),(54,0000000097,'SANTO SANTO SANTOAb Ab Ab Gb Gb E ( E Gb Ab )E Db Db B A Ab BE Db Db B A Ab B B E ( E Gb Ab )A B Ab Gb E'),(55,0000000097,'SANTO SANTO SANTOAb Ab Ab Gb Gb E ( E Gb Ab )E Db Db B A Ab BE Db Db B A Ab B B E ( E Gb Ab )A B Ab Gb E'),(56,0000000101,'Hello'),(57,0000000101,'Hello'),(58,0000000101,'Notes A B C'),(59,0000000101,'Notes A B C'),(60,0000000101,'Note 3 C D E'),(61,0000000101,'Note 3 C D E'),(62,0000000101,'Note 4 G'),(63,0000000101,'Note 4 G'),(64,0000000101,'LA MESA - Dm SaxIntro:E D E  E EE D E  B B Solo:G F'),(65,0000000101,'LA MESA - Dm SaxIntro:E D E  E EE D E  B B Solo:G F');
/*!40000 ALTER TABLE `notesheets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `ID` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (0000000001,'nathan','motif','nrodriguez1498@gmail.com',NULL,NULL),(0000000002,'test','$2y$10$HWKbImBGh7D3liw/a1MW8uHiaf44LTO7rn/UkBo90BYdZzu/NbVZa','testemail@gmail.com',NULL,NULL),(0000000003,'testSHA','b5d3dd771ad9cd539a0504016c14da9ea4495dea46693ac3a552bef4dd635120','testSHA@gmail.com',NULL,NULL),(0000000008,'registerTest','20d1192ff03fb7a6283c9c77b3125a0ac86daff2f9de05587c84aa5c5c74155a','fakeemail@fake.net','Nathan','6615291498'),(0000000009,'registerTest2','c891c1ee39e65f8eb0ed7523f247933d7c14e3298963cbdb950f4dbe43b4706a','fakeemail2@fake.net','Nathan Rodriguez','6615291498'),(0000000010,'isaiahZM','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8','ishmllx7@gmail.com','Isaiah Malleaux','6615291794'),(0000000015,'mariaxg','1f93d32ec371e9ad4a8d1dc3a4cf28c9ce2c150066e64dd9e4d80fcc02643b2b','mariaxg@gmail.com','Maria Gonz','6612349129'),(0000000018,'abeldiaz','30d70e3a96052e41a30f92b27a0119fd7ac8b5b39c0c098dedbeeba518f63618','abeldiaz@gmail.com','Abel Diaz','6612394234'),(0000000025,'ariaxg','c11f47f6257ced45ab85c6871cf0ac329ceca11c9f86ccb61493d8e8f548e746','ariaxg@gmail.com','aria','6612344603'),(0000000028,'registerTest3','f30b34814b1f858e15e3a8750b714168179f4479e285148d53189ae980e35a62','fakeemail4@fake.net','Nathan','6615291496'),(0000000031,'nate','5a2a558c78d3717db731600c4f354fa1d9c84b556f108091a891f444f1bdec40','nate661@gmail.com','Nahth','6615291485'),(0000000045,'nate43','8f8a663e846b808c0f86cab7313924fb9b9d5e5279088233a13feaa015c83f3c','nate664@gmail.com','Nahgth','6615291485'),(0000000050,'nate662','5a2a558c78d3717db731600c4f354fa1d9c84b556f108091a891f444f1bdec40','nate757@gmail.com','Nahth','6615291482'),(0000000054,'nate665','5a2a558c78d3717db731600c4f354fa1d9c84b556f108091a891f444f1bdec40','nate777@gmail.com','Nahth','6615291482'),(0000000056,'nathan661','1546311130d9edf530cc528a60153efac0bc52988cee4a141db344ae92c74d92','nate7127@gmail.com','Nahth','6615291422'),(0000000083,'testname','d67fdd0c0e917b0c55cc9480fb7257d00ab33cd832cd88e0eefbcf6626265d49','testname@testname@testname.net','testname','6615291498'),(0000000085,'another','ae448ac86c4e8e4dec645729708ef41873ae79c6dff84eff73360989487f08e5','another@another.net','another','6615291498'),(0000000087,'qNewUser','73999637a6f638dca56fe2f32349d7713556905927c24c40c36646fe4d4f594b','qNewUser@gmail.com','Nathan Rodriguez ','6615291498'),(0000000089,'dhfna','35241941169249243a3988c612066f214a0670cdc3fb71925564a0a6911cdae2','shfnf','nathan','661292944'),(0000000091,'slunt','6f128c07c782034a62653fbc45dcb42e5ce7052fe24b25fedc29be384c4fb87f','slunt@email.com','Nathan Rod','4949494884'),(0000000097,'Johnny','d74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1','johnsmith@gmail.com','John Smith','6612223333'),(0000000099,'nate661','d74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1','gmail@gmail.com','Nathan Rodriguez ','6615291498'),(0000000101,'Sam','08fa299aecc0c034e037033e3b0bbfaef26b78c742f16cf88ac3194502d6c394','samueljohnson@gmail.com','Samuel Johnson','6611234567');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-10 23:53:39
