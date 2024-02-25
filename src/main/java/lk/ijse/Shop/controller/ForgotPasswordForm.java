package lk.ijse.Shop.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//import lk.ijse.util.Service;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ForgotPasswordForm {
    @FXML
    private AnchorPane root;
    @FXML
    private JFXButton snd;
    @FXML
    private TextField firstbtn;

    @FXML
    private TextField secbtn;
    @FXML
    private TextField thirdbtn;

    @FXML
    private TextField fourthbtn;

    @FXML
    private TextField fifthbtn ;
    @FXML
    private Text ddlbl;
    @FXML
    private Text otplbl;
    @FXML
    private Text resendlbl;
    @FXML
    private JFXButton verifybtn;



    private static String fgotp;
    @FXML
    private static String name;
    @FXML
    private static AnchorPane npane;


    @FXML
    void sndPressed(ActionEvent event){

        Thread thread = new Thread(()->{
            try {
                fgotp = Service.senMail();

            }catch (Exception e){
                e.printStackTrace();
            }
        });

        thread.start();

        firstbtn.setVisible(true);
        secbtn.setVisible(true);
        thirdbtn.setVisible(true);
        fourthbtn.setVisible(true);
        fifthbtn.setVisible(true);
        resendlbl.setVisible(true);
        ddlbl.setVisible(true);
        verifybtn.setVisible(true);
        snd.setVisible(false);
    }


    public static void runn(AnchorPane root,String nm) {
        npane = root;
        name = nm;
    }

    @FXML
    void fifithTyped(KeyEvent event) {

        if(event.getCharacter().matches("[^\\e\t\r\\d+$]")){
            fifthbtn.setText("");
            Toolkit.getDefaultToolkit().beep();
        }else {
            verifybtn.requestFocus();
        }
    }

    @FXML
    void firstTyped(KeyEvent event) {
        keyHandle(event,firstbtn,secbtn);
    }

    @FXML
    void fourthTyped(KeyEvent event) {
        keyHandle(event,fourthbtn,fifthbtn);
    }

    @FXML
    void resendClicked(MouseEvent event) {
        Thread thread = new Thread(()->{
            try {
                fgotp = Service.senMail();
                firstbtn.setText("");
                secbtn.setText("");
                thirdbtn.setText("");
                fourthbtn.setText("");
                fifthbtn.setText("");

            }catch (Exception e){
                e.printStackTrace();
            }
        });

        thread.start();
    }

    @FXML
    void secTyped(KeyEvent event) {
        keyHandle(event,secbtn,thirdbtn);
    }


    @FXML
    void thirdTyped(KeyEvent event) {
        keyHandle(event,thirdbtn,fourthbtn);
    }

    @FXML
    void verifyPressed(ActionEvent event) throws  IOException {
        String demootp = firstbtn.getText() + secbtn.getText() + thirdbtn.getText() + fourthbtn.getText() + fifthbtn.getText();
        if (!(demootp.equals(""))) {
            firstbtn.setText("");
            secbtn.setText("");
            thirdbtn.setText("");
            fourthbtn.setText("");
            fifthbtn.setText("");
            System.out.println("Verify 1");
            if (fgotp.equals(demootp)) {
                System.out.println("Verify 1");
                //DashboardFormController.useName = name;
                new Alert(Alert.AlertType.CONFIRMATION, "Login Successfull").show();
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboardForm.fxml"));
                Stage stage = (Stage) root.getScene().getWindow();

                stage.setScene(new Scene(anchorPane));
                stage.setTitle("Dashboard");
                stage.centerOnScreen();
                stage.close();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Check your OTP again").show();
            }
        } else {
            new Alert(Alert.AlertType.CONFIRMATION, "Add your OTP  .").show();
        }
    }
    private void keyHandle(KeyEvent event,TextField btn,TextField btn2){
        if(event.getCharacter().matches("[^\\e\t\r\\d+$]")){
            btn.setText("");
            Toolkit.getDefaultToolkit().beep();
        }else{
            btn2.requestFocus();
        }
    }
}