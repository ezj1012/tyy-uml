package com.tyy.uml.core.gui.frame;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import com.tyy.uml.gui.comm.SingleColorIcon;

/**
 * 系统托盘
 * 
 * @Author : XiongJian 2022/5/7 18:49
 */
public class Tray {

    private SystemTray tray;

    private TrayIcon trayIcon;

    private boolean added = false;

    CustomFrame frame;

    public Tray(CustomFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        if (!SystemTray.isSupported()) { return; }
        tray = SystemTray.getSystemTray();
        ImageIcon imageIcon = new SingleColorIcon("list.png").getImageIcon(20, 20, Color.WHITE);
        // 创建点击图标时的弹出菜单
        PopupMenu popupMenu = new PopupMenu();

        MenuItem exitItem = new MenuItem("close");

        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });

        popupMenu.add(exitItem);
        trayIcon = new TrayIcon(imageIcon.getImage(), "测试 ", popupMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(e -> showFrame());
    }

    public synchronized void showFrame() {
        if (!frame.isVisible()) {
            frame.doVisible();
        }
        if (frame.getState() == Frame.ICONIFIED) {
            frame.setExtendedState(Frame.NORMAL);
        }
        frame.requestFocus();
    }

    public synchronized void setVisible(boolean v) {
        if (v) {
            if (!added) {
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                }
                added = true;
            }
        } else {
            if (added) {
                tray.remove(trayIcon);
                added = false;
            }
        }
    }

    public boolean isVisible() {
        return added;
    }

}