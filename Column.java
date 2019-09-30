package database;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private String attribute;
    private String dataType;
    private List<T> values;
    public Column(String attribute, String dataType) {
        this.attribute = attribute;
        this.dataType = dataType;
        this.values = new ArrayList<>();
    }
    public boolean add(String value) {
        if (value == null || value.equals("null")) {
            values.add(null);
            return true;
        }
        if (dataType.equals("Integer")) {
            values.add((T) Integer.valueOf(value));
            return true;
        } else if (dataType.equals("Short")) {
            values.add((T) Short.valueOf(value));
            return true;
        } else if (dataType.equals("Byte")) {
            values.add((T) Byte.valueOf(value));
            return true;
        } else if (dataType.equals("Long")) {
            values.add((T) Long.valueOf(value));
            return true;
        } else if (dataType.equals("Float")) {
            values.add((T) Float.valueOf(value));
            return true;
        } else if (dataType.equals("Double")) {
            values.add((T) Double.valueOf(value));
            return true;
        } else if (dataType.equals("Character")) {
            values.add((T) Character.valueOf(value.charAt(0)));
            return true;
        } else if (dataType.equals("Boolean")) {
            values.add((T) Boolean.valueOf(value));
            return true;
        } else if (dataType.equals("String")) {
            values.add((T) value);
            return true;
        } else {
            return false;
        }
    }

    public T get(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }

    public String toString() {
        String result = this.attribute + "(" + dataType + "): [";
        for (int i = 0; i < this.size(); i++) {
            if (i == this.size() - 1) {
                result += String.valueOf(this.get(i)) + "]";
            } else {
                result += String.valueOf(this.get(i)) + ", ";
            }
        }
        return result;
    }

    public boolean[] filter(String operator, String operand) {
        boolean[] check = new boolean[this.size()];
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) == null) continue;
            if (dataType.equals("String")) {
                if (operator.equals("=")) {
                    if (operand.equals(String.valueOf(this.get(i)))) check[i] = true;
                } else {
                    if (!operand.equals(String.valueOf(this.get(i)))) check[i] = true;
                }
            } else if (dataType.equals("Character")) {
                if (operator.equals("=")) {
                    if (operand.charAt(0) == Character.valueOf((String.valueOf(this.get(i))).charAt(0))) check[i] = true;
                } else {
                    if (operand.charAt(0) != Character.valueOf((String.valueOf(this.get(i))).charAt(0))) check[i] = true;
                }
            } else if (dataType.equals("Boolean")) {
                if (operator.equals("=")) {
                    check[i] = Boolean.valueOf(String.valueOf(this.get(i)));
                } else {
                    check[i] = !Boolean.valueOf(String.valueOf(this.get(i)));
                }
            } else if (dataType.equals("Long")) {
                if (operator.equals(">=")) {
                    if ((long) Long.valueOf(String.valueOf(this.get(i))) >= Long.valueOf(operand)) check[i] = true;
                } else if (operator.equals(">")) {
                    if ((long) Long.valueOf(String.valueOf(this.get(i))) > Long.valueOf(operand)) check[i] = true;
                } else if (operator.equals("<")) {
                    if ((long) Long.valueOf(String.valueOf(this.get(i))) < Long.valueOf(operand)) check[i] = true;
                } else if (operator.equals("<=")) {
                    if ((long) Long.valueOf(String.valueOf(this.get(i))) <= Long.valueOf(operand)) check[i] = true;
                } else {
                    if ((long) Long.valueOf(String.valueOf(this.get(i))) == Long.valueOf(operand)) check[i] = true;
                }
            } else {
                if (operator.equals(">=")) {
                    if ((double) Double.valueOf(String.valueOf(this.get(i))) >= Double.valueOf(operand)) check[i] = true;
                } else if (operator.equals(">")) {
                    if ((double) Double.valueOf(String.valueOf(this.get(i))) > Double.valueOf(operand)) check[i] = true;
                } else if (operator.equals("<")) {
                    if ((double) Double.valueOf(String.valueOf(this.get(i))) < Double.valueOf(operand)) check[i] = true;
                } else if (operator.equals("<=")) {
                    if ((double) Double.valueOf(String.valueOf(this.get(i))) <= Double.valueOf(operand)) check[i] = true;
                } else {
                    if ((double) Double.valueOf(String.valueOf(this.get(i))) == Double.valueOf(operand)) check[i] = true;
                }
            }
        }
        return check;
    }

    public String getDataType() {
        return this.dataType;
    }

    public List<String> search(List<Integer> indexes) {
        List<String> result = new ArrayList<>();
        for (int index: indexes) {
            result.add(String.valueOf(this.get(index)));
        }
        return result;
    }

    public static void main(String[] args) {
        Column<Integer> col1 = new Column<>("Id", "Integer");
        col1.add("123");
        col1.add("456");
        col1.add(null);
        col1.add("789");
        System.out.println(col1.toString());
        Column<Character> col2 = new Column<>("Gender", "Character");
        col2.add("a");
        col2.add("b");
        col2.add(null);
        col2.add("c");
        col2.add("d");
        System.out.println(col2.toString());
    }
}
