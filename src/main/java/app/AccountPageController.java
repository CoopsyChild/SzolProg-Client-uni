package app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class AccountPageController {

    @FXML
    private PasswordField newPassField;
    @FXML
    private PasswordField newPassConfirmField;
    @FXML
    private TextField newLastNameTextField;
    @FXML
    private Button logoutButton;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label registrationDateLabel;
    @FXML
    private Label usernameLabel;

    public void initialize(){
        lastNameLabel.setText(UserSession.getInstance().getLastName());
        usernameLabel.setText(UserSession.getInstance().getUsername());
        registrationDateLabel.setText(UserSession.getInstance().getRegistrationDate());
    }

    public void onNewPassSubmitButton(){
        if(!newPassField.getText().isBlank() && !newPassConfirmField.getText().isBlank()) {
            if (newPassField.getText().equals(newPassConfirmField.getText())) {
                APICalls.updateUserPassword(UserSession.getInstance().getId(),newPassField.getText());
                showInfoDialog("Password successfully updated","Success!");
            } else {
                showErrorDialog("Provided passwords don't match. Please try again","Match Error");
            }
        }
        else {
            showErrorDialog("Blank Password field(s).","Blank Field Error");
        }
    }
    public void showErrorDialog(String message, String title){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInfoDialog(String message, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onNewLastNameSubmitButtonClick(){
        if(!newLastNameTextField.getText().isBlank()) {
            if(!APICalls.updateUserLastName(UserSession.getInstance().getLastName(), UserSession.getInstance().getId(), newLastNameTextField.getText()).equals(UserSession.getInstance().getLastName())) {
                showInfoDialog("Last name successfully changed!","Success!");
                UserSession.getInstance().setLastName(newLastNameTextField.getText());
                lastNameLabel.setText(UserSession.getInstance().getLastName());
                newLastNameTextField.setText("");
            } else {
                showErrorDialog("Something went wrong or the new last name that you given is the same as before","Error");
            }
        } else {
            showErrorDialog("Name field is empty!","Empty Field");
        }
    }

    public void onStockButtonClick(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("stockPage.fxml")));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root));
            loginStage.show();
            ((Stage) logoutButton.getScene().getWindow()).close();
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void onLogoutButtonClick(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root,520, 400));
            loginStage.show();
            UserSession.getInstance().clearUserSession();
            ((Stage) logoutButton.getScene().getWindow()).close();
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    public void onExitButtonClick() {
        Platform.exit();
    }
}
