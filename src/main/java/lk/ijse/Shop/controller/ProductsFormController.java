package lk.ijse.Shop.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Shop.dto.EmployeeDto;
import lk.ijse.Shop.dto.ProductsDto;
import lk.ijse.Shop.dto.tm.EmployeeTm;
import lk.ijse.Shop.dto.tm.ProductsTm;
import lk.ijse.Shop.model.EmployeeModel;
import lk.ijse.Shop.model.ProductsModel;


import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


public class ProductsFormController {

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
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colQtyonhand;

    @FXML
    private TableColumn<?, ?> colUnitprice;
    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<ProductsTm> tblProduct;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtProductId;

    @FXML
    private TextField txtQtyonhand;

    @FXML
    private TextField txtUnitprice;
    private final ProductsModel productsModel = new ProductsModel();
    @FXML
    private Label time;
    private volatile boolean stop  = false;
    @FXML
    private Label date;
    private volatile boolean s  = false;

    public void initialize() {
        setCellValueFactory();
        loadAllProducts();
        setListener();
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


    private void setListener() {
        tblProduct.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    var dto = new ProductsDto(
                            newValue.getId(),
                            newValue.getDescription(),
                            newValue.getUnitprice(),
                            newValue.getQtyonhand()
                    );
                    setFields(dto);
                });
    }

    private void setFields(ProductsDto dto) {
        txtProductId.setText(dto.getId());
        txtDescription.setText(dto.getDescription());
        txtUnitprice.setText(String.valueOf(dto.getUnitprice()));
        txtQtyonhand.setText(String.valueOf(dto.getQtyonhand()));

    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitprice.setCellValueFactory(new PropertyValueFactory<>("unitprice"));
        colQtyonhand.setCellValueFactory(new PropertyValueFactory<>("qtyonhand"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }

    private void loadAllProducts() {
        try {
            List<ProductsDto> dtoList = productsModel.loadAllProducts();

            ObservableList<ProductsTm> obList = FXCollections.observableArrayList();

            for (ProductsDto dto : dtoList) {
                Button btn = new Button("remove");
                btn.setCursor(Cursor.HAND);

                btn.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

                    if (type.orElse(no) == yes) {
                        int selectedIndex = tblProduct.getSelectionModel().getSelectedIndex();

                        if (selectedIndex != -1) {
                            ProductsTm selectedProduct = tblProduct.getItems().get(selectedIndex);
                            String code = selectedProduct.getId();

                            deleteProducts(code);   //delete item from the database

                            obList.remove(selectedIndex);   //delete item from the JFX-Table
                            tblProduct.refresh();
                        } else {
                            new Alert(AlertType.ERROR, "No product selected").show();
                        }
                    }
                });

                var tm = new ProductsTm(
                        dto.getId(),
                        dto.getDescription(),
                        dto.getUnitprice(),
                        dto.getQtyonhand(),
                        btn
                );
                obList.add(tm);
            }
            tblProduct.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void deleteProducts(String code) {
        try {

            boolean isDeleted = productsModel.deleteProducts(code);
            if (isDeleted) {
                new Alert(AlertType.CONFIRMATION, "Products deleted!").show();
                loadAllProducts();
            }
        } catch (SQLException ex) {
            new Alert(AlertType.ERROR, ex.getMessage()).show();
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
        txtProductId.setText("");
        txtDescription.setText("");
        txtUnitprice.setText("");
        txtQtyonhand.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String code = txtProductId.getText();

        try {
            boolean isDeleted = productsModel.deleteProducts(code);

            if (isDeleted) {

                new Alert(AlertType.CONFIRMATION, "Product deleted!").show();
                loadAllProducts();
            }
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnNextOnAction(ActionEvent event) {

    }

    @FXML
    void codeSearchOnAction(ActionEvent event) {
        String code = txtProductId.getText();

        try {
            ProductsDto dto = productsModel.searchProducts(code);
            if (dto != null) {
                setFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "item not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String code = txtProductId.getText();
        String description = txtDescription.getText();
        double unitPrice;
        int qtyOnHand;

        if (!validateCode(code) || !validateDescription(description)) {
            return;
        }

        try {
            unitPrice = Double.parseDouble(txtUnitprice.getText());

        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "Invalid numeric input for unitPrice!").show();
            return;
        }


        try {
            qtyOnHand = Integer.parseInt(txtQtyonhand.getText());
        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "Invalid numeric input for qtyOnHand!").show();
            return;
        }


        var itemDto = new ProductsDto(code, description, unitPrice, qtyOnHand);

        try {
            boolean isSaved = productsModel.saveProducts(itemDto);
            if (isSaved) {
                new Alert(AlertType.CONFIRMATION, "Product saved!").show();
                clearFields();
                loadAllProducts();
            }
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

    private boolean validateCode(String code) {
        if (Pattern.matches("[P][0-9]{3,}", code)) {
            return true;
        } else {
            new Alert(AlertType.ERROR, "Invalid Products ID!").show();
            return false;
        }
    }

    private boolean validateDescription(String description) {
        if (Pattern.matches("[A-Za-z]{3,}", description)) {
            return true;
        } else {
            new Alert(AlertType.ERROR, "Invalid description!").show();
            return false;
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String code = txtProductId.getText();
        String description = txtDescription.getText();
        double unitPrice;
        int qtyOnHand;

        if (!validateCode(code) || !validateDescription(description)) {
            return; // Validation failed
        }

        try {
            unitPrice = Double.parseDouble(txtUnitprice.getText());

        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "Invalid numeric input for unitPrice!").show();
            return;
        }
        try {

            qtyOnHand = Integer.parseInt(txtQtyonhand.getText());
        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "Invalid numeric input for qtyOnHand!").show();
            return;
        }

        try {
            boolean isUpdated = productsModel.updateProducts(new ProductsDto(code, description, unitPrice, qtyOnHand));
            if (isUpdated) {
                new Alert(AlertType.CONFIRMATION, "Product updated").show();
                loadAllProducts();
            }
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }
}


