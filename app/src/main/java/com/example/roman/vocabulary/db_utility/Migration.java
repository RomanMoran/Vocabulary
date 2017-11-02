package com.example.roman.vocabulary.db_utility;

import com.example.roman.vocabulary.data.Words;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * Created by roman on 02.11.2017.
 */

@com.raizlabs.android.dbflow.annotation.Migration(version = AppDatabase.VERSION,database = AppDatabase.class)
public class Migration extends AlterTableMigration<Words> {

    public Migration(Class<Words> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        addColumn(SQLiteType.TEXT,"association");
    }
}
