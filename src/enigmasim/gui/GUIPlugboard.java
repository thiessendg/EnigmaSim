package enigmasim.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * @author Daniel Boschofsky
 * 
 * ENIGMA_TEC 2010
 * technik[at]enigma-ausstellung.at
 * http://enigma-ausstellung.at
 * 
 * HTL Rennweg
 * Rennweg 89b
 * A-1030 Wien
 * 
 */
@SuppressWarnings("serial")
class GUIPlugboard extends JPanel implements ActionListener {
	private GUIPlugButton[] letters = new GUIPlugButton[26];
	private GUIPlugButton temp = null;

	private boolean clicked = false;

	private TreeMap<Character, Character> plug = new TreeMap<>();

	GUIPlugboard() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		for (int i = 0; i < letters.length; i++) {
			letters[i] = new GUIPlugButton(
					Character.toString(GUIEnigma.KEYBOARD.charAt(i)) + "\u2192" +
					Character.toString(GUIEnigma.KEYBOARD.charAt(i)));
			letters[i].setFont(GUIEnigma.monoFont);
			letters[i].setPreferredSize(new Dimension(40, 30));
			letters[i].addActionListener(this);
			//letters[i].setBorder(BorderFactory.createRaisedBevelBorder());
			plug.put(GUIEnigma.KEYBOARD.charAt(i), GUIEnigma.KEYBOARD.charAt(i));
		}

		JPanel line1 = new JPanel(new GridLayout(1, 0, 5, 0));
		JPanel line1a = new JPanel();
		JPanel line1b = new JPanel();
		line1a.setLayout(new BoxLayout(line1a, BoxLayout.LINE_AXIS));
		line1b.setLayout(new BoxLayout(line1b, BoxLayout.PAGE_AXIS));
		for (int i = 0; i < 10; i++)
			line1.add(letters[i]);
		/*
		 * WindowStateListener
		 * WindowStateChanged -> WindowEvent
		 * WindowEvent -> 
		 */
		//WindowEvent x = new WindowEvent(null, 1);

		line1b.add(Box.createVerticalGlue());
		line1a.add(Box.createHorizontalGlue());
		line1a.add(line1);
		line1a.add(Box.createHorizontalGlue());
		line1b.add(line1a);
		line1b.add(Box.createVerticalGlue());
		add (line1b);

		JPanel line2 = new JPanel(new GridLayout(1, 0, 5, 0));
		JPanel line2a = new JPanel();
		JPanel line2b = new JPanel();
		line2a.setLayout(new BoxLayout(line2a, BoxLayout.LINE_AXIS));
		line2b.setLayout(new BoxLayout(line2b, BoxLayout.PAGE_AXIS));
		for (int i = 10; i < 19; i++)
			line2.add(letters[i]);
		line2b.add(Box.createVerticalGlue());
		line2a.add(Box.createHorizontalGlue());
		line2a.add(line2);
		line2a.add(Box.createHorizontalGlue());
		line2b.add(line2a);
		line2b.add(Box.createVerticalGlue());
		add (line2b);


		JPanel line3 = new JPanel(new GridLayout(1, 0, 5, 0));
		JPanel line3a = new JPanel();
		JPanel line3b = new JPanel();
		line3a.setLayout(new BoxLayout(line3a, BoxLayout.LINE_AXIS));
		line3b.setLayout(new BoxLayout(line3b, BoxLayout.PAGE_AXIS));
		for (int i = 19; i < 26; i++)
			line3.add(letters[i]);
		line3b.add(Box.createVerticalGlue());
		line3a.add(Box.createHorizontalGlue());
		line3a.add(line3);
		line3a.add(Box.createHorizontalGlue());
		line3b.add(line3a);
		line3b.add(Box.createVerticalGlue());
		add (line3b);
		
		for (char c = 'A'; c <= 'Z'; c++)
			plug.put(c, c);
	}

	String getPlugSettings() {
		String ret = "";
		for (Character x : plug.values()) {
			ret += x;
		}
		return ret;
	}

	void setPlugSettings(String setting) {
		for (int i = 0; i < letters.length; i++) {
			letters[i].setSelected(false);
			char curr = letters[i].getText().charAt(0);
			int pos = GUIEnigma.LETTERS.indexOf(curr);
			letters[i].setText(curr + "\u2192" + setting.charAt(pos));
			plug.put(GUIEnigma.LETTERS.charAt(i), setting.charAt(i));
			if (letters[i].getText().charAt(0) != letters[i].getText().charAt(2))
				letters[i].setSelected(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < letters.length; i++) {
			if (e.getSource() == letters[i]) {
				if (clicked) {
					if (!letters[i].isSelected() && temp != letters[i]) {
						temp.setSelected(false);
						temp.setText(temp.getText().charAt(0) + "\u2192" + temp.getText().charAt(0));
						letters[i].setSelected(true);
						clicked = false;
						temp = null;
						return;
					}
					temp.setText(temp.getText().charAt(0) + "\u2192" +letters[i].getText().charAt(0));

					letters[i].setText(letters[i].getText().charAt(0) + "\u2192" +temp.getText().charAt(0));

					plug.put(temp.getText().charAt(0), letters[i].getText().charAt(0));
					plug.put(letters[i].getText().charAt(0), temp.getText().charAt(0));

					temp = null;
					clicked = false;
					break;
				} else {

					if (!letters[i].isSelected() && !clicked) {
						// Position des 2. elements
						int pos = GUIEnigma.KEYBOARD.indexOf(letters[i].getText().charAt(2));
						letters[pos].setSelected(false);
						letters[pos].setText(GUIEnigma.KEYBOARD.charAt(pos) + "\u2192" + GUIEnigma.KEYBOARD.charAt(pos));
						letters[i].setText(GUIEnigma.KEYBOARD.charAt(i) + "\u2192" + GUIEnigma.KEYBOARD.charAt(i));
						plug.put(GUIEnigma.KEYBOARD.charAt(pos), GUIEnigma.KEYBOARD.charAt(pos));
						plug.put(GUIEnigma.KEYBOARD.charAt(i), GUIEnigma.KEYBOARD.charAt(i));
						break;
					}
					temp = letters[i];
					temp.setText(GUIEnigma.KEYBOARD.charAt(i) + "\u2192?");

					clicked = true;
					break;
				}
			}// end if getsource
		}
	}

}
