package cc.ives.aeg.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * 日志打印的公共类
 * Created by 王子 on 2016/6/18.
 */

public class ESLog {
    private static final String TAG = ESLog.class.getSimpleName();
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;
    private static final int LOG_LEVEL = VERBOSE;

    public static void d(String tag,String str){
        if(LOG_LEVEL<=DEBUG){
            Log.d(tag,str);
        }
    }
    public static void dlog(String str){
        d(TAG, str);
    }

    /**
     * 可以指定模块关键字，用于过滤日志
     * @param tag
     * @param moduleKeys 模块关键字，可多个组合
     * @param str
     */
    public static void i(String tag, String moduleKeys, String str){
        if(LOG_LEVEL<=INFO) {
            int maxLength = 2000;
            if(str!=null && str.length()>maxLength){
                int start = 0;
                String subString;
                while (start<str.length()){
                    if(start + maxLength >= str.length()){
                        subString = str.substring(start);
                    }else {
                        subString = str.substring(start, start + maxLength);
                    }

                    if(!TextUtils.isEmpty(moduleKeys)) {
                        subString = moduleKeys + "||||" + subString;
                    }

                    Log.i(tag, subString);
                    start += maxLength;
                }
            }else {
                if(TextUtils.isEmpty(moduleKeys)){
                    Log.i(tag, str);
                }else {
                    moduleKeys = moduleKeys + "||||";
                    Log.i(tag, moduleKeys + str);
                }
            }
        }
    }
    public static void i(String tag, String str){
        i(tag, null, str);
    }
    public static void ilog(String str){
        i(TAG, str);
    }
    public static void w(String tag, String moduleKeys, String str){
        if(LOG_LEVEL<=WARN) {
            if(TextUtils.isEmpty(moduleKeys)){
                Log.w(tag, str);
            }else {
                moduleKeys = moduleKeys + "||||";
                Log.w(tag, moduleKeys + str);
            }
        }
    }
    public static void w(String tag, String str){
        w(tag, null, str);
    }
    public static void wlog(String str){
        w(TAG, str);
    }
    public static void e(String tag, String str){
        if(LOG_LEVEL<=ERROR) {
            Log.e(tag, str);
        }
    }
    public static void elog(String str){
        e(TAG, str);
    }
    public static void e(String tag, Exception e) {
        if (null != e) {
            ESLog.e(tag, e.getClass().getCanonicalName() + " follow stack is:");
            StackTraceElement[] eles = e.getStackTrace();
            if (null != eles) {
                for (StackTraceElement stackTraceElement : eles) {

                    ESLog.e(tag, stackTraceElement.toString());

                }
            }
        }

    }
}
