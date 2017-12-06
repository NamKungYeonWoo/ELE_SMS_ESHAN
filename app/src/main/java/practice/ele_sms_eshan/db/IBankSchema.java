package practice.ele_sms_eshan.db;

/**
 * Created by Eshan on 12/5/17.
 */

public interface IBankSchema
{

    String BANK_TABLE = "banks";
    String COLUMN_ID = "_id";
    String COLUMN_NAME = "name";
    String COLUMN_ACCOUNT = "account";
    String COLUMN_AMOUNT = "amount";
    String COLUMN_TRANS_TIME = "trans_time";
    String COLUMN_RECEIVE_TIME = "receive_time";
    String BANK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + BANK_TABLE
            + " ("
            + COLUMN_ID
            + " TEXT PRIMARY KEY, "
            + COLUMN_NAME
            + " TEXT NOT NULL, "
            + COLUMN_ACCOUNT
            + " TEXT,"
            + COLUMN_AMOUNT
            + " TEXT,"
            + COLUMN_TRANS_TIME
            + " TEXT,"
            + COLUMN_RECEIVE_TIME
            + " TEXT"
            + ")";

    String[] USER_COLUMNS = new String[] { COLUMN_ID,
            COLUMN_NAME,COLUMN_ACCOUNT, COLUMN_AMOUNT, COLUMN_TRANS_TIME, COLUMN_RECEIVE_TIME};
}
