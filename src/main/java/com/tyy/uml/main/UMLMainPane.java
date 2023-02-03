package com.tyy.uml.main;

import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;

import com.alibaba.fastjson2.JSON;
import com.tyy.uml.adapter.DComponentListener;
import com.tyy.uml.adapter.DKeyListener;
import com.tyy.uml.bean.BeanHelper;
import com.tyy.uml.bean.SaveData;
import com.tyy.uml.bean.UMLConfig;
import com.tyy.uml.bean.UMLModel;
import com.tyy.uml.canvas.UMLCanvas;
import com.tyy.uml.canvas.UMLScrollHelper;
import com.tyy.uml.editor.UMLEditor;
import com.tyy.uml.group.GroupItem;
import com.tyy.uml.info.UMLInfoPanel;

import cn.hutool.core.io.IoUtil;

public class UMLMainPane extends JLayeredPane implements DComponentListener, Ctrl, DKeyListener {

    private static final long serialVersionUID = 1L;

    UMLCanvas umlPanel;

    UMLScrollHelper umlScorllPanel;

    UMLEditor editor;

    File configFile;

    UMLConfig config;

    List<UMLModel> models = new ArrayList<UMLModel>();

    public UMLMainPane(File config) {
        this.configFile = config;
        this.loadDatas(config);
        umlPanel = new UMLCanvas(this);
        editor = new UMLEditor(this);
        this.addComponentListener(this);
        this.addComponentListener(editor);
        initScorll();
        // this.add(umlPanel, JLayeredPane.DEFAULT_LAYER);
        this.add(editor, JLayeredPane.PALETTE_LAYER);
        this.addKeyListener(this);
        umlPanel.addKeyListener(this);
        editor.addKeyListener(this);
        editor.setVisible(false);
        this.refreshDatas();
    }

    public void initScorll() {
        umlScorllPanel = new UMLScrollHelper(umlPanel);
        this.add(umlScorllPanel.getUmlScorllPanel(), JLayeredPane.DEFAULT_LAYER);
    }

    @Override
    public void refreshDatas() {
        umlPanel.refreshModels(models);
        umlPanel.refreshConfig(config);
        editor.refreshConfig(config);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        umlScorllPanel.setSize(e.getComponent().getWidth(), e.getComponent().getHeight());
        umlPanel.setCenter();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            saveDatas(this.configFile);
        }
    }

    public UMLEditor setEditorContent(UMLInfoPanel info) {
        editor.setVisible(true);
        editor.setModel(info.getModel());
        umlPanel.setCenter(info);
        this.revalidate();
        this.repaint();
        return editor;
    }

    public void hideEditor() {
        this.remove(editor);
    }

    @Override
    public SaveData loadDatas(File configFile) {
        models.clear();
        SaveData saveData = null;
        try (FileInputStream fis = new FileInputStream(configFile)) {
            String string = IoUtil.read(fis, Charset.forName("UTF-8"));
            saveData = JSON.parseObject(string, SaveData.class);
            if (saveData.getConfig() == null) {
                saveData.setConfig(new UMLConfig());
            }
            if (saveData.getModels() == null) {
                saveData.setModels(new ArrayList<>());
            }
        } catch (Exception e) {
            saveData = saveDatas(configFile);
        }
        this.config = BeanHelper.proxy(saveData.getConfig());
        models.addAll(saveData.getModels());
        return new SaveData(this.config, saveData.getModels());
    }

    @Override
    public SaveData saveDatas(File file) {
        SaveData saveData = getSaveData();
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
        }
        try (FileOutputStream fis = new FileOutputStream(configFile)) {
            IoUtil.write(fis, true, JSON.toJSONString(saveData, com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat).getBytes(Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveData;
    }

    private SaveData getSaveData() {
        List<UMLModel> models = new ArrayList<UMLModel>();
        if (this.umlPanel != null) {
            for (GroupItem groupItem : this.umlPanel.getItems()) {
                if (groupItem instanceof UMLInfoPanel) {
                    UMLModel model = ((UMLInfoPanel) groupItem).getModel();
                    models.add(model);
                }
            }
        }
        UMLConfig cfg = this.config == null ? new UMLConfig() : this.config;

        return new SaveData(cfg, models);
    }

    @Override
    public UMLConfig getCfg() {
        return config;
    }

}
