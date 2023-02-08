package com.tyy.uml.gui.canvas;

import java.util.List;

public interface Canvas {

    List<? extends CanvasElement> getElements();

    void addElements(CanvasElement ele);

    CanvasElement removeElements(CanvasElement ele);

}
