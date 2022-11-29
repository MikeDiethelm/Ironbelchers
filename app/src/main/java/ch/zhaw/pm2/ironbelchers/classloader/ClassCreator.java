package ch.zhaw.pm2.ironbelchers.classloader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ClassCreator {
    private final String path;

    public ClassCreator(String path) {
        this.path = path;
    }

    public File save(String name, String content) {
        File file = new File(path + name + ".java");

        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            try (Writer writer = new FileWriter(file)) {
                writer.write(content);
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }
}
