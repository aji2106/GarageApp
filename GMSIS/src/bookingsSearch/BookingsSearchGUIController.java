package bookingsSearch;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeTableView;

public class BookingsSearchGUIController implements Initializable {

    @FXML
    private JFXButton btn_confirm;

    @FXML
    private JFXButton btn_view;

    @FXML
    private JFXButton btn_search;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXRadioButton radio_parts;

    @FXML
    private JFXRadioButton radio_veh;

    @FXML
    private JFXRadioButton radio_bookings;

    @FXML
    private JFXTextField txt_search;

    @FXML
    private JFXButton btn_add;

    @FXML
    private JFXButton btn_edit;

    @FXML
    private JFXButton btn_delete;

    @FXML
    private JFXTextField txt_cusName;

    @FXML
    private JFXTextField txt_typeOfBooking;

    @FXML
    private TreeTableView<?> table_pastFutureDates;

    @FXML
    private TextArea txtarea_partsUsed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtarea_partsUsed.setVisible(false);
        table_pastFutureDates.setVisible(false);
        txt_cusName.setVisible(false);
        txt_typeOfBooking.setVisible(false);
    }

    @FXML
    void add(ActionEvent event) {
        if (radio_bookings.isSelected()) {
            //Open diagrep GUI
        } else if (radio_parts.isSelected()) {
            //Open parts GUI
        } else {
            //Open vehicle records GUI
        }

    }

    @FXML
    void bookings(ActionEvent event) {
        txt_cusName.setEditable(false);
        txtarea_partsUsed.setEditable(false);
        txtarea_partsUsed.setVisible(false);
        table_pastFutureDates.setVisible(false);
        txt_cusName.setVisible(false);
        txt_typeOfBooking.setVisible(false);
        if (radio_bookings.isSelected()) {
            table_pastFutureDates.setVisible(true);
            txt_cusName.setVisible(true);
            txt_typeOfBooking.setVisible(true);
        }
    }

    @FXML
    void delete(ActionEvent event) {
        //Detele selected record

    }

    @FXML
    void edit(ActionEvent event) {

        txt_typeOfBooking.setEditable(false);
        txt_cusName.setEditable(false);
        txtarea_partsUsed.setEditable(false);
        if (radio_bookings.isSelected()) {
            txt_cusName.setEditable(true);
            txt_typeOfBooking.setEditable(true);
        } else if (radio_parts.isSelected()) {
            txt_cusName.setEditable(true);
            txt_typeOfBooking.setEditable(true);
        } else {
            txt_cusName.setEditable(true);
            txtarea_partsUsed.setEditable(true);
        }

    }

    @FXML
    void parts(ActionEvent event) {
        txt_cusName.setEditable(false);
        txtarea_partsUsed.setEditable(false);
        txtarea_partsUsed.setVisible(false);
        table_pastFutureDates.setVisible(false);
        txt_cusName.setVisible(false);
        txt_typeOfBooking.setVisible(false);
        if (radio_parts.isSelected()) {
            table_pastFutureDates.setVisible(true);
            txt_cusName.setVisible(true);
            txt_typeOfBooking.setVisible(true);
            txtarea_partsUsed.setVisible(true);
        }

    }

    @FXML
    void vehicles(ActionEvent event) {
        txt_cusName.setEditable(false);
        txtarea_partsUsed.setEditable(false);
        txtarea_partsUsed.setVisible(false);
        table_pastFutureDates.setVisible(false);
        txt_cusName.setVisible(false);
        txt_typeOfBooking.setVisible(false);
        if (radio_veh.isSelected()) {
            table_pastFutureDates.setVisible(true);
            txt_cusName.setVisible(true);
            txtarea_partsUsed.setVisible(true);
        }
    }

    @FXML
    void search(ActionEvent event) {
        if (radio_veh.isSelected()) {
            //Show list of vehicles,customer details and next booking date in big table, which match the search
        } else if (radio_parts.isSelected()) {
            //Show list of vehicles,customer details in the big table and parts used inside the txtarea_partsUsed box, which match the seach
        } else {
            //Show list of vehicles,customer details and next booking date in big table, which match the search
        }
    }

    @FXML
    void view(ActionEvent event) {
        if (radio_bookings.isSelected()) {
            txt_cusName.setText("Cus name from table");
            txt_typeOfBooking.setText("The type of booking from table");
            //Put past and future bookings into the smaller table on the right
        } else if (radio_veh.isSelected()) {
            //Put past and future bookings into the smaller table on the right
            txt_cusName.setText("Cus name from table");
            txtarea_partsUsed.setText("The parts used for the that specific vehicle from database");
        } else {
            //Put past and future bookings into the smaller table on the right
            txt_cusName.setText("Cus name from table");
            txt_typeOfBooking.setText("The type of booking from table");
            txtarea_partsUsed.setText("The parts used for the that specific vehicle from database");
        }

    }

    @FXML
    void confirm(ActionEvent event) {
        if (radio_bookings.isSelected()) {
            txt_cusName.getText();//Write changes to database
            txt_typeOfBooking.getText();//Write changes to database
        } else if (radio_veh.isSelected()) {
            txt_cusName.getText();//Write changes to database
            txtarea_partsUsed.getText();//Write changes to database
        } else {
            txt_cusName.getText();//Write changes to database
            txt_typeOfBooking.getText();//Write changes to database
            txtarea_partsUsed.getText();//Write changes to database

        }

    }
}
