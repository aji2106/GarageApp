/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import diagrep.diagRepTable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Tanzil
 */
public class activeUsersTableHandle {

    private static Connection connection;

    static void handleTable(TableView<ActiveUsersTable> tableView) {
        ObservableList<TableColumn<ActiveUsersTable, ?>> list = tableView.getColumns();
        for (TableColumn column : list) {
            if (column.getId().equals("firstNameColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<ActiveUsersTable, String>("firstname"));
            }
            if (column.getId().equals("LastNameColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<ActiveUsersTable, String>("Surname"));
            }
            if (column.getId().equals("userIDNameColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<ActiveUsersTable, String>("ID"));
            }
            if (column.getId().equals("hourlyRateColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<ActiveUsersTable, String>("hourlyRate"));
            }
            if (column.getId().equals("adminColumn")) {
                column.setCellValueFactory(new PropertyValueFactory<ActiveUsersTable, String>("admin"));
            }
        }
    }

    public static ObservableList<ActiveUsersTable> getData(TableView<ActiveUsersTable> tableView) throws ClassNotFoundException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from User");
        ObservableList<ActiveUsersTable> ActiveUsersList = FXCollections.observableArrayList();
        while (rs.next()) {
            ActiveUsersList.add(new ActiveUsersTable(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(6)));
        }
        tableView.setItems(ActiveUsersList);
        return ActiveUsersList;
    }
}

