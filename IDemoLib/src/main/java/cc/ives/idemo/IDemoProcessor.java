package cc.ives.idemo;

import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class IDemoProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 拦截IDAction的类，加入缓存
        // 创建新类把缓存信息作为硬编码写入

        return true;
    }
}