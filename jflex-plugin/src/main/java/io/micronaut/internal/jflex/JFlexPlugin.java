package io.micronaut.internal.jflex;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

//(1) declare a project scoped plugin
public class JFlexPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        //(2) This plugin will contribute Java sources, so it depends on the Java plugin, let's apply it
        project.getPluginManager().apply(JavaPlugin.class);

        //(3) The Java plugin defines a Java extension that we're going to need
        JavaPluginExtension javaExt =
                project.getExtensions().getByType(JavaPluginExtension.class);

        //(4) Register our generateLexer task
        TaskProvider<JFlexTask> generateLexer =
                project.getTasks().register("generateLexer", JFlexTask.class, task -> {
                    task.setGroup(LifecycleBasePlugin.BUILD_GROUP);
                    task.setDescription("Generates lexer files from JFlex grammar files.");
                    task.getSourceDirectory().convention(
                            //(5) Defines the conventional (default) location of JFlex source files
                            project.getLayout().getProjectDirectory().dir("src/main/jflex")
                    );
                    task.getOutputDirectory().convention(
                            //(6) Defines the conventional (default) location of generated Java sources
                            project.getLayout().getBuildDirectory().dir("generated/jflex")
                    );
                });

        // Register the output of the JFlex task as generated sources
        /*
            This says "please add the output of the generateLexer task as a source directory".
        The magic is that because the generateLexer task defines an output directory,
        now we just said that this output directory contains java classes.
        And any task which requires Java sources will automatically trigger the execution of our generateLexer task:
        we don't have to define the relationship explicitly.
            In other words, because the input of the Java compilation task is a source set,
        and that source set defines that as an input, it has a directory which is generated
        by the generateLexer task, Gradle knows that before compiling,
        it needs to call that generateLexer task. Any other task using the source sets as an input
        will do the same: it avoids duplication of code and hard-wiring.
         */
        javaExt.getSourceSets()
                .getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                .getJava()
                .srcDir(generateLexer);
        //(7) Defines that the output of the task are Java files which needs to be compiled

    }
}
