package com.dpforge.fastproxy.sample;

public interface Foo {
    void bar();

    void zzz(boolean b, String s, int i);

    void argName(@ArgName("ololo") Object value);
}
