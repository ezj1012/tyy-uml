package com.tyy.uml.editor;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.adapter.DComponentListener;
import com.tyy.uml.adapter.DMouseListener;
import com.tyy.uml.bean.UMLConfig;
import com.tyy.uml.comm.FieldPanel;
import com.tyy.uml.main.Ctrl;
import com.tyy.uml.util.Buts;
import com.tyy.uml.util.SWUtils;

public class UMLTitle extends JLayeredPane implements DMouseListener, DComponentListener {

    private static final long serialVersionUID = 1L;

    protected int pressedX = 0;

    protected int pressedY = 0;

    protected boolean fixedPos = false;

    Ctrl ctrl;

    UMLEditor editor;

    JPanel btns;

    FieldPanel fieldPanel;

    public UMLTitle(Ctrl ctrl, UMLEditor editor) {
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.ctrl = ctrl;
        this.editor = editor;
        SWUtils.fixedHeight(this, 20);
        this.initCentent();
        this.initBtn();
        this.addComponentListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.fieldPanel.addMouseListener(this);
        this.fieldPanel.addMouseMotionListener(this);
    }

    private void initBtn() {
        btns = new JPanel();
        btns.setLayout(new BoxLayout(btns, BoxLayout.X_AXIS));
        btns.setBackground(Color.RED);
        SWUtils.optimize(btns);
        JButton fixedBtn = Buts.create(20, 20, null, (btn, g) -> {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, btn.getWidth(), btn.getHeight());
            
            // System.out.println(this.getParent().getBackground());
            // System.out.println(btn.getWidth() + " " + btn.getHeight());
            // g.setColor(Color.WHITE);
            // g.fillRect(0, 0, 20, 20);
            g.setColor(Color.BLACK);

            g.fillArc(4, 4, 12, 12, 0, 360);

        });

        // JButton b = new JButton("你好");
        btns.add(fixedBtn);
        // btns.add(b);
        this.add(btns, JLayeredPane.PALETTE_LAYER);
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
        fieldPanel = new FieldPanel(20, fontColor);
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
        int w = btns.getComponentCount() * 20;
        btns.setBounds(e.getComponent().getWidth() - w, 0, w, 20);
    }

}
