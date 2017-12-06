package practice.ele_sms_eshan.application;

import android.app.Application;

import java.sql.SQLException;

import practice.ele_sms_eshan.db.Database;

/**
 * Created by Eshan on 12/5/17.
 */

public class App extends Application
{

    public static Database db;

    @Override
    public void onCreate()
    {
        super.onCreate();
        db = new Database(this);
        try
        {
            db.open();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onTerminate() {
        db.close();
        super.onTerminate();
    }
}
