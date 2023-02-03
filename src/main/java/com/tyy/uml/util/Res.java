package com.tyy.uml.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import org.springframework.core.io.ClassPathResource;

public class Res {

    private static Res res;

    public Res() {
        res = this;
    }

    public static Res get() {
        if (Res.res == null) {
            synchronized (Res.class) {
                if (Res.res == null) {
                    new Res();
                }
            }
        }
        return Res.res;
    }

    // @Autowired
    // CfgSvc cfgSvc;

    @SuppressWarnings("deprecation")
    public ImageIcon getImage(File file) {
        try {
            return new ImageIcon(file.toURL());
        } catch (IOException e) {
            return null;
        }
    }

    public ImageIcon getImage(String key) {
        // CfgImage image = cfgSvc.getImage(key);
        // if (image == null) {
        // TODO
        // return null;
        // }
        // ClassPathResource resource = new ClassPathResource(image.getUrl());
        return loadLocal(key);
    }

    public Image getImg(String key) {
        ImageIcon image = getImage(key);
        return image == null ? null : image.getImage();
    }

    private ImageIcon loadLocal(String key) {
        ClassPathResource resource = new ClassPathResource("static/" + key);
        try {
            return new ImageIcon(resource.getURL());
        } catch (IOException e) {
            return null;
        }
    }

}
