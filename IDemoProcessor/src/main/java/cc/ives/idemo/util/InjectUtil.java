//package cc.ives.idemo.util;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//
//import cc.ives.idemo.annotation.IDItemInfo;
//import javassist.CannotCompileException;
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtConstructor;
//import javassist.NotFoundException;
//
///**
// * 代码插入工具
// *
// * Processor阶段似乎不能修改类，只能使用生成新类的方式
// */
//public class InjectUtil {
//    private static final String INJECTED_CLASS_NAME = IDemoGenerator2.class.getCanonicalName();
//
//
////    static{
////        HashMap<String, LinkedList<IDItemInfo>> inputMap = (HashMap<String, LinkedList<IDItemInfo>>) classInfoCache.clone();
////        Iterator<String> keyIt = inputMap.keySet().iterator();
////        String key;
////        LinkedList<IDItemInfo> itemList2;
////        while (keyIt.hasNext()) {
////            key = keyIt.next();
////
////            itemList2 = new LinkedList<>();
////            LinkedList<IDItemInfo> itemList1 = inputMap.get(key);
////            for (IDItemInfo itemInfo1 : itemList1) {
////                IDItemInfo itemInfo2 = new IDItemInfo();
////                itemInfo2.setFunctionName(itemInfo1.getFunctionName());
////                itemInfo2.setIndexTime(itemInfo1.getIndexTime());
////                itemInfo2.setClassName(itemInfo1.getClassName());
////                itemInfo2.setItemName(itemInfo1.getItemName());
////                itemList2.add(itemInfo2);
////            }
////
////            classInfoCache.put(key, itemList2);
////        }
////    }
//
//    /**
//     * 注入注解信息的初始化代码
//     * @param annotationInfo
//     * @throws NotFoundException
//     * @throws CannotCompileException
//     */
//    public static void injectInitAnnotationInfoSet(HashMap<String, LinkedList<IDItemInfo>> annotationInfo) throws NotFoundException, CannotCompileException, IOException {
//        CtClass ctClass = ClassPool.getDefault().getCtClass(INJECTED_CLASS_NAME);
//        CtConstructor staticBlock = ctClass.makeClassInitializer();
//        if (staticBlock == null){
//            System.out.println("init annotation info failure because make static block failure");
//            return;
//        }
//
//        // 编写item信息初始化代码
//        StringBuilder sb = new StringBuilder();
//        Iterator<String> keyIt = annotationInfo.keySet().iterator();
//        String key;
//        LinkedList<IDItemInfo> inputItemList;
//        int index = 0;
//        while (keyIt.hasNext()){
//            key = keyIt.next();
//            sb.append("LinkedList<IDItemInfo> itemList").append(index).append(" = new LinkedList<>();");//LinkedList<IDItemInfo> itemList2 = new LinkedList<>();
//            inputItemList = annotationInfo.get(key);
//            for (IDItemInfo itemInfo1 : inputItemList) {
//                sb.append("IDItemInfo itemInfo").append(index).append(" = new IDItemInfo();");
//                sb.append("itemInfo").append(index).append(".setFunctionName(").append(itemInfo1.getFunctionName()).append(");");
//                sb.append("itemInfo").append(index).append(".setIndexTime(").append(itemInfo1.getIndexTime()).append(");");
//                sb.append("itemInfo").append(index).append(".setClassName(").append(itemInfo1.getClassName()).append(");");
//                sb.append("itemInfo").append(index).append(".setItemName(").append(itemInfo1.getItemName()).append(");");
//                sb.append("itemList").append(index).append(".add(itemInfo").append(index).append(");");
//            }
//            sb.append("classInfoCache.put(").append(key).append(", itemList").append(index).append(");");
//            index++;
//        }
//
//        staticBlock.setBody(sb.toString());
//        ctClass.writeFile();
//        ctClass.defrost();
//
//    }
//}
