package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxListCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.stage.Stage;

public class InsertDrinkController {
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
    ObservableList<String> categories;

    public void initialize(){
        categories = FXCollections.observableArrayList(QueryHelper.selectDrinkCategoryNames());
        categoryChoiceBox.setItems(categories);
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
        if(!drinkNameTextField.getText().isBlank() && !sizeTextField.getText().isBlank() && !priceTextField.getText().isBlank() && !itemNumberTextField.getText().isBlank() && categoryChoiceBox.getSelectionModel().getSelectedItem() != null) {
            float size;
            int price;
            if(QueryHelper.selectDrinkCategoryIdByName(categoryChoiceBox.getSelectionModel().getSelectedItem()) != null) {
                try {
                    size = Float.parseFloat(sizeTextField.getText());
                    price = Integer.parseInt(priceTextField.getText());
                    Integer categoryId = QueryHelper.selectDrinkCategoryIdByName(categoryChoiceBox.getSelectionModel().getSelectedItem());
                    try {
                        if(size > 0 && price >= 0) {
                            if (!QueryHelper.isItemNumberExistsForUser(itemNumberTextField.getText(), UserSession.getInstance().getId())) {
                                if (QueryHelper.insertNewDrinkForUser(itemNumberTextField.getText(), drinkNameTextField.getText(), size, price, categoryId, UserSession.getInstance().getId())) {
                                    showInfoDialog("Item successfully added!", "Success.");
                                    ((Stage) cancelButton.getScene().getWindow()).close();
                                } else {
                                    showErrorDialog("Something went wrong while inserting into the database. Please try again!", "DB Error");
                                }
                            } else {
                                showErrorDialog("Item with this item number already exists. Please try again.", "Item number Error");
                            }
                        } else {
                            showErrorDialog("Size and Price should be greater than 0.", "Invalid value");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        e.getCause();
                        showErrorDialog("Something went wrong while inserting into the database. Please try again!", "DB Error");
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

    public void onCancelButtonClick(){
        ((Stage) cancelButton.getScene().getWindow()).close();
    }


}
