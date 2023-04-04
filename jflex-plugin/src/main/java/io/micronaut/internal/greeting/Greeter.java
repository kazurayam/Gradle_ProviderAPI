package io.micronaut.internal.jflex;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class Greeter extends DefaultTask {

    @Input
    public abstract Property<String> getUser();

    @Input
    public abstract Property<String> getIntro();

    @Input
    public abstract Property<String> getOutro();

    @TaskAction
    public void sayHello() {
        String user = getUser().get();
        String intro = getIntro().get();
        String outro = getOutro().get();
        System.out.println(intro + user + outro);
    }
}
