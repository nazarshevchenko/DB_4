package com.shevchenko;

import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;

public class App {
    private static String url = "jdbc:mysql://localhost:3306/Lab_4";
    private static String user = "root";
    private static String password = "CfhgVFxGgGVDHsG5";

    private static Connection connection=null;
    private static Statement statement=null;
    private static ResultSet resultSet=null;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            LinkedList tables = getTables();
            while(true){
                Scanner input = new Scanner(System.in);
                System.out.println("Choose command:");
                System.out.println("0 - exit");
                System.out.println("1 - insert data");
                System.out.println("2 - select data");
                System.out.println("3 - update data");
                System.out.println("4 - delete data");

                String command = input.nextLine();
                if (command.equals("0")){
                    break;
                }

                else if(command.equals("1")){
                    while (true){
                        System.out.println("Select table");
                        tables = getTables();
                        System.out.println("0 - exit");

                        for (int i = 0; i < tables.size(); i++){
                            System.out.println((i + 1) + " - " + tables.get(i));
                        }

                        command = input.nextLine();
                        if(command.equals("0")){
                            break;
                        }

                        else {
                            try {
                                int tableId = Integer.parseInt(command) - 1;

                                String table = (String) tables.get(tableId);
                                LinkedList colums = getColums(table);
                                colums.removeFirst();
                                String insert = String.join(", ", colums);

                                System.out.println("Write Values");
                                System.out.println(String.format("(%s)", insert));
                                command = input.nextLine();
                                insert(command, table);





                            }catch (Exception e) {System.out.println("What???");}
                        }

                    }
                }

                else if(command.equals("2")){
                    while (true){
                        System.out.println("Select table");
                        tables = getTables();
                        System.out.println("0 - exit");

                        for (int i = 0; i < tables.size(); i++){
                            System.out.println((i + 1) + " - " + tables.get(i));
                        }

                        command = input.nextLine();
                        if(command.equals("0")){
                            break;
                        }

                        else {
                            try {
                                int tableId = Integer.parseInt(command) - 1;

                                String table = (String) tables.get(tableId);
                                LinkedList colums = getColums(table);

                                System.out.println("Select colums");
                                System.out.println("0 - exit");
                                for (int i = 0; i < colums.size(); i++){
                                    System.out.println((i + 1) + " - "+colums.get(i));
                                }

                                System.out.println("* - all");
                                command = input.nextLine();
                                if (command.equals("*")) {
                                    System.out.println(getData(command, table));
                                }
                                else{
                                    String[] result = command.split(" ");
                                    String[] selected_colums = new String[result.length];
                                    for (int i = 0; i < result.length; i++){
                                        selected_colums[i] = (String) colums.get(Integer.parseInt(result[i]) - 1);
                                    }
                                    command = String.join(",", selected_colums);
                                    System.out.println(getData(command, table));

                                }

                            }catch (Exception e) {System.out.println("What???");}
                        }

                    }



                }

                else if(command.equals("3")){
                    while (true){
                        System.out.println("Select table");
                        tables = getTables();
                        System.out.println("0 - exit");

                        for (int i = 0; i < tables.size(); i++){
                            System.out.println((i + 1) + " - " + tables.get(i));
                        }

                        command = input.nextLine();
                        if(command.equals("0")){
                            break;
                        }

                        else {
                            try {
                                int tableId = Integer.parseInt(command) - 1;

                                String table = (String) tables.get(tableId);
                                LinkedList colums = getColums(table);
                                colums.removeFirst();
                                LinkedList<String> update = new LinkedList<>();

                                System.out.println("Write id");
                                command = input.nextLine();
                                String id = command;
                                System.out.println(getById(table, command));

                                for (int i = 0; i < colums.size(); i++){
                                    System.out.println(colums.get(i));
                                    command = input.nextLine();
                                    update.add(String.format("%s = '%s'", colums.get(i), command));
                                }
                                update(String.join(", ", update), id, table);


                            }catch (Exception e) {System.out.println("What???");}
                        }

                    }

                }

                else if(command.equals("4")){
                    while (true){
                        System.out.println("Select table");
                        tables = getTables();
                        System.out.println("0 - exit");

                        for (int i = 0; i < tables.size(); i++){
                            System.out.println((i + 1) + " - " + tables.get(i));
                        }

                        command = input.nextLine();
                        if(command.equals("0")){
                            break;
                        }

                        else {
                            try {
                                int tableId = Integer.parseInt(command) - 1;

                                String table = (String) tables.get(tableId);

                                System.out.println(getData("*", table));
                                System.out.println("Select rows by id to delete");
                                command = input.nextLine();
                                delete(command, table);

                            }catch (Exception e) {System.out.println("What???");}
                        }

                    }





                }
                else {
                    System.out.println("What???");
                }
            }




        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (resultSet != null) try {
                resultSet.close();
            }catch (SQLException e){}

            if (statement != null) try {
                statement.close();
            }catch (SQLException e){}

            if (connection != null) try {
                connection.close();
            }catch (SQLException e){}
        }
    }

    private static LinkedList getTables() throws SQLException {
        resultSet = statement.executeQuery("Show tables");
        LinkedList<String> linkedList = new LinkedList<>();
        while (resultSet.next()){
            linkedList.add(resultSet.getString(1));
        }
        return linkedList;
    }

    private static LinkedList getColums(String table) throws SQLException {

        String request = String.format("SELECT * FROM %s", table);
        resultSet = statement.executeQuery(request);
        ResultSetMetaData rsmd = resultSet.getMetaData();

        LinkedList<String> linkedList = new LinkedList<>();
        int count = rsmd.getColumnCount();
        for(int i = 1; i<=count; i++) {
            linkedList.add(rsmd.getColumnName(i));
        }
        return linkedList;
    }

    private static LinkedList getData(String colums, String table) throws SQLException {

        String request = String.format("SELECT %s FROM %s", colums, table);
        resultSet = statement.executeQuery(request);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int count = rsmd.getColumnCount();
        LinkedList<String> linkedList = new LinkedList<>();

        while(resultSet.next()){
            LinkedList<String> row = new LinkedList<>();
            for(int i = 1; i<=count; i++) {
                row.add(resultSet.getString(i));
            }
            linkedList.add(String.valueOf(row));
        }
        return linkedList;
    }

    private static void insert(String values, String table) throws SQLException {

        LinkedList colums = getColums(table);
        colums.removeFirst();
        String value = String.join(", ", colums);

        String[] data = values.split(",");

        for (int i = 0; i < data.length; i++){
            data[i] = String.format("'%s'", data[i]);
        }

        values = String.join(", ", data);
        values = String.format("(%s)", values);

        String sql = String.format("INSERT INTO %s(%s) " +
                "VALUES %s", table, value, values);

        String request = String.format(sql);
        statement.executeUpdate(request);
    }

    private static LinkedList getById(String table, String id) throws SQLException {

        String request = String.format("SELECT * FROM %s " +
                "WHERE id = %s", table, id);

        resultSet = statement.executeQuery(request);

        ResultSetMetaData rsmd = resultSet.getMetaData();
        int count = rsmd.getColumnCount();

        LinkedList<String> linkedList = new LinkedList<>();
        while(resultSet.next()){
            LinkedList<String> row = new LinkedList<>();
            for(int i = 1; i<=count; i++) {
                row.add(resultSet.getString(i));
            }

            linkedList.add(String.valueOf(row));
        }

        return linkedList;

    }

    private static void update(String update, String id, String table) throws SQLException {

        String sql = String.format("UPDATE %s " +
                "SET %s WHERE id = %s", table, update, id);

        statement.executeUpdate(sql);
    }

    private static void delete(String id, String table) throws SQLException {

        String sql = String.format("DELETE FROM %s " +
                "WHERE id = %s", table, id);
        statement.executeUpdate(sql);

    }


}
