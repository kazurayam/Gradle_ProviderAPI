package io.micronaut.internal.jflex;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;

/**
 * https://melix.github.io/blog/2022/01/understanding-provider-api.html
 */
@CacheableTask  // this annotation tells Gradle that the result of executing this task can be cached
public abstract class JFlexTask extends DefaultTask {  // every Gradle task needs to extend the DefaultTask type
// our task is defined abstract: this is because we will let Gradle generate boilerplate code for us, in particular how to inject the input and output properties.
    // you do not need to know how to create a DirectoryProperty: Gradle will do it for you

    // we are using DirectoryProperty as the type for our input and output properties.
    // This is a very important Gradle type, which belongs t the so-called "provider API" or, as you can sometimes read, the "lazy API".
    // Most of the ordering problems that earlier versions of Gradle had are fixed by this API, so use it!
    // One thing we can notice is that it's strongly typed: to declare an input directory, we don't define the property as a File or Path: it's a directory, which helps both Gradle and users understand what you are supposed to give as an input:
    // if the property is set to a regular file, then Gradle can provide a reasonable error message explaining that it expected a directory instead.
    @InputDirectory  // thie input of our task is a directory containing JFlex files
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract DirectoryProperty getSourceDirectory();

    @OutputDirectory  // the output of our task is going to be a directory containing generated Java files
    public abstract DirectoryProperty getOutputDirectory();

    @TaskAction  // this is the main task action, which is going to call JFlex
    public void generateSources() {
        // call JFlex library
    }

}
