package com.tyy.uml.gui.frame;

import com.tyy.uml.context.UMLGUIConfig;
import com.tyy.uml.core.gui.frame.CustomFrame;
import com.tyy.uml.gui.ctrl.UMLControllerPane;

public class UMLFrame extends CustomFrame<UMLControllerPane, UMLGUIConfig> {

    private static final long serialVersionUID = 1L;

    public UMLFrame(UMLGUIConfig config) {
        super(config);
        // mainPanel = new UMLControllerPane(this, config);
        // setMainPanel(mainPanel);
    }

}
