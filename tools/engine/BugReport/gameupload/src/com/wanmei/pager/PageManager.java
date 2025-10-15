package com.locojoy.pager;
import java.util.List;
import java.util.StringTokenizer;

public abstract class PageManager<T> {
    /**
     * 记录数
     */
    protected PageList pagelist;
    public PageManager() {
    }
    public PageManager(PageList pl) {
        this.pagelist = pl;
    }
    public void setPageList(PageList pl) {
        this.pagelist = pl;
    }
    /**
     * 获得首页的链接的HTML tag
     * @param name 名称，是“首页”还是用别的字符，如果name==null,就用缺省的“首页”字符
     * @return 首页的链接的HTML tag
     */
    public String getFirstPage(String name) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("请先执行查询操作");
        if (name == null)
            name = "首页";
        int start = pagelist.getStart();
        String uri = pagelist.getURIAndParams();
        if (start > 1) {
            StringBuffer sb = new StringBuffer();
            sb.append("<a href=\"");
            String stemp = "start=" + start;
            int n = uri.indexOf(stemp);
            if (n >= 0) {
                uri = uri.replaceAll(stemp, "start=1");
            } else {
                uri += "&start=1";
            }
            sb.append(uri);
            sb.append("\">");
            sb.append(name);
            sb.append("</a>");
            return sb.toString();
        } else {
            return name;
        }
    }

    /**
     * 获得上一页的链接的HTML tag
     * @param name 名称，是“上一页”还是用别的字符，如果name==null,就用缺省的“上一页”字符
     * @return 上一页的链接的HTML tag
     */
    public String getPreviousPage(String name) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("请先执行查询操作");
        if (name == null)
            name = "上一页";
        int start = pagelist.getStart();
        String uri = pagelist.getURIAndParams();
        if (start > 1) {
            StringBuffer sb = new StringBuffer();
            sb.append("<a href=\"");
            String stemp = "start=" + start;
            int n = uri.indexOf(stemp);
            int num = start - 1;
            num = num < 0 ? 0 : num;
            if (n >= 0) {
                uri = uri.replaceAll(stemp, "start=" + num);
            } else {
                uri += "&start=" + num;
            }
            sb.append(uri);
            sb.append("\">");
            sb.append(name);
            sb.append("</a>");
            return sb.toString();
        } else {
            return name;
        }
    }

    /**
     * 获得下一页的链接的HTML tag
     * @param name 名称，是“下一页”还是用别的字符，如果name==null,就用缺省的“下一页”字符
     * @return 下一页的链接的HTML tag
     */
    public String getNextPage(String name) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("请先执行查询操作");
        if (name == null)
            name = "下一页";
        int start = pagelist.getStart();
        int maxpage = pagelist.getMaxPage();
        String uri = pagelist.getURIAndParams();
        if (start < maxpage) {
            StringBuffer sb = new StringBuffer();
            sb.append("<a href=\"");
            String stemp = "start=" + start;
            int n = uri.indexOf(stemp);
            int num = start + 1;
            if (n >= 0) {
                uri = uri.replaceAll(stemp, "start=" + num);
            } else {
                uri += "&start=" + num;
            }
            sb.append(uri);
            sb.append("\">");
            sb.append(name);
            sb.append("</a>");
            return sb.toString();
        } else {
            return name;
        }
    }

    /**
     * 获得末页的链接的HTML tag
     * @param name 名称，是“末页”还是用别的字符，如果name==null,就用缺省的“末页”字符
     * @return 末页的链接的HTML tag
     */
    public String getLastPage(String name) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("请先执行查询操作");
        if (name == null)
            name = "末页";
        int start = pagelist.getStart();
        int maxpage = pagelist.getMaxPage();
        String uri = pagelist.getURIAndParams();
        if (start< maxpage) {
            StringBuffer sb = new StringBuffer();
            sb.append("<a href=\"");
            String stemp = "start=" + start;
            int n = uri.indexOf(stemp);
            if (n >= 0) {
                uri = uri.replaceAll(stemp, "start=" + maxpage);
            } else {
                uri += "&start=" + maxpage;
            }
            sb.append(uri);
            sb.append("\">");
            sb.append(name);
            sb.append("</a>");
            return sb.toString();
        } else {
            return name;
        }

    }

    /**
     * 获得通过文本框输入数字直接跳转到某页的链接的HTML tag
     * @param pageNum 到第几页
     * @return 通过文本框输入数字直接跳转到某页的链接的HTML tag
     */
    private String getGotoPageLink(int pageNum) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("请先执行查询操作");
        int start = pagelist.getStart();
        int pagesize = pagelist.getPageSize();
        String uri = pagelist.getURIAndParams();
        int newstart = (pageNum - 1) * pagesize;
        //超过最后一页，设置为最后一页
        if(newstart > pagelist.getCount())
        	newstart = pagelist.getCount();
        if (newstart >= 0) {
            StringBuffer sb = new StringBuffer();
            String stemp = "start=" + start;
            int n = uri.indexOf(stemp);
            int num = newstart;
            if (n >= 0) {
                uri = uri.replaceAll(stemp, "start=" + num);
            } else {
                uri += "&start=" + num;
            }
            sb.append(uri);
            return sb.toString();
        } else {
            return null;
        }
    }

    private String getGoToPage(int n) {
        StringBuffer sb = new StringBuffer();
        sb.append("<a href=\"");
        sb.append(getGotoPageLink(n));
        sb.append("\">");
        sb.append(n);
        sb.append("</a>");
        return sb.toString();
    }

    public String getDefaultListPages() {
        return this.getNumListPages(10);
    }

    public String getJavaScript(){
    	StringBuilder builder = new StringBuilder();
    	builder.append("<script language=\"javascript\">\n" +
    			"function subForm(){\n" +
    			"   var form = document.getElementById('gotoPageForm');\n" +
    			"	form.start.value=form.gotoPageNum.value;" +
    			"   form.submit();\n"+
    			"}\n" +
    	"</script>");
    	return builder.toString();
    }
    
    public String getButtonListPages() {
    	 StringBuffer sb = new StringBuffer();
         int start = pagelist.getStart();
         int maxpage = pagelist.getMaxPage();
         int currpage = start;
         sb.append("<form method=\"post\" name=\"gotoPageForm\" id=\"gotoPageForm\" action=\"");
         sb.append(pagelist.getRelativeURI());
         sb.append("\"><br/>\n");
         sb.append(this.getFirstPage(null));
         sb.append(" ");
         sb.append(this.getPreviousPage(null));
         sb.append(" ");
         sb.append(this.getNextPage(null));
         sb.append(" ");
         sb.append(this.getLastPage(null));
         sb.append(" \n");
         String uri = this.pagelist.getURIAndParams();
         String params = uri.substring(uri.lastIndexOf('?') + 1);
         StringTokenizer st = new StringTokenizer(params, "&");
         while (st.hasMoreTokens()) {
             String param = st.nextToken();
             int n = param.indexOf('=');
             if (n == -1)
                 continue;
             String name = param.substring(0, n);
             String value = param.substring(n + 1);
             sb.append("<input type=\"hidden\" name=\"");
             sb.append(name);
             sb.append("\" value=\"");
             sb.append(value);
             sb.append("\"/>");
         }
         sb.append(" 第");
         sb.append(currpage);
         sb.append("/");
         sb.append(maxpage);
         sb.append("页 共");
         sb.append(pagelist.getCount());
         sb.append("条记录 ");
         sb.append(" \n转到：<input type=\"text\" name=\"gotoPageNum\" id=\"gotoPageNum\" size=\"3\" value=\"");
         sb.append((currpage));
         sb.append("\"/>\n");
         sb.append("<a href=\"#here\" onclick=\"subForm();\">页go</a>");
         sb.append("\n</form>\n");
         return sb.toString();
    }

    public String getNumListPages(int most) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("请先执行查询操作");
        int start = pagelist.getStart();
        int pagesize = pagelist.getPageSize();
        int maxpage = pagelist.getMaxPage();
        int currpage = start / pagesize + 1;
        int ngroup = currpage / most + (currpage % most == 0 ? -1 : 0); //当前显示组1 2 3 4 5 6 7 8
        int firstPage = ngroup * most + 1;
        StringBuffer sb = new StringBuffer();
        sb.append("共");
        sb.append(pagelist.getCount());
        sb.append("条，");
        sb.append(maxpage);
        sb.append("页 结果页码 ");
        if (start > 0) {
            sb.append(this.getPreviousPage("<<上一页"));
        }
        sb.append(" ");
        if (firstPage > 1) {
            sb.append(this.getGoToPage(firstPage - 1));
        }
        int pageNum = firstPage;
        for (int i = 0; i < most; i++) {
            pageNum = firstPage + i;
            if (pageNum > maxpage)
                break;
            sb.append(" ");
            if (currpage == pageNum) {
                sb.append("<font color=\"red\">");
                sb.append(pageNum);
                sb.append("</font>");
            } else {
                sb.append(this.getGoToPage(pageNum));
            }
        }
        if (maxpage > currpage) {
            sb.append(" ");
            sb.append(this.getNextPage("下一页>>"));
        }
        return sb.toString();

    }
    public abstract List<T> getResultSet();
    public abstract List<T> getResultSet(Filter<T> filter);
    public abstract List<T> getResultSet(List<T> list);

  /**
   * 获得总结果大小，要回调pagelist.getCountSql()
   * @return
   */
    public int getCount() {
        int total = 0;
        return total;
    }
    /**
     * 垃圾清理时关闭数据库连接
     */
    public void destroy() {
        this.close();
    }

    /**
     * 关闭本管理器与数据库有关的连接
     */
    public void close() {
    }
}
