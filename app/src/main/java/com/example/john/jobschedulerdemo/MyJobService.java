package com.example.john.jobschedulerdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class MyJobService  extends JobService{
    private static final String TAG = "LogJob";
    private boolean jobCancelled = false;


    //-onStartJob-
    //  -runs on main thread-so if u need seperate thread
    //      you have to do that yourself-or app freezes
    //  -
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        //this method-runs seperate thread for 10 seconds
        doFakeBackgroundWork(params);

        return true;
        //-return false-tells the system-the job is done when
        //  the method is done(end of scope)
        //-return true-tells the system the job is still going
        //  even after the method scope & tells the system to stay
        //  awake for our job-example of a job like this is a thread
    }

    private void doFakeBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0;i < 10;i++){
                    if(jobCancelled){
                        return;
                    }
                    Log.d(TAG, "run: "+i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "Job finished");
                //2nd arg-if something went wrong-we need
                // to reschedule the job
                jobFinished(params,false);
                //this method tells the system our job is done
                //-or failed & needs rescheduling

            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");

        //we are responsible for closing all threads ourselfs
        jobCancelled = true;
        //use AsyncTask.cancel() method if used a AsyncTask

        return false;
        //-return true if u want to re-try the job later
        //-false if this job isn't important-dont retry
    }
}
