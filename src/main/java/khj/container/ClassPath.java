package main.java.khj.container;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ClassPath {

    // 클래스패스를 스캔하여 특정 조건에 맞는 클래스를 수집
    public static Set<Class<?>> findClassesWithAnnotation(Class<? extends Annotation> annotation, String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            String packagePath = packageName.replace(".", "/");
            //System.out.println("url : " + packagePath);
            // 클래스 로더를 통해 리소스 검색
            URL resource = Thread.currentThread().getContextClassLoader().getResource(packagePath);
            if (resource == null) {
                System.out.println("Package not found: " + packageName);
                return classes;
            }

            File directory = new File(resource.getFile());
            //System.out.println("directory : " + directory);
            if (!directory.exists()) {
                System.out.println("Directory not found: " + directory.getAbsolutePath());
                return classes;
            }

            for (File file : directory.listFiles()) {
                //System.out.println("file : " + file);
                if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    //System.out.println("class : " + className);
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(annotation)) {
                        classes.add(clazz);
                    }
                } else {
                    //System.out.println("path : " +file.getPath());
                    for (File file1 : file.listFiles()) {
                        //System.out.println("folder : " + file.getName());
                        //System.out.println("class2 : " + file1.getName());
                        if (file1.getName().endsWith(".class")) {
                            String className = packageName + "." + file.getName() + "." + file1.getName().replace(".class", "");
                            //System.out.println("class3 : " + className);
                            Class<?> clazz = Class.forName(className);
                            if (clazz.isAnnotationPresent(annotation)) {
                                //System.out.println("anno ok!!");
                                classes.add(clazz);
                            }
                        }
                    }

                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // 클래스패스를 스캔하여 특정 어노테이션이 있는 필드를 가진 클래스를 수집
    public static Set<Class<?>> findClassesWithFieldAnnotation(Class<? extends Annotation> annotation, String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            String packagePath = packageName.replace(".", "/");
            URL resource = Thread.currentThread().getContextClassLoader().getResource(packagePath);
            if (resource == null) {
                System.out.println("Package not found: " + packageName);
                return classes;
            }

            File directory = new File(resource.getFile());
            if (!directory.exists()) {
                System.out.println("Directory not found: " + directory.getAbsolutePath());
                return classes;
            }

            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);

                    // 필드에 어노테이션이 있는지 검사
                    for (Field field : clazz.getDeclaredFields()) {
                        if (field.isAnnotationPresent(annotation)) {
                            classes.add(clazz);
                            break; // 해당 클래스에 어노테이션이 있는 필드가 하나라도 있으면 추가
                        }
                    }
                } else if (file.isDirectory()) {
                    // 하위 디렉토리 탐색
                    String subPackage = packageName + "." + file.getName();
                    classes.addAll(findClassesWithFieldAnnotation(annotation, subPackage));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
