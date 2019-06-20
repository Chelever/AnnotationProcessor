package com.renan.main;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import javassist.NotFoundException;

public class Processor {

	private static Processor processor;
	private static HashMap<String, Object> classesInstances;

	private Processor() {
		classesInstances = new HashMap<String, Object>();
	}

	public static Processor getInstance() {
		if (processor == null)
			processor = new Processor();
		return processor;
	}

	public String process(JSONObject request) throws NotFoundException{
		try {
			for (Class mClass : getClasses("com.renan")) {
				System.out.println("Class: "+mClass.getName());
				String type = request.getString("type");
				String function = request.getString("function");
				JSONObject data = request.getJSONObject("data");
				String answer = checkClass(mClass, type, function, data);
				if(!answer.equals("error"))
					return answer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		throw new NotFoundException("");
	}

	private static String checkClass(Class<CustomClass> obj, String type, String function, JSONObject data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		if (obj.isAnnotationPresent(CustomAnnotation.class)) {

			Annotation annotation = obj.getAnnotation(CustomAnnotation.class);
			CustomAnnotation testerInfo = (CustomAnnotation) annotation;

			System.out.println("Value :" + testerInfo.value());

			if (type.equals(testerInfo.value()))
				for (Method method : obj.getDeclaredMethods()) {

					System.out.println("Methods " + method.getName());
					// if method is annotated with @Test
					if (method.isAnnotationPresent(Function.class)) {

						Annotation methodAnnotation = method.getAnnotation(Function.class);
						Function f = (Function) methodAnnotation;

						System.out.println("Function: " + f.value());
						
						if (function.equals(f.value())) {
							Object instance = getSingleton(obj, f);
							String returnValue = (String) method.invoke(instance, data);
							System.out.println("Return value = "+returnValue);
							return returnValue;
						}

					}

				}

			System.out.printf("Finish");

		}
		return "error";
	}

	private static Object getSingleton(Class<CustomClass> obj, Function f)
			throws InstantiationException, IllegalAccessException {
		Object instance = classesInstances.get(f.value());
			if(instance == null) {
				classesInstances.put(f.value(), obj.newInstance());
				instance = classesInstances.get(f.value());
			}
		return instance;
	}

	private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
}
