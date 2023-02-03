package com.tyy.uml.adapter;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public interface DComponentListener extends ComponentListener {

    /**
     * Invoked when the component's size changes.
     */
    public default void componentResized(ComponentEvent e) {
    }

    /**
     * Invoked when the component's position changes.
     */
    public default void componentMoved(ComponentEvent e) {
    }

    /**
     * Invoked when the component has been made visible.
     */
    public default void componentShown(ComponentEvent e) {
    }

    /**
     * Invoked when the component has been made invisible.
     */
    public default void componentHidden(ComponentEvent e) {
    }

}
