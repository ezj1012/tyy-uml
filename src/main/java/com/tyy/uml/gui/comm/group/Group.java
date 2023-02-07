package com.tyy.uml.gui.comm.group;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.tyy.uml.gui.comm.AbsMovable;

public class Group extends AbsMovable {

    private static final long serialVersionUID = 1L;

    protected List<GroupItem> items = new ArrayList<>();

    protected List<GroupSelectListener> selectListeners = new ArrayList<>();

    public void replace(List<? extends GroupItem> newItems) {
        for (int i = 0; i < items.size();) {
            this.remove(items.get(i));
        }
        for (GroupItem groupItem : newItems) {
            this.add(groupItem);
        }
    }

    public void add(GroupItem item) {
        item.setGroup(this);
        items.add(item);
        this.add((JComponent) item);
        this.revalidate();
        this.repaint();
    }

    public void remove(GroupItem item) {
        items.remove(item);
        this.remove((Component) item);
    }

    @Override
    public void mouseOneClicked(MouseEvent e) {
        super.mouseOneClicked(e);
        items.forEach(i -> i.unselect());
        this.repaint();
    }

    public void addSelectListener(GroupSelectListener selectListener) {
        this.selectListeners.add(selectListener);
    }

    public void selectItem(GroupItem item, MouseEvent e) {
        if (e == null || !e.isControlDown()) {
            items.forEach(i -> {
                if (item != i) {
                    i.unselect();
                }
            });
        }
        item.setSelected();
        this.selectListeners.forEach(ls -> {
            ls.selectedItem(item);
        });
        this.repaint();
    }

    public List<GroupItem> getItems() {
        return items;
    }

}
