package lk.ijse.Shop.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class DashboardFormController {
    public static String useName;

    @FXML
    private AnchorPane anchorpaneD;

    @FXML
    private JFXButton btnCustomer;

    @FXML
    private JFXButton btnEmployee;

    @FXML
    private JFXButton btnProducts;

    @FXML
    private JFXButton btnSupplier;

    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        try {
            // Load the CustomerForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/signUpForm.fxml"));
            Parent root = loader.load();

            // Create a new scene with the CustomerForm
            Scene scene = new Scene(root);

            // Get the stage information from the event's source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("signup page");
            stage.centerOnScreen();
            // Set the new scene on the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML
        }
    }
    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        try {
            // Load the CustomerForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customerForm.fxml"));
            Parent root = loader.load();

            // Create a new scene with the CustomerForm
            Scene scene = new Scene(root);

            // Get the stage information from the event's source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Customer Manage");
            stage.centerOnScreen();
            // Set the new scene on the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML
        }


    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) {
        try {
            // Load the CustomerForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/employeeForm.fxml"));
            Parent root = loader.load();

            // Create a new scene with the CustomerForm
            Scene scene = new Scene(root);

            // Get the stage information from the event's source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.setTitle("Employee Manage");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML
        }


    }

    @FXML
    void btnProductsOnAction(ActionEvent event) {
        try {
            // Load the CustomerForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/productsForm.fxml"));
            Parent root = loader.load();

            // Create a new scene with the CustomerForm
            Scene scene = new Scene(root);

            // Get the stage information from the event's source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Products Manage");
            stage.centerOnScreen();
            // Set the new scene on the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML
        }
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) {
        try {
            // Load the CustomerForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/supplierForm.fxml"));
            Parent root = loader.load();

            // Create a new scene with the CustomerForm
            Scene scene = new Scene(root);

            // Get the stage information from the event's source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Supplier Manage");
            stage.centerOnScreen();
            // Set the new scene on the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML
        }
    }

    public void btnOrdersOnAction(ActionEvent event) {
        try {
            // Load the CustomerForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ordersForm.fxml"));
            Parent root = loader.load();

            // Create a new scene with the CustomerForm
            Scene scene = new Scene(root);

            // Get the stage information from the event's source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Orders details");
            stage.centerOnScreen();
            // Set the new scene on the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML
        }
    }

    @FXML
    void btnDeliveryOnAction(ActionEvent event) {
        try {
            // Load the CustomerForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/deliveryForm.fxml"));
            Parent root = loader.load();

            // Create a new scene with the CustomerForm
            Scene scene = new Scene(root);

            // Get the stage information from the event's source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Delivery Manage");
            stage.centerOnScreen();
            // Set the new scene on the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML
        }
    }
}


