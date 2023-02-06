package com.tyy.uml.gui.frame;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JLayeredPane;

import com.tyy.uml.bean.BeanHelper;
import com.tyy.uml.bean.BeanHelper.BeanObservale;
import com.tyy.uml.bean.UMLModel;
import com.tyy.uml.bean.UMLProject;
import com.tyy.uml.bean.UMLProjectData;
import com.tyy.uml.bean.UMLWork;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.context.UMLContext;
import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.core.gui.adapter.DKeyListener;
import com.tyy.uml.gui.canvas.UMLCanvas;
import com.tyy.uml.gui.canvas.UMLInfoPanel;
import com.tyy.uml.gui.canvas.UMLScrollHelper;
import com.tyy.uml.gui.comm.group.GroupItem;
import com.tyy.uml.gui.op.UMLOperatePanel;
import com.tyy.uml.gui.op.editor.UMLEditor;
import com.tyy.uml.gui.op.setting.UMLSettings;
import com.tyy.uml.util.SystemUtils;

public class UMLMainPane extends JLayeredPane implements DComponentListener, Ctrl, DKeyListener {

    private static final long serialVersionUID = 1L;

    UMLFrame frame;

    UMLCanvas canvasPanel;

    UMLScrollHelper umlScorllPanel;

    UMLOperatePanel operatePanel;

    UMLEditor editor;

    UMLSettings settings;

    UMLWork workConfig;

    UMLProjectData projectData;

    public UMLMainPane(UMLFrame frame, UMLWork workConfig) {
        this.frame = frame;
        this.workConfig = workConfig;
        if (workConfig.getProjects().size() == 0) {
            createProject(new UMLProject("", randFile()));
        }
        loadProject(workConfig.getProjects().get(0), false);
        initCanvas();
        initOperatePanel();

        //
        this.setPreferredSize(new Dimension(workConfig.getConfig().getFrameWidth(), workConfig.getConfig().getFrameHeight()));
        this.addKeyListener(this);
        this.addComponentListener(this);

        this.refreshProject();
    }

    public void initOperatePanel() {
        operatePanel = new UMLOperatePanel(this);
        this.addComponentListener(operatePanel);
        this.add(operatePanel, JLayeredPane.PALETTE_LAYER);
        operatePanel.addKeyListener(this);
        operatePanel.setVisible(false);

        this.editor = new UMLEditor(this, operatePanel);
        this.settings = new UMLSettings(this, operatePanel, workConfig);
    }

    public void initCanvas() {
        canvasPanel = new UMLCanvas(this);
        canvasPanel.addKeyListener(this);
        umlScorllPanel = new UMLScrollHelper(canvasPanel);
        this.add(umlScorllPanel.getUmlScorllPanel(), JLayeredPane.DEFAULT_LAYER);
    }

    @Override
    public void saveConfigs() {
        UMLContext.getContext().saveConfigs();
    }

    @Override
    public void createProject(UMLProject project) {
        workConfig.getProjects().add(project);
        this.saveConfigs();
        UMLProjectData converBean = SystemUtils.converBean(project, UMLProjectData.class);
        saveProject(converBean);
    }

    @Override
    public UMLProjectData loadProject(UMLProject project, boolean peek) {
        if (projectData != null && projectData.getConfig() instanceof BeanObservale) {
            if (!peek) {
                ((BeanObservale) projectData.getConfig()).deleteObservers();
            }
        }

        if (SystemUtils.isEmpty(project.getPath())) {
            project.setPath(randFile());
        }
        File f = new File(project.getPath());
        UMLProjectData data = null;
        if (f.exists()) {
            try {
                data = SystemUtils.readFile(new File(project.getPath()), UMLProjectData.class);
            } catch (IOException e) {
            }
        }
        if (data == null) {
            data = SystemUtils.converBean(project, UMLProjectData.class);
            try {
                SystemUtils.writeFile(f, data);
            } catch (Exception e) {
            }
        }
        UMLGUIConfig config = data.getConfig();
        data.setConfig(BeanHelper.proxy(config));
        if (!peek) {
            this.projectData = data;
        }
        return data;
    }

    @Override
    public UMLProjectData saveProject(UMLProjectData project) {
        if (SystemUtils.isEmpty(project.getPath())) {
            project.setPath(randFile());
        }

        List<UMLModel> models = project.getModels();
        if (models == null) {
            models = new ArrayList<UMLModel>();
            project.setModels(models);
        }

        if (projectData == project) {
            models.clear();
            if (this.canvasPanel != null) {
                for (GroupItem groupItem : this.canvasPanel.getItems()) {
                    if (groupItem instanceof UMLInfoPanel) {
                        UMLModel model = ((UMLInfoPanel) groupItem).getModel();
                        models.add(model);
                    }
                }
            }
        }

        if (project.getConfig() == null) {
            project.setConfig(new UMLGUIConfig());
        }

        File f = new File(project.getPath());
        try {
            SystemUtils.writeFile(f, project);
        } catch (Exception e) {
        }
        return project;
    }

    private String randFile() {
        return new File(workConfig.getBaseDir(), UUID.randomUUID().toString() + ".json").getAbsolutePath();
    }

    @Override
    public void refreshProject() {
        List<UMLModel> models = new ArrayList<>();
        models.addAll(projectData.getModels());
        canvasPanel.refreshModels(models);
        canvasPanel.refreshConfig(projectData.getConfig());
        operatePanel.refreshConfig(projectData.getConfig());
        editor.refreshConfig(projectData.getConfig());
        settings.refreshConfig(projectData.getConfig());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        umlScorllPanel.setSize(e.getComponent().getWidth(), e.getComponent().getHeight());
        canvasPanel.setCenter();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            saveProject(projectData);
        }
    }

    @Override
    public void showSettings(boolean setting) {
        if (setting) {
            this.settings.showMe();
            this.settings.refresh();
        } else if (this.editor.getUmlInfoPanel() != null) {
            this.editor.showMe();
        } else {
            UMLInfoPanel selected = null;
            List<GroupItem> items = this.canvasPanel.getItems();
            if (items.isEmpty()) {
                UMLInfoPanel info = new UMLInfoPanel(this, null, this.canvasPanel.getWidth() / 2, this.canvasPanel.getHeight() / 2);
                this.canvasPanel.create(info);
            }
            for (GroupItem groupItem : items) {
                if (groupItem.isSelected()) {
                    selected = (UMLInfoPanel) groupItem;
                    break;
                }
            }
            if (selected == null && !items.isEmpty()) {
                this.canvasPanel.selectItem(items.get(0), null);
                selected = (UMLInfoPanel) items.get(0);
            }
            showEditor(selected);
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public void showEditor(UMLInfoPanel info) {
        canvasPanel.setCenter(info);
        this.editor.setModel(info);
        this.editor.showMe();
        this.revalidate();
        this.repaint();
    }

    public void hideEditor() {
        this.operatePanel.setVisible(false);
    }

    @Override
    public UMLGUIConfig getCfg() {
        return projectData.getConfig();
    }

    @Override
    public UMLProjectData getCurProject() {
        return projectData;
    }

    @Override
    public UMLScrollHelper getScrollHelper() {
        return umlScorllPanel;
    }

    @Override
    public void setEditorContentCenter() {
        getScrollHelper().setCenter(this.editor.getUmlInfoPanel());
    }

    @Override
    public void toggleEditorList() {
        this.editor.toggleEditorList();
    }

}
