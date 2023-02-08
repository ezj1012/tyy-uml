package com.tyy.uml.gui.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.tyy.uml.core.ctx.Ctrl;
import com.tyy.uml.core.ctx.bean.UMLGUIConfig;
import com.tyy.uml.core.ctx.model.UMLModel;
import com.tyy.uml.gui.canvas.elements.UMLInfoPanel;
import com.tyy.uml.util.BeanHelper.BeanObservale;
import com.tyy.uml.util.BeanHelper.BeanObserver;
import com.tyy.uml.util.SWUtils;

public class UMLCanvas extends AbsScrollCanvas implements BeanObserver {

    private static final long serialVersionUID = 1L;

    private Ctrl ctrl;

    public UMLCanvas(Ctrl ctrl) {
        this.ctrl = ctrl;
        setLayout(null);

        UMLGUIConfig cfg = this.ctrl.getCfg();
        setBounds(0, 0, cfg.getCanvasWidth(), cfg.getCanvasHeight());
        setPreferredSize(new Dimension(cfg.getCanvasWidth(), cfg.getCanvasHeight()));
        setCenter();
        this.requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawLine(3000, 0, 3000, getHeight());
        g.drawLine(0, 2000, getWidth(), 2000);
    }

    @Override
    public void mouseDblClicked(MouseEvent e, int c) {
        UMLInfoPanel classDes = new UMLInfoPanel(ctrl, null, e.getX(), e.getY());
        create(classDes, e);
    }

  

    public void create(UMLInfoPanel info) {
        create(info, null);
    }

    public void create(UMLInfoPanel info, MouseEvent e) {
        this.add(info);
        this.selectItem(info, e);
        this.repaint();
    }

    public void refreshConfig(UMLGUIConfig cfg) {
        if (cfg instanceof BeanObservale) {
            ((BeanObservale) cfg).addObserver(this);
        }
        updateConfig(cfg, null, null);
    }

    private void updateConfig(UMLGUIConfig cfg, String prop, Object newValue) {
        if (prop == null || "canvasBackColor".equals(prop)) {
            Color backColor = SWUtils.decodeColor(ctrl.getCfg().getCanvasBackColor(), UMLGUIConfig.c252526);
            setBackground(backColor);
            setBackground(backColor);
        }
        if (prop == null || "canvasThumbColor".equals(prop)) {
            setThumbColor(SWUtils.decodeColor(ctrl.getCfg().getCanvasThumbColor(), UMLGUIConfig.c424242));
        }
        if (prop == null || "canvasThumbRolloverColor".equals(prop)) {
            setThumbRolloverColor(SWUtils.decodeColor(ctrl.getCfg().getCanvasThumbRolloverColor(), UMLGUIConfig.c4F4F4F));
        }
        if (prop == null || "canvasThumbDraggingColor".equals(prop)) {
            setThumbDraggingColor(SWUtils.decodeColor(ctrl.getCfg().getCanvasThumbDraggingColor(), UMLGUIConfig.c5E5E5E));
        }
    }

    @Override
    public void update(Observable o, Object source, String prop, Object oldValue, Object newValue) {
        if (source instanceof UMLGUIConfig) {
            updateConfig((UMLGUIConfig) source, prop, newValue);
        }
    }

    public void refreshModels(List<UMLModel> models) {
        List<UMLInfoPanel> newItems = new ArrayList<>();
        for (UMLModel umlModel : models) {
            newItems.add(new UMLInfoPanel(ctrl, umlModel, umlModel.getX(), umlModel.getY()));
        }
        this.replace(newItems);
    }

}