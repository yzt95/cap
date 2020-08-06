package cool.yzt.cap.entity;

import java.util.Date;

/**
 * @author : yzt
 */
public class StatisticData {
    private int id;
    private Date date;
    private long uv;
    private long dau;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getUv() {
        return uv;
    }

    public void setUv(long uv) {
        this.uv = uv;
    }

    public long getDau() {
        return dau;
    }

    public void setDau(long dau) {
        this.dau = dau;
    }

    @Override
    public String
    toString() {
        return "StatisticData{" +
                "id=" + id +
                ", date=" + date +
                ", uv=" + uv +
                ", dau=" + dau +
                '}';
    }
}
