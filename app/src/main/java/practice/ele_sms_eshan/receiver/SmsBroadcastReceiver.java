package practice.ele_sms_eshan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import practice.ele_sms_eshan.views.MainActivity;
import practice.ele_sms_eshan.db.BankTrans;

/**
 * Created by Eshan on 12/5/17.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver
{
    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null)
        {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i)
            {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                long date = smsMessage.getTimestampMillis();

                String re1 = "(Credit Card)";
                Pattern p = Pattern.compile(re1, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(smsBody);
                Pattern p1 = Pattern.compile(" INR [0-9]+(,)?[0-9]+(.)?[0-9]+ | (Rs.)( )?[0-9]+(,)?[0-9]+(.)?[0-9]+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m1 = p1.matcher(smsBody);
                Pattern p2 = Pattern.compile("(tran)[a-zA-Z]+ | (spent) | (inititated)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m2 = p2.matcher(smsBody);
                Pattern p3 = Pattern.compile("(declined)|[a-zA-Z]*(due)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m3 = p3.matcher(smsBody);
                Pattern p4 = Pattern.compile("[0-9]*(x)+[0-9]+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m4 = p4.matcher(smsBody);
                if (m1.find() && m.find() && !m3.find())
                {
                    try
                    {

                        BankTrans bankTrans = new BankTrans();
                        bankTrans.setBankName(address);
                        bankTrans.setId(UUID.randomUUID().toString());
                        bankTrans.setTransAmount(m1.group());
                        if (m4.find())
                        {
                            bankTrans.setAccountNumber(m4.group());
                        }
                        String dateString = DateFormat.format("MM/dd/yyyy HH:mm:ss", new Date(date)).toString();
                        bankTrans.setReceiveTime(dateString);
                        Pattern transDatePattern = Pattern.compile("\\d\\d\\d\\d_\\d\\d_\\d\\d");
                        Matcher dateMatcher = transDatePattern.matcher(smsBody);
                        if (dateMatcher.find())
                        {
                            bankTrans.setTransTime(dateMatcher.group());
                        }

                        //this will update the UI with message
                        MainActivity inst = MainActivity.instance();
                        inst.updateList(bankTrans);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    Log.e("Mismatch", "Mismatch value");
                }

            }
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();


        }
    }
}
