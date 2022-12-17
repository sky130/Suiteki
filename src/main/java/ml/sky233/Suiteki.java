package ml.sky233;

import android.util.Log;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import ml.sky233.util.Eson.*;
import ml.sky233.util.Text.*;
import ml.sky233.SuitekiObject;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Suiteki {
    private String[] AuthKey;//AuthKey列表
    // private Map<String,ArrayList> return_map = new HashMap<>();
    private ArrayList<SuitekiObject> KeyArray; 
    private String user_email="",user_password="",user_code="";
    private String app_token = "";
    private String user_id = "";
    private String result_code = "";
    private String method = "";
    private String log = "";

    public Suiteki(String email,String password){
        user_email = email;
        user_password = password;
    }   

    public void setEmail(String email){
        user_email = email;
    }

    public void setPassword(String password){
        user_password = password;
    }

    public void setCode(String code){
        user_code = code;
    }

    public boolean isUserEmpty(){
        if(user_email != "" && user_password != ""){
            return true;
        }else if(code != ""){
            return true;
        }else{
            return false;
        }
    }

    public void getHuamiToken() {
        Thread thread = null;
        //用线程发送请求
        thread = new Thread(new Runnable() {
            String response_body = "";

            public void run() {
                ArrayList<SuitekiObject> mapArray = new ArrayList<>();
                OkHttpClient client = new OkHttpClient();
                Headers header = new Headers.Builder()
                        .add("apptoken", app_token)//app令牌
                        .build();
                Request getRequest = new Request.Builder()
                        .url("https://api-mifit-us2.huami.com/users/" + user_id + "/devices?enableMultiDevice=true")//这里中间要改为user_id
                        .headers(header)
                        .build();
                try {
                    Response response = client.newCall(getRequest).execute();
                    response_body = response.body().string();
                    Object object = getArray(toObject(response_body), "items");//解析Json
                    String[] authkeyList = new String[getArrayLength(object)];//解析Json
                    for (int a = 0; getArrayLength(object) > a; a++) {
                        // map.put("MacAddress",getObjectText(getArrayObject(object, a), "macAddress"));
                        SuitekiObject obj = new SuitekiObject(getObjectText(toObject(getObjectText(getArrayObject(object, a), "additionalInfo")), "auth_key"),);
                        // Map<String,String> map = new HashMap<>();
                        // map.put("Authkey",);
                        // map.put("MacAddress",getObjectText(getArrayObject(object, a), "macAddress"));
                        mapArray.add(obj);
                    }
                    KeyArray = mapArray;
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();//等待线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList getResultData(){
        return KeyArray;
    }

    public void loginHuami(){
        if(user_email != "" && user_password != ""){
            loginHuami(user_email,user_password);
        }else if(code != ""){
            loginHuami(user_code);
        }else{
            result_code="-1"
        }
    }
    
    //通过小米登录接口登录Huami
    public void loginHuami(String code) {
        method = "Xiaomi";
        Thread thread = null;
        thread = new Thread(new Runnable() {
            String response_body = "";

            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()//构建请求Body，数据类型为application/x-www-form-urlencoded
                        .add("dn", "account.huami.com,api-user.huami.com,app-analytics.huami.com,api-watch.huami.com,api-analytics.huami.com,api-mifit.huami.com")
                        .add("app_version", "5.9.2-play_100355")
                        .add("source", "com.huami.watch.hmwatchmanager")
                        .add("country_code", "US")
                        .add("device_id", createDeviceCode())
                        .add("third_name", "mi-watch")
                        .add("lang", "en")
                        .add("device_model", "android_phone")
                        .add("allow_registration", "false")
                        .add("app_name", "com.huami.midong")
                        .add("code", code)
                        .add("grant_type", "request_token")
                        .build();
                Request postRequest = new Request.Builder()
                        .url("https://account.huami.com/v2/client/login")//请求接口
                        .post(requestBody)//post请求
                        .build();
                try {
                    Response response = client.newCall(postRequest).execute();
                    response_body = response.body().string();//因为response.body()只能调用一次,必须这样
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (getObjectText(toObject(response_body), "result").equals("ok")) {
                    result_code = "200";
                    app_token = getObjectText(getObject(toObject(response_body), "token_info"), "app_token");//app令牌,接下来会用到这个token
                    user_id = getObjectText(getObject(toObject(response_body), "token_info"), "user_id");//用户id,稍后会用到
                } else
                    result_code = getObjectText(toObject(response_body), "error_code");
            }
        });
        thread.start();
        try {
            thread.join();//等待线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    //通过Amazfit接口登录Huami
    public void loginHuami(String email, String password) {
        method = "Amazfit";
        Thread thread = null;
        thread = new Thread(new Runnable() {
            String response_body = "";
            String header = "";
            String token = "";

            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder().followRedirects(false).build();
                RequestBody requestBody = new FormBody.Builder()//构建请求Body，数据类型为application/x-www-form-urlencoded
                        .add("client_id", "HuaMi")
                        .add("country_code", "US")
                        .add("password", password)
                        .add("redirect_uri", "https://sky233.ml/suiteki")
                        .add("token", "access")
                        .build();
                Request postRequest = new Request.Builder()
                        .url("https://api-user.huami.com/registrations/" + email.replace("@", "%40") + "/tokens")//请求接口
                        .post(requestBody)//post请求
                        .build();
                try {
                    Response response = client.newCall(postRequest).execute();
                    header = response.header("Location");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result_code = "200";
                if (getOneParameter(header, "error").equals("401")) {
                    result_code = "401";
                } else
                    token = getOneParameter(header, "access");
                    client = new OkHttpClient();
                    requestBody = new FormBody.Builder()//构建请求Body，数据类型为application/x-www-form-urlencoded
                            .add("dn", "account.huami.com,api-user.huami.com,app-analytics.huami.com,api-watch.huami.com,api-analytics.huami.com,api-mifit.huami.com")
                            .add("app_version", "5.9.2-play_100355")
                            .add("source", "com.huami.watch.hmwatchmanager")
                            .add("country_code", "US")
                            .add("device_id", createDeviceCode())
                            .add("third_name", "huami")
                            .add("lang", "en")
                            .add("device_model", "android_phone")
                            .add("allow_registration", "false")
                            .add("app_name", "com.huami.midong")
                            .add("code", token)
                            .add("grant_type", "access_token")
                            .build();
                postRequest = new Request.Builder()
                        .url("https://account.huami.com/v2/client/login")//请求接口
                        .post(requestBody)//post请求
                        .build();
                try {
                    Response response = client.newCall(postRequest).execute();
                    response_body = response.body().string();//因为response.body()只能调用一次
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (getObjectText(toObject(response_body), "result").equals("ok")) {
                    result_code = "200";
                    app_token = getObjectText(getObject(toObject(response_body), "token_info"), "app_token");//app令牌,接下来会用到这个token
                    user_id = getObjectText(getObject(toObject(response_body), "token_info"), "user_id");//用户id,稍后会用到
                } else
                    result_code = getObjectText(toObject(response_body), "error_code");
            }
        });
        thread.start();
        try {
            thread.join();//等待线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getResultCode(){
        return result_code;
    }

    private String getOneParameter(String url, String keyWord) {
        String retValue = "";
        try {
            final String charset = "utf-8";
            url = URLDecoder.decode(url, charset);
            if (url.indexOf('?') != -1) {
                final String contents = url.substring(url.indexOf('?') + 1);
                String[] keyValues = contents.split("&");
                for (int i = 0; i < keyValues.length; i++) {
                    String key = keyValues[i].substring(0, keyValues[i].indexOf("="));
                    String value = keyValues[i].substring(keyValues[i].indexOf("=") + 1);
                    if (key.equals(keyWord)) {
                        if (value != null || !"".equals(value.trim())) {
                            retValue = value;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }

    //取设备的型号,仅限一个设备,准备废弃
    public String getModel(String key) {
        String[] loge = AnalyzeText(log, "\n");
        String[] model;
        String cache = "";
        for (int a = 0; loge.length > a; a++) {
            if (Lookfor(loge[a], key, 0) != -1) {
                cache = getTheTexto(loge[a], "model='", "', name=") + "\n" + cache;
            }
        }
        model = AnalyzeText(cache, "\n");
        model = deleteText(model);
        return model[0];
    }

    //取多个手环AuthKey,可能会报错,建议检测length是否大于0再使用
    public ArrayList getAuthKeyList() {
        String[] loge;
        String[] key = null;
        String cache = "";
        ArrayList<SuitekiObject> objs = new ArrayList<>;
        loge = AnalyzeText(log, "\n");
        for (int a = 0; loge.length > a; a++) {
            if (Lookfor(loge[a], "authKey", 0) != -1) {
                String atk = getTextRight(getTheTexto(loge[a], "authKey", ","), 32), mac = getTextRight(getTheTexto(loge[a], "macAddress", ","), 16),getTheTexto(loge[a], "model='", "', name="))
                SuitekiObject obj = new SuitekiObject();
            }
        }
        key = AnalyzeText(cache, "\n");
        key = deleteText(key);
        return objs;
    }

    //设置Log文件内容
    public void setLog(String str) {
        log = str;
    }
}
