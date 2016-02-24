package lab1;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class ListModifier {
	private JList<String> list = null;
	private DefaultListModel<String> model = null;

	public ListModifier(JList<String> list) {
		this.list = list;
		model = new DefaultListModel<>();
	}

	public void addElement(String str) {
		model.addElement(str);
	}

	public void loadModelOnList() {
		list.setModel(model);
	}

}
