-- MySQL dump 10.13  Distrib 8.0.27, for macos11 (x86_64)
--
-- Host: localhost    Database: gitebleu
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Current Database: `gitebleu`
--

/*!40000 DROP DATABASE IF EXISTS `gitebleu`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `gitebleu` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `gitebleu`;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `clientID` int DEFAULT NULL,
  `bookingType` varchar(200) DEFAULT NULL,
  `roomName` varchar(200) DEFAULT NULL,
  `adult` int DEFAULT NULL,
  `child` int DEFAULT NULL,
  `singleBed` int DEFAULT NULL,
  `doubleBed` int DEFAULT NULL,
  `pricePerDay` int DEFAULT NULL,
  `checkIn` varchar(50) DEFAULT NULL,
  `timeIn` varchar(20) DEFAULT NULL,
  `checkOut` varchar(50) DEFAULT NULL,
  `timeOut` varchar(20) DEFAULT NULL,
  `numberOfStay` int DEFAULT NULL,
  `bound` int DEFAULT NULL,
  `myOption` int DEFAULT NULL,
  `totalAmount` float DEFAULT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `contratStatus` varchar(20) DEFAULT NULL,
  `boundStatus` varchar(20) DEFAULT NULL,
  `advanceStatus` varchar(20) DEFAULT NULL,
  `paid` float DEFAULT NULL,
  `toPay` float DEFAULT NULL,
  `tax` float DEFAULT NULL,
  `baseTax` float DEFAULT NULL,
  `totalWithTax` float DEFAULT NULL,
  `paiementMethod` varchar(200) DEFAULT NULL,
  `contractName` varchar(500) DEFAULT NULL,
  `factureName` varchar(500) DEFAULT NULL,
  `addressProperty` varchar(500) DEFAULT NULL,
  `advanceAmount` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (1,1,'Maison','Le Gite Bleu',2,2,2,1,147,'24-01-22','17h','30-01-22','10h',6,600,110,882,'','terminé','true','true','true',260,742.56,10.56,0.88,1002.56,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 1.pdf','documents/gitebleu/facture/Facture Le Gite Bleu 1.pdf','5 Rue Des Moulin Neufs, 49480 Huillez','260'),(2,1,'Maison','Le Gite Bleu',2,2,2,1,170,'10-07-22','17h','15-07-22','10h',5,600,0,850,'souhaite arrivé plus tot','en cours','true','false','true',300,558.8,8.8,0.88,858.8,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 2.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','220'),(3,1,'Maison','Le Gite Bleu',2,2,1,1,147,'20-01-22','17h','20-01-22','10h',1,600,0,147,NULL,'annulé','false','false','false',0,148.76,1.76,0.88,148.76,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 3.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40'),(4,1,'Maison','Le Gite Bleu',0,0,0,0,147,'20-01-22','17h','21-01-22','10h',1,600,80,147,NULL,'annulé','false','false','false',0,227,0,0.88,227,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 4.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40'),(5,1,'Maison','Le Gite Bleu',2,0,0,2,147,'20-01-22','17h','20-01-22','10h',1,600,80,147,NULL,'annulé','false','false','false',0,228.76,1.76,0.88,228.76,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 5.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40'),(6,1,'Maison','Le Gite Bleu',0,0,0,0,147,'20-01-22','17h','21-01-22','10h',1,600,0,147,NULL,'annulé','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 6.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40'),(7,2,'Maison','Le Gite Bleu',4,2,2,1,147,'20-02-22','17h','26-02-22','10h',6,600,0,882,'','terminé','true','false','true',903.12,0,21.12,0.88,903.12,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 7.pdf','documents/gitebleu/facture/Facture Le Gite Bleu 7.pdf','5 Rue Des Moulin Neufs, 49480 Huillez','260'),(8,2,'Maison','Le Gite Bleu',2,2,0,0,147,'01-09-23','17h','03-09-23','10h',2,600,80,294,'','terminé','true','true','true',377.52,0,3.52,0.88,377.52,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 8.pdf','documents/gitebleu/facture/Facture Le Gite Bleu 8.pdf','5 Rue Des Moulin Neufs, 49480 Huillez','80'),(9,3,'Maison','Le Gite Bleu',2,0,0,2,147,'15-03-22','17h','25-03-22','10h',10,600,50,1470,'','en cours','false','false','true',1093.1,1037.6,17.6,0.88,1537.6,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 9.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','500'),(10,3,'Maison','Le Gite Bleu',2,0,0,2,147,'05-10-23','17h','10-10-23','10h',5,600,105,735,NULL,'annulé','false','false','false',0,848.8,8.8,0.88,848.8,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 10.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','220'),(11,3,'Maison','Gite Vert',2,0,0,1,120,'05-10-23','17h','10-10-23','10h',5,500,105,600,'ajouter siege bébé','en cours','true','false','true',180,533.8,8.8,0.88,713.8,NULL,'documents/gitebleu/contrat/Contrat Gite Vert 11.pdf',NULL,'7 Rue Des Mulin Neufs, 49480 Huillez','200'),(12,4,'Maison','Le Gite Bleu',4,4,4,2,147,'01-04-22','17h','15-04-22','10h',14,600,180,2058,'','terminé','false','false','false',2287.28,0,49.28,0.88,2287.28,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 12.pdf','documents/gitebleu/facture/Facture Le Gite Bleu 12.pdf','5 Rue Des Moulin Neufs, 49480 Huillez','610'),(13,5,'Maison','Le Gite Bleu',4,0,1,3,147,'08-05-22','17h','18-05-22','10h',10,600,0,1470,'','terminé','true','true','true',1505.2,0,35.2,0.88,1505.2,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 13.pdf','documents/gitebleu/facture/Facture Le Gite Bleu 13.pdf','5 Rue Des Moulin Neufs, 49480 Huillez','440'),(14,5,'Maison','Le Gite Bleu',4,0,1,3,147,'18-10-23','17h','28-10-23','10h',10,600,0,1470,NULL,'en cours','false','false','false',0,1505.2,35.2,0.88,1505.2,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 14.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','440'),(15,4,'Maison','Le Gite Bleu',4,2,2,2,147,'15-09-23','17h','30-09-23','10h',15,600,0,2205,'','en cours','true','false','true',850,1407.8,52.8,0.88,2257.8,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 15.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','850'),(16,7,'Maison','Le Gite Bleu',1,0,0,1,147,'11-08-22','17h','12-08-22','10h',1,600,0,147,'','en cours','false','false','true',100,47.88,0.88,0.88,147.88,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 16.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40'),(17,1,'Maison','Le Gite Bleu',2,2,2,1,117,'02-02-22','17h','08-02-22','10h',6,600,140,700,'','terminé','true','false','true',850.56,0,10.56,0.88,850.56,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 17.pdf','documents/gitebleu/facture/Facture Le Gite Bleu 17.pdf','5 Rue Des Moulin Neufs, 49480 Huillez','200'),(18,1,'Maison','Le Gite Bleu',3,2,0,0,121,'20-01-22','16h','29-01-22','12h',9,600,45,1089,'','en cours','true','false','true',300,857.76,23.76,0.88,1157.76,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 18.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','300'),(19,4,'Maison','Le Gite Bleu',1,0,0,1,350,'15-09-22','17h','17-09-22','10h',2,600,0,700,'','en cours','false','false','true',180,521.76,1.76,0.88,701.76,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 19.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','210'),(20,4,'Maison','Le Gite Bleu',1,0,0,0,147,'21-11-22','17h','23-11-22','10h',2,600,0,294,'','en cours','false','false','true',80,215.76,1.76,0.88,295.76,'Espèce','documents/gitebleu/contrat/Contrat Le Gite Bleu 20.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','80'),(21,4,'Maison','Le Gite Bleu',2,2,2,1,144,'22-01-22','12h','27-01-22','12h',5,600,25,720,'','en cours','true','false','true',210,543.8,8.8,0.88,753.8,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 21.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','210'),(22,3,'Maison','Le Gite Bleu',2,2,2,1,167,'27-06-22','17h','03-07-22','10h',6,600,0,1000,'','en cours','true','false','true',260,750.56,10.56,0.88,1010.56,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 22.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','260'),(23,5,'Chambre','Feneu-101',1,0,1,0,20,'27-01-22','17h','29-01-22','10h',2,0,40,40,'','en cours','false','','true',41.6,0,1.6,0.8,41.6,'Espèce','documents/gitebleu/contrat/Contrat Feneu-101 23.pdf',NULL,'8 Rue De La Fontaine Rouillee','10'),(26,4,'Maison','Le Gite Bleu',2,0,0,1,79,'23-01-22','17h','28-01-22','10h',5,600,80,394,'','en cours','true','false','true',100,382.8,8.8,0.88,482.8,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 26.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','100'),(27,5,'Chambre','Feneu-101',2,0,0,0,20,'24-01-22','17h','24-01-22','10h',1,NULL,0,20,NULL,'en cours','false',NULL,'false',0,21.6,1.6,0.8,21.6,NULL,'documents/gitebleu/contrat/Contrat Feneu-101 27.pdf',NULL,'8 Rue De La Fontaine Rouillee','20'),(28,5,'Chambre','Feneu-102',2,0,0,1,19,'24-01-22','17h','25-01-22','10h',1,NULL,0,19,NULL,'en cours','false',NULL,'false',0,20.6,1.6,0.8,20.6,NULL,'documents/gitebleu/contrat/Contrat Feneu-102 28.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480','19'),(29,5,'Chambre','Feneu-102',2,0,0,1,19,'24-01-22','17h','26-01-22','10h',2,NULL,0,38,NULL,'en cours','false',NULL,'false',0,41.2,3.2,0.8,41.2,NULL,'documents/gitebleu/contrat/Contrat Feneu-102 29.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480','20'),(30,5,'Maison','Le Gite Bleu',1,0,1,0,147,'24-01-22','17h','25-01-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147.88,0.88,0.88,147.88,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 30.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40'),(31,5,'Chambre','Feneu-101',1,0,1,0,13,'24-01-22','17h','27-01-22','10h',3,0,0,40,'','en cours','false','','false',42.4,0,2.4,0.8,42.4,'Espèce','documents/gitebleu/contrat/Contrat Feneu-101 31.pdf',NULL,'8 Rue De La Fontaine Rouillee','25'),(32,5,'Dortoir','Feneu-201',1,0,1,0,15,'24-01-22','17h','31-01-22','10h',7,0,35,105,'','en cours','false','','false',0,145.6,5.6,0.8,145.6,NULL,'documents/gitebleu/contrat/Contrat Feneu-201 32.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480 Feneu','30'),(33,4,'Chambre','Feneu-102',1,0,0,1,6,'25-01-22','17h','28-01-22','10h',3,NULL,15,19,'','en cours','false',NULL,'false',0,36.4,2.4,0.8,36.4,NULL,'documents/gitebleu/contrat/Contrat Feneu-102 33.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480','19.8'),(34,5,'Chambre','Feneu-102',1,0,0,1,10,'24-01-22','17h','26-01-22','10h',2,NULL,0,19,'','en cours','false',NULL,'false',0,20.6,1.6,0.8,20.6,NULL,'documents/gitebleu/contrat/Contrat Feneu-102 34.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480','19.8'),(35,5,'Chambre','Feneu-102',2,0,0,1,8,'24-01-22','17h','31-01-22','10h',7,0,35,58,'','en cours','false','','true',30,74.2,11.2,0.8,104.2,NULL,'documents/gitebleu/contrat/Contrat Feneu-102 35.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480','30.0'),(36,5,'Chambre','Feneu-102',1,0,0,1,15,'24-01-22','17h','26-01-22','10h',2,0,10,29,'','terminé','true','','true',40.6,0,1.6,0.8,40.6,'Espèce','documents/gitebleu/contrat/Contrat Feneu-102 36.pdf','documents/gitebleu/facture/Facture Feneu-102 36.pdf','8 Rue De La Fontaine Rouillee, 49480','19.8'),(37,5,'Chambre','Feneu-102',1,0,0,1,10,'25-01-22','17h','27-01-22','10h',2,NULL,10,19,'','en cours','false',NULL,'false',0,30.6,1.6,0.8,30.6,NULL,'documents/gitebleu/contrat/Contrat Feneu-102 37.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480','19.8'),(38,5,'Chambre','Feneu-102',1,0,0,1,19,'24-01-22','17h','25-01-22','10h',1,NULL,30,19,'','en cours','false',NULL,'false',0,49.8,0.8,0.8,49.8,NULL,'documents/gitebleu/contrat/Contrat Feneu-102 38.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480','19.8'),(39,5,'Chambre','Feneu-101',1,0,1,0,25,'24-01-22','17h','25-01-22','10h',1,0,0,25,'','en cours','false','','false',0,25.8,0.8,0.8,25.8,'Espèce','documents/gitebleu/contrat/Contrat Feneu-101 39.pdf',NULL,'8 Rue De La Fontaine Rouillee','25.8'),(40,5,'Chambre','Feneu-101',2,0,0,0,22,'24-01-22','17h','29-01-22','10h',5,0,0,110,'','en cours','false','','false',0,118,8,0.8,118,NULL,'documents/gitebleu/contrat/Contrat Feneu-101 40.pdf',NULL,'8 Rue De La Fontaine Rouillee','20.0'),(41,5,'Dortoir','Feneu-201',1,0,1,0,10,'24-01-22','17h','25-01-22','10h',1,NULL,5,10,'','en cours','false',NULL,'false',0,15.8,0.8,0.8,15.8,NULL,'documents/gitebleu/contrat/Contrat Feneu-201 41.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480 Feneu','10.8'),(42,5,'Dortoir','Feneu-201',2,0,2,0,5,'24-01-22','17h','28-01-22','10h',4,NULL,15,20,'','en cours','false',NULL,'false',0,41.4,6.4,0.8,41.4,NULL,'documents/gitebleu/contrat/Contrat Feneu-201 42.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480 Feneu','21.6'),(43,5,'Dortoir','Feneu-201',2,0,2,0,20,'24-01-22','17h','26-01-22','10h',2,NULL,10,40,'','en cours','false',NULL,'false',0,53.2,3.2,0.8,53.2,NULL,'documents/gitebleu/contrat/Contrat Feneu-201 43.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480 Feneu','20.0'),(44,5,'Maison','Le Gite Bleu',2,0,0,1,147,'24-01-22','17h','26-01-22','10h',2,600,0,294,NULL,'en cours','false','false','false',0,297.52,3.52,0.88,297.52,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 44.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','80'),(45,8,'Maison','Le Gite Bleu',2,0,0,1,129,'01-04-22','17h','15-04-22','10h',14,600,150,1800,NULL,'en cours','false','false','false',0,1974.64,24.64,0.88,1974.64,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 45.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','540'),(46,4,'Maison','Le Gite Bleu',2,0,0,1,96,'02-02-22','12h','07-02-22','12h',5,600,135,481,'','terminé','false','false','false',624.8,0,8.8,0.88,624.8,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 46.pdf','documents/gitebleu/facture/Facture Le Gite Bleu 46.pdf','5 Rue Des Moulin Neufs, 49480 Huillez','130.0'),(47,5,'Dortoir','Feneu-201',3,0,2,0,60,'01-02-22','17h','05-02-22','10h',4,0,0,240,'','en cours','false','','false',0,748.8,9.6,0.8,748.8,NULL,'documents/gitebleu/contrat/Contrat Feneu-201 47.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480 Feneu','20.0'),(48,5,'Dortoir','Feneu-201',3,0,0,0,2430,'01-02-22','17h','03-02-22','10h',2,0,10,4860,'','en cours','false','','false',0,4874.8,4.8,0.8,4874.8,NULL,'documents/gitebleu/contrat/Contrat Feneu-201 48.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480 Feneu','21.6'),(49,5,'Dortoir','Feneu-201',2,0,0,0,10,'01-02-22','17h','04-02-22','10h',3,NULL,0,60,NULL,'en cours','false',NULL,'false',0,64.8,4.8,0.8,64.8,NULL,'documents/gitebleu/contrat/Contrat Feneu-201 49.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480 Feneu','20.0'),(50,5,'Dortoir','Feneu-201',3,0,0,0,10,'01-02-22','17h','04-02-22','10h',3,0,15,90,'','en cours','false','','false',0,112.2,7.2,0.8,112.2,NULL,'documents/gitebleu/contrat/Contrat Feneu-201 50.pdf',NULL,'8 Rue De La Fontaine Rouillee, 49480 Feneu','20.0'),(51,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 51.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(52,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 52.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(53,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 53.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(54,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 54.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(55,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 55.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(56,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 56.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(57,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 57.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(58,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 58.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(59,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 59.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(60,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 60.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(61,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 61.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(62,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 62.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(63,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 63.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(64,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 64.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(65,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 65.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(66,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 66.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(67,4,'Maison','Le Gite Bleu',0,0,0,0,147,'01-02-22','17h','01-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 67.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0'),(68,4,'Maison','Le Gite Bleu',0,0,0,0,147,'02-02-22','17h','02-02-22','10h',1,600,0,147,NULL,'en cours','false','false','false',0,147,0,0.88,147,NULL,'documents/gitebleu/contrat/Contrat Le Gite Bleu 68.pdf',NULL,'5 Rue Des Moulin Neufs, 49480 Huillez','40.0');
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookingOption`
--

DROP TABLE IF EXISTS `bookingOption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookingOption` (
  `bookingID` int DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `dailyRate` varchar(20) DEFAULT NULL,
  `amount` varchar(20) DEFAULT NULL,
  `optionID` int DEFAULT NULL,
  `selected` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookingOption`
--

LOCK TABLES `bookingOption` WRITE;
/*!40000 ALTER TABLE `bookingOption` DISABLE KEYS */;
INSERT INTO `bookingOption` VALUES (1,'Forfait Ménage','false','80',1,'false'),(1,'Draps Et Serviette','false','30',2,'false'),(1,'Wifi Unlimited','true','5',3,'false'),(2,'Forfait Ménage','false','80',1,'false'),(2,'Draps Et Serviette','false','30',2,'false'),(2,'Wifi Unlimited','true','5',3,'false'),(3,'Forfait Ménage','false','80',1,'true'),(3,'Draps Et Serviette','false','30',2,'true'),(3,'Wifi Unlimited','true','5',3,'true'),(4,'Forfait Ménage','false','80',1,'true'),(4,'Draps Et Serviette','false','30',2,'false'),(4,'Wifi Unlimited','true','5',3,'false'),(5,'Forfait Ménage','false','80',1,'true'),(5,'Draps Et Serviette','false','30',2,'false'),(5,'Wifi Unlimited','true','5',3,'false'),(6,'Forfait Ménage','false','80',1,'false'),(6,'Draps Et Serviette','false','30',2,'false'),(6,'Wifi Unlimited','true','5',3,'false'),(7,'Forfait Ménage','false','80',1,'false'),(7,'Draps Et Serviette','false','30',2,'false'),(7,'Wifi Unlimited','true','5',3,'false'),(8,'Forfait Ménage','false','80',1,'true'),(8,'Draps Et Serviette','false','30',2,'false'),(8,'Wifi Unlimited','true','5',3,'false'),(9,'Forfait Ménage','false','80',1,'false'),(9,'Draps Et Serviette','false','30',2,'false'),(9,'Wifi Unlimited','true','5',3,'true'),(10,'Forfait Ménage','false','80',1,'true'),(10,'Draps Et Serviette','false','30',2,'false'),(10,'Wifi Unlimited','true','5',3,'true'),(11,'Forfait Ménage','false','80',1,'true'),(11,'Draps Et Serviette','false','30',2,'false'),(11,'Wifi Unlimited','true','5',3,'true'),(12,'Forfait Ménage','false','80',1,'true'),(12,'Draps Et Serviette','false','30',2,'true'),(12,'Wifi Unlimited','true','5',3,'true'),(13,'Forfait Ménage','false','80',1,'false'),(13,'Draps Et Serviette','false','30',2,'false'),(13,'Wifi Unlimited','true','5',3,'false'),(14,'Forfait Ménage','false','80',1,'false'),(14,'Draps Et Serviette','false','30',2,'false'),(14,'Wifi Unlimited','true','5',3,'false'),(15,'Forfait Ménage','false','80',1,'false'),(15,'Draps Et Serviette','false','30',2,'false'),(15,'Wifi Unlimited','true','5',3,'false'),(16,'Forfait Ménage','false','80',1,'false'),(16,'Draps Et Serviette','false','30',2,'false'),(16,'Wifi Unlimited','true','5',3,'false'),(17,'Forfait Ménage','false','80',1,'true'),(17,'Draps Et Serviette','false','30',2,'true'),(17,'Wifi Unlimited','true','5',3,'true'),(18,'Forfait Ménage','false','80',1,'false'),(18,'Draps Et Serviette','false','30',2,'false'),(18,'Wifi Unlimited','true','5',3,'true'),(19,'Forfait Ménage','false','80',1,'false'),(19,'Draps Et Serviette','false','30',2,'false'),(19,'Wifi Unlimited','true','5',3,'true'),(20,'Forfait Ménage','false','80',1,'false'),(20,'Draps Et Serviette','false','30',2,'false'),(20,'Wifi Unlimited','true','5',3,'false'),(21,'Forfait Ménage','false','80',1,'false'),(21,'Draps Et Serviette','false','30',2,'false'),(21,'Wifi Unlimited','true','5',3,'true'),(22,'Forfait Ménage','false','80',1,'false'),(22,'Draps Et Serviette','false','30',2,'false'),(22,'Wifi Unlimited','true','5',3,'false'),(23,'Forfait Ménage','false','80',1,'false'),(23,'Draps Et Serviette','false','30',2,'true'),(23,'Wifi Unlimited','true','5',3,'true'),(25,'Forfait Ménage','false','80',1,'false'),(25,'Draps Et Serviette','false','30',2,'false'),(25,'Wifi Unlimited','true','5',3,'false'),(26,'Forfait Ménage','false','80',1,'true'),(26,'Draps Et Serviette','false','30',2,'true'),(26,'Wifi Unlimited','true','5',3,'true'),(27,'Forfait Ménage','false','80',1,'false'),(27,'Draps Et Serviette','false','30',2,'false'),(27,'Wifi Unlimited','true','5',3,'true'),(28,'Forfait Ménage','false','80',1,'false'),(28,'Draps Et Serviette','false','30',2,'false'),(28,'Wifi Unlimited','true','5',3,'false'),(29,'Forfait Ménage','false','80',1,'false'),(29,'Draps Et Serviette','false','30',2,'false'),(29,'Wifi Unlimited','true','5',3,'false'),(30,'Forfait Ménage','false','80',1,'false'),(30,'Draps Et Serviette','false','30',2,'false'),(30,'Wifi Unlimited','true','5',3,'false'),(31,'Forfait Ménage','false','80',1,'false'),(31,'Draps Et Serviette','false','30',2,'false'),(31,'Wifi Unlimited','true','5',3,'false'),(32,'Forfait Ménage','false','80',1,'false'),(32,'Draps Et Serviette','false','30',2,'false'),(32,'Wifi Unlimited','true','5',3,'true'),(33,'Forfait Ménage','false','80',1,'false'),(33,'Draps Et Serviette','false','30',2,'false'),(33,'Wifi Unlimited','true','5',3,'true'),(34,'Forfait Ménage','false','80',1,'false'),(34,'Draps Et Serviette','false','30',2,'false'),(34,'Wifi Unlimited','true','5',3,'false'),(35,'Forfait Ménage','false','80',1,'false'),(35,'Draps Et Serviette','false','30',2,'false'),(35,'Wifi Unlimited','true','5',3,'true'),(36,'Forfait Ménage','false','80',1,'false'),(36,'Draps Et Serviette','false','30',2,'false'),(36,'Wifi Unlimited','true','5',3,'true'),(37,'Forfait Ménage','false','80',1,'false'),(37,'Draps Et Serviette','false','30',2,'false'),(37,'Wifi Unlimited','true','5',3,'true'),(38,'Forfait Ménage','false','80',1,'false'),(38,'Draps Et Serviette','false','30',2,'true'),(38,'Wifi Unlimited','true','5',3,'false'),(39,'Forfait Ménage','false','80',1,'false'),(39,'Draps Et Serviette','false','30',2,'false'),(39,'Wifi Unlimited','true','5',3,'false'),(40,'Forfait Ménage','false','80',1,'false'),(40,'Draps Et Serviette','false','30',2,'false'),(40,'Wifi Unlimited','true','5',3,'false'),(41,'Forfait Ménage','false','80',1,'false'),(41,'Draps Et Serviette','false','30',2,'true'),(41,'Wifi Unlimited','true','5',3,'true'),(42,'Forfait Ménage','false','80',1,'false'),(42,'Draps Et Serviette','false','30',2,'false'),(42,'Wifi Unlimited','true','5',3,'true'),(43,'Forfait Ménage','false','80',1,'false'),(43,'Draps Et Serviette','false','30',2,'true'),(43,'Wifi Unlimited','true','5',3,'true'),(44,'Forfait Ménage','false','80',1,'false'),(44,'Draps Et Serviette','false','30',2,'false'),(44,'Wifi Unlimited','true','5',3,'false'),(45,'Forfait Ménage','false','80',1,'true'),(45,'Draps Et Serviette','false','30',2,'false'),(45,'Wifi Unlimited','true','5',3,'true'),(46,'Forfait Ménage','false','80',1,'true'),(46,'Draps Et Serviette','false','30',2,'true'),(46,'Wifi Unlimited','true','5',3,'true'),(47,'Forfait Ménage','false','80',1,'false'),(47,'Draps Et Serviette','false','30',2,'false'),(47,'Wifi Unlimited','true','5',3,'false'),(48,'Forfait Ménage','false','80',1,'false'),(48,'Draps Et Serviette','false','30',2,'false'),(48,'Wifi Unlimited','true','5',3,'true'),(49,'Forfait Ménage','false','80',1,'false'),(49,'Draps Et Serviette','false','30',2,'false'),(49,'Wifi Unlimited','true','5',3,'false'),(50,'Forfait Ménage','false','80',1,'false'),(50,'Draps Et Serviette','false','30',2,'false'),(50,'Wifi Unlimited','true','5',3,'true'),(51,'Forfait Ménage','false','80',1,'false'),(51,'Draps Et Serviette','false','30',2,'false'),(51,'Wifi Unlimited','true','5',3,'false'),(52,'Forfait Ménage','false','80',1,'false'),(52,'Draps Et Serviette','false','30',2,'false'),(52,'Wifi Unlimited','true','5',3,'false'),(53,'Forfait Ménage','false','80',1,'false'),(53,'Draps Et Serviette','false','30',2,'false'),(53,'Wifi Unlimited','true','5',3,'false'),(54,'Forfait Ménage','false','80',1,'false'),(54,'Draps Et Serviette','false','30',2,'false'),(54,'Wifi Unlimited','true','5',3,'false'),(55,'Forfait Ménage','false','80',1,'false'),(55,'Draps Et Serviette','false','30',2,'false'),(55,'Wifi Unlimited','true','5',3,'false'),(56,'Forfait Ménage','false','80',1,'false'),(56,'Draps Et Serviette','false','30',2,'false'),(56,'Wifi Unlimited','true','5',3,'false'),(57,'Forfait Ménage','false','80',1,'false'),(57,'Draps Et Serviette','false','30',2,'false'),(57,'Wifi Unlimited','true','5',3,'false'),(58,'Forfait Ménage','false','80',1,'false'),(58,'Draps Et Serviette','false','30',2,'false'),(58,'Wifi Unlimited','true','5',3,'false'),(59,'Forfait Ménage','false','80',1,'false'),(59,'Draps Et Serviette','false','30',2,'false'),(59,'Wifi Unlimited','true','5',3,'false'),(60,'Forfait Ménage','false','80',1,'false'),(60,'Draps Et Serviette','false','30',2,'false'),(60,'Wifi Unlimited','true','5',3,'false'),(61,'Forfait Ménage','false','80',1,'false'),(61,'Draps Et Serviette','false','30',2,'false'),(61,'Wifi Unlimited','true','5',3,'false'),(62,'Forfait Ménage','false','80',1,'false'),(62,'Draps Et Serviette','false','30',2,'false'),(62,'Wifi Unlimited','true','5',3,'false'),(63,'Forfait Ménage','false','80',1,'false'),(63,'Draps Et Serviette','false','30',2,'false'),(63,'Wifi Unlimited','true','5',3,'false'),(64,'Forfait Ménage','false','80',1,'false'),(64,'Draps Et Serviette','false','30',2,'false'),(64,'Wifi Unlimited','true','5',3,'false'),(65,'Forfait Ménage','false','80',1,'false'),(65,'Draps Et Serviette','false','30',2,'false'),(65,'Wifi Unlimited','true','5',3,'false'),(66,'Forfait Ménage','false','80',1,'false'),(66,'Draps Et Serviette','false','30',2,'false'),(66,'Wifi Unlimited','true','5',3,'false'),(67,'Forfait Ménage','false','80',1,'false'),(67,'Draps Et Serviette','false','30',2,'false'),(67,'Wifi Unlimited','true','5',3,'false'),(68,'Forfait Ménage','false','80',1,'false'),(68,'Draps Et Serviette','false','30',2,'false'),(68,'Wifi Unlimited','true','5',3,'false');
/*!40000 ALTER TABLE `bookingOption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `ID` int DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `firstName` varchar(200) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `mobileNumber` varchar(20) DEFAULT NULL,
  `tel2` varchar(20) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `street` varchar(200) DEFAULT NULL,
  `street2` varchar(200) DEFAULT NULL,
  `district` varchar(200) DEFAULT NULL,
  `cp` varchar(20) DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `country` varchar(200) DEFAULT NULL,
  `IDproof` varchar(200) DEFAULT NULL,
  `times` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,'Landreau','Julien','Homme','01 01 01 01 01','','dailleredavid@gmail.com','Rue','','District','Cp','Ville','Pays','id',NULL),(2,'Gourdon','Xavier','Homme','02 02 02 02 02','','dailleredavid@gmail.com','Rue','','District','Cp','Ville','Pays','id',NULL),(3,'Pillet','Marlene','Femme','03 03 03 03 03','','dailleredavid@gmail.com','Rue','','District','Cp','City','Pays','id',NULL),(4,'Bournizeau','','Madame/Monsieur ','04 04 04 04 04','07 07 07 07 07','dailleredavid@gmail.com','1 Rue Des Peupliers','batiment c','St Sylvain','49000','Angers Est - Les Baleares','Pays','id',NULL),(5,'Nawrot','','Madame/Monsieur ','05 05 05 05 05','','dailleredavid@gmail.com','Rue','','District','Cp','Ville','Pays','id',NULL),(6,'Morgane','','Madame/Monsieur ','06 06 06 06 06','','dailleredavid@gmail.com','Rue','','District','Cp','Ville','Pays','id',NULL),(7,'Derouet','','Madame/Monsieur','07 07 07 07 07 ','','dailleredavid@gmail.com','Rue','','District','Cp','Ville','Pays','id',NULL),(8,'Peris','Shehani','Femme','10 10 10 10 10 10','','dailleredavid@gmail.com','5 Wimala Watte Road','','','00001','Nugegoda','Sri Lanka','',NULL);
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documents`
--

DROP TABLE IF EXISTS `documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documents` (
  `name` varchar(200) DEFAULT NULL,
  `bookingType` varchar(200) DEFAULT NULL,
  `property` varchar(200) DEFAULT NULL,
  `object` varchar(200) DEFAULT NULL,
  `text` varchar(5000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documents`
--

LOCK TABLES `documents` WRITE;
/*!40000 ALTER TABLE `documents` DISABLE KEYS */;
INSERT INTO `documents` VALUES ('1','gite','all',NULL,'<genre> <prénom> <nom>, \n \nCeci est un contrat de location pour <prénom> <nom>, concernant la location: <nom location> (<type de location>), durant la periode :\ndu <arrivée> au <départ>, (soit <nuit> nuit(s) ),\npour <adulte> adulte(s) et <enfant> enfant(s).\nle prix de la location est de <prix jour> € /jour pour un montant de <montant> € .\nLa taxe de sejour est de <taxe de séjour> (charge additionel pour toutes personnes supplémentaire). \nLe montant total estimé est de <total avec taxe>€ .\nAfin de confirmer la réservation, merci de payer <arrhe> € à l’avance et de retourner ce document daté et signé.\nCordialement.\n<ma compagnie>'),('2','all','all','Votre réservation à été enregistrée','<genre> <prénom> <nom>,\n \nJ\'ai le plaisir de vous informer que votre demande à bien été enregistrée.\nVeuillez trouver ci-joint les documents liés à votre réservation.\nAfin de confirmer votre réservation, merci de renvoyer un exemplaire revêtu de votre accord daté et signé, accompagné des arrhes.\nEn espérant avoir le plaisir de vous accueillir très prochainement, je vous adresse mes sincères salutations.\n\nCordialement\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('3','all','all','Merci de votre visite','<genre> <prénom> <nom>,\n \nMerci de votre visite. Nous ésperont que vous avez passé un agréable séjour. vous trouverez ci-joint le detail de la facturation\nEn esperant vous revoir bientôt.\nCordialement\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('4','all','all','Réservation Confirmée','<genre> <prénom> <nom>,\n \nJ\'ai le plaisir de vous informer avoir bien reçu le contrat et les Arrhes. \nVotre réservation est par conséquent confirmée.\n\nCordialement\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('5','all','all','Contrat et/ou Arrhes non reçus','<genre> <prénom> <nom>,\n \nSuite à votre demande, nous avons enregistré une réservation à votre non. Cependant, Nous n\'avons à ce jour pas encore recus le contrat et/ou les Arrhes. Afin d\'éviter l\'anulation de votres réservations, merci de nous retourner les élements manquants dans les plus brefs délais. Sans retour de votre part, les dates enregistrées seront de nouveau disponibles\nCordialement\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('6','all','all','','<genre> <prénom> <nom>,\n\n\n\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('contratInfoTitle','gite','all',NULL,'Contrat de Location'),('contratIntro','gite','all',NULL,'<genre> <prénom> <nom>,\n\nSuite à votre demande de réservation, j\'ai le plaisir de vous adresser le présent contrat de location.\nAfin de confirmer la location, veuillez renvoyer un exemplaire daté et signé revêtu de votre accord et accompagné du règlement du montant des arrhes.\nLe second éxemplaire est à conserver.'),('contratOwnerTitle','gite','all',NULL,'Entre le propriétaire (adresse de règlement)'),('contratOwner','gite','all',NULL,'<mon prénom> <mon nom>, \nAdresse: <ma rue>, <ma commune dlg>  <mon cp> <ma ville>.\nTéléphone: <mon tel>   Email: <mon email>      Siret: <mon siret>'),('contratPropertyTitle','gite','all',NULL,'Pour la location: '),('contratProperty','gite','all',NULL,'nom de la location: <nom location>, type: <type de location> \nAdresse de la propriété: <adresse loc>\n'),('contratClientTitle','gite','all',NULL,'Et le locataire:'),('contratClient','gite','all',NULL,'<genre> <nom> <prénom> \nAdresse: <adresse1> <adresse2>, <commune dlg>  <cp> <ville>.\nTéléphone: <tel>-<tel2>  Email: <email>'),('contratBookingTitle','gite','all',NULL,'Détail de la réservation'),('contratBooking','gite','all',NULL,'Arrivée: <arrivée>\nDépart: <départ>\nNombre de nuit: <nuit>\nNombre d\'adutle(s): <adulte> - Enfant(s): <enfant>\nMontant de la location: <TOTAL> €\n(séjour: <montant> € , taxe de séjour: <taxe de séjour> € , option(s): <total option> € ) \noption(s): <option>\nArrhes: <arrhes> €'),('contratConditionTitle','gite','all',NULL,'Informations Importantes'),('contratCondition','gite','all',NULL,'Un dépôt de garantie de <caution> € vous sera demandé à votre arrivée.\nCette caution sera annulée dans un délai de 15 jours à compter du départ des lieux, déduction faite des éventuelles détériorations ou du coût de remise en état des lieux.\nLe solde de la location devra être versé au plus tard le jour de votre arrivée, en cas de règlement par chèques vacances, le solde devra être réglé un mois avant votre arrivée. La confirmation de réservation prendra effet immediatement après reception à mon adresse, d\'un exemplaire du présent contrat daté et signé avec la mention lue et approuvéAccompagné du montant des arrhes de (30 % de la location) soit: <arrhes> €. '),('contratSignatureTitle','gite','all',NULL,'Merci de dater et signer'),('contratSignature','gite','all',NULL,'Le présent contrat est établi en deux exemplaires.\nJ\'ai pris connaissance des conditions générales de locations précisées ci-joint.\n\n\n\n                           Le propriétaire:                                                              Le locataire:');
/*!40000 ALTER TABLE `documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `myInfo`
--

DROP TABLE IF EXISTS `myInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `myInfo` (
  `ID` int DEFAULT NULL,
  `company` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `firstName` varchar(200) DEFAULT NULL,
  `tel` varchar(200) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `street` varchar(200) DEFAULT NULL,
  `district` varchar(200) DEFAULT NULL,
  `cp` varchar(20) DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `country` varchar(200) DEFAULT NULL,
  `idCompany` varchar(200) DEFAULT NULL,
  `website` varchar(1000) DEFAULT NULL,
  `facebook` varchar(1000) DEFAULT NULL,
  `instagram` varchar(1000) DEFAULT NULL,
  `google` varchar(1000) DEFAULT NULL,
  `tripAdvisor` varchar(1000) DEFAULT NULL,
  `otherReview` varchar(1000) DEFAULT NULL,
  `APEcode` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myInfo`
--

LOCK TABLES `myInfo` WRITE;
/*!40000 ALTER TABLE `myInfo` DISABLE KEYS */;
INSERT INTO `myInfo` VALUES (1,'Chez Annie','Daillere','David','06 80 03 80 24','dailleredavid@gmail.com','Youarebeautiful_69','3 Rue Emmanuel Voisin','St Sylvain d\'Anjou','49480','Les Verriere En Anjou','France','08ap05155','www.gitebleu.net','www.facebook.com','www.instagram.com','www.google.com','www.tripadvisor.com','www.other.com','');
/*!40000 ALTER TABLE `myInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `myOption`
--

DROP TABLE IF EXISTS `myOption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `myOption` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `amount` varchar(200) DEFAULT NULL,
  `dailyRate` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myOption`
--

LOCK TABLES `myOption` WRITE;
/*!40000 ALTER TABLE `myOption` DISABLE KEYS */;
INSERT INTO `myOption` VALUES (1,'Forfait Ménage','80','false'),(2,'Draps Et Serviette','30','false'),(3,'Wifi Unlimited','5','true');
/*!40000 ALTER TABLE `myOption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `ID` int NOT NULL,
  `building` varchar(20) DEFAULT NULL,
  `bookingType` varchar(200) DEFAULT NULL,
  `roomName` varchar(200) DEFAULT NULL,
  `roomQte` varchar(20) DEFAULT NULL,
  `singleBed` int DEFAULT NULL,
  `doubleBed` int DEFAULT NULL,
  `price` int DEFAULT NULL,
  `bound` varchar(20) DEFAULT NULL,
  `tax` varchar(20) DEFAULT NULL,
  `open` varchar(20) DEFAULT NULL,
  `adress` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'Le Gite Bleu','Maison',' - ','4',5,3,147,'600','0.88','true','5 Rue Des Moulin Neufs, 49480 Huillez'),(2,'Gite Vert','Maison',' - ','2',2,1,120,'500','0.88','true','7 Rue Des Mulin Neufs, 49480 Huillez'),(3,'Feneu','Chambre','101','-',2,0,25,'-','0.8','true','8 Rue De La Fontaine Rouillee'),(4,'Feneu','Chambre','102','-',0,1,19,'-','0.8','true','8 Rue De La Fontaine Rouillee, 49480'),(5,'Feneu','Dortoir','201','4',4,0,10,'-','0.8','true','8 Rue De La Fontaine Rouillee, 49480 Feneu');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storedInfo`
--

DROP TABLE IF EXISTS `storedInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storedInfo` (
  `name` varchar(20) DEFAULT NULL,
  `info` varchar(1000) DEFAULT NULL,
  `info2` varchar(2000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storedInfo`
--

LOCK TABLES `storedInfo` WRITE;
/*!40000 ALTER TABLE `storedInfo` DISABLE KEYS */;
INSERT INTO `storedInfo` VALUES ('logoName','documents/gitebleu/image/home(1).png',NULL),('logoName','documents/gitebleu/image/ô gite bleu.jpg',NULL),('logoName','documents/gitebleu/image/home(1).png',NULL);
/*!40000 ALTER TABLE `storedInfo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-02 11:30:09
