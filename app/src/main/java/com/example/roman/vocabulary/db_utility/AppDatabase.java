package com.example.roman.vocabulary.db_utility;

import android.support.annotation.NonNull;

import com.example.roman.vocabulary.data.Words;
import com.example.roman.vocabulary.data.Words_Table;
import com.raizlabs.android.dbflow.annotation.*;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by roman on 23.08.2017.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, insertConflict = ConflictAction.IGNORE, updateConflict= ConflictAction.REPLACE)
public class AppDatabase {

    public static final String NAME = "Vocabulary";

    public static final int VERSION = 2;

}
