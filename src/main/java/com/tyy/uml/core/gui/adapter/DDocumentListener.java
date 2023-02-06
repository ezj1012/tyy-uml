package com.tyy.uml.core.gui.adapter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public interface DDocumentListener extends DocumentListener {

    @Override
    public default void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public default void removeUpdate(DocumentEvent e) {
        System.out.println(e.getLength());
        changedUpdate(e);
    }

    @Override
    public default void changedUpdate(DocumentEvent e) {
        try {
            String newValue = e.getDocument().getText(0, e.getDocument().getLength());
            changed(e, newValue);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    public void changed(DocumentEvent e, String newValue);

}
