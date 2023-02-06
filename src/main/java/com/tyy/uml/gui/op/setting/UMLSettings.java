package com.tyy.uml.gui.op.setting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Observable;

import javax.swing.border.EmptyBorder;

import com.tyy.uml.bean.BeanHelper.BeanObservale;
import com.tyy.uml.bean.BeanHelper.BeanObserver;
import com.tyy.uml.bean.UMLWork;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.gui.op.AbsUMLOperateMain;
import com.tyy.uml.gui.op.UMLOperatePanel;
import com.tyy.uml.util.SWUtils;

public class UMLSettings extends AbsUMLOperateMain implements BeanObserver {

    private static final long serialVersionUID = 1L;

    UMLWork workConfig;

    ProjectList projectList;

    public UMLSettings(Ctrl ctrl, UMLOperatePanel operatePanel, UMLWork workConfig) {
        super(ctrl, operatePanel);
        this.workConfig = workConfig;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout());
        projectList = new ProjectList(ctrl, workConfig);
        projectList.addTo(this, BorderLayout.CENTER);
    }

    @Override
    public int getWidth() {
        // return 600;
        return super.getWidth();
    }

    public void toggleEditorList() {
        refreshSize();
    }

    public void refresh() {
        projectList.refresh();
    }

    public void refreshConfig(UMLGUIConfig cfg) {
        if (cfg instanceof BeanObservale) {
            ((BeanObservale) cfg).addObserver(this);
        }
        updateConfig(cfg, null, null);
    }

    public void updateConfig(UMLGUIConfig cfg, String prop, Object newValue) {
        if (prop == null || "editorBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCfg().getEditorBackColor(), UMLGUIConfig.c252526);
            setBackground(backColor);
            projectList.setBackground(backColor);
        }
        if (prop == null || "canvasThumbColor".equals(prop)) {
            this.projectList.setThumbColor(SWUtils.decodeColor(ctrl.getCfg().getCanvasThumbColor(), UMLGUIConfig.c424242));
        }
        if (prop == null || "canvasThumbRolloverColor".equals(prop)) {
            this.projectList.setThumbRolloverColor(SWUtils.decodeColor(ctrl.getCfg().getCanvasThumbRolloverColor(), UMLGUIConfig.c4F4F4F));
        }
        if (prop == null || "canvasThumbDraggingColor".equals(prop)) {
            this.projectList.setThumbDraggingColor(SWUtils.decodeColor(ctrl.getCfg().getCanvasThumbDraggingColor(), UMLGUIConfig.c5E5E5E));
        }
    }

    @Override
    public void update(Observable o, Object source, String prop, Object oldValue, Object newValue) {
        if (source instanceof UMLGUIConfig) {
            updateConfig((UMLGUIConfig) source, prop, newValue);
        }
        // else if (source instanceof UMLModel) {
        // updateModel((UMLModel) source, prop, newValue);
        // }
    }

}
