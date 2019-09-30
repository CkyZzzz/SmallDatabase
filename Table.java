package database;

import java.util.*;

public class Table {
    private String tableName;
    private String primaryKey;
    private Set<String> primaryKeySet;
    private Map<String, Column> cols;
    public Table(String tableName, String primaryKey, List<String> attributes, List<String> dataTypes) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.primaryKeySet = new HashSet<>();
        this.cols = new HashMap<>();
        for (int i = 0; i < attributes.size(); i++) {
            String attribute = attributes.get(i);
            String dataType = dataTypes.get(i);
            if (dataType.equals("Integer")) {
                cols.put(attribute, new Column<Integer>(attribute, dataType));
            } else if (dataType.equals("Short")) {
                cols.put(attribute, new Column<Short>(attribute, dataType));
            } else if (dataType.equals("Byte")) {
                cols.put(attribute, new Column<Byte>(attribute, dataType));
            } else if (dataType.equals("Long")) {
                cols.put(attribute, new Column<Long>(attribute, dataType));
            } else if (dataType.equals("Float")) {
                cols.put(attribute, new Column<Float>(attribute, dataType));
            } else if (dataType.equals("Double")) {
                cols.put(attribute, new Column<Double>(attribute, dataType));
            } else if (dataType.equals("Character")) {
                cols.put(attribute, new Column<Character>(attribute, dataType));
            } else if (dataType.equals("Boolean")) {
                cols.put(attribute, new Column<Boolean>(attribute, dataType));
            } else if (dataType.equals("String")) {
                cols.put(attribute, new Column<String>(attribute, dataType));
            } else {
                return;
            }
        }
    }

    public boolean insert(String[] columnNames, String[] values) {
        Map<String, String> tempMap = new HashMap<>();
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(this.primaryKey)) {
                if (values[i].equals("null") || this.primaryKeySet.contains(values[i])) {
                    return false;
                }
                this.primaryKeySet.add(values[i]);
            }
            tempMap.put(columnNames[i], values[i]);
        }
        for (String attribute: cols.keySet()) {
            if (tempMap.containsKey(attribute)) {
                cols.get(attribute).add(tempMap.get(attribute));
            } else {
                cols.get(attribute).add(null);
            }
        }
        return true;
    }

    public String toString() {
        String result = "Table Name: " + tableName + "\n";
        result += "Primary Key: " + primaryKey + "\n";
        for (String attribute: cols.keySet()) {
            Column col = cols.get(attribute);
            result += col.toString() + "\n";
        }
        return result.substring(0, result.length() - 1);
    }

    private List<Integer> filter(List<List<List<String>>> conditions) {
        List<Integer> result = new ArrayList<>();
        boolean[] valid = new boolean[this.primaryKeySet.size()];
        for (List<List<String>> condition: conditions) {
            boolean[] tempValid = new boolean[this.primaryKeySet.size()];
            boolean isFirst = true;
            for (List<String> parameters: condition) {
                boolean[] tempCheck = cols.get(parameters.get(0)).filter(parameters.get(1), parameters.get(2));
                if (isFirst) {
                    tempValid = tempCheck;
                    isFirst = false;
                } else {
                    for (int i = 0; i < tempCheck.length; i++) {
                        tempValid[i] = (tempValid[i] && tempCheck[i]);
                    }
                }
            }
            for (int i = 0; i < tempValid.length; i++) {
                valid[i] = (valid[i] || tempValid[i]);
            }
        }
        for (int i = 0; i < valid.length; i++) {
            if (valid[i]) result.add(i);
        }
        return result;
    }

    public List<List<String>> search(List<String> attributes, List<List<List<String>>> conditions) {
        List<List<String>> result = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < primaryKeySet.size(); i++) {
            indexes.add(i);
        }
        if (conditions.size() != 0) {
            indexes = filter(conditions);
        }
        for (int i = 0; i < indexes.size(); i++) {
            result.add(new ArrayList<>());
        }
        if (attributes.size() == 1 && attributes.get(0).equals("*")) {
            List<String> tempList = new ArrayList<>();
            for (String key: cols.keySet()) {
                tempList.add(key);
            }
            attributes = tempList;
        }
        for (String attribute: attributes) {
            List<String> values = cols.get(attribute).search(indexes);
            for (int i = 0; i < indexes.size(); i++) {
                result.get(i).add(values.get(i));
            }
        }
        return result;
    }

    public void order(List<String> attributes, String orderByAttribute, String orderWay, List<List<String>> result) {
        int tempIndex = 0;
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).equals(orderByAttribute)) {
                tempIndex = i;
                break;
            }
        }
        final int index = tempIndex;
        if (orderWay.equals(Constants.DESC)) {
            Collections.sort(result, (a, b) -> (b.get(index).compareTo(a.get(index))));
        } else {
            Collections.sort(result, (a, b) -> (a.get(index).compareTo(b.get(index))));
        }
    }

    public List<List<String>> group(List<String> attributes, String groupByAttribute, List<List<List<String>>> conditions) {
        boolean isFound = false;
        int indexOfGroupByAttribute = -1;
        int indexOfAggreAttribute = -1;
        String aggreFunc = "";
        String aggreAttribute = "";
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).startsWith(Constants.SUM + "(") && attributes.get(i).endsWith(")") ||
                    attributes.get(i).startsWith(Constants.COUNT + "(") && attributes.get(i).endsWith(")")) {
                aggreFunc = attributes.get(i).substring(0, attributes.get(i).indexOf("("));
                attributes.set(i, attributes.get(i).substring(attributes.get(i).indexOf("(") + 1, attributes.get(i).length() - 1));
                aggreAttribute = attributes.get(i);
                indexOfAggreAttribute = i;
            }
            if (attributes.get(i).equals(groupByAttribute)) {
                indexOfGroupByAttribute = i;
                isFound = true;
            }
        }
        if (!isFound) {
            indexOfGroupByAttribute = attributes.size();
            attributes.add(groupByAttribute);
        }
        List<List<String>> total = search(attributes, conditions);
        Map<String, String> groupByMap = new HashMap<>();
        for (List<String> tuple: total) {
            if (!groupByMap.containsKey(tuple.get(indexOfGroupByAttribute))) {
                groupByMap.put(tuple.get(indexOfGroupByAttribute), "0");
            }
            if (aggreFunc.equals(Constants.SUM)) {
                if (cols.get(aggreAttribute).getDataType().equals("Integer")) {
                    int tempValue = Integer.valueOf(tuple.get(indexOfAggreAttribute));
                    int currValue = Integer.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + tempValue));
                } else if (cols.get(aggreAttribute).getDataType().equals("Short")) {
                    short tempValue = Short.valueOf(tuple.get(indexOfAggreAttribute));
                    short currValue = Short.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + tempValue));
                } else if (cols.get(aggreAttribute).getDataType().equals("Byte")) {
                    byte tempValue = Byte.valueOf(tuple.get(indexOfAggreAttribute));
                    byte currValue = Byte.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + tempValue));
                } else if (cols.get(aggreAttribute).getDataType().equals("Long")) {
                    long tempValue = Long.valueOf(tuple.get(indexOfAggreAttribute));
                    long currValue = Long.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + tempValue));
                } else if (cols.get(aggreAttribute).getDataType().equals("Float")) {
                    float tempValue = Float.valueOf(tuple.get(indexOfAggreAttribute));
                    float currValue = Float.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + tempValue));
                } else if (cols.get(aggreAttribute).getDataType().equals("Double")) {
                    double tempValue = Double.valueOf(tuple.get(indexOfAggreAttribute));
                    double currValue = Double.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + tempValue));
                } else {
                    return new ArrayList<>();
                }
            } else {
                if (cols.get(aggreAttribute).getDataType().equals("Integer")) {
                    int currValue = Integer.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + 1));
                } else if (cols.get(aggreAttribute).getDataType().equals("Short")) {
                    short currValue = Short.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + 1));
                } else if (cols.get(aggreAttribute).getDataType().equals("Byte")) {
                    byte currValue = Byte.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + 1));
                } else if (cols.get(aggreAttribute).getDataType().equals("Long")) {
                    long currValue = Long.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + 1l));
                } else if (cols.get(aggreAttribute).getDataType().equals("Float")) {
                    float currValue = Float.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + 1.0));
                } else if (cols.get(aggreAttribute).getDataType().equals("Double")) {
                    double currValue = Double.valueOf(groupByMap.get(tuple.get(indexOfGroupByAttribute)));
                    groupByMap.put(tuple.get(indexOfGroupByAttribute), String.valueOf(currValue + 1.0));
                } else {
                    return new ArrayList<>();
                }
            }
        }
        List<List<String>> result = new ArrayList<>();
        for (String key: groupByMap.keySet()) {
            List<String> tempList = new ArrayList<>();
            tempList.add(key);
            tempList.add(groupByMap.get(key));
            result.add(tempList);
        }
        return result;
    }

    public static void main(String[] args) {
        String tableName = "Users";
        String primaryKey = "Id";
        List<String> attributes = new ArrayList<>();
        List<String> dataTypes = new ArrayList<>();
        attributes.addAll(Arrays.asList("Id", "Name", "Age", "Gender", "Email"));
        dataTypes.addAll(Arrays.asList("Integer", "String", "Integer", "Character", "String"));
        Table t = new Table(tableName, primaryKey, attributes, dataTypes);
        String[] columnNames = new String[]{"Id", "Name", "Age", "Gender", "Email"};
        String[] values1 = new String[]{"1", "Chris", "24", "M", "chris@yahoo.com"};
        String[] values2 = new String[]{"2", "Peter", "70", "M", "peter@yahoo.com"};
        String[] values3 = new String[]{"3", "Tom", "30", "M", "tom@yahoo.com"};
        String[] values4 = new String[]{"4", "Jerry", "30", "W", "jerry@yahoo.com"};
        String[] values5 = new String[]{"5", "Alex", "1", "W", "alex@yahoo.com"};
        t.insert(columnNames, values1);
        t.insert(columnNames, values2);
        t.insert(columnNames, values3);
        t.insert(columnNames, values4);
        t.insert(columnNames, values5);
        System.out.println(t.toString());

        List<String> temp1 = Arrays.asList("Age", ">=", "30");
        List<String> temp2 = Arrays.asList("Age", "<", "60");
        List<String> temp3 = Arrays.asList("Gender", "=", "W");
        List<List<String>> condition1 = new ArrayList<>();
        List<List<String>> condition2 = new ArrayList<>();
        condition1.add(temp1);
        condition1.add(temp2);
        condition2.add(temp3);
        List<List<List<String>>> conditions = new ArrayList<>();
        conditions.add(condition1);
        conditions.add(condition2);
        List<Integer> list = t.filter(conditions);
        System.out.println(list.toString());

        List<List<String>> results = t.search(Arrays.asList("Id", "Email"), conditions);
        t.order(Arrays.asList("Id", "Email"), "Email", Constants.ASC, results);
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
    }
}
