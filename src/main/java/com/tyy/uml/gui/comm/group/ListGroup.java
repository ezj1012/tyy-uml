package com.tyy.uml.gui.comm.group;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.gui.comm.UmlScrollBarUI;
import com.tyy.uml.gui.op.UMLOperatePanel;
import com.tyy.uml.gui.op.setting.mgr.ProjectManager;
import com.tyy.uml.util.SWUtils;

public class ListGroup extends Group implements DComponentListener {

    private static final long serialVersionUID = 1L;

    protected JScrollPane umlScorllPanel;

    protected UmlScrollBarUI vBarUI = new UmlScrollBarUI();

    protected Object filterCdf;

    public ListGroup() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initScorllPanel();
    }

    private void initScorllPanel() {
        umlScorllPanel = new JScrollPane(this);
        umlScorllPanel.setOpaque(false);
        SWUtils.fixedAndNoBorder(umlScorllPanel, ProjectManager.fixedWidth, UMLOperatePanel.fixedHeight);
        umlScorllPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        umlScorllPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar vBar = umlScorllPanel.getVerticalScrollBar();
        vBar.setUI(vBarUI);
        SWUtils.fixedWidth(vBar, 10);
        umlScorllPanel.addComponentListener(this);
    }

    public List<GroupItem> getFilterDatas() {
        if (filterCdf == null) {
            return new ArrayList<GroupItem>(getItems());
        } else {
            return items.stream().filter(i -> i.match(filterCdf)).collect(Collectors.toList());
        }
    }

    public void filter(Object filterCdf) {
        this.filterCdf = filterCdf;
        if (filterCdf == null) {
            for (int i = 0; i < items.size();) {
                this.remove((JComponent) items.get(i));
            }
            for (int i = 0; i < items.size();) {
                this.add((JComponent) items.get(i));
            }
        } else {
            for (int i = 0; i < items.size();) {
                this.remove((JComponent) items.get(i));
            }
            for (int i = 0; i < items.size();) {
                if (items.get(i).match(filterCdf)) {
                    this.add((JComponent) items.get(i));
                }
            }
        }
        this.revalidate();
        this.repaint();
    }

    public void addTo(Consumer<JScrollPane> con) {
        con.accept(umlScorllPanel);
    }

    @Override
    public void setBackground(Color bg) {
        if (umlScorllPanel != null) {
            this.umlScorllPanel.setBackground(bg);
            this.umlScorllPanel.getVerticalScrollBar().setBackground(bg);
        }
        super.setBackground(bg);
    }

    public void setThumbDraggingColor(Color thumbDraggingColor) {
        vBarUI.setThumbDraggingColor(thumbDraggingColor);
    }

    public void setThumbRolloverColor(Color thumbRolloverColor) {
        vBarUI.setThumbRolloverColor(thumbRolloverColor);
    }

    public void setThumbColor(Color color) {
        vBarUI.setThumbColor(color);
    }

    public void setTrackColor(Color color) {
        vBarUI.setTrackColor(color);
    }

}
