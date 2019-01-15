package com.dpforge.fastproxy.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dpforge.fastproxy.ProxyBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Foo foo = ProxyBuilder.createProxy(getApplicationContext(), Foo.class, new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) {
                return Log.d("_@_", "Method '" + method + "' called with args: " + Arrays.toString(args));
            }
        });
        foo.bar();
        foo.zzz( true, "Hello World!", -5);
    }
}
