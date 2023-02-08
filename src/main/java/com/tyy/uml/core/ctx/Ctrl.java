package com.tyy.uml.core.ctx;

import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.core.ctx.model.UMLProject;
import com.tyy.uml.core.ctx.model.UMLProjectData;
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

    void setEditorNextCenter();

    void setEditorPrevCenter();

    void showEditor(UMLInfoPanel info);

    void hideEditor();

    void toggleEditorList();

    UMLGUIConfig getCfg();

    void showSettings(boolean setting);

}