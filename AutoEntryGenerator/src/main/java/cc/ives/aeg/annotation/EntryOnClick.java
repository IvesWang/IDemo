package cc.ives.aeg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cc.ives.aeg.util.AegHelper;

/**
 * @author wangziguang
 * @date 2020/5/24 0024
 * @description 标记该方法为点击入口类时的执行方法
 * todo 需要加入参数检查，目前要求必须是无参方法，对于非静态方法，要求类必须有非私有的无参构造器。见{@link AegHelper#invokeEntryMethod(Class)}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EntryOnClick {
}
