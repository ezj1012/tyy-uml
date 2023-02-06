package com.tyy.uml.core.gui.adapter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface DKeyListener extends KeyListener {

    /**
     * Invoked when a key has been typed. See the class description for {@link KeyEvent} for a definition of a key typed event.
     */
    public default void keyTyped(KeyEvent e) {
    }

    /**
     * Invoked when a key has been pressed. See the class description for {@link KeyEvent} for a definition of a key pressed event.
     */
    public default void keyPressed(KeyEvent e) {
    }

    /**
     * Invoked when a key has been released. See the class description for {@link KeyEvent} for a definition of a key released event.
     */
    public default void keyReleased(KeyEvent e) {
    }

}