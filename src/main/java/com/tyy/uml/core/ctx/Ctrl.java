package com.tyy.uml.core.ctx;

import com.tyy.uml.core.ctx.model.UMLProject;
import com.tyy.uml.core.ctx.model.UMLProjectData;
import com.tyy.uml.core.ctx.model.UMLWork;
import com.tyy.uml.core.gui.adapter.DKeyListener;

public interface Ctrl extends ActionManager, DKeyListener {

    void saveConfigs();

    void createProject(UMLProject project);

    UMLProjectData loadProject(UMLProject project, boolean peek);

    UMLProjectData getCurProject();

    UMLProjectData saveProject(UMLProjectData project);

    void delProject(UMLProject project);

    void refreshProject();

    UMLWork getWorkConfig();

    // void setEditorContentCenter();
    // void setEditorNextCenter();
    // void setEditorPrevCenter();
    // void showEditor(UMLInfoPanel info);
    // void hideEditor();
    // void toggleEditorList();
    // UMLGUIConfig getCfg();

    // void showSettings(boolean setting);

}