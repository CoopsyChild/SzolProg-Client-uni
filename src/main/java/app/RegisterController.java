package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.json.JSONObject;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class RegisterController implements Initializable {
    @FXML
    private Label registerErrorMessageLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField passwordConfirmTextField;
    @FXML
    private TextField lastNameTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void backToLogin() {
        try {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.setScene(new Scene(root,520, 400));
        loginStage.show();
        ((Stage) cancelButton.getScene().getWindow()).close();

        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    // TODO Validator Class or ?Interface?
    public void onRegisterButtonClick(ActionEvent event) {
        if (!passwordTextField.getText().isBlank() && !usernameTextField.getText().isBlank() && !passwordConfirmTextField.getText().isBlank() && !lastNameTextField.getText().isBlank()){
            if(passwordValid(passwordTextField.getText(),passwordConfirmTextField.getText())){
                    registerUser(usernameTextField.getText(),passwordTextField.getText(),lastNameTextField.getText());
            } else {
                registerErrorMessageLabel.setText("Provided passwords don't match.");
            }
        } else {
                registerErrorMessageLabel.setText("Some fields are blank!");
        }
    }

    public boolean passwordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public void showConfirmationDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Successful Registration! Please click the OK button to go back to the login screen.");
        alert.showAndWait();
    }
    public void registerUser(String username, String password, String lastName){
         try {
             JSONObject jsonBody = new JSONObject();
             jsonBody.put("username",username);
             jsonBody.put("password",password);
             jsonBody.put("last_name",lastName);
             jsonBody.put("is_admin",0);
             HttpResponse<JsonNode> postResponse = Unirest.post("http://localhost/SzolProg-Rest-uni/users/register")
                     .header("Content-Type", "application/json")
                     .header("Accept", "application/json")
                     .body(jsonBody)
                     .asJson();
             showConfirmationDialog();
             backToLogin();
         } catch (Exception e){
             e.printStackTrace();
             e.getCause();
         }
    }
}