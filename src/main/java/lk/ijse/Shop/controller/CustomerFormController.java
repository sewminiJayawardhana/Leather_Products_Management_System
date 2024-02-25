package lk.ijse.Shop.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Shop.dto.CustomerDto;
import lk.ijse.Shop.dto.tm.CustomerTm;
import lk.ijse.Shop.model.CustomerModel;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerFormController {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnNext;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private AnchorPane root;
    @FXML
    private TableView<CustomerTm> tblCustomer;
    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtEmail;
    @FXML
    private Label time;
    private volatile boolean stop  = false;
    @FXML
    private Label date;
    private volatile boolean s  = false;

    public void initialize() {
        setCellValueFactory();
        loadAllCustomers();
        TimeNow();
        Date();

    }
    private void TimeNow(){
        Thread thread = new Thread(()->{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while(!stop){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
                final String timeNow = sdf.format(new Date());
                Platform.runLater(()->{
                    time.setText(timeNow);
                });
            }
        });
        thread.start();
    }
    private void Date(){
        Thread thread = new Thread(()->{
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            while (!s){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
                final String dateNow = sdf1.format(new Date());
                Platform.runLater(()->{
                    date.setText(dateNow);
                });
            }
        });
        thread.start();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllCustomers() {
        var model = new CustomerModel();

        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<CustomerDto> dtoList = model.getAllCustomers();

            for (CustomerDto dto : dtoList) {
                obList.add(
                        new CustomerTm(
                                dto.getId(),
                                dto.getName(),
                                dto.getAddress(),
                                dto.getEmail()
                        )
                );
            }

            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboardForm.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtCustomerId.getText();

        var model = new CustomerModel();
        try {
            CustomerDto dto = model.searchCustomer(id);

            if (dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void fillFields(CustomerDto dto) {
        txtCustomerId.setText(dto.getId());
        txtCustomerName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtEmail.setText(dto.getEmail());
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtCustomerId.getText();

        var customerModel = new CustomerModel();
        try {
            boolean isDeleted = customerModel.deleteCustomer(id);

            if (isDeleted) {
                tblCustomer.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
                loadAllCustomers();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnNextOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

        boolean isCustomerValidated = validateCustomer();
        if (isCustomerValidated) {
            new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully!").show();

        }
    }

    private boolean validateCustomer() {
        String id = txtCustomerId.getText();
        boolean isCustomerIDValidated = Pattern.matches("[C][0-9]{3,}", id);
        if (!isCustomerIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID!").show();
            return false;
        }


        String name = txtCustomerName.getText();
        boolean isCustomerNameValidated = Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", name);
        if (!isCustomerNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer email").show();
            return false;
        }


        String address = txtAddress.getText();
        boolean isCustomerAddressValidated = Pattern.matches("^\\d+/\\d+,\\s?[A-Za-z0-9\\s.,'-]+|^[A-Za-z]+$", address);
        if (!isCustomerAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer address").show();
            return false;
        }


        String email = txtEmail.getText();
        boolean isCustomerEmailValidated = Pattern.matches("[A-Za-z]{3,}", email);
        if (!isCustomerEmailValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer name").show();
            return false;
        }


        var dto = new CustomerDto(id, name, address, email);

        var model = new CustomerModel();
        try {
            boolean isSaved = model.saveCustomer(dto);
            if (isSaved) {

                clearFields();
                CustomerTm newCustomer = new CustomerTm(id, email, address, name);
                tblCustomer.getItems().add(newCustomer);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        return true;
    }

    void clearFields() {
        txtCustomerId.setText("");
        txtCustomerName.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        String id = txtCustomerId.getText();
        String name = txtCustomerName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();


        boolean isCustomerValidated = validateUpdatedCustomer(id, name, address, email);

        if (isCustomerValidated) {
            var dto = new CustomerDto(id, name, address, email);

            var model = new CustomerModel();
            try {
                boolean isUpdated = model.updateCustomer(dto);
                System.out.println(isUpdated);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer updated!").show();
                    loadAllCustomers();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateUpdatedCustomer(String id, String name, String address, String email) {

        boolean isCustomerIDValidated = Pattern.matches("[C][0-9]{3,}", id);
        if (!isCustomerIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID!").show();
            return false;
        }


        boolean isCustomerNameValidated = Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", name);
        if (!isCustomerNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Name!").show();
            return false;
        }


        boolean isCustomerAddressValidated = Pattern.matches("^\\d+/\\d+,\\s?[A-Za-z0-9\\s.,'-]+|^[A-Za-z]+$", address);
        if (!isCustomerAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer address").show();
            return false;
        }


        boolean isCustomerEmailValidated = Pattern.matches("[A-Za-z]{3,}", email);
        if (!isCustomerEmailValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer email").show();
            return false;
        }

        return true;
    }
}