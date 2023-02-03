package com.tyy.uml.canvas;

import java.awt.Color;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.comm.UmlScrollBarUI;
import com.tyy.uml.info.UMLInfoPanel;
import com.tyy.uml.util.SWUtils;

public class UMLScrollHelper {

    JScrollPane umlScorllPanel;

    UmlScrollBarUI hBarUI = new UmlScrollBarUI();

    UmlScrollBarUI vBarUI = new UmlScrollBarUI();

    public UMLScrollHelper(UMLCanvas umlPanel) {
        umlScorllPanel = new JScrollPane(umlPanel);
        umlScorllPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        umlScorllPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        umlScorllPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar hBar = umlScorllPanel.getHorizontalScrollBar();
        JScrollBar vBar = umlScorllPanel.getVerticalScrollBar();
        hBar.setUI(hBarUI);
        vBar.setUI(vBarUI);
        SWUtils.fixedHeight(hBar, 10);
        SWUtils.fixedWidth(vBar, 10);

        SWUtils.printSize("hBar", hBar);
        SWUtils.printSize("vBar", vBar);
        umlPanel.setScroll(this);
    }

    public JScrollPane getUmlScorllPanel() {
        return umlScorllPanel;
    }

    public void setCenter() {
        if (this.umlScorllPanel == null) { return; }
        setCenter(this.umlScorllPanel.getHorizontalScrollBar(), this.umlScorllPanel.getWidth() - this.umlScorllPanel.getVerticalScrollBar().getPreferredSize().width);
        setCenter(this.umlScorllPanel.getVerticalScrollBar(), this.umlScorllPanel.getHeight() - this.umlScorllPanel.getHorizontalScrollBar().getPreferredSize().height);
    }

    private int setCenter(JScrollBar bar, int s) {
        int v = (bar.getMaximum() - s) / 2;
        bar.setValue(v);
        return v;
    }

    public void setCenter(UMLInfoPanel info) {
        // 水平 居中
        JScrollBar bar = this.umlScorllPanel.getHorizontalScrollBar();
        int hv = (int) (info.getSize().getWidth() / 2 + info.getX()) - (this.umlScorllPanel.getWidth() - this.umlScorllPanel.getVerticalScrollBar().getPreferredSize().width) / 2;
        if (hv > 0 && hv < bar.getMaximum()) {
            bar.setValue(hv);
        }

        // 垂直 居中
        bar = this.umlScorllPanel.getVerticalScrollBar();
        int vv = info.getY() - 50;
        if (vv > 0 && vv < bar.getMaximum()) {
            bar.setValue(vv);
        }
    }

    public void setSize(int w, int h) {
        umlScorllPanel.setSize(w, h);
    }

    public void setBackground(Color backColor) {
        umlScorllPanel.setBackground(backColor);
        this.umlScorllPanel.getHorizontalScrollBar().setBackground(backColor);
        this.umlScorllPanel.getVerticalScrollBar().setBackground(backColor);
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

}
