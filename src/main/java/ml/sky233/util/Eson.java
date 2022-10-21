package ml.sky233.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Eson {

    //叒一个对你们来说没什么用的函数
    public static Object toObject(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            return obj;
        } catch (JSONException e) {
            return null;
        }
    }

    //叕一个对你们来说没什么用的函数
    public static Object getObject(Object str1, String str2) {
        JSONObject obj = (JSONObject) str1;
        if (obj == null) {
            return null;
        } else {
            try {
                return obj.getJSONObject(str2);
            } catch (JSONException e) {
                return null;
            }
        }
    }

    //叕又一个对你们来说没什么用的函数
    public static String getObjectText(Object obj, String str) {
        JSONObject object = (JSONObject) obj;
        if (object == null) {
            return "";
        } else {
            try {
                return object.getString(str);
            } catch (JSONException e) {
                return "";
            }
        }
    }

    //一个对你们来说没什么用的函数
    public static Object getArray(Object obj, String str) {
        JSONObject object = (JSONObject) obj;
        if (object == null) {
            return null;
        } else {
            try {
                return object.getJSONArray(str);
            } catch (JSONException e) {
                return null;
            }
        }
    }

    //一个对你们来说没什么用的函数
    public static int getArrayLength(Object obj) {
        JSONArray object = (JSONArray) obj;
        return object == null ? 0 : object.length();
    }

    //一个对你们来说没什么用的函数
    public static Object getArrayObject(Object obj, int i) {
        JSONArray object = (JSONArray) obj;
        if (object == null) {
            return null;
        } else {
            try {
                return object.getJSONObject(i);
            } catch (JSONException e) {
                return null;
            }
        }
    }
}
