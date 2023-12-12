package app;


import com.google.gson.Gson;

import com.google.gson.JsonObject;
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
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.json.JSONObject;


import java.util.Objects;
import java.util.ResourceBundle;
import java.net.URL;


public class LoginController implements Initializable {
    @FXML
    private Label loginErrorMessageLabel;
    @FXML
    private Button cancelButton;
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
        loginErrorMessageLabel.setText("");
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
    public void createUserSession(HttpResponse<JsonNode> login, String username){
        JSONObject jsonResponse = login.getBody().getObject();
        if (jsonResponse != null){
            var session = UserSession.getInstance();
            session.setId(jsonResponse.getInt("id"));
            session.setLastName(jsonResponse.getString("last_name"));
            session.setUsername(username);
            session.setRegistrationDate(jsonResponse.getString("registration_date"));
            session.setToken(jsonResponse.getString("token"));
            session.setIs_admin(jsonResponse.getInt("is_admin") == 1);
        }
    }
    public void validateLogin(String username, String password){
        //TODO  Password hashing
         try {
             JSONObject jsonBody = new JSONObject();
             jsonBody.put("username",username);
             jsonBody.put("password",password);
             HttpResponse <JsonNode> loginResponse = Unirest.post("http://localhost/SzolProg-Rest-uni/users/login")
                     .header("Content-Type", "application/json")
                     .header("Accept", "application/json")
                     .body(jsonBody)
                     .asJson();
                 if (loginResponse.getStatus() == 200) {
                     createUserSession(loginResponse, username);
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
         } catch (Exception e){
             e.printStackTrace();
             e.getCause();
             loginErrorMessageLabel.setText("Database connection failed.");
         }
    }
}