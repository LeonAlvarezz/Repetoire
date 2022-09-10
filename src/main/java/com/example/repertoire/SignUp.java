package com.example.repertoire;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUp {
    @FXML
    AnchorPane pane;
    @FXML
    TextField signUpUsernameText;
    @FXML
    PasswordField signUpPasswordText;
    private Stage stage;
    private Scene scene;
    public void switchToLogin() throws IOException {
        //Switch To Login
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage)pane.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void signUp() throws SQLException, IOException {
        int count = 0;
        Connection con = dbConnection.dbConnector();
        //Set Up Successful Alert
        Alert succAlert = new Alert(Alert.AlertType.WARNING);
        Image success = new Image("D:\\Assignment\\Semester 2\\Object Oriented Programming\\JavaFXTest\\Repertoire\\graphic\\success.png");
        ImageView successIcon = new ImageView(success);
        succAlert.setTitle("Sign Up Successful");
        succAlert.setGraphic(successIcon);
        succAlert.setHeaderText("Welcome To Repertoire");
        succAlert.setContentText("You Have Successfully Created an Account");

        //Set Up Duplicate Account Alert
        Alert duplicateAlert = new Alert(Alert.AlertType.WARNING);
        Image duplicate = new Image("D:\\Assignment\\Semester 2\\Object Oriented Programming\\JavaFXTest\\Repertoire\\graphic\\duplicate.png");
        ImageView duplicateIcon = new ImageView(duplicate);
        duplicateAlert.setTitle("Duplicated");
        duplicateAlert.setGraphic(duplicateIcon);
        duplicateAlert.setHeaderText("Duplicate Account");
        duplicateAlert.setContentText("There is already another account with the same username");
        //Set Up Database to Verify Whether There is Any Duplication
        String sql2 = "select * from accountInformation where username = ? and password = ?";
        PreparedStatement checkStatement = con.prepareStatement(sql2);
        checkStatement.setString(1, signUpUsernameText.getText());
        checkStatement.setString(2, signUpPasswordText.getText());
        //Set Up Database to Insert the Non-Duplicated Account into the table
        String sql = "insert into accountInformation (username, password) values(?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, signUpUsernameText.getText());
        pst.setString(2, signUpPasswordText.getText());
        ResultSet result = checkStatement.executeQuery();
        while(result.next())
        {
            count+=1;
        }
        if(count >= 1)
        {
            duplicateAlert.showAndWait();
        } else
        {
            pst.execute();
            pst.close();
            //Once Clicked OK Switch to Login Scene
            if(succAlert.showAndWait().get() == ButtonType.OK)
            {
                switchToLogin();
            }
        }
    }


}
