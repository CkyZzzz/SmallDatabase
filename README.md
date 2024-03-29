# SmallDatabase
Small Database in Java  
A small database with functions:  
&emsp;Create a database  
&emsp;Create tables  
&emsp;Insert rows into tables  
&emsp;Search with SQL key words: SELECT, FROM, WHERE, ORDER BY, GROUP BY.  
  
Time Complexity:  
&emsp;create a database: O(1)  
&emsp;create tables: O(n) and n = number of attributes in this table.  
&emsp;insert rows into tables: O(n) and n = number of attributes in this insert operation.  
&emsp;search with SQL key words SELECT, FROM: O(mn) and  
&emsp;&emsp;&emsp;&emsp;m = the number of attributes in SELECT statement,  
&emsp;&emsp;&emsp;&emsp;n = the number of rows in this table.  
&emsp;search with SQL key words SELECT, FROM, WHERE: O(mn) and  
&emsp;&emsp;&emsp;&emsp;m = the number of attributes in SELECT statement,  
&emsp;&emsp;&emsp;&emsp;n = the number of rest rows filtered by WHERE statement.  
&emsp;search with SQL key words SELECT, FROM, GROUP BY: O(mn) and  
&emsp;&emsp;&emsp;&emsp;m = the number of attributes in SELECT statement,  
&emsp;&emsp;&emsp;&emsp;n = the number of rest rows filtered by GROUP BY statement.  
&emsp;search with SQL key words SELECT, FROM, ORDER BY: O(mn + logn) and  
&emsp;&emsp;&emsp;&emsp;m = the number of attributes in SELECT statement,  
&emsp;&emsp;&emsp;&emsp;n = the number of rows in this table.  
&emsp;search with SQL key words SELECT, FROM, WHERE, GROUP BY: O(mn) and  
&emsp;&emsp;&emsp;&emsp;m = the number of attributes in SELECT statement,  
&emsp;&emsp;&emsp;&emsp;n = the number of rest rows filtered by WHERE statement and GROUP BY statement.  
&emsp;search with SQL key words SELECT, FROM, WHERE, ORDER BY: O(mn + logn) and  
&emsp;&emsp;&emsp;&emsp;m = the number of attributes in SELECT statement,  
&emsp;&emsp;&emsp;&emsp;n = the number of rest rows filtered by WHERE statement.  
&emsp;search with SQL key words SELECT, FROM, GROUP BY, ORDER BY: O(mn + logn) and  
&emsp;&emsp;&emsp;&emsp;m = the number of attributes in SELECT statement,  
&emsp;&emsp;&emsp;&emsp;n = the number of rest rows filtered by GROUP BY statement.  
&emsp;search with SQL key words SELECT, FROM, WHERE, GROUP BY, ORDER BY: O(mn + logn) and  
&emsp;&emsp;&emsp;&emsp;m = the number of attributes in SELECT statement,  
&emsp;&emsp;&emsp;&emsp;n = the number of rest rows filtered by WHERE statement and GROUP BY statement.  
  
Output and Test:  
The two tables shown below are tables that added into the database in Test.java and also input by hand.
![image](https://github.com/CkyZzzz/SmallDatabase/blob/master/images/tables.png)
  
Output through adding those tables and quering by hand in console(You can do it in ExternalAPI.java):  
![image](https://github.com/CkyZzzz/SmallDatabase/blob/master/images/console_output_1.png)  
![image](https://github.com/CkyZzzz/SmallDatabase/blob/master/images/console_output_2.png)
  
Output through Test.java:  
![image](https://github.com/CkyZzzz/SmallDatabase/blob/master/images/console_output_3.png)
