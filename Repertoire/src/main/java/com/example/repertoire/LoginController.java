package com.example.repertoire;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    AnchorPane pane;
    private Stage stage;
    private Scene scene;
    @FXML
    private TextField loginUsernameText;
    @FXML
    private PasswordField loginPasswordText;
    Singleton singleton = Singleton.getInstance();


    public void createAccount() throws IOException {
        //Switch To Sign Up Scene
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        stage = (Stage) pane.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void loginAccount() throws SQLException, IOException {
        //Use Singleton to Transfer Username to Repertoire Scene
        singleton.setUsername(loginUsernameText.getText());
        //Set Up Fail Alert
        Alert failAlert = new Alert(Alert.AlertType.WARNING);
        failAlert.setTitle("Failed!");
        failAlert.setHeaderText("Login Failed");
        failAlert.setContentText("Username or Password Input is Invalid");
        Connection con = dbConnection.dbConnector();
        //Set Up Database to Verify The Account
        String sql = "select * from accountInformation where username = ? and password = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, loginUsernameText.getText());
        pst.setString(2, loginPasswordText.getText());
        ResultSet result = pst.executeQuery();
        int count = 0;
        while(result.next())
        {
            count += 1;
        }

        if(count == 1)
        {
            //Once Success Switch To Repertoire
            Parent root = FXMLLoader.load(getClass().getResource("Repertoire.fxml"));
            stage = (Stage) pane.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            failAlert.showAndWait();
        }
    }

}