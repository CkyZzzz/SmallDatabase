# SmallDatabase
Small Database in Java  
A small database with functions:  
  create a database  
  create tables  
  insert rows into tables  
  search with SQL key words: SELECT, FROM, WHERE, ORDER BY, GROUP BY.  
  
Time Complexity:  
  create a database: O(1)  
  create tables: O(n) and n = number of attributes in this table.  
  insert rows into tables: O(n) and n = number of attributes in this insert operation.  
  search with SQL key words SELECT, FROM: O(mn) and  
                                          m = the number of attributes in SELECT statement, 
                                          n = the number of rows in this table.
  search with SQL key words SELECT, FROM, WHERE: O(mn) and 
                                          m = the number of attributes in SELECT statement, 
                                          n = the number of rest rows filtered by WHERE statement.
  search with SQL key words SELECT, FROM, GROUP BY: O(mn) and 
                                          m = the number of attributes in SELECT statement, 
                                          n = the number of rest rows filtered by GROUP BY statement.
  search with SQL key words SELECT, FROM, ORDER BY: O(mn + logn) and 
                                          m = the number of attributes in SELECT statement, 
                                          n = the number of rows in this table.
  search with SQL key words SELECT, FROM, WHERE, GROUP BY: O(mn) and 
                                          m = the number of attributes in SELECT statement, 
                                          n = the number of rest rows filtered by WHERE statement and GROUP BY statement.
  search with SQL key words SELECT, FROM, WHERE, ORDER BY: O(mn + logn) and 
                                          m = the number of attributes in SELECT statement, 
                                          n = the number of rest rows filtered by WHERE statement.
  search with SQL key words SELECT, FROM, GROUP BY, ORDER BY: O(mn + logn) and 
                                          m = the number of attributes in SELECT statement, 
                                          n = the number of rest rows filtered by GROUP BY statement.
  search with SQL key words SELECT, FROM, WHERE, GROUP BY, ORDER BY: O(mn + logn) and 
                                          m = the number of attributes in SELECT statement, 
                                          n = the number of rest rows filtered by WHERE statement and GROUP BY statement.
  
