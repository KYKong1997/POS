package com.example.kuoky.myapplication.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Member extends GenericJson {

    @Key
    private String Status;
    @Key
    private Integer Points;
    @Key
    private String Email;
    @Key
    private String HPNO;
    @Key
    private String L_Name;
    @Key
    private String F_Name;
    @Key
    private String ICNO;
    @Key
    private String M_Card_No;

    public Member() {
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getHPNO() {
        return HPNO;
    }

    public void setHPNO(String HPNO) {
        this.HPNO = HPNO;
    }

    public String getL_Name() {
        return L_Name;
    }

    public void setL_Name(String l_Name) {
        L_Name = l_Name;
    }

    public String getF_Name() {
        return F_Name;
    }

    public void setF_Name(String f_Name) {
        F_Name = f_Name;
    }

    public String getICNO() {
        return ICNO;
    }

    public void setICNO(String ICNO) {
        this.ICNO = ICNO;
    }

    public String getM_Card_No() {
        return M_Card_No;
    }

    public void setM_Card_No(String m_Card_No) {
        M_Card_No = m_Card_No;
    }
}
