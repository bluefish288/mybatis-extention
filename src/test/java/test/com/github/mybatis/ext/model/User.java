package test.com.github.mybatis.ext.model;


import com.github.mybatis.ext.annotation.Column;
import com.github.mybatis.ext.annotation.Id;
import com.github.mybatis.ext.annotation.Table;

import java.sql.Timestamp;

@Table("users")
public class User {

    @Id
    @Column("id")
    private Integer testId;

    @Column
    private String name;

    @Column
    private State state;

    @Column
    private Timestamp created;

    @Column
    private Timestamp updated;

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

}