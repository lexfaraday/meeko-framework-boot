package org.meeko.sit.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.meeko.sit.annotation.MeekoTest;

public class MeekoTestUtils {
    public static boolean matchEnvironment(MeekoTest meekoTest, String environment) {
        if (meekoTest != null && environment != null) {
            for (String meekoEnv : meekoTest.environment()) {
                if (environment.equalsIgnoreCase(meekoEnv)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Map<Class<?>, MeekoTest> findMeekoTestSingleClass(String packageName, String singleClass) throws Exception {
        return mapWithMeekoTestClasses(MeekoTestUtils.getClasses(packageName, singleClass));
    }

    public static Map<Class<?>, MeekoTest> findMeekoTestClasses(String packageName) throws Exception {
        return mapWithMeekoTestClasses(MeekoTestUtils.getClasses(packageName, null));
    }

    private static Map<Class<?>, MeekoTest> mapWithMeekoTestClasses(Iterable<Class<?>> clazzes) {
        Map<Class<?>, MeekoTest> clazzesWithAnnotation = new HashMap<Class<?>, MeekoTest>();
        for (Class<?> clazz : clazzes) {
            if (clazz.isAnnotationPresent(MeekoTest.class)) {
                MeekoTest profile = clazz.getAnnotation(MeekoTest.class);
                if (profile.environment() != null) {
                    clazzesWithAnnotation.put(clazz, profile);
                }
            }
        }
        return clazzesWithAnnotation;
    }

    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     * 
     * @param packageName
     *            The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Iterable<Class<?>> getClasses(String packageName, String singleClass) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        List<Class<?>> classes = new ArrayList<Class<?>>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource != null) {
                if ("file".equals(resource.getProtocol())) {
                    dirs.add(new File(resource.getFile()));
                } else if ("jar".equals(resource.getProtocol())) {
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!")); //strip out only the JAR file
                    JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                    Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar

                    while (entries.hasMoreElements()) {
                        String name = entries.nextElement().getName();
                        if (name.startsWith(path)) { //filter according to the path
                            String entry = name.substring(path.length());
                            if (entry.indexOf(".class") >= 0) {
                                if (entry.startsWith("/")) {
                                    entry = entry.substring(1, entry.length());
                                }

                                String className = entry.substring(0, entry.length() - 6);
                                if (singleClass != null) {
                                    if (singleClass.equals(className)) {
                                        classes.add(Class.forName(packageName + '.' + className));
                                        jar.close();
                                        return classes;
                                    }
                                } else {
                                    classes.add(Class.forName(packageName + '.' + className));
                                }

                            }
                        }
                    }
                    jar.close();
                }
            }

        }

        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName, singleClass));
        }

        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs.
     * 
     * @param directory
     *            The base directory
     * @param packageName
     *            The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findClasses(File directory, String packageName, String singleClass) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName(), singleClass));
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                if (singleClass != null) {
                    if (singleClass.equals(className)) {
                        classes.add(Class.forName(packageName + '.' + className));
                        return classes;
                    }
                } else {
                    classes.add(Class.forName(packageName + '.' + className));
                }
            }
        }
        return classes;
    }
}
