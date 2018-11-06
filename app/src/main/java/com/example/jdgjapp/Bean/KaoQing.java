package com.example.jdgjapp.Bean;

/**
 * author : chen
 * date   : 2018/10/31  23:51
 * desc   :
 */
public class KaoQing {


    /**
     * cq : 3
     * cc : 0
     * qj : 2
     * cd : 3
     * zt : 0
     * qq : 0
     */

    private String cq;
    private String cc;
    private String qj;
    private String cd;
    private String zt;
    private String qq;

    public KaoQing() {
    }

    public KaoQing(String cq, String cc, String qj, String cd, String zt, String qq) {
        this.cq = cq;
        this.cc = cc;
        this.qj = qj;
        this.cd = cd;
        this.zt = zt;
        this.qq = qq;
    }

    public String getCq() {
        return cq;
    }

    public void setCq(String cq) {
        this.cq = cq;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getQj() {
        return qj;
    }

    public void setQj(String qj) {
        this.qj = qj;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Override
    public String toString() {
        return "KaoQing{" +
                "cq='" + cq + '\'' +
                ", cc='" + cc + '\'' +
                ", qj='" + qj + '\'' +
                ", cd='" + cd + '\'' +
                ", zt='" + zt + '\'' +
                ", qq='" + qq + '\'' +
                '}';
    }
}
