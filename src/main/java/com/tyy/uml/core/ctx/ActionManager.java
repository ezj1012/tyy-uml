package com.tyy.uml.core.ctx;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.JButton;

public interface ActionManager {

    public static final Map<String, BiConsumer<JButton, ActionEvent>> ACTIONS = new HashMap<>();

    default void registerAction(String key, BiConsumer<JButton, ActionEvent> ac) {
        ACTIONS.put(key, ac);
    }

    default ActionListener createAction(JButton btn, String key) {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                BiConsumer<JButton, ActionEvent> biConsumer = ACTIONS.get(key);
                biConsumer.accept(btn, e);
            }

        };
    }

}
