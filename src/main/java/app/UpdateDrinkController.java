package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateDrinkController {
    @FXML
    private TextField drinkNameTextField;
    @FXML
    private TextField sizeTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField itemNumberTextField;
    @FXML
    private Button cancelButton;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    private int itemId;
    private String initialItemNumber;
    ObservableList<String> categories;
    public void initDrinkData(Drink selectedDrink){
        drinkNameTextField.setText(selectedDrink.getName());
        sizeTextField.setText(selectedDrink.getSize().toString());
        priceTextField.setText(selectedDrink.getPrice().toString());
        itemNumberTextField.setText(selectedDrink.getItemNumber());
        categoryChoiceBox.getSelectionModel().select(selectedDrink.getCategory());
        itemId=selectedDrink.getId();
        initialItemNumber=selectedDrink.getItemNumber();
    }
    public void initialize(){
        categories = FXCollections.observableArrayList(APICalls.selectDrinkCategoryNames());
        categoryChoiceBox.setItems(categories);
    }
    public void onCancelButtonClick(){
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
    public void onUpdateButtonClick(){
        if(!drinkNameTextField.getText().isBlank() && !sizeTextField.getText().isBlank() && !priceTextField.getText().isBlank() && !itemNumberTextField.getText().isBlank() && categoryChoiceBox.getSelectionModel().getSelectedItem() != null) {
            float size;
            int price;
            if(APICalls.selectDrinkCategoryIdByName(categoryChoiceBox.getSelectionModel().getSelectedItem()) != null) {
                try {
                    size = Float.parseFloat(sizeTextField.getText());
                    price = Integer.parseInt(priceTextField.getText());
                    Integer categoryId = APICalls.selectDrinkCategoryIdByName(categoryChoiceBox.getSelectionModel().getSelectedItem());
                    try {
                        if(size > 0 && price >= 0) {
                            if (initialItemNumber.equals(itemNumberTextField.getText()) || !APICalls.isItemNumberExistsForUser(itemNumberTextField.getText(), UserSession.getInstance().getId())) {
                                if (APICalls.updateDrinkForUser(itemId, itemNumberTextField.getText(), drinkNameTextField.getText(), size, price, categoryId)) {
                                    showInfoDialog("Item successfully updated!", "Success.");
                                    ((Stage) cancelButton.getScene().getWindow()).close();
                                } else {
                                    showErrorDialog("Something went wrong while updating the item. Please try again!", "DB Error");
                                }
                            } else {
                                showErrorDialog("Item with this item number already exists (Original item number was: " + initialItemNumber + "). Please try again.", "Item number Error");
                            }
                        } else {
                            showErrorDialog("Size and Price should be greater than 0.", "Invalid value");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        e.getCause();
                        showErrorDialog("Something went wrong while updating the item. Please try again!", "DB Error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getCause();
                    showErrorDialog("Invalid size or price value ", "Invalid value");
                }
            }
        } else {
            showErrorDialog("Empty Field(s)","Empty Fields Error");
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
}
