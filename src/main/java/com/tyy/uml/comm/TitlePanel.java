package com.tyy.uml.comm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.util.SWUtils;

public class TitlePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    JTextField showField;

    public TitlePanel() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        showField = createLabel(12, Font.PLAIN, Color.BLACK, 0, 5, 0, 0);
        this.add(showField, BorderLayout.CENTER);
        SWUtils.fixedHeight(this, 16);
    }

    public TitlePanel(int fixedHeight, Color fontColor) {
        this(fixedHeight, fontColor, 0, 0, 0, 0);
    }

    public TitlePanel(int fixedHeight, Color fontColor, int top, int left, int bottom, int right) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        showField = createLabel(fixedHeight - 4, Font.PLAIN, fontColor, top, left, bottom, right);
        this.add(showField, BorderLayout.CENTER);
        SWUtils.fixedHeight(this, fixedHeight);
    }

    public TitlePanel(int fontSize, int fontStyle, Color fontColor, int top, int left, int bottom, int right) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        showField = createLabel(fontSize, fontStyle, fontColor, top, left, bottom, right);
        this.add(showField, BorderLayout.CENTER);
        SWUtils.fixedHeight(this, fontSize + 4);
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

    public JTextField createLabel(int fontSize, int fontStyle, Color fontColor, int top, int left, int bottom, int right) {
        JTextField area = new JTextField();

        // CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), new EmptyBorder(top, left, bottom, right));
        // area.setBorder(compoundBorder);
        area.setBorder(new EmptyBorder(top, left, bottom, right));

        area.setEditable(false);
        area.setOpaque(false);
        area.setForeground(fontColor);
        area.setHorizontalAlignment(JTextField.LEFT);
        // area.setLineWrap(true); // 激活自动换行功能
        SWUtils.setFontSize(area, fontSize, fontStyle);
        return area;
    }

    public void setFieldColor(Color fg) {
        if (this.showField != null) {
            this.showField.setForeground(fg);
        }
    }

}
