package com.tyy.uml.gui;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.swing.JLayeredPane;

import com.tyy.uml.core.ctx.Ctrl;
import com.tyy.uml.core.ctx.UMLContext;
import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.core.ctx.model.UMLModel;
import com.tyy.uml.core.ctx.model.UMLProject;
import com.tyy.uml.core.ctx.model.UMLProjectData;
import com.tyy.uml.core.ctx.model.UMLWork;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.core.gui.adapter.DKeyListener;
import com.tyy.uml.gui.canvas.UMLCanvas;
import com.tyy.uml.gui.canvas.elements.UMLInfoPanel;
import com.tyy.uml.gui.comm.group.GroupItem;
import com.tyy.uml.gui.op.UMLOperatePanel;
import com.tyy.uml.gui.op.editor.UMLEditor;
import com.tyy.uml.gui.op.setting.UMLSettings;
import com.tyy.uml.util.BeanHelper;
import com.tyy.uml.util.BeanHelper.BeanObservale;
import com.tyy.uml.util.SystemUtils;

public class UMLMainPane extends JLayeredPane implements DComponentListener, Ctrl, DKeyListener {

    private static final long serialVersionUID = 1L;

    UMLFrame frame;

    UMLCanvas canvasPanel;

    // UMLCanvasScroll umlScorllPanel;

    UMLOperatePanel operatePanel;

    UMLEditor editor;

    UMLSettings settings;

    UMLWork workConfig;

    UMLProjectData projectData;

    public UMLMainPane(UMLFrame frame, UMLWork workConfig) {
        this.frame = frame;
        this.workConfig = workConfig;
        if (workConfig.getProjects().size() == 0) {
            createProject(new UMLProject("未命名", randFile()));
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
        // umlScorllPanel = new UMLCanvasScroll(canvasPanel);
        canvasPanel.addTo(e -> {
            this.add(e, JLayeredPane.DEFAULT_LAYER);
        });
    }

    @Override
    public void saveConfigs() {
        UMLContext.getContext().saveConfigs();
    }

    @Override
    public void createProject(UMLProject project) {
        UMLProjectData converBean = SystemUtils.converBean(project, UMLProjectData.class);
        saveProject(converBean);
        project.setPath(converBean.getPath());
        workConfig.getProjects().add(project);
        this.saveConfigs();
        if (this.settings != null) {
            this.settings.refresh();
        }
    }

    @Override
    public void delProject(UMLProject project) {
        Iterator<UMLProject> iterator = workConfig.getProjects().iterator();
        boolean delF = false;
        while (iterator.hasNext()) {
            UMLProject umlProject = iterator.next();
            if (Objects.equals(umlProject.getPath(), project.getPath())) {
                iterator.remove();
                File file = new File(project.getPath());
                if (file.exists()) {
                    File del = new File(workConfig.getBaseDir(), "history");
                    del.mkdirs();
                    File nf = null;
                    String namep = umlProject.getName();
                    int i = 0;
                    while (true) {
                        String newName = namep + (i == 0 ? "" : "(" + i + ")") + ".json";
                        nf = new File(del, newName);
                        if (!nf.exists()) {
                            file.renameTo(nf);
                            break;
                        }
                        i++;
                    }
                }
                delF = true;
                break;
            }
        }
        if (delF) {
            if (workConfig.getProjects().size() == 0) {
                createProject(new UMLProject("未命名", randFile()));
            }
            saveConfigs();
            loadProject(workConfig.getProjects().get(0), false);
            refreshProject();
            this.revalidate();
            this.repaint();
        }
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
            data = SystemUtils.readFile(new File(project.getPath()), UMLProjectData.class);
        }
        if (data == null) {
            data = SystemUtils.converBean(project, UMLProjectData.class);
            SystemUtils.writeFile(f, data);
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
        editor.setModel(null);
        settings.refreshConfig(projectData.getConfig());
        settings.refresh();
        frame.setTitle(projectData.getName());
        frame.refreshFrameParameters(projectData.getConfig());
        projectData.getConfig().setFrameTitle(projectData.getName());
        this.revalidate();
        this.repaint();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        canvasPanel.setScorllSize(e.getComponent().getWidth(), e.getComponent().getHeight());
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
            this.settings.requestFocus();
        } else {

            setEditorContentCenter();
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
    public synchronized void setEditorContentCenter() {
        UMLInfoPanel umlInfoPanel = this.editor.getUmlInfoPanel();
        if (umlInfoPanel == null) {
            umlInfoPanel = getSelectOrCreateUMLInfoPanel(0);
        }
        showEditor(umlInfoPanel);
    }

    @Override
    public synchronized void setEditorNextCenter() {
        showEditor(getSelectOrCreateUMLInfoPanel(1));
    }

    @Override
    public synchronized void setEditorPrevCenter() {
        showEditor(getSelectOrCreateUMLInfoPanel(-1));
    }

    /**
     * 
     * @param i
     *            -1 上一个,0当前,1 下一个
     * @return
     */
    private UMLInfoPanel getSelectOrCreateUMLInfoPanel(int i) {
        UMLInfoPanel selected = null;
        List<GroupItem> items = this.canvasPanel.getItems();
        if (items.isEmpty()) {
            UMLInfoPanel info = new UMLInfoPanel(this, null, this.canvasPanel.getWidth() / 2, this.canvasPanel.getHeight() / 2);
            this.canvasPanel.create(info);
        }
        for (int j = 0; j < items.size(); j++) {
            GroupItem groupItem = items.get(j);
            if (groupItem.isSelected()) {
                selected = (UMLInfoPanel) groupItem;
                if (i != 0) {
                    int idx = j + i;
                    idx = idx < 0 ? items.size() - 1 : idx;
                    idx = idx >= items.size() ? 0 : idx;
                    selected = (UMLInfoPanel) items.get(idx);
                    this.canvasPanel.selectItem(selected, null);
                }
                break;
            }
        }

        if (selected == null && !items.isEmpty()) {
            this.canvasPanel.selectItem(items.get(0), null);
            selected = (UMLInfoPanel) items.get(0);
        }
        return selected;
    }

    @Override
    public void toggleEditorList() {
        if (this.editor.isVisible()) {
            this.editor.toggleEditorList();
        }
        if (this.settings.isVisible()) {
            this.settings.toggleEditorList();
        }
    }

}
