package com.josenaves.score;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import io.fabric.sdk.android.Fabric;

public class ScoreApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // init Fabric
        Fabric.with(this,
                new Crashlytics(),
                new Answers());
    }
}
