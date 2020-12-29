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
        File mBuildDir = new File(project.getBuildDir(), "FunctionAdder")
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
                    project.extensions[FeatureExtension].run {
                        configureR2Generation(project, featureVariants)
                        configureR2Generation(project, libraryVariants)
                    }
                    break
                case LibraryPlugin :
                    project.extensions[LibraryExtension].run {
                        configureR2Generation(project, libraryVariants)
                    }
                    break
                case AppPlugin :
                    project.extensions[AppExtension].run {
                        configureR2Generation(project, applicationVariants)
                    }
                    break
            }
        }
    }

    static void configureR2Generation(Project project, DomainObjectSet<BaseVariant> variants) {
        variants.all { variant ->
            def myOutputDir = project.buildDir.resolve(
                    "generated/source/r2/${variant.dirName}")
            println "variant is${variant.dirName},myOutputDir is:${myOutputDir}"

            variant.outputs.all { output ->
                println "output file dir:${output.dirName}"
//                variant.registerJavaGeneratingTask(generate, outputDir)
            }
        }
    }
}
