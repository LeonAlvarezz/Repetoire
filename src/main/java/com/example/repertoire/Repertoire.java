package com.example.repertoire;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class Repertoire implements Initializable {
    @FXML
    private Button addButton;

    @FXML
    private TextField enterAssignmentText;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button addAssignmentButton;
    @FXML
    VBox assignmentPane;
    @FXML
    private AnchorPane pane;
    @FXML
    private Label welcomeUsername;
    private Stage stage;
    private Scene scene;
    private Singleton singleton = Singleton.getInstance();



    public String datePicker()
    {
        //Convert Date From DatePicker Into Readable Format
        LocalDate date = datePicker.getValue();
        String dayOfWeekFirst = date.getDayOfWeek().toString().substring(0, 1).toUpperCase();
        String dayOfWeekLast = date.getDayOfWeek().toString().substring(1, 3).toLowerCase();
        String monthFirst = date.getMonth().toString().substring(0, 1).toUpperCase();
        String monthLast = date.getMonth().toString().substring(1, 3).toLowerCase();
        int day = date.getDayOfMonth();
        String dateValue = dayOfWeekFirst + dayOfWeekLast + ", " + monthFirst + monthLast + " " + day;
        return dateValue;
    }

    public void addButtonFadeOut() {
        //Prevent User from Clicking the Add Button Multiple Times causing the Animation to be janky
        addAssignmentButton.setDisable(false);
        addButton.setDisable(true);

        //Add Button Translate And Fade Out
        TranslateTransition translateAdd = new TranslateTransition(Duration.millis(500), addButton);
        FadeTransition addFadeOut = new FadeTransition(Duration.millis(200), addButton);
        translateAdd.setByY(60);
        translateAdd.setDuration(Duration.millis(500));
        addFadeOut.setFromValue(1.0);
        addFadeOut.setToValue(0.0);

        TranslateTransition translateText = new TranslateTransition(Duration.millis(500), enterAssignmentText);
        FadeTransition textFadeIn = new FadeTransition(Duration.millis(200), enterAssignmentText);
        translateText.setByY(-40);
        translateText.setDelay(Duration.millis(500));
        textFadeIn.setFromValue(0.0);
        textFadeIn.setToValue(1.0);
        textFadeIn.setDelay(Duration.millis(200));

        TranslateTransition translateDate = new TranslateTransition(Duration.millis(500), datePicker);
        FadeTransition dateFadeIn = new FadeTransition(Duration.millis(200), datePicker);
        translateDate.setByY(-40);
        translateDate.setDelay(Duration.millis(500));
        dateFadeIn.setFromValue(0.0);
        dateFadeIn.setToValue(1.0);
        dateFadeIn.setDelay(Duration.millis(200));

        TranslateTransition translateAssignment = new TranslateTransition(Duration.millis(500), addAssignmentButton);
        FadeTransition assignmentFadeIn = new FadeTransition(Duration.millis(200), addAssignmentButton);
        translateAssignment.setByY(-40);
        translateAssignment.setDelay(Duration.millis(500));
        assignmentFadeIn.setFromValue(0.0);
        assignmentFadeIn.setToValue(1.0);
        assignmentFadeIn.setDelay(Duration.millis(200));

        //Play Animation
        addFadeOut.play();
        translateAdd.play();
        textFadeIn.play();
        translateText.play();
        dateFadeIn.play();
        translateDate.play();
        assignmentFadeIn.play();
        translateAssignment.play();
    }


    public void addButtonFadeIn() {
        //Prevent The User From Clicking The Add Button Multiple Times
        addAssignmentButton.setDisable(true);
        addButton.setDisable(false);

        //For Clicking to Return Event, When the TextField and DatePicker have not Appeared yet don't do anything
        if(enterAssignmentText.getOpacity() == 0)
        {
            return;
        }

        //The TextField, and DatePicker Fade Out, Add Button Fade Back In
        TranslateTransition translateAdd = new TranslateTransition(Duration.millis(500), addButton);
        FadeTransition addFadeIn = new FadeTransition(Duration.millis(200), addButton);
        translateAdd.setByY(-60);
        translateAdd.setDelay(Duration.millis(200));
        addFadeIn.setDelay(Duration.millis(200));
        addFadeIn.setFromValue(0.0);
        addFadeIn.setToValue(1.0);


        TranslateTransition translateText = new TranslateTransition(Duration.millis(500), enterAssignmentText);
        FadeTransition textFadeOut = new FadeTransition(Duration.millis(200), enterAssignmentText);
        translateText.setByY(40);
        textFadeOut.setFromValue(1.0);
        textFadeOut.setToValue(0.0);

        TranslateTransition translateDate = new TranslateTransition(Duration.millis(500), datePicker);
        FadeTransition dateFadeOut = new FadeTransition(Duration.millis(200), datePicker);
        translateDate.setByY(40);
        dateFadeOut.setFromValue(1.0);
        dateFadeOut.setToValue(0.0);

        TranslateTransition translateAssignment = new TranslateTransition(Duration.millis(500), addAssignmentButton);
        FadeTransition assignmentFadeOut = new FadeTransition(Duration.millis(200), addAssignmentButton);
        translateAssignment.setByY(40);
        assignmentFadeOut.setFromValue(1.0);
        assignmentFadeOut.setToValue(0.0);

        //Play Animation
        addFadeIn.play();
        translateAdd.play();
        textFadeOut.play();
        translateText.play();
        dateFadeOut.play();
        translateDate.play();
        assignmentFadeOut.play();
        translateAssignment.play();
    }

    public void addAssignment() throws SQLException {
        //If The TextField Or DatePicker is Empty, Don't Do Anything
        if(enterAssignmentText.getText().equals("") || datePicker.getValue() == (null))
        {
            return;
        }
        String date = datePicker();
        //Setup Database to Insert The Assignment
        Connection con = dbConnection.dbConnector();
        String sql = "insert into assignmentInfo (assignment, date) values(?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, enterAssignmentText.getText());
        pst.setString(2, date);
        pst.execute();
        //Dynamically Add the Assignment Radio Button, and Deadline Label
        RadioButton assignment = new RadioButton();
        Label dueBy = new Label("Due By ");
        dueBy.setTextFill(Color.WHITE);
        dueBy.setTextFill(Color.WHITE);
        dueBy.setFont(Font.font("Calibri", 13));
        Label dueDate = new Label(date);
        dueDate.setTextFill(Color.WHITE);
        dueDate.setFont(Font.font("Calibri", 13));
        assignment.getStyleClass().add("assignment");
        assignment.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        assignment.setText(enterAssignmentText.getText());
        FlowPane dueFlow = new FlowPane();
        FlowPane assignmentFlow = new FlowPane();
        dueFlow.getChildren().add(dueBy);
        dueFlow.getChildren().add(dueDate);
        assignmentFlow.getChildren().add(assignment);
        assignmentPane.getChildren().add(dueFlow);
        assignmentPane.getChildren().add(assignmentFlow);

        //After Finish Set the TextField Back to Empty
        enterAssignmentText.setText("");
        datePicker.setValue(null);

        //Setup Database to Delete The Task
        String sql2 = "select * from assignmentInfo";
        PreparedStatement pst2 = con.prepareStatement(sql2);
        ResultSet result = pst2.executeQuery();
        while(result.next())
        {
            String current = result.getString("assignment");
            String currentDate = result.getString("date");
            assignment.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                    String sql = "delete from assignmentInfo where assignment = ? and date = ?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, current);
                    pst.setString(2, currentDate);
                    assignmentPane.getChildren().remove(assignmentFlow);
                    assignmentPane.getChildren().remove(dueFlow);
                    pst.execute();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        //After Finish with Everything, TextField and DatePicker Fade Out
        addButtonFadeIn();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //Use Singleton To Transfer Username From Login Scene
            String username = singleton.getUsername();
            welcomeUsername.setText(username);
            //Set Up Database to Display All the Task in the Table Upon Initialize
        Connection con = dbConnection.dbConnector();
        String sql = "select * from assignmentInfo ";
        PreparedStatement pst2 = con.prepareStatement(sql);
            ResultSet result = pst2.executeQuery();
            while(result.next())
            {
                //Use Information to Construct a Button and Label
                String current = result.getString("assignment");
                String currentDate = result.getString("date");
                RadioButton assignment = new RadioButton();
                Label dueBy = new Label("Due By ");
                dueBy.setTextFill(Color.WHITE);
                dueBy.setTextFill(Color.WHITE);
                dueBy.setFont(Font.font("Calibri", 13));
                Label dueDate = new Label(currentDate);
                dueDate.setTextFill(Color.WHITE);
                dueDate.setFont(Font.font("Calibri", 13));
                assignment.getStyleClass().add("assignment");
                assignment.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                assignment.setText(current);
                FlowPane dueFlow = new FlowPane();
                FlowPane assignmentFlow = new FlowPane();
                dueFlow.getChildren().add(dueBy);
                dueFlow.getChildren().add(dueDate);
                assignmentFlow.getChildren().add(assignment);
                assignmentPane.getChildren().add(dueFlow);
                assignmentPane.getChildren().add(assignment);
                //When RadioButton is Clicked It Will Delete From Both DataBase and The Screen
                assignment.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                        String sql = "delete from assignmentInfo where assignment = ? and date = ?";
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setString(1, current);
                        pst.setString(2, currentDate);
                        assignmentPane.getChildren().remove(assignment);
                        assignmentPane.getChildren().remove(dueFlow);
                        pst.execute();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void logOut() throws IOException {
        //Logout By Switch Back To Login Scene
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage)pane.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}


