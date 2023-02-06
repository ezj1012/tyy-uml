package com.tyy.uml.gui.frame;

import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.core.gui.frame.CustomFrame;

public class UMLFrame extends CustomFrame<UMLMainPane, UMLGUIConfig> {

    private static final long serialVersionUID = 1L;

    public UMLFrame(UMLGUIConfig config) {
        super(config);
        // mainPanel = new UMLControllerPane(this, config);
        // setMainPanel(mainPanel);
    }

}
