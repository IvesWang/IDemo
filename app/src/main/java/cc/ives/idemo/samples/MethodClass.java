package cc.ives.idemo.samples;

import android.widget.Toast;

import cc.ives.idemo.annotation.IDModule;
import cc.ives.idemo.annotation.IDAction;
import cc.ives.idemo.ui.IDemoPage;

/**
 * @author wangziguang
 * @date 2020/7/1
 * @description 由方法添加注解产生item的类
 */
@IDModule(indexTime = 20070101, desc = "多个测试方法的类")
public class MethodClass {
    private volatile int voltest;

    @IDAction
    public static void test0(){
        Toast.makeText(MyApp.getContext(), "入口的click方法", Toast.LENGTH_SHORT).show();
    }
    @IDAction
    public static void test00(){
        Toast.makeText(MyApp.getContext(), "入口的click00方法", Toast.LENGTH_SHORT).show();
    }

    @IDAction(itemName = "写一个调试方法")
    public void test1(){
        voltest =3;
        Toast.makeText(MyApp.getContext(), "第一个方法" + voltest, Toast.LENGTH_SHORT).show();
    }

    @IDAction(itemName = "再写一个调试方法")
    public void test2(){
        Toast.makeText(MyApp.getContext(), "第二个方法", Toast.LENGTH_SHORT).show();
    }

    @IDAction(itemName = "任意调试代码")
    public void test3(){
        Toast.makeText(MyApp.getContext(), "第三个方法", Toast.LENGTH_SHORT).show();
    }

    @IDAction(itemName = "打开一个webview")
    public void openWebView(){
        IDemoPage.openWebView("https://www.jianshu.com/p/14edcb444c51");
    }
}
