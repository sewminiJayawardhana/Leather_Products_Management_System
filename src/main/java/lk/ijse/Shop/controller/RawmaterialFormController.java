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
import lk.ijse.Shop.dto.RawMaterialDto;
import lk.ijse.Shop.dto.tm.RawTm;
import lk.ijse.Shop.model.RawmaterialModel;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class RawmaterialFormController {

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
    private TableColumn<?, ?> colRawId;

    @FXML
    private TableColumn<?, ?> colQtyOnStock;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<RawTm> tblRawMaterial;

    @FXML
    private TextField txtRawId;

    @FXML
    private TextField txtQtyOnStock;

    @FXML
    private TextField txtUnitPrice;
    @FXML
    private Label time;
    private volatile boolean stop  = false;
    @FXML
    private Label date;
    private volatile boolean s  = false;

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboardForm.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
    }

    public void initialize() {
        setCellValueFactory();
        loadAllMaterials();
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
        colRawId.setCellValueFactory(new PropertyValueFactory<>("rawMaterialId"));
        colQtyOnStock.setCellValueFactory(new PropertyValueFactory<>("qtyOnStock"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

    }

    private void loadAllMaterials() {
        var model = new RawmaterialModel();

        ObservableList<RawTm> obList = FXCollections.observableArrayList();

        try {
            List<RawMaterialDto> dtoList = model.getAllMaterials();

            for(RawMaterialDto dto : dtoList) {
                obList.add(
                        new RawTm(
                                dto.getRawMaterialId(),
                                dto.getQtyOnStock(),
                                dto.getUnitPrice()

                        )
                );
            }

            tblRawMaterial.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String rawId = txtRawId.getText();

        var rawMaterialModel = new RawmaterialModel();
        try {
            boolean isDeleted = rawMaterialModel.deleteRawMaterial(rawId);

            if(isDeleted) {
                tblRawMaterial.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "material deleted!").show();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtRawId.getText();
        Double qtyOnStock = Double.valueOf(txtQtyOnStock.getText());
        Double unitPrice = Double.valueOf(txtUnitPrice.getText());

        boolean isValidate = validateRawMaterial();

        if (isValidate) {

            var dto = new RawMaterialDto(id, qtyOnStock, unitPrice);

            var model = new RawmaterialModel();
            try {
                boolean isSaved = model.saveRawMaterial(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "material saved!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

    }

    private boolean validateRawMaterial() {

        String rawMaterialIdText = txtRawId.getText();

        boolean isRawMaterialIDValidation = Pattern.matches("[R][0-9]{3,}", rawMaterialIdText);

        if (!isRawMaterialIDValidation) {

            new Alert(Alert.AlertType.ERROR, "INVALID RAW MATERIAL ID").show();
            txtRawId.setStyle("-fx-border-color: Red");

        }



        Double qtyOnStock = Double.parseDouble(txtQtyOnStock.getText());
        String qtyOnStockString = String.format("%.2f",qtyOnStock);
        boolean isQtyOnStockValidation = Pattern.matches("[-+]?[0-9]*\\.?[0-9]+", qtyOnStockString);

        if (!isQtyOnStockValidation) {

            new Alert(Alert.AlertType.ERROR, "INVALID QTY").show();
            txtQtyOnStock.setStyle("-fx-border-color: Red");
            return false;
        }

      /*  Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        String unitPriceString = String.format("%.2f",unitPrice);
        boolean isUnitPriceValidation = Pattern.matches("[A-Za-z.]{3,}", unitPriceString);

        if (!isUnitPriceValidation) {

            new Alert(Alert.AlertType.ERROR, "INVALID UNIT PRICE").show();
            txtUnitPrice.setStyle("-fx-border-color: Red");
            return false;
        }

       */





        return true;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String rawMaterialId = txtRawId.getText();
        Double qtyOnStock = Double.valueOf(txtQtyOnStock.getText());
        Double unitPrice = Double.valueOf(txtUnitPrice.getText());


        var dto = new RawMaterialDto(rawMaterialId,  qtyOnStock, unitPrice);

        var model = new RawmaterialModel();
        try {
            boolean isUpdated = model.updateRawMaterial(dto);
            System.out.println(isUpdated);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "material updated!").show();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void txtRawIdSearchOnAction(ActionEvent event) {
        String rawId = txtRawId.getText();

        var model = new RawmaterialModel();
        try {
            RawMaterialDto dto = model.searchRawMaterial(rawId);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "material not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void fillFields(RawMaterialDto dto) {
        txtRawId.setText(dto.getRawMaterialId());
        txtQtyOnStock.setText(String.valueOf(dto.getQtyOnStock()));
        txtUnitPrice.setText(String.valueOf(dto.getUnitPrice()));

    }

    void clearFields() {
        txtRawId.setText("");
        txtQtyOnStock.setText("");
        txtUnitPrice.setText("");

    }

}

