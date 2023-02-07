package com.tyy.uml.core.gui.adapter;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public interface DFocusListener extends FocusListener {

    /**
     *
     * Invoked when
     * 
     * a component
     * 
     * gains the
     * 
     * keyboard focus.
     */

    public default void focusGained(FocusEvent e) {
    }

    /**
     * Invoked when a component loses the keyboard focus.
     */
    public default void focusLost(FocusEvent e) {
    }

}
