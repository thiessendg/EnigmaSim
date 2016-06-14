package enigmasim.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * @author David Thiessen based on previous work by Daniel Boschofsky
 *
 */
@SuppressWarnings("serial")
class GUIButton extends JButton implements MouseListener {

    GUIButton() {
        super();
        addMouseListener(this);
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    GUIButton(String text) {
        super(text);
        addMouseListener(this);
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

}
