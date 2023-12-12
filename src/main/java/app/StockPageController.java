package app;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class StockPageController {

    @FXML
    private TableView<Drink> drinkTableView;
    @FXML
    private TableColumn<Drink,String> name;
    @FXML
    private TableColumn<Drink,Integer> id;
    @FXML
    private TableColumn<Drink,String> itemNumber;
    @FXML
    private TableColumn<Drink,Integer> price;
    @FXML
    private TableColumn<Drink,Integer> quantity;
    @FXML
    private TableColumn<Drink,String> category;
    @FXML
    private TableColumn<Drink,Float> size;
    @FXML
    private TableColumn<Drink,Integer> ownerId;
    @FXML
    private Button logoutButton;
    @FXML
    private Button deleteDrinkButton;
    @FXML
    private SplitPane windowPane;
    ObservableList<Drink> drinks;

    public void initialize(){
        name.setCellValueFactory(new PropertyValueFactory<Drink, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<Drink, Integer>("id"));
        itemNumber.setCellValueFactory(new PropertyValueFactory<Drink, String>("itemNumber"));
        price.setCellValueFactory(new PropertyValueFactory<Drink, Integer>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<Drink, Integer>("quantity"));
        category.setCellValueFactory(new PropertyValueFactory<Drink, String>("category"));
        size.setCellValueFactory(new PropertyValueFactory<Drink, Float>("size"));
        ownerId.setCellValueFactory(new PropertyValueFactory<Drink, Integer>("ownerId"));
        drinks=FXCollections.observableArrayList(APICalls.getUserDrinkStock());
        drinkTableView.setItems(drinks);
        if(!UserSession.getInstance().getIs_admin()) {
            ownerId.setVisible(false);
            id.setVisible(false);
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

    public void onInsertButtonClick(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("insertDrink.fxml")));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setScene(new Scene(root));
            windowPane.opacityProperty().setValue(0.4);
            loginStage.showAndWait();
            windowPane.opacityProperty().setValue(1);
            initialize();
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void onUpdateButtonClick(){
        if(drinkTableView.getSelectionModel().getSelectedItem() != null){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Objects.requireNonNull(getClass().getResource("updateDrink.fxml")));
                Parent root = loader.load();

                // This passes the selected drink to the Update Item Scene
                UpdateDrinkController controller = loader.getController();
                controller.initDrinkData(drinkTableView.getSelectionModel().getSelectedItem());

                Stage loginStage = new Stage();
                loginStage.initStyle(StageStyle.UNDECORATED);
                loginStage.initModality(Modality.APPLICATION_MODAL);
                loginStage.setScene(new Scene(root));
                windowPane.opacityProperty().setValue(0.4);
                loginStage.showAndWait();
                windowPane.opacityProperty().setValue(1);
                initialize();
            } catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
        else {
            showErrorDialog("No item selected!","No selection!");
        }
    }

    public void onAccountPageButtonClick(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("accountPage.fxml")));
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

    public void onDeleteDrinkButtonClick(){
        if(drinkTableView.getSelectionModel().getSelectedItem() != null){
            if(APICalls.deleteDrinkById(drinkTableView.getSelectionModel().getSelectedItem().getId())){
            drinkTableView.getItems().removeAll(drinkTableView.getSelectionModel().getSelectedItem());
            }
            else {
                showErrorDialog("Something went wrong! DB Error!","DB Error!");
            }
        }
        else {
            showErrorDialog("No item selected!","No selection!");
        }
    }
    public void onExitButtonClick() {
        Platform.exit();
    }
}
