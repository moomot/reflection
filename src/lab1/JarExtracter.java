package lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JTree;

public class JarExtracter {

	public static void extractJar(String jarFile, JTree tree) {
		java.util.jar.JarFile jar = null;
		try {
			jar = new java.util.jar.JarFile(jarFile);
			Enumeration<JarEntry> enumEntries = jar.entries();
			TreeModifier modifier = new TreeModifier(tree, jar, enumEntries);
			modifier.exec();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Class loadClass(String className, String pathToJar) {
		try {
			JarFile jarFile = new JarFile(pathToJar);

			URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
			URLClassLoader cl = URLClassLoader.newInstance(urls);

			Class loaded_class = cl.loadClass(className);
			return loaded_class;
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	public static String loadFile(String path, String pathToJar) throws IOException {
		URL url = new URL("jar:file:" + pathToJar + "!/" + path);
		InputStream is = url.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
		}
		reader.close();
		return out.toString();
	}

	public static ArrayList<String> getFileData(String path, String jarPath) throws IOException {
		ArrayList<String> array = new ArrayList<>();
		JarFile jarFile = new JarFile(jarPath);
		JarEntry entry = jarFile.getJarEntry(path);
		array.add("Время модификации: " + entry.getLastModifiedTime());
		array.add("Размер: " + entry.getSize());
		array.add("Тип: " + getMimeType("jar:file:" + jarPath + "!/" + path));
		return array;
	}

	private static String getMimeType(String fileUrl) throws java.io.IOException {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String type = fileNameMap.getContentTypeFor(fileUrl);

		return type == null ? "не определен" : type;
	}
}
