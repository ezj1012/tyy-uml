package com.tyy.uml.gui.op.setting;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import com.tyy.uml.core.ctx.model.UMLModel;
import com.tyy.uml.gui.op.UMLOperatePanel;

public class ProjectModels extends JPanel {

    private static final long serialVersionUID = 1L;

    public ProjectModels() {
        setPreferredSize(new Dimension(300, UMLOperatePanel.fixedHeight));
    }

    public void refresModels(List<UMLModel> models) {

    }

}
