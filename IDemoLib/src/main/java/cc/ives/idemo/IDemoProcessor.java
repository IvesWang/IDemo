package cc.ives.idemo;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        messager.printMessage(Diagnostic.Kind.NOTE, "IDemoProcessor process start");
        messager.printMessage(Diagnostic.Kind.NOTE, "roundEnvironment class:"+roundEnvironment.getClass().getCanonicalName());
        messager.printMessage(Diagnostic.Kind.NOTE, "set长度：" + set.size());// 找到的注解类型长度，不是添加了注解的类的集合。是getSupportedAnnotationTypes()的一个子集

        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(IDModule.class);
        messager.printMessage(Diagnostic.Kind.NOTE, "IDModule集合长度：" + elementSet.size());

        messager.printMessage(Diagnostic.Kind.NOTE, "=================");
        for (Element element : elementSet) {
            messager.printMessage(Diagnostic.Kind.NOTE, "元素名：" + element.getSimpleName());
            if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.INTERFACE){
                Name qualifiedName = ((TypeElement)element).getQualifiedName();
                messager.printMessage(Diagnostic.Kind.NOTE, "qualifiedName：" + qualifiedName.toString());
                messager.printMessage(Diagnostic.Kind.NOTE, "name type：" + qualifiedName.getClass().getCanonicalName());

                List<? extends Element> classEnclosedElements = element.getEnclosedElements();
                for (Element enclosedElement : classEnclosedElements) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "classEnclosedElements name：" + enclosedElement.getSimpleName());
                }

                // 取方法
                messager.printMessage(Diagnostic.Kind.NOTE, "element type：" + element.getClass().getCanonicalName());
                Set<? extends Element> functionElements = roundEnvironment.getElementsAnnotatedWith(IDAction.class);
                messager.printMessage(Diagnostic.Kind.NOTE, "方法长度：" + functionElements.size());
                messager.printMessage(Diagnostic.Kind.NOTE, "-----------------");
                for (Element functionElement : functionElements) {
                    List<? extends Element> enclosedElements = functionElement.getEnclosedElements();

                    messager.printMessage(Diagnostic.Kind.NOTE, "function Type：" + functionElement.getClass().getCanonicalName());
                    messager.printMessage(Diagnostic.Kind.NOTE, "getEnclosingElement getSimpleName：" + functionElement.getEnclosingElement().getSimpleName());
                    messager.printMessage(Diagnostic.Kind.NOTE, "— — — — — — — — — —");
                    for (Element enclosedElement : enclosedElements) {
                        messager.printMessage(Diagnostic.Kind.NOTE, "enclosedElement name：" + enclosedElement.getSimpleName());
                    }
                }

            }else {
                messager.printMessage(Diagnostic.Kind.NOTE, "这个注解应当只在类或接口上添加");
            }
//            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
//            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
//            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
//            writePoetCode();
        }
        return false;
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