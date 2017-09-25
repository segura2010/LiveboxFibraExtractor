/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alberto
 */
public class LiveboxFibraExtractor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try{
            if(args.length >= 1){
                // Users can pass the MAC address from command line in order to
                // generate the default password for testintg purposes
                System.out.println(PasswordGenerator.GenerateAuth(args[0]));
            }
            
            GUI gui = new GUI();
            gui.setVisible(true);
            gui.setTitle("Livebox Fibra Extractor");
            
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public static void getAllSIPData() throws UnirestException, Exception{
        LiveboxAPI.INSTANCE.getWAN();
        System.out.println(LiveboxAPI.INSTANCE.getMAC());
        
        JSONArray lines = LiveboxAPI.getLines();
        //System.out.println(lines.toString());
        for(int i=0;i<lines.length();i++){
            JSONObject l = lines.getJSONObject(i);
            //System.out.println(l.getString("name"));
            if(l.getString("status").equals("Up"))
            {
                JSONObject lineInfo = LiveboxAPI.getLine(l.getString("name"));
                //System.out.println(lineInfo.toString());
                
                System.out.println("userId: "+ lineInfo.getString("name"));
                System.out.println("authPassword: "+ lineInfo.getString("authPassword"));
                System.out.println("authId: "+ lineInfo.getString("authUserName"));
            }
        }
        
        JSONObject sip = LiveboxAPI.getSIP();
        System.out.println("userAgentDomain: "+ sip.getString("userAgentDomain"));
        System.out.println("outboundProxyServer: "+ sip.getString("outboundProxyServer"));
        System.out.println("outboundProxyServerPort: "+ sip.getInt("outboundProxyServerPort"));
    }
    
   
}
