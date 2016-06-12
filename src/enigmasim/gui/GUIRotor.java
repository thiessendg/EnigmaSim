package enigmasim.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import enigmasim.parts.Mapper;
import enigmasim.parts.Reflector;
import enigmasim.parts.Rotor;

/**
 * @author Daniel Boschofsky, Philip Wölfl
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
class GUIRotor extends JPanel implements ActionListener, GUIRotorListener, KeyListener {
	private final GUIButton BUP = new GUIButton ("\u2227");
	private final GUIButton BDOWN = new GUIButton ("\u2228");

	private final JComponent[] lLetter = new JComponent[7];
	private final JLabel lrefl = new JLabel();

	private final JComboBox<String> availRotors = new JComboBox<>();
	private String prevChosenRotor = "";

	private final JComboBox<String> availRotorOffset = new JComboBox<>();

	private final ArrayList<GUIRotorListener> rotorListeners = new ArrayList<>();

	private Mapper self = null;

	//FIXME: machinensettings kommen vom logicobject .> anpassen
	/**
	 * Erzeugt eine Walze je nach Typ
	 */
    GUIRotor(String[] rotors, Mapper m, String conf) {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());

		self = m;

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		add(availRotors, c);

		// Labels standardinitialisieren und auf das Panel setzen
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		if (m instanceof Rotor) {
			for (int i = 0; i < lLetter.length; i++, c.gridy++){
				if(i==lLetter.length/2){
					lLetter[i] = new JTextField(Character.toString(
							GUIEnigma.LETTERS.charAt(((i - lLetter.length/2) +26) % 26)));
					lLetter[i].addKeyListener(this);
				}
				else{
					lLetter[i] = new JLabel(Character.toString(
							GUIEnigma.LETTERS.charAt(((i - lLetter.length/2) +26) % 26)));
				}
				lLetter[i].setAlignmentX(JLabel.CENTER);
				lLetter[i].setFont(GUIEnigma.MONOFONT);
				add(lLetter[i], c);
			}

			//dgt: add offset as part of each rotor
			add(availRotorOffset,c);

			lLetter[3].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		} else if (m instanceof Reflector) {
			lrefl.setText("Reflector");
			GUIVerticalLabel lvrefl = new GUIVerticalLabel(false);
			lrefl.setUI(lvrefl);
			lrefl.setHorizontalAlignment(JLabel.CENTER);
			add(lrefl, c);
		}


		// TODO fertig denken
		// Buttons auf Panel setzen sofern nötig
		if (!(m instanceof Reflector)) {
			c.gridy = 3;
			c.gridx = 1;
			add(BUP, c);

			c.gridy = 5;
			add(BDOWN, c);

			BUP.addActionListener(this);
			BDOWN.addActionListener(this);
			BUP.setBorder(BorderFactory.createRaisedBevelBorder());
			BDOWN.setBorder(BorderFactory.createRaisedBevelBorder());
			BUP.setPreferredSize(new Dimension(22,22));
			BDOWN.setPreferredSize(new Dimension(22,22));
		}

		for (String r : rotors) {
            availRotors.addItem(r);
        }
		String[] offset = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
				"21", "22", "23", "24", "25", "26"};
		for (String o : offset) {
			availRotorOffset.addItem(o);
		}

		availRotors.addActionListener(this);
		availRotors.setSelectedItem(conf);
		prevChosenRotor = (String)availRotors.getSelectedItem();

		availRotorOffset.addActionListener(this);
		availRotorOffset.setSelectedItem(conf);
	}

	String getMapperConf() {
		//i believe this is return rotor and start position as I:A, II:A, III:A
		//i think i want to start returning I:A:1, II:A:1, III:A:1
		if (self instanceof Rotor){
			JTextField middle = (JTextField) lLetter[3];
			//return availRotors.getSelectedItem() + ":" + middle.getText().charAt(0);
			return availRotors.getSelectedItem() + ":" + middle.getText().charAt(0) +
					":" + availRotorOffset.getSelectedItem();
		}
		else return availRotors.getSelectedItem() + ": ";
	}

	void setLetter(char c) {
		for (int i = 0; i < lLetter.length; i++) {
			if(lLetter[i] instanceof JLabel){
				JLabel temp = (JLabel) lLetter[i];
				temp.setText(Character.toString(
						GUIEnigma.LETTERS.charAt((GUIEnigma.LETTERS.indexOf(c) +
								(i - lLetter.length/2) +26) % 26)));
			}
			else if(lLetter[i] instanceof JTextField){
				JTextField temp = (JTextField) lLetter[i];
				temp.setText(Character.toString(
						GUIEnigma.LETTERS.charAt((GUIEnigma.LETTERS.indexOf(c) +
								(i - lLetter.length/2) +26) % 26)));
			}

		}
	}

	JComboBox<String> getAvailRotors() {
		return availRotors;
	}

	//JComboBox<String> getAvailRotorOffset() {
	//	return availRotorOffset;
	//}

	@Override
	public void actionPerformed(ActionEvent e) {
		//dgt: I believe this is where th up and down arrows are used to
		// select the start position of each rotor. I don't think I have to do
		// this with offest because it is a ComboBox.
		if (e.getSource() == BUP) {
			JTextField middle = (JTextField) lLetter[3];
			String curr = middle.getText();
			for (int i = 0; i < lLetter.length; i++) {
				if(lLetter[i] instanceof JLabel){
					JLabel temp = (JLabel) lLetter[i];
					temp.setText(Character.toString(
						GUIEnigma.LETTERS.charAt((GUIEnigma.LETTERS.indexOf(curr) +
								(i - lLetter.length/2) +25) % 26)));
				}
				else if(lLetter[i] instanceof JTextField){
					JTextField temp = (JTextField) lLetter[i];
					temp.setText(Character.toString(
						GUIEnigma.LETTERS.charAt((GUIEnigma.LETTERS.indexOf(curr) +
								(i - lLetter.length/2) +25) % 26)));
				}
			}
		} else if (e.getSource() == BDOWN) {
			JTextField middle = (JTextField) lLetter[3];
			String curr = middle.getText();
			for (int i = 0; i < lLetter.length; i++) {
				if(lLetter[i] instanceof JLabel){
					JLabel temp = (JLabel) lLetter[i];
					temp.setText(Character.toString(
							GUIEnigma.LETTERS.charAt((GUIEnigma.LETTERS.indexOf(curr) +
									(i - lLetter.length/2) +27) % 26)));
				}
				else if(lLetter[i] instanceof JTextField){
					JTextField temp = (JTextField) lLetter[i];
					temp.setText(Character.toString(
							GUIEnigma.LETTERS.charAt((GUIEnigma.LETTERS.indexOf(curr) +
									(i - lLetter.length/2) +27) % 26)));
				}
			}
		} else if (e.getSource() == availRotors) {
			fireRotorChange();
			prevChosenRotor = (String)availRotors.getSelectedItem();
		}
	}

    void addRotorListeners(ArrayList<GUIRotor> l){
        rotorListeners.addAll(l.stream().filter(rl -> rl != this).collect(Collectors.toList()));
	}

	JLabel getLrefl() { return lrefl; }

    private void fireRotorChange() {
        rotorListeners.stream().forEach((l) -> {
            l.rotorChange((String)availRotors.getSelectedItem(), this);
            });
	}

	@Override
	public void rotorChange(String conf, GUIRotor otherRot) {
		if (availRotors.getSelectedItem().equals(conf)) {
			availRotors.setSelectedItem(otherRot.prevChosenRotor);
		}
	}

	//dgt: do i need an offsetchange method?

	Mapper getMapper() {
		return self;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		e.consume();
		char input = Character.toUpperCase(e.getKeyChar());
		if(GUIEnigma.LETTERS.contains(input+"")){
			((JTextField)lLetter[3]).setText(input+"");
			setLetter(input);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		e.consume();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		e.consume();
	}


}