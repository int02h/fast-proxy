package com.dpforge.fastproxy.sample;

import android.os.SystemClock;

public abstract class AbstractBenchmark {

    private long startTime;

    void startTimeMeasurement() {
        startTime = SystemClock.elapsedRealtimeNanos();
    }

    long stopTimeMeasurement() {
        return SystemClock.elapsedRealtimeNanos() - startTime;
    }
}
