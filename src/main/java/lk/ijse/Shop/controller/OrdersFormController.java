package lk.ijse.Shop.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.CustomerDto;
import lk.ijse.Shop.dto.PlaceOrderDto;
import lk.ijse.Shop.dto.ProductsDto;
import lk.ijse.Shop.dto.tm.CartTm;
import lk.ijse.Shop.model.CustomerModel;
import lk.ijse.Shop.model.OrderModel;
import lk.ijse.Shop.model.PlaceOrderModel;
import lk.ijse.Shop.model.ProductsModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


public class OrdersFormController {
    private final CustomerModel customerModel = new CustomerModel();
    private final ProductsModel productsModel = new ProductsModel();
    private final OrderModel orderModel = new OrderModel();
    private final ObservableList<CartTm> obList = FXCollections.observableArrayList();
    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private JFXButton btnPlaceorder;

    @FXML
    private JFXComboBox<String> cmbCustomerid;

    @FXML
    private JFXComboBox<String> cmbItemCode;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colProductcode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitprice;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblOrderDate;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CartTm> tblOrderCart;

    @FXML
    private TextField txtQty;
    private final PlaceOrderModel placeOrderModel = new PlaceOrderModel();
    @FXML
    private Label time;
    private volatile boolean stop  = false;
    @FXML
    private Label date;
    private volatile boolean s  = false;
    public void initialize() {
        setCellValueFactory();
        generateNextOrderId();
        setDate();
        loadCustomerIds();
        loadProductsCodes();
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
        colProductcode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitprice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("tot"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    private void generateNextOrderId() {
        try {
            String orderId = orderModel.generateNextOrderId();
            lblOrderId.setText(orderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadProductsCodes() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<ProductsDto> productsList = productsModel.loadAllProducts();

            for (ProductsDto productsDto : productsList) {
                obList.add(productsDto.getId());
            }

            cmbItemCode.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<CustomerDto> cusList = customerModel.loadAllCustomers();

            for (CustomerDto dto : cusList) {
                obList.add(dto.getId());
            }
            cmbCustomerid.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        String date = String.valueOf(LocalDate.now());
        lblOrderDate.setText(date);
    }
    @FXML
    void btnAddtocartOnAction(ActionEvent event) {
        String code = cmbItemCode.getValue();
        String description = lblDescription.getText();
       // int qty = Integer.parseInt(txtQty.getText());


        int qtyhand= Integer.parseInt(lblQtyOnHand.getText());

       int qty;
        try {
            qty = Integer.parseInt(txtQty.getText());
            if(qty>qtyhand){
               // System.out.println("Please enter a value less than"+qtyhand);
                new Alert(Alert.AlertType.ERROR, "Please enter a value less than Qty On hand ->"+qtyhand).show();

            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid numeric input for qtyOnHand!").show();
            return;
        }






        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        double total = qty * unitPrice;
        Button btn = new Button("remove");
        btn.setCursor(Cursor.HAND);

        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int index = tblOrderCart.getSelectionModel().getSelectedIndex();
                obList.remove(index);
                tblOrderCart.refresh();

                calculateNetTotal();
            }
        });

        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            if (code.equals(colProductcode.getCellData(i))) {
                qty += (int) colQty.getCellData(i);
                total = qty * unitPrice;

                obList.get(i).setQty(qty);
                obList.get(i).setTot(total);

                tblOrderCart.refresh();
                calculateNetTotal();
                return;
            }
        }

        obList.add(new CartTm(
                code,
                description,
                qty,
                unitPrice,
                total,
                btn
        ));

        tblOrderCart.setItems(obList);
        calculateNetTotal();
        txtQty.clear();
    }

    private void calculateNetTotal() {
        double total = 0;
        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            total += (double) colTotal.getCellData(i);
        }

        lblNetTotal.setText(String.valueOf(total));
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
    void btnNewOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/customerForm.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Customer Manage");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnPlaceorderOnAction(ActionEvent event) {
        String orderId = lblOrderId.getText();
        String cusId =  cmbCustomerid.getValue();
        LocalDate date = LocalDate.parse(lblOrderDate.getText());

        List<CartTm> tmList = new ArrayList<>();

        for (CartTm cartTm : obList) {
            tmList.add(cartTm);
        }

        var placeOrderDto = new PlaceOrderDto(
                orderId,
                cusId,
                date,
                tmList
        );

        try {
            boolean isSuccess = placeOrderModel.placeOrder(placeOrderDto);
            if(isSuccess) {
                new Alert(Alert.AlertType.CONFIRMATION, "order completed!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }


    @FXML
    void cmbCustomerOnAction(ActionEvent event) throws SQLException {
        String id = cmbCustomerid.getValue();
        CustomerDto dto = customerModel.searchCustomer(id);

        lblCustomerName.setText(dto.getEmail());

    }

    @FXML
    void cmbProductOnAction(ActionEvent event) {
        String code =cmbItemCode.getValue();

        txtQty.requestFocus();

        try {
            ProductsDto dto = productsModel.searchProducts(code);

            lblDescription.setText(dto.getDescription());
            lblUnitPrice.setText(String.valueOf(dto.getUnitprice()));
            lblQtyOnHand.setText(String.valueOf(dto.getQtyonhand()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddtocartOnAction(event);
    }
    @FXML
    void btnPrintOnAction(ActionEvent event) throws JRException, SQLException {
        //System.out.println("sales details");
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/SalesDetails.jrxml");
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
}