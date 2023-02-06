package com.tyy.uml.context;

import com.tyy.uml.bean.UMLProjectData;
import com.tyy.uml.bean.UMLProject;
import com.tyy.uml.canvas.UMLScrollHelper;
import com.tyy.uml.core.gui.adapter.DKeyListener;
import com.tyy.uml.gui.editor.UMLEditor;
import com.tyy.uml.gui.info.UMLInfoPanel;

public interface Ctrl extends DKeyListener {

    void saveConfigs();

    void createProject(UMLProject project);

    UMLProjectData loadProject(UMLProject project);

    UMLProjectData saveProject(UMLProjectData project);

    void refreshProject();

    UMLScrollHelper getScrollHelper();

    UMLEditor setEditorContent(UMLInfoPanel info);

    void hideEditor();

    void toggleEditorList();

    UMLGUIConfig getCfg();

}