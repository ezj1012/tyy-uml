package com.tyy.uml.util;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JButton;

public class Buts {

    public static JButton create(int w, int h, ActionListener l, final BiConsumer<JButton, Graphics> gg) {
        JButton but = new JButton() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                gg.accept(this, g);
            }

        };
        SWUtils.fixedAndNoBorder(but, w, h);

        if (l != null) {
            but.addActionListener(l);
        }
        return but;
    }

}
