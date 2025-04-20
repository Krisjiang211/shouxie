package com.jiang.singlelearningdemo.callback;

public  class MyCallback<T> {

    private final CallbackHandler<T> callbackHandler;

    MyCallback(){
        this.callbackHandler = new DefaultCallbackHandler<>();
    }
    MyCallback(CallbackHandler<T> callbackHandler){
        this.callbackHandler = callbackHandler;
    }
    //1. 大文件

    //2. 异步任务

    //3. 多线程任务


    public void execute(){}


    public final MyCallback<T> onMessage(T message){
        callbackHandler.onMessage(message);
        return this;
    }

    public final MyCallback<T> onError(Throwable te){
        callbackHandler.onError(te);
        return this;
    }

    public final MyCallback<T> onComplete(T res){
        callbackHandler.onComplete(res);
        return this;
    }








    class DefaultCallbackHandler<T> implements CallbackHandler<T> {
        @Override
        public T onMessage(T message) {
            System.out.println("接收到------"+message);
            return message;
        }
        @Override
        public void onError(Throwable te) {
            System.out.println("任务异常------"+te.getMessage());
        }
        @Override
        public void onComplete(T res) {
            System.out.println("任务正常完成, 结果是"+res);
        }
    }
}
