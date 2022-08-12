package ml.sky233;

import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Suiteki {
    public static String[] AuthKey;

    public static String[] getKey(String code){
        Thread thread = null;
        thread = new Thread(new Runnable() {
            String token = "";
            String user = "";
            String text = "";
            //String[] authkeyList;
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("dn", "account.huami.com,api-user.huami.com,app-analytics.huami.com,api-watch.huami.com,api-analytics.huami.com,api-mifit.huami.com")
                        .add("app_version", "5.9.2-play_100355")
                        .add("source", "com.huami.watch.hmwatchmanager")
                        .add("country_code", "US")
                        .add("device_id", "02:00:00:6f:ad:18")
                        .add("third_name", "mi-watch")
                        .add("lang", "en")
                        .add("device_model", "android_phone")
                        .add("allow_registration", "false")
                        .add("app_name", "com.huami.midong")
                        .add("code", code)
                        .add("grant_type", "request_token")
                        .build();
                Request postRequest = new Request.Builder()
                        .url("https://account.huami.com/v2/client/login")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(postRequest).execute();
                    text = response.body().string();
                    Log.d("Suiteki.test", text);
                    token = getObjectText(getObject(toObject(text), "token_info"), "app_token");
                    user = getObjectText(getObject(toObject(text), "token_info"), "user_id");
                    Log.d("Suiteki.test", "user : " + user + "\n token : " + token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!getObjectText(toObject(text),"error_code").equals("0106")) {
                    client = new OkHttpClient();
                    Headers header = new Headers.Builder()
                            .add("apptoken", token)
                            .build();
                    postRequest = new Request.Builder()
                            .url("https://api-mifit-us2.huami.com/users/" + user + "/devices?enableMultiDevice=true")
                            .headers(header)
                            .build();
                    try {
                        Response response = client.newCall(postRequest).execute();
                        text = response.body().string();
                        Log.d("Suiteki.test", text);
                        Object object = getArray(toObject(text),"items");
                        String[] authkeyList = new String[getArrayLength(object)];
                        for(int a = 0;getArrayLength(object) > a;a++){
                            authkeyList[a] = getObjectText(toObject(getObjectText(getArrayObject(object,a), "additionalInfo")),"auth_key") + "\n" + getObjectText(getArrayObject(object,a), "macAddress");
                            //getObjectText(getArrayObject(object,a), "macAddress");
                        }
                        AuthKey = authkeyList;
                        Log.d("Suiteki.test",text);
                        Log.d("Suiteki.test", Arrays.toString(authkeyList));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    String[] authkeyList = new String[1];
                    AuthKey[0] = "0106";
                }
            }

        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return AuthKey;
    }

    public static String getAuthKey(String log){
        String key = getTextRight(getTheTexto(log,"authKey",","),32);
        Log.d("Suiteki.test","Authkey:" + key);
        return key;
    }

    public static boolean isMoreAuthkey(String log){
        String[] loge = getAuthKeyList(log);
        if (loge.length > 1){
            return true;
        }else{
            return false;
        }
    }

    public static String getModel(String key,String log){
        String[] loge = AnalyzeText(log,"\n");
        String[] model;
        String cache = "";
        Log.d("Suiteki.test","Log:" + log);
        for(int a = 0;loge.length > a;a++){
            if(Lookfor(loge[a],key,0) != -1){
                cache = getTheTexto(loge[a],"model='","', name=") + "\n" + cache;
                Log.d("Suiteki.test",getTheTexto(loge[a],"model='","', name="));
            }
        }
        model = AnalyzeText(cache,"\n");
        model = deleteText(model);
        Log.d("Suiteki.test","Model:" + model[0]);
        return model[0];
    }

    public static String[] getAuthKeyList(String log){
        String[] loge;
        String[] key = null;
        String cache = "";
        loge = AnalyzeText(log,"\n");
        for(int a = 0;loge.length > a;a++){
            if(Lookfor(loge[a],"authKey",0) != -1){
                cache = getAuthKey(loge[a]) + "\n" + cache;
            }
        }
        key = AnalyzeText(cache,"\n");
        key = deleteText(key);
        return key;
    }

    public static String getModelName(String model){
        String name;
        switch(model){
            case "hmpace.bracelet.v5" :
                name = "小米手环5";
                break;
            case "hmpace.bracelet.v5h":
                name = "小米手环5 NFC版";
                break;
            case "hmpace.motion.v6":
                name = "小米手环6";
                break;
            case "hmpace.motion.v6nfc":
                name = "小米手环6 NFC版";
                break;
            case "hqbd3.watch.l67":
                name = "小米手环7 Pro";
                break;
            case "hmpace.watch.v7":
                name = "小米手环7";
                break;
            case "hmpace.watch.v7nfc":
                name = "小米手环7 NFC版";
                break;
            default :
                name = "notFound";
        }
        return name;
    }

    private static int Lookfor(String str1, String str2, int start) {
        return start >= 0 && start <= str1.length() && !"".equals(str1) && !"".equals(str2) ? str1.indexOf(str2, start) : -1;
    }

    private static String[] AnalyzeText(String str, String separator) {
        if (!"".equals(separator) && !"".equals(str)) {
            if (separator.equals("\n")) {
                str = exchangeText(str, "\r", "");
            }
            return getTextRight(str, getTextLength(separator)).equals(separator) ? getTheText(separator + str, separator, separator) : getTheText(separator + str + separator, separator, separator);
        } else {
            return new String[0];
        }
    }

    private static String[]  deleteText(String[] key){
        List list = new ArrayList();
        for(int i=0;i<key.length;i++){
            if(!list.contains(key[i])){
                list.add(key[i]);
            }
        }
        String[] newArr = AnalyzeText(getTheTexto(list.toString(),"[","]"),", ");
        Log.d("Suiteki.test",list.toString());
        return newArr;
    }

    private static String[] getTheText(String str, String left, String right) {
        return !"".equals(str) && !"".equals(left) && !"".equals(right) ? regexMatch(str, "(?<=\\Q" + left + "\\E).*?(?=\\Q" + right + "\\E)") : new String[0];
    }

    private static String getTheTexto(String str, String left, String right) {
        String[] temp = getTheText(str, left, right);
        return temp.length > 0 ? temp[0] : "";
    }

    private static String getTextRight(String str, int len) {
        if (!"".equals(str) && len > 0) {
            if (len > str.length()) {
                return str;
            } else {
                int start = str.length() - len;
                return str.substring(start, str.length());
            }
        } else {
            return "";
        }
    }

    private static String exchangeText(String str, String find, String replace) {
        if (!"".equals(find) && !"".equals(str)) {
            find = "\\Q" + find + "\\E";
            return str.replaceAll(find, replace);
        } else {
            return "";
        }
    }

    private static int getTextLength(String str) {
        return str.length();
    }

    private static String[] regexMatch(String text, String statement) {
        Pattern pn = Pattern.compile(statement, 40);
        Matcher mr = pn.matcher(text);
        ArrayList list = new ArrayList();
        while(mr.find()) {
            list.add(mr.group());
        }
        String[] strings = new String[list.size()];
        return (String[])list.toArray(strings);
    }

    private static Object toObject(String var1) {
        try {
            JSONObject var2 = new JSONObject(var1);
            return var2;
        } catch (JSONException var3) {
            return null;
        }
    }

    private static Object getObject(Object var1, String var2) {
        JSONObject var3 = (JSONObject)var1;
        if (var3 == null) {
            return null;
        } else {
            try {
                JSONObject var4 = var3.getJSONObject(var2);
                return var4;
            } catch (JSONException var5) {
                return null;
            }
        }
    }

    private static String getObjectText(Object var1, String var2) {
        JSONObject var3 = (JSONObject)var1;
        if (var3 == null) {
            return "";
        } else {
            try {
                String var4 = var3.getString(var2);
                return var4;
            } catch (JSONException var5) {
                return "";
            }
        }
    }

    private static Object getArray(Object var1, String var2) {
        JSONObject var3 = (JSONObject)var1;
        if (var3 == null) {
            return null;
        } else {
            try {
                JSONArray var4 = var3.getJSONArray(var2);
                return var4;
            } catch (JSONException var5) {
                return null;
            }
        }
    }

    private static int getArrayLength(Object var1) {
        JSONArray var2 = (JSONArray)var1;
        return var2 == null ? 0 : var2.length();
    }

    private static Object getArrayObject(Object var1, int var2) {
        JSONArray var3 = (JSONArray)var1;
        if (var3 == null) {
            return null;
        } else {
            try {
                JSONObject var4 = var3.getJSONObject(var2);
                return var4;
            } catch (JSONException var5) {
                return null;
            }
        }
    }
}
