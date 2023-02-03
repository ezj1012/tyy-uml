package com.tyy.uml.adapter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface DMouseListener extends MouseListener, MouseMotionListener {

    public default void mouseClicked(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public default void mousePressed(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public default void mouseReleased(MouseEvent e) {
    }

    /**
     * Invoked when the mouse enters a component.
     */
    public default void mouseEntered(MouseEvent e) {
    }

    /**
     * Invoked when the mouse exits a component.
     */
    public default void mouseExited(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged. <code>MOUSE_DRAGGED</code> events will continue to be delivered to the component where the drag originated until the mouse button is released (regardless of whether the mouse position is within the
     * bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations, <code>MOUSE_DRAGGED</code> events may not be delivered during a native Drag&amp;Drop operation.
     */
    public default void mouseDragged(MouseEvent e) {
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component but no buttons have been pushed.
     */
    public default void mouseMoved(MouseEvent e) {
    }

}
