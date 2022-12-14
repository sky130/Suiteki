package ml.sky233;

public class SuitekiObject {
    private String AuthKey = "", Mac = "", DeviceName = "";

    public SuitekiObject() {

    }

    public SuitekiObject(String authKey, String mac, String name) {
        DeviceName = name;
        AuthKey = authKey;
        Mac = mac;
    }

    public SuitekiObject(String authKey, String mac) {
        AuthKey = authKey;
        Mac = mac;
    }

    public String getAuthKey() {
        return AuthKey;
    }

    public String toString() {
        return "AuthKey:" + AuthKey + "\nMac:" + Mac + "\nModel" + DeviceName;
    }

    public String getMac() {
        return Mac;
    }

    public String getDeviceName() {
        return DeviceName;
    }
}
