-- MySQL dump 10.13  Distrib 8.0.27, for macos11 (x86_64)
--
-- Host: localhost    Database: test6
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
-- Current Database: `test6`
--

/*!40000 DROP DATABASE IF EXISTS `test6`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `test6` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `test6`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
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
INSERT INTO `documents` VALUES ('1','gite','all',NULL,'<genre> <prénom> <nom>, \n \nCeci est un contrat de location pour <prénom> <nom>, concernant la location: <nom location> (<type de location>), durant la periode :\ndu <arrivée> au <départ>, (soit <nuit> nuit(s) ),\npour <adulte> adulte(s) et <enfant> enfant(s).\nle prix de la location est de <prix jour> € /jour pour un montant de <montant> € .\nLa taxe de sejour est de <taxe de séjour> (charge additionel pour toutes personnes supplémentaire). \nLe montant total estimé est de <total avec taxe>€ .\nAfin de confirmer la réservation, merci de payer <arrhe> € à l’avance et de retourner ce document daté et signé.\nCordialement.\n<ma compagnie>'),('2','all','all','Votre réservation à été enregistrée','<genre> <prénom> <nom>,\n \nJ\'ai le plaisir de vous informer que votre demande à bien été enregistrée.\nVeuillez trouver ci-joint les documents liés à votre réservation.\nAfin de confirmer votre réservation, merci de renvoyer un exemplaire revêtu de votre accord daté et signé, accompagné des arrhes.\nEn espérant avoir le plaisir de vous accueillir très prochainement, je vous adresse mes sincères salutations.\n\nCordialement\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('3','all','all','Merci de votre visite','<genre> <prénom> <nom>,\n \nMerci de votre visite. Nous ésperont que vous avez passé un agréable séjour. vous trouverez ci-joint le detail de la facturation\nEn esperant vous revoir bientôt.\nCordialement\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('4','all','all','Réservation Confirmée','<genre> <prénom> <nom>,\n \nJ\'ai le plaisir de vous informer avoir bien reçu le contrat et les Arrhes. \nVotre réservation est par conséquent confirmée.\n\nCordialement\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('5','all','all','Contrat et/ou Arrhes non reçus','<genre> <prénom> <nom>,\n \nSuite à votre demande, nous avons enregistré une réservation à votre nom. Cependant, Nous n\'avons à ce jour pas encore recus le contrat et/ou les Arrhes. Afin d\'éviter l\'anulation de votres réservations, merci de nous retourner les élements manquants dans les plus brefs délais. Sans retour de votre part, les dates enregistrées seront de nouveau disponibles\nCordialement\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('6','all','all','','<genre> <prénom> <nom>,\n\n\n\n<mon prénom>\n\n<ma compagnie>\n<mon tel>\n<ma rue>, <ma commune dlg>\n<mon cp> <ma ville>\n<mon site>'),('contratInfoTitle','gite','all',NULL,'Contrat de Location'),('contratIntro','gite','all',NULL,'<genre> <prénom> <nom>,\n\nSuite à votre demande de réservation, j\'ai le plaisir de vous adresser le présent contrat de location.\nAfin de confirmer la location, veuillez renvoyer un exemplaire daté et signé revêtu de votre accord et accompagné du règlement du montant des arrhes.\nLe second éxemplaire est à conserver.'),('contratOwnerTitle','gite','all',NULL,'Entre le propriétaire (adresse de règlement)'),('contratOwner','gite','all',NULL,'<mon prénom> <mon nom>, \nAdresse: <ma rue>, <ma commune dlg>  <mon cp> <ma ville>.\nTéléphone: <mon tel> - Email: <mon email> - Siret: <mon siret>\n'),('contratPropertyTitle','gite','all',NULL,'Pour la location: '),('contratProperty','gite','all',NULL,'nom de la location: <nom location>, type: <type de location> \nAdresse de la propriété: <adresse loc>\n'),('contratClientTitle','gite','all',NULL,'Et le locataire: '),('contratClient','gite','all',NULL,'<genre> <nom> <prénom> \nAdresse: <adresse1> <adresse2>, <commune dlg>  <cp> <ville>.\nTéléphone: <tel> - <tel2> Email: <email>'),('contratBookingTitle','gite','all',NULL,'Détail de la réservation'),('contratBooking','gite','all',NULL,'Date: du <arrivée> au <départ>, nombre de nuit: <nuit>\nNombre d\'adutle(s): <adulte> - Enfant(s): <enfant>\nMontant de la location: <TOTAL> €\n(séjour: <montant> € , taxe de séjour: <taxe de séjour> € , option(s): <total option> € ) \noption(s): <option>\nArrhes: <arrhes> €\n'),('contratConditionTitle','gite','all',NULL,'Informations Importantes'),('contratCondition','gite','all',NULL,'Un dépôt de garantie de <caution> € vous sera demandé à votre arrivée.\nCette caution sera annulée dans un délai de 15 jours à compter du départ des lieux, déduction faite des éventuelles détériorations ou du coût de remise en état des lieux.\nLe solde de la location devra être versé au plus tard le jour de votre arrivée, en cas de règlement par chèques vacances, le solde devra être réglé un mois avant votre arrivée. La confirmation de réservation prendra effet immediatement après reception à mon adresse, d\'un exemplaire du présent contrat daté et signé avec la mention lu et approuvé, accompagné du montant des arrhes de (30 % de la location) soit: <arrhes> €. '),('contratSignatureTitle','gite','all',NULL,'Merci de dater et signer'),('contratSignature','gite','all',NULL,'Le présent contrat est établi en deux exemplaires.\nJ\'ai pris connaissance des conditions générales de locations précisées ci-joint.\n\n\n\n                           Le propriétaire:                                                              Le locataire:\n\n                           <mon prénom> <mon nom>');
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
  `APEcode` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myInfo`
--

LOCK TABLES `myInfo` WRITE;
/*!40000 ALTER TABLE `myInfo` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myOption`
--

LOCK TABLES `myOption` WRITE;
/*!40000 ALTER TABLE `myOption` DISABLE KEYS */;
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

-- Dump completed on 2022-02-01 11:38:50
