package com.tyy.uml.gui.op.setting.ps;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.bean.UMLProjectData;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.core.gui.adapter.DMouseListener;
import com.tyy.uml.gui.comm.OperateLabelPanel;
import com.tyy.uml.gui.op.UMLOperatePanel;

public class ProjectSettings extends JPanel implements DMouseListener {

    private static final long serialVersionUID = 1L;

    public static final int fixedHeight = 30;

    OperateLabelPanel titlePanel;

    Ctrl ctrl;

    UMLProjectData project;

    public ProjectSettings(Ctrl ctrl) {
        this.ctrl = ctrl;
        setPreferredSize(new Dimension(300, UMLOperatePanel.fixedHeight));
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setOpaque(false);
        setLayout(new BorderLayout());
        titlePanel = new OperateLabelPanel(fixedHeight, JTextField.LEFT);
        titlePanel.addRightButton("close.png", 12, (btn, e) -> {
            ctrl.delProject(project);
        });
        titlePanel.addRightButton("check.png", 12, (btn, e) -> {
            ctrl.saveProject(project);
        });
        titlePanel.addRightButton("go-to-link.png", 12, (btn, e) -> {
            if (project != null) {
                ctrl.loadProject(project, false);
                ctrl.refreshProject();
            }
        });
        this.add(titlePanel, BorderLayout.NORTH);
        this.setVisible(false);
        this.addMouseListener(this);
    }

    public void refresData(UMLProjectData config) {
        this.setVisible(config != null);
        this.project = config;
        if (config == null) { return; }
        titlePanel.setText(config.getName());
    }

}
