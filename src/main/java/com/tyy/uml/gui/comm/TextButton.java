package com.tyy.uml.gui.comm;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import com.tyy.uml.util.SWUtils;

public class TextButton extends JButton {

    private static final long serialVersionUID = 1L;

    private static final int AW = 16;

    private Color rolloverBackgroupColor;

    private Color pressedBackgroupColor;

    private Color selectedBackgroupColor;

    public TextButton(String text) {
        this(text, null);
    }

    public TextButton(String text, Integer fontSize) {
        this.setText(text);
        if (fontSize != null) {
            SWUtils.setFontSize(this, fontSize);
        }
        FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
        Rectangle2D stringBounds = fontMetrics.getStringBounds(text, getGraphics());
        int w = (int) stringBounds.getWidth() + AW;
        int h = (int) stringBounds.getHeight() + 4;
        SWUtils.fixed(this, w, h);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setUI(new BtnUI());
    }

    @Override
    public int getWidth() {
        int width2 = super.getWidth();
        return width2;
    }

    public int getHeight() {
        int height2 = super.getHeight();
        return height2;
    };

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
    }

    public class BtnUI extends BasicButtonUI {

        int offx;

        protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
            iconRect.x = 0;
            iconRect.y = 0;
            SingleColorIconButton b = (SingleColorIconButton) c;
            ButtonModel model = b.getModel();
            Icon icon = b.getIcon();
            Icon tmpIcon = null;

            if (icon == null) { return; }

            int w = b.getPreferredSize().width;
            int h = b.getPreferredSize().height;

            Icon selectedIcon = null;

            /* the fallback icon should be based on the selected state */
            if (model.isSelected()) {
                selectedIcon = b.getSelectedIcon();
                if (selectedIcon != null) {
                    icon = selectedIcon;
                }
            }

            if (!model.isEnabled()) {
                if (model.isSelected()) {
                    tmpIcon = b.getDisabledSelectedIcon();
                    if (tmpIcon == null) {
                        tmpIcon = selectedIcon;
                    }
                }

                if (tmpIcon == null) {
                    tmpIcon = b.getDisabledIcon();
                }
            } else if (model.isPressed() && model.isArmed()) {
                tmpIcon = b.getPressedIcon();
                if (tmpIcon != null) {
                    // revert back to 0 offset
                    clearTextShiftOffset();
                }

                Color temp = pressedBackgroupColor != null ? pressedBackgroupColor : rolloverBackgroupColor != null ? rolloverBackgroupColor : null;
                if (temp != null) {
                    g.setColor(temp);
                    g.fillRect(0, 0, w, h);
                }
            } else if (b.isRolloverEnabled() && model.isRollover()) {
                if (model.isSelected()) {
                    tmpIcon = b.getRolloverSelectedIcon();
                    if (tmpIcon == null) {
                        tmpIcon = selectedIcon;
                    }
                }

                if (tmpIcon == null) {
                    tmpIcon = b.getRolloverIcon();
                }
                if (rolloverBackgroupColor != null) {
                    g.setColor(rolloverBackgroupColor);
                    g.fillRect(0, 0, w, h);
                }
            }

            if (b.isSelected()) {
                Color temp = selectedBackgroupColor != null ? selectedBackgroupColor : pressedBackgroupColor != null ? pressedBackgroupColor : rolloverBackgroupColor != null ? rolloverBackgroupColor : null;
                if (temp != null) {
                    g.setColor(temp);
                    g.fillRect(0, 0, w, h);
                }
            }

            if (tmpIcon != null) {
                icon = tmpIcon;
            }

            iconRect.x = (w - icon.getIconWidth()) / 2;
            iconRect.y = (h - icon.getIconHeight()) / 2;

            if (model.isPressed() && model.isArmed()) {
                icon.paintIcon(c, g, iconRect.x + getTextShiftOffset(), iconRect.y + getTextShiftOffset());
            } else {
                icon.paintIcon(c, g, iconRect.x, iconRect.y);
            }

        }

        protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
            TextButton b = (TextButton) c;
            Color bg = getBackground();
            Color fc = b.getForeground();
            if (b.getModel().isRollover()) {
                bg = bg.brighter();
            } else {
                fc = fc.darker();
            }
            g.setColor(bg);
            g.fillRect(0, 0, getWidth(), getHeight());

            FontMetrics fm = g.getFontMetrics();
            g.setColor(fc);
            g.drawString(b.getText(), AW / 2, fm.getAscent());
        }

    }

}
