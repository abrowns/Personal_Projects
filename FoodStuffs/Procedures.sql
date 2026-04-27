DELIMITER $$

USE Food_Stuffs$$

-- Get All Ingredients That Satisfy A Diet
DROP PROCEDURE IF EXISTS findIngredients;
CREATE DEFINER=`root`@`localhost` PROCEDURE `findIngredients`(IN Diet INT)
BEGIN

    SELECT id.IngredientID
    FROM Ingredient_Diets id
    WHERE id.DietID = Diet;

END $$

-- Get All Food Items in a meal
DROP PROCEDURE IF EXISTS getFoodItems;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getFoodItems`(IN MealName VARCHAR(45))
BEGIN

    SELECT fii.FoodItemID
    FROM Meal_FoodItems mfi
    JOIN Meals m
    ON m.MealID = mfi.MealID
    WHERE m.Meal_Name = MealName;

END $$

-- Get All ingredients in a Food Item
DROP PROCEDURE IF EXISTS getFoodItems;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getFoodItems`(IN FoodItem_Name VARCHAR(45))
BEGIN

    SELECT Ingredient_Name
    FROM Ingredients i
    JOIN FoodItem_Ingredients fii
    ON fii.IngredientID = i.IngredientID
    JOIN Food_Items fi
    ON fi.FoodItemID = fii.FoodItemID
    WHERE fi.Item_Name = FoodItem_Name;

END $$


-- Get All Food items that include the given ingredient
DROP Procedure IF EXISTS findFoodItems;
CREATE DEFINER=`root`@`localhost` Procedure `findFoodItems`(IN IngID int)
BEGIN

    SELECT fii.FoodItemID
    FROM FoodItem_Ingredients fii
    WHERE fii.IngredientID = IngID;

END $$


-- Get All Meals that include the given Food item
DROP Procedure IF EXISTS findMeals;
CREATE DEFINER=`root`@`localhost` Procedure `findMeals`(IN FdItemID int)
BEGIN

    SELECT mf.MealID
    FROM Meal_FoodItems mf
    WHERE mf.FoodItemID = FdItemID;

END $$

-- Get Macro details of a given ingredient
DROP Procedure IF EXISTS Details;
CREATE DEFINER=`root`@`localhost` Procedure `Details`(IN IngName VARCHAR(45))
BEGIN

    SELECT Fiber, Carbohydrates, Glucose, Fats, Protein, Calories
    FROM Ingredients i
    WHERE i.Ingredient_Name = IngName;

END $$


-- Get Customer ID from username
DROP Procedure IF EXISTS getCust;
CREATE DEFINER=`root`@`localhost` Procedure `getCust`(IN user VARCHAR(15))
BEGIN

    SELECT c.CustID
    FROM customers c
    WHERE c.Username = user;

END $$


-- Create new Customer with only username
DROP Procedure IF EXISTS newUser;
CREATE DEFINER=`root`@`localhost` Procedure `newUser`(IN user VARCHAR(15))
BEGIN
    INSERT INTO Customers (`Username`) VALUES (user);

END $$

-- Delete Customer Data
DROP Procedure IF EXISTS DelUser;
CREATE DEFINER=`root`@`localhost` Procedure `DelUser`(IN cID INT)
BEGIN
    DELETE FROM Customers WHERE CustID = cID;

END $$





-- Get a Customer's Orders
DROP PROCEDURE IF EXISTS CustOrders;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CustOrders`(IN CustID INT)
BEGIN

    SELECT o.OrderID
    From Orders o
    Where o.CustID = CustID;

END $$


-- Get a Customer's Diets
DROP PROCEDURE IF EXISTS CustDiets;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CustDiets`(IN CustID INT)
BEGIN

    SELECT cd.DietID
    From Customer_Diets cd
    Where cd.CustID = CustID;

END $$


-- Get a Customer's Meals
DROP PROCEDURE IF EXISTS CustMeals;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CustMeals`(IN CustID INT)
BEGIN

    SELECT cm.MealID
    From Customer_Meals cm
    Where cm.CustID = CustID;

END $$

DELIMITER ;


