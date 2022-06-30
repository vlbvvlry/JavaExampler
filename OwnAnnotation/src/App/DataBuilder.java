package App;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBuilder {

    static {
        for (Class<?> clazz : getClassList("App")) {
            if (clazz.isAnnotationPresent(Data.class)) {
                System.out.println("There was find annotated class " + clazz.getName());
            }
        }
    }

    private static List<Class<?>> getClassList(String packageName) {
        String path = packageName.replace(".", "/");
        URL url = Thread.currentThread()
                .getContextClassLoader()
                .getResource(path);
        if (url == null) {
            throw new IllegalArgumentException
                    (String.format("Unable to get resource from '%s'", path));
        }
        File dir = new File(url.getFile());
        List<Class<?>> classes = new ArrayList<>();
        for (File file : dir.listFiles()) {
            classes.addAll(find(file, packageName));
        }
        return classes;
    }

    private static List<Class<?>> find(File file, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = packageName + "." + file.getName();
        if(file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            int endIndex = resource.length() - ".class".length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
                //
            }
        }
        return classes;
    }

}