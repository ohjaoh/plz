package kr.ac.yuhan.cs.admin.data;

import java.util.Date;

public class MemberData {
    private int number;
    private String userId;
    private Date joinDate;
    private int point;

    public MemberData(int number, String userId, Date joinDate, int point) {
        this.number = number;
        this.userId = userId;
        this.joinDate = joinDate;
        this.point = point;
    }

    public int getNumber() {
        return number;
    }

    public String getUserId() {
        return userId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public int getPoint() {
        return point;
    }
}
