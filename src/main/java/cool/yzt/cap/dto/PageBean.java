package cool.yzt.cap.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageBean implements Serializable {
    private List<Map<String,Object>> contents;
    private int current;
    private int size;
    private long totalContent;
    private int totalPage;
    private int prePage;
    private int nextPage;
    private boolean first;
    private boolean last;

    public List<Map<String, Object>> getContents() {
        return contents;
    }

    public void setContents(List<Map<String, Object>> contents) {
        this.contents = contents;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalContent() {
        return totalContent;
    }

    public void setTotalContent(long totalContent) {
        this.totalContent = totalContent;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getFrom() {
        if(current<5) return 1;
        if(totalPage-current<6) return totalPage-9;
        return current-3;
    }

    public int getTo() {
        if(totalPage<10) return totalPage;
        if(current<5) return 10;
        return Math.min(current+6,totalPage);
    }

}
