package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.pythongong.util.PathUtils;

public class PathUtilsTest {
    
    @Test
    public void test_FilePath() {
        Set<String> paths = PathUtils.getFileNamesOfPackage("util.com.test", null);
        Set<String> correctPaths = new HashSet<>();
        String baseDir = "util" + PathUtils.SYSTEM_PATH_SEPARATOR + 
        "com" + PathUtils.SYSTEM_PATH_SEPARATOR + 
        "test" + PathUtils.SYSTEM_PATH_SEPARATOR;
        correctPaths.add(baseDir + "PathA.class");
        correctPaths.add(baseDir + "a.properties");
        correctPaths.add(baseDir + "inside" + PathUtils.SYSTEM_PATH_SEPARATOR +"PathB.class");
        assertEquals(correctPaths, paths);
    }

    @Test
    public void test_JarPath() {
        Set<String> paths = PathUtils.getFileNamesOfPackage("org.junit.jupiter", null);
        assertTrue(!paths.isEmpty());;
    }
}
