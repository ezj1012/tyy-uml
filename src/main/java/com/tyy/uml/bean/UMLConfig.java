package com.tyy.uml.bean;

import java.awt.Color;

import com.tyy.uml.util.SWUtils;

import lombok.Data;

@Data
public class UMLConfig {

    public static final Color c1e1e1e = new Color(30, 30, 30);

    public static final Color c252526 = new Color(37, 37, 38);

    public static final Color c333333 = new Color(51, 51, 51);
    
    public static final Color c424242 = new Color(66, 66, 66);
    public static final Color c4F4F4F = new Color(79, 79, 79);
    public static final Color c5E5E5E = new Color(94, 94, 94);

    public static final Color cd4d4d4 = new Color(212, 212, 212);

    public static final Color caeafad = new Color(174, 175, 173);

    public static void main(String[] args) {
        System.out.println(SWUtils.toHex(c5E5E5E));
    }
    
    private String canvasBackColor;

    private String canvasThumbColor;

    private String canvasThumbRolloverColor;

    private String canvasThumbDraggingColor;

    private String editorBackColor;

    private String editorTitleBackColor;

    private String editorFontColor;

    private String editorCaretColor;

    private int canvasWidth = 6000;

    private int canvasHeight = 6000;

    public UMLConfig() {
        canvasBackColor = SWUtils.toHex(c252526);
        editorBackColor = SWUtils.toHex(c1e1e1e);
        editorFontColor = SWUtils.toHex(cd4d4d4);
        editorCaretColor = SWUtils.toHex(caeafad);
        editorTitleBackColor = SWUtils.toHex(c333333);
        canvasThumbColor = SWUtils.toHex(c424242);
        canvasThumbRolloverColor = SWUtils.toHex(c4F4F4F);
        canvasThumbDraggingColor = SWUtils.toHex(c5E5E5E);
    }

}
