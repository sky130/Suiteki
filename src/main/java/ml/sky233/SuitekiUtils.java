package ml.sky233;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SuitekiUtils {

    public static final String MI_HEALTH_PATH = "/storage/emulated/0/Android/data/com.mi.health/files/log/XiaomiFit.device.log";
    public static final String MI_WEARABLE_PATH = "/storage/emulated/0/Android/data/com.xiaomi.wearable/files/log/Wearable.log";

    public static Uri changeToUri(String path) {
        path = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
        //d("PathToUri"+"content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A" + path);
        return Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A" + path);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startFor(Activity activity,int request_code) {
        Uri uri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
        DocumentFile documentFile = DocumentFile.fromTreeUri(activity, uri);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        assert documentFile != null;
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, documentFile.getUri());
        activity.startActivityForResult(intent, request_code);//开始授权
    }

    public static String getLog(Context context, String path) {
        String res = "";
        InputStream inputStream = null;
        try {
            Uri uri = changeToUri(path);
            inputStream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fin = (FileInputStream) inputStream;
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, 0, length, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
