package lab1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreeModifier {
	private JarFile jar;
	private String jarFile;
	private Enumeration<JarEntry> enumEntries;
	private JTree tree;

	public TreeModifier(JTree tree, JarFile jar, Enumeration<JarEntry> enumEntries) {
		this.jar = jar;
		this.jarFile = jar.getName();
		this.enumEntries = enumEntries;
		this.tree = tree;
	}

	public void exec() {
		String[] parts = jarFile.split("/");
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(parts[parts.length - 1]);
		DefaultTreeModel model = new DefaultTreeModel(root);
		try {
			String key = "";
			while (enumEntries.hasMoreElements()) {
				// Get file, split path
				JarEntry file = enumEntries.nextElement();
				String[] fileNameParts = file.getName().split("/");
				// Find last node by path

				DefaultMutableTreeNode findNode = find(root, key);

				// If file current iter is file, find node by file directory
				if (fileNameParts.length > 1) {
					if (!fileNameParts[fileNameParts.length - 2].equalsIgnoreCase(key)) {
						findNode = find(root, fileNameParts[fileNameParts.length - 2]);
					}
				}

				// If parent is root
				if (fileNameParts.length == 1)
					findNode = root;

				// Create node
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileNameParts[fileNameParts.length - 1]);

				// Something was found, add new node to the found node
				if (findNode != null)
					findNode.add(node);
				else
					root.add(node);

				if (file.isDirectory())
					key = fileNameParts[fileNameParts.length - 1];
			}
			jar.close();
			sortchildren(root);
			tree.setModel(model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private DefaultMutableTreeNode find(DefaultMutableTreeNode root, String s) {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			if (node.toString().equalsIgnoreCase(s)) {
				return node;
			}
		}
		return null;
	}

	private void sortchildren(DefaultMutableTreeNode node) {
		ArrayList children = Collections.list(node.children());
		// for getting original location
		ArrayList<String> orgCnames = new ArrayList<String>();
		// new location
		ArrayList<String> cNames = new ArrayList<String>();
		// move the child to here so we can move them back
		DefaultMutableTreeNode temParent = new DefaultMutableTreeNode();
		for (Object child : children) {
			DefaultMutableTreeNode ch = (DefaultMutableTreeNode) child;
			temParent.insert(ch, 0);
			cNames.add(ch.toString().toUpperCase());
			orgCnames.add(ch.toString().toUpperCase());
		}
		Collections.sort(cNames);
		for (String name : cNames) {
			// find the original location to get from children arrayList
			int indx = orgCnames.indexOf(name);
			node.insert((DefaultMutableTreeNode) children.get(indx), node.getChildCount());
		}
	}

}
