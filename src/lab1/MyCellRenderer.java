package lab1;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class MyCellRenderer extends DefaultListCellRenderer {
	final JPanel p = new JPanel(new BorderLayout());
	final JLabel lt = new JLabel();
	String pre = "<html><body style='width: 363px;border-bottom: 1px solid #cccccc; padding: 5px 15px 5px 5px'>";

	MyCellRenderer() {
		p.add(lt, BorderLayout.CENTER);
		// text
	}

	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, final int index,
			final boolean isSelected, final boolean hasFocus) {
		String text = (String) value;
		text = text.replace("<", "&lt;");
		text = text.replace(">", "&gt;");
		lt.setText(pre + text);

		return p;
	}

}
