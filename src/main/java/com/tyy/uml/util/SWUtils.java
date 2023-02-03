package com.tyy.uml.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class SWUtils {

    public static final EmptyBorder EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);

    public static void optimize(JComponent cn) {
        cn.setBorder(EMPTY_BORDER);
        cn.setLayout(new BorderLayout());
    }

    public static void optimize(JComponent cn, Border border, LayoutManager lm, Color backColor, boolean isOpaque) {
        cn.setBorder(border);
        cn.setLayout(lm);
        cn.setBackground(backColor);
        cn.setOpaque(isOpaque);
    }

    /**
     * 解析颜色
     * 
     * @param nm
     *            #FFFFFF
     * @param def
     *            解析失败返回
     * @return
     */
    public static Color decodeColor(String nm, Color def) {
        if (isEmpty(nm)) { return def; }
        try {
            return Color.decode(nm);
        } catch (Exception e) {
        }
        return def;
    }

    public static String toHex(Color color) {
        return toHex(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * RGB颜色值转换成十六进制颜色码
     *
     * @param r
     *            红(R)
     * @param g
     *            绿(G)
     * @param b
     *            蓝(B)
     * @return 返回字符串形式的 十六进制颜色码 如
     */
    public static String toHex(int r, int g, int b) {
        // rgb 小于 255
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) { throw new IllegalArgumentException("RGB must be 0~255!"); }
        return String.format("#%02X%02X%02X", r, g, b);
    }

    /**
     * 固定组件高度
     * 
     * @param cn
     * @param height
     */
    public static void fixedHeight(Component cn, int height) {
        fixedHeight(cn, height, null, null);
    }

    public static void setFontSize(Component cn, int size) {
        Font font = cn.getFont();
        Font f = new Font(font.getFontName(), font.getStyle(), size);
        cn.setFont(f);
    }

    public static void setFontSize(Component cn, int size, int style) {
        Font font = cn.getFont();
        Font f = new Font(font.getFontName(), style, size);
        cn.setFont(f);
    }

    public static void fixedAndNoBorder(JComponent cn, int width, int height) {
        fixed(cn, width, height);
        noBorder(cn);
    }

    public static void noBorder(JComponent cn) {
        cn.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    public static void fixed(Component cn, int width, int height) {
        Dimension size = new Dimension(width, height);
        cn.setMaximumSize(size);
        cn.setMinimumSize(size);
        cn.setPreferredSize(size);
        cn.setSize(size);
    }

    /**
     * 固定组件高度
     * 
     * @param cn
     * @param height
     * @param maxWidth
     * @param minWidth
     */
    public static void fixedHeight(Component cn, int height, Integer maxWidth, Integer minWidth) {
        Dimension maximumSize = cn.getMaximumSize();
        Dimension minimumSize = cn.getMinimumSize();
        Dimension preferredSize = cn.getPreferredSize();
        maximumSize.height = height;
        minimumSize.height = height;
        preferredSize.height = height;
        if (maxWidth != null) {
            maximumSize.width = maxWidth;
        }
        if (minWidth != null) {
            minimumSize.width = minWidth;
        }

        cn.setMaximumSize(maximumSize);
        cn.setMinimumSize(minimumSize);
        cn.setPreferredSize(preferredSize);
    }

    /**
     * 固定组件高度
     * 
     * @param cn
     * @param height
     * @param maxWidth
     * @param minWidth
     */
    public static void fixedWidth(Component cn, int width) {
        Dimension maximumSize = cn.getMaximumSize();
        Dimension minimumSize = cn.getMinimumSize();
        Dimension preferredSize = cn.getPreferredSize();
        maximumSize.width = width;
        minimumSize.width = width;
        // if (maxWidth != null) {
        // maximumSize.width = maxWidth;
        // }
        // if (minWidth != null) {
        // minimumSize.width = minWidth;
        // }
        preferredSize.width = width;

        cn.setMaximumSize(maximumSize);
        cn.setMinimumSize(minimumSize);
        cn.setPreferredSize(preferredSize);
    }

    public static void printSize(String name, Component cn) {
        printSize(name, cn, System.out);
    }

    public static void printSize(String name, Component cn, java.io.PrintStream out) {
        Dimension size = cn.getSize();
        Dimension pSize = cn.getPreferredSize();
        Dimension maxSize = cn.getMaximumSize();
        Dimension minSize = cn.getMinimumSize();
        out.println(name + " : size(" + size.width + "," + size.height + "), pSize(" + pSize.width + "," + pSize.height + "), maxSize(" + maxSize.width + "," + maxSize.height + "), minSize(" + minSize.width + "," + minSize.height + ") ");
    }

    public static int width(Component cn, String content) {
        if (isEmpty(content)) { return 0; }
        return cn.getFontMetrics(cn.getFont()).stringWidth(content);
    }

    public static JTextField createLabel(int fontSize, int fontStyle, Color fontColor, int top, int left, int bottom, int right) {
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

    public static boolean isEmpty(String content) {
        return content == null || content.trim().length() == 0;
    }

}
