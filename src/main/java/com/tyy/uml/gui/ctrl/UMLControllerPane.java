package com.tyy.uml.gui.ctrl;

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
import com.tyy.uml.canvas.UMLCanvas;
import com.tyy.uml.canvas.UMLScrollHelper;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.context.UMLContext;
import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.core.gui.adapter.DKeyListener;
import com.tyy.uml.gui.comm.group.GroupItem;
import com.tyy.uml.gui.editor.UMLEditor;
import com.tyy.uml.gui.frame.UMLFrame;
import com.tyy.uml.gui.info.UMLInfoPanel;
import com.tyy.uml.util.SystemUtils;

public class UMLControllerPane extends JLayeredPane implements DComponentListener, Ctrl, DKeyListener {

    private static final long serialVersionUID = 1L;

    UMLFrame frame;

    UMLCanvas umlPanel;

    UMLScrollHelper umlScorllPanel;

    UMLEditor editor;

    UMLWork workConfig;

    UMLProjectData projectData;

    public UMLControllerPane(UMLFrame frame, UMLWork workConfig) {
        this.frame = frame;
        this.workConfig = workConfig;
        if (workConfig.getProjects().size() == 0) {
            createProject(new UMLProject("", randFile()));
        }
        loadProject(workConfig.getProjects().get(0));
        this.setPreferredSize(new Dimension(workConfig.getConfig().getFrameWidth(), workConfig.getConfig().getFrameHeight()));
        umlPanel = new UMLCanvas(this);
        editor = new UMLEditor(this);
        this.addComponentListener(this);
        this.addComponentListener(editor);
        // this.openProject(config);
        initScorll();
        this.add(editor, JLayeredPane.PALETTE_LAYER);
        this.addKeyListener(this);
        umlPanel.addKeyListener(this);
        editor.addKeyListener(this);
        editor.setVisible(false);
        this.refreshProject();
    }

    public void initScorll() {
        umlScorllPanel = new UMLScrollHelper(umlPanel);
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
    public UMLProjectData loadProject(UMLProject project) {
        if (projectData != null && projectData.getConfig() instanceof BeanObservale) {
            ((BeanObservale) projectData.getConfig()).deleteObservers();
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
        return this.projectData = data;
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
            if (this.umlPanel != null) {
                for (GroupItem groupItem : this.umlPanel.getItems()) {
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
        umlPanel.refreshModels(models);
        umlPanel.refreshConfig(projectData.getConfig());
        editor.refreshConfig(projectData.getConfig());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        umlScorllPanel.setSize(e.getComponent().getWidth(), e.getComponent().getHeight());
        umlPanel.setCenter();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            saveProject(projectData);
        }
    }

    public UMLEditor setEditorContent(UMLInfoPanel info) {
        editor.setVisible(true);
        editor.setModel(info);
        umlPanel.setCenter(info);
        this.revalidate();
        this.repaint();
        return editor;
    }

    public void hideEditor() {
        // this.remove(editor);
        this.editor.setVisible(false);
    }

    // @Override
    // public UMLProjectData loadDatas(File configFile) {
    // models.clear();
    // UMLProjectData saveData = null;
    // try (FileInputStream fis = new FileInputStream(configFile)) {
    // String string = IoUtil.read(fis, Charset.forName("UTF-8"));
    // saveData = JSON.parseObject(string, UMLProjectData.class);
    // if (saveData.getConfig() == null) {
    // saveData.setConfig(new UMLGUIConfig());
    // }
    // if (saveData.getModels() == null) {
    // saveData.setModels(new ArrayList<>());
    // }
    // } catch (Exception e) {
    // saveData = saveDatas(configFile);
    // }
    // if (this.config != null && this.config instanceof BeanObservale) {
    // ((BeanObservale) this.config).deleteObservers();
    // }
    // this.config = BeanHelper.proxy(saveData.getConfig());
    // models.addAll(saveData.getModels());
    //
    // return new UMLProjectData(this.config, saveData.getModels());
    // }

    // @Override
    // public UMLProjectData saveDatas(File file) {
    // UMLProjectData saveData = getSaveData();
    // if (!configFile.exists()) {
    // configFile.getParentFile().mkdirs();
    // }
    // try (FileOutputStream fis = new FileOutputStream(configFile)) {
    // IoUtil.write(fis, true, JSON.toJSONString(saveData, com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat).getBytes(Charset.forName("UTF-8")));
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return saveData;
    // }

    @Override
    public UMLGUIConfig getCfg() {
        return projectData.getConfig();
    }

    @Override
    public UMLScrollHelper getScrollHelper() {
        return umlScorllPanel;
    }

    @Override
    public void toggleEditorList() {
        this.editor.toggleEditorList();

    }

}
