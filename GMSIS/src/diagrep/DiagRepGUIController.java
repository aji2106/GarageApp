package diagrep;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import common.Database;
import common.HomeGUIController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import users.User;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class DiagRepGUIController implements Initializable {

    @FXML
    private TabPane tabpane_home;
    @FXML
    private ResourceBundle resources;
    @FXML
    private JFXComboBox<String> drp_time, drp_veh, drp_type, drp_mechID, drp_hourly, drp_monthly, drp_daily, drp_cusID;
    @FXML
    private URL location;
    @FXML
    private Tab tab_diagRep, tab_search;
    @FXML
    private JFXTextField txt_labour, txt_vehMile, txt_repair, txt_bill, txt_typeOfBooking, txt_booking, txt_search;
    @FXML
    private JFXDatePicker datepic_cal;
    @FXML
    private JFXButton btn_book, btn_home, btn_confirm, btn_delete, btn_edit, btn_goBack, btn_reload;
    @FXML
    private TableView<diagRepTable> table_search;
    @FXML
    private TableColumn column_cusID, column_vehReg, column_bookingDate, column_bookingID;
    @FXML
    private TextArea txtarea_otherBookings, txtarea_partsAdded;
    private ObservableList<diagRepTable> createList;
    private static final Database DB = new Database();
    private final User loggedInUser;
    private String temp = "";
    String tempWarrantyID = "";
    int warrantyID = 0;

    public DiagRepGUIController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTable();
        setDropDowns();
        setVisibilitySearch(false);
        btn_goBack.setVisible(false);
    }
//Method for adding a booking to database

    @FXML
    void book(ActionEvent event) throws ClassNotFoundException, SQLException {
        try {
            String fName = "";
            String lName = "";
            String manu = "";
            int txtVehMile = Integer.parseInt(txt_vehMile.getText());
            int txtHours = Integer.parseInt(txt_repair.getText());

            if (!drp_cusID.getSelectionModel().isEmpty() && !drp_mechID.getSelectionModel().isEmpty() && !txt_vehMile.getText().isEmpty() && !txt_repair.getText().isEmpty() && !drp_type.getSelectionModel().isEmpty() && !drp_veh.getSelectionModel().isEmpty() && datepic_cal.getValue() != null && !drp_time.getSelectionModel().isEmpty()) {
                int labourCostInPence = 0;
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery("select hourlyRate from User where id =" + drp_mechID.getValue());) {
                    labourCostInPence = txtHours * rs.getInt("hourlyRate");
                } catch (Exception e) {

                }
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery("select * from Customer where id =" + drp_cusID.getSelectionModel().getSelectedItem());) {
                    fName = rs.getString("firstname");
                    lName = rs.getString("surname");
                } catch (Exception e) {

                }
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery("select * from Vehicle where registration ='" + drp_veh.getSelectionModel().getSelectedItem() + "'");) {
                    manu = rs.getString("make");
                } catch (Exception e) {

                }
                txt_labour.setText(Double.toString((double) labourCostInPence / 100));
                DB.executeUpdate("insert into Booking (CustomerID, bill,customerFirstName,dayOfbooking,monthOfBooking,customerLastname,manufacturer,mechanicID,vehicleMileage,hoursSpent,type,vehicleRegistration,dateOfBooking,timeOfBooking) values (" + drp_cusID.getSelectionModel().getSelectedItem() + " , '" + txt_labour.getText() + "','" + fName + "' ,'" + subStringDay() + "','" + subStringMonth() + "','" + lName + "' ,'" + manu + "'" + "," + drp_mechID.getValue() + "," + txtVehMile + "," + txtHours + ",\" " + drp_type.getValue() + "\",\"" + drp_veh.getValue() + "\",'" + getDate() + "',\"" + drp_time.getValue() + "\")");
                showInfo("Booking Successfully Added");
                loadTable();
                clear();
                setDropDowns();
            } else {
                showAlert("Please Ensure All Boxes Have Information In Them");
            }

        } catch (NumberFormatException e) {
            showAlert("Please Enter Numbers Only For The Vehicle Mileage\nPlease Enter Numbers Only For The Time Taken To Repair");
        }
    }
//Method for populating vehicle registration combo box

    @FXML
    void checker(ActionEvent event) {
        drp_veh.getItems().removeAll(drp_veh.getItems());
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from Vehicle where customerID =" + drp_cusID.getSelectionModel().getSelectedItem());) {
            while (rs.next()) {
                drp_veh.getItems().add(rs.getString("registration"));
            }
        } catch (Exception e) {

        }

    }
//Method to go back to the home page

    @FXML
    void home(ActionEvent event) throws IOException {
        Stage stage = (Stage) btn_home.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guis/HomeGUI.fxml"));
        HomeGUIController c = new common.HomeGUIController(loggedInUser);
        loader.setController(c);
        Parent root = (Parent) loader.load();
        loader.setController(c);

        Scene scene = new Scene(root, 721, 479);

        stage.setTitle("GMSIS | Home");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
//Method for populating the time of booking combo box while also making sure the dates are not off bounds dates

    @FXML
    void checkerTime(ActionEvent event) {
        drp_time.getItems().removeAll(drp_time.getItems());

        boolean noHol = false;
        boolean after = false;
        ArrayList<LocalDate> holidays = new ArrayList<>();
        holidays = add(holidays, datepic_cal.getValue());
        if (datepic_cal.getValue() != null) {
            for (int i = 0; i < holidays.size(); i++) {
                if (holidays.get(i).equals(datepic_cal.getValue())) {
                    showAlert("Garage is closed on bank holidays and public holidays");
                    noHol = false;
                    break;
                } else {
                    noHol = true;
                }
            }

            if (datepic_cal.getValue().isBefore(LocalDate.now())) {
                showAlert("Please select a date in the future");
            } else {
                after = true;
            }
            if (datepic_cal.getValue().getDayOfWeek() == DayOfWeek.SUNDAY) {
                showAlert("Garage is closed on sundays");
                noHol = false;
            }
            if (noHol && after) {
                drp_time.getItems().removeAll(drp_time.getItems());
                drp_time.getItems().addAll("0900", "0930", "1000", "1030", "1100", "1130", "1200", "1230", "1300", "1330", "1400", "1430", "1500", "1530", "1600", "1630", "1700", "1730");
            }
            if (datepic_cal.getValue().getDayOfWeek() == DayOfWeek.SATURDAY) {
                drp_time.getItems().removeAll(drp_time.getItems());
                drp_time.getItems().addAll("0900", "0930", "1000", "1030", "1100", "1130", "1200");
            }
        }
    }
//Method for getting mileage from the database which matches the vehicle reg

    @FXML
    void setMileage(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from Vehicle where registration = '" + drp_veh.getSelectionModel().getSelectedItem() + "'");) {
            txt_vehMile.setText(rs.getString("currentMileage"));
        } catch (Exception e) {

        }
    }
//Method to handle a table row selection

    @FXML
    void onSelection(MouseEvent event) {
        try {
            txtarea_otherBookings.clear();
            txtarea_partsAdded.clear();
            txtarea_otherBookings.setText("Past and Future Bookings\n");
            txtarea_partsAdded.setText("Parts Added To Booking\n");
            setVisibilitySearch(true);
            diagRepTable extractRecords = table_search.getSelectionModel().getSelectedItem();
            txt_booking.setText(Integer.toString(extractRecords.getBooking()));
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("select * from Booking where vehicleRegistration = '" + extractRecords.getVehReg() + "'");) {
                while (rs.next()) {
                    txtarea_otherBookings.appendText(rs.getString("dateOfBooking") + "\n");
                }
            } catch (Exception e) {

            }
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("select * from Booking where id = '" + txt_booking.getText() + "'");) {
                txt_typeOfBooking.setText(rs.getString("type"));
                txt_bill.setText(rs.getString("bill"));
            } catch (Exception e) {

            }
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("select * from Part where bookingID = '" + txt_booking.getText() + "'");) {
                while (rs.next()) {
                    txtarea_partsAdded.appendText(rs.getString("name") + "\n");
                }
            } catch (Exception e) {

            }
        } catch (java.lang.NullPointerException e) {
            setVisibilitySearch(false);
            showAlert("Please Select A Row");
        }
    }
//Method to handle drop down selection of hour and clear the other two combox boxes

    @FXML
    void oneSelectHour(ActionEvent event) throws ClassNotFoundException, SQLException {
        populateHours(table_search);
        if (!drp_monthly.getSelectionModel().isEmpty()) {
            drp_monthly.setValue("");
        }
        if (!drp_daily.getSelectionModel().isEmpty()) {
            drp_daily.setValue("");
        }

    }
//Method to handle drop down selection of month and clear the other two combox boxes

    @FXML
    void oneSelectMonth(ActionEvent event) throws ClassNotFoundException, SQLException {
        populateMonthly(table_search);
        if (!drp_hourly.getSelectionModel().isEmpty()) {
            drp_hourly.setValue("");
        }
        if (!drp_daily.getSelectionModel().isEmpty()) {
            drp_daily.setValue("");
        }
    }
//Method to handle drop down selection of day and clear the other two combox boxes

    @FXML
    void oneSelectDay(ActionEvent event) throws ClassNotFoundException, SQLException {
        populateDaily(table_search);
        if (!drp_monthly.getSelectionModel().isEmpty()) {
            drp_monthly.setValue("");
        }

        if (!drp_hourly.getSelectionModel().isEmpty()) {
            drp_hourly.setValue("");
        }

    }
//Method to put details from database onto the edit page

    @FXML
    void edit(ActionEvent event) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from Booking where id='" + txt_booking.getText() + "'");) {
            if (convertDate(rs.getString(8)).isBefore(LocalDate.now())) {
                showAlert("You cannot edit this booking because it has past");
            } else {
                btn_goBack.setVisible(true);
                btn_book.setVisible(false);
                btn_edit.setVisible(true);
                temp = txt_booking.getText();
                tabpane_home.getSelectionModel().select(tab_diagRep);
                table_search.getSelectionModel().select(null);
                setVisibilitySearch(false);
                drp_cusID.setValue(rs.getString(2));
                checker(event);
                drp_mechID.setValue(rs.getString(3));
                txt_vehMile.setText(rs.getString(4));
                txt_repair.setText(rs.getString(5));
                drp_type.setValue(rs.getString("type"));
                drp_veh.setValue(rs.getString(7));
                datepic_cal.setValue(convertDate(rs.getString(8)));
                drp_time.setValue(rs.getString("timeoFBooking"));
            }

        } catch (Exception e) {

        }

    }
//Method to update details of an booking to the database

    @FXML
    void update(ActionEvent event) throws SQLException {
        try {
            String fName = "";
            String lName = "";
            String manu = "";
            int txtVehMile = Integer.parseInt(txt_vehMile.getText());
            int txtHours = Integer.parseInt(txt_repair.getText());

            if (!drp_cusID.getSelectionModel().isEmpty() && !drp_mechID.getSelectionModel().isEmpty() && !txt_vehMile.getText().isEmpty() && !txt_repair.getText().isEmpty() && !drp_type.getSelectionModel().isEmpty() && !drp_veh.getSelectionModel().isEmpty() && datepic_cal.getValue() != null && !drp_time.getSelectionModel().isEmpty()) {
                int labourCostInPence = 0;
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery("select hourlyRate from User where id =" + drp_mechID.getValue());) {
                    labourCostInPence = txtHours * rs.getInt("hourlyRate");
                } catch (Exception e) {

                }
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery("select * from Customer where id =" + drp_cusID.getSelectionModel().getSelectedItem());) {
                    fName = rs.getString("firstname");
                    lName = rs.getString("surname");
                } catch (Exception e) {

                }
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery("select * from Vehicle where registration ='" + drp_veh.getSelectionModel().getSelectedItem() + "'");) {
                    manu = rs.getString("make");
                } catch (Exception e) {

                }
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery("select warrantyExpiry from Vehicle where registration='" + drp_veh.getSelectionModel().getSelectedItem() + "'");) {
                    while (rs.next()) {
                        LocalDate expiry = convertDate(rs.getString("warrantyExpiry"));
                        if (expiry.isBefore(LocalDate.now())) {
                            labourCostInPence = 0;
                        }
                    }
                    txt_labour.setText(Double.toString((double) labourCostInPence / 100));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DB.executeUpdate("UPDATE Booking SET customerID =" + drp_cusID.getSelectionModel().getSelectedItem() + " , mechanicID = '" + drp_mechID.getSelectionModel().getSelectedItem() + " ' , type = '" + drp_type.getSelectionModel().getSelectedItem() + " ' , hoursSpent = '" + txt_repair.getText() + " ' , vehicleMileage = " + txtVehMile + " , vehicleRegistration = '" + drp_veh.getSelectionModel().getSelectedItem() + " ' , dateOfBooking = '" + getDate() + " ' , bill = ' " + txt_labour.getText() + "' , timeOfBooking = '" + drp_time.getSelectionModel().getSelectedItem() + " ' , customerFirstName = '" + fName + " ' , customerLastName= ' " + lName + " ' , manufacturer = '" + manu + "', monthOfBooking='" + subStringMonth() + "', dayOfBooking='" + subStringDay() + " ' WHERE id = ' " + temp + " '");
                loadTable();
                showInfo("Booking Successfully Edited");
                goBack(event);
            } else {
                showAlert("Please Ensure All Boxes Have Information In Them");
            }

        } catch (NumberFormatException e) {
            showAlert("Please Enter Numbers Only For The Vehicle Mileage\nPlease Enter Numbers Only For The Time Taken To Repair");
        }
    }
//Method to delete booking from the database

    @FXML
    void delete(ActionEvent event) throws SQLException {
        if (deleteConfirmation("Would you like to delete this booking?")) {
            DB.executeUpdate("DELETE from Booking WHERE id= '" + txt_booking.getText() + "'");
            loadTable();
        }

    }
//Method to go back to the add page when on the edit page

    @FXML
    void goBack(ActionEvent event) {
        clear();
        setDropDowns();
        btn_edit.setVisible(false);
        btn_book.setVisible(true);
        btn_goBack.setVisible(false);
    }
//Method to handle the search event

    @FXML
    void search(ActionEvent event) throws ClassNotFoundException, SQLException {
        populateSearch(table_search);
    }

//Method to refresh the table
    @FXML
    void loadTable() {
        try {
            drp_daily.setValue("");
            drp_hourly.setValue("");
            drp_monthly.setValue("");
            handleTable(table_search);
            createList = getData(table_search);
        } catch (ClassNotFoundException | SQLException e) {
        }
    }
//Method to add all public holidays to an array list

    static ArrayList<LocalDate> add(ArrayList<LocalDate> a, LocalDate year) {
        try {
            a.add(LocalDate.of(year.getYear(), Month.JANUARY, 1)); // New Years Day
            a.add(LocalDate.of(year.getYear(), Month.APRIL, 14)); // Good Friday
            a.add(LocalDate.of(year.getYear(), Month.APRIL, 17)); // Easter Monday
            a.add(LocalDate.of(year.getYear(), Month.MAY, 1)); // Bank Holiday
            a.add(LocalDate.of(year.getYear(), Month.MAY, 29)); //Bank Holiday
            a.add(LocalDate.of(year.getYear(), Month.AUGUST, 28)); // Bank Holiday
            a.add(LocalDate.of(year.getYear(), Month.DECEMBER, 25)); // Christmas Day
            a.add(LocalDate.of(year.getYear(), Month.DECEMBER, 31)); // Boxing Day
            return a;
        } catch (NullPointerException e) {
            return null;
        }
    }
//Method to set the visibility of the search menu text fields

    void setVisibilitySearch(boolean condition) {
        txt_bill.setVisible(condition);
        txt_typeOfBooking.setVisible(condition);
        txtarea_otherBookings.setVisible(condition);
        btn_confirm.setVisible(condition);
        btn_delete.setVisible(condition);
        txt_booking.setVisible(condition);
        txtarea_partsAdded.setVisible(condition);
    }
//Method to set all the dropdowns

    private void setDropDowns() {
        drp_type.getItems().removeAll(drp_type.getItems());
        drp_mechID.getItems().removeAll(drp_mechID.getItems());
        drp_hourly.getItems().removeAll(drp_hourly.getItems());
        drp_daily.getItems().removeAll(drp_daily.getItems());
        drp_monthly.getItems().removeAll(drp_monthly.getItems());
        drp_cusID.getItems().removeAll(drp_cusID.getItems());
        drp_type.getItems().addAll("Diagnostic & Repair", "Scheduled Maintenance");
        drp_hourly.getItems().addAll("0900", "0930", "1000", "1030", "1100", "1130", "1200", "1230", "1300", "1330", "1400", "1430", "1500", "1530", "1600", "1630", "1700", "1730");
        drp_daily.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09");
        for (int i = 10; i < 32; i++) {
            String num = Integer.toString(i);
            drp_daily.getItems().add(num);
        }
        drp_monthly.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from User")) {
            while (rs.next()) {
                drp_mechID.getItems().add(rs.getString("id"));
            }
        } catch (Exception e) {

        }
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from Customer")) {
            while (rs.next()) {
                drp_cusID.getItems().add(rs.getString("id"));
            }
        } catch (Exception e) {

        }
    }
//Method to convert LocalDate to String

    String getDate() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = datepic_cal.getValue().format(formatter);
        return date;
    }
//Method to clear all input boxes

    void clear() {
        drp_cusID.getItems().removeAll(drp_cusID.getItems());
        drp_mechID.getItems().removeAll(drp_mechID.getItems());
        txt_vehMile.clear();
        txt_repair.clear();
        txt_labour.clear();
        drp_type.getItems().removeAll(drp_type.getItems());
        drp_veh.getItems().removeAll(drp_veh.getItems());
        datepic_cal.setValue(null);
        drp_time.getItems().removeAll(drp_time.getItems());
    }
//Method to intialize table view

    static void handleTable(TableView<diagRepTable> tableView) {
        ObservableList<TableColumn<diagRepTable, ?>> list = tableView.getColumns();
        for (TableColumn column : list) {
            if (column.getId().equals("CustomerID")) {
                column.setCellValueFactory(new PropertyValueFactory<diagRepTable, String>("customerID"));
            }
            if (column.getId().equals("VehicleID")) {
                column.setCellValueFactory(new PropertyValueFactory<diagRepTable, String>("vehReg"));
            }
            if (column.getId().equals("NextBookingDate")) {
                column.setCellValueFactory(new PropertyValueFactory<diagRepTable, String>("futureBooking"));
            }
            if (column.getId().equals("bookingID")) {
                column.setCellValueFactory(new PropertyValueFactory<diagRepTable, String>("booking"));
            }
        }

    }
//Method to populate the table

    public ObservableList<diagRepTable> getData(TableView<diagRepTable> tableView) throws ClassNotFoundException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from Booking");
        ObservableList<diagRepTable> diagrepList = FXCollections.observableArrayList();
        while (rs.next()) {
            diagrepList.add(new diagRepTable(rs.getInt(2), rs.getString(7), rs.getString(8), rs.getInt(1)));
        }
        tableView.setItems(diagrepList);
        return diagrepList;
    }
//Method to get the subString of day from the dateOfBooking

    public String subStringDay() {
        String day = getDate();
        return day.substring(0, 2);

    }
//Method to get the subString of month from the dateOfBooking

    public String subStringMonth() {
        String month = getDate();
        return month.substring(3, 5);
    }
//Method to convert String to LocalDate

    public LocalDate convertDate(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Instant instant;
            java.util.Date d = simpleDateFormat.parse(date);
            instant = d.toInstant();
            ZoneId defaultZoneId = ZoneId.systemDefault();
            LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
            return localDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//Method to populate table with selected option from hours combobox

    public ObservableList<diagRepTable> populateHours(TableView<diagRepTable> tableView) throws ClassNotFoundException, SQLException {
        ObservableList<diagRepTable> diagrepList = FXCollections.observableArrayList();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM Booking WHERE  timeOfBooking LIKE '%" + drp_hourly.getSelectionModel().getSelectedItem() + "%'");
        while (rs.next()) {
            diagrepList.add(new diagRepTable(rs.getInt(2), rs.getString(7), rs.getString(8), rs.getInt(1)));
        }
        tableView.setItems(diagrepList);
        return diagrepList;
    }
//Method to populate table with selected option from daily combobox

    public ObservableList<diagRepTable> populateDaily(TableView<diagRepTable> tableView) throws ClassNotFoundException, SQLException {
        ObservableList<diagRepTable> diagrepList = FXCollections.observableArrayList();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM Booking WHERE  dayOfbooking='" + drp_daily.getSelectionModel().getSelectedItem() + "'");
        while (rs.next()) {
            diagrepList.add(new diagRepTable(rs.getInt(2), rs.getString(7), rs.getString(8), rs.getInt(1)));
        }
        tableView.setItems(diagrepList);
        return diagrepList;
    }
//Method to populate table with selected option from monthly combobox

    public ObservableList<diagRepTable> populateMonthly(TableView<diagRepTable> tableView) throws ClassNotFoundException, SQLException {
        ObservableList<diagRepTable> diagrepList = FXCollections.observableArrayList();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM Booking WHERE  monthOfBooking='" + drp_monthly.getSelectionModel().getSelectedItem() + "'");
        while (rs.next()) {
            diagrepList.add(new diagRepTable(rs.getInt(2), rs.getString(7), rs.getString(8), rs.getInt(1)));
        }
        tableView.setItems(diagrepList);
        return diagrepList;
    }
//Method to populate table which match what the user typed

    public ObservableList<diagRepTable> populateSearch(TableView<diagRepTable> tableView) throws ClassNotFoundException, SQLException {
        {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Booking WHERE  customerFirstName LIKE '%" + txt_search.getText() + "%' or customerLastName LIKE '%" + txt_search.getText() + "%' or vehicleRegistration LIKE '%" + txt_search.getText() + "%' or manufacturer LIKE '%" + txt_search.getText() + "%'");
            ObservableList<diagRepTable> diagrepList = FXCollections.observableArrayList();
            while (rs.next()) {
                diagrepList.add(new diagRepTable(rs.getInt(2), rs.getString(7), rs.getString(8), rs.getInt(1)));
            }
            tableView.setItems(diagrepList);
            return diagrepList;
        }

    }
    //Method for showiing an alert box with specified message

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    //Method for showing a delete confirmation box

    public boolean deleteConfirmation(String message) {
        Alert dAlert = new Alert(Alert.AlertType.CONFIRMATION);
        dAlert.setTitle("Deletion confirmation");
        dAlert.setHeaderText(message);
        dAlert.showAndWait();
        return dAlert.getResult() == ButtonType.OK;
    }
}
