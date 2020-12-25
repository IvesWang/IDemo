package cc.ives.idemo

import cc.ives.idemo.annotation.IDAction
import cc.ives.idemo.annotation.IDModule
import com.google.auto.service.AutoService
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author wangziguang
 * @date 2020/12/23
 * @description
 */
//@AutoService(Processor::class)
class KotlinProcessor : AbstractProcessor() {

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {

        // 拦截IDAction的类，加入缓存
        // 创建新类把缓存信息作为硬编码写入
        val messager = processingEnv.messager
        messager.printMessage(Diagnostic.Kind.NOTE, "IDemoProcessor kotlin process start")
        messager.printMessage(
            Diagnostic.Kind.NOTE,
            "roundEnvironment class:" + roundEnvironment?.javaClass?.getCanonicalName()
        )
        messager.printMessage(
            Diagnostic.Kind.NOTE,
            "set长度：" + annotations?.size
        ) // 找到的注解类型长度，不是添加了注解的类的集合。是getSupportedAnnotationTypes()的一个子集


        val elementSet: Set<Element?> =
            roundEnvironment!!.getElementsAnnotatedWith(
                IDModule::class.java
            )
        messager.printMessage(Diagnostic.Kind.NOTE, "IDModule集合长度：" + elementSet.size)

        messager.printMessage(Diagnostic.Kind.NOTE, "=========kotlin========")
        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val typeSets: MutableSet<String> =
            HashSet()
        typeSets.add(IDAction::class.java.canonicalName)
        typeSets.add(IDModule::class.java.canonicalName)
        return typeSets
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }
}