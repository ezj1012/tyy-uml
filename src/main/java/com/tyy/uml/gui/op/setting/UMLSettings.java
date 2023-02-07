package com.tyy.uml.gui.op.setting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.border.EmptyBorder;

import com.tyy.uml.bean.BeanHelper.BeanObservale;
import com.tyy.uml.bean.BeanHelper.BeanObserver;
import com.tyy.uml.bean.UMLProjectData;
import com.tyy.uml.bean.UMLWork;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.gui.comm.group.GroupItem;
import com.tyy.uml.gui.comm.group.GroupSelectListener;
import com.tyy.uml.gui.op.AbsUMLOperateMain;
import com.tyy.uml.gui.op.UMLOperatePanel;
import com.tyy.uml.util.SWUtils;

public class UMLSettings extends AbsUMLOperateMain implements BeanObserver, GroupSelectListener {

    private static final long serialVersionUID = 1L;

    UMLWork workConfig;

    ProjectManager projectManager;

    UMLProjectData selectData;

    ProjectModels infoList;

    ProjectSettings settings;

    public UMLSettings(Ctrl ctrl, UMLOperatePanel operatePanel, UMLWork workConfig) {
        super(ctrl, operatePanel);
        this.workConfig = workConfig;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout());
        projectManager = new ProjectManager(ctrl, workConfig);
        this.add(projectManager, BorderLayout.CENTER);
        projectManager.addSelectListener(this);

        infoList = new ProjectModels();
        this.add(infoList, BorderLayout.WEST);
        infoList.setVisible(false);
        settings = new ProjectSettings();
        this.add(settings, BorderLayout.EAST);
        settings.setVisible(false);
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public void requestFocus() {
        this.projectManager.requestFocus();
    }

    public void refresh() {
        projectManager.refresh();
        this.repaint();
    }

    public void refreshConfig(UMLGUIConfig cfg) {
        if (cfg instanceof BeanObservale) {
            ((BeanObservale) cfg).addObserver(this);
        }
        updateConfig(cfg, null, null);
        projectManager.refreshConfig(cfg);
    }

    public void updateConfig(UMLGUIConfig cfg, String prop, Object newValue) {
        if (prop == null || "editorBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCfg().getEditorBackColor(), UMLGUIConfig.c252526);
            setBackground(backColor);
        }
    }

    @Override
    public void update(Observable o, Object source, String prop, Object oldValue, Object newValue) {
        if (source instanceof UMLGUIConfig) {
            updateConfig((UMLGUIConfig) source, prop, newValue);
        }
    }

    @Override
    public void selectedItem(GroupItem item) {
        ProjectItem pi = (ProjectItem) item;
        selectData = ctrl.loadProject(pi.getProject(), true);

        infoList.setVisible(true);
        infoList.refresModels(selectData.getModels());

        settings.setVisible(true);
        settings.refresData(selectData);

        refreshSize();
    }

    public void toggleEditorList() {
        if (!infoList.isVisible()) {
            infoList.refresModels(selectData == null ? new ArrayList<>() : selectData.getModels());
            infoList.setVisible(true);
        } else {
            infoList.setVisible(false);
        }
        refreshSize();
    }

}
