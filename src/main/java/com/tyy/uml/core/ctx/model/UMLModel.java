package com.tyy.uml.core.ctx.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.util.SWUtils;

import lombok.Data;

@Data
public class UMLModel {

    private int x;

    private int y;

    private String backColor;

    private String borderColor;

    private String classColor;

    private String fieldColor;

    private String umlString;

    private String subPack;

    private String className;

    private String classDesc;

    private String classDescs;

    private List<UMLModelField> fields = new ArrayList<UMLModelField>();

    public UMLModel() {
        this.umlString = "";
        this.backColor = SWUtils.toHex(UMLGUIConfig.c1e1e1e);
        this.borderColor = SWUtils.toHex(Color.BLACK);
        this.classColor = SWUtils.toHex(UMLGUIConfig.cd4d4d4);
        this.fieldColor = SWUtils.toHex(UMLGUIConfig.cd4d4d4);
    }

}
