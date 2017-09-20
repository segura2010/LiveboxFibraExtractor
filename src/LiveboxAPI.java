
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Locale;
import org.json.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alberto
 */
public enum LiveboxAPI {
    
    INSTANCE;
    
    private String DOMAIN = "192.168.1.1";
    private String MAC = "00:00:00:00:00:00";
    private String ADMIN_PASSWORD = "";

    
    private String getBaseUrl(){
        return "http://" + DOMAIN + "/API/";
    }
    
    public void setMAC(String m){
        MAC = m;
    }
    public void setAdminPassword(String m){
        ADMIN_PASSWORD = m;
    }
    public String getAdminPassword(){
        return ADMIN_PASSWORD;
    }
    public String getMAC(){
        return INSTANCE.MAC;
    }
    public void setDOMAIN(String d){
        DOMAIN = d;
    }
    
    public JSONObject getWAN() throws UnirestException, Exception{
        String url = INSTANCE.getBaseUrl() + "WAN";
        
        JSONObject result = Unirest.get(url).basicAuth("ApiUsr", "ApiUsrPass").asJson().getBody().getObject();
        INSTANCE.MAC = result.getString("MACAddress");
        
        System.out.println(PasswordGenerator.GenerateAuth(INSTANCE.MAC));
        INSTANCE.ADMIN_PASSWORD = PasswordGenerator.encrypt(INSTANCE.MAC);
        
        return result;
    }
    
    public static JSONArray getLines() throws UnirestException, Exception{
        String url = INSTANCE.getBaseUrl() + "VoIP/SIP/Lines";
        
        JSONArray result = Unirest.get(url).basicAuth("UsrAdmin", INSTANCE.ADMIN_PASSWORD).asJson().getBody().getArray();
        return result;
    } 
    
    public static JSONObject getLine(String lineName) throws UnirestException, Exception{
        String url = INSTANCE.getBaseUrl() + "VoIP/SIP/Lines/{line}";
        
        JSONObject result = Unirest.get(url).routeParam("line", lineName).basicAuth("UsrAdmin", INSTANCE.ADMIN_PASSWORD).asJson().getBody().getObject();
        return result;
    } 
    
    public static JSONObject getSIP() throws UnirestException, Exception{
        String url = INSTANCE.getBaseUrl() + "VoIP/SIP";
        
        JSONObject result = Unirest.get(url).basicAuth("UsrAdmin", INSTANCE.ADMIN_PASSWORD).asJson().getBody().getObject();
        return result;
    } 
    
    public static JSONObject setOspPassword(String password) throws UnirestException, Exception{
        String url = INSTANCE.getBaseUrl() + "Access/OspApi";
        
        String body = "{\"User\":\"UsrOrange\",\"Password\":\""+ password +"\"}";
        
        JSONObject result = Unirest.put(url).basicAuth("UsrAdmin", INSTANCE.ADMIN_PASSWORD).body(body).asJson().getBody().getObject();
        return result;
    } 
    
    public static JSONObject setUsrAdminPassword(String password) throws UnirestException, Exception{
        String url = INSTANCE.getBaseUrl() + "Access/LanApi";
        
        String body = "{\"User\":\"UsrAdmin\",\"Password\":\""+ password +"\"}";
        
        JSONObject result = Unirest.put(url).basicAuth("UsrAdmin", INSTANCE.ADMIN_PASSWORD).body(body).asJson().getBody().getObject();
        return result;
    } 
    
    public static String rebootRouter() throws UnirestException, Exception{
        String url = INSTANCE.getBaseUrl() + "GeneralInfo/Reboot";
        
        String result = Unirest.put(url).basicAuth("UsrAdmin", INSTANCE.ADMIN_PASSWORD).asString().getBody();
        return result;
    } 
}



