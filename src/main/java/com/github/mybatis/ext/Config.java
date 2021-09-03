package com.github.mybatis.ext;

/**
 * @author xiebo
 */
public class Config {

    private Config config = new Config();

    public final static Config INSTANCE = new Config();

    /**
     仅扫描model中Column标注的字段
     */
    private boolean useColumnAnnotation = true;

    public boolean isUseColumnAnnotation() {
        return useColumnAnnotation;
    }

    public void setUseColumnAnnotation(boolean useColumnAnnotation) {
        this.useColumnAnnotation = useColumnAnnotation;
    }
}
