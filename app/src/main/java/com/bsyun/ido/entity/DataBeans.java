package com.bsyun.ido.entity;

public class DataBeans {

    private String name;
    private String msg;
    private String app;
    private long recv_time;
    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public long getRecv_time() {
        return recv_time;
    }

    public void setRecv_time(long recv_time) {
        this.recv_time = recv_time;
    }

    @Override
    public String toString() {
        return "DataBeans{" +
                "name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                ", app='" + app + '\'' +
                ", recv_time=" + recv_time +
                ", group='" + group + '\'' +
                '}';
    }
}
