package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class QueryHelper {
    private QueryHelper() { // private constructor
    }
    public static Connection dBconnection(){
        DatabaseConnection dbConnection = new DatabaseConnection();
        return dbConnection.getConnection();
    }
    public static Integer selectUserIdByUsername(String username){
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username=?");
            preparedStatement.setString(1,username);
            ResultSet queryResult = preparedStatement.executeQuery();
            queryResult.next();
            return queryResult.getInt(1);
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }
    public static User selectUserData(Integer id){
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, username, password, registration_date, last_name FROM user WHERE id=?");
            preparedStatement.setString(1,id.toString());
            ResultSet queryResult = preparedStatement.executeQuery();
            queryResult.next();
            return new User(queryResult.getString(2),queryResult.getString(5),queryResult.getString(4));
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }

    public static Integer updateUserPassword(Integer id, String newPassword){
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET password=? WHERE id=?");
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,id.toString());
            return preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return 0;
    }

    public static String getCategoryName(Integer id){
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM drink_category WHERE id=?");
            preparedStatement.setString(1,id.toString());
            ResultSet queryResult = preparedStatement.executeQuery();
            queryResult.next();
            return queryResult.getString(1);
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }

    public static List<String> selectDrinkCategoryNames(){
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM drink_category");
            List<String> categoryNames = new ArrayList<String>();
            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()){
                String categoryName= queryResult.getString(1);
                categoryNames.add(categoryName);
            }
            return categoryNames;
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }

    public static boolean isItemNumberExistsForUser(String itemNumber,Integer userId) {
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(1) FROM drink_stock WHERE item_number=? AND owner_id=?");
            preparedStatement.setString(1, itemNumber);
            preparedStatement.setString(2, userId.toString());
            ResultSet queryResult = preparedStatement.executeQuery();
            queryResult.next();
            return queryResult.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return true;
    }

    public static boolean insertNewDrinkForUser(String itemNumber, String name, Float size, Integer price, Integer categoryId, Integer ownerId){
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO drink_stock (item_number, name, size, price, category_id, owner_id) values (?,?,?,?,?,?)");
            preparedStatement.setString(1,itemNumber);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,size.toString());
            preparedStatement.setString(4,price.toString());
            preparedStatement.setString(5,categoryId.toString());
            preparedStatement.setString(6,ownerId.toString());
            if(preparedStatement.executeUpdate()!=0) return true;
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }
    public static boolean updateDrinkForUser(Integer itemId,String itemNumber, String name, Float size, Integer price, Integer categoryId){
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE drink_stock SET item_number=?, name=?, size=?, price=?, category_id=? WHERE id=?");
            preparedStatement.setString(1,itemNumber);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,size.toString());
            preparedStatement.setString(4,price.toString());
            preparedStatement.setString(5,categoryId.toString());
            preparedStatement.setString(6,itemId.toString());
            if(preparedStatement.executeUpdate()!=0) return true;
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }

    public static Integer selectDrinkCategoryIdByName(String name){
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM drink_category WHERE name=?");
            preparedStatement.setString(1,name);
            ResultSet queryResult = preparedStatement.executeQuery();
            if(queryResult.next()) {
                return queryResult.getInt(1);
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }
    public static Boolean deleteDrinkById(Integer id){
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM drink_stock WHERE id=?");
            preparedStatement.setString(1,id.toString());
            if(preparedStatement.executeUpdate()!=0){
                return true;
            }
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }

    public static List<Drink> selectUserDrinkStock(Integer id){
        try {
            Connection connection = dBconnection();
            List<Drink> drinkList=new ArrayList<Drink>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, item_number, name, size, price, category_id FROM drink_stock WHERE owner_id=?");
            preparedStatement.setString(1,id.toString());
            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()){
                Drink drink= new Drink(queryResult.getInt(1), queryResult.getString(2), queryResult.getString(3), queryResult.getFloat(4), queryResult.getInt(5),getCategoryName(queryResult.getInt(6)) );
                drinkList.add(drink);
            }
            return drinkList;
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }

    public static String updateUserLastName(String currentUsername, Integer id, String newUsername) {
        try {
            Connection connection = dBconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET last_name=? WHERE id=?");
            preparedStatement.setString(1,newUsername);
            preparedStatement.setString(2,id.toString());
            if(preparedStatement.executeUpdate()>0){
                return newUsername;
            }
            else {
                return currentUsername;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return currentUsername;
    }
}
