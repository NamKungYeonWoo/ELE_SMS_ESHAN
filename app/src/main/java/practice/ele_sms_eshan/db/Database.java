package practice.ele_sms_eshan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Eshan on 12/5/17.
 */

public class Database
{


    private static final String TAG = "MyDatabase";
    private static final String DATABASE_NAME = "my_database.db";
    private DatabaseHelper mDbHelper;
    // Increment DB Version on any schema change
    private static final int DATABASE_VERSION = 1;
    private final Context mContext;
    public static BankDao mBankDao;


    public Database open() throws SQLException
    {
        mDbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

        mBankDao = new BankDao(mDb);

        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public Database(Context context)
    {
        this.mContext = context;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(IBankSchema.BANK_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            Log.w(TAG, "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " which destroys all old data");

            db.execSQL("DROP TABLE IF EXISTS "
                    + IBankSchema.BANK_TABLE);
            onCreate(db);

        }
    }
}
