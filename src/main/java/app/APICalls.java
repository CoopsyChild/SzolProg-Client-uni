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
    public static boolean updateUserData(Integer userId,String newPassword, String newLastName){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id",userId);
            if(!newLastName.isEmpty())
            {
                jsonBody.put("last_name",newLastName);
            }
            if(!newPassword.isEmpty())
            {
                jsonBody.put("password",newPassword);
            }
            HttpResponse <JsonNode> postResponse = Unirest.put("http://localhost/SzolProg-Rest-uni/users")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Token", UserSession.getInstance().getToken())
                    .body(jsonBody)
                    .asJson();
            return postResponse.isSuccess();
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return false;
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
}
