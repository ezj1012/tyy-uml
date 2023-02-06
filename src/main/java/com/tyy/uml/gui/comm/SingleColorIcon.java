package com.tyy.uml.gui.comm;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.tyy.uml.util.Res;

import sun.awt.image.ToolkitImage;

@SuppressWarnings("restriction")
public class SingleColorIcon {

    private int w;

    private int h;

    private ToolkitImage icon;

    private ImageIcon sicon;

    public SingleColorIcon(String icon) {
        this(Res.get().getImage(icon));
    }

    public SingleColorIcon(ImageIcon icon) {
        this(icon, icon.getIconWidth(), icon.getIconHeight());
    }

    public SingleColorIcon(String icon, int w, int h) {
        this(Res.get().getImage(icon), w, h);
    }

    public SingleColorIcon(ImageIcon icon, int w, int h) {
        this.sicon = icon;
        this.icon = (ToolkitImage) icon.getImage();
        this.w = w;
        this.h = h;
    }

    public ImageIcon getImageIcon() {
        return getImageIcon(w, h);
    }

    public ImageIcon getImageIcon(Color fontColor) {
        return getImageIcon(w, h, fontColor);
    }

    public ImageIcon getImageIcon(int w, int h) {
        return getImageIcon(w, h, null);
    }

    public ImageIcon getImageIcon(int w, int h, Color fontColor) {
        if (w == icon.getWidth() && h == icon.getHeight() && fontColor == null) { return sicon; }

        BufferedImage img = icon.getBufferedImage();
        BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = b.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, w, h, null);

        g.dispose();
        b.flush();
        if (fontColor != null) {
            for (int i = 0; i < b.getWidth(); i++) {
                for (int j = 0; j < b.getHeight(); j++) {
                    if ((b.getRGB(i, j) >> 24) != 0x00) {
                        b.setRGB(i, j, fontColor.getRGB());
                    }
                }
            }
            b.flush();
        }

        ImageIcon imageIcon = new ImageIcon(b);
        return imageIcon;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

}
