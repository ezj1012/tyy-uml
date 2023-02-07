package com.tyy.uml.context;

import com.tyy.uml.bean.UMLProject;
import com.tyy.uml.bean.UMLProjectData;
import com.tyy.uml.core.gui.adapter.DKeyListener;
import com.tyy.uml.gui.canvas.UMLInfoPanel;
import com.tyy.uml.gui.canvas.UMLScrollHelper;

public interface Ctrl extends DKeyListener {

    void saveConfigs();

    void createProject(UMLProject project);

    UMLProjectData loadProject(UMLProject project, boolean peek);

    UMLProjectData getCurProject();

    UMLProjectData saveProject(UMLProjectData project);

    void delProject(UMLProject project);

    void refreshProject();

    UMLScrollHelper getScrollHelper();

    void setEditorContentCenter();

    void showEditor(UMLInfoPanel info);

    void hideEditor();

    void toggleEditorList();

    UMLGUIConfig getCfg();

    void showSettings(boolean setting);

}