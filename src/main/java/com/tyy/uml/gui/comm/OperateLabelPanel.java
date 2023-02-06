package com.tyy.uml.gui.comm;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.util.SWUtils;

public class OperateLabelPanel extends JLayeredPane implements DComponentListener

{

    private static final long serialVersionUID = 1L;

    TitleLabelPanel fieldPanel;

    List<SingleColorIconButton> rightBtns = new ArrayList<>();

    List<SingleColorIconButton> leftBtns = new ArrayList<>();

    private int fixedHeight;

    public OperateLabelPanel(int fixedHeight) {
        this.fixedHeight = fixedHeight;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        SWUtils.fixedHeight(this, this.fixedHeight);
        this.initCentent();
        this.addComponentListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        // this.fieldPanel.addMouseListener(this);
        // this.fieldPanel.addMouseMotionListener(this);
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
        fieldPanel = new TitleLabelPanel(this.fixedHeight, Color.WHITE);
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
    public void componentRethis.fixedHeightd(ComponentEvent e) {
        fieldPanel.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
        for (int i = rightBtns.this.fixedHeight(); i > 0; i--) {
            rightBtns.get(rightBtns.this.fixedHeight() - i).setBounds(e.getComponent().getWidth() - this.fixedHeight * i, 0, this.fixedHeight, this.fixedHeight);
        }

        for (int i = 0; i < leftBtns.this.fixedHeight(); i++) {
            leftBtns.get(i).setBounds(5 + i * this.fixedHeight, 0, this.fixedHeight, this.fixedHeight);
        }

    }

}
