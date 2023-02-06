package com.tyy.uml.gui.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.util.Observable;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import com.tyy.uml.bean.BeanHelper.BeanObservale;
import com.tyy.uml.bean.BeanHelper.BeanObserver;
import com.tyy.uml.context.Ctrl;
import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.core.gui.adapter.DDocumentListener;
import com.tyy.uml.gui.info.UMLInfoPanel;
import com.tyy.uml.bean.UMLModel;
import com.tyy.uml.bean.UMLModelParser;
import com.tyy.uml.util.SWUtils;

public class UMLEditor extends JPanel implements BeanObserver, DComponentListener, DDocumentListener {

    private static final long serialVersionUID = 1L;

    static final int fixedWidth = 500;

    static final int fixedHeight = 300;

    UMLList umlList;

    UMLEditorTitle umlTitle;

    JPanel content;

    JEditorPane editorPane = new JEditorPane();

    UMLRltEditor rltEditor = new UMLRltEditor(this);

    UMLModel model;

    UMLInfoPanel umlInfoPanel;

    private Ctrl ctrl;

    public UMLEditor(Ctrl ctrl) {
        this.ctrl = ctrl;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout());

        this.umlTitle = new UMLEditorTitle(ctrl, this);
        this.add(umlTitle, BorderLayout.NORTH);
        this.umlList = new UMLList();
        this.add(umlList, BorderLayout.WEST);
        umlList.setVisible(false);
        this.initEditorPanel();
        this.setCenter();
    }

    private void initEditorPanel() {
        SWUtils.optimize(content = new JPanel());
        editorPane.setPreferredSize(new Dimension(300, fixedHeight));
        editorPane.getDocument().addDocumentListener(this);
        editorPane.setBorder(new EmptyBorder(5, 10, 0, 5));

        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        content.add(editorPane);
        content.add(rltEditor);
        this.editorPane.addKeyListener(ctrl);
        this.rltEditor.addKeyListener(ctrl);
        this.umlList.addKeyListener(ctrl);
        this.add(content, BorderLayout.CENTER);
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

    private void updateConfig(UMLGUIConfig cfg, String prop, Object newValue) {
        if (prop == null || "editorBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCfg().getEditorBackColor(), UMLGUIConfig.c252526);
            setBackground(backColor);
            editorPane.setBackground(backColor);
            rltEditor.setBackground(backColor);
            content.setBackground(backColor);
        }
        if (prop == null || "editorTitleBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCfg().getEditorTitleBackColor(), UMLGUIConfig.c333333);
            umlTitle.setBackground(backColor);
        }
        if (prop == null || "editorFontColor".equals(prop)) {
            Color fontColor = SWUtils.decodeColor(ctrl.getCfg().getEditorFontColor(), UMLGUIConfig.cd4d4d4);
            editorPane.setForeground(fontColor);
            umlTitle.setForeground(fontColor);
        }
        if (prop == null || "editorCaretColor".equals(prop)) {
            editorPane.setCaretColor(SWUtils.decodeColor(ctrl.getCfg().getEditorCaretColor(), UMLGUIConfig.caeafad));
        }

    }

    private void updateModel(UMLModel model, String prop, Object newValue) {
        if (prop == null || "className".equals(prop) || "classDesc".equals(prop)) {
            umlTitle.setTitle(UMLModelParser.toClassTitle(this.model));
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
        this.umlInfoPanel = umlInfoPanel;
        UMLModel newModel = umlInfoPanel.getModel();
        if (model == newModel) { return; }
        if (model != null && model instanceof BeanObservale) {
            ((BeanObservale) model).deleteObserver(this);
        }
        ((BeanObservale) newModel).addObserver(this);
        this.model = newModel;
        this.editorPane.setText(this.model.getUmlString());
        this.umlTitle.setTitle(UMLModelParser.toClassTitle(this.model));
        this.rltEditor.refresModel(this.model);
    }

    @Override
    public void changed(DocumentEvent e, String newValue) {
        model.setUmlString(newValue);
        UMLModelParser.parser(newValue, model);
    }

    private void setCenter() {
        if (this.getParent() != null) {
            Container parent = this.getParent();
            int x = (parent.getWidth() - fixedWidth) / 2;
            x = x < 0 ? 0 : x;
            int y = (parent.getHeight() - fixedHeight) - 20;
            y = y < 0 ? 0 : y;
            this.setBounds(x, y, fixedWidth, fixedHeight);
            this.repaint();
            this.getParent().repaint();
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        setCenter();
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
            this.setPreferredSize(new Dimension(300, UMLEditor.fixedHeight));
            this.setBorder(new EmptyBorder(0, 0, 0, 0));
        }

        public void refresModel(UMLModel model) {
            this.model = model;
        }

    }

    public void toggleEditorList() {
        if (!umlList.isVisible()) {
            umlList.refresModel(umlInfoPanel);
            umlList.setVisible(true);
        } else {
            umlList.setVisible(false);
        }
    }

}
