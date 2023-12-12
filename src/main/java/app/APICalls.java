package app;

import com.google.gson.Gson;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class APICalls {
    private APICalls() { // private constructor
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

    public static List<Category> selectDrinkCategories(){
        try {
            HttpResponse<JsonNode> getResponse = Unirest.get("http://localhost/SzolProg-Rest-uni/drink-category")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Token",UserSession.getInstance().getToken())
                    .asJson();
            return List.of(new Gson().fromJson(getResponse.getBody().toString(), Category[].class));
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

    public static boolean insertNewDrinkForUser(String itemNumber, String name, Float size, Integer price, Integer categoryId, Integer ownerId, Integer quantity){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("item_number",itemNumber);
            jsonBody.put("name",name);
            jsonBody.put("size",size);
            jsonBody.put("price",price);
            jsonBody.put("quantity",quantity);
            jsonBody.put("category_id",categoryId);
            jsonBody.put("owner_id",ownerId);
            HttpResponse <JsonNode> postResponse = Unirest.post("http://localhost/SzolProg-Rest-uni/drinks")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Token", UserSession.getInstance().getToken())
                    .body(jsonBody)
                    .asJson();
            return postResponse.isSuccess();
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }
    public static boolean updateDrinkForUser(Integer itemId, String itemNumber, String name, Float size, Integer price, Integer categoryId, Integer quantity){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("product_id",itemId);
            jsonBody.put("item_number",itemNumber);
            jsonBody.put("name",name);
            jsonBody.put("size",size);
            jsonBody.put("price",price);
            jsonBody.put("quantity",quantity);
            jsonBody.put("category_id",categoryId);
            HttpResponse <JsonNode> postResponse = Unirest.put("http://localhost/SzolProg-Rest-uni/drinks")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Token", UserSession.getInstance().getToken())
                    .body(jsonBody)
                    .asJson();
            return postResponse.isSuccess();
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }

    public static Integer selectDrinkCategoryIdByName(String name){
        try {
            List<Category> categoryObjects = APICalls.selectDrinkCategories();
            for (Category category  : categoryObjects) {
                if (name.equals(category.getName())) {
                    return category.getId();
                }
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
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("product_id",id);
            HttpResponse <JsonNode> deleteResponse = Unirest.delete("http://localhost/SzolProg-Rest-uni/drinks")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Token", UserSession.getInstance().getToken())
                    .body(jsonBody)
                    .asJson();
            return deleteResponse.getStatus()==200;
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }

    public static List<Drink> getUserDrinkStock(){
        try {
            HttpResponse<JsonNode> getResponse = Unirest.get("http://localhost/SzolProg-Rest-uni/drinks")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Token",UserSession.getInstance().getToken())
                    .asJson();
            return List.of(new Gson().fromJson(getResponse.getBody().toString(), Drink[].class));
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
