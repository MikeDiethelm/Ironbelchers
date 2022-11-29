package ch.zhaw.pm2.ironbelchers.classloader;

import javax.tools.*;
import java.io.File;
import java.util.List;

public class ClassCompiler {
    private final File classFile;
    private static final List<String> OPTION_LIST = List.of("-classpath",
                                                            System.getProperty(
                                                                    "java.class.path") +
                                                                    File.pathSeparator +
                                                                    "dist/InlineCompiler.jar",
                                                            "-d", "bin/main");

    public ClassCompiler(File classFile) {
        this.classFile = classFile;
    }

    public boolean compile() {
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                diagnostics, null, null);


        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(
                List.of(classFile));

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                                                             diagnostics,
                                                             OPTION_LIST, null,
                                                             compilationUnits);

        return Boolean.TRUE.equals(task.call());
    }
}
