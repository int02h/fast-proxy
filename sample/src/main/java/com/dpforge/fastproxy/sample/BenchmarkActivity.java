package com.dpforge.fastproxy.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dpforge.fastproxy.ProxyBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BenchmarkActivity extends AppCompatActivity {

    private Button btnRun;

    private View progress;

    private TextView result;

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark);

        btnRun = findViewById(R.id.btn_benchmark_run);
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startProgress();
                executor.execute(new Benchmark());
            }
        });

        progress = findViewById(R.id.progress);
        result = findViewById(R.id.txt_result);
    }

    private void postResult(final String result, final Object... args) {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                TextView view = BenchmarkActivity.this.result;
                view.setText(view.getText() + "\n" + String.format(result, args));
            }
        });
    }

    private void startProgress() {
        btnRun.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
    }

    private void stopProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.INVISIBLE);
                btnRun.setVisibility(View.VISIBLE);
            }
        });
    }

    public interface Foo {
        void noArgs();

        void threePrimitiveArgs(int arg1, float arg2, boolean arg3);

        void threeRefArgs(String arg1, Object arg2, Activity arg3);
    }

    private static class FooImpl implements Foo {
        @Override
        public void noArgs() {
        }

        @Override
        public void threePrimitiveArgs(final int arg1, final float arg2, final boolean arg3) {
        }

        @Override
        public void threeRefArgs(final String arg1, final Object arg2, final Activity arg3) {
        }
    }

    private class Benchmark extends AbstractBenchmark implements Runnable {

        private static final int COUNT = 1_000_000;

        private final InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) {
                return null;
            }
        };

        @Override
        public void run() {
            final Foo javaProxy = (Foo) Proxy.newProxyInstance(getClassLoader(), new Class[]{Foo.class}, invocationHandler);
            final Foo fastProxy = ProxyBuilder.createProxy(getApplicationContext(), Foo.class, invocationHandler);
            final Foo direct = new FooImpl();

            postResult("No Args");
            measureNoArg("java.lang.reflect.Proxy", javaProxy);
            measureNoArg("Fast Proxy", fastProxy);
            measureNoArg("Direct call", direct);

            postResult("Three primitive args");
            measureThreePrimitiveArgs("java.lang.reflect.Proxy", javaProxy);
            measureThreePrimitiveArgs("Fast Proxy", fastProxy);
            measureThreePrimitiveArgs("Direct call", direct);

            postResult("Three reference args");
            measureThreeRefArgs("java.lang.reflect.Proxy", javaProxy);
            measureThreeRefArgs("Fast Proxy", fastProxy);
            measureThreeRefArgs("Direct call", direct);

            stopProgress();
            postResult("Done\n");
        }

        void measureNoArg(final String name, final Foo proxy) {
            startTimeMeasurement();
            for (int i = 0; i < COUNT; i++) {
                proxy.noArgs();
            }
            double avg = 1d * stopTimeMeasurement() / COUNT;
            postResult("%s: %f ns", name, avg);
        }

        void measureThreePrimitiveArgs(final String name, final Foo proxy) {
            startTimeMeasurement();
            for (int i = 0; i < COUNT; i++) {
                proxy.threePrimitiveArgs(1, 1.0f, true);
            }
            double avg = 1d * stopTimeMeasurement() / COUNT;
            postResult("%s: %f ns", name, avg);
        }

        void measureThreeRefArgs(final String name, final Foo proxy) {
            startTimeMeasurement();
            for (int i = 0; i < COUNT; i++) {
                proxy.threeRefArgs("Hello", this, BenchmarkActivity.this);
            }
            double avg = 1d * stopTimeMeasurement() / COUNT;
            postResult("%s: %f ns", name, avg);
        }
    }
}
