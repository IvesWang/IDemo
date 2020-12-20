package cc.ives.idemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cc.ives.idemo.util.IDemoHelper;

/**
 * @author wangziguang
 * @date 2020/5/24 0024
 * @description 标记该方法为点击入口类时的执行方法
 * todo 需要加入参数检查，目前要求必须是无参方法，对于非静态方法，要求类必须有非私有的无参构造器。见{@link IDemoHelper#invokeModuleMethod(Class)}
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface IDAction {

    // 1.如果不设的话，将会作为没有子页面的list item的点击，有就会当做有子页面的activity。
    // 2.一旦类里有一个方法有itemName，其它的至多只有一个没有itemName。否则无法准确定位相应的方法
    String itemName() default "";
}
