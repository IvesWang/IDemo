package cc.ives.idemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangziguang
 * @date 2020/5/23
 * @description 标记为入口activity，会放在mainactivity中
 * todo 当前是运行时方式，考虑是否可以用编译期的方式来做
 * 编译期好处：不需要遍历所有activity，可以被动过滤到添加了注解的类，后期可扩展性也更强，不一定非得activity。但编译时间会加长
 * 运行期：好坏除了更编译期反过来外，会增加运行时内存和执行时间（可忽略该时间）。
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface IDModule {
    int indexTime();// 当前层级的排序索引，使用时间格式，比如：20052301表示2020年5月23日第1个增加的入口类
    String desc() default "";// 描述名，用于显示，空则用类名
    Class preModule() default Object.class;// 上一个入口的类
}
