package lk.ijse.Shop.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lk.ijse.Shop.dto.UserDTO;
import lk.ijse.Shop.model.UserModel;


import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    @FXML
    private AnchorPane root;
    @FXML
    private JFXButton btnLogin;

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    @FXML
    void hperlinkRegisterOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/signUpForm.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Signup page");
        stage.centerOnScreen();
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {

        String UserName = txtUsername.getText();
        String Password = txtPassword.getText();

        try {
            boolean useIsExist = UserModel.isExistUser(UserName, Password);
            if (useIsExist) {
                closeLoginForm();
                navigateToMainWindow();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "User Name Password is Wrong").show();
            }
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }

    }

    private void closeLoginForm() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private void navigateToMainWindow() throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboardForm.fxml"));
        Scene scene = new Scene(rootNode);

        // root.getScene().clear();
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Main Form");
        primaryStage.show();

    }

    @FXML
    void hyperlinkForgotOnAction(ActionEvent event) throws IOException, SQLException {
        String nm = txtUsername.getText();
        if (nm.equals("")) {
            new Alert(Alert.AlertType.CONFIRMATION, "Please Enter a Username").show();
        } else {
            UserDTO user = UserModel.getUser(nm);
            if (!(user == null)) {

                ForgotPasswordForm.runn(root, nm);
                Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/forgotpasswordForm.fxml"));
                Scene scene = new Scene(rootNode);

                // root.getScene().clear();
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
                primaryStage.setTitle("Forgot password form ");
                primaryStage.show();

            }else {
                new Alert(Alert.AlertType.CONFIRMATION, "You haven't registered").show();
            }
        }
    }
}