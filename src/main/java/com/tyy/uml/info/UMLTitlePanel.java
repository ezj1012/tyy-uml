//package com.tyy.uml.info;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics2D;
//import java.awt.Shape;
//import java.awt.event.MouseEvent;
//import java.awt.geom.Path2D;
//import java.awt.geom.Rectangle2D;
//import java.awt.geom.RoundRectangle2D;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Observable;
//
//import javax.swing.BorderFactory;
//import javax.swing.BoxLayout;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.border.Border;
//import javax.swing.border.CompoundBorder;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.LineBorder;
//
//import com.tyy.plate.BeanHelper;
//import com.tyy.plate.BeanHelper.BeanObservale;
//import com.tyy.plate.BeanHelper.BeanObserver;
//import com.tyy.plate.JFr.GroupItem;
//import com.tyy.studio.core.util.SWUtils;
//import com.tyy.uml.Ctrl;
//import com.tyy.uml.UMLModelParser;
//import com.tyy.uml.bean.UMLConfig;
//import com.tyy.uml.bean.UMLModel;
//import com.tyy.uml.bean.UMLModelField;
//
//public class UMLTitlePanel extends GroupItem implements BeanObserver {
//
//    private static final long serialVersionUID = 1L;
//
//    private static final int dw = 200;
//
//    private static final int dh = 100;
//
//    private Ctrl ctrl;
//
//    private UMLModel model;
//
////    private JTextField classNameLabel;
//
//    private UMLFieldsPanel fieldsPanel;
//
//    public UMLTitlePanel(Ctrl ctrl, UMLModel model, int x, int y) {
//        this.ctrl = ctrl;
//        this.setBounds(x, y, dw, dh);
//        model = model == null ? new UMLModel() : model;
//        this.setLayout(new BorderLayout());
//        this.initTitle();
//        this.add(fieldsPanel = new UMLFieldsPanel(this, SWUtils.decodeColor(model.getFieldColor(), Color.BLACK)), BorderLayout.CENTER);
//        model.setX(x);
//        model.setY(y);
//        setModel(model);
//        this.addKeyListener(ctrl);
//    }
//
//    private void initTitle() {
//        classNameLabel = createLabel(12, Font.PLAIN, Color.BLACK, 5, 0, 5, 0);
//        classNameLabel.addMouseListener(this);
//        classNameLabel.addMouseMotionListener(this);
//        this.add(classNameLabel, BorderLayout.NORTH);
//    }
//
//    public UMLModel getModel() {
//        return model;
//    }
//
//    public void setModel(UMLModel model) {
//        if (model == null) {
//            this.model = model;
//            return;
//        }
//        if (BeanHelper.isEnhancer(model.getClass())) {
//            this.model = model;
//        } else {
//            this.model = BeanHelper.proxy(model, "umlString");
//        }
//        ((BeanObservale) this.model).addObserver(this);
//        update(model, null, null, null);
//    }
//
//    @Override
//    public void mouseDragged(MouseEvent e) {
//        if (isSelected()) {
//            int x = getX() + e.getX() - pressedX;
//            int y = getY() + e.getY() - pressedY;
//            this.setLocation(x, y);
//            model.setX(x);
//            model.setY(y);
//        }
//    }
//
//    @Override
//    public void update(Observable o, Object bean, String prop, Object oldValue, Object newValue) {
//        if (bean instanceof UMLModel) {
//            update((UMLModel) bean, prop, oldValue, newValue);
//        }
//    }
//
//    private void update(UMLModel model, String prop, Object oldValue, Object newValue) {
//        System.out.println("[" + prop + "] : " + oldValue + " =>" + newValue);
//        if (prop == null || "className".equals(prop)) {
//            this.classNameLabel.setText(model.getClassName());
//        }
//        if (prop == null || "classDesc".equals(prop)) {
//            this.classNameLabel.setToolTipText(model.getClassDesc());
//        }
//        if (prop == null || "fields".equals(prop)) {
//            this.fieldsPanel.refreshFields(model.getFields());
//            this.setBounds();
//        }
//
//        if (prop == null || "backColor".equals(prop)) {
//            this.setBackground(SWUtils.decodeColor(model.getBackColor(), UMLConfig.c1e1e1e));
//            this.setBounds();
//        }
//        if (prop == null || "borderColor".equals(prop)) {
//            Color borderColor = SWUtils.decodeColor(model.getBorderColor(), Color.BLACK);
//            Border insideBorder = ((CompoundBorder) classNameLabel.getBorder()).getInsideBorder();
//            classNameLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderColor, 1), insideBorder));
//            fieldsPanel.setBorderColor(borderColor);
//        }
//
//        if (prop == null || "classColor".equals(prop)) {
//            Color fg = SWUtils.decodeColor(model.getClassColor(), Color.BLACK);
//            classNameLabel.setForeground(fg);
//            classNameLabel.repaint();
//        }
//        if (prop == null || "fieldColor".equals(prop)) {
//            Color fg = SWUtils.decodeColor(model.getFieldColor(), Color.BLACK);
//            fieldsPanel.setForeground(fg);
//            fieldsPanel.repaint();
//        }
//
//        if (prop == null || "fields".equals(prop)) {
//            this.fieldsPanel.refreshFields(model.getFields());
//            this.setBounds();
//        }
//
//    }
//
//    private void setBounds() {
//        int th = this.classNameLabel.getHeight();
//        int fh = this.fieldsPanel.getPreferredSize().height;
//        fh = fh < 72 ? 72 : fh;
//        this.setBounds(getX(), getY(), dw, fh + th);
//        if (this.getParent() != null) {
//            this.getParent().repaint();
//        }
//    }
//
//    @Override
//    public void mouseDblClicked(MouseEvent e, int c) {
//        this.ctrl.setEditorContent(this);
//    }
//
//    public static class UMLFieldsPanel extends JPanel {
//
//        private static final long serialVersionUID = 1L;
//
//        private List<UMLFieldInfoPanel> cacheMI = new ArrayList<>();
//
//        private UMLTitlePanel infoPanel;
//
//        private Color borderColor;
//
//        private Color fieldColor;
//
//        public UMLFieldsPanel(UMLTitlePanel infoPanel, Color fieldColor) {
//            this.infoPanel = infoPanel;
//            this.fieldColor = fieldColor;
//            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//            setBorder();
//            setOpaque(false);
//        }
//
//        public void refreshFields(List<UMLModelField> fields) {
//            int h = 0;
//            List<UMLFieldInfoPanel> result = new ArrayList<>();
//            for (int i = 0; i < fields.size(); i++) {
//                UMLModelField model = fields.get(i);
//                UMLFieldInfoPanel mi = null;
//                if (cacheMI.size() > i) {
//                    mi = cacheMI.get(i);
//                } else {
//                    mi = new UMLFieldInfoPanel(infoPanel);
//                    cacheMI.add(mi);
//                }
//                mi.setFieldColor(fieldColor);
//                this.add(mi);
//                mi.refresh(model);
//                mi.repaint();
//                result.add(mi);
//                h += mi.getPreferredSize().height;
//            }
//            int size = cacheMI.size();
//            while (size - fields.size() > 0 && size > 0) {
//                this.remove(cacheMI.get(--size));
//            }
//            if (cacheMI.size() - fields.size() > 10) {
//                for (int i = 0; i < 5; i++) {
//                    cacheMI.remove(cacheMI.size() - 1);
//                }
//            }
//            h += getInsets().top + getInsets().bottom;
//            h = h < 72 ? 72 : h;
//            SWUtils.fixedHeight(this, h);
//            this.repaint();
//        }
//
//        public void setBorderColor(Color color) {
//            this.borderColor = color;
//        }
//
//        public void setFieldColor(Color fieldColor) {
//            this.fieldColor = fieldColor;
//            if (cacheMI != null) {
//                for (UMLFieldInfoPanel umlFieldInfoPanel : cacheMI) {
//                    umlFieldInfoPanel.setFieldColor(fieldColor);
//                    umlFieldInfoPanel.repaint();
//                }
//            }
//        }
//
//        private void setBorder() {
//            CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK, 1) {
//
//                private static final long serialVersionUID = 1L;
//
//                public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
//                    if ((this.thickness > 0) && (g instanceof Graphics2D)) {
//                        Graphics2D g2d = (Graphics2D) g;
//
//                        Color oldColor = g2d.getColor();
//                        g2d.setColor(borderColor);
//
//                        Shape outer;
//                        Shape inner;
//
//                        int offs = this.thickness;
//                        int size = offs + offs;
//                        if (this.roundedCorners) {
//                            float arc = .2f * offs;
//                            outer = new RoundRectangle2D.Float(x, y, width, height, offs, offs);
//                            inner = new RoundRectangle2D.Float(x + offs, y + offs, width - size, height - size, arc, arc);
//                        } else {
//                            outer = new Rectangle2D.Float(x, y, width, height);
//                            inner = new Rectangle2D.Float(x + offs, y, width - size, height - offs);
//                        }
//                        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
//                        path.append(outer, false);
//                        path.append(inner, false);
//                        g2d.fill(path);
//                        g2d.setColor(oldColor);
//                    }
//                };
//
//            }
//
//                    , new EmptyBorder(0, 0, 5, 0));
//            setBorder(compoundBorder);
//        }
//
//    }
//
//    public static class UMLFieldInfoPanel extends JPanel {
//
//        private static final long serialVersionUID = 1L;
//
//        JTextField showField;
//
//        UMLTitlePanel infoPanel;
//
//        public UMLFieldInfoPanel(UMLTitlePanel infoPanel) {
//            this.infoPanel = infoPanel;
//            this.setLayout(new BorderLayout());
//            this.setOpaque(false);
//            showField = createLabel(12, Font.PLAIN, Color.BLACK, 0, 5, 0, 0);
//            this.add(showField, BorderLayout.CENTER);
//            SWUtils.fixedHeight(this, 16);
//            this.addMouseListener(infoPanel);
//            this.addMouseMotionListener(infoPanel);
//        }
//
//        public void refresh(UMLModelField field) {
//            UMLModelParser.setUMLString(field, showField);
//        }
//
//        public JTextField createLabel(int fontSize, int fontStyle, Color fontColor, int top, int left, int bottom, int right) {
//            JTextField area = new JTextField();
//
//            // CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), new EmptyBorder(top, left, bottom, right));
//            // area.setBorder(compoundBorder);
//            area.setBorder(new EmptyBorder(top, left, bottom, right));
//
//            area.setEditable(false);
//            area.setOpaque(false);
//            area.setForeground(fontColor);
//            area.setHorizontalAlignment(JTextField.LEFT);
//            // area.setLineWrap(true); // 激活自动换行功能
//            SWUtils.setFontSize(area, fontSize, fontStyle);
//            return area;
//        }
//
//        public void setFieldColor(Color fg) {
//            if (this.showField != null) {
//                this.showField.setForeground(fg);
//            }
//        }
//
//    }
//
//}