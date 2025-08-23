package com.jiang.singlelearningdemo.objPool.domain;

public class NeedNewFlag {
    private boolean isNeedNew = false;

    private NeedNewFlag(){}

    public static NeedNewFlag getInstance(){
        return new NeedNewFlag();
    }

    public void iDontNeed() {
        this.isNeedNew=false;
    }

    public void iNeed() {
        this.isNeedNew=true;
    }

    public boolean doINeed() {
        return isNeedNew;
    }


}
