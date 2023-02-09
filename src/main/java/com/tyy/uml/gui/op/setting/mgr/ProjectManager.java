package com.tyy.uml.gui.op.setting.mgr;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.ctx.Ctrl;
import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.core.ctx.model.UMLWork;
import com.tyy.uml.gui.comm.group.GroupSelectListener;
import com.tyy.uml.util.BeanHelper.BeanObservale;

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
        projectList.addTo(e -> {
            this.add(e, BorderLayout.CENTER);
        });
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
