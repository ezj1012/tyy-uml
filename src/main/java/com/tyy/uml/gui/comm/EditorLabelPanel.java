package com.tyy.uml.gui.comm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.util.SWUtils;

public class EditorLabelPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    JTextField showField;

    public EditorLabelPanel() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        showField = createLabel(12, Font.PLAIN, Color.BLACK, 0, 5, 0, 0, false);
        this.add(showField, BorderLayout.CENTER);
        SWUtils.fixedHeight(this, 16);
    }

    public EditorLabelPanel(int fixedHeight, Color fontColor) {
        this(fixedHeight, fontColor, 0, 0, 0, 0);
    }

    public EditorLabelPanel(int fixedHeight, Color fontColor, int top, int left, int bottom, int right) {
        this(fixedHeight, fontColor, top, left, bottom, right, false);
    }

    public EditorLabelPanel(int fixedHeight, Color fontColor, int top, int left, int bottom, int right, boolean editable) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        showField = createLabel(fixedHeight - 4, Font.PLAIN, fontColor, top, left, bottom, right, editable);
        this.add(showField, BorderLayout.CENTER);
        SWUtils.fixedHeight(this, fixedHeight);
    }

    public EditorLabelPanel(int fontSize, int fontStyle, Color fontColor, int top, int left, int bottom, int right, boolean editable) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        showField = createLabel(fontSize, fontStyle, fontColor, top, left, bottom, right, editable);
        this.add(showField, BorderLayout.CENTER);
        SWUtils.fixedHeight(this, fontSize + 4);
    }

    public void setEditable(boolean editable) {
        if (!this.showField.isEditable()) {
            this.showField.setEditable(editable);
            this.showField.requestFocus();
        }
        this.showField.repaint();
        System.out.println(SWUtils.toHex(this.showField.getCaretColor()));
    }

    @Override
    public void requestFocus() {
        // super.requestFocus();
        this.showField.requestFocus();
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        this.showField.addMouseListener(l);
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        this.showField.addMouseMotionListener(l);
    }

    public void setHorizontalAlignment(int alignment) {
        showField.setHorizontalAlignment(alignment);
    }

    public void setText(String text) {
        showField.setText(text);
    }

    public void setFont(int style, int size) {
        Font font = showField.getFont();
        showField.setFont(new Font(font.getFontName(), style, size));
    }

    public void setForeground(Color color) {
        if (showField != null) {
            showField.setForeground(color);
        }
        super.setForeground(color);
    }

    public void setCaretColor(Color c) {
        showField.setCaretColor(c);
    }

    public JTextField createLabel(int fontSize, int fontStyle, Color fontColor, int top, int left, int bottom, int right, boolean editable) {
        JTextField area = new JTextField();
        area.setBorder(new EmptyBorder(top, left, bottom, right));
        area.setOpaque(false);
        area.setForeground(fontColor);
        area.setHorizontalAlignment(JTextField.LEFT);
        // area.setLineWrap(true); // 激活自动换行功能
        SWUtils.setFontSize(area, fontSize, fontStyle);
        area.setEditable(editable);
        return area;
    }

    public void setFieldColor(Color fg) {
        if (this.showField != null) {
            this.showField.setForeground(fg);
        }
    }

    public synchronized void addFocusListener(FocusListener l) {
        this.showField.addFocusListener(l);
    }

    public String getText() {
        return this.showField.getText();
    }

}
