package lk.ijse.Shop.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.SupplierDto;
import lk.ijse.Shop.dto.tm.SupplierTm;
import lk.ijse.Shop.model.SupplierModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.engine.JasperReport;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class SupplierFormController {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colTel;
    @FXML
    private TableColumn<?, ?> colName;


    @FXML
    private AnchorPane root;

    @FXML
    private TableView<SupplierTm> tblSupplier;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtSupAddress;

    @FXML
    private TextField txtSupId;

    @FXML
    private TextField txtSupName;

    @FXML
    private TextField txtSupTel;
    @FXML
    private javafx.scene.control.Label time;
    private volatile boolean stop  = false;
    @FXML
    private Label date;
    private volatile boolean s  = false;

    private static  String sup_id ;
    public void initialize() {
        setCellValueFactory();
        loadAllSuppliers();
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
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));

    }

    private void loadAllSuppliers() {
        var model = new SupplierModel();

        ObservableList<SupplierTm> obList = FXCollections.observableArrayList();

        try {
            List<SupplierDto> dtoList = model.getAllSuppliers();

            for(SupplierDto dto : dtoList) {
                obList.add(
                        new SupplierTm(
                                dto.getSupplierId(),
                                dto.getSupplierName(),
                                dto.getAddress(),
                                dto.getTel()

                        )
                );
            }

            tblSupplier.setItems(obList);
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
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtSupId.setText("");
        txtSupName.setText("");
        txtSupAddress.setText("");
        txtSupTel.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtSupId.getText();

        var supplierModel = new SupplierModel();
        try {
            boolean isDeleted = supplierModel.deleteSupplier(id);

            if(isDeleted) {
                tblSupplier.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "supplier deleted!").show();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    @FXML
    void btnRawmaterialOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/rawmaterialForm.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Raw material details");
        stage.centerOnScreen();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String supplierId = txtSupId.getText();
        String supplierName = txtSupName.getText();
        String address = txtSupAddress.getText();
        String tel = txtSupTel.getText();

        boolean isValidate = validateSupplier();

        if (isValidate) {
            var dto = new SupplierDto(supplierId, supplierName, address, tel);

            var model = new SupplierModel();
            try {
                boolean isSaved = model.saveSupplier(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "supplier saved!").show();
                    clearFields();
                    initialize();
                    sup_id=supplierId;
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

    }

    private boolean validateSupplier() {

        String idText = txtSupId.getText();

        boolean isCustomerIDValidation = Pattern.matches("[S][0-9]{3,}", idText);

        if (!isCustomerIDValidation) {

            new Alert(Alert.AlertType.ERROR, "INVALID SUPPLIER ID").show();
            txtSupId.setStyle("-fx-border-color: Red");

        }


        String nameText = txtSupName.getText();

        boolean isSupplierNameValidation = Pattern.matches("[A-Za-z.]{3,}", nameText);

        if (!isSupplierNameValidation) {

            new Alert(Alert.AlertType.ERROR, "INVALID SUPPLIER name").show();
            txtSupName.setStyle("-fx-border-color: Red");

        }

        String addressText = txtSupAddress.getText();

        boolean isCustomerAddressValidation = Pattern.matches("[A-Za-z0-9/.\\s]{3,}", addressText);

        if (!isCustomerAddressValidation) {

            new Alert(Alert.AlertType.ERROR, "INVALID SUPPLIER address").show();
            txtSupAddress.setStyle("-fx-border-color: Red");

        }

        String telText = txtSupTel.getText();

        boolean isCustomerTelValidation = Pattern.matches("[0-9]{10}", telText);

        if (!isCustomerTelValidation) {

            new Alert(Alert.AlertType.ERROR, "INVALID SUPPLIER tel").show();
            txtSupTel.setStyle("-fx-border-color: Red");
            return false;
        }

        return  true;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String supplierId = txtSupId.getText();
        String supplierName = txtSupName.getText();
        String address = txtSupAddress.getText();
        String tel = txtSupTel.getText();

        var dto = new SupplierDto(supplierId, supplierName, address, tel);

        var model = new SupplierModel();
        try {
            boolean isUpdated = model.updateSupplier(dto);
            System.out.println(isUpdated);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "supplier updated!").show();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void txtSupIdSearchOnAction(ActionEvent event) {
        String supplierId = txtSupId.getText();

        var model = new SupplierModel();
        try {
            SupplierDto dto = model.searchSupplier(supplierId);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "supplier not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }
    @FXML
    void btnPrintReciptOnAction(ActionEvent event) {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/SupplierReport.jrxml");

            JRDesignQuery jrDesignQuery = new JRDesignQuery();

            jrDesignQuery.setText("select * from supplier where supplier_id=  \""+ sup_id + "\"");

            jasperDesign.setQuery(jrDesignQuery);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(
                            jasperReport, //compiled report
                            null,
                            DbConnection.getInstance().getConnection() //database connection
                    );

            JFrame frame = new JFrame("Jasper Report Viewer");
            JRViewer viewer = new JRViewer(jasperPrint);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(viewer);
            frame.setSize(new Dimension(1200, 800));
            frame.setVisible(true);

        }catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }


    private void fillFields(SupplierDto dto) {
        txtSupId.setText(dto.getSupplierId());
        txtSupName.setText(dto.getSupplierName());
        txtSupAddress.setText(dto.getAddress());
        txtSupTel.setText(dto.getTel());
    }
}