package com.tyy.uml.core.gui.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.function.BiConsumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.ctx.UMLContext;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.gui.comm.TextButton;
import com.tyy.uml.gui.comm.TitleLabelPanel;
import com.tyy.uml.util.BeanHelper.BeanObservale;
import com.tyy.uml.util.BeanHelper.BeanObserver;
import com.tyy.uml.util.SWUtils;

public class TitleBarContent extends JLayeredPane implements DComponentListener, BeanObserver {

    private static final long serialVersionUID = 1L;

    protected JFrameParameters parameters;

    protected List<JButton> rightBtns = new ArrayList<>();

    protected List<JButton> leftBtns = new ArrayList<>();

    protected TitleLabelPanel fieldPanel;

    protected boolean appendLeft = true;

    protected boolean appendRight = false;

    public TitleBarContent(JFrameParameters parameters) {
        this.parameters = parameters;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        SWUtils.fixedHeight(this, parameters.getTitleBarHeight());
        this.initCentent(JLabel.CENTER);
        this.addComponentListener(this);
    }

    private void initCentent(int alignment) {
        fieldPanel = new TitleLabelPanel(parameters.getTitleBarHeight(), Color.WHITE, 0, 10, 0, 0);
        fieldPanel.setFont(Font.PLAIN, parameters.getTitleBarHeight() - 8);
        setHorizontalAlignment(alignment);
        this.add(fieldPanel, JLayeredPane.DEFAULT_LAYER);
    }

    public int getLeftButtonWidth() {
        int w = 0;
        for (JButton btn : leftBtns) {
            w += btn.getWidth();
        }
        return w;
    }

    public int getRightButtonWidth() {
        int w = 0;
        for (JButton btn : rightBtns) {
            w += btn.getWidth();
        }
        return w;
    }

    public JButton addLeftButton(String text, String actionKey) {
        TextButton tb = new TextButton(text);
        ActionListener al = UMLContext.getContext().getCtrl().createAction(tb, actionKey);
        return addLeftButton(tb, al);
    }

    public JButton addLeftButton(String text, BiConsumer<JButton, ActionEvent> actionListener) {
        TextButton tb = new TextButton(text);
        return addLeftButton(tb, e -> {
            actionListener.accept(tb, e);
        });
    }

    private JButton addLeftButton(TextButton tb, ActionListener al) {
        this.leftBtns.add(tb);
        if (al != null) {
            tb.addActionListener(al);
        }
        this.leftBtns.add(tb);
        this.add(tb, JLayeredPane.MODAL_LAYER);
        refreshButtonPos();
        return tb;
    }

    private void refreshButtonPos() {
        int x = parameters.getIconWidth();

        for (JButton btn : leftBtns) {
            int y = (parameters.getTitleBarHeight() - btn.getHeight()) / 2 + 2;
            btn.setBounds(x, y, btn.getWidth(), btn.getHeight());
            x += btn.getWidth();
        }
    }

    public void refreshFrameParameters(JFrameParameters frameParameters) {
        if (this.parameters != null && this.parameters instanceof BeanObservale) {
            ((BeanObservale) this.parameters).deleteObserver(this);
        }
        this.parameters = frameParameters;
        if (this.parameters != null && this.parameters instanceof BeanObservale) {
            ((BeanObservale) this.parameters).addObserver(this);
        }

        SWUtils.fixedHeight(this, parameters.getTitleBarHeight());
    }

    @Override
    public void update(Observable o, Object source, String prop, Object oldValue, Object newValue) {
        if (!(source instanceof JFrameParameters)) { return; }
        if (isNullOrEq(prop, "frameTitle")) {
            setText(newValue == null ? "" : newValue.toString());
        }

        if (isNullOrEq(prop, "iconWidth")) {

        }
    }

    @Override
    public void setBackground(Color bg) {
        if (fieldPanel != null) {
            fieldPanel.setOpaque(true);
            fieldPanel.setBackground(bg);
        }
        if (leftBtns != null) {
            for (JButton jButton : leftBtns) {
                jButton.setBackground(bg);
            }
        }
        if (rightBtns != null) {
            for (JButton jButton : rightBtns) {
                jButton.setBackground(bg);
            }
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
        if (leftBtns != null) {
            for (JButton jButton : leftBtns) {
                jButton.setForeground(color);
            }
        }
        if (rightBtns != null) {
            for (JButton jButton : rightBtns) {
                jButton.setForeground(color);
            }
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
        refreshButtonPos();
        // for (int i = 0; i < leftBtns.size(); i++) {
        // leftBtns.get(i).setBounds(5 + i * parameters.getTitleBarHeight(), 0, parameters.getTitleBarHeight(), parameters.getTitleBarHeight());
        // }
        // for (int i = rightBtns.size(); i > 0; i--) {
        // rightBtns.get(rightBtns.size() - i).setBounds(w - parameters.getTitleBarHeight() * i, 0, parameters.getTitleBarHeight(), parameters.getTitleBarHeight());
        // }

        fieldPanel.setBounds(0, 0, w, h);
        fieldPanel.revalidate();
    }

}
