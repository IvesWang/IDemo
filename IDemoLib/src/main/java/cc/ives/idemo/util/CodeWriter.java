package cc.ives.idemo.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

import cc.ives.idemo.annotation.IDItemInfo;

/**
 * @author wangziguang
 * @date 2020/12/28
 * @description
 */
public class CodeWriter {

    // 构建cc.ives.idemo.util.IDemoGenerator2类
    public static void blewJava(HashMap<String, LinkedList<IDItemInfo>> annotationInfo, Filer filer){

        // 编写静态代码块，item信息初始化的代码
        StringBuilder sb = new StringBuilder();
        Iterator<String> keyIt = annotationInfo.keySet().iterator();
        String key;
        LinkedList<IDItemInfo> inputItemList;
        int listIndex = 0;
        int itemIndex = 0;
        while (keyIt.hasNext()){
            key = keyIt.next();
            sb.append("LinkedList<IDItemInfo> itemList").append(listIndex).append(" = new LinkedList<>();").append("\n");//LinkedList<IDItemInfo> itemList2 = new LinkedList<>();
            inputItemList = annotationInfo.get(key);
            for (IDItemInfo itemInfo1 : inputItemList) {
                sb.append("IDItemInfo itemInfo").append(itemIndex).append(" = new IDItemInfo();").append("\n");
                sb.append("itemInfo").append(itemIndex).append(".setFunctionName(\"").append(itemInfo1.getFunctionName()).append("\");").append("\n");
                sb.append("itemInfo").append(itemIndex).append(".setIndexTime(").append(itemInfo1.getIndexTime()).append(");").append("\n");
                sb.append("itemInfo").append(itemIndex).append(".setClassName(\"").append(itemInfo1.getClassName()).append("\");").append("\n");
                sb.append("itemInfo").append(itemIndex).append(".setItemName(\"").append(itemInfo1.getItemName()).append("\");").append("\n");
                sb.append("itemList").append(listIndex).append(".add(itemInfo").append(itemIndex).append(");").append("\n");
                itemIndex++;
            }
            sb.append("classInfoCache.put(\"").append(key).append("\", itemList").append(listIndex).append(");").append("\n");
            listIndex++;
        }

        CodeBlock.Builder staticBlockBuilder = CodeBlock.builder();
        staticBlockBuilder.addStatement(sb.toString());

//        String preModuleName = preModule == null? Object.class.getCanonicalName():preModule.getCanonicalName();
//        List<IDItemInfo> list = classInfoCache.get(preModuleName);
//        if (list == null){
//            list = new ArrayList<>();
//        }
//        return list;

        // 增加一个静态方法
        MethodSpec getChildClassInfoMethod = MethodSpec.methodBuilder("getChildClassInfo")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Class.class, "preModule")
                .returns(ParameterizedTypeName.get(List.class, IDItemInfo.class))
                .addStatement("$T preModuleName = preModule == null? $T.class.getCanonicalName():preModule.getCanonicalName();", String.class, Object.class)
                .addStatement("$T<$T> list = classInfoCache.get(preModuleName);", List.class, IDItemInfo.class)
                .addStatement("if (list == null){")
                .addStatement("list = new $T<>();", ArrayList.class)
                .addStatement("}")
                .addStatement("return list")
                .build();


        TypeName mapType = ParameterizedTypeName.get(
                ClassName.get(HashMap.class),
                TypeName.get(String.class),
                ParameterizedTypeName.get(LinkedList.class, IDItemInfo.class));// HashMap<String, LinkedList<IDItemInfo>>
        FieldSpec cacheFieldSpec = FieldSpec.builder(mapType, "classInfoCache", Modifier.PRIVATE, Modifier.STATIC)
                .initializer("new $T<>()", HashMap.class)
                .build();

        TypeSpec iDemoGenerator2Class = TypeSpec.classBuilder("IDemoGenerator2")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(cacheFieldSpec)
                .addStaticBlock(staticBlockBuilder.build())
                .addMethod(getChildClassInfoMethod)
                .build();

        JavaFile javaFile = JavaFile.builder("cc.ives.idemo.util", iDemoGenerator2Class)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
