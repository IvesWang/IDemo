可以动态地生成主activity上各个模块的入口item

这是运行期版本的module
但目前编译期版本也需要使用到部分ui类，直接在这module上面改了，并注释掉了部分不需要的类

原理：
通过在显示第一个页面时，就通过调用IDemoGenerator类的getModuleClass()->scanModuleClass()->阿里的ClassUtil扫描并缓存下来所有的添加了idemo注解的类信息
当执行点击item事件时，根据item带的从注解解析而来的实体信息进行跳转或执行方法