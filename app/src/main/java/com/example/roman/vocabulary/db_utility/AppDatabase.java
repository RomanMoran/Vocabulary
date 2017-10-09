package com.example.roman.vocabulary.db_utility;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by roman on 23.08.2017.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, insertConflict = ConflictAction.IGNORE, updateConflict= ConflictAction.REPLACE)
public class AppDatabase {

    public static final String NAME = "Vocabulary";

    public static final int VERSION = 1;

}
