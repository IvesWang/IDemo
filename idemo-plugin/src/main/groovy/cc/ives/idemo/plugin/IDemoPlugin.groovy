package cc.ives.idemo.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.FeatureExtension
import com.android.build.gradle.FeaturePlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project

class IDemoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        printPaths(project)
        File mBuildDir = new File(project.getBuildDir(), "idemo_plugin")
        project.android.registerTransform(new IDemoTransform(project))
    }

    void printPaths(Project project){
        println "=========print paths==================${hashCode()}"
        System.out.println("build目录:"+project.getBuildDir().absolutePath)
        project.allprojects.each {
            println "project name:${it.name}, path:${it.path}"
        }
        println "rootDir is:${project.rootDir.path}"
        println "childProject is:${project.childProjects.toMapString()}"
        println "subProject is:${project.subprojects.toListString()}"
        println "--------------------------------------"


        project.plugins.all {
            println "plugin is:${it}"
            switch (it) {
                case FeaturePlugin :
                    def extension = project.extensions.getByType(FeatureExtension)
                    configureR2Generation(project, extension.featureVariants)
                    configureR2Generation(project, extension.libraryVariants)
                    break
                case LibraryPlugin :
                    def extension = project.extensions.getByType(LibraryExtension)
                    configureR2Generation(project, extension.libraryVariants)
                    break
                case AppPlugin :
                    def extension = project.extensions.getByType(AppExtension)
                    configureR2Generation(project, extension.applicationVariants)
                    break
            }
        }
    }

    static void configureR2Generation(Project project, DomainObjectSet<BaseVariant> variants) {
        variants.all { variant ->
            def myOutputDir = "${project.buildDir.absolutePath}${File.separator}generated${File.separator}source${File.separator}r2${File.separator}${variant.dirName}"
            println "variant is${variant.dirName},myOutputDir is:${myOutputDir}"    // E:\asproject\IDemo\app\build\generated\source\r2\debug

            variant.outputs.all { output ->
                println "output file dir:${output.dirName}"
//                variant.registerJavaGeneratingTask(generateTask, myOutputDir)
            }
        }
    }
}
