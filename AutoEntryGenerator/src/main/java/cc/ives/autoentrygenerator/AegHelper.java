package cc.ives.autoentrygenerator;

import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.ives.common.AgentApp;
import com.ives.common.JLog;
import com.ives.common.util.ClassUtil;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import cc.ives.autoentrygenerator.annotation.Entry;
import cc.ives.autoentrygenerator.annotation.EntryOnClick;

/**
 * @author wangziguang
 * @date 2020/5/24 0024
 * @description
 */
public class AegHelper {

    /**
     * 查找指定类的入口方法
     * @param entryClass
     * @return
     */
    private static Method findEntryMethod(Class entryClass){
        Method[] methods = entryClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(EntryOnClick.class)){
                return method;
            }
        }
        return null;
    }

    /**
     * 实例化或者静态调用该类的入口方法
     * @param entryClass
     */
    public static void invokeEntryMethod(Class entryClass){
        Method entryMethod = findEntryMethod(entryClass);
        if (entryMethod == null){
            JLog.w("AegHelper", "invokeEntryMethod() you have no declared any entry method with annotation EntryOnClick");
            return;
        }
        entryMethod.setAccessible(true);

        if (Modifier.isStatic(entryMethod.getModifiers())){

            try {
                entryMethod.invoke(null);// 必须是无参方法
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }else {

            try {
                Object obj = entryClass.newInstance();// 必须有非私有的无参构造器
                entryMethod.invoke(obj);// 必须是无参方法
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取该包名下的所有类
     * @return
     */
    public static Set<String> getAllClassUnderPackage(){
        try {
            return ClassUtil.getFileNameByPackageName(AgentApp.getContext(), AgentApp.getContext().getPackageName());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }
}
