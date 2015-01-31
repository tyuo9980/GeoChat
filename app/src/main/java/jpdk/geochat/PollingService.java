package jpdk.geochat;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class PollingService extends Service
{
    PollingAlarm alarm = new PollingAlarm();
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        alarm.SetAlarm(PollingService.this);
        return START_STICKY;
    }



    public void onStart(Context context,Intent intent, int startId)
    {
        alarm.SetAlarm(context);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}