package rcb.file.trasnfer.scp.refactored.scp;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * @author roopesh-chandra bose
 */
class ComplexUserInformation implements UserInfo, UIKeyboardInteractive {
	private String passwd;
	private Container panel;
	private JTextField passwordField = (JTextField) new JPasswordField(20);
	private GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

	public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 0;
		panel.add(new JLabel(instruction), gbc);
		gbc.gridy++;

		gbc.gridwidth = GridBagConstraints.RELATIVE;

		JTextField[] texts = new JTextField[prompt.length];
		for (int i = 0; i < prompt.length; i++) {
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = 0;
			gbc.weightx = 1;
			panel.add(new JLabel(prompt[i]), gbc);

			gbc.gridx = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weighty = 1;
			if (echo[i]) {
				texts[i] = new JTextField(20);
			} else {
				texts[i] = new JPasswordField(20);
			}
			panel.add(texts[i], gbc);
			gbc.gridy++;
		}

		if (JOptionPane.showConfirmDialog(null, panel, destination + ": " + name, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
			String[] response = new String[prompt.length];
			for (int i = 0; i < prompt.length; i++) {
				response[i] = texts[i].getText();
			}
			//System.out.println("response" + response);
			return response;
		} else {
			return null; // cancel
		}
	}

	public String getPassphrase() {
		return null;
	}

	public String getPassword() {
		return passwd;
	}

	public boolean promptPassword(String message) {
		Object[] ob = { passwordField };
		if ((JOptionPane.showConfirmDialog(null, ob, message, JOptionPane.OK_CANCEL_OPTION)) == JOptionPane.OK_OPTION) {
			passwd = passwordField.getText();
			return true;
		} else {
			return false;
		}
	}

	public boolean promptPassphrase(String paramString) {
		return true;
	}

	public boolean promptYesNo(String message) {
		Object[] options = { "Yes", "No" };
		int value = JOptionPane.showOptionDialog(null, message, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		//System.out.println("promptYesNo" + value);
		return (0 == value);
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}	
}