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
import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.SalaryDto;
import lk.ijse.Shop.dto.tm.SalaryTm;
import lk.ijse.Shop.model.SalaryModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class SalaryFormController {

    @FXML
    private JFXButton btnBack;
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnPrintSD;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colBasicSal;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableColumn<?, ?> colSalId;
    @FXML
    private TableColumn<?, ?> colMonth;

    @FXML
    private TableView<SalaryTm> tblSalary;


    @FXML
    private TextField txtBasicSal;

    @FXML
    private TextField txtEmpId;

    @FXML
    private TextField txtSalId;
    @FXML
    private TextField txtMonth;
    @FXML
    private Label time;
    private volatile boolean stop  = false;
    @FXML
    private Label date;
    private volatile boolean s  = false;


    public void initialize() {
        setCellValueFactory();
        loadAllSalary();
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
        colSalId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("e_id"));
        colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colBasicSal.setCellValueFactory(new PropertyValueFactory<>("salary"));
    }

    private void loadAllSalary() {
        var model = new SalaryModel();

        ObservableList<SalaryTm> obList = FXCollections.observableArrayList();

        try {
            List<SalaryDto> dtoList = model.getAllSalary();

            for (SalaryDto dto : dtoList) {
                obList.add(
                        new SalaryTm(
                                dto.getId(),
                                dto.getE_id(),
                                dto.getMonth(),
                                dto.getSalary()
                        )
                );
            }
            tblSalary.setItems(obList);
            tblSalary.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/employeeForm.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Employee details");
        stage.centerOnScreen();

    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }
    void clearFields() {
        txtSalId.setText("");
        txtEmpId.setText("");
        txtMonth.setText("");
        txtBasicSal.setText("");

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String s_id = txtSalId.getText();

        var salaryModel = new SalaryModel();
        try {
            boolean isDeleted = salaryModel.deleteSalary(s_id);

            if (isDeleted) {
                tblSalary.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Salary deleted!").show();
                loadAllSalary();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void btnPrintSDOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/SalaryDetails.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(load);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        jasperReport, //compiled report
                        null,
                        DbConnection.getInstance().getConnection() //database connection
                );

        JasperViewer.viewReport(jasperPrint, false);
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isSalaryValidated = validateSalary();
        if (isSalaryValidated) {
            // new Alert(Alert.AlertType.INFORMATION, "Salary Saved Successfully!").show();
        }
    }


    private boolean validateSalary() {

        String s_id = txtSalId.getText();
        boolean isSalaryIDValidated = Pattern.matches("[S][0-9]{3,}", s_id);
        if (!isSalaryIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Salary ID!").show();
            return false;
        }

        String e_id = txtEmpId.getText();
        boolean isEmployeeIdValidated = Pattern.matches("[E][0-9]{3,}", e_id);
        if (!isEmployeeIdValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Id").show();
            return false;
        }

        String month = txtMonth.getText();
        boolean isSalaryMonthValidated = Pattern.matches("^[A-Za-z0-9\\s.,;:'\"!?()-]+$", month);
        if (!isSalaryMonthValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Salary Month").show();
            return false;
        }
        int amount = Integer.parseInt(txtBasicSal.getText());


        var dto = new SalaryDto(s_id,e_id,month,amount);

        var model = new SalaryModel();
        try{
            boolean isSaved = model.saveSalary(dto);
            if(isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"Salary Saved!").show();
                loadAllSalary();
                clearFields();
                tblSalary.refresh();
                loadAllSalary();
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return true;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String s_id = txtSalId.getText();
        String e_id = txtEmpId.getText();
        String month = txtMonth.getText();
        double amount = Double.parseDouble(txtBasicSal.getText());

        boolean isSalaryValidated = validateUpdatedSalary(s_id,e_id,month, String.valueOf(amount));

        if (isSalaryValidated) {

            var  dto = new SalaryDto(s_id,e_id,month,amount);

            var model = new SalaryModel();
            try{
                boolean isUpdated = model.updateSalary(dto);
                System.out.println(isUpdated);
                if(isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION,"Salary Updated!").show();
                    loadAllSalary();
                }
            }catch(SQLException e){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    private boolean validateUpdatedSalary(String s_id, String e_id, String month, String amount) {

        boolean isSalaryIDValidated = Pattern.matches("[S][0-9]{3,}", s_id);
        if (!isSalaryIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Salary ID!").show();
            return false;
        }

        boolean isEmployeeIdValidated = Pattern.matches("[E][0-9]{3,}", e_id);
        if (!isEmployeeIdValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Id").show();
            return false;
        }


        boolean isSalaryMonthValidated = Pattern.matches("^[A-Za-z0-9\\s.,;:'\"!?()-]+$", month);
        if (!isSalaryMonthValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Salary Month").show();
            return false;
        }


        boolean isAmountValidated = Pattern.matches("^[0-9]+(\\.[0-9]{1,2})?$", amount);
        if (!isAmountValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Amount").show();
            return false;
        }
        return true;
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String s_id = txtSalId.getText();

        var model = new SalaryModel();
        try{
            SalaryDto dto = model.searchSalary(s_id);

            if(dto != null){
                fillFields(dto);
            }else{
                new Alert(Alert.AlertType.INFORMATION,"Salary not found!").show();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void fillFields(SalaryDto dto) {
        txtSalId.setText(dto.getId());
        txtEmpId.setText(dto.getE_id());
        txtMonth.setText(dto.getMonth());
        txtBasicSal.setText(String.valueOf(dto.getSalary()));
    }
}