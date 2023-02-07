package com.tyy.uml.gui.op.setting;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.bean.UMLWork;
import com.tyy.uml.bean.BeanHelper.BeanObservale;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.gui.comm.group.GroupSelectListener;

public class ProjectManager extends JPanel {

    private static final long serialVersionUID = 1L;

    public static final int fixedWidth = 200;

    ProjectAdd add;

    ProjectList projectList;

    public ProjectManager(Ctrl ctrl, UMLWork workConfig) {
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout());
        setOpaque(false);
        add = new ProjectAdd(ctrl, workConfig);
        projectList = new ProjectList(ctrl, workConfig);
        add(add, BorderLayout.NORTH);
        projectList.addTo(this, BorderLayout.CENTER);

    }

    @Override
    public void requestFocus() {
        this.add.requestFocus();
    }

    public void refresh() {
        this.projectList.refresh();
        this.repaint();
    }

    public void refreshConfig(UMLGUIConfig cfg) {
        if (cfg instanceof BeanObservale) {
            ((BeanObservale) cfg).addObserver(projectList);
        }
        projectList.updateConfig(cfg, null, null);
    }

    public void addSelectListener(GroupSelectListener selectListener) {
        this.projectList.addSelectListener(selectListener);
    }

}
