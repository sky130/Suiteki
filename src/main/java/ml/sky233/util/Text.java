package ml.sky233.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text {
    //查找文本是否存在,如不存在则返回 -1
    public static int Lookfor(String str1, String str2, int start) {
        return start >= 0 && start <= str1.length() && !"".equals(str1) && !"".equals(str2) ? str1.indexOf(str2, start) : -1;
    }

    //将文本以特定字符为分隔转换为数组
    public static String[] AnalyzeText(String str, String separator) {
        if (!"".equals(separator) && !"".equals(str)) {
            if (separator.equals("\n")) {
                str = exchangeText(str, "\r", "");
            }
            return getTextRight(str, separator.length()).equals(separator) ? getTheText(separator + str, separator, separator) : getTheText(separator + str + separator, separator, separator);
        } else {
            return new String[0];
        }
    }

    //删除重复的文本
    public static String[] deleteText(String[] key) {
        List list = new ArrayList();
        for (int i = 0; i < key.length; i++) {
            if (!list.contains(key[i])) {
                list.add(key[i]);
            }
        }
        String[] newArr = AnalyzeText(getTheTexto(list.toString(), "[", "]"), ", ");
        Log.d("Suiteki.test", list.toString());
        return newArr;
    }

    //查找文本,选择中所有str1和str2中间的文本
    public static String[] getTheText(String str, String left, String right) {
        return !"".equals(str) && !"".equals(left) && !"".equals(right) ? regexMatch(str, "(?<=\\Q" + left + "\\E).*?(?=\\Q" + right + "\\E)") : new String[0];
    }

    //查找文本,选择str1和str2中间的文本,只选择第一个
    public static String getTheTexto(String str, String left, String right) {
        String[] temp = getTheText(str, left, right);
        return temp.length > 0 ? temp[0] : "";
    }

    //一个对你们来说没什么用的函数
    public static String getTextRight(String str, int len) {
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

    //又一个对你们来说没什么用的函数
    public static String exchangeText(String str, String find, String replace) {
        if (!"".equals(find) && !"".equals(str)) {
            find = "\\Q" + find + "\\E";
            return str.replaceAll(find, replace);
        } else {
            return "";
        }
    }


    //又又一个对你们来说没什么用的函数
    public static String[] regexMatch(String text, String statement) {
        Pattern pn = Pattern.compile(statement, 40);
        Matcher mr = pn.matcher(text);
        ArrayList list = new ArrayList();
        while (mr.find()) {
            list.add(mr.group());
        }
        String[] strings = new String[list.size()];
        return (String[]) list.toArray(strings);
    }

    //随机生成设备码
    public static String createDeviceCode() {
        return "02:00:00:" + randomChar() + randomChar() + ":" + randomChar() + randomChar() + ":" + randomChar() + randomChar();
    }

    private static char randomChar() {
        String default_code = "0123456789abcdef";
        Random random = new Random();
        return default_code.charAt(random.nextInt(default_code.length()));
    }
}
