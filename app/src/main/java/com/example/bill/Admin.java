package com.example.bill;

public class Admin {
    private int logid;
    private String logname;
    private String logpass;

    public Admin() {
        // Default constructor
    }

    public Admin(int logid, String logname, String logpass) {
        this.logid = logid;
        this.logname = logname;
        this.logpass = logpass;
    }

    //Getters & Setters

    public int getIIdd() {
        return logid;
    }

    public void setIIdd(int logid) {
        this.logid = logid;
    }

    public String getNName() {
        return logname;
    }

    public void setNNamee(String logname) {
        this.logname = logname;
    }

    public String getPPass() {
        return logpass;
    }

    public void setPPass(String logpass) {
        this.logpass = logpass;
    }
}
