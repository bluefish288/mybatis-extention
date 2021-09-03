package test.com.github.mybatis.ext.model;


import com.github.mybatis.ext.enumdata.EnumValue;

public enum State implements EnumValue<Integer> {

    UNKNOWN(0,"未知"),

    ALLOW(1,"允许"),

    DENY(2,"禁止")

    ;

    private int code;

    private String title;

    State(int code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}