package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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
    private TextField quantityTextField;
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
        quantityTextField.setText(selectedDrink.getQuantity().toString());
        itemNumberTextField.setText(selectedDrink.getItemNumber());
        List<Category> categoryObjects = APICalls.selectDrinkCategories();
        String categoryName = null;
        for (Category category  : categoryObjects) {
            if (category.getId() == Integer.parseInt(selectedDrink.getCategory())){
                categoryName=category.getName();
            }
        }
        categoryChoiceBox.getSelectionModel().select(categoryName);
        itemId=selectedDrink.getId();
        initialItemNumber=selectedDrink.getItemNumber();
    }
    public void initialize(){
        List<Category> categoryObjects = APICalls.selectDrinkCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category  : categoryObjects) {
            categoryNames.add(category.getName());
        }
        categories = FXCollections.observableArrayList(categoryNames);
        categoryChoiceBox.setItems(categories);
    }
    public void onCancelButtonClick(){
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
    public void onUpdateButtonClick(){
        if(!quantityTextField.getText().isBlank() && !drinkNameTextField.getText().isBlank() && !sizeTextField.getText().isBlank() && !priceTextField.getText().isBlank() && !itemNumberTextField.getText().isBlank() && categoryChoiceBox.getSelectionModel().getSelectedItem() != null) {
            float size;
            int price;
            int quantity;
            if(APICalls.selectDrinkCategoryIdByName(categoryChoiceBox.getSelectionModel().getSelectedItem()) != null) {
                try {
                    size = Float.parseFloat(sizeTextField.getText());
                    price = Integer.parseInt(priceTextField.getText());
                    quantity = Integer.parseInt(quantityTextField.getText());
                    Integer categoryId = APICalls.selectDrinkCategoryIdByName(categoryChoiceBox.getSelectionModel().getSelectedItem());
                    try {
                        if(size > 0 && price >= 0 && quantity >= 0) {
                                if (APICalls.updateDrinkForUser(itemId, itemNumberTextField.getText(), drinkNameTextField.getText(), size, price, categoryId, quantity)) {
                                    showInfoDialog("Item successfully updated!", "Success.");
                                    ((Stage) cancelButton.getScene().getWindow()).close();
                                } else {
                                    showErrorDialog("Something went wrong while updating the item. Please try again!", "DB Error");
                                }
                        } else {
                            showErrorDialog("Quantity Size and Price should be greater than 0.", "Invalid value");
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
