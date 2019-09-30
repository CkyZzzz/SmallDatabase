package database;

public class Test {
    public static void main(String[] args) {
        ExternalAPI e = new ExternalAPI();

        // Create Database
        e.handleInput("CREATE DATABASE MyProjectDatabase;");

        // Create Table 1 Items
        e.handleInput("CREATE TABLE Items (\n" +
                "Id Integer PRIMARY_KEY,\n" +
                "Name String,\n" +
                "Price Double,\n" +
                "Company String);");

        // Insert 3 pieces of records to Table 1
        e.handleInput("INSERT INTO Items (Id,Name,Price,Company)\n" +
                "VALUES (1,iPhone 11 Pro,1200,Apple);");
        e.handleInput("INSERT INTO Items (Id,Name,Price,Company)\n" +
                "VALUES (2,iPhone X,800,Apple);");
        e.handleInput("INSERT INTO Items (Id,Name,Price,Company)\n" +
                "VALUES (3,Galaxy S10,1100,Samsung);");

        // Search in Table 1
        e.handleInput("SELECT Company,count(Id)\n" +
                "FROM Items\n" +
                "GROUP BY Company\n" +
                "ORDER BY Company ASC;");

        // Create Table 2 Users
        e.handleInput("CREATE TABLE Users (\n" +
                "Id Integer PRIMARY_KEY,\n" +
                "Name String,\n" +
                "Age Integer,\n" +
                "Gender Character,\n" +
                "Email String,\n" +
                "Salary Double);");

        // Insert 6 pieces of records to Table 2
        e.handleInput("INSERT INTO Users (Id,Name,Age,Gender,Email,Salary)\n" +
                "VALUES (1,Chris,24,M,chris@yahoo.com,5500);");
        e.handleInput("INSERT INTO Users (Id,Name,Age,Gender,Email,Salary)\n" +
                "VALUES (2,Peter,20,M,peter@yahoo.com,6050);");
        e.handleInput("INSERT INTO Users (Id,Name,Age,Gender,Email,Salary)\n" +
                "VALUES (3,Cathy,19,W,cathy@yahoo.com,8000);");
        e.handleInput("INSERT INTO Users (Id,Name,Age,Gender,Email,Salary)\n" +
                "VALUES (4,Amy,27,W,amy@yahoo.com,10000.5);");
        e.handleInput("INSERT INTO Users (Id,Name,Age,Gender,Email,Salary)\n" +
                "VALUES (5,Sarah,30,W,sarah@yahoo.com,3000.5);");
        e.handleInput("INSERT INTO Users (Id,Name,Age,Gender,Email,Salary)\n" +
                "VALUES (6,Alice,35,W,alice@yahoo.com,7990.5);");

        // Search in Table 2
        e.handleInput("SELECT Gender,sum(Salary)\n" +
                "FROM Users\n" +
                "WHERE Age>20\n" +
                "GROUP BY Gender;");

        // Search in Table 2
        e.handleInput("SELECT *\n" +
                "FROM Users\n" +
                "WHERE Age>20\n" +
                "ORDER BY Age DESC;");
    }
}
