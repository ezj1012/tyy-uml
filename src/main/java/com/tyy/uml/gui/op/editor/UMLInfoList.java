package com.tyy.uml.gui.op.editor;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.tyy.uml.gui.canvas.UMLInfoPanel;
import com.tyy.uml.gui.op.UMLOperatePanel;

public class UMLInfoList extends JPanel {

    private static final long serialVersionUID = 1L;

    public UMLInfoList() {
        setPreferredSize(new Dimension(300, UMLOperatePanel.fixedHeight));
    }

    public void refresModel(UMLInfoPanel umlInfoPanel) {

    }

}
