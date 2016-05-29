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

	private JComponent[] lLetter = new JComponent[7];
    private JLabel lrefl = new JLabel();

	private JComboBox<String> availRotors = new JComboBox<>();
	private String prevChoosen = "";

	private ArrayList<GUIRotorListener> rotorListeners = new ArrayList<>();
	
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
				lLetter[i].setFont(GUIEnigma.monoFont);
				add(lLetter[i], c);
			}
			lLetter[3].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		} else if (m instanceof Reflector) {
			lrefl.setText("Reflektor");
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
		
		availRotors.addActionListener(this);
		availRotors.setSelectedItem(conf);
		prevChoosen = (String)availRotors.getSelectedItem();
	}

	String getMapperConf() {
		if (self instanceof Rotor){
			JTextField middle = (JTextField) lLetter[3];
			return availRotors.getSelectedItem() + ":" + middle.getText().charAt(0);
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

	@Override
	public void actionPerformed(ActionEvent e) {
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
			prevChoosen = (String)availRotors.getSelectedItem();
		}
	}

    void addRotorListeners(ArrayList<GUIRotor> l){
        rotorListeners.addAll(l.stream().filter(rl -> rl != this).collect(Collectors.toList()));
	}
	
	JLabel getLrefl() { return lrefl; }

    private void fireRotorChange() {
		for (GUIRotorListener l : rotorListeners)
			l.rotorChange((String)availRotors.getSelectedItem(), this);
	}

	@Override
	public void rotorChange(String conf, GUIRotor otherRot) {
		if (availRotors.getSelectedItem().equals(conf)) {
			availRotors.setSelectedItem(otherRot.prevChoosen);
		}
	}
	
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