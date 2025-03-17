package com.pythongong.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import com.pythongong.exception.BeansException;

public class PathUtils {
	
	private PathUtils(){}

	public static final String ROOT_CLASS_PATH = "";

	public static final String PROPERTY_SUFFIX = ".properties";

	/** The package separator string: {@code "."}. */
	public static final String PACKAGE_SEPARATOR = ".";

	public static final String PATH_SEPARATOR = "/";

	/** The system path separator string: {@code "/"} for Linux/macOS,  {@code "\"} for Windows. */
	public static final String SYSTEM_PATH_SEPARATOR = System.getProperty("file.separator");

	/** The nested class separator character: {@code '$'}. */
	public static final char NESTED_CLASS_SEPARATOR = '$';

	/** The CGLIB class separator: {@code "$$"}. */
	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	/** The ".class" file suffix. */
	public static final String CLASS_FILE_SUFFIX = ".class";

	public static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	public static final String SPACE = " ";

	
    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    /** Pseudo URL prefix for loading from the class path: "classpath:". */
	public static final String 
	CLASSPATH_URL_PREFIX = "classpath:";

	/** URL prefix for loading from the file system: "file:". */
	public static final String FILE_URL_PREFIX = "file:";

	/** URL prefix for loading from a jar file: "jar:". */
	public static final String JAR_URL_PREFIX = "jar:";


	public static void findClassPathFileNames(ClassPathSerchParam param) {
		CheckUtils.nullArgs(param, "PathUtils.findClassPathFileNames recevies null param");
		if (! (param.serachFile() && param.serachJar())) {
			return;
		}

		String packagePath = param.packagePath();
		
		try {
			Enumeration<URL> enumUrls = ClassUtils.getDefaultClassLoader().getResources(packagePath);
			while (enumUrls.hasMoreElements()) {
				URL url = enumUrls.nextElement();
				URI uri;
				uri = url.toURI();
				String uriStr = URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8);
				Path basePath = null;
				if (uriStr.startsWith(FILE_URL_PREFIX) && param.serachFile()) {
					basePath = Paths.get(uri);
					
				} else if (uriStr.startsWith(JAR_URL_PREFIX) && param.serachJar()) {
					// Jar system needs a new file system to create path
					basePath = FileSystems.newFileSystem(uri, Map.of()).getPath(packagePath);
				}

				if (basePath == null) {
					continue;
				}

				if (param.searchSudDirect() ) {
					findFileNamesIncluSubdirect(basePath, param.pathMapper());
				} else {
					findFileNames(basePath, param.pathMapper());		
				}
				
				
			}
		} catch (URISyntaxException | IOException e) {
			throw new BeansException("file error");
		}
	}

	private static Set<String> findFileNamesIncluSubdirect(Path basePath, BiConsumer<Path, Path> pathMapper) {
		Set<String> fileNames = new HashSet<>();
		try {
			Files.walk(basePath).filter(Files::isRegularFile).forEach(filePath -> {
				pathMapper.accept(basePath, filePath);
			});
		} catch (IOException e) {
			throw new BeansException("file error");
		}
		return fileNames;
	}

	private static void findFileNames(Path basePath, BiConsumer<Path, Path> pathMapper) {
		try (DirectoryStream<Path> stream = 
		Files.newDirectoryStream(basePath, entry -> Files.isRegularFile(entry))) {
			for (Path filePath : stream) {
				pathMapper.accept(basePath, filePath);
			}
		} catch (IOException e) {
			throw new BeansException("file error");
		}

	
	}

	public static String convertPackageToPath(String packageName) {
		return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
	}
	
}

