package com.tyy.uml.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.adapter.DComponentListener;
import com.tyy.uml.adapter.DMouseListener;
import com.tyy.uml.bean.UMLConfig;
import com.tyy.uml.comm.SingleColorIcon;
import com.tyy.uml.comm.SingleColorIconButton;
import com.tyy.uml.comm.TitleLabelPanel;
import com.tyy.uml.main.Ctrl;
import com.tyy.uml.util.SWUtils;

public class UMLTitle extends JLayeredPane implements DMouseListener, DComponentListener {

    private static final long serialVersionUID = 1L;

    private static final int SIZE = 20;

    protected int pressedX = 0;

    protected int pressedY = 0;

    protected boolean fixedPos = false;

    Ctrl ctrl;

    UMLEditor editor;

    List<SingleColorIconButton> btns = new ArrayList<>();

    TitleLabelPanel fieldPanel;

    public UMLTitle(Ctrl ctrl, UMLEditor editor) {
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.ctrl = ctrl;
        this.editor = editor;
        SWUtils.fixedHeight(this, SIZE);
        this.initCentent();
        this.initBtn();
        this.addComponentListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.fieldPanel.addMouseListener(this);
        this.fieldPanel.addMouseMotionListener(this);
    }

    SingleColorIconButton viewBtn;

    SingleColorIconButton listBtn;

    SingleColorIconButton closeBtn;

    private void initBtn() {
        btns.add(createBtn("view.png", (btn, e) -> this.ctrl.getScrollHelper().setCenter(editor.getUmlInfoPanel())));
        btns.add(createBtn("list.png", (btn, e) -> this.ctrl.toggleEditorList()));
        btns.add(createBtn("close.png", (btn, e) -> this.ctrl.hideEditor()));
        for (int i = btns.size(); i > 0; i--) {
            btns.get(i - 1).setBounds(getWidth() - SIZE * i, 0, SIZE, SIZE);
            this.add(btns.get(i - 1), JLayeredPane.PALETTE_LAYER);
        }

        SingleColorIconButton fixedBtn = createBtn("unlock.png", (btn, e) -> {
            fixedPos = !fixedPos;
            btn.setSelected(fixedPos);
            this.repaint();
        });
        fixedBtn.setSelectedIcon(new SingleColorIcon("lock.png", SIZE - 6, SIZE - 6).getImageIcon(new Color(133, 133, 133)));
        fixedBtn.setBounds(5, 0, SIZE, SIZE);
        this.add(fixedBtn, JLayeredPane.PALETTE_LAYER);
    }

    private SingleColorIconButton createBtn(String icon, BiConsumer<SingleColorIconButton, ActionEvent> l) {
        SingleColorIconButton button = new SingleColorIconButton(SIZE, SIZE, new SingleColorIcon(icon, SIZE - 6, SIZE - 6), new Color(133, 133, 133));
        button.setRolloverBackgroupColor(new Color(70, 70, 70));
        // button.setBounds(offIdx * SIZE, 0, SIZE, SIZE);
        if (l != null) {
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    l.accept(button, e);
                }

            });
        }
        return button;
    }

    public void setButtonRolloverBackgroupColor(Color rolloverBackgroupColor) {
        for (SingleColorIconButton c : this.btns) {
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
        Color fontColor = SWUtils.decodeColor(ctrl.getCfg().getEditorFontColor(), UMLConfig.cd4d4d4);
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
        if (!fixedPos && editor != null) {
            int x = this.editor.getX() + e.getX() - pressedX;
            int y = this.editor.getY() + e.getY() - pressedY;
            this.editor.setLocation(x, y);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        fieldPanel.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
        for (int i = btns.size(); i > 0; i--) {
            btns.get(btns.size() - i).setBounds(e.getComponent().getWidth() - SIZE * i, 0, SIZE, SIZE);
        }
    }

}
