package com.tyy.uml.gui.op.setting;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.bean.UMLProjectData;
import com.tyy.uml.gui.op.UMLOperatePanel;

public class ProjectSettings extends JPanel {

    private static final long serialVersionUID = 1L;

    public ProjectSettings() {
        setPreferredSize(new Dimension(300, UMLOperatePanel.fixedHeight));
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setOpaque(false);
    }

    public void refresData(UMLProjectData config) {
        System.out.println(config);
    }

}
