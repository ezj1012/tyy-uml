package com.tyy.uml.gui.editor;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.bean.BeanHelper.BeanObserver;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.core.gui.adapter.DComponentListener;

public class UMLOperatePanel extends JPanel implements BeanObserver, DComponentListener {

    private static final long serialVersionUID = 1L;

    // static final int fixedWidth = 500;

    static final int fixedHeight = 300;

    UMLEditorTitle umlTitle;

    private Ctrl ctrl;

    public UMLOperatePanel(Ctrl ctrl) {
        this.ctrl = ctrl;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout());
        this.umlTitle = new UMLEditorTitle(ctrl, this);
    }

    @Override
    public void update(Observable o, Object source, String prop, Object oldValue, Object newValue) {

    }

}
