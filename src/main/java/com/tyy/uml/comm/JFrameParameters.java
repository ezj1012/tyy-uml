package com.tyy.uml.comm;

import java.util.concurrent.atomic.AtomicInteger;

public class JFrameParameters {

    /**
     * 标题栏高度,jna判断事件需要
     */
    private AtomicInteger titleBarHeight = new AtomicInteger(27);

    /**
     * 标题栏图标宽度
     */
    private AtomicInteger iconWidth = new AtomicInteger(48);

    /**
     * titleBar 控制器(最大,最小,关闭)宽度,jna判断事件需要
     */
    private AtomicInteger controlWidth = new AtomicInteger(46);

    /**
     * titleBar 控制器(最大,最小,关闭)宽度,jna判断事件需要
     */
    private AtomicInteger controlBoxWidth = new AtomicInteger(138);

    public JFrameParameters() {
    }

    public int getControlWidth() {
        return controlWidth.get();
    }

    public void setControlWidth(int value) {
        controlWidth.set(value);
    }

    public int getControlBoxWidth() {
        return controlBoxWidth.get();
    }

    public void setControlBoxWidth(int value) {
        controlBoxWidth.set(value);
    }

    public int getTitleBarHeight() {
        return titleBarHeight.get();
    }

    public void setTitleBarHeight(int value) {
        titleBarHeight.set(value);
    }

    public int getIconWidth() {
        return iconWidth.get();
    }

    public void setIconWidth(int value) {
        iconWidth.set(value);
    }

}
