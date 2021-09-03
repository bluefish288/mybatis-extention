package com.github.mybatis.ext.model;

/**
 * @author xiebo
 */
public class UpdateModel {

    private Object model;

    private boolean allowUpdateNull = false;

    private UpdateModel(Object model){
        this.model = model;
    }

    public static UpdateModel build(Object model){
        return new UpdateModel(model);
    }

    public UpdateModel allowUpdateNull(boolean allowUpdateNull) {
        this.allowUpdateNull = allowUpdateNull;
        return this;
    }

    public boolean isAllowUpdateNull() {
        return allowUpdateNull;
    }

    public Object getModel() {
        return model;
    }
}
