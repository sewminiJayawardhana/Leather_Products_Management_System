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
import lk.ijse.Shop.dto.EmployeeDto;
import lk.ijse.Shop.dto.tm.EmployeeTm;
import lk.ijse.Shop.model.EmployeeModel;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeFormController {

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
    private TableColumn<?, ?> colIntime;

    @FXML
    private TableColumn<?, ?> colLeavetime;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private AnchorPane root;
    @FXML
    private TableView<EmployeeTm> tblEmployee;
    @FXML
    private TextField txtEmployeeid;

    @FXML
    private TextField txtEmployeename;

    @FXML
    private TextField txtIntime;

    @FXML
    private TextField txtLeavetime;
    @FXML
    private Label time;
    private volatile boolean stop  = false;
    @FXML
    private Label date;
    private volatile boolean s  = false;

    public void initialize() {
        setCellValueFactory();
        loadAllEmployees();
        Date();
        TimeNow();
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
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colIntime.setCellValueFactory(new PropertyValueFactory<>("Intime"));
        colLeavetime.setCellValueFactory(new PropertyValueFactory<>("Leavetime"));
    }

    private void loadAllEmployees() {
        var model = new EmployeeModel();

        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<EmployeeDto> dtoList = model.getAllEmployees();

            for (EmployeeDto dto : dtoList) {
                obList.add(
                        new EmployeeTm(
                                dto.getId(),
                                dto.getName(),
                                dto.getIntime(),
                                dto.getLeavetime()
                        )
                );
            }

            tblEmployee.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtEmployeeid.getText();

        var model = new EmployeeModel();
        try {
            EmployeeDto dto = model.searchEmployee(id);

            if (dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Employee not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void fillFields(EmployeeDto dto) {
        txtEmployeeid.setText(dto.getId());
        txtEmployeename.setText(dto.getName());
        txtIntime.setText(dto.getIntime());
        txtLeavetime.setText(dto.getLeavetime());
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
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    void clearFields() {
        txtEmployeeid.setText("");
        txtEmployeename.setText("");
        txtIntime.setText("");
        txtLeavetime.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtEmployeeid.getText();

        var employeeModel = new EmployeeModel();
        try {
            boolean isDeleted = employeeModel.deleteEmployee(id);

            if (isDeleted) {
                tblEmployee.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Employee deleted!").show();
                loadAllEmployees();
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
        boolean isEmployeeValidated = validateEmployee();
        if (isEmployeeValidated) {
            new Alert(Alert.AlertType.INFORMATION, "Employee Saved Successfully!").show();

        }
    }

    private boolean validateEmployee() {

        String e_id = txtEmployeeid.getText();
        boolean isEmployeeIDValidated = Pattern.matches("[E][0-9]{3,}", e_id);
        if (!isEmployeeIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID!").show();
            return false;
        }

        String name = txtEmployeename.getText();
        boolean isEmployeeNameValidated = Pattern.matches("[A-Za-z]{3,}", name);
        if (!isEmployeeNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee name").show();
            return false;
        }

        String intime = txtIntime.getText();
        boolean isIntimeValidated = Pattern.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", intime);
        if (!isIntimeValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Intime").show();
            return false;
        }

        String leavetime = txtLeavetime.getText();
        boolean isleavetimeValidated = Pattern.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", leavetime);
        if (!isleavetimeValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid leavetime").show();
            return false;
        }


        var dto = new EmployeeDto(e_id, name, intime, leavetime);

        var model = new EmployeeModel();
        try {
            boolean isSaved = model.saveEmployee(dto);
            if (isSaved) {
                clearFields();
                EmployeeTm newEmployee = new EmployeeTm(e_id, name, intime, leavetime);
                tblEmployee.getItems().add(newEmployee);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        return true;

    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtEmployeeid.getText();
        String name = txtEmployeename.getText();
        String intime = txtIntime.getText();
        String leavetime = txtLeavetime.getText();

        boolean isEmployeeValidated = validateUpdatedEmployee(id, name, intime, leavetime);

        if (isEmployeeValidated) {
            var dto = new EmployeeDto(id, name, intime, leavetime);

            var model = new EmployeeModel();
            try {
                boolean isUpdated = model.updateEmployee(dto);
                System.out.println(isUpdated);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Employee updated!").show();
                    loadAllEmployees();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateUpdatedEmployee(String id, String name, String intime, String leavetime) {

        boolean isEmployeeIDValidated = Pattern.matches("[E][0-9]{3,}", id);
        if (!isEmployeeIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID!").show();
            return false;
        }

        boolean isEmployeeNameValidated = Pattern.matches("[A-Za-z]{3,}", name);
        if (!isEmployeeNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Name!").show();
            return false;
        }

        boolean isEmployeeIntimeValidated = Pattern.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", intime);
        if (!isEmployeeIntimeValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Intime!").show();
            return false;
        }

        boolean isEmployeeLeavetimeValidated = Pattern.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", leavetime);
        if (!isEmployeeLeavetimeValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Leavetime!").show();
            return false;
        }
        return true;
    }
    @FXML
    void btnSalaryOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/salaryForm.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Manage salary ");
        stage.centerOnScreen();
    }

}
