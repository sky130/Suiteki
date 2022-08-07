package ml.sky233;

import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Suiteki {



    public static String getAuthKey(String log){
        String key = getTextRight(getTheTexto(log,"authKey",","),32);
        Log.d("Suiteki.test","Authkey:" + key);
        return key;
    }

    public static String getModel(String log){
        String model = getTheTexto(log,"model='","', name=");
        Log.d("Suiteki.test","Model:" + model);
        return model;
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

    public static String[] getModelList(String log) {
        String[] loge;
        String[] model = null;
        loge = AnalyzeText(log,"\n");
        String cache = "";
        for(int a = 0;loge.length > a;a++){
            if(Lookfor(loge[a],"model='",0) != -1){
                cache = getModel(loge[a]) + "\n" + cache;
            }
        }
        //Log.d("Suiteki.test","Model:true");
        model = AnalyzeText(cache,"\n");
        model = deleteText(model);
        return model;
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
        //子文本替换
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
}
