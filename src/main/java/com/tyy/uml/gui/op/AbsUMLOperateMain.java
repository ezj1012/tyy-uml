package com.tyy.uml.gui.op;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.ctx.Ctrl;

public abstract class AbsUMLOperateMain extends JPanel {

    private static final long serialVersionUID = 1L;

    protected Ctrl ctrl;

    protected UMLOperateTitle title;

    protected UMLOperatePanel operatePanel;

    public AbsUMLOperateMain(Ctrl ctrl, UMLOperatePanel operatePanel) {
        this.ctrl = ctrl;
        this.operatePanel = operatePanel;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout());
    }

    public void showMe() {
        operatePanel.showMe(this);
        this.getTitle().setTitle("");
    }

    public void setTitle(String title) {
        this.getTitle().setTitle(title);
    }

    public UMLOperateTitle getTitle() {
        return this.operatePanel.getTitle();
    }

    public UMLOperatePanel getOperatePanel() {
        return operatePanel;
    }

    @Override
    public int getWidth() {
        int w = 0;
        for (Component component : getComponents()) {
            if (component.isVisible()) {
                w += component.getPreferredSize().getWidth();
            }
        }
        return w;
    }

    public void refreshSize() {
        if (operatePanel != null) {
            // int newWidth = getWidth();
            // Rectangle b = operatePanel.getBounds();
            // if (b.width == newWidth) { return; }
            // int ox = (b.width - newWidth) / 2;
            // b.x += ox;
            // b.x = b.x < 0 ? 1 : b.x;
            // operatePanel.setBounds(b.x, b.y, newWidth, b.height);
            operatePanel.setCenter();
        }
    }

}
