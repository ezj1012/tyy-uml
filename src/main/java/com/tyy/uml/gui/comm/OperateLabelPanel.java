package com.tyy.uml.gui.comm;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.core.gui.adapter.DFocusListener;
import com.tyy.uml.core.gui.adapter.DMouseListener;
import com.tyy.uml.util.SWUtils;

public class OperateLabelPanel extends JLayeredPane implements DComponentListener, DMouseListener, DFocusListener {

    private static final long serialVersionUID = 1L;

    TitleLabelPanel fieldPanel;

    EditorLabelPanel editorPanel;

    List<SingleColorIconButton> rightBtns = new ArrayList<>();

    List<SingleColorIconButton> leftBtns = new ArrayList<>();

    private int fixedHeight;

    public OperateLabelPanel(int fixedHeight, int alignment) {
        this.fixedHeight = fixedHeight;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        SWUtils.fixedHeight(this, this.fixedHeight);
        this.initCentent(alignment);
        this.addComponentListener(this);
        editorPanel.addFocusListener(this);
    }

    private void initCentent(int alignment) {
        fieldPanel = new TitleLabelPanel(this.fixedHeight, Color.WHITE, 0, 10, 0, 0);
        editorPanel = new EditorLabelPanel(this.fixedHeight, Color.WHITE, 0, 10, 0, 0, true);
        fieldPanel.setFont(Font.PLAIN, this.fixedHeight - 8);
        editorPanel.setFont(Font.PLAIN, this.fixedHeight - 8);
        setHorizontalAlignment(alignment);
        editorPanel.setVisible(false);
        this.add(fieldPanel, JLayeredPane.DEFAULT_LAYER);
        this.add(editorPanel, JLayeredPane.PALETTE_LAYER);
        fieldPanel.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // if (e.getClickCount() > 2) {
        // fieldPanel.setVisible(false);
        // editorPanel.setVisible(true);
        // editorPanel.requestFocus();
        // }
    }

    @Override
    public void focusLost(FocusEvent e) {
        editorPanel.setVisible(false);
        fieldPanel.setVisible(true);
    }

    public String getNewText() {
        return this.editorPanel.getText();
    }

    @Override
    public synchronized void addFocusListener(FocusListener l) {
        this.editorPanel.addFocusListener(l);
    }

    @Override
    public void setBackground(Color bg) {
        if (fieldPanel != null) {
            fieldPanel.setOpaque(true);
            fieldPanel.setBackground(bg);
        }
        super.setBackground(bg);
    }

    public void setCaretColor(Color c) {
        editorPanel.setCaretColor(c);
    }

    public void setHorizontalAlignment(int alignment) {
        fieldPanel.setHorizontalAlignment(alignment);
    }

    public void setForeground(Color color) {
        if (fieldPanel != null) {
            fieldPanel.setForeground(color);
            editorPanel.setForeground(color);
        }
        super.setForeground(color);
    }

    public void setTitle(String title) {
        fieldPanel.setText(title);
        editorPanel.setText(title);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int w = e.getComponent().getWidth();
        int h = e.getComponent().getHeight();
        fieldPanel.setBounds(0, 0, w, h);
        editorPanel.setBounds(0, 0, w, h);
        SWUtils.printSize("field", fieldPanel);
        SWUtils.printSize(" editor", editorPanel);
        fieldPanel.revalidate();
        editorPanel.revalidate();
        for (int i = rightBtns.size(); i > 0; i--) {
            rightBtns.get(rightBtns.size() - i).setBounds(w - this.fixedHeight * i, 0, this.fixedHeight, this.fixedHeight);
        }

        for (int i = 0; i < leftBtns.size(); i++) {
            leftBtns.get(i).setBounds(5 + i * this.fixedHeight, 0, this.fixedHeight, this.fixedHeight);
        }

    }

}
