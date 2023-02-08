package com.tyy.uml.gui.op.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import com.tyy.uml.core.ctx.Ctrl;
import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.core.ctx.model.UMLModel;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.core.gui.adapter.DDocumentListener;
import com.tyy.uml.gui.canvas.elements.UMLInfoPanel;
import com.tyy.uml.gui.op.AbsUMLOperateMain;
import com.tyy.uml.gui.op.UMLOperatePanel;
import com.tyy.uml.util.SWUtils;
import com.tyy.uml.util.UMLModelParser;
import com.tyy.uml.util.BeanHelper.BeanObservale;
import com.tyy.uml.util.BeanHelper.BeanObserver;

public class UMLEditor extends AbsUMLOperateMain implements BeanObserver, DComponentListener, DDocumentListener {

    private static final long serialVersionUID = 1L;

    UMLInfoList umlList;

    JEditorPane editorPane = new JEditorPane();

    UMLRltEditor rltEditor = new UMLRltEditor(this);

    UMLModel model;

    UMLInfoPanel umlInfoPanel;

    public UMLEditor(Ctrl ctrl, UMLOperatePanel operatePanel) {
        super(ctrl, operatePanel);
        this.initUMLInfos();
        this.initEditorPanel();
        this.initUMLConfig();
    }

    private void initUMLInfos() {
        this.umlList = new UMLInfoList();
        this.umlList.setVisible(false);
        this.add(umlList, BorderLayout.WEST);
        this.umlList.addKeyListener(ctrl);
    }

    private void initUMLConfig() {
        this.rltEditor.addKeyListener(ctrl);
        this.add(rltEditor, BorderLayout.EAST);
    }

    private void initEditorPanel() {
        editorPane.setPreferredSize(new Dimension(300, UMLOperatePanel.fixedHeight));
        editorPane.getDocument().addDocumentListener(this);
        editorPane.setBorder(new EmptyBorder(5, 10, 0, 5));

        this.editorPane.addKeyListener(ctrl);
        this.add(editorPane, BorderLayout.CENTER);
    }

    public void refreshConfig(UMLGUIConfig cfg) {
        if (cfg instanceof BeanObservale) {
            ((BeanObservale) cfg).addObserver(this);
        }
        updateConfig(cfg, null, null);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag && this.editorPane != null) {
            this.editorPane.requestFocus();
            this.editorPane.repaint();
        }
    }

    public void updateConfig(UMLGUIConfig cfg, String prop, Object newValue) {
        if (prop == null || "editorBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCfg().getEditorBackColor(), UMLGUIConfig.c252526);
            setBackground(backColor);
            editorPane.setBackground(backColor);
            rltEditor.setBackground(backColor);
        }
        if (prop == null || "editorFontColor".equals(prop)) {
            Color fontColor = SWUtils.decodeColor(ctrl.getCfg().getEditorFontColor(), UMLGUIConfig.cd4d4d4);
            editorPane.setForeground(fontColor);
        }
        if (prop == null || "editorCaretColor".equals(prop)) {
            editorPane.setCaretColor(SWUtils.decodeColor(ctrl.getCfg().getEditorCaretColor(), UMLGUIConfig.caeafad));
        }
    }

    private void updateModel(UMLModel model, String prop, Object newValue) {
        if (prop == null || "className".equals(prop) || "classDesc".equals(prop)) {
            setTitle(UMLModelParser.toClassTitle(this.model));
        }
    }

    @Override
    public void update(Observable o, Object source, String prop, Object oldValue, Object newValue) {
        if (source instanceof UMLGUIConfig) {
            updateConfig((UMLGUIConfig) source, prop, newValue);
        } else if (source instanceof UMLModel) {
            updateModel((UMLModel) source, prop, newValue);
        }
    }

    public void setModel(UMLInfoPanel umlInfoPanel) {
        if (umlInfoPanel == null) {
            if (this.model != null && model instanceof BeanObservale) {
                ((BeanObservale) model).deleteObserver(this);
            }
            this.model = null;
            this.umlInfoPanel = umlInfoPanel;
            this.rltEditor.refresModel(umlInfoPanel);
            return;
        }

        this.umlInfoPanel = umlInfoPanel;
        UMLModel newModel = umlInfoPanel.getModel();
        if (model == newModel) { return; }
        if (model != null && model instanceof BeanObservale) {
            ((BeanObservale) model).deleteObserver(this);
        }
        ((BeanObservale) newModel).addObserver(this);
        this.model = newModel;
        this.editorPane.setText(this.model.getUmlString());
        setTitle(UMLModelParser.toClassTitle(this.model));

        this.rltEditor.refresModel(umlInfoPanel);
    }

    @Override
    public void changed(DocumentEvent e, String newValue) {
        model.setUmlString(newValue);
        UMLModelParser.parser(newValue, model);
    }

    public UMLModel getModel() {
        return model;
    }

    public UMLInfoPanel getUmlInfoPanel() {
        return umlInfoPanel;
    }

    public static class UMLRltEditor extends JPanel {

        private static final long serialVersionUID = 1L;

        private UMLModel model;

        private UMLEditor editor;

        public UMLRltEditor(UMLEditor editor) {
            this.setPreferredSize(new Dimension(300, UMLOperatePanel.fixedHeight));
            this.setBorder(new EmptyBorder(0, 0, 0, 0));
        }

        public void refresModel(UMLInfoPanel umlInfoPanel) {
            if (umlInfoPanel != null) {
                this.model = umlInfoPanel.getModel();
            } else {
                this.model = null;
            }
        }

    }

    public void toggleEditorList() {
        if (!umlList.isVisible()) {
            umlList.refresModel(umlInfoPanel);
            umlList.setVisible(true);
        } else {
            umlList.setVisible(false);
        }
        refreshSize();
    }

}
