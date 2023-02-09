package com.tyy.uml.gui.op.setting.mgr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Objects;

import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import com.tyy.uml.core.ctx.Ctrl;
import com.tyy.uml.core.ctx.UMLContext;
import com.tyy.uml.core.ctx.model.UMLProject;
import com.tyy.uml.core.ctx.model.UMLWork;
import com.tyy.uml.core.exception.ServiceException;
import com.tyy.uml.core.gui.adapter.DComponentListener;
import com.tyy.uml.core.gui.adapter.DDocumentListener;
import com.tyy.uml.core.gui.adapter.DFocusListener;
import com.tyy.uml.core.gui.adapter.DKeyListener;
import com.tyy.uml.gui.comm.EditorLabelPanel;
import com.tyy.uml.gui.comm.SingleColorIcon;
import com.tyy.uml.gui.comm.SingleColorIconButton;
import com.tyy.uml.gui.comm.group.GroupItem;
import com.tyy.uml.util.SWUtils;
import com.tyy.uml.util.SystemUtils;

public class ProjectAdd extends GroupItem implements DComponentListener, DFocusListener, DKeyListener, DDocumentListener {

    private static final long serialVersionUID = 1L;

    int fixedHeight = 30;

    EditorLabelPanel labelPanel;

    SingleColorIconButton btn;

    Ctrl ctrl;

    UMLWork workConfig;

    public ProjectAdd(Ctrl ctrl, UMLWork workConfig) {
        this.ctrl = ctrl;
        this.workConfig = workConfig;
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 10, 5, 5));
        labelPanel = new EditorLabelPanel(20, Color.WHITE, 0, 0, 0, 0, true);
        this.add(labelPanel, BorderLayout.CENTER);
        SWUtils.fixedHeight(this, fixedHeight);
        btn = new SingleColorIconButton(20, 20, new SingleColorIcon("check.png", 14, 14), new Color(133, 133, 133));
        btn.addActionListener(e -> addProject());
        this.add(btn, BorderLayout.EAST);
        labelPanel.addKeyListener(this);
        labelPanel.addDocumentListener(this);
    }

    @Override
    public void changed(DocumentEvent e, String newValue) {
        System.out.println(newValue);
    }

    private synchronized void addProject() {
        String text = labelPanel.getText();
        if (!SystemUtils.isEmpty(text)) {
            List<UMLProject> projects = workConfig.getProjects();
            for (UMLProject umlProject : projects) {
                if (Objects.equals(umlProject.getName(), text)) {
                    UMLContext.getContext().throwEx(new ServiceException("[" + text + "]已经存在!"));
                    return;
                }
            }

            UMLProject project = new UMLProject(text, null);
            ctrl.createProject(project);
            this.labelPanel.setText("");
        } else {
            UMLContext.getContext().throwEx(new ServiceException("产品名称不能为空!"));
        }
    }

    @Override
    public void requestFocus() {
        this.labelPanel.requestFocus();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            addProject();
        }
    }

}
