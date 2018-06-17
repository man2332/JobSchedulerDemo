package com.example.john.jobschedulerdemo;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//background jobs
//  -nothing update UI-defferable
//  -self contained
//Device-idl mode
//JobScheduler-
//  specifying constraints-based on conditions-not just time
//JobService class
//  -onStart()-start the job on main thread
//      if you start asynchronous processing in this method, return true otherwise false.
//  -onStop()- if the job needs to stop because one of the conditions
//      is no longer met or whatever other reason-If the job fails for some reason,
//      return true from on the onStopJob to restart the job
//  -jobFinished()-arg current job, boolean rescheduleJob
//      you call this to tell the system your job is really finished

//JobInfo object
//  -contains the condition where we want to schedule our job
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LogJob";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scheduleJob(View view){
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)//device must be pluged in & charging
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)//must be connected to wi-fi
                .setPersisted(true)//continues the job after system reboot
                .setPeriodic(15 * 60 * 1000)//how often execute this task(minimum is every 15 mins)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "Job scheduled SUCCESSS!!:");
        }else{
            Log.d(TAG, "Job scheduled FAILED!");
        }
    }

    public void cancelJob(View view){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancelAll();
        Log.d(TAG, "cancelJob: ");
    }
}
