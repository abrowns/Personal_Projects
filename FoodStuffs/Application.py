'''
File: Application.py
    Contains the functions and main program to run the application

Author: Andrew Brown

'''


#       =================== Imports =======================================================

import mysql.connector
import os


#       =================== mySQL Connection ===============================================

# connector using personal password to mySQL
def getConn(pw: str) -> mysql.connector.connect:

        conn = mysql.connector.connect(
        user = "root",
        password = pw,
        database = 'food_stuffs',
        host = "localhost"
        )
        return conn


#       =================== Helpers ========================================================

# get meal name from meal ID
def getMeal(mealID: int, pw:str) -> str:
    conn = getConn(pw)
    curs = conn.cursor()
    curs.execute("SELECT m.Meal_Name FROM Meals m WHERE m.MealID = %s", mealID)

    meal = curs.fetchone()[0]
    conn.close()

    return meal

# get Order details from order id
def getOrder(OrderID: int, pw:str) -> str:
    ordDetails = "SELECT * FROM Orders o WHERE o.OrderID = %s"
    conn = getConn(pw)
    curs = conn.cursor()
    curs.execute(ordDetails, OrderID)

    order = curs.fetchall()
    return order

# get all food items from a meal
def getFoodItems(MealID: int, pw: str) -> list[int]:
    conn = getConn(pw)
    curs = conn.cursor()
    curs.execute("CALL getIngredients(%s)", MealID)
    meal = curs.fetchall()
    FoodItems = list[int]

    for item in meal:
        FoodItems.append(item)

    conn.close()
    return FoodItems

# find all meals that relate to a given string
def findMeal(Name: str, pw: str) -> list[str]:
    conn = getConn(pw)
    curse = conn.cursor()
    curse.execute('SELECT Meal_Name FROM Meals;')
    allMeals = curse.fetchall()
    meals = []
    for meal in allMeals:
        if (str(meal).lower().find(Name.lower()) != -1):
            meals.append(str(meal)[2:-2])
    
    return meals

# find all food items that relate to a given string
def find_FoodItem(Name: str, pw: str) -> list[str]:
    conn = getConn(pw)
    curse = conn.cursor()
    curse.execute('SELECT Item_Name FROM Food_Items;')
    items = curse.fetchall()
    FoodItems = []
    for item in items:
        if (str(item).lower().find(Name.lower()) != -1):
            FoodItems.append(str(item))
    
    return FoodItems

# get all macros from a single food item
def item_Macros(Name: str, pw:str) -> list[int]:
    return list[int]


# print macro details of a food item
def food_Details(Name: str, pw: str, filter: int) -> None:
    print("=================")
    print(Name)
    print("=================")
    conn = getConn(pw)
    curse = conn.cursor()
    if filter == 1: # if filter is set for meals
        curse.execute("CALL getFoodItems(%s);", (Name,)) # get all food items in meal
        allItems = curse.fetchall()
        for item in allItems:
            print("    " + str(item))
            macs = item_Macros()
            print("Fiber: %d | Carbs: %d | Glucose: %d | Fats: %d | Protein: %d | Calories: %d", 
                    macs[0],   macs[1],    macs[2],      macs[3],   macs[4],      macs[5]       )



#       =================== Actions =========================================================


# Create new user using a username with a phone number
def CreateUser(user:str, pw:str) -> None:
    conn = getConn(pw)
    curs = conn.cursor()
    args = (user,)
    curs.callproc('newUser', args) # procedure to create new user
    conn.commit()
    conn.close()
    return None

# Print user's meal history
def printMeals(CustID: int, pw: str) -> None:
    conn = getConn(pw)
    curs = conn.cursor()
    curs.execute("Call CustMeals(%s)", (CustID,))
    meals = curs.fetchall()

    if(not meals):
        print("You have no personal meals...")
        conn.close()
        return None
    
    for meal in meals:
        print(f"- {getMeal(meal, pw)}")

    conn.close()
    return None

# Print user's order history
def printOrders(CustID: int, pw:str) -> None:
    conn = getConn(pw)
    curs = conn.cursor()
    curs.execute("Call CustOrders(%s)", (CustID,))
    orders = curs.fetchall()

    if(not orders):
        print("You have no previous orders...")
        conn.close()
        return None
    
    for order in orders:
        print(f"1) {getOrder(order, pw)}")
    
    conn.close()
    return None

# search for meal or food item by name
def search(pw: str) -> None:
    print("1: Meals || 2: Food Items || 3: Both")
    filt = input("Choose a filter: ")
    if not filt.isnumeric(): # Default filter is both
        filt = "3"

    Name = input("\nSearch: ")

    found = findMeal(Name, pw)
    size = found.__len__()
    if (filt == "2"):
        found = find_FoodItem(Name, pw)
    if (filt == "3"):
        FoodI = find_FoodItem(Name,pw)
        for item in FoodI:
            found.append(item)
    

    i = 1
    for meal in found:
        print(f'{i}: {meal}')
        i += 1
    ch = input("Enter number to see food details, or leave empty to keep searching: ")
    if ch.isnumeric():
        num = int(ch)
        query = found[num - 1]
        if (filt == "3") and (num > size):
            filt = "2"      # determine if food item
        elif filt == "3":
            filt = "1"

        food_Details(query, pw, int(filt))
    else:
        os.system('clear')
        search(pw)


#       =================== Choice ===========================================================

# completes the action that the user chooses
def action(choice: int, pw: str, Cid: int) -> None:

    # See all user's meals
    if(choice == 1):
        print(f"\n{customer}'s meals:")
        print()
        printMeals(Cid, pw)

    # Search Meal / Food Item
    if (choice == 2):
        search(pw)

    # Create new meal
    if (choice == 3):
        print("Enter Meal Name: ")

    # Add new food item
    if (choice == 4):
        print("")
    
    # Add new ingredient
    if (choice == 5):
        print("")

    print()
    print("------------------------")
    print()

    return None


#       =================== Main =============================================================

if (__name__ == "__main__"):

    os.system('clear')
    pw = input("===Enter sql password===\n")
    os.system('clear')
    con = getConn(pw)
    curs = con.cursor()
    print("------------------------")

    customer = (input("Enter username or leave empty for new/guest account\n>>"))
    Guest = False
    os.system('clear')

    if (customer == ""):
        print("Hello New Customer!")
        choice = input("\nPress Enter to use guest account, or type 'y' to create a new account: ")
        customer = "Guest"
        Guest = True
        os.system('clear')

        if(choice.upper() == "Y"):
            customer = input("Enter new username (maximum 15 characters): ")
            Guest = False
        
        length = customer.__len__()
        while ( (length > 15) or (length == 0) ):
            print("Length requirement not met...")
            customer = input("Enter new username (maximum 15 characters): ")
            os.system('clear')

        CreateUser(customer, pw)


    curs.execute("CALL getCust(%s);", (customer,)) # procedure to find customer id
    custID = curs.fetchone()

    Cid = int(custID[0])

    print("------------------------")
    print(f"Hello {customer}! What would you like to do?")

    choices = ["All Meals", "Search Meal / Food Item", "Create new Meal", "Add New Food Item", "Add New Ingredient"] 
    i = 0
    for choice in choices:
        print(f"{i+1}) {choices[i]}")
        i+=1
    
    ch = input("Enter Choice or leave blank to exit: ")
    os.system('clear')

    while(ch != ""): # loop until input is empty

        if not ch.isdigit() and not ch == "" or int(ch) > choices.__len__():
            print("Invalid choice; Type a number...")
        else:
            action(int(ch), pw, Cid) # commit customer's choice
        
        print(f"What would you like to do?")
        j = 0
        for choice in choices:
            print(f"{j+1}) {choices[j]}")
            j+=1
        print()
        ch = input("Enter Choice or leave blank to exit: ")
        os.system('clear')

    print("===============")
    print("\n   Logging Out....")
    print("\n===============")

    if(Guest):
        garb = curs.fetchall() # garbage left over from previous procedure
        curs.callproc('DelUser', (Cid,))
        con.commit()
    con.close()



