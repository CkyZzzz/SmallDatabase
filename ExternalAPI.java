package database;

import java.util.*;
import java.util.List;

public class ExternalAPI {
    private Database database = null;

    public String handleInput(String input) {
        if (input.substring(input.length() - 1).equals(";")) {
            input = input.substring(0, input.length() - 1);
        }
        if (input.startsWith(Constants.CREATE_DATABASE_HEADER)) {
            // CREATE DATABASE database_name;
            String databaseName = input.substring(Constants.CREATE_DATABASE_HEADER.length());
            database = new Database(databaseName);
            return "Create Database Successfully";
        } else if (input.startsWith(Constants.CREATE_TABLE_HEADER)) {
            /*
                CREATE TABLE table_name (
                    column_name1 data_type(size),
                    column_name2 data_type(size),
                    column_name3 data_type(size),
                    ....
                );
            */
            if (database == null) {
                return "Error: Database Not Found";
            }
            input = input.replace("\n", "");
            String rest = input.substring(Constants.CREATE_TABLE_HEADER.length());
            int firstIndexLeftParentheses = rest.indexOf("(");
            String tableName = rest.substring(0, firstIndexLeftParentheses - 1);
            rest = rest.substring(firstIndexLeftParentheses + 1, rest.length() - 1);
            String[] groups = rest.split(",");
            List<String> attributes = new ArrayList<>();
            List<String> dataTypes = new ArrayList<>();
            String primaryKey = "";
            for (String group: groups) {
                String[] attributeAndDataType = group.split(" ");
                attributes.add(attributeAndDataType[0]);
                dataTypes.add(attributeAndDataType[1]);
                if (attributeAndDataType.length > 2 && attributeAndDataType[2].equals("PRIMARY_KEY")) {
                    primaryKey = attributeAndDataType[0];
                }
            }
            Table table = new Table(tableName, primaryKey, attributes, dataTypes);
            database.getTables().put(tableName, table);
            return "Create Table Successfully";
        } else if (input.startsWith(Constants.DROP_DATABASE_HEADER)) {
            // DROP DATABASE database_name;
            database = null;
            return "Drop Database Successfully";
        } else if (input.startsWith(Constants.DROP_TABLE_HEADER)) {
            // DROP TABLE table_name
            String tableName = input.substring(Constants.DROP_TABLE_HEADER.length());
            if (!database.getTables().containsKey(tableName)) {
                return "Error: Table Not Found";
            }
            database.getTables().remove(tableName);
            return "Drop Table Successfully";
        } else if (input.startsWith(Constants.SELECT_HEADER)) {
            /*
                SELECT column_name,column_name
                FROM table_name;

                SELECT column_name,column_name
                FROM table_name
                WHERE column_name operator value;

                SELECT column_name,column_name
                FROM table_name
                ORDER BY column_name ASC|DESC;

                SELECT column_name, aggregate_function(column_name)
                FROM table_name
                WHERE column_name operator value
                GROUP BY column_name;
             */
            String[] lines = input.split("\n");
            List<String> columnNames = null;
            String tableName = "";
            String groupByAttribute = "";
            String orderByAttribute = "";
            String order = "ASC";
            List<List<List<String>>> orList = new ArrayList<>();
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].startsWith(Constants.SELECT_HEADER)) {
                    columnNames = Arrays.asList(lines[i].substring(Constants.SELECT_HEADER.length()).split(","));
                } else if (lines[i].startsWith(Constants.FROM_HEADER)) {
                    tableName = lines[i].substring(Constants.FROM_HEADER.length());
                } else if (lines[i].startsWith(Constants.WHERE_HEADER)) {
                    lines[i] = lines[i].substring(Constants.WHERE_HEADER.length());
                    String[] ors = lines[i].split(" OR ");
                    for (int j = 0; j < ors.length; j++) {
                        String[] ands = ors[j].split(" AND ");
                        List<List<String>> andList = new ArrayList<>();
                        for (int k = 0; k < ands.length; k++) {
                            for (int l = 0; l < ands[k].length(); l++) {
                                if (ands[k].charAt(l) == '=' || ands[k].charAt(l) == '>' || ands[k].charAt(l) == '<' || ands[k].charAt(l) == '!') {
                                    int start = l;
                                    int end = l;
                                    while (ands[k].charAt(end) == '=' || ands[k].charAt(end) == '>' || ands[k].charAt(end) == '<' || ands[k].charAt(end) == '!') {
                                        end++;
                                    }
                                    andList.add(Arrays.asList(ands[k].substring(0, start), ands[k].substring(start, end), ands[k].substring(end)));
                                    break;
                                }
                            }
                        }
                        orList.add(andList);
                    }
                } else if (lines[i].startsWith(Constants.GROUP_BY_HEADER)) {
                    groupByAttribute = lines[i].substring(Constants.GROUP_BY_HEADER.length());
                } else if (lines[i].startsWith(Constants.ORDER_BY_HEADER)) {
                    String[] attributeAndOrder = lines[i].substring(Constants.ORDER_BY_HEADER.length()).split(" ");
                    orderByAttribute = attributeAndOrder[0];
                    if (attributeAndOrder.length > 1) {
                        order = attributeAndOrder[1];
                    }
                } else {
                    return "Error: Invalid Key Word in " + lines[i];
                }
            }
            List<List<String>> results = null;
            if (groupByAttribute.equals("")) {
                results = database.getTable(tableName).search(columnNames, orList);
            } else {
                results = database.getTable(tableName).group(columnNames, groupByAttribute, orList);
            }
            if (!orderByAttribute.equals("")) {
                database.getTable(tableName).order(columnNames, orderByAttribute, order, results);
            }
            for (List<String> result: results) {
                for (int i = 0; i < result.size(); i++) {
                    if (i == result.size() - 1) {
                        System.out.print(result.get(i));
                    } else {
                        System.out.print(result.get(i) + " ");
                    }
                }
                System.out.println();
            }
            return "Query Database Successfully";
        } else if (input.startsWith(Constants.INSERT_INTO_HEADER)) {
            // INSERT INTO table_name (column1,column2,column3,...)
            // VALUES (value1,value2,value3,...);
            input = input.replace("\n", "");
            String rest = input.substring(Constants.INSERT_INTO_HEADER.length());
            int firstIndexSpace = rest.indexOf(" ");
            String tableName = rest.substring(0, firstIndexSpace);
            if (!database.getTables().containsKey(tableName)) {
                return "Error: Table Not Found";
            }
            int firstIndexLeftParentheses = rest.indexOf("(");
            int firstIndexRightParentheses = rest.indexOf(")");
            String[] columnNames = rest.substring(firstIndexLeftParentheses + 1, firstIndexRightParentheses).split(",");
            rest = rest.substring(firstIndexRightParentheses + 1);
            firstIndexLeftParentheses = rest.indexOf("(");
            firstIndexRightParentheses = rest.indexOf(")");
            String[] values = rest.substring(firstIndexLeftParentheses + 1, firstIndexRightParentheses).split(",");
            if (columnNames.length != values.length) {
                return "Error: Invalid Insert Operation";
            }
            boolean returnVal = database.getTable(tableName).insert(columnNames, values);
            if (!returnVal) {
                return "Error: Primary Key is Null or Duplicate";
            }
            return "Insert One Row to Database Successfully";
        } else if (input.startsWith(Constants.UPDATE_HEADER)) {
            /*** Need Work ***/
            // UPDATE table_name
            // SET column1=value1,column2=value2,...
            // WHERE some_column=some_value;
            return "Successful Database Operation";
        } else if (input.startsWith(Constants.DELETE_FROM_HEADER)) {
            /*** Need Work ***/
            // DELETE FROM table_name
            // WHERE some_column=some_value;
            return "Successful Database Operation";
        } else {
            return "Invalid Database Operation";
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        ExternalAPI e = new ExternalAPI();
        System.out.println("Enter SQL query:");
        String query = "";
        while (s.hasNext()) {
            String input = s.nextLine();
            if (query.length() == 0) {
                query = input;
            } else {
                query = query + "\n" + input;
            }
            if (query.charAt(query.length() - 1) == ';') {
                System.out.println(e.handleInput(query));
                System.out.println("Enter next SQL query:");
                query = "";
            }
            if (input.equals("exit")) break;
        }
    }
}
