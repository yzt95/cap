package cool.yzt.cap.util;

import com.github.pagehelper.PageInfo;
import cool.yzt.cap.dto.PageBean;

import java.util.List;
import java.util.Map;

/** PageBean封装参数
 * @author : yzt
 */
public class PageBeanUtil {

    public static PageBean getPageBean(PageInfo pageInfo, List<Map<String, Object>> contents) {
        PageBean pageBean = new PageBean();
        pageBean.setContents(contents);
        pageBean.setCurrent(pageInfo.getPageNum());
        pageBean.setSize(pageInfo.getPageSize());
        pageBean.setTotalContent(pageInfo.getTotal());
        pageBean.setTotalPage(pageInfo.getPages());
        pageBean.setPrePage(pageInfo.getPrePage());
        pageBean.setNextPage(pageInfo.getNextPage());
        pageBean.setFirst(pageInfo.isIsFirstPage());
        pageBean.setLast(pageInfo.isIsLastPage());
        return pageBean;
    }

}
