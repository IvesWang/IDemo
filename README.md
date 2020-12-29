### 场景介绍
我们在做安卓开发时是不是经常遇到这样一种场景？经常为了写个demo就创建一个新的工程，本地目录总是一堆工程。有经验的会把所有demo的代码都集中在一个工程，分好不同的模块进行管理。不过这样还有个问题，就是经常为了写一点点测试代码而需要新建一个新的activity和相应的布局文件，然后又要注册manifest，如果有多个页面跳转还得有多个activity，但实际你想写的测试代码可能只有一点点，而为了这点点代码就得创建一堆activity。比如想跑一段正则表达式，想调用一下系统的api获取手机长宽信息，想试试Gson的某个使用方式等等。这不仅让工程里的代码不够简洁，而且增加了写代码的工作量。IDemo框架就是为了解决这类问题而生，让你的demo代码变得简洁、专注于你想写的业务代码。让你写代码变得更加轻松，爱上写demo.
### 集成步骤
###### 1. 添加依赖
在根目录的build.gralde文件添加以下代码：
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

添加以下依赖：
```
dependencies {
	implementation 'com.github.IvesWang:IDemo:0.2-alpha'
}
```
[![](https://jitpack.io/v/IvesWang/IDemo.svg)](https://jitpack.io/#IvesWang/IDemo)


###### 2. 初始化IDemo
在任意你想被"托管"的activity中加入以下代码</br>
```
IDemoPage.init(getSupportFragmentManager());
```
这样你这个activity的界面就会被IDemo的一个fragment替换掉，后边的界面显示就开始使用IDemo的注解逻辑了。


###### 3. 引入注解
注解举例：</br>

```
@IDModule(indexTime = 20070101, desc = "多个测试方法的类")
public class MethodClass {
.....
}
```
使用 **@IDModule**给类添加注解，如果该类是activity，点击则会启动该activity。如果是普通类，会找类里的 **@IDAction**注解方法作为下一级页面的点击事件。


在普通类里添加想调试的代码片段：
```
@IDAction(itemName = "任意调试代码")
public void test3(){
    Toast.makeText(MyApp.getContext(), "第三个方法", Toast.LENGTH_SHORT).show();
}

@IDAction(itemName = "打开一个webview")
public void openWebView(){
    IDemoPage.openWebView("https://www.jianshu.com/p/14edcb444c51");
}
```

**注解说明：**</br>
> @IDModule</br>
indexTime 用于对列表的item进行排序的索引，一般按照编码当时的时间来填比较方便。格式为yyMMdd + 两位数字，后面两个数字表示该类为该天添加的第几个类，这样会比较便于记忆，也不会造成太多的编号。</br>
desc 用于item显示的名称</br>
preModule 上一级点击事件的类class对象，表示当前类的事件列表会作为preModule类的子列表。不填则当前类会作为第一个列表里的item。</br>
@IDAction 作为@IDModule入口事件后的事件列表项</br>
itemName 点击事件列表中该item显示的名称

上两张效果图：
![image](https://upload-images.jianshu.io/upload_images/2698088-95992e807ca8efd1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/2698088-2d19ada7e5b6a7ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
