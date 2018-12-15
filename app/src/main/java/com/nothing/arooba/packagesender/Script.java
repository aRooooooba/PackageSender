package com.nothing.arooba.packagesender;

import android.util.Log;

import java.util.ArrayList;

public class Script {

    private static final String TAG = "Script";

    // 脚本名
    private String name;
    // 网包集合
    private ArrayList<Package> packageSet;
    // 备注
    private String remark;
    // 使用次数
    private int executeNum;

    public Script(String name, ArrayList<Package> packageSet, String remark, int executeNum) {
        this.name = name;
        this.packageSet = packageSet;
        this.remark = remark;
        this.executeNum = executeNum;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Package> getPackageSet() {
        return packageSet;
    }

    public String getRemark() {
        return remark;
    }

    public String getExecuteNum() {
        return String.valueOf(executeNum);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean executeScript() {
        for (Package pck : packageSet) {
            if (!pck.sendPackage()) {
                return false;
            }
            Log.d(TAG, pck.getType() + pck.getUrl() + " sent.");
        }
        executeNum++;
        return true;
    }
}
