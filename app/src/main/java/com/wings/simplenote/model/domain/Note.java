package com.wings.simplenote.model.domain;

import java.util.Date;

/**
 * Created by wing on 2016/4/12.
 */
public class Note {
    public long id;
    public String title;
    public String content;
    public boolean hasAlarm;
    public Date date;

    public Note() {
    }

    public Note(long id, String title, String content, boolean hasAlarm, Date date) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.hasAlarm = hasAlarm;
        this.id = id;
    }

    public Note(String title, String content, boolean hasAlarm, Date date) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.hasAlarm = hasAlarm;
    }

}
