package com.tyy.uml.gui.op.setting.mgr;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.tyy.uml.core.ctx.Ctrl;
import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.core.ctx.model.UMLProject;
import com.tyy.uml.core.ctx.model.UMLWork;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.gui.comm.group.GroupItem;
import com.tyy.uml.gui.comm.group.ListGroup;
import com.tyy.uml.util.BeanHelper.BeanObserver;
import com.tyy.uml.util.SWUtils;

public class ProjectList extends ListGroup implements BeanObserver, DComponentListener {

    private static final long serialVersionUID = 1L;

    private Ctrl ctrl;

    UMLWork workConfig;

    Color editorFontColor;

    Color editorCaretColor;

    public ProjectList(Ctrl ctrl, UMLWork workConfig) {
        this.ctrl = ctrl;
        this.workConfig = workConfig;
        this.refresh();
    }

    public void refresh() {
        List<UMLProject> projects = workConfig.getProjects();
        List<GroupItem> newItems = new ArrayList<>();
        for (UMLProject project : projects) {
            ProjectItem projectItem = new ProjectItem(ProjectManager.fixedWidth - 10, project);
            projectItem.setForeground(editorFontColor);
            newItems.add(projectItem);
        }
        this.replace(newItems);
    }

    public void updateConfig(UMLGUIConfig cfg, String prop, Object newValue) {
        if (prop == null || "editorBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCurProject().getConfig().getEditorBackColor(), UMLGUIConfig.c252526);
            this.umlScorllPanel.setBackground(backColor);
            this.umlScorllPanel.getVerticalScrollBar().setBackground(backColor);
            super.setBackground(backColor);
        }
        if (prop == null || "canvasThumbColor".equals(prop)) {
            vBarUI.setThumbColor(SWUtils.decodeColor(ctrl.getCurProject().getConfig().getCanvasThumbColor(), UMLGUIConfig.c424242));
        }
        if (prop == null || "canvasThumbRolloverColor".equals(prop)) {
            vBarUI.setThumbRolloverColor(SWUtils.decodeColor(ctrl.getCurProject().getConfig().getCanvasThumbRolloverColor(), UMLGUIConfig.c4F4F4F));
        }
        if (prop == null || "canvasThumbDraggingColor".equals(prop)) {
            vBarUI.setThumbDraggingColor(SWUtils.decodeColor(ctrl.getCurProject().getConfig().getCanvasThumbDraggingColor(), UMLGUIConfig.c5E5E5E));
        }
        if (prop == null || "editorFontColor".equals(prop)) {
            editorFontColor = SWUtils.decodeColor(ctrl.getCurProject().getConfig().getEditorFontColor(), UMLGUIConfig.cd4d4d4);
            for (GroupItem groupItem : getItems()) {
                groupItem.setForeground(editorFontColor);
            }
            super.setForeground(editorFontColor);
        }
    }

    @Override
    public void update(Observable o, Object source, String prop, Object oldValue, Object newValue) {
        if (source instanceof UMLGUIConfig) {
            updateConfig((UMLGUIConfig) source, prop, newValue);
        }
    }

}
