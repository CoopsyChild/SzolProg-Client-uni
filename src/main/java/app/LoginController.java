package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;
import java.net.URL;

public class LoginController implements Initializable {
    @FXML
    private Label loginErrorMessageLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label signUpLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // TODO New Window Opener
    public void onSignUpLabelClick(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root,400, 500));
            loginStage.show();
            ((Stage) signUpLabel.getScene().getWindow()).close();
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    public void onLoginButtonClick(ActionEvent event) {
        if (!passwordTextField.getText().isBlank() && !usernameTextField.getText().isBlank()){
            validateLogin(usernameTextField.getText(),passwordTextField.getText());
        } else {
            if (passwordTextField.getText().isBlank() && usernameTextField.getText().isBlank()) {
                loginErrorMessageLabel.setText("Blank Password and Password field.");
            }
            else if (usernameTextField.getText().isBlank()){
                loginErrorMessageLabel.setText("Blank Username field.");
            }
            else {
                loginErrorMessageLabel.setText("Blank Password field.");
            }
        }
    }
    public void createUserSession(Integer userId){
        User userData= QueryHelper.selectUserData(userId);
        if (userData != null){
            var session = UserSession.getInstance();
            session.setId(userId);
            session.setLastName(userData.getLastName());
            session.setUsername(userData.getUsername());
            session.setRegistrationDate(userData.getRegistrationDate());
        }

    }
    public void validateLogin(String username, String password){
        //TODO  Password hashing

         try {
             DatabaseConnection dbConnection = new DatabaseConnection();
             Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(1) FROM user WHERE username=? AND password=?");
             preparedStatement.setString(1,username);
             preparedStatement.setString(2,password);
             ResultSet queryResult = preparedStatement.executeQuery();

             while (queryResult.next()){
                 if (queryResult.getInt(1) == 1) {
                     createUserSession(QueryHelper.selectUserIdByUsername(username));
                     try {
                         Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("accountPage.fxml")));
                         Stage loginStage = new Stage();
                         loginStage.initStyle(StageStyle.UNDECORATED);
                         loginStage.setScene(new Scene(root));
                         loginStage.show();
                         ((Stage) cancelButton.getScene().getWindow()).close();
                     } catch (Exception e){
                         e.printStackTrace();
                         e.getCause();
                         loginErrorMessageLabel.setText("Application crashed");
                     }
                 } else {
                     loginErrorMessageLabel.setText("Invalid Username or Password. Please try again!");
                 }
             }
         } catch (Exception e){
             e.printStackTrace();
             e.getCause();
             loginErrorMessageLabel.setText("Database connection failed.");
         }
    }
}