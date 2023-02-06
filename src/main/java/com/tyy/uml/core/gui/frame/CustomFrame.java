package com.tyy.uml.core.gui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

public class CustomFrame<T extends Component, C extends JFrameParameters> extends JFrame {

    private static final long serialVersionUID = 1L;

    protected JFrameProc frameProc;

    protected TitleBar titleBar;

    protected Tray tray;

    protected T mainPanel;

    protected C frameParameters;

    public CustomFrame(C frameParameters) {
        frameProc = new JFrameProc(frameParameters);
        titleBar = new TitleBar(this, frameParameters);
        tray = new Tray(this);
        this.getContentPane().setBackground(new Color(0, 0, 0, 0));
        this.getContentPane().add(titleBar, BorderLayout.NORTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addWindowStateListener(new WindowAdapter() {

            @Override
            public void windowStateChanged(WindowEvent e) {
                Container contentPane = getContentPane();
                JComponent cntPane = null;
                if (!(contentPane instanceof JComponent)) { return; }
                cntPane = (JComponent) contentPane;

                if (e.getNewState() == MAXIMIZED_BOTH) {
                    final int padding = 8;
                    cntPane.setBorder(new EmptyBorder(padding, padding, padding, padding));
                } else if (e.getOldState() == MAXIMIZED_BOTH) {
                    cntPane.setBorder(new EmptyBorder(0, 0, 0, 0));
                }
            }

        });
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public Tray getTray() {
        return tray;
    }

    /**
     * @deprecated {@link #doVisible()}
     */
    @Deprecated
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public void doVisible() {
        this.titleBar.refreshVisible();
        this.pack();
        super.setVisible(true);
        this.frameProc.init(this);
    }

    public void refreshFrameParameters(C c) {
        this.frameParameters = c;
        this.titleBar.refreshFrameParameters(c);
        this.frameProc.refreshFrameParameters(c);
    }

    public void setMainPanel(T mainPanel) {
        this.mainPanel = mainPanel;
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public T getMainPanel() {
        return mainPanel;
    }

}
