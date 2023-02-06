package com.tyy.uml.context;

import java.io.File;
import java.io.IOException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.tyy.uml.bean.UMLWork;
import com.tyy.uml.core.gui.frame.TitleBar;
import com.tyy.uml.core.gui.frame.Tray;
import com.tyy.uml.gui.frame.UMLMainPane;
import com.tyy.uml.gui.frame.UMLFrame;
import com.tyy.uml.util.SystemUtils;

public class UMLContext {

    private static UMLContext ctx = new UMLContext();

    protected ConfigurableApplicationContext applicationContext;

    protected volatile UMLFrame frame;

    protected volatile Ctrl ctrl;

    protected volatile UMLWork workConfig;

    protected volatile File workConfigFile;

    private UMLContext() {
    }

    public static UMLContext getContext() {
        return ctx;
    }

    public void loadProps(ConfigurableEnvironment environment) {
        String dataPath = environment.getProperty("uml.data.path");
        workConfigFile = getOrDefault(dataPath, "uml.json");
        workConfig = readOrCreate(workConfigFile, UMLWork.class);
        workConfig.setBaseDir(workConfigFile.getParentFile().getAbsolutePath());
        frame = new UMLFrame(workConfig.getConfig());
        ctrl = new UMLMainPane(frame, workConfig);
        frame.setMainPanel((UMLMainPane) ctrl);
    }

    public void saveConfigs() {
        try {
            SystemUtils.writeFile(workConfigFile, workConfig);
        } catch (IOException e) {
        }
    }

    private File getOrDefault(String fileDir, String filename) {
        return SystemUtils.isEmpty(fileDir) ? new File("./", filename).getAbsoluteFile() : new File(fileDir, filename);
    }

    private <T> T readOrCreate(File filePath, Class<T> cl) {
        T data = null;
        if (filePath.exists()) {
            try {
                data = SystemUtils.readFile(filePath, cl);
            } catch (IOException e) {
            }
        }
        if (data == null) {
            filePath.getParentFile().mkdirs();
            try {
                data = cl.newInstance();
                SystemUtils.writeFile(filePath, data);
            } catch (Exception e) {
            }
        }
        return data;
    }

    private <T> T readOrCreate(String fileDir, String filename, Class<T> cl) {
        File filePath = getOrDefault(fileDir, filename);
        return readOrCreate(filePath, cl);
    }

    public UMLFrame getFrame() {
        return frame;
    }

    public TitleBar getTitleBar() {
        return frame.getTitleBar();
    }

    public Tray getTray() {
        return frame.getTray();
    }

    public Ctrl getCtrl() {
        return frame.getMainPanel();
    }

    public void setApplicationContext(ConfigurableApplicationContext context) {
        this.applicationContext = context;
        this.getTray().setVisible(true);
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
