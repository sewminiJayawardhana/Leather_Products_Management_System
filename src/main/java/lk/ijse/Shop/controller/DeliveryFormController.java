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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.DeliveryDto;
import lk.ijse.Shop.dto.tm.CustomerTm;
import lk.ijse.Shop.dto.tm.DeliveryTm;
import lk.ijse.Shop.model.DeliveryModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DeliveryFormController {

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
    private TableColumn<?, ?> colDeliveryId;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<DeliveryTm> tblDelivery;

    @FXML
    private TextField txtDeliveryId;

    @FXML
    private TextField txtDeliveryStatus;

    @FXML
    private TextField txtEmployeeId;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtTel;
    @FXML
    private Label time;
    private volatile boolean stop  = false;
    @FXML
    private Label date;
    private volatile boolean s  = false;
    public void initialize() {
        setCellValueFactory();
        loadAllDelivery();
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
        colDeliveryId.setCellValueFactory(new PropertyValueFactory<>("deliveryId"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("deliveryStatus"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
    }

    private void loadAllDelivery() {
        var model = new DeliveryModel();

        ObservableList<DeliveryTm> obList = FXCollections.observableArrayList();

        try {
            List<DeliveryDto> dtoList = model.getAllDelivery();

            for(DeliveryDto dto : dtoList) {
                obList.add(
                        new DeliveryTm(
                                dto.getDeliveryId(),
                                dto.getOrderId(),
                                dto.getEmployeeId(),
                                dto.getLocation(),
                                dto.getDeliveryStatus(),
                                dto.getTel()
                        )
                );
            }

            tblDelivery.setItems(obList);
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
    void clearFields() {
        txtDeliveryId.setText("");
        txtOrderId.setText("");
        txtEmployeeId.setText("");
        txtLocation.setText("");
        txtDeliveryStatus.setText("");
        txtTel.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String deliveryId = txtDeliveryId.getText();

        var deliveryModel = new DeliveryModel();
        try {
            boolean isDeleted = deliveryModel.deleteDelivery(deliveryId);

            if(isDeleted) {
                tblDelivery.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "delivery deleted!").show();
                loadAllDelivery();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

        boolean isDeliveryValidated = validateDelivery();
        if (isDeliveryValidated) {
            new Alert(Alert.AlertType.INFORMATION, "Delivery Saved Successfully!").show();


        }
    }
    private boolean validateDelivery(){
        String deliveryId = txtDeliveryId.getText();
        boolean isDeliveryIDValidated = Pattern.matches("[D][0-9]{3,}", deliveryId);
        if (!isDeliveryIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Delivery ID!").show();
            return false;
        }

        String orderId = txtOrderId.getText();
        boolean isOrderIDValidated = Pattern.matches("[O][0-9]{3,}", orderId);
        if (!isOrderIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Order ID!").show();
            return false;
        }

        String employeeId = txtEmployeeId.getText();
        boolean isEmployeeIDValidated = Pattern.matches("[E][0-9]{3,}", employeeId);
        if (!isEmployeeIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID!").show();
            return false;
        }

        String location = txtLocation.getText();
        boolean isLocationValidated = Pattern.matches("[A-Za-z]{3,}", location);
        if (!isLocationValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Location").show();
            return false;
        }

        String deliveryStatus = txtDeliveryStatus.getText();
        boolean isStatusValidated = Pattern.matches("[A-Za-z]{3,}", deliveryStatus);
        if (!isStatusValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Status").show();
            return false;
        }

        String tel = txtTel.getText();
        boolean isTelValidated = Pattern.matches("[0-9]{10}", tel);
        if (!isTelValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Telephone number").show();
            return false;
        }


        var dto = new DeliveryDto(deliveryId, orderId, employeeId, location, deliveryStatus, tel);

        var model = new DeliveryModel();
        try {
            boolean isSaved = model.saveDelivery(dto);
            if (isSaved) {
                clearFields();
                del_id=deliveryId;
                DeliveryTm newDelivery = new DeliveryTm(deliveryId, orderId, employeeId, location,deliveryStatus,tel);
                tblDelivery.getItems().add(newDelivery);

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        return true;
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String deliveryId = txtDeliveryId.getText();
        String orderId = txtOrderId.getText();
        String employeeId = txtEmployeeId.getText();
        String location = txtLocation.getText();
        String deliveryStatus = txtDeliveryStatus.getText();
        String tel = txtTel.getText();

        boolean isDeliveryValidated = validateUpdatedDelivery(deliveryId, orderId, employeeId, location, deliveryStatus, tel);

        if (isDeliveryValidated) {

            var dto = new DeliveryDto(deliveryId, orderId, employeeId, location, deliveryStatus, tel);

            var model = new DeliveryModel();
            try {
                boolean isUpdated = model.updateDelivery(dto);
                System.out.println(isUpdated);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Delivery updated!").show();
                    loadAllDelivery();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
    private boolean validateUpdatedDelivery(String deliveryId, String orderId,  String employeeId, String location,String deliveryStatus,String tel){
        boolean isDeliveryIDValidated = Pattern.matches("[D][0-9]{3,}", deliveryId);
        if (!isDeliveryIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Delivery ID!").show();
            return false;
        }

        boolean isOrderIDValidated = Pattern.matches("[O][0-9]{3,}", orderId);
        if (!isOrderIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Order ID!").show();
            return false;
        }

        boolean isEmployeeIDValidated = Pattern.matches("[E][0-9]{3,}", employeeId);
        if (!isEmployeeIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID!").show();
            return false;
        }

        boolean isLocationValidated = Pattern.matches("[A-Za-z]{3,}", location);
        if (!isLocationValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid location!").show();
            return false;
        }

        boolean isStatusValidated = Pattern.matches("[A-Za-z]{3,}", deliveryStatus);
        if (!isStatusValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid status!").show();
            return false;
        }
        return true;
    }
    @FXML
    void txtDeliveryIdSearchOnAction(ActionEvent event) {
        String deliveryId = txtDeliveryId.getText();

        var model = new DeliveryModel();
        try {
            DeliveryDto dto = model.searchDelivery(deliveryId);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Delivery not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }
    private void fillFields(DeliveryDto dto) {
        txtDeliveryId.setText(dto.getDeliveryId());
        txtOrderId.setText(dto.getOrderId());
        txtEmployeeId.setText(dto.getEmployeeId());
        txtLocation.setText(dto.getLocation());
        txtDeliveryStatus.setText(dto.getDeliveryStatus());
        txtTel.setText(dto.getTel());
    }
    private static  String del_id ;
    @FXML
    void btnPrintDDOnAction(ActionEvent event){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/DeliveryReceipt.jrxml");

            JRDesignQuery jrDesignQuery = new JRDesignQuery();

            jrDesignQuery.setText("select * from delivery where delivery_id=  \""+ del_id + "\"");

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


}