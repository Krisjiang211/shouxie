package com.jiang.singlelearningdemo.callback;

public interface CallbackHandler<T> {

    T onMessage(T message);

    void onError(Throwable te);

    void onComplete(T res);
}
