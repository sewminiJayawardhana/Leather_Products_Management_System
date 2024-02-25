package lk.ijse.Shop.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.Shop.model.UserModel;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class OTPformController {
    private static String otp2;
    private static String userName;
    private static String pw;
    private static String email;


    @FXML
    private JFXButton backbtn;

    @FXML
    private TextField fifthbtn;

    @FXML
    private TextField firstbtn;

    @FXML
    private TextField fourthbtn;

    @FXML
    private Text resendlbl;

    @FXML
    private TextField secbtn;

    @FXML
    private TextField thirdbtn;

    @FXML
    private JFXButton verifybtn;
    @FXML
    private AnchorPane root;

    @FXML
    void backPressed(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/signUpForm.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("signup page ");
        stage.centerOnScreen();

    }

    @FXML
    void fifithTyped(KeyEvent event) {

        if (event.getCharacter().matches("[^\\e\t\r\\d+$]")) {
            fifthbtn.setText("");
            Toolkit.getDefaultToolkit().beep();
        } else {
            verifybtn.requestFocus();
        }
    }

    @FXML
    void firstTyped(KeyEvent event) {
        keyHandle(event, firstbtn, secbtn);
    }

    @FXML
    void fourthTyped(KeyEvent event) {
        keyHandle(event, fourthbtn, fifthbtn);
    }

    @FXML
    void resendClicked(MouseEvent event) {
        otp2 = Service.senMail();
    }

    @FXML
    void secTyped(KeyEvent event) {
        keyHandle(event, secbtn, thirdbtn);
    }

    @FXML
    void thirdTyped(KeyEvent event) {
        keyHandle(event, thirdbtn, fourthbtn);
    }


    @FXML
    void verifyPressed(ActionEvent event) {
        String demootp = firstbtn.getText() + secbtn.getText() + thirdbtn.getText() + fourthbtn.getText() + fifthbtn.getText();
        firstbtn.setText("");
        secbtn.setText("");
        thirdbtn.setText("");
        fourthbtn.setText("");
        fifthbtn.setText("");
        if (otp2.equals(demootp)) {

            try {
                boolean num = UserModel.isExistUser(userName, pw);
                if (num) {
                    new Alert(Alert.AlertType.ERROR, "Registration Successful").show();
                    AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/loginpageForm.fxml"));
                    Stage stage = (Stage) root.getScene().getWindow();

                    stage.setScene(new Scene(anchorPane));
                    stage.setTitle("signup page ");
                    stage.centerOnScreen();


                } else {
                    new Alert(Alert.AlertType.ERROR, "Sql server error").show();
                }
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        } else {
            new Alert(Alert.AlertType.ERROR, "Check your OTP Again").show();

        }
    }

    public void getOtp(String otp, String name, String pas, String id) {
        otp2 = otp;
        userName = name;
        pw = pas;
    }

    private void keyHandle(KeyEvent event, TextField btn, TextField btn2) {
        if (event.getCharacter().matches("[^\\e\t\r\\d+$]")) {
            btn.setText("");
            Toolkit.getDefaultToolkit().beep();
        } else {
            btn2.requestFocus();
        }
    }
}