package cc.gfc.mvc.framework.core;


import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author xiaoguo
 * @Description:
 * @Params:
 * @Return:
 * @CreatedAt: 2019/10/26 12:56
 */
public class ClassScanner {

    public static List<Class<?>> scanClasses(String basePackageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();

        //这里的packageName是 用户应用程序入口类所在的package名称，以它为基础进行组件扫描
        String path = basePackageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();
            if (protocol.contains("jar")) {
                //如果应用程序是以jar包的方式运行（java -jar），那么就会进入到这里面。
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName();
                classList.addAll(getClassesFromJar(jarFilePath, path));
            } else if (protocol.contains("file")) {
                //有时候我们想要对程序进行调试，而不是以jar方式运行。那么就会进入到这里。
                String fileOrPath = resource.getFile();
                classList.addAll(getClassFromPath(fileOrPath, basePackageName));
            }
        }
        return classList;
    }

    private static List<Class<?>> getClassFromPath(String fileOrPath, String packageName) throws ClassNotFoundException {
        List<Class<?>> list = new ArrayList<>();
        List<String> listOfClassAbsoluteName = new ArrayList<>();
        File[] files = new File(fileOrPath).listFiles();

        recursiveFindClass(listOfClassAbsoluteName, files);

        int l = fileOrPath.length();

        for (String classAbsoluteName : listOfClassAbsoluteName) {
            String className = packageName + "." + classAbsoluteName.substring(l, classAbsoluteName.length() - 6).replace(String.valueOf(File.separatorChar), ".");
            list.add(Class.forName(className));
        }

        return list;

    }

    private static void recursiveFindClass(List<String> list, File[] files) {
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                list.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                recursiveFindClass(list, file.listFiles());
            }
        }
    }

    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                String classFullName = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
