package cc.ives.aegdemo;

import android.widget.Toast;

import cc.ives.aeg.annotation.Entry;
import cc.ives.aeg.annotation.EntryOnClick;

/**
 * @author wangziguang
 * @date 2020/7/1
 * @description 由方法添加注解产生item的类
 */
@Entry(indexTime = 20070101, desc = "多个测试方法的类")
public class MethodClass {

    @EntryOnClick
    public static void test0(){
        Toast.makeText(MyApp.getContext(), "入口的click方法", Toast.LENGTH_SHORT).show();
    }
    @EntryOnClick
    public static void test00(){
        Toast.makeText(MyApp.getContext(), "入口的click00方法", Toast.LENGTH_SHORT).show();
    }

    @EntryOnClick(itemName = "第一个方法")
    public void test1(){
        Toast.makeText(MyApp.getContext(), "第一个方法", Toast.LENGTH_SHORT).show();
    }

    @EntryOnClick(itemName = "第二个方法")
    public void test2(){
        Toast.makeText(MyApp.getContext(), "第二个方法", Toast.LENGTH_SHORT).show();
    }

    @EntryOnClick(itemName = "第三个方法")
    public void test3(){
        Toast.makeText(MyApp.getContext(), "第三个方法", Toast.LENGTH_SHORT).show();
    }
}
