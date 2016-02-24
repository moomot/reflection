package lab1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class MainWindow extends JFrame {

	private JFrame frmReflection;
	private JTree tree;
	private JScrollPane scrollPane;
	private JMenuBar menuBar;
	private JMenuItem menuItem;

	private String jarPath = "jar_files/test.jar";
	private Class loaded_class = null;
	private JPanel panel_1;
	private JList list;
	private JScrollPane scrollPane_1;
	private MouseAdapter treeMouseAdapter = null;
	private String selectionPath = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainWindow window = new MainWindow();
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					window.frmReflection.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		load();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmReflection = new JFrame();
		frmReflection.setTitle("Reflection");
		frmReflection.setBounds(100, 100, 972, 532);
		frmReflection.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmReflection.getContentPane().setLayout(new GridLayout(1, 1, 5, 0));
		frmReflection.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setBorder(
				new CompoundBorder(new EmptyBorder(0, 5, 5, 0), new EtchedBorder(EtchedBorder.LOWERED, null, null)));
		frmReflection.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane);

		tree = new JTree();
		tree.addTreeSelectionListener(treeSelectionListener());
		scrollPane.setViewportView(tree);

		panel_1 = new JPanel();
		panel_1.setBackground(new Color(238, 238, 238));
		panel_1.setBorder(
				new CompoundBorder(new EmptyBorder(0, 0, 5, 5), new EtchedBorder(EtchedBorder.LOWERED, null, null)));
		frmReflection.getContentPane().add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_1.add(scrollPane_1);

		list = new JList();
		scrollPane_1.setViewportView(list);
		list.setOpaque(false);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new MyCellRenderer());
		list.setModel(new AbstractListModel() {
			String[] values = new String[] { "Список", "для отображения" };

			@Override
			public int getSize() {
				return values.length;
			}

			@Override
			public Object getElementAt(int index) {
				return values[index];
			}
		});

		menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(0, 0, 5, 0));
		frmReflection.setJMenuBar(menuBar);

		menuItem = new JMenuItem("Открыть");
		menuItem.addActionListener(e -> openDialog());
		menuBar.add(menuItem);
	}

	private void load() {
		JarExtracter.extractJar(jarPath, tree);
	}

	private void openDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			loaded_class = null;
			File selected_file = chooser.getSelectedFile();
			jarPath = selected_file.getAbsolutePath();
			JarExtracter.extractJar(jarPath, tree);
		}
	}

	private MouseAdapter loadMouseAdapter() {
		return new MouseAdapter() {
			private void myPopupEvent(MouseEvent e) {
				// Get cursor position
				int x = e.getX();
				int y = e.getY();

				JTree tree = (JTree) e.getSource();
				TreePath path = tree.getPathForLocation(x, y);
				if (path == null)
					return;

				tree.setSelectionPath(path);

				JPopupMenu popup = new JPopupMenu();
				ActionListener actionListener = new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						switch (e.getActionCommand()) {
						case "Конструкторы": {
							Reflect r = new Reflect(loaded_class);
							ListModifier lst = new ListModifier(list);
							lst.addElement(e.getActionCommand() + " :");
							ArrayList<String> array = r.getConstructors();
							for (String string : array) {
								lst.addElement(string);
							}
							if (array.isEmpty())
								lst.addElement("Нет конструкторов");
							lst.loadModelOnList();
							break;
						}
						case "Методы": {
							Reflect r = new Reflect(loaded_class);
							ListModifier lst = new ListModifier(list);
							lst.addElement(e.getActionCommand() + " :");
							ArrayList<String> array = r.getMethodsWithAnnotations();
							for (String string : array) {
								lst.addElement(string);
							}
							if (array.isEmpty())
								lst.addElement("Нет методов");
							lst.loadModelOnList();
							break;
						}
						case "Поля": {
							Reflect r = new Reflect(loaded_class);
							ListModifier lst = new ListModifier(list);
							lst.addElement(e.getActionCommand() + " :");
							ArrayList<String> array = r.getFields();
							for (String string : array) {
								lst.addElement(string);
							}
							if (array.isEmpty())
								lst.addElement("Нет полей");
							lst.loadModelOnList();
							break;
						}
						case "Пакет": {
							Reflect r = new Reflect(loaded_class);
							ListModifier lst = new ListModifier(list);
							lst.addElement(e.getActionCommand() + " :");
							String pkg = r.getPackageName();
							lst.addElement(pkg);
							lst.loadModelOnList();
							break;
						}
						case "Суперкласс": {
							Reflect r = new Reflect(loaded_class);
							ListModifier lst = new ListModifier(list);
							lst.addElement(e.getActionCommand() + " :");
							ArrayList<String> array = r.getSuperClassName();
							for (String string : array) {
								lst.addElement(string);
							}
							lst.loadModelOnList();
							break;
						}
						case "Интерфейсы": {
							Reflect r = new Reflect(loaded_class);
							ListModifier lst = new ListModifier(list);
							lst.addElement(e.getActionCommand() + " :");
							ArrayList<String> array = r.getInterfaces();
							for (String string : array) {
								lst.addElement(string);
							}
							if (array.isEmpty())
								lst.addElement("Нет интерфейсов");
							lst.loadModelOnList();
							break;
						}
						}
					}
				};

				String[] labels = { "Конструкторы", "Методы", "Поля", "Пакет", "Суперкласс", "Интерфейсы" };

				for (int i = 0; i < labels.length; i++) {
					JMenuItem item = new JMenuItem(labels[i]);
					item.addActionListener(actionListener);
					popup.add(item);
				}

				popup.show(tree, x, y);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}
		};
	}

	private MouseAdapter loadFileMouseAdapter(boolean modifyDate, boolean fileSize, boolean type,
			boolean showFileContents) {
		return new MouseAdapter() {
			private void myPopupEvent(MouseEvent e) {
				// Get cursor position
				int x = e.getX();
				int y = e.getY();

				JTree tree = (JTree) e.getSource();
				TreePath path = tree.getPathForLocation(x, y);
				if (path == null)
					return;

				tree.setSelectionPath(path);

				JPopupMenu popup = new JPopupMenu();
				ActionListener actionListener = new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						switch (e.getActionCommand()) {
						case "Метаданные": {
							try {
								ArrayList<String> array = JarExtracter.getFileData(selectionPath, jarPath);
								String fileModDate = "", size = "", fileType = "";
								if (modifyDate)
									fileModDate = array.get(0);
								if (fileSize)
									size = array.get(1);
								if (type)
									fileType = array.get(2);
								try {
									DialogWindow dialog = new DialogWindow();
									dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
									dialog.setTextArea(fileModDate + "\n" + size + "\n" + fileType + "\n");
									dialog.setTitle("Метаданные");
									dialog.setVisible(true);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							break;
						}
						case "Содержимое": {
							try {
								String content = JarExtracter.loadFile(selectionPath, jarPath);
								try {
									DialogWindow dialog = new DialogWindow();
									dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
									dialog.setTextArea(content);
									dialog.setTitle("Содержимое");
									dialog.setVisible(true);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							break;
						}
						}
					}
				};

				ArrayList<String> labels = new ArrayList<>();
				labels.add("Метаданные");
				if (showFileContents)
					labels.add("Содержимое");
				System.out.println(labels);

				for (String string : labels) {
					JMenuItem item = new JMenuItem(string);
					item.addActionListener(actionListener);
					popup.add(item);
				}

				popup.show(tree, x, y);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}
		};
	}

	private TreeSelectionListener treeSelectionListener() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				tree.removeMouseListener(treeMouseAdapter);
				// Path of JTree item
				TreePath currentSelection = tree.getSelectionPath();

				if (currentSelection != null) {
					// Path to com.package.myclass.class
					String str = "";
					for (int i = 1; i < currentSelection.getPathCount(); i++) {
						str += currentSelection.getPathComponent(i) + "/";
					}
					// Remove last character
					str = str.substring(0, str.length() - 1);
					selectionPath = str;
					str = str.replace("/", ".");

					// Load only classes
					if (str.endsWith(".class")) {
						treeMouseAdapter = loadMouseAdapter();
						tree.addMouseListener(treeMouseAdapter);

						String className = str.substring(0, str.length() - 6);
						loaded_class = JarExtracter.loadClass(className, jarPath);
					} else if (str.endsWith(".properties")) {
						treeMouseAdapter = loadFileMouseAdapter(true, true, true, true);
						tree.addMouseListener(treeMouseAdapter);
					} else if (selectionPath.toLowerCase().contains("resources")) {
						treeMouseAdapter = loadFileMouseAdapter(true, true, true, false);
						tree.addMouseListener(treeMouseAdapter);
					} else {
						treeMouseAdapter = loadFileMouseAdapter(true, true, false, false);
						tree.addMouseListener(treeMouseAdapter);
					}
				}
			}
		};
	}

}
