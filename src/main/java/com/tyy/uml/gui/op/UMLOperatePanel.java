package com.tyy.uml.gui.op;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.ctx.Ctrl;
import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.util.SWUtils;
import com.tyy.uml.util.BeanHelper.BeanObservale;
import com.tyy.uml.util.BeanHelper.BeanObserver;

public class UMLOperatePanel extends JPanel implements BeanObserver, DComponentListener {

    private static final long serialVersionUID = 1L;

    // static final int fixedWidth = 500;

    public static final int fixedHeight = 300;

    Ctrl ctrl;

    UMLOperateTitle title;

    // UMLEditor editor;

    // UMLSettings settings;

    AbsUMLOperateMain curMain;

    public UMLOperatePanel(Ctrl ctrl) {
        this.ctrl = ctrl;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout());
        this.title = new UMLOperateTitle(ctrl);
        this.add(title, BorderLayout.NORTH);
    }

    public UMLOperateTitle getTitle() {
        return title;
    }

    public void refreshConfig(UMLGUIConfig cfg) {
        if (cfg instanceof BeanObservale) {
            ((BeanObservale) cfg).addObserver(this);
        }
        updateConfig(cfg, null, null);
    }

    private void updateConfig(UMLGUIConfig cfg, String prop, Object newValue) {
        if (prop == null || "editorBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCfg().getEditorBackColor(), UMLGUIConfig.c252526);
            setBackground(backColor);
        }
        if (prop == null || "editorTitleBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCfg().getEditorTitleBackColor(), UMLGUIConfig.c333333);
            title.setBackground(backColor);
        }
        if (prop == null || "editorFontColor".equals(prop)) {
            Color fontColor = SWUtils.decodeColor(ctrl.getCfg().getEditorFontColor(), UMLGUIConfig.cd4d4d4);
            title.setForeground(fontColor);
        }
    }

    @Override
    public void update(Observable o, Object source, String prop, Object oldValue, Object newValue) {
        if (source instanceof UMLGUIConfig) {
            updateConfig((UMLGUIConfig) source, prop, newValue);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        setCenter();
    }

    public void setCenter() {
        if (this.getParent() != null && curMain != null) {
            int wid = this.curMain.getWidth();
            Container parent = this.getParent();
            int x = (parent.getWidth() - wid) / 2;
            x = x < 0 ? 0 : x;
            int y = (parent.getHeight() - fixedHeight) - 20;
            y = y < 0 ? 0 : y;
            this.setBounds(x, y, wid, fixedHeight);
            this.repaint();
            this.getParent().repaint();
        }
    }

    public void showMe(AbsUMLOperateMain main) {
        if (curMain != main) {
            if (curMain != null) {
                this.remove(curMain);
            }
            this.add(curMain = main, BorderLayout.CENTER);
        }
        this.setCenter();
        this.setVisible(true);
    }

}
