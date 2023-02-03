package com.tyy.uml.main;

import java.io.File;

import com.tyy.uml.adapter.DKeyListener;
import com.tyy.uml.bean.SaveData;
import com.tyy.uml.bean.UMLConfig;
import com.tyy.uml.editor.UMLEditor;
import com.tyy.uml.info.UMLInfoPanel;

public interface Ctrl extends DKeyListener {

    SaveData loadDatas(File file);

    SaveData saveDatas(File file);

    void refreshDatas();

    UMLEditor setEditorContent(UMLInfoPanel info);

    void hideEditor();

    UMLConfig getCfg();

}