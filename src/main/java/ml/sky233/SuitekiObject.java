package ml.sky233;

public class SuitekiObject{
    private AuthKey="",Mac="";

    public SuitekiObject(){
        
    }

    public SuitekiObject(String authKey,String mac){
        AuthKey = authKey;
        Mac = mac;
    }

    public String getAuthKey(){
        return AuthKey;
    }

    public String getMac(){
        return Mac;
    }
}