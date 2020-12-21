package cc.ives.idemo;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import cc.ives.idemo.annotation.IDAction;
import cc.ives.idemo.annotation.IDClassInfo;
import cc.ives.idemo.annotation.IDModule;

@AutoService(Processor.class)
public class IDemoProcessor extends AbstractProcessor {

    private HashMap<String, LinkedList<IDClassInfo>> classInfoCache = new HashMap<>();// key:preEntryClz的全类名, value:该类下的第一级子类集合（按顺序排好，可直接引入到最终生成的map）

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 拦截IDAction的类，加入缓存
        // 创建新类把缓存信息作为硬编码写入

        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.WARNING, "IDemoProcessor process start");

        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(IDModule.class);

        for (Element element : elementSet) {
            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
            if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.INTERFACE){
                Name qualifiedName = ((TypeElement)element).getQualifiedName();
                messager.printMessage(Diagnostic.Kind.WARNING, "qualifiedName：" + qualifiedName.toString());
                messager.printMessage(Diagnostic.Kind.WARNING, "name type：" + qualifiedName.getClass().getCanonicalName());

                // 取方法
                messager.printMessage(Diagnostic.Kind.NOTE, "element type：" + element.getClass().getCanonicalName());
                messager.printMessage(Diagnostic.Kind.WARNING, "element type：" + element.getClass().getCanonicalName());
                Set<? extends Element> functionElements = roundEnvironment.getElementsAnnotatedWith((TypeElement) element);
                messager.printMessage(Diagnostic.Kind.WARNING, "方法长度：" + functionElements.size());
                for (Element functionElement : functionElements) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "function Type：" + functionElement.getClass().getCanonicalName());
                }

            }else {
                messager.printMessage(Diagnostic.Kind.WARNING, "这个注解应当只在类或接口上添加");
            }
//            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
//            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
//            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
//            writePoetCode();
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> typeSets = new HashSet<>();
        typeSets.add(IDAction.class.getCanonicalName());
        typeSets.add(IDModule.class.getCanonicalName());
        return typeSets;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}