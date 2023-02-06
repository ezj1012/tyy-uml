package com.tyy.uml.core.gui.frame;

import static com.sun.jna.platform.win32.WinUser.SWP_FRAMECHANGED;
import static com.sun.jna.platform.win32.WinUser.SWP_NOMOVE;
import static com.sun.jna.platform.win32.WinUser.SWP_NOSIZE;
import static com.sun.jna.platform.win32.WinUser.SWP_NOZORDER;
import static com.sun.jna.platform.win32.WinUser.WM_DESTROY;

import javax.swing.JFrame;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.W32APIOptions;

public class JFrameProc implements WinUser.WindowProc {

    interface User32Ex extends User32 {

        LONG_PTR SetWindowLong(HWND hWnd, int nIndex, WindowProc wndProc);

        LONG_PTR SetWindowLongPtr(HWND hWnd, int nIndex, WindowProc wndProc);

        LRESULT CallWindowProc(LONG_PTR proc, HWND hWnd, int uMsg, WPARAM uParam, LPARAM lParam);

    }

    // 计算窗口事件msg
    final int WM_NCCALCSIZE = 0x0083;

    // 计算
    final int WM_NCHITTEST = 0x0084;

    // https://learn.microsoft.com/en-us/windows/win32/winmsg/wm-sizing
    // 发送到用户正在调整大小的窗口。通过处理此消息，应用进程可以监视拖动矩形的大小和位置，并在需要时更改其大小或位置。
    final int WM_SIZING = 0x0214;

    final int WM_GETMINMAXINFO = 0x0024;

    // 为窗口设定一个新的处理函数。
    final int GWLP_WNDPROC = -4;

    WinDef.HWND hwnd = new WinDef.HWND();

    BaseTSD.LONG_PTR defWndProc;

    User32Ex user32Ex;

    JFrameParameters frameParameters;

    JFrame frame;

    public JFrameProc(JFrameParameters frameParameters) {
        // this.frameParameters = new JFrameParameters();
        this.frameParameters = frameParameters;
        user32Ex = (User32Ex) Native.load("user32", User32Ex.class, W32APIOptions.DEFAULT_OPTIONS);
    }

    public JFrameParameters getJFrameParameters() {
        return frameParameters;
    }

    public void refreshFrameParameters(JFrameParameters frameParameters) {
        this.frameParameters = frameParameters;
    }

    public void init(JFrame frame) {
        if (frame == null) {
            this.frame = frame;
            return;
        }
        if (this.frame == frame) { return; }
        this.frame = frame;
        this.hwnd = getHwnd(frame);

        // 为窗口设定一个新的处理函数。就在本类
        if (is64Bit()) {
            defWndProc = user32Ex.SetWindowLongPtr(hwnd, GWLP_WNDPROC, this);
        } else {
            defWndProc = user32Ex.SetWindowLong(hwnd, GWLP_WNDPROC, this);
        }
        user32Ex.SetWindowPos(hwnd, hwnd, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_FRAMECHANGED);
    }

    @Override
    public LRESULT callback(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam) {
        switch (uMsg) {
            case WM_NCCALCSIZE :
                return new LRESULT(0);
            case WM_DESTROY :
                if (is64Bit()) {
                    user32Ex.SetWindowLongPtr(hwnd, GWLP_WNDPROC, defWndProc.toPointer());
                } else {
                    user32Ex.SetWindowLong(hwnd, GWLP_WNDPROC, defWndProc.intValue());
                }
                return new LRESULT(0);
            case WM_NCHITTEST :
                LRESULT lresult = this.borderLessHitTest(hwnd, uMsg, wParam, lParam);
                if (lresult.intValue() != new LRESULT(0).intValue()) { return lresult; }
            default :
                return user32Ex.CallWindowProc(defWndProc, hwnd, uMsg, wParam, lParam);
        }
    }

    private LRESULT borderLessHitTest(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam) {
        int borderThickness = 4;
        WinDef.POINT ptMouse = new WinDef.POINT();
        RECT rcWindow = new RECT();
        User32.INSTANCE.GetCursorPos(ptMouse);
        User32.INSTANCE.GetWindowRect(hwnd, rcWindow);

        int uRow = 1, uCol = 1;
        /** 拖动大小 */
        boolean onResizeBorder = false, onFrameDrag = false;

        int topOffset = frameParameters.getTitleBarHeight() == 0 ? borderThickness : frameParameters.getTitleBarHeight();
        if (ptMouse.y >= rcWindow.top && ptMouse.y < rcWindow.top + topOffset) {
            // 拖动大小
            onResizeBorder = (ptMouse.y < (rcWindow.top + borderThickness)); // Top Resizing
            if (!onResizeBorder) {
                // 拖动窗口和双击判断
                onFrameDrag = (ptMouse.y <= rcWindow.top + frameParameters.getTitleBarHeight()) && //
                        (ptMouse.x < (rcWindow.right - (frameParameters.getControlBoxWidth())));
            }
            uRow = 0;
        } else if (ptMouse.y < rcWindow.bottom && ptMouse.y >= rcWindow.bottom - borderThickness) {
            uRow = 2;
        }
        if (ptMouse.x >= rcWindow.left && ptMouse.x < rcWindow.left + borderThickness) {
            uCol = 0;
        } else if (ptMouse.x < rcWindow.right && ptMouse.x >= rcWindow.right - borderThickness) {
            uCol = 2;
        }
        final int HTTOPLEFT = 13, HTTOP = 12, HTCAPTION = 2, HTTOPRIGHT = 14, //
                HTLEFT = 10, HTNOWHERE = 0, HTRIGHT = 11, //
                HTBOTTOMLEFT = 16, HTBOTTOM = 15, HTBOTTOMRIGHT = 17
        // , HTSYSMENU = 3
        ;

        int[][] hitTests = {
                // 左上拖动大小(13), 拖动大小(12) | 拖动窗口(2) | 不动(0),右上侧拖动大小(14)
                {HTTOPLEFT, onResizeBorder ? HTTOP : onFrameDrag ? HTCAPTION : HTNOWHERE, HTTOPRIGHT}, //
                // 左侧拖动大小(10) , 不动(0) , 右侧拖动大小(11)
                {HTLEFT, HTNOWHERE, HTRIGHT}, //
                // 左下拖动大小(16),下拖动大小(15),右下拖动大小(17)
                {HTBOTTOMLEFT, HTBOTTOM, HTBOTTOMRIGHT}, //
        };

        return new LRESULT(hitTests[uRow][uCol]);
    }

    private WinDef.HWND getHwnd(JFrame frame) {
        WinDef.HWND hwnd = new WinDef.HWND();
        hwnd.setPointer(Native.getComponentPointer(frame));
        return hwnd;
    }

    public static final boolean is64Bit() {

        String model = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
        if (model != null) { return "64".equals(model); }
        return false;
    }

}