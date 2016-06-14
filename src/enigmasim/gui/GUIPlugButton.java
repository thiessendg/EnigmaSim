package enigmasim.gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;

/**
 * @author David Thiessen based on previous work by DI Franz Breunig
 * 
 */
@SuppressWarnings("serial")
class GUIPlugButton extends JToggleButton {
	
	GUIPlugButton(String text) {
		setText (text);
		addActionListener(e -> setSelected(isSelected()));
		setFont(new Font("Monospaced", Font.BOLD, 16));
		setPreferredSize(new Dimension(50,30));
		setSelected(isSelected());
	}

        @Override
	public void setSelected(boolean state) {
		super.setSelected (state);
		if (state) setBorder(BorderFactory.createLoweredBevelBorder());
		else setBorder(BorderFactory.createRaisedBevelBorder());
	}
}