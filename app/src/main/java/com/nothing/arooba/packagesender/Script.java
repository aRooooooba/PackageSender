package com.nothing.arooba.packagesender;

import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

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
        for (Package pck : packageSet) {
            if (!pck.sendPackage()) {
                return false;
            }
            Log.d(TAG, pck.getType() + pck.getUrl() + " sent.");
        }
        executeNum++;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Script) {
            Script other = (Script) obj;
            return name.equals(other.name) && remark.equals(other.remark);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, remark);
    }
}
