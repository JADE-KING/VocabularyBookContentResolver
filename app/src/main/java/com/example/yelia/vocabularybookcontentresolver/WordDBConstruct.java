package com.example.yelia.vocabularybookcontentresolver;

/**
 * Created by yelia on 2017/10/24.
 */

public interface WordDBConstruct {
    String DB_NAME = "Words";
    int DB_VERSION = 4;
    String TABLE_NAME = "WordDetail";
    String COLUMN_NAME_ID = "id";
    String COLUMN_NAME_WORD = "word";
    String COLUMN_NAME_TRANSLATION = "translation";
    String COLUMN_NAME_PHONETIC = "phonetic";
    String COLUMN_NAME_UKPHONETIC = "ukPhonetic";
    String COLUMN_NAME_USPHONETIC = "usPhonetic";
    String COLUMN_NAME_EXPLAINS = "explains";
    String COLUMN_NAME_WEBEXPLAINS = "webExplains";
    String COLUMN_NAME_USERNOTE = "userNote";
}
