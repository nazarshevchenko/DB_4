
CREATE DATABASE IF NOT EXISTS Lab_4;
USE Lab_4;

SET foreign_key_checks = 0;
DROP TABLE IF EXISTS `city`;
DROP TABLE IF EXISTS `country`;
DROP TABLE IF EXISTS `degree`;
DROP TABLE IF EXISTS `doctor`;
DROP TABLE IF EXISTS `heart_rhytm`;
DROP TABLE IF EXISTS `hospital`;
DROP TABLE IF EXISTS `medium`;
DROP TABLE IF EXISTS `password`;
DROP TABLE IF EXISTS `patient`;
DROP TABLE IF EXISTS `presure`;
DROP TABLE IF EXISTS `region`;
DROP TABLE IF EXISTS `temperature`;
SET foreign_key_checks = 1;


CREATE TABLE country (
                         `id` INT AUTO_INCREMENT PRIMARY KEY,
                         `name` VARCHAR(50) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE city (
                      `id` INT AUTO_INCREMENT PRIMARY KEY,
                      `name` VARCHAR(50) NOT NULL,
                      `Country_id` INT NOT NULL,
                      FOREIGN KEY (`Country_id`)
                          REFERENCES `country` (`id`)
) ENGINE = InnoDB;

CREATE TABLE degree (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL
) ENGINE = INNODB;

CREATE TABLE region (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL
) ENGINE = INNODB;

CREATE TABLE hospital (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          `name` VARCHAR(50) NOT NULL,
                          region_id int NOT NULL,
                          City_id INT NOT NULL,
                          FOREIGN KEY (`City_id`)
                              REFERENCES `city` (`id`),
                          FOREIGN KEY (`region_id`)
                              REFERENCES `region` (`id`)
) ENGINE = INNODB;

CREATE TABLE doctor (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        email VARCHAR(50) NOT NULL,

                        degree_id INT,
                        FOREIGN KEY (`degree_id`)
                            REFERENCES `degree` (`id`),

                        hospital_id INT,
                        FOREIGN KEY (`hospital_id`)
                            REFERENCES `hospital` (`id`)
) ENGINE = INNODB;

CREATE TABLE `password` (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            pass VARCHAR(50) NOT NULL
) ENGINE = INNODB;


CREATE TABLE `patient` (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(50) NOT NULL,
                           password_id INT NOT NULL,
                           FOREIGN KEY (`password_id`)
                               REFERENCES `password` (`id`)
) ENGINE = INNODB;

CREATE TABLE heart_rhytm (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             value VARCHAR(50) NOT NULL,
                             patient_id INT NOT NULL,
                             FOREIGN KEY (`patient_id`)
                                 REFERENCES `patient` (`id`)
) ENGINE = INNODB;


CREATE TABLE medium (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        patient_id INT NOT NULL,
                        doctor_id INT NOT NULL,
                        FOREIGN KEY (`patient_id`)
                            REFERENCES `patient` (`id`),
                        FOREIGN KEY (`doctor_id`)
                            REFERENCES `doctor` (`id`)
) ENGINE = INNODB;


CREATE TABLE presure (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         value VARCHAR(50) NOT NULL,
                         patient_id INT NOT NULL,
                         FOREIGN KEY (`patient_id`)
                             REFERENCES `patient` (`id`)
) ENGINE = INNODB;


CREATE TABLE temperature (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             value VARCHAR(50) NOT NULL,
                             patient_id INT NOT NULL,
                             FOREIGN KEY (`patient_id`)
                                 REFERENCES `patient` (`id`)
) ENGINE = INNODB;

CREATE INDEX ind ON medium (doctor_id);
CREATE INDEX ind ON heart_rhytm (value);
CREATE INDEX ind ON temperature (value);
CREATE INDEX ind ON presure (value);

INSERT INTO country(name) VALUES
('Ukraine'),('Italy'),('Polland'),('USA');

INSERT INTO city(name, Country_id) VALUES
('Kyiv', 1), ('Milane', 2), ('Krakow', 3), ('Lviv', 1);

INSERT INTO region(name) VALUES
('west'), ('east'), ('north'), ('south');

INSERT INTO hospital(name, region_id, City_id) VALUES
('A', 1, 1), ('B', 3, 1), ('R', 3, 3);


INSERT INTO degree(name) VALUES
('low'), ('medium'), ('high');

INSERT INTO doctor(`name`, `email`, `degree_id`, `hospital_id`) VALUES
('Ivan', '123@321', 1, 3), ('Taras', 'ads@3ads1', 3, 1),
('Nazar', 'Naz@S132', 2, 2), ('Svitlana', 'Sv@Tl21', 1, 1);

INSERT INTO password(pass) VALUES
('213124'), ('dsgq2rt135'), ('v233521f'), ('1f356hv125');

INSERT INTO patient(name, password_id) VALUES
('Alex', 1), ('Oleg', 2), ('James', 3), ('Ivan', 4);

INSERT INTO medium(patient_id, doctor_id) VALUES
(1, 1), (1,3), (2,1), (2,4), (4,2), (3, 2);

INSERT INTO temperature(value, patient_id) VALUES
(36, 1), (37, 2), (35, 1), (38, 3), (36, 4);


INSERT INTO presure(value, patient_id) VALUES
('120/80', 3), ('125/80', 1), ('110/90', 2), ('120/80', 4);

INSERT INTO heart_rhytm(value, patient_id) VALUES
(70 , 1), (120, 4), (110, 3), (90, 2);