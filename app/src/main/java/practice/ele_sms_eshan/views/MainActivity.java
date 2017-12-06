package practice.ele_sms_eshan.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import practice.ele_sms_eshan.broadcastreceiver.MyResultReceiver;
import practice.ele_sms_eshan.R;
import practice.ele_sms_eshan.services.SmsSaveService;
import practice.ele_sms_eshan.db.BankTrans;
import practice.ele_sms_eshan.db.Database;

public class MainActivity extends AppCompatActivity implements MyResultReceiver.Receiver
{


    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private static MainActivity inst;
    List<BankTrans> smsMessagesList = new ArrayList<>();
    private static final int REQUEST_SMS = 123;
    public MyResultReceiver mReceiver;
    private ProgressDialog dialog;

    private RecyclerAdapter mRecyclerAdapter;

    public static MainActivity instance()
    {
        return inst;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Fetching SMS, please wait");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        mayRequestSMS();
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);


    }


    private boolean mayRequestSMS()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_SMS))
            {


            } else
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_SMS},
                        REQUEST_SMS);

            }
        } else {
            Log.d(TAG, " Entered here");
            smsMessagesList = Database.mBankDao.fetchAllBanks();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(mRecyclerAdapter == null)
            {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerAdapter = new RecyclerAdapter(MainActivity.this, smsMessagesList);
                mRecyclerView.setAdapter(mRecyclerAdapter);
            } else {
                mRecyclerAdapter.notifyDataSetChanged();
            }
        }

        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_SMS)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG, "Service Started");

                Intent serviceIntent = new Intent(MainActivity.this, SmsSaveService.class);
                serviceIntent.putExtra("receiverTag", mReceiver);
                startService(serviceIntent);
            }
        }
    }


    public void updateList(final BankTrans bankTrans)
    {
        new Thread()
        {
            public void run()
            {
                Database.mBankDao.addBank(bankTrans);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        smsMessagesList.add(0, bankTrans);
                        if(mRecyclerAdapter != null)
                        {
                            mRecyclerAdapter.notifyItemInserted(0);
                            mRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            mRecyclerAdapter = new RecyclerAdapter(MainActivity.this, smsMessagesList);
                            mRecyclerView.setAdapter(mRecyclerAdapter);
                        }
                    }
                });
            }
        }.start();

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData)
    {

        Log.d(TAG, "Result received");
        smsMessagesList = Database.mBankDao.fetchAllBanks();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerAdapter = new RecyclerAdapter(MainActivity.this, smsMessagesList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}
