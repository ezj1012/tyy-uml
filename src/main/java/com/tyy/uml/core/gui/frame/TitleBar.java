package com.tyy.uml.core.gui.frame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.util.Res;
import com.tyy.uml.util.SWUtils;

public class TitleBar extends JLayeredPane implements ComponentListener {

    private static final long serialVersionUID = 1L;

    private static final int ICON_SIZE = 24;

    private Color defaultColor = new Color(133, 133, 133);

    JFrame mainFrame;

    JFrameParameters parameters;

    JPanel iconPanel = new JPanel();

    ImageIcon imageIcon;

    JPanel butsPanel = new JPanel();

    /**
     * 最小化
     */
    JButton minBtn;

    JButton restoreBtn;

    /**
     * 关闭
     */
    JButton closeBut;

    private Boolean mainFrameResizable = null;

    private TitleBarContent baseContent;

    private TitleBarContent customContent;

    public TitleBar(JFrame mainFrame, JFrameParameters parameters) {
        this.mainFrame = mainFrame;
        this.parameters = parameters;
        this.setOpaque(false);
        this.setMinimumSize(new Dimension(parameters.getIconWidth() + parameters.getControlBoxWidth(), parameters.getTitleBarHeight()));
        this.addComponentListener(this);
        // setLayout(new BorderLayout());
        initIcon();
        initContent();
        initBtns();
    }

    private void initIcon() {
        iconPanel = new JPanel() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imageIcon != null) {
                    int x = (parameters.getIconWidth() - ICON_SIZE) / 2;
                    int y = (parameters.getTitleBarHeight() - ICON_SIZE) / 2;
                    g.drawImage(imageIcon.getImage(), x, y, ICON_SIZE, ICON_SIZE, this);
                }
            }

        };
        iconPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        iconPanel.setBounds(0, 0, parameters.getIconWidth(), parameters.getTitleBarHeight());
        this.add(iconPanel, JLayeredPane.MODAL_LAYER);
    }

    private void initContent() {
        baseContent = new TitleBarContent(parameters);
        baseContent.setBorder(new EmptyBorder(0, 0, 0, 0));
        baseContent.setOpaque(false);
        baseContent.setBounds(0, 0, getWidth(), getHeight());
        this.add(baseContent, JLayeredPane.DEFAULT_LAYER);
    }

    public void setTitleBarContent(TitleBarContent content) {
        if (customContent != null) {
            this.remove(customContent);
        }
        if (content != null) {
            this.remove(baseContent);
            this.add(customContent = content, JLayeredPane.DEFAULT_LAYER);
        } else {
            customContent = null;
            this.add(baseContent, JLayeredPane.DEFAULT_LAYER);
        }
    }

    public TitleBarContent getTitleBarContent() {
        return customContent != null ? customContent : baseContent;
    }

    private void initBtns() {
        minBtn = addBtn(1, e -> this.mainFrame.setExtendedState(Frame.ICONIFIED));
        restoreBtn = addBtn(2, e -> this.mainFrame.setExtendedState(this.mainFrame.getExtendedState() == Frame.MAXIMIZED_BOTH ? Frame.NORMAL : Frame.MAXIMIZED_BOTH));
        closeBut = addBtn(3, e -> this.mainFrame.setVisible(false));
        butsPanel.setBounds(0, 0, parameters.getControlBoxWidth(), parameters.getTitleBarHeight());
        butsPanel.setLayout(new BoxLayout(butsPanel, BoxLayout.LINE_AXIS));
        this.butsPanel.add(closeBut);
        this.add(butsPanel, JLayeredPane.MODAL_LAYER);
    }

    public void refreshFrameParameters(JFrameParameters frameParameters) {
        this.parameters = frameParameters;
        baseContent.refreshFrameParameters(frameParameters);
        if (customContent != null) {
            customContent.refreshFrameParameters(frameParameters);
        }
        this.refreshVisible();
    }

    public void refreshVisible() {
        Color back = SWUtils.decodeColor(parameters.getTitleBarBackground(), UMLGUIConfig.c333333);
        Color fontColor = SWUtils.decodeColor(parameters.getTitleBarFontColor(), UMLGUIConfig.cd4d4d4);
        this.setBackground(back);

        // 左侧图标
        imageIcon = Res.get().getImage(parameters.getFrameIcon());
        Image image = imageIcon == null ? null : imageIcon.getImage();
        this.mainFrame.setIconImage(image);
        iconPanel.setBackground(back);
        iconPanel.setBounds(0, 0, parameters.getIconWidth(), parameters.getTitleBarHeight());
        //
        baseContent.setBackground(back);
        baseContent.setForeground(fontColor);
        baseContent.setBounds(0, 0, getWidth(), getHeight());
        if (customContent != null) {
            customContent.setBackground(back);
            customContent.setForeground(fontColor);
            customContent.setBounds(0, 0, getWidth(), getHeight());
        }

        // 右侧按钮
        this.minBtn.setBackground(back);
        this.restoreBtn.setBackground(back);
        this.closeBut.setBackground(back);

        refreshBtns();
        this.setMinimumSize(new Dimension(parameters.getIconWidth() + parameters.getControlBoxWidth(), parameters.getTitleBarHeight()));
        this.setPreferredSize(new Dimension(0, parameters.getTitleBarHeight()));
    }

    private void refreshBtns() {
        if (mainFrameResizable == null || mainFrameResizable != mainFrame.isResizable()) {
            int l = parameters.getControlWidth() * 3;
            butsPanel.removeAll();
            butsPanel.add(minBtn);
            if (!mainFrame.isResizable()) {
                l = parameters.getControlWidth() * 2;
                mainFrameResizable = false;
            } else {
                mainFrameResizable = true;
                butsPanel.add(restoreBtn);
            }
            butsPanel.add(closeBut);
            parameters.setControlBoxWidth(l);
            this.revalidate();
        }
        butsPanel.setBounds(getWidth() - parameters.getControlBoxWidth(), 0, parameters.getControlBoxWidth(), parameters.getTitleBarHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        baseContent.setBounds(0, 0, getWidth(), getHeight());
        refreshBtns();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    /**
     * 
     * @param type
     *            1:最小化,2:最大化,3:还原,4:关闭.
     * @param l
     * @return
     */
    private JButton addBtn(int type, ActionListener l) {
        JButton jButton = new JButton("" + type) {

            private static final long serialVersionUID = 1L;

            @Override
            public Insets getInsets() {
                return new Insets(0, 0, 0, 0);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int width = getWidth();
                int height = getHeight();

                ButtonModel model = getModel();

                Graphics2D g2d = (Graphics2D) g.create();

                g2d.setColor(getBackground());
                g2d.fillRect(0, 0, width, height);

                if (model.isRollover()) {
                    g.setColor(getForeground());
                    g.fillRect(0, 0, width, height);
                }

                g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (model.isRollover()) {
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(defaultColor);
                }

                Point markStPt = new Point(width / 2 - 5, height / 2 - 5);
                Point markEnPt = new Point(width / 2 + 5, height / 2 + 5);

                switch (type) {
                    case 1 :
                        markStPt = new Point(width / 2 - 5, height / 2);
                        markEnPt = new Point(width / 2 + 5, height / 2);
                        g2d.drawLine(markStPt.x, markStPt.y, markEnPt.x, markEnPt.y);
                        break;
                    case 2 :
                        if (mainFrame.getExtendedState() == Frame.NORMAL) {
                            g2d.drawRect(markStPt.x, markStPt.y, 10, 10);
                        } else {
                            markStPt = new Point(width / 2 - 5, height / 2 - 3 + 1);
                            g2d.drawRect(markStPt.x, markStPt.y, 8, 8);
                            markStPt = new Point(width / 2 + 5, height / 2 - 5 + 1);
                            g2d.drawLine(markStPt.x, markStPt.y, markStPt.x - 8, markStPt.y);
                            g2d.drawLine(markStPt.x, markStPt.y, markStPt.x, markStPt.y + 8);
                            g2d.drawLine(markStPt.x - 8, markStPt.y, markStPt.x - 8, markStPt.y + 2);
                            g2d.drawLine(markStPt.x, markStPt.y + 8, markStPt.x - 2, markStPt.y + 8);
                        }
                        break;
                    case 3 :
                        g2d.drawLine(markStPt.x, markStPt.y, markEnPt.x, markEnPt.y);
                        markStPt = new Point(width / 2 + 5, height / 2 - 5);
                        markEnPt = new Point(width / 2 - 5, height / 2 + 5);
                        g2d.drawLine(markStPt.x, markStPt.y, markEnPt.x, markEnPt.y);
                        break;
                }
                g2d.dispose();
            }

        };
        jButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        jButton.setSize(parameters.getControlWidth(), parameters.getTitleBarHeight());
        jButton.setMaximumSize(new Dimension(parameters.getControlWidth(), parameters.getTitleBarHeight()));
        jButton.setMinimumSize(new Dimension(parameters.getControlWidth(), parameters.getTitleBarHeight()));
        jButton.setPreferredSize(new Dimension(parameters.getControlWidth(), parameters.getTitleBarHeight()));

        jButton.setForeground(new Color(0, 0, 0, 64));
        jButton.addActionListener(l);
        return jButton;
    }

}
