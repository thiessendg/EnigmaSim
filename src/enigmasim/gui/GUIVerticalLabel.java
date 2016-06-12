package enigmasim.gui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

class GUIVerticalLabel extends BasicLabelUI {

    static {
        labelUI = new GUIVerticalLabel(false);
    }

    private final boolean clockwise;
    private static final Rectangle PAINT_ICON_R = new Rectangle();
    private static final Rectangle PAINT_TEXT_R = new Rectangle();
    private static final Rectangle PAINT_VIEW_R = new Rectangle();
    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

    GUIVerticalLabel(boolean clockwise) {
        super();
        this.clockwise = clockwise;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension dim = super.getPreferredSize(c);
        return new Dimension(dim.height, dim.width);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        JLabel label = (JLabel) c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        if ((icon == null) && (text == null)) {
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        paintViewInsets = c.getInsets(paintViewInsets);

        PAINT_VIEW_R.x = paintViewInsets.left;
        PAINT_VIEW_R.y = paintViewInsets.top;

        // Use inverted height & width
        PAINT_VIEW_R.height = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
        PAINT_VIEW_R.width = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);

        PAINT_ICON_R.x = PAINT_ICON_R.y = PAINT_ICON_R.width = PAINT_ICON_R.height = 0;
        PAINT_TEXT_R.x = PAINT_TEXT_R.y = PAINT_TEXT_R.width = PAINT_TEXT_R.height = 0;

        String clippedText
                = layoutCL(label, fm, text, icon, PAINT_VIEW_R, PAINT_ICON_R, PAINT_TEXT_R);

        Graphics2D g2 = (Graphics2D) g;
        AffineTransform tr = g2.getTransform();
        if (clockwise) {
            g2.rotate(Math.PI / 2);
            g2.translate(0, -c.getWidth());
        } else {
            g2.rotate(-Math.PI / 2);
            g2.translate(-c.getHeight(), 0);
        }

        if (icon != null) {
            icon.paintIcon(c, g, PAINT_ICON_R.x, PAINT_ICON_R.y);
        }

        if (text != null) {
            int textX = PAINT_TEXT_R.x;
            int textY = PAINT_TEXT_R.y + fm.getAscent();

            if (label.isEnabled()) {
                paintEnabledText(label, g, clippedText, textX, textY);
            } else {
                paintDisabledText(label, g, clippedText, textX, textY);
            }
        }

        g2.setTransform(tr);
    }
}
