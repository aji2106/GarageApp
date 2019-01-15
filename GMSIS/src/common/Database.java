/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static final Database INSTANCE = new Database();

    private Connection connection = null;

    public Database() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Database connection failed!", ex);
        }
    }

    public void test_database() {

        Statement statement;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate("drop table if exists `test`");
            statement.executeUpdate("create table `test` (`id` integer, `name` string)");
            statement.executeUpdate("insert into `test` values('1', 'test 1')");
            statement.executeUpdate("insert into `test` values('2', 'test 2')");

            ResultSet rs = statement.executeQuery("select * from `test`");

            System.out.println("id	name");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "	" + rs.getString("name"));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void createConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Database connection failed!", ex);
        }
    }

    public void executeUpdate(String query) throws SQLException {

        if (connection != null) {
            createConnection();
        }

        Statement statement;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

}
