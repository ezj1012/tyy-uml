package com.tyy.uml.gui.comm;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.JLayeredPane;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.core.gui.adapter.DFocusListener;
import com.tyy.uml.core.gui.adapter.DMouseListener;
import com.tyy.uml.util.SWUtils;

public class OperateLabelPanel extends JLayeredPane implements DComponentListener, DMouseListener, DFocusListener {

    private static final long serialVersionUID = 1L;

    TitleLabelPanel fieldPanel;

    // EditorLabelPanel editorPanel;

    List<SingleColorIconButton> rightBtns = new ArrayList<>();

    List<SingleColorIconButton> leftBtns = new ArrayList<>();

    private int fixedHeight;

    public OperateLabelPanel(int fixedHeight, int alignment) {
        this.fixedHeight = fixedHeight;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        SWUtils.fixedHeight(this, this.fixedHeight);
        this.initCentent(alignment);
        this.addComponentListener(this);
        // editorPanel.addFocusListener(this);
    }

    public SingleColorIconButton addLeftButton(String icon, int size, BiConsumer<SingleColorIconButton, ActionEvent> l) {
        SingleColorIconButton button = createBtn(icon, size, l);
        addLeftButton(button);
        return button;
    }

    public SingleColorIconButton addLeftButton(SingleColorIconButton button) {
        this.leftBtns.add(button);
        this.add(button, PALETTE_LAYER);
        return button;
    }

    public SingleColorIconButton addRightButton(String icon, int size, BiConsumer<SingleColorIconButton, ActionEvent> l) {
        SingleColorIconButton button = createBtn(icon, size, l);
        addRightButton(button);
        return button;
    }

    public SingleColorIconButton addRightButton(SingleColorIconButton button) {
        this.rightBtns.add(button);
        this.add(button, PALETTE_LAYER);
        return button;
    }

    private SingleColorIconButton createBtn(String icon, int size, BiConsumer<SingleColorIconButton, ActionEvent> l) {
        SingleColorIconButton button = new SingleColorIconButton(fixedHeight, fixedHeight, new SingleColorIcon(icon, size, size), new Color(133, 133, 133));
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

    private void initCentent(int alignment) {
        fieldPanel = new TitleLabelPanel(this.fixedHeight, Color.WHITE, 0, 10, 0, 0);
        fieldPanel.setFont(Font.PLAIN, this.fixedHeight - 8);
        setHorizontalAlignment(alignment);
        this.add(fieldPanel, JLayeredPane.DEFAULT_LAYER);
    }

    @Override
    public void focusLost(FocusEvent e) {
        fieldPanel.setVisible(true);
    }

    @Override
    public void setBackground(Color bg) {
        if (fieldPanel != null) {
            fieldPanel.setOpaque(true);
            fieldPanel.setBackground(bg);
        }
        super.setBackground(bg);
    }

    public void setHorizontalAlignment(int alignment) {
        fieldPanel.setHorizontalAlignment(alignment);
    }

    public void setForeground(Color color) {
        if (fieldPanel != null) {
            fieldPanel.setForeground(color);
        }
        super.setForeground(color);
    }

    public void setText(String text) {
        fieldPanel.setText(text);
    }

    public void setTitle(String title) {
        setText(title);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int w = e.getComponent().getWidth();
        int h = e.getComponent().getHeight();
        for (int i = 0; i < leftBtns.size(); i++) {
            leftBtns.get(i).setBounds(5 + i * this.fixedHeight, 0, this.fixedHeight, this.fixedHeight);
        }
        for (int i = rightBtns.size(); i > 0; i--) {
            rightBtns.get(rightBtns.size() - i).setBounds(w - this.fixedHeight * i, 0, this.fixedHeight, this.fixedHeight);
        }

        fieldPanel.setBounds(0, 0, w, h);
        fieldPanel.revalidate();
        SWUtils.printSize("field", fieldPanel);

        // editorPanel.setBounds(0, 0, w, h);
        // SWUtils.printSize(" editor", editorPanel);
        // editorPanel.revalidate();
        //

    }

}
