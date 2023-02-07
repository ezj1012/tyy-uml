package com.tyy.uml.gui.op.setting.mgr;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.bean.BeanHelper.BeanObserver;
import com.tyy.uml.bean.UMLProject;
import com.tyy.uml.bean.UMLWork;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.gui.comm.UmlScrollBarUI;
import com.tyy.uml.gui.comm.group.Group;
import com.tyy.uml.gui.comm.group.GroupItem;
import com.tyy.uml.gui.op.UMLOperatePanel;
import com.tyy.uml.util.SWUtils;

public class ProjectList extends Group implements BeanObserver, DComponentListener {

    private static final long serialVersionUID = 1L;

    private Ctrl ctrl;

    JScrollPane umlScorllPanel;

    UmlScrollBarUI vBarUI = new UmlScrollBarUI();

    UMLWork workConfig;

    Color editorFontColor;

    Color editorCaretColor;

    public ProjectList(Ctrl ctrl, UMLWork workConfig) {
        this.ctrl = ctrl;
        this.workConfig = workConfig;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initScorllPanel();
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

    private void initScorllPanel() {
        umlScorllPanel = new JScrollPane(this);
        umlScorllPanel.setOpaque(false);
        SWUtils.fixedAndNoBorder(umlScorllPanel, ProjectManager.fixedWidth, UMLOperatePanel.fixedHeight);
        umlScorllPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        umlScorllPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar vBar = umlScorllPanel.getVerticalScrollBar();
        vBar.setUI(vBarUI);
        SWUtils.fixedWidth(vBar, 10);
        umlScorllPanel.addComponentListener(this);
    }

    public void addTo(JComponent parent, String center) {
        parent.add(umlScorllPanel, center);
    }

    public void updateConfig(UMLGUIConfig cfg, String prop, Object newValue) {
        if (prop == null || "editorBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCfg().getEditorBackColor(), UMLGUIConfig.c252526);
            this.umlScorllPanel.setBackground(backColor);
            this.umlScorllPanel.getVerticalScrollBar().setBackground(backColor);
            super.setBackground(backColor);
        }
        if (prop == null || "canvasThumbColor".equals(prop)) {
            vBarUI.setThumbColor(SWUtils.decodeColor(ctrl.getCfg().getCanvasThumbColor(), UMLGUIConfig.c424242));
        }
        if (prop == null || "canvasThumbRolloverColor".equals(prop)) {
            vBarUI.setThumbRolloverColor(SWUtils.decodeColor(ctrl.getCfg().getCanvasThumbRolloverColor(), UMLGUIConfig.c4F4F4F));
        }
        if (prop == null || "canvasThumbDraggingColor".equals(prop)) {
            vBarUI.setThumbDraggingColor(SWUtils.decodeColor(ctrl.getCfg().getCanvasThumbDraggingColor(), UMLGUIConfig.c5E5E5E));
        }
        if (prop == null || "editorFontColor".equals(prop)) {
            editorFontColor = SWUtils.decodeColor(ctrl.getCfg().getEditorFontColor(), UMLGUIConfig.cd4d4d4);
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
