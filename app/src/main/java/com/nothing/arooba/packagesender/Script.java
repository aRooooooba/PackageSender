package com.nothing.arooba.packagesender;

import android.util.Log;

import java.util.ArrayList;

public class Script {

    private static final String TAG = "Script";

    // 脚本名
    private String name;
    // 网包集合
    private ArrayList<Package> pckSet;
    // 备注
    private String remark;
    // 使用次数
    private int executeNum;

    public Script(String name, ArrayList<Package> pckSet, String remark, int executeNum) {
        this.name = name;
        this.pckSet = pckSet;
        this.remark = remark;
        this.executeNum = executeNum;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Package> getPckSet() {
        return pckSet;
    }

    public String getRemark() {
        return remark;
    }

    public int getExecuteNum() {
        return executeNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean executeScript() {
        for (Package pck : pckSet) {
            if (!pck.sendPackage()) {
                return false;
            }
            Log.d(TAG, pck.getType() + pck.getUrl() + " sent.");
        }
        executeNum++;
        return true;
    }
}
