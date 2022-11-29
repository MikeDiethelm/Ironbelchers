package ch.zhaw.pm2.ironbelchers.classloader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassCompilerTest {
    static final String TEST_CLASS_NAME = "TestClass";
    static final String ERRONEOUS_TEST_CLASS_NAME = "ErroneousTestClass";
    static final String TEST_CLASS_JAVA_PATH =
            PathsForTests.PATH_TO_TEST_CLASSES + TEST_CLASS_NAME +
                    PathsForTests.JAVA_ENDING;
    static final String ERRONEOUS_TEST_CLASS_JAVA_PATH =
            PathsForTests.PATH_TO_TEST_CLASSES + ERRONEOUS_TEST_CLASS_NAME +
                    PathsForTests.JAVA_ENDING;

    static final String TEST_CLASS_TXT_PATH =
            PathsForTests.PATH_TO_TEST_CLASSES + TEST_CLASS_NAME +
                    PathsForTests.TXT_ENDING;

    static final String ERRONEOUS_TEST_CLASS_TXT_PATH =
            PathsForTests.PATH_TO_TEST_CLASSES + ERRONEOUS_TEST_CLASS_NAME +
                    PathsForTests.TXT_ENDING;


    static File testClass;
    static File erroneousTestClass;

    @BeforeAll
    static void setUp() throws IOException {
        testClass = new File(TEST_CLASS_JAVA_PATH);
        erroneousTestClass = new File(ERRONEOUS_TEST_CLASS_JAVA_PATH);

        try (FileWriter testClassFileWriter = new FileWriter(testClass);
             FileWriter erroneousTestClassFileWriter = new FileWriter(
                     erroneousTestClass);
             Scanner txtScanner = new Scanner(new File(TEST_CLASS_TXT_PATH));
             Scanner erroneousTxtScanner = new Scanner(
                     ERRONEOUS_TEST_CLASS_TXT_PATH)) {
            while (txtScanner.hasNextLine()) {
                testClassFileWriter.write(txtScanner.nextLine());
            }

            while (erroneousTxtScanner.hasNextLine()) {
                erroneousTestClassFileWriter.write(
                        erroneousTxtScanner.nextLine());
            }
        }
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.delete(testClass.toPath());
        Files.delete(erroneousTestClass.toPath());
    }

    @Test
    void testCompileFileWithoutErrors() {
        ClassCompiler classCompiler = new ClassCompiler(testClass);
        assertTrue(classCompiler.compile());
    }

    @Test
    void testCompileFileWithErrors() {
        ClassCompiler classCompiler = new ClassCompiler(erroneousTestClass);
        assertFalse(classCompiler.compile());
    }
}
