package com.tyy.uml.gui.canvas;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.gui.canvas.elements.UMLInfoPanel;
import com.tyy.uml.gui.comm.UmlScrollBarUI;
import com.tyy.uml.gui.comm.group.Group;
import com.tyy.uml.util.SWUtils;

public abstract class AbsScrollCanvas extends Group {

    private static final long serialVersionUID = 1L;

    protected JScrollPane scorllPanel;

    protected UmlScrollBarUI hBarUI = new UmlScrollBarUI();

    protected UmlScrollBarUI vBarUI = new UmlScrollBarUI();

    public AbsScrollCanvas() {
        initScroll();
    }

    protected void initScroll() {
        scorllPanel = new JScrollPane(this);
        scorllPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        scorllPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scorllPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar hBar = scorllPanel.getHorizontalScrollBar();
        JScrollBar vBar = scorllPanel.getVerticalScrollBar();
        hBar.setUI(hBarUI);
        vBar.setUI(vBarUI);
        SWUtils.fixedHeight(hBar, 10);
        SWUtils.fixedWidth(vBar, 10);

        SWUtils.printSize("hBar", hBar);
        SWUtils.printSize("vBar", vBar);
    }

    public void addTo(Consumer<JScrollPane> a) {
        a.accept(scorllPanel);
    }

    public void addTo(JComponent parent, String center) {
        parent.add(scorllPanel, center);
    }

    public JScrollPane getUmlScorllPanel() {
        return scorllPanel;
    }

    public void setCenter() {
        if (this.scorllPanel == null) { return; }
        setCenter(this.scorllPanel.getHorizontalScrollBar(), this.scorllPanel.getWidth() - this.scorllPanel.getVerticalScrollBar().getPreferredSize().width);
        setCenter(this.scorllPanel.getVerticalScrollBar(), this.scorllPanel.getHeight() - this.scorllPanel.getHorizontalScrollBar().getPreferredSize().height);
    }

    private int setCenter(JScrollBar bar, int s) {
        int v = (bar.getMaximum() - s) / 2;
        bar.setValue(v);
        return v;
    }

    public void setCenter(UMLInfoPanel info) {
        // 水平 居中
        JScrollBar bar = this.scorllPanel.getHorizontalScrollBar();
        int hv = (int) (info.getSize().getWidth() / 2 + info.getX()) - (this.scorllPanel.getWidth() - this.scorllPanel.getVerticalScrollBar().getPreferredSize().width) / 2;
        if (hv > 0 && hv < bar.getMaximum()) {
            bar.setValue(hv);
        }

        // 垂直 居中
        bar = this.scorllPanel.getVerticalScrollBar();
        int vv = info.getY() - 50;
        if (vv > 0 && vv < bar.getMaximum()) {
            bar.setValue(vv);
        }
    }

    public void setScorllSize(int w, int h) {
        scorllPanel.setSize(w, h);
    }

    @Override
    public void setBackground(Color backColor) {
        if (scorllPanel != null) {
            scorllPanel.setBackground(backColor);
            this.scorllPanel.getHorizontalScrollBar().setBackground(backColor);
            this.scorllPanel.getVerticalScrollBar().setBackground(backColor);
        }
        super.setBackground(backColor);
    }

    public void setThumbColor(Color thumbColor) {
        hBarUI.setThumbColor(thumbColor);
        vBarUI.setThumbColor(thumbColor);
    }

    public void setThumbRolloverColor(Color thumbRolloverColor) {
        hBarUI.setThumbRolloverColor(thumbRolloverColor);
        vBarUI.setThumbRolloverColor(thumbRolloverColor);
    }

    public void setThumbDraggingColor(Color thumbDraggingColor) {
        hBarUI.setThumbDraggingColor(thumbDraggingColor);
        vBarUI.setThumbDraggingColor(thumbDraggingColor);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (pressedButton == MouseEvent.BUTTON3) {
            int x = e.getX() - pressedX;
            int y = e.getY() - pressedY;
            if (x != 0) {
                JScrollBar bar = this.scorllPanel.getHorizontalScrollBar();
                int value = bar.getValue();
                bar.setValue(value - x);
            }
            if (y != 0) {
                JScrollBar bar = this.scorllPanel.getVerticalScrollBar();
                int value = bar.getValue();
                bar.setValue(value - y);
            }
        }
    }

}
