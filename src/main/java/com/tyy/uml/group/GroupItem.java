package com.tyy.uml.group;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.tyy.uml.comm.AbsMovable;

public class GroupItem extends AbsMovable {

    private static final long serialVersionUID = 1L;

    protected Group group;

    protected volatile boolean selected;

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public void mouseOneClicked(MouseEvent e) {
        // group.selectItem(this, e);
    }

    @Override
    public void mouseDblClicked(MouseEvent e, int c) {
        // group.selectItem(this, e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        group.selectItem(this, e);
    }

    public void unselect() {
        this.selected = false;
        repaint();
    }

    public void setSelected() {
        this.selected = true;
        this.requestFocus();
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected()) {
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
        }
    }

}
