package database;

import java.util.*;

public class Database {
    private String databaseName;
    private Map<String, Table> tables;
    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.tables = new HashMap<>();
    }

    public Map<String, Table> getTables() {
        return this.tables;
    }

    public Table getTable(String tableName) {
        return this.tables.get(tableName);
    }

    public String toString() {
        String result = "Database Name: " + databaseName + "\n";
        for (String tableName: tables.keySet()) {
            result += "{\n" + tables.get(tableName).toString() + "\n}\n";
        }
        return result.substring(0, result.length() - 1);
    }
}
