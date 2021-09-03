package com.github.mybatis.ext.model;

public class InsertModel {

    private Object model;

    private boolean ignoreOnDuplicate = false;

    private InsertModel(Object model) {
        this.model = model;
    }

    public static InsertModel build(Object model){
        return new InsertModel(model);
    }

    public InsertModel ignoreOnDuplicate(boolean ignoreOnDuplicate){
        this.ignoreOnDuplicate = ignoreOnDuplicate;
        return this;
    }

    public boolean isIgnoreOnDuplicate() {
        return ignoreOnDuplicate;
    }

    public Object getModel() {
        return model;
    }
}