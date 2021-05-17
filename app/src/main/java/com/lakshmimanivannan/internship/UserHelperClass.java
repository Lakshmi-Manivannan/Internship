package com.lakshmimanivannan.internship;

public class UserHelperClass {

    String fname,uname,email,pswd,pno;

    String points,level,badge;

    String Id,timestamp;

    public UserHelperClass(String uname, String points, String level, String badge, String id, String timestamp) {
        this.uname = uname;
        this.points = points;
        this.level = level;
        this.badge = badge;
        Id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public UserHelperClass(String fname, String uname, String email, String pswd, String pno, String points, String level, String badge) {
        this.fname = fname;
        this.uname = uname;
        this.email = email;
        this.pswd = pswd;
        this.pno = pno;
        this.points = points;
        this.level = level;
        this.badge = badge;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public UserHelperClass(String fname, String uname, String email, String pswd, String pno) {
        this.fname = fname;
        this.uname = uname;
        this.email = email;
        this.pswd = pswd;
        this.pno = pno;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}

