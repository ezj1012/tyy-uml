package com.tyy.uml.gui.op;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.ctx.Ctrl;
import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.core.gui.adapter.DMouseListener;
import com.tyy.uml.gui.comm.SingleColorIcon;
import com.tyy.uml.gui.comm.SingleColorIconButton;
import com.tyy.uml.gui.comm.TitleLabelPanel;
import com.tyy.uml.util.Constant;
import com.tyy.uml.util.SWUtils;

public class UMLOperateTitle extends JLayeredPane implements DMouseListener, DComponentListener {

    private static final long serialVersionUID = 1L;

    private static final int SIZE = 20;

    protected int pressedX = 0;

    protected int pressedY = 0;

    protected boolean fixedPos = false;

    protected boolean setting = false;

    Ctrl ctrl;

    List<SingleColorIconButton> rightBtns = new ArrayList<>();

    List<SingleColorIconButton> leftBtns = new ArrayList<>();

    TitleLabelPanel fieldPanel;

    SingleColorIconButton settingBtn;

    UMLOperatePanel operatePanel;

    public UMLOperateTitle(Ctrl ctrl, UMLOperatePanel operatePanel) {
        this.ctrl = ctrl;
        this.operatePanel = operatePanel;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        SWUtils.fixedHeight(this, SIZE);
        this.initCentent();
        this.registerAction();
        this.initBtn();
        this.addComponentListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.fieldPanel.addMouseListener(this);
        this.fieldPanel.addMouseMotionListener(this);
    }

    private void registerAction() {
        this.ctrl.registerAction(Constant.fixedEditorPanel, (btn, e) -> {
            fixedPos = !fixedPos;
            btn.setSelected(fixedPos);
            this.repaint();
        });

        this.ctrl.registerAction(Constant.openSetting, (btn, e) -> {
            setting = true;
            btn.setSelected(setting);
            operatePanel.showSettings(setting);
            this.repaint();
        });

        this.ctrl.registerAction(Constant.toggleSetting, (btn, e) -> {
            setting = !setting;
            btn.setSelected(setting);
            operatePanel.showSettings(setting);
            this.repaint();
        });

        this.ctrl.registerAction(Constant.closeEditor, (btn, e) -> {
            this.operatePanel.setVisible(false);
            settingBtn.setSelected(setting = false);
        });

    }

    private void initBtn() {
        SingleColorIconButton fixedBtn = createBtn("unlock.png", Constant.fixedEditorPanel);
        fixedBtn.setSelectedIcon(new SingleColorIcon("lock.png", SIZE - 6, SIZE - 6).getImageIcon(new Color(133, 133, 133)));
        settingBtn = createBtn("setting.png", Constant.toggleSetting);
        settingBtn.setSelectedIcon(new SingleColorIcon("setting.png", SIZE - 6, SIZE - 6).getImageIcon(new Color(133, 133, 133)));

        leftBtns.add(settingBtn);
        leftBtns.add(fixedBtn);
        for (int i = 0; i < leftBtns.size(); i++) {
            this.add(leftBtns.get(i), JLayeredPane.PALETTE_LAYER);
        }

        rightBtns.add(createBtn("page_first.png", Constant.prevModel));
        rightBtns.add(createBtn("view.png", Constant.curModel));
        rightBtns.add(createBtn("page_last.png", Constant.nextModel));
        rightBtns.add(createBtn("list.png", Constant.toggleEditorList));
        rightBtns.add(createBtn("close.png", Constant.closeEditor));
        for (int i = rightBtns.size(); i > 0; i--) {
            rightBtns.get(i - 1).setBounds(getWidth() - SIZE * i, 0, SIZE, SIZE);
            this.add(rightBtns.get(i - 1), JLayeredPane.PALETTE_LAYER);
        }

    }

    private SingleColorIconButton createBtn(String icon, String key) {
        SingleColorIconButton button = new SingleColorIconButton(SIZE, SIZE, new SingleColorIcon(icon, SIZE - 6, SIZE - 6), new Color(133, 133, 133));
        button.setRolloverBackgroupColor(new Color(70, 70, 70));
        button.addActionListener(this.ctrl.createAction(button, key));
        return button;
    }

    public void setButtonRolloverBackgroupColor(Color rolloverBackgroupColor) {
        for (SingleColorIconButton c : this.rightBtns) {
            c.setRolloverBackgroupColor(rolloverBackgroupColor);
        }
    }

    @Override
    public void setBackground(Color bg) {
        if (fieldPanel != null) {
            fieldPanel.setOpaque(true);
            fieldPanel.setBackground(bg);
        }
        super.setBackground(bg);
    }

    private void initCentent() {
        Color fontColor = SWUtils.decodeColor(ctrl.getCurProject().getConfig().getEditorFontColor(), UMLGUIConfig.cd4d4d4);
        fieldPanel = new TitleLabelPanel(SIZE, fontColor);
        fieldPanel.setHorizontalAlignment(JTextField.CENTER);
        this.add(fieldPanel, JLayeredPane.DEFAULT_LAYER);
    }

    public void setForeground(Color color) {
        if (fieldPanel != null) {
            fieldPanel.setForeground(color);
        }
        super.setForeground(color);
    }

    public void setTitle(String title) {
        fieldPanel.setText(title);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressedX = e.getX();
        pressedY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressedX = 0;
        pressedY = 0;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!fixedPos && this.getParent() != null) {
            int x = this.getParent().getX() + e.getX() - pressedX;
            int y = this.getParent().getY() + e.getY() - pressedY;
            this.getParent().setLocation(x, y);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        fieldPanel.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
        for (int i = rightBtns.size(); i > 0; i--) {
            rightBtns.get(rightBtns.size() - i).setBounds(e.getComponent().getWidth() - SIZE * i, 0, SIZE, SIZE);
        }

        for (int i = 0; i < leftBtns.size(); i++) {
            leftBtns.get(i).setBounds(5 + i * SIZE, 0, SIZE, SIZE);
        }

    }

}
