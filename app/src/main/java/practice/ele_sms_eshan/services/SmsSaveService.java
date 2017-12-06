package practice.ele_sms_eshan.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import practice.ele_sms_eshan.db.BankTrans;
import practice.ele_sms_eshan.db.Database;

/**
 * Created by Eshan on 12/5/17.
 */

public class SmsSaveService extends Service
{
    private static final String TAG = SmsSaveService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    ResultReceiver rec;

    @Override
    public void onCreate()
    {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        rec = intent.getParcelableExtra("receiverTag");
        refreshSmsInbox();
        Log.d(TAG, "Service Started");
        return START_REDELIVER_INTENT;
    }

    public void refreshSmsInbox()
    {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int indexDate = smsInboxCursor.getColumnIndex("date");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        do
        {
            String re0 = "[a-zA-Z]*(HDFC)[a-zA-Z]*|[a-zA-Z]*(ICIC)[a-zA-Z]*|[a-zA-Z]*(CITI)[a-zA-Z]*|" +
                    "[a-zA-Z]*(AXIS)[a-zA-Z]*|[a-zA-Z]*(SBI)[a-zA-Z]*|[a-zA-Z]*(HSBC)[a-zA-Z]*" +
                    "|[a-zA-Z]*(KOTAK)[a-zA-Z]*|[a-zA-Z]*(IDBI)[a-zA-Z]*";
            Pattern p0 = Pattern.compile(re0, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m0 = p0.matcher(smsInboxCursor.getString(indexAddress));
            String re1 = "(Credit Card)";
            Pattern p = Pattern.compile(re1, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(smsInboxCursor.getString(indexBody));
            Pattern p1 = Pattern.compile(" INR [0-9]+(,)?[0-9]+(.)?[0-9]+ | (Rs.)( )?[0-9]+(,)?[0-9]+(.)?[0-9]+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m1 = p1.matcher(smsInboxCursor.getString(indexBody));
            Pattern p2 = Pattern.compile("(tran)[a-zA-Z]+|(spent)[a-zA-Z]*|(inititated)[a-zA-Z]*|" +
                    "(credit)[a-zA-Z]*|(debit)[a-zA-Z]*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m2 = p2.matcher(smsInboxCursor.getString(indexBody));
            Pattern p3 = Pattern.compile("(declined)|[a-zA-Z]*(due)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m3 = p3.matcher(smsInboxCursor.getString(indexBody));
            Pattern p4 = Pattern.compile("[0-9]*(x)+[0-9]+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m4 = p4.matcher(smsInboxCursor.getString(indexBody));
            if (m1.find() && m.find() && !m3.find() && m0.find() && m2.find())
            {
                try
                {

                    BankTrans bankTrans = new BankTrans();
                    bankTrans.setBankName(smsInboxCursor.getString(indexAddress));
                    bankTrans.setId(UUID.randomUUID().toString());
                    bankTrans.setTransAmount(m1.group());
                    if (m4.find())
                    {
                        bankTrans.setAccountNumber(m4.group());
                    }
                    String date = smsInboxCursor.getString(indexDate);
                    long millisecond = Long.parseLong(date);
                    String dateString = DateFormat.format("MM/dd/yyyy HH:mm:ss", new Date(millisecond)).toString();
                    bankTrans.setReceiveTime(dateString);
                    Pattern transDatePattern = Pattern.compile("\\d\\d\\d\\d_\\d\\d_\\d\\d");
                    Matcher dateMatcher = transDatePattern.matcher(smsInboxCursor.getString(indexBody));
                    if (dateMatcher.find())
                    {
                        bankTrans.setTransTime(dateMatcher.group());
                    }

                    Log.d(TAG," Adding to bank");
                    Database.mBankDao.addBank(bankTrans);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else
            {
                Log.e("Mismatch", "Mismatch value");
            }

        } while (smsInboxCursor.moveToNext());

        Bundle b = new Bundle();
        rec.send(0, b);

        stopSelf();
    }

}
