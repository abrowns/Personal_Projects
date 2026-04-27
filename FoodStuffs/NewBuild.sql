-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Food_Stuffs
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Food_Stuffs
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Food_Stuffs` DEFAULT CHARACTER SET utf8 ;
USE `Food_Stuffs` ;

-- -----------------------------------------------------
-- Table `Customers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Customers` ;

CREATE TABLE IF NOT EXISTS `Customers` (
  `CustID` INT NOT NULL AUTO_INCREMENT,
  `First_Name` VARCHAR(25) NULL,
  `Last_Name` VARCHAR(25) NULL,
  `Phone` VARCHAR(12) NULL,
  `Email` VARCHAR(45) NULL,
  `DoB` DATE NULL,
  `Username` VARCHAR(15) NOT NULL,
  `Show-name` VARCHAR(15) NULL,
  PRIMARY KEY (`CustID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Orders` ;

CREATE TABLE IF NOT EXISTS `Orders` (
  `OrderID` INT NOT NULL AUTO_INCREMENT,
  `CustID` INT NOT NULL,
  `OrderDate` DATE NOT NULL,
  `OrderTime` TIME NOT NULL,
  `Staff_Hourly` DECIMAL NOT NULL,
  PRIMARY KEY (`OrderID`),
  INDEX `fk_Orders_Customers_idx` (`CustID` ASC) VISIBLE,
  CONSTRAINT `fk_Orders_Customers`
    FOREIGN KEY (`CustID`)
    REFERENCES `Customers` (`CustID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Food_Items`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Food_Items` ;

CREATE TABLE IF NOT EXISTS `Food_Items` (
  `FoodItemID` INT NOT NULL AUTO_INCREMENT,
  `Item_Name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`FoodItemID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Ingredients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Ingredients` ;

CREATE TABLE IF NOT EXISTS `Ingredients` (
  `IngredientID` INT NOT NULL AUTO_INCREMENT,
  `Fiber` INT UNSIGNED NOT NULL,
  `Carbohydrates` INT UNSIGNED NOT NULL,
  `Glucose` INT UNSIGNED NOT NULL,
  `Fats` INT UNSIGNED NOT NULL,
  `Protein` INT UNSIGNED NOT NULL,
  `Calories` INT UNSIGNED NOT NULL,
  `Cost` DECIMAL UNSIGNED NOT NULL,
  `Ingredient_Name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`IngredientID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Meals`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Meals` ;

CREATE TABLE IF NOT EXISTS `Meals` (
  `MealID` INT NOT NULL AUTO_INCREMENT,
  `Meal_Name` VARCHAR(45) NOT NULL,
  `Meal_Description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`MealID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Diets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Diets` ;

CREATE TABLE IF NOT EXISTS `Diets` (
  `DietID` INT NOT NULL AUTO_INCREMENT,
  `Diet_Name` VARCHAR(45) NOT NULL,
  `Diet_Description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`DietID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Customer_Meals`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Customer_Meals` ;

CREATE TABLE IF NOT EXISTS `Customer_Meals` (
  `CustID` INT NOT NULL,
  `MealID` INT NOT NULL,
  PRIMARY KEY (`CustID`, `MealID`),
  INDEX `fk_Customers_has_Meals_Meals1_idx` (`MealID` ASC) VISIBLE,
  INDEX `fk_Customers_has_Meals_Customers1_idx` (`CustID` ASC) VISIBLE,
  CONSTRAINT `fk_Customers_has_Meals_Customers1`
    FOREIGN KEY (`CustID`)
    REFERENCES `Customers` (`CustID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Customers_has_Meals_Meals1`
    FOREIGN KEY (`MealID`)
    REFERENCES `Meals` (`MealID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Customer_Diets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Customer_Diets` ;

CREATE TABLE IF NOT EXISTS `Customer_Diets` (
  `CustID` INT NOT NULL,
  `DietID` INT NOT NULL,
  PRIMARY KEY (`CustID`, `DietID`),
  INDEX `fk_Customers_has_Diets_Diets1_idx` (`DietID` ASC) VISIBLE,
  INDEX `fk_Customers_has_Diets_Customers1_idx` (`CustID` ASC) VISIBLE,
  CONSTRAINT `fk_Customers_has_Diets_Customers1`
    FOREIGN KEY (`CustID`)
    REFERENCES `Customers` (`CustID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Customers_has_Diets_Diets1`
    FOREIGN KEY (`DietID`)
    REFERENCES `Diets` (`DietID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Order_Meals`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Order_Meals` ;

CREATE TABLE IF NOT EXISTS `Order_Meals` (
  `OrderID` INT NOT NULL,
  `MealID` INT NOT NULL,
  PRIMARY KEY (`OrderID`, `MealID`),
  INDEX `fk_Orders_has_Meals_Meals1_idx` (`MealID` ASC) VISIBLE,
  INDEX `fk_Orders_has_Meals_Orders1_idx` (`OrderID` ASC) VISIBLE,
  CONSTRAINT `fk_Orders_has_Meals_Orders1`
    FOREIGN KEY (`OrderID`)
    REFERENCES `Orders` (`OrderID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Orders_has_Meals_Meals1`
    FOREIGN KEY (`MealID`)
    REFERENCES `Meals` (`MealID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Meal_FoodItems`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Meal_FoodItems` ;

CREATE TABLE IF NOT EXISTS `Meal_FoodItems` (
  `MealID` INT NOT NULL,
  `FoodItemID` INT NOT NULL,
  `Cook_Start_Time` TIME NOT NULL,
  `Cook_End_Time` TIME NOT NULL,
  PRIMARY KEY (`MealID`, `FoodItemID`),
  INDEX `fk_Meals_has_Food_Items_Food_Items1_idx` (`FoodItemID` ASC) VISIBLE,
  INDEX `fk_Meals_has_Food_Items_Meals1_idx` (`MealID` ASC) VISIBLE,
  CONSTRAINT `fk_Meals_has_Food_Items_Meals1`
    FOREIGN KEY (`MealID`)
    REFERENCES `Meals` (`MealID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Meals_has_Food_Items_Food_Items1`
    FOREIGN KEY (`FoodItemID`)
    REFERENCES `Food_Items` (`FoodItemID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `FoodItem_Ingredients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `FoodItem_Ingredients` ;

CREATE TABLE IF NOT EXISTS `FoodItem_Ingredients` (
  `FoodItemID` INT NOT NULL,
  `IngredientID` INT NOT NULL,
  `Measurements` DECIMAL UNSIGNED NOT NULL,
  PRIMARY KEY (`FoodItemID`, `IngredientID`),
  INDEX `fk_Food_Items_has_Ingredients_Ingredients1_idx` (`IngredientID` ASC) VISIBLE,
  INDEX `fk_Food_Items_has_Ingredients_Food_Items1_idx` (`FoodItemID` ASC) VISIBLE,
  CONSTRAINT `fk_Food_Items_has_Ingredients_Food_Items1`
    FOREIGN KEY (`FoodItemID`)
    REFERENCES `Food_Items` (`FoodItemID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Food_Items_has_Ingredients_Ingredients1`
    FOREIGN KEY (`IngredientID`)
    REFERENCES `Ingredients` (`IngredientID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Ingredients_Inside_Ingredients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Ingredients_Inside_Ingredients` ;

CREATE TABLE IF NOT EXISTS `Ingredients_Inside_Ingredients` (
  `IngredientID1` INT NOT NULL,
  `IngredientID2` INT NOT NULL,
  PRIMARY KEY (`IngredientID1`, `IngredientID2`),
  INDEX `fk_Ingredients_has_Ingredients_Ingredients2_idx` (`IngredientID2` ASC) VISIBLE,
  INDEX `fk_Ingredients_has_Ingredients_Ingredients1_idx` (`IngredientID1` ASC) VISIBLE,
  CONSTRAINT `fk_Ingredients_has_Ingredients_Ingredients1`
    FOREIGN KEY (`IngredientID1`)
    REFERENCES `Ingredients` (`IngredientID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Ingredients_has_Ingredients_Ingredients2`
    FOREIGN KEY (`IngredientID2`)
    REFERENCES `Ingredients` (`IngredientID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Ingredient_Diets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Ingredient_Diets` ;

CREATE TABLE IF NOT EXISTS `Ingredient_Diets` (
  `IngredientID` INT NOT NULL,
  `DietID` INT NOT NULL,
  PRIMARY KEY (`IngredientID`, `DietID`),
  INDEX `fk_Ingredients_has_Diets_Diets1_idx` (`DietID` ASC) VISIBLE,
  INDEX `fk_Ingredients_has_Diets_Ingredients1_idx` (`IngredientID` ASC) VISIBLE,
  CONSTRAINT `fk_Ingredients_has_Diets_Ingredients1`
    FOREIGN KEY (`IngredientID`)
    REFERENCES `Ingredients` (`IngredientID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Ingredients_has_Diets_Diets1`
    FOREIGN KEY (`DietID`)
    REFERENCES `Diets` (`DietID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


INSERT INTO `Food_Stuffs`.`Customers` (`First_Name`, `Last_Name`, `Phone`, `Email`, `DoB`, `Username`)
VALUES
('Alice', 'Turner', '607-894-2555', 'alice.turner@example.com', '1990-04-12', 'C_1'),
('Brian', 'Smith', '622-354-3658', 'brian.smith@example.com', '1985-11-22', 'C_2'),
('Chloe', 'Adams', '607-345-9875', 'chloe.adams@example.com', '2001-06-30', 'C_3'),
('David', 'Lee', '648-799-4633', 'david.lee@example.com', '1997-03-18', 'C_4'),
('Emma', 'Wright', '222-678-2252', 'emma.wright@example.com', '1999-12-09', 'C_5'),
('Felix', 'Nguyen', '447-872-3765', 'felix.nguyen@example.com', '1994-01-05', 'C_6'),
('Grace', 'Howard', '908-244-6565', 'grace.howard@example.com', '2002-08-27', 'C_7'),
('Hannah', 'Patel', '697-800-4735', 'hannah.patel@example.com', '1988-09-14', 'C_8'),
('Ian', 'Brooks', '777-888-2445', 'ian.brooks@example.com', '1991-07-07', 'C_9'),
('Julia', 'Cole', '607-943-7884', 'julia.cole@example.com', '1996-02-23', 'C_10');


INSERT INTO `Food_Stuffs`.`Orders` (`CustID`, `OrderDate`, `OrderTime`, `Staff_Hourly`)
VALUES
(1, '2024-01-10', '12:30:00', 15.50),
(2, '2024-01-11', '14:10:00', 16.00),
(3, '2024-01-11', '18:45:00', 14.25),
(4, '2024-01-12', '11:05:00', 17.00),
(5, '2024-01-12', '19:20:00', 15.75),
(6, '2024-01-13', '09:40:00', 16.25),
(7, '2024-01-13', '13:15:00', 15.00),
(8, '2024-01-14', '17:50:00', 16.50),
(9, '2024-01-14', '20:05:00', 14.75),
(10,'2024-01-15', '08:25:00', 15.25);


INSERT INTO `Food_Stuffs`.`Food_Items` (`Item_Name`)
VALUES
('Chicken Breast'),
('Brown Rice'),
('Broccoli'),
('Olive Oil'),
('Banana'),
('Oatmeal'),
('Ground Beef'),
('Salmon Fillet'),
('Avocado'),
('Tomato'),
('Parmesan Chicken');


INSERT INTO `Food_Stuffs`.`Ingredients` 
(`Fiber`, `Carbohydrates`, `Glucose`, `Fats`, `Protein`, `Calories`, `Cost`, `Ingredient_Name`)
VALUES
(3, 0, 0, 1, 0, 0,     1.20, 'Salt'),
(2, 10, 1, 0, 4, 0,     0.50, 'Garlic'),
(4, 5, 2, 1, 1, 44,    0.80, 'Onion'),
(6, 20, 3, 2, 1, 25,    2.10, 'Carrot'),
(8, 25, 5, 0, 1, 7,      1.90, 'Raw Spinach'),
(1, 0, 0, 14, 0, 102,    3.50, 'Butter'),
(5, 15, 3, 10, 7, 113,   2.70, 'Cheddar Cheese'),
(7, 30, 8, 1, 1, 90,     2.20, 'Apple'),
(9, 40, 12, 0, 2, 160,    1.60, 'Potato'),
(3, 35, 18, 0, 0, 6,     1.00, 'Strawberry'),
(5, 17, 18, 0, 0, 6,     1.00, 'Parmesan Cheese'),
(5, 35, 21, 0, 0, 6,     2.50, 'Peanut Butter'),
(5, 7, 14, 0, 0, 6,     1.00, 'Peanuts'),
(5, 47, 67, 0, 0, 226,     3.50, 'Strawberry Jelly'),
(5, 27, 40, 0, 0, 156,     3.00, 'Grape Jelly'),
(5, 2, 28, 0, 0, 16,     0.10, 'Grape'),
(5, 53, 45, 0, 0, 125,     3.75, 'Applesauce'),
(5, 5, 4, 0, 105, 95,     4.75, 'Milk'),
(5, 5, 4, 0, 0, 95,     4.75, 'Cinnamon');


INSERT INTO `Food_Stuffs`.`Meals` (`Meal_Name`, `Meal_Description`)
VALUES
('Grilled Chicken Plate', 'Chicken, rice, and vegetables'),
('Beef Bowl', 'Ground beef with rice and sauce'),
('Salmon Special', 'Salmon with greens and quinoa'),
('Veggie Mix', 'Mixed seasonal vegetables'),
('Oat Bowl', 'Oats with fruit toppings'),
('Breakfast Platter', 'Eggs, toast, and sides'),
('Pasta Primavera', 'Vegetarian pasta with vegetables'),
('Steak Classic', 'Steak with potatoes'),
('Avocado Toast', 'Toast topped with avocado and spices'),
('Fruit Medley', 'Assorted fresh fruits'),
('Parmesan Chicken Plate', 'Chicken breast topped with parmesan cheese, with a rice bowl side');


INSERT INTO `Food_Stuffs`.`Diets` (`Diet_name`, `Diet_Description`)
VALUES
('Keto', 'Low carb, high fat'),
('Vegan', 'Plant-based diet'),
('Vegetarian', 'No meat products'),
('Paleo', 'Prehistoric-style whole foods'),
('Low Carb', 'Reduced carbohydrate intake'),
('Gluten Free', 'No gluten-containing foods'),
('High Protein', 'Focused on protein intake'),
('Mediterranean', 'Whole grains, vegetables, healthy fats'),
('Low Fat', 'Restricted fat consumption'),
('Balanced', 'Standard balanced diet plan');


INSERT INTO `Food_Stuffs`.`Customer_Meals` (`CustID`, `MealID`)
VALUES
(1,1),(2,2),(3,3),(4,4),(5,5),
(6,6),(7,7),(8,8),(9,9),(10,10),
(1,2), (1,3), (2,4), (2,1), (2,3),
(3,10), (4,7), (4,6), (3,2), (7,2);


INSERT INTO `Food_Stuffs`.`Customer_Diets` (`CustID`, `DietID`)
VALUES
(1,1),(2,2),(3,3),(4,4),(5,5),
(6,6),(7,7),(8,8),(9,9),(10,10), (1,3);


INSERT INTO `Food_Stuffs`.`Order_Meals` (`OrderID`, `MealID`)
VALUES
(1,1),(1,3),(1,5),(2,3),(3,2),(4,4),(5,6),
(6,5),(7,7),(8,8),(9,9),(10,10);


INSERT INTO `Food_Stuffs`.`Meal_FoodItems` (`MealID`, `FoodItemID`, `Cook_Start_Time`, `Cook_End_Time`)
VALUES
(1,1,'01:30:29','01:30:29'),(2,7,'01:30:29','01:30:29'),(3,8,'01:30:29','01:30:29'),(4,3,'01:30:29','01:30:29'),(5,6,'01:30:29','01:30:29'),
(6,4,'01:30:29','01:30:29'),(7,2,'01:30:29','01:30:29'),(8,9,'01:30:29','01:30:29'),(9,10,'01:30:29','01:30:29'),(10,5,'01:30:29','01:30:29'),
(1,8,'01:30:29','01:30:29'),(1,2,'01:30:29','01:30:29'),(1,4,'01:30:29','01:30:29'),(11,1,'01:30:29','01:30:29'), (11, 2,'01:30:29','01:30:29'),
(11, 11,'01:30:29','01:30:29');


INSERT INTO `Food_Stuffs`.`FoodItem_Ingredients` (`FoodItemID`, `IngredientID`, `Measurements`)
VALUES
(1,2,0.99),(2,4,0.99),(3,5,0.99),(4,1,0.99),(5,8,0.99),
(6,9,0.99),(7,3,0.99),(8,7,0.99),(9,6,0.99),(10,10,0.99), (1,1,0.99), (1,3,0.99),
(11,11,0.99), (11,1,0.99), (11,2,0.99), (11,3,0.99);


INSERT INTO `Food_Stuffs`.`Ingredients_Inside_Ingredients` (`IngredientID1`, `IngredientID2`)
VALUES
(1, 2), (8, 17), (16, 15), (10, 14), (13, 12), (6, 12), (18, 6), (18, 7), (18, 11), (19, 17);


INSERT INTO `Food_Stuffs`.`Ingredient_Diets` (`IngredientID`, `DietID`)
VALUES
(1,1),(2,2),(3,3),(4,4),(5,3),
(6,6),(7,7),(8,8),(9,9),(10,10), (2,1), (3,1), (11, 1);


