package cc.ives.idemo.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import org.gradle.api.Project

class IDemoTransform extends Transform {
    private static final Set<QualifiedContent.ContentType> TYPES = new HashSet<>()
    private static final Set<QualifiedContent.Scope> SCOPES = new HashSet<>()

    private Project project

    IDemoTransform(Project project) {
        this.project = project
    }

    static {
        TYPES.add(QualifiedContent.DefaultContentType.CLASSES)

        SCOPES.add(QualifiedContent.Scope.PROJECT)
    }

    @Override
    String getName() {
        return "IDemoTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TYPES
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return SCOPES
    }

    @Override
    boolean isIncremental() {
        return true
    }
}