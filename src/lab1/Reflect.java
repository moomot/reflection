package lab1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Reflect {

	private Class jar_class = null;

	public Reflect(Class jar_class) {
		this.jar_class = jar_class;
	}

	public String getPackageName() {
		Package pkg = jar_class.getPackage();
		return pkg.getName();
	}

	public ArrayList<String> getSuperClassName() {
		ArrayList<String> array = new ArrayList<>();
		Class subclass = jar_class.getClass();
		Class superclass = subclass.getSuperclass();
		while (superclass != null) {
			String className = superclass.getName();
			array.add(className);
			subclass = superclass;
			superclass = subclass.getSuperclass();
		}
		return array;
	}

	public ArrayList<String> getInterfaces() {
		ArrayList<String> array = new ArrayList<>();
		Class[] interfaces = jar_class.getInterfaces();
		for (Class item : interfaces) {
			array.add(item.getSimpleName());
		}
		return array;
	}

	public ArrayList<String> getFields() {
		ArrayList<String> array = new ArrayList<>();
		Field[] fields = jar_class.getDeclaredFields();
		for (Field field : fields) {
			String generic = "";
			Type genericFieldType = field.getGenericType();
			if (genericFieldType instanceof ParameterizedType) {
				ParameterizedType aType = (ParameterizedType) genericFieldType;
				Type[] fieldArgTypes = aType.getActualTypeArguments();
				for (Type type : fieldArgTypes) {
					Class fieldArgClass = (Class) type;
					generic = "<" + fieldArgClass.getSimpleName() + ">";
				}
			}
			array.add(getModifiers(field.getModifiers()) + " " + getType(field.getType()) + generic + " "
					+ field.getName());
		}
		return array;
	}

	public ArrayList<String> getConstructors() {
		ArrayList<String> array = new ArrayList<>();
		Constructor[] constructors = jar_class.getDeclaredConstructors();
		for (Constructor constructor : constructors) {
			String str = getModifiers(constructor.getModifiers()) + " " + jar_class.getSimpleName() + " ("
					+ getParameters(constructor.getParameterTypes()) + ") { }";

			array.add(str);
		}
		return array;
	}

	public ArrayList<String> getMethodsWithAnnotations() {
		ArrayList<String> array = new ArrayList<>();

		Method[] methods = jar_class.getDeclaredMethods();
		for (Method method : methods) {
			String str = "";
			Annotation[] annotation = method.getAnnotations();
			for (Annotation item : annotation) {
				str += "\n@" + item.annotationType().getSimpleName();
				System.out.println("ANNOTATION");
			}

			str += "\n";
			str += getModifiers(method.getModifiers()) + getType(method.getReturnType()) + " " + method.getName() + "(";

			str += getParameters(method.getParameterTypes());
			str += ") { }";
			array.add(str);
		}
		return array;
	}

	private String getParameters(Class[] params) {
		String p = "";
		for (int i = 0, size = params.length; i < size; i++) {
			if (i > 0)
				p += ", ";
			p += getType(params[i]) + " param" + i;
		}
		return p;
	}

	private String getType(Class field) {
		String type = field.isArray() ? field.getComponentType().getSimpleName() : field.getSimpleName();
		if (field.isArray())
			type += "[]";
		return type;
	}

	private String getModifiers(int m) {
		String modifiers = "";
		if (Modifier.isPublic(m))
			modifiers += "public ";
		if (Modifier.isProtected(m))
			modifiers += "protected ";
		if (Modifier.isPrivate(m))
			modifiers += "private ";
		if (Modifier.isStatic(m))
			modifiers += "static ";
		if (Modifier.isAbstract(m))
			modifiers += "abstract ";
		if (Modifier.isVolatile(m))
			modifiers += "volatile ";
		if (Modifier.isTransient(m))
			modifiers += "transient ";
		return modifiers;
	}

	public void test() {
		System.out.println(this.getPackageName());
		System.out.println(this.getInterfaces());
		System.out.println(this.getConstructors());
		System.out.println(this.getMethodsWithAnnotations());
		System.out.println(this.getFields());
		System.out.println(this.getSuperClassName());
	}
}
