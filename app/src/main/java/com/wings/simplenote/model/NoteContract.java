package com.wings.simplenote.model;

import android.provider.BaseColumns;

/**
 * Created by wing on 2016/4/15.
 */
public class NoteContract {
    public static final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public FeedReaderContract() {}

        /* Inner class that defines the table contents */
        public static abstract class NoteEntry implements BaseColumns {
            public static final String TABLE_NAME = "note";
            public static final String COLUMN_NAME_ALARM = "alarm";
            public static final String COLUMN_NAME_TITLE = "title";
            public static final String COLUMN_NAME_ID = "id";
            public static final String COLUMN_NAME_CONTENT = "content";
            public static final String COLUMN_NAME_ALARM_TIME = "alarm_time";
            public static String COLUMN_NAME_NULLABLE ="NULL";
        }
    }
}
