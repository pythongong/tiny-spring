/*
 * Copyright 2025 Cheng Gong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/**
 * Utility class for handling classpath resources and file system operations.
 * Provides functionality for scanning classpath resources, handling file paths,
 * and managing resource locations in both file system and JAR environments.
 *
 * @author Cheng Gong
 */
public class PathUtils {
	
	/** Private constructor to prevent instantiation */
	private PathUtils(){}

	/** Root classpath constant */
	public static final String ROOT_CLASS_PATH = "";

	/** File extension for properties files */
	public static final String PROPERTY_SUFFIX = ".properties";

	/** Package separator used in fully qualified class names */
	public static final String PACKAGE_SEPARATOR = ".";

	/** Forward slash separator used in paths */
	public static final String PATH_SEPARATOR = "/";

	/** System-specific file separator (/ for Unix-like systems, \ for Windows) */
	public static final String SYSTEM_PATH_SEPARATOR = System.getProperty("file.separator");

	/** Character used to separate nested class names */
	public static final char NESTED_CLASS_SEPARATOR = '$';

	/** Separator used in CGLIB-generated class names */
	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	/** File extension for Java class files */
	public static final String CLASS_FILE_SUFFIX = ".class";

	/** Default pattern for finding class files */
	public static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	/** Space character constant */
	public static final String SPACE = " ";

	/** Prefix for searching all matching resources in the classpath */
    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    /** Prefix for loading from the class path */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/** Prefix for loading from the file system */
	public static final String FILE_URL_PREFIX = "file:";

	/** Prefix for loading from a JAR file */
	public static final String JAR_URL_PREFIX = "jar:";

    /**
     * Finds class path file names based on the provided search parameters.
     * Supports searching in both file system and JAR environments.
     *
     * @param param the search parameters containing configuration for the search
     * @throws BeansException if an error occurs during file operations
     */
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

				if (param.searchSudDirect()) {
					findFileNamesIncluSubdirect(basePath, param.pathMapper());
				} else {
					findFileNames(basePath, param.pathMapper());		
				}
			}
		} catch (URISyntaxException | IOException e) {
			throw new BeansException("file error");
		}
	}

    /**
     * Recursively finds file names in a directory and its subdirectories.
     *
     * @param basePath the base path to start searching from
     * @param pathMapper consumer to process found paths
     * @return set of found file names
     * @throws BeansException if an error occurs during file operations
     */
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

    /**
     * Finds file names in a single directory (non-recursive).
     *
     * @param basePath the directory to search in
     * @param pathMapper consumer to process found paths
     * @throws BeansException if an error occurs during file operations
     */
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

    /**
     * Converts a package name to a path by replacing package separators with path separators.
     *
     * @param packageName the package name to convert
     * @return the converted path
     */
	public static String convertPackageToPath(String packageName) {
		return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
	}
}
