package cc.ives.idemo;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.sun.source.util.Trees;

import net.ltgt.gradle.incap.IncrementalAnnotationProcessor;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;

import cc.ives.idemo.annotation.IDAction;
import cc.ives.idemo.annotation.IDItemInfo;
import cc.ives.idemo.annotation.IDModule;
import cc.ives.idemo.util.CodeWriter;

@AutoService(Processor.class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.DYNAMIC)
public class IDemoProcessor extends AbstractProcessor {
    // 官方中文文档 https://www.apiref.com/java11-zh/java.compiler/javax/lang/model/element/Element.html

    private HashMap<String, LinkedList<IDItemInfo>> classInfoCache = new HashMap<>();// key:preEntryClz的全类名, value:该类下的第一级子类集合（按顺序排好，可直接引入到最终生成的map）。只缓存一级父子关系

    private Trees trees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        try {
            trees = Trees.instance(processingEnv);
        } catch (IllegalArgumentException ignored) {
            try {
                // Get original ProcessingEnvironment from Gradle-wrapped one or KAPT-wrapped one.
                for (Field field : processingEnv.getClass().getDeclaredFields()) {
                    if (field.getName().equals("delegate") || field.getName().equals("processingEnv")) {
                        field.setAccessible(true);
                        ProcessingEnvironment javacEnv = (ProcessingEnvironment) field.get(processingEnv);
                        trees = Trees.instance(javacEnv);
                        break;
                    }
                }
            } catch (Throwable ignored2) {
            }
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 拦截IDAction的类，加入缓存
        // 创建新类把缓存信息作为硬编码写入

        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "\n\nIDemoProcessor process start");
        messager.printMessage(Diagnostic.Kind.NOTE, "\nroundEnvironment class:"+roundEnvironment.getClass().getCanonicalName());
        messager.printMessage(Diagnostic.Kind.NOTE, "\nset长度：" + set.size());// 找到的注解类型长度，不是添加了注解的类的集合。是getSupportedAnnotationTypes()的一个子集

        if (set.isEmpty()){
            messager.printMessage(Diagnostic.Kind.NOTE, "\nset empty,ignore.");
            return false;
        }

        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(IDModule.class);
        messager.printMessage(Diagnostic.Kind.NOTE, "\nIDModule集合长度：" + elementSet.size());

        messager.printMessage(Diagnostic.Kind.NOTE, "\n=================");
        for (Element element : elementSet) {
            messager.printMessage(Diagnostic.Kind.NOTE, "\n\n元素名：" + element.getSimpleName());
            if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.INTERFACE){
                IDModule classAnnotation = element.getAnnotation(IDModule.class);
                if (classAnnotation != null){
                    messager.printMessage(Diagnostic.Kind.NOTE, "\nIDModule desc是:" + classAnnotation.desc());
                    messager.printMessage(Diagnostic.Kind.NOTE, "\nIDModule indexTime是:" + classAnnotation.indexTime());

                    // 加入类信息映射关系到内存
                    String preModuleName;
                    try {
                        preModuleName = classAnnotation.preModule().getCanonicalName();
                    } catch (MirroredTypeException e){
                        preModuleName = e.getTypeMirror().toString();
                        messager.printMessage(Diagnostic.Kind.NOTE, "\nIDModule preModule是:" + e.getTypeMirror().toString());
                    }
                    LinkedList<IDItemInfo> itemListOfParent = classInfoCache.get(preModuleName);// 找出与之同父级的二级类列表
                    if(itemListOfParent == null){
                        itemListOfParent = new LinkedList<>();
                        classInfoCache.put(preModuleName, itemListOfParent);
                    }
                    // 将当前类作为item加入到上级列表
                    IDItemInfo itemInfo = new IDItemInfo();
                    itemInfo.setClassName(element.asType().toString());
                    itemInfo.setItemName((classAnnotation.desc() == null || "".equals(classAnnotation.desc())) ? element.getSimpleName().toString() : classAnnotation.desc());
                    itemInfo.setIndexTime(classAnnotation.indexTime());
                    itemInfo.setFunctionName("");//对于上级，这只是一个跳转项，不存在注解方法
                    itemListOfParent.add(itemInfo);

                    // 将当前类的方法作为item，构建至当前类的下级列表
                    LinkedList<IDItemInfo> itemListOfCurrent = classInfoCache.get(itemInfo.getClassName());// 找出当前类的二级类列表
                    if(itemListOfCurrent == null){
                        itemListOfCurrent = new LinkedList<>();
                        classInfoCache.put(itemInfo.getClassName(), itemListOfCurrent);
                    }
                    // 该类下的方法
                    List<? extends Element> functionElements = element.getEnclosedElements();
                    IDItemInfo childItemInfo;
                    for (Element functionElement : functionElements) {// 方法和构造器列表
                        messager.printMessage(Diagnostic.Kind.NOTE, "\nfunctionElement name：" + functionElement.getSimpleName());
                        IDAction funAnnotation = functionElement.getAnnotation(IDAction.class);
                        if(funAnnotation != null){
                            messager.printMessage(Diagnostic.Kind.NOTE, "\n注解item值是:" + funAnnotation.itemName());
                            childItemInfo = new IDItemInfo();
                            childItemInfo.setItemName((funAnnotation.itemName()==null||"".equals(funAnnotation.itemName()))?functionElement.getSimpleName().toString():funAnnotation.itemName());
                            childItemInfo.setClassName(element.asType().toString());
                            childItemInfo.setIndexTime(classAnnotation.indexTime());
                            childItemInfo.setFunctionName(functionElement.getSimpleName().toString());//跳转方法
                            itemListOfCurrent.add(childItemInfo);
                        }
                    }

                }

                Name qualifiedName = ((TypeElement)element).getQualifiedName();
                messager.printMessage(Diagnostic.Kind.NOTE, "\nqualifiedName：" + qualifiedName.toString());
                messager.printMessage(Diagnostic.Kind.NOTE, "\nname type：" + qualifiedName.getClass().getCanonicalName());

                List<? extends Element> classEnclosedElements = element.getEnclosedElements();
                for (Element enclosedElement : classEnclosedElements) {// 方法和构造器列表
                    messager.printMessage(Diagnostic.Kind.NOTE, "\nclassEnclosedElements name：" + enclosedElement.getSimpleName());
                    IDAction funAnnotation = enclosedElement.getAnnotation(IDAction.class);
                    if(funAnnotation == null){
                        messager.printMessage(Diagnostic.Kind.NOTE, "\n注解是空？");
                    } else {
                        messager.printMessage(Diagnostic.Kind.NOTE, "\n注解item值是:" + funAnnotation.itemName());
                    }

                    messager.printMessage(Diagnostic.Kind.NOTE, "\n#################");
                }

                // 取方法
                // 这种方式是直接从RoundEnvironment取，取出来的是全部注解的方法，没有区分类。
                messager.printMessage(Diagnostic.Kind.NOTE, "\nelement type：" + element.getClass().getCanonicalName());
                Set<? extends Element> functionElements = roundEnvironment.getElementsAnnotatedWith(IDAction.class);
                messager.printMessage(Diagnostic.Kind.NOTE, "\n方法长度：" + functionElements.size());
                messager.printMessage(Diagnostic.Kind.NOTE, "\n-----------------");
                for (Element functionElement : functionElements) {
                    List<? extends Element> enclosedElements = functionElement.getEnclosedElements();

                    messager.printMessage(Diagnostic.Kind.NOTE, "\nfunction Type：" + functionElement.getClass().getCanonicalName());
                    messager.printMessage(Diagnostic.Kind.NOTE, "\ngetEnclosingElement getSimpleName：" + functionElement.getEnclosingElement().getSimpleName());
                    messager.printMessage(Diagnostic.Kind.NOTE, "\n— — — — — — — — — —");
                    for (Element enclosedElement : enclosedElements) {
                        messager.printMessage(Diagnostic.Kind.NOTE, "\nenclosedElement name：" + enclosedElement.getSimpleName());
                    }
                }

            }else {
                messager.printMessage(Diagnostic.Kind.NOTE, "\n这个注解应当只在类或接口上添加");
            }
//            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
//            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
//            messager.printMessage(Diagnostic.Kind.WARNING, "元素名：" + element.getSimpleName());
        }
        // <<< 内存构建完毕
//            writePoetCode();
//                IDemoGenerator2.classInfoCache = classInfoCache;

        //javapoet结合filer，可方便地找到地方来创建类
        CodeWriter.blewJava(classInfoCache, this.processingEnv.getFiler());

        //javassist不可行，找不到类来编辑
//                try {
//                    InjectUtil.injectInitAnnotationInfoSet(classInfoCache);
//                } catch (NotFoundException e) {
//                    e.printStackTrace();
//                } catch (CannotCompileException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
        return true;//true-只被当前processor处理 false-后续其它处理器可继续处理
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> typeSets = new HashSet<>();
        typeSets.add(IDAction.class.getCanonicalName());
        typeSets.add(IDModule.class.getCanonicalName());
        return typeSets;
    }

    @Override
    public Set<String> getSupportedOptions() {
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (trees != null) {
            builder.add(IncrementalAnnotationProcessorType.ISOLATING.getProcessorOption());
        }
        return builder.build();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    private void writeClass(){
        // 生成一个作用相当于IDemoGenerator的类
    }
}