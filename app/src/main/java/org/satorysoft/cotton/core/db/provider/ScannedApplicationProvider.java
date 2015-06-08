package org.satorysoft.cotton.core.db.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.helper.TableBuilder;
import com.tjeannin.provigen.helper.TableUpdater;
import com.tjeannin.provigen.model.Constraint;

import org.satorysoft.cotton.core.db.contract.ScannedApplicationContract;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ScannedApplicationProvider extends ProviGenProvider {
    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scannedappdb";
    private final static Class[] CONTRACTS = new Class[]{ScannedApplicationContract.class};

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public Class[] contractClasses() {
        return CONTRACTS;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            new TableBuilder(ScannedApplicationContract.class)
                    .addConstraint(ScannedApplicationContract._ID, Constraint.UNIQUE,
                            Constraint.OnConflict.REPLACE)
                    .createTable(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            TableUpdater.addMissingColumns(sqLiteDatabase, ScannedApplicationContract.class);
        }
    }
}
