package cc.ives.idemo.util;

import java.util.HashMap;
import java.util.LinkedList;

import cc.ives.idemo.annotation.IDItemInfo;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

/**
 * 代码插入工具
 */
public class InjectUtil {

    public static void initAnnotationInfoSet(HashMap<String, LinkedList<IDItemInfo>> annotationInfos) throws NotFoundException, CannotCompileException {
        CtClass ctClass = ClassPool.getDefault().getCtClass("cc.ives.idemo.util.IDemoGenerator2");
        CtConstructor staticBlock = ctClass.makeClassInitializer();
        if (staticBlock == null){
            System.out.println("init annotation infos failure because make static block failure");
            return;
        }
        staticBlock.setBody("");
    }
}
