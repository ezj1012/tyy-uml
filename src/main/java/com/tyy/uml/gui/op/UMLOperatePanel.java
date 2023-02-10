package com.tyy.uml.gui.op;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.ctx.Ctrl;
import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.gui.canvas.elements.UMLInfoPanel;
import com.tyy.uml.gui.comm.group.GroupItem;
import com.tyy.uml.gui.op.editor.UMLEditor;
import com.tyy.uml.gui.op.setting.UMLSettings;
import com.tyy.uml.util.Constant;
import com.tyy.uml.util.SWUtils;
import com.tyy.uml.util.BeanHelper.BeanObservale;
import com.tyy.uml.util.BeanHelper.BeanObserver;

public class UMLOperatePanel extends JPanel implements BeanObserver, DComponentListener {

    private static final long serialVersionUID = 1L;

    public static final int fixedHeight = 300;

    Ctrl ctrl;

    UMLOperateTitle title;

    UMLEditor editor;

    UMLSettings settings;

    AbsUMLOperateMain curMain;

    public UMLOperatePanel(Ctrl ctrl) {
        this.ctrl = ctrl;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout());
        this.title = new UMLOperateTitle(ctrl, this);
        this.add(title, BorderLayout.NORTH);
        this.editor = new UMLEditor(ctrl, this);
        this.settings = new UMLSettings(ctrl, this);
        this.ctrl.registerAction(Constant.prevModel, (btn, e) -> this.setEditorPrevCenter());
        this.ctrl.registerAction(Constant.nextModel, (btn, e) -> this.setEditorNextCenter());
        this.ctrl.registerAction(Constant.curModel, (btn, e) -> this.setEditorContentCenter());
        this.ctrl.registerAction(Constant.toggleEditorList, (btn, e) -> this.toggleEditorList());
    }

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

    public void toggleEditorList() {
        if (this.editor.isVisible()) {
            this.editor.toggleEditorList();
        }
        if (this.settings.isVisible()) {
            this.settings.toggleEditorList();
        }
    }

    public void showEditor(UMLInfoPanel info) {
        canvasPanel.setCenter(info);
        this.editor.setModel(info);
        this.editor.showMe();
        this.revalidate();
        this.repaint();
    }

    public synchronized void setEditorContentCenter() {
        UMLInfoPanel umlInfoPanel = this.editor.getUmlInfoPanel();
        if (umlInfoPanel == null) {
            umlInfoPanel = getSelectOrCreateUMLInfoPanel(0);
        }
        showEditor(umlInfoPanel);
    }

    public synchronized void setEditorNextCenter() {
        showEditor(getSelectOrCreateUMLInfoPanel(1));
    }

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

    public UMLOperateTitle getTitle() {
        return title;
    }

    public void refreshConfig(UMLGUIConfig cfg) {
        if (cfg instanceof BeanObservale) {
            ((BeanObservale) cfg).addObserver(this);
        }
        updateConfig(cfg, null, null);
    }

    private void updateConfig(UMLGUIConfig cfg, String prop, Object newValue) {
        if (prop == null || "editorBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCurProject().getConfig().getEditorBackColor(), UMLGUIConfig.c252526);
            setBackground(backColor);
        }
        if (prop == null || "editorTitleBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCurProject().getConfig().getEditorTitleBackColor(), UMLGUIConfig.c333333);
            title.setBackground(backColor);
        }
        if (prop == null || "editorFontColor".equals(prop)) {
            Color fontColor = SWUtils.decodeColor(ctrl.getCurProject().getConfig().getEditorFontColor(), UMLGUIConfig.cd4d4d4);
            title.setForeground(fontColor);
        }
    }

    @Override
    public void update(Observable o, Object source, String prop, Object oldValue, Object newValue) {
        if (source instanceof UMLGUIConfig) {
            updateConfig((UMLGUIConfig) source, prop, newValue);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        setCenter();
    }

    public void setCenter() {
        if (this.getParent() != null && curMain != null) {
            int wid = this.curMain.getWidth();
            Container parent = this.getParent();
            int x = (parent.getWidth() - wid) / 2;
            x = x < 0 ? 0 : x;
            int y = (parent.getHeight() - fixedHeight) - 20;
            y = y < 0 ? 0 : y;
            this.setBounds(x, y, wid, fixedHeight);
            this.repaint();
            this.getParent().repaint();
        }
    }

    public void showMe(AbsUMLOperateMain main) {
        if (curMain != main) {
            if (curMain != null) {
                this.remove(curMain);
            }
            this.add(curMain = main, BorderLayout.CENTER);
        }
        this.setCenter();
        this.setVisible(true);
    }

}
