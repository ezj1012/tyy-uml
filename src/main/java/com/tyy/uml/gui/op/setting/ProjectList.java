package com.tyy.uml.gui.op.setting;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.bean.UMLWork;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.gui.comm.UmlScrollBarUI;
import com.tyy.uml.gui.comm.group.Group;
import com.tyy.uml.gui.op.UMLOperatePanel;
import com.tyy.uml.util.SWUtils;

public class ProjectList extends Group {

    private static final long serialVersionUID = 1L;

    private Ctrl ctrl;

    JScrollPane umlScorllPanel;

    UmlScrollBarUI vBarUI = new UmlScrollBarUI();

    UMLWork workConfig;

    public ProjectList(Ctrl ctrl, UMLWork workConfig) {
        this.ctrl = ctrl;
        this.workConfig = workConfig;
        initScorllPanel();
    }

    public void refresh() {

    }

    private void initScorllPanel() {
        umlScorllPanel = new JScrollPane(this);
        SWUtils.fixedAndNoBorder(umlScorllPanel, 400, UMLOperatePanel.fixedHeight);
        umlScorllPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        // umlScorllPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        umlScorllPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // JScrollBar hBar = umlScorllPanel.getHorizontalScrollBar();
        JScrollBar vBar = umlScorllPanel.getVerticalScrollBar();
        // hBar.setUI(hBarUI);
        vBar.setUI(vBarUI);
        // SWUtils.fixedHeight(hBar, 10);
        SWUtils.fixedWidth(vBar, 10);
    }

    public void addTo(JComponent parent, String center) {
        parent.add(umlScorllPanel, center);
    }

    @Override
    public void setBackground(Color backColor) {
        if (this.umlScorllPanel != null) {
            this.umlScorllPanel.setBackground(backColor);
            this.umlScorllPanel.getHorizontalScrollBar().setBackground(backColor);
            // this.umlScorllPanel.getVerticalScrollBar().setBackground(Color.RED);
        }
        super.setBackground(backColor);
    }

    public void setThumbColor(Color thumbColor) {
        vBarUI.setThumbColor(thumbColor);
        // vBarUI.setThumbColor(Color.RED);
    }

    public void setThumbRolloverColor(Color thumbRolloverColor) {
        vBarUI.setThumbRolloverColor(thumbRolloverColor);
    }

    public void setThumbDraggingColor(Color thumbDraggingColor) {
        vBarUI.setThumbDraggingColor(thumbDraggingColor);
    }

}
