-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: waitershelper
-- ------------------------------------------------------
-- Server version	5.7.9-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `denomination`
--

DROP TABLE IF EXISTS `denomination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `denomination` (
  `Id` int(11) NOT NULL,
  `portion` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `timeWhenAdded` tinyblob,
  `timeWhenIsReady` tinyblob,
  `dish_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FKposibf2xdx2n29dkhmht408qo` (`dish_id`),
  KEY `FKc8e5uo68avwv3o2wpo0bqq95o` (`order_id`),
  CONSTRAINT `FKc8e5uo68avwv3o2wpo0bqq95o` FOREIGN KEY (`order_id`) REFERENCES `ordering` (`id`),
  CONSTRAINT `FKposibf2xdx2n29dkhmht408qo` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `denomination`
--

LOCK TABLES `denomination` WRITE;
/*!40000 ALTER TABLE `denomination` DISABLE KEYS */;
INSERT INTO `denomination` VALUES (73,2995,194675,5,'2016-02-07 16:24:30',NULL,16,72),(83,4,65,5,'2016-02-08 16:01:05',NULL,16,82),(84,1,65,5,'2016-02-08 16:37:16',NULL,16,82),(86,5,325,5,'2016-02-08 16:39:56',NULL,16,85),(87,792,11088,5,'2016-02-08 16:40:19',NULL,18,85),(88,5905,5905,5,'2016-02-08 16:41:19',NULL,24,85),(90,10,140,5,'2016-02-08 16:47:19',NULL,18,85),(91,100,100,5,'2016-02-08 16:47:28',NULL,24,85),(92,10,650,5,'2016-02-08 16:49:23',NULL,16,85),(104,20,650,7,'2016-02-11 00:26:23',NULL,16,103),(105,4,56,6,'2016-02-11 00:29:35',NULL,18,103),(129,10,650,7,'2016-02-15 01:16:40',NULL,16,103),(132,10,140,0,'2016-02-17 03:14:14',NULL,18,131),(133,10,10,0,'2016-02-17 03:14:21',NULL,24,131),(134,2,130,0,'2016-02-17 03:14:35',NULL,16,131),(137,5,70,0,'2016-02-21 00:57:43',NULL,18,75);
/*!40000 ALTER TABLE `denomination` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dish`
--

DROP TABLE IF EXISTS `dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dish` (
  `Id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `priceForPortion` double DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  `whoCoockDishType` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish`
--

LOCK TABLES `dish` WRITE;
/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
INSERT INTO `dish` VALUES (16,'0.5 vodka','Vodka Hortizia',65,1,'',3),(18,'Salat s ovoshchami, miasom pod mayonezom i suhariami','Cezar',14,0,'',0),(24,'hleb schitaut po grivne za porziyu','hleb',1,0,'',0),(138,'mmm niamka','Huy zepechenniy v pizde',500,3,'',3);
/*!40000 ALTER TABLE `dish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fund`
--

DROP TABLE IF EXISTS `fund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fund` (
  `id` int(11) NOT NULL,
  `finalPrice` double DEFAULT NULL,
  `ko` double NOT NULL,
  `price` double DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKyuekqrgf0necwqoc8nrp4y2p` (`order_id`),
  CONSTRAINT `FKyuekqrgf0necwqoc8nrp4y2p` FOREIGN KEY (`order_id`) REFERENCES `ordering` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fund`
--

LOCK TABLES `fund` WRITE;
/*!40000 ALTER TABLE `fund` DISABLE KEYS */;
INSERT INTO `fund` VALUES (72,25,0,325,72),(75,325,0,325,75),(79,NULL,0,NULL,79),(81,NULL,0,NULL,81),(82,NULL,0,NULL,82),(85,NULL,0,NULL,85),(93,NULL,0,NULL,93),(94,NULL,0,NULL,94),(97,NULL,0,NULL,97),(103,540,0,650,103),(131,NULL,0,NULL,131);
/*!40000 ALTER TABLE `fund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) NOT NULL,
  `next_val` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('default',139);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingridient`
--

DROP TABLE IF EXISTS `ingridient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ingridient` (
  `Id` int(11) NOT NULL,
  `amount` double DEFAULT NULL,
  `dish_Id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FKlqbyg24lqsksfmbbqc6rdn82h` (`dish_Id`),
  KEY `FKlkq4owl6425vxn9vm5nmom5em` (`product_id`),
  CONSTRAINT `FKlkq4owl6425vxn9vm5nmom5em` FOREIGN KEY (`product_id`) REFERENCES `product` (`Id`),
  CONSTRAINT `FKlqbyg24lqsksfmbbqc6rdn82h` FOREIGN KEY (`dish_Id`) REFERENCES `dish` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingridient`
--

LOCK TABLES `ingridient` WRITE;
/*!40000 ALTER TABLE `ingridient` DISABLE KEYS */;
INSERT INTO `ingridient` VALUES (17,0.5,16,14),(19,100,18,8),(20,50,18,9),(21,100,18,10),(22,30,18,11),(23,50,18,12),(25,50,24,15);
/*!40000 ALTER TABLE `ingridient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordering`
--

DROP TABLE IF EXISTS `ordering`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordering` (
  `id` int(11) NOT NULL,
  `advancePayment` double DEFAULT NULL,
  `amountOfPeople` int(11) DEFAULT NULL,
  `dateClientsCome` tinyblob,
  `dateOrderCreated` tinyblob NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `user_serving_id` varchar(255) DEFAULT NULL,
  `user_taken_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKots3f37jj84ix3ukwqw8e9qwk` (`user_serving_id`),
  KEY `FKagxq4yphh30jnae58u2lmsxxq` (`user_taken_id`),
  CONSTRAINT `FKagxq4yphh30jnae58u2lmsxxq` FOREIGN KEY (`user_taken_id`) REFERENCES `user` (`login`),
  CONSTRAINT `FKots3f37jj84ix3ukwqw8e9qwk` FOREIGN KEY (`user_serving_id`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordering`
--

LOCK TABLES `ordering` WRITE;
/*!40000 ALTER TABLE `ordering` DISABLE KEYS */;
INSERT INTO `ordering` VALUES (72,300,10,'2016-02-07 16:20:00','2016-02-07 16:17:27','barmen adding ordering',0,'waiter','qwerty'),(75,0,5,'2016-02-07 17:59:00','2016-02-07 17:59:42','current with not available dishes',1,NULL,'qqqqq'),(79,45,3,'2016-02-08 22:03:00','2016-02-07 22:03:53','sdfsfsdff',0,'vasia','vasia'),(81,0,1,'2016-02-09 14:57:00','2016-02-08 14:57:47','terefe',0,NULL,'vasia'),(82,0,1000,'2016-02-08 14:57:00','2016-02-08 14:58:06','terefedfsdfafafdsf',1,'vasia','vasia'),(85,100,10,'2016-02-08 16:39:00','2016-02-08 16:39:38','test',0,'waiter','vasia'),(93,0,15151,'2016-02-08 21:23:00','2016-02-08 21:23:30','test my orderings',1,'barmen','barmen'),(94,0,15151,'2016-02-08 21:23:00','2016-02-08 21:24:12','test my orderings werwr',0,'barmen','barmen'),(97,0,5,'2016-02-10 23:15:00','2016-02-10 23:14:05','test',1,NULL,'vasia'),(103,110,1,'2016-02-11 00:23:00','2016-02-11 00:26:09','werwersdfsfdsfsd',0,'barmen','vasia'),(131,1000,10,'2016-02-18 03:13:00','2016-02-17 03:13:51','kjkjkk',0,NULL,'vasia');
/*!40000 ALTER TABLE `ordering` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `Id` int(11) NOT NULL,
  `mesuarment` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (8,0,'Pomidor'),(9,0,'Ogurez'),(10,0,'Balik'),(11,0,'suhari'),(12,0,'mayonez'),(14,5,'vodka hortizia'),(15,0,'hleb'),(136,1,'Huy sobaki');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `login` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pass` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `locked` bit(1) DEFAULT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('barmen','barmen',45806640,3,'\0'),('petia','Petro Valentinovich',46792755,0,''),('qqqqq','barmen2',45806640,3,''),('qwerty','barmen',45806640,3,'\0'),('vasia','Petruha',46792755,0,'\0'),('waiter','waiter',45806640,1,'\0');
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

-- Dump completed on 2016-02-22 21:47:57
