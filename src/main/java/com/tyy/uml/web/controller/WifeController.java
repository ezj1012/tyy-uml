package com.tyy.uml.web.controller;

import java.util.Locale;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tyy.uml.core.ctx.UMLContext;
import com.tyy.uml.gui.UMLFrame;

@RestController
public class WifeController {

    @GetMapping("/message")
    public String callMe() {
        UMLFrame frame = UMLContext.getContext().getFrame();
        synchronized (this) {
            frame.getTray().showFrame();
        }
        frame.requestFocus();
        frame.setLocationRelativeTo(null);
        new Thread(() -> {
            try {
                frame.setAlwaysOnTop(true);
                int step = 10;
                for (int i = 0; i < step / 2; i++) {
                    int x = frame.getX() + 1;
                    int y = frame.getY();
                    frame.setLocation(x, y);
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                    }
                }
                for (int i = 0; i < 40; i++) {
                    int t = i / step;
                    int o = t % 2 == 0 ? -1 : 1;
                    int x = frame.getX() + o;
                    int y = frame.getY();
                    frame.setLocation(x, y);
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                    }
                }
            } finally {
                frame.setAlwaysOnTop(false);
            }
        }).start();

        return "收到";
    }

}
