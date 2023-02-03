package com.tyy.uml.comm;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.tyy.uml.util.SWUtils;

public class UmlScrollBarUI extends BasicScrollBarUI {

    Image thumbImage;

    Image thumbImageHl;

    int thumbH = -1;

    boolean b = false;

    public UmlScrollBarUI() {
        trackColor = new Color(0, 0, 0, 255);
    }

    @Override
    public void layoutContainer(Container scrollbarContainer) {
        super.layoutContainer(scrollbarContainer);
    }

    @Override
    protected void configureScrollBarColors() {
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton d = new JButton();
        d.setBorder(SWUtils.EMPTY_BORDER);
        d.setOpaque(false);
        return d;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton d = new JButton();
        d.setBorder(SWUtils.EMPTY_BORDER);
        d.setOpaque(false);
        return d;
    }

    @Override
    protected void paintDecreaseHighlight(Graphics g) {
        super.paintDecreaseHighlight(g);
    }

    @Override
    protected void paintIncreaseHighlight(Graphics g) {
        super.paintIncreaseHighlight(g);
    }

    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) { return; }

        int w = thumbBounds.width;
        int h = thumbBounds.height;

        g.translate(thumbBounds.x, thumbBounds.y);

        // g.setColor(thumbDarkShadowColor);
        // drawRect(g, 0, 0, w - 1, h - 1);

        if (isDragging && thumbDraggingColor != null) {
            // 拖拽中
            g.setColor(thumbDraggingColor);
        } else if (isThumbRollover() && thumbRolloverColor != null) {
            // hoving
            g.setColor(thumbRolloverColor);
        } else {
            g.setColor(thumbColor);
        }

        g.fillRect(0, 0, w - 1, h - 1);
        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

    protected Color thumbRolloverColor;

    protected Color thumbDraggingColor;

    public void setThumbDraggingColor(Color thumbDraggingColor) {
        this.thumbDraggingColor = thumbDraggingColor;
    }

    public void setThumbRolloverColor(Color thumbRolloverColor) {
        this.thumbRolloverColor = thumbRolloverColor;
    }

    public void setThumbColor(Color color) {
        this.thumbColor = color;
    }

    public void setTrackColor(Color color) {
        this.trackColor = color;
    }

    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(c.getBackground());
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

        if (trackHighlight == DECREASE_HIGHLIGHT) {
            paintDecreaseHighlight(g);
        } else if (trackHighlight == INCREASE_HIGHLIGHT) {
            paintIncreaseHighlight(g);
        }
    }

}
