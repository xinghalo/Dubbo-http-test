package com.xingoo.test.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 *
 * 参考：http://blog.csdn.net/sun5769675/article/details/51789375
 * 参考：http://blog.csdn.net/baibinboss/article/details/68947929
 */
public class DubboApiUtils {

    private static Logger logger = LoggerFactory.getLogger(DubboApiUtils.class);
    public static Set<String> NORMAL_METHODS = new HashSet<>(Arrays.asList("wait","equals","toString","hashCode","getClass","notify","notifyAll"));

    /**
     * 根据包名扫描下面的所有class
     * @param pack
     * @return
     */
    public static Set<Class<?>> getClasses(String pack){

        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();

        String packageDirName = pack.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {

                URL url = dirs.nextElement();

                //如果是jar，则采用JarURLConnection的方式连接，获得class文件
                if("jar".equals(url.getProtocol())){
                    findClassesInJar(url,classes,pack);
                }else{
                    findClassesInSrc(url,classes,pack);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return classes;
    }

    public static void findClassesInJar(URL url,Set<Class<?>> classes,String basePackage) throws IOException, ClassNotFoundException {
        //转换为JarURLConnection
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        if (connection != null) {
            JarFile jarFile = connection.getJarFile();
            if (jarFile != null) {
                //得到该jar文件下面的类实体
                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                while (jarEntryEnumeration.hasMoreElements()) {
                    JarEntry entry = jarEntryEnumeration.nextElement();
                    String jarEntryName = entry.getName();
                    //这里我们需要过滤不是class文件和不在basePack包名下的类
                    if (jarEntryName.contains(".class") && jarEntryName.replaceAll("/",".").startsWith(basePackage)) {

                        String className = jarEntryName
                                .substring(0, jarEntryName.lastIndexOf("."))
                                .replace("/", ".");

                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
                    }
                }
            }
        }
    }

    public static void findClassesInSrc(URL url, Set<Class<?>> classes, String basePackage) throws UnsupportedEncodingException {

        File dir = new File(URLDecoder.decode(url.getFile(), "UTF-8"));

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        Arrays.stream(dir.listFiles())
                .filter(file->file.getName().contains("Provider"))
                .forEach(file -> {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(basePackage + '.' + className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 获得对应的方法
     * @param className
     * @return
     */
    public static List<Method> getMethod(String className){
        try {

            Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            Method[] methods = clazz.getMethods();

            return Arrays.stream(methods)
                    .filter(method -> !NORMAL_METHODS.contains(method.getName()))
                    .collect(Collectors.toList());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 根据字符串拼接，获得对应的参数类型数组
     * @param types
     * @return
     */
    public static Class<?>[] paramTypeFormat(String types){
        List<Class<?>> paramsClasses = new ArrayList<>();
        for(String type : types.split(",")){
            try {
                paramsClasses.add(Class.forName(type));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return paramsClasses.toArray(new Class[]{});
    }

    /**
     * 根据参数类型，转换类型
     * @param paramStr
     * @param types
     * @return
     */
    public static Object[] paramsFormat(String paramStr,String types){
        Class<?>[] classes = paramTypeFormat(types);
        List<Object> formats = new ArrayList<>();
        String[] params = paramStr.split(",");
        for(int i =0;i<classes.length; i++){
            if("Long".equals(classes[i].getSimpleName())){
                formats.add(Long.valueOf(params[i]));
            }else{
                formats.add(params[i]);
            }
        }
        return formats.toArray();
    }

    public static void main(String[] args) {

    }
}
