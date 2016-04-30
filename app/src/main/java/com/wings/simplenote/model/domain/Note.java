package com.wings.simplenote.model.domain;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by wing on 2016/4/12.
 */
public class Note implements Serializable {
    public long id;
    public String title;
    public String content;
    public boolean hasAlarm;
    public Date date;
    public Date createDate;
    private static final long serialVersionUID = 0L;

    public Note() {
    }

    public Note(long id, String title, String content, boolean hasAlarm, Date date, Date createTime) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.hasAlarm = hasAlarm;
        this.id = id;
        this.createDate = createTime;
    }

    public Note(String title, String content, boolean hasAlarm, Date date, Date createTime) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.hasAlarm = hasAlarm;
        this.createDate = createTime;
    }

}
