package lk.ijse.Shop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Shop.dto.UserDTO;
import lk.ijse.Shop.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    void btnSignupOnAction(ActionEvent event) {
        try {
            boolean userCheck = txtEmail.getText().equals(UserModel.getEmail(txtEmail.getText()));
            if (!userCheck){

                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(txtEmail.getText());
                userDTO.setUsername(txtUsername.getText());
                userDTO.setPassword(txtPassword.getText());

                boolean saved = UserModel.saveUser(userDTO);
                if (saved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "user Saved").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "user  not saved").show();
                }
            }else {
                new Alert(Alert.AlertType.WARNING, "Already exist ").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void hyperLoginOnAction(ActionEvent event) throws IOException {
        Object rootNode = FXMLLoader.load(this.getClass().getResource("/view/loginpageForm.fxml"));

        Scene scene;
        scene = new Scene((Parent) rootNode);


        root.getChildren().clear();
        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Form");
    }
}
