package cc.ives.aegdemo;

import android.widget.Toast;

import cc.ives.aeg.annotation.Entry;
import cc.ives.aeg.annotation.EntryItem;

/**
 * @author wangziguang
 * @date 2020/7/1
 * @description 由方法添加注解产生item的类
 */
@Entry(indexTime = 20070101, desc = "多个测试方法的类")
public class MethodClass {

    @EntryItem
    public static void test0(){
        Toast.makeText(MyApp.getContext(), "入口的click方法", Toast.LENGTH_SHORT).show();
    }
    @EntryItem
    public static void test00(){
        Toast.makeText(MyApp.getContext(), "入口的click00方法", Toast.LENGTH_SHORT).show();
    }

    @EntryItem(itemName = "第一个方法")
    public void test1(){
        Toast.makeText(MyApp.getContext(), "第一个方法", Toast.LENGTH_SHORT).show();
    }

    @EntryItem(itemName = "第二个方法")
    public void test2(){
        Toast.makeText(MyApp.getContext(), "第二个方法", Toast.LENGTH_SHORT).show();
    }

    @EntryItem(itemName = "第三个方法")
    public void test3(){
        Toast.makeText(MyApp.getContext(), "第三个方法", Toast.LENGTH_SHORT).show();
    }
}
