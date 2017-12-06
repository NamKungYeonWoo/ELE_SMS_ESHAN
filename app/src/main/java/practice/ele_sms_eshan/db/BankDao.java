package practice.ele_sms_eshan.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eshan on 12/5/17.
 */

public class BankDao extends DbContentProvider implements IBankDao, IBankSchema
{
    private static ContentValues initialValues;
    private static Cursor cursor;

    public BankDao(SQLiteDatabase db)
    {
        super(db);
    }

    @Override
    public BankTrans fetchBankById(int bankTransId)
    {
        final String selectionArgs[] = {String.valueOf(bankTransId)};
        final String selection = COLUMN_ID + " = ?";
        BankTrans bankTrans = new BankTrans();
        cursor = super.query(BANK_TABLE, USER_COLUMNS, selection,
                selectionArgs, COLUMN_ID);
        if (cursor != null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                bankTrans = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return bankTrans;
    }

    @Override
    public List<BankTrans> fetchAllBanks()
    {
        List<BankTrans> bankList = new ArrayList<BankTrans>();
        cursor = super.query(BANK_TABLE, USER_COLUMNS, null,
                null, COLUMN_RECEIVE_TIME + " DESC");

        if (cursor != null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                BankTrans bankTrans = cursorToEntity(cursor);
                bankList.add(bankTrans);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return bankList;
    }

    @Override
    public boolean addBank(BankTrans bank)
    {
        // set values
        setContentValue(bank);
        try
        {
            return super.insert(BANK_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex)
        {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean addBanks(List<BankTrans> banks)
    {
        return false;
    }

    @Override
    public boolean deleteAllBanks()
    {
        return false;
    }

    @Override
    protected BankTrans cursorToEntity(Cursor cursor)
    {
        BankTrans bankTrans = new BankTrans();

        int idIndex;
        int bankNameIndex;
        int accountIndex;
        int amountIndex;
        int transTimeIndex;
        int receiveTimeIndex;

        if (cursor != null)
        {
            if (cursor.getColumnIndex(COLUMN_ID) != -1)
            {
                idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                bankTrans.id = cursor.getString(idIndex);
            }
            if (cursor.getColumnIndex(COLUMN_NAME) != -1)
            {
                bankNameIndex = cursor.getColumnIndexOrThrow(
                        COLUMN_NAME);
                bankTrans.bankName = cursor.getString(bankNameIndex);
            }
            if (cursor.getColumnIndex(COLUMN_ACCOUNT) != -1)
            {
                accountIndex = cursor.getColumnIndexOrThrow(
                        COLUMN_ACCOUNT);
                bankTrans.accountNumber = cursor.getString(accountIndex);
            }
            if (cursor.getColumnIndex(COLUMN_AMOUNT) != -1)
            {
                amountIndex = cursor.getColumnIndexOrThrow(
                        COLUMN_AMOUNT);
                bankTrans.transAmount = cursor.getString(amountIndex);
            }
            if (cursor.getColumnIndex(COLUMN_TRANS_TIME) != -1)
            {
                transTimeIndex = cursor.getColumnIndexOrThrow(
                        COLUMN_TRANS_TIME);
                bankTrans.transTime = cursor.getString(transTimeIndex);
            }
            if (cursor.getColumnIndex(COLUMN_RECEIVE_TIME) != -1)
            {
                receiveTimeIndex = cursor.getColumnIndexOrThrow(
                        COLUMN_RECEIVE_TIME);
                bankTrans.receiveTime = cursor.getString(receiveTimeIndex);
            }
        }
        return bankTrans;
    }

    private void setContentValue(BankTrans bank)
    {
        initialValues = new ContentValues();
        initialValues.put(COLUMN_ID, bank.id);
        initialValues.put(COLUMN_NAME, bank.bankName);
        initialValues.put(COLUMN_ACCOUNT, bank.accountNumber);
        initialValues.put(COLUMN_AMOUNT, bank.transAmount);
        initialValues.put(COLUMN_TRANS_TIME, bank.transTime);
        initialValues.put(COLUMN_RECEIVE_TIME, bank.receiveTime);
    }

    private ContentValues getContentValue()
    {
        return initialValues;
    }
}
