package lab1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class DialogWindow extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextArea textArea;

	/**
	 * Create the dialog.
	 */
	public DialogWindow() {
		setModal(true);
		setAlwaysOnTop(true);
		setBounds(350, 220, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBounds(10, 10, 424, 216);
			panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			contentPanel.add(panel);
			panel.setBackground(new Color(238, 238, 238));
			panel.setLayout(null);
			{
				textArea = new JTextArea();
				textArea.setLineWrap(true);
				textArea.setBounds(12, 12, 400, 192);
				textArea.setEditable(false);
				textArea.setBackground(new Color(238, 238, 238));
				panel.add(textArea);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton OkButton = new JButton("OK");
				OkButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				OkButton.setActionCommand("OK");
				buttonPane.add(OkButton);
			}
		}
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(String text) {
		textArea.setText(text);
	}
}
