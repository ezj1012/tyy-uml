package com.tyy.uml.gui.op.setting.mgr;

import java.awt.Color;
import java.awt.Font;

import com.tyy.uml.core.ctx.model.UMLProject;
import com.tyy.uml.gui.comm.TitleLabelPanel;
import com.tyy.uml.gui.comm.group.GroupItem;
import com.tyy.uml.util.SWUtils;

public class ProjectItem extends GroupItem {

    private static final long serialVersionUID = 1L;

    TitleLabelPanel label;

    UMLProject project;

    public static final int fixedHeight = 20;

    public ProjectItem(int fixedWidth, UMLProject project) {
        setLayout(null);
        setOpaque(false);
        this.project = project;
        SWUtils.fixed(this, fixedWidth, fixedHeight);
        // label = new JLabel(getText());
        label = new TitleLabelPanel(fixedHeight, Color.WHITE, 0, 10, 0, 0);
        label.setFont(Font.PLAIN, fixedHeight - 8);
        SWUtils.fixed(label, fixedWidth, fixedHeight);
        label.setText(getText());
        label.setBounds(0, 0, fixedWidth, fixedHeight);
        this.add(label);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public UMLProject getProject() {
        return project;
    }

    public String getText() {
        return project.getName();
    }

    @Override
    public void setForeground(Color fg) {
        if (label != null) {
            label.setForeground(fg);
        }
        super.setForeground(fg);
    }

    public int getFixedHeight() {
        return fixedHeight;
    }

}
