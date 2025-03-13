package com.pythongong.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.pythongong.exception.IocException;

public class PathUtils {
	
	public PathUtils(){}

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

	/**
	 * 
	 * @param packageName
	 * @param mapper Function to access the file name
	 * @return
	 */
	public static Set<String> getFileNamesOfPackage(String packageName, Function<String, String> mapper) {
		String basePackagePath = packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
		Set<String> paths = new HashSet<>();
		try {
			// Get urls by the classloader
			Enumeration<URL> en = ClassUtils.getDefaultClassLoader().getResources(basePackagePath);
			while (en.hasMoreElements()) {
				URL url = en.nextElement();
				paths.addAll(getFileNameOfPath(url, basePackagePath, mapper));
			}
		} catch (IOException e) {
			throw new IocException("package name:" + packageName, e);
		}

		return paths;
	}
	
	/**
	 * 
	 * @param url
	 * @param basePackagePath
	 * @param mapper
	 * @return
	 */
	private static Set<String> getFileNameOfPath(URL url, String basePackagePath, Function<String, String> mapper) {
		Set<String> paths = new HashSet<>();
		try {
			URI uri = url.toURI();
			String uriStr = URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8);
			
			if (uriStr.startsWith(FILE_URL_PREFIX)) {
				// Remove the file prefix and leading slash
				String rootDir = stripLeadingSlash(uriStr.substring(
				FILE_URL_PREFIX.length(), uriStr.length() - basePackagePath.length()));
				Path path = Paths.get(uri);
				Files.walk(path).filter(Files::isRegularFile).forEach(filePath -> {
					// Remove the root dir and just leave the 
					String fileName = filePath.toString().substring(rootDir.length());
					accessFileName(fileName, mapper, paths);
				});
			} else if (uriStr.startsWith(JAR_URL_PREFIX)) {
				// Jar system needs a new file system to create path
				Path path = FileSystems.newFileSystem(uri, Map.of()).getPath(basePackagePath);
				Files.walk(path).filter(Files::isRegularFile).forEach(filePath -> {
					String fileName = filePath.toString();
					accessFileName(fileName, mapper, paths);
				});
			}
		} catch (URISyntaxException | IOException e) {
			throw new IocException("package path:" + basePackagePath, e);
		}
		
		return paths;
	}

	private static void accessFileName(String fileName , Function<String, String> mapper, Set<String> paths ) {
		if (mapper != null) {
			fileName = mapper.apply(fileName);
		}
		
		if (fileName != null) {
			paths.add(fileName);
		}
	}

	private static String stripLeadingSlash(String path) {
		return (path.startsWith(PATH_SEPARATOR) ? path.substring(PACKAGE_SEPARATOR.length()) : path);
	}
	
}
