package com.tyy.uml.gui.comm;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.gui.adapter.DMouseListener;
import com.tyy.uml.util.SWUtils;

public class AbsMovable extends JPanel implements DMouseListener {

    private static final long serialVersionUID = 1L;

    final static ExecutorService executor = Executors.newFixedThreadPool(20, r -> new Thread(r, "test"));

    public AbsMovable() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    Future<?> oneClick;

    private volatile boolean oneClickRun = false;

    /**
     * @deprecated {@link #mouseOneClicked(MouseEvent)} {@link #mouseDblClicked(MouseEvent, int)}
     */
    @Override
    @Deprecated
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() < 2) {
            oneClick = executor.submit(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e1) {
                    return;
                }
                try {
                    oneClickRun = true;
                    mouseOneClicked(e);
                } finally {
                    oneClickRun = false;
                }
            });
        } else {
            if (oneClick != null && !oneClick.isDone()) {
                oneClick.cancel(true);
                oneClick = null;
            }
            if (!oneClickRun) {
                mouseDblClicked(e, e.getClickCount());
            }
        }
    }

    public void mouseOneClicked(MouseEvent e) {
    }

    public void mouseDblClicked(MouseEvent e, int clickCount) {
    }

    protected int pressedX = 0;

    protected int pressedY = 0;

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

    public JTextField createLabel(int fontSize, int fontStyle, Color fontColor) {
        return createLabel(fontSize, fontStyle, fontColor, 0, 0, 0, 0);
    }

    public JTextField createLabel(int fontSize, int fontStyle, Color fontColor, int top, int left, int bottom, int right) {
        JTextField area = new JTextField();
        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), new EmptyBorder(top, left, bottom, right));
        // area.setBorder(new EmptyBorder(top, left, bottom, right));
        area.setBorder(compoundBorder);

        area.setEditable(false);
        area.setOpaque(false);
        area.setForeground(fontColor);
        area.setHorizontalAlignment(JTextField.CENTER);
        // area.setLineWrap(true); // 激活自动换行功能
        SWUtils.setFontSize(area, fontSize, fontStyle);
        return area;
    }

}
