package com.tyy.uml.comm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class SingleColorIconButton extends JButton {

    private static final long serialVersionUID = 1L;

    SingleColorIcon iconImg;

    private static final Color defaultColor = new Color(204, 204, 204);
    // private Color selectedColor;

    private Color rolloverBackgroupColor;

    private Color pressedBackgroupColor;

    private Color selectedBackgroupColor;

    public SingleColorIconButton(int w, int h, String img) {
        this(w, h, new SingleColorIcon(img, w, h));
    }

    public SingleColorIconButton(int w, int h, SingleColorIcon img, Color color) {
        this.iconImg = img;
        this.setIcon(img.getImageIcon(color));
        this.setRolloverIcon(img.getImageIcon(img.getW() + 2, img.getH() + 2, color));
        this.setSelectedIcon(img.getImageIcon(color));
        this.setSize(w, h);
        this.setMaximumSize(new Dimension(w, h));
        this.setMinimumSize(new Dimension(w, h));
        this.setPreferredSize(new Dimension(w, h));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setBorderPainted(false);

        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setUI(new BtnUI());
    }

    public SingleColorIconButton(int w, int h, SingleColorIcon img) {
        this(w, h, img, defaultColor);
    }

    public void setRolloverBackgroupColor(Color rolloverBackgroupColor) {
        this.rolloverBackgroupColor = rolloverBackgroupColor;
    }

    public void setPressedBackgroupColor(Color pressedBackgroupColor) {
        this.pressedBackgroupColor = pressedBackgroupColor;
    }

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

            // SingleColorIconButton b = (SingleColorIconButton) c;
            // ButtonModel model = b.getModel();
            // FontMetrics fm = g.getFontMetrics();
            // int mnemonicIndex = b.getDisplayedMnemonicIndex();
            //
            // int maxW = c.getWidth() - 6;
            // String a = ((AbstractButton) c).getText();
            // if (a == null) { return; }
            // if (btnText == null || !a.equals(btnText)) {
            // btnText = a;
            // drawText = "";
            // for (int i = 0; i < a.length(); i++) {
            // String t = drawText + a.charAt(i);
            // if (SwingUtilities2.stringWidth(c, fm, t) > maxW) {
            // break;
            // } else {
            // drawText += a.charAt(i);
            // }
            // }
            // offx = (c.getWidth() - SwingUtilities2.stringWidth(c, fm, text)) / 2;
            // }
            // text = drawText;
            // textRect.x = 0;
            // if (model.isEnabled()) {
            // g.setColor(b.getForeground());
            // if (model.isPressed() && model.isArmed()) {
            // SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, textRect.x + offx, textRect.y + fm.getAscent() + getTextShiftOffset() + 1);
            // } else {
            // SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, textRect.x + offx - 1, textRect.y + fm.getAscent() + getTextShiftOffset());
            // }
            //
            // } else {
            // /*** paint the text disabled ***/
            // g.setColor(b.getBackground().brighter());
            // SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, textRect.x + offx, textRect.y + fm.getAscent());
            // g.setColor(b.getBackground().darker());
            // SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, textRect.x + offx - 1, textRect.y + fm.getAscent() - 1);
            // }
        }

    }

}
