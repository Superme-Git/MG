package com.locojoy.pager;
import java.util.List;
import java.util.StringTokenizer;

public abstract class PageManager<T> {
    /**
     * ��¼��
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
     * �����ҳ�����ӵ�HTML tag
     * @param name ���ƣ��ǡ���ҳ�������ñ���ַ������name==null,����ȱʡ�ġ���ҳ���ַ�
     * @return ��ҳ�����ӵ�HTML tag
     */
    public String getFirstPage(String name) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("����ִ�в�ѯ����");
        if (name == null)
            name = "��ҳ";
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
     * �����һҳ�����ӵ�HTML tag
     * @param name ���ƣ��ǡ���һҳ�������ñ���ַ������name==null,����ȱʡ�ġ���һҳ���ַ�
     * @return ��һҳ�����ӵ�HTML tag
     */
    public String getPreviousPage(String name) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("����ִ�в�ѯ����");
        if (name == null)
            name = "��һҳ";
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
     * �����һҳ�����ӵ�HTML tag
     * @param name ���ƣ��ǡ���һҳ�������ñ���ַ������name==null,����ȱʡ�ġ���һҳ���ַ�
     * @return ��һҳ�����ӵ�HTML tag
     */
    public String getNextPage(String name) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("����ִ�в�ѯ����");
        if (name == null)
            name = "��һҳ";
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
     * ���ĩҳ�����ӵ�HTML tag
     * @param name ���ƣ��ǡ�ĩҳ�������ñ���ַ������name==null,����ȱʡ�ġ�ĩҳ���ַ�
     * @return ĩҳ�����ӵ�HTML tag
     */
    public String getLastPage(String name) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("����ִ�в�ѯ����");
        if (name == null)
            name = "ĩҳ";
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
     * ���ͨ���ı�����������ֱ����ת��ĳҳ�����ӵ�HTML tag
     * @param pageNum ���ڼ�ҳ
     * @return ͨ���ı�����������ֱ����ת��ĳҳ�����ӵ�HTML tag
     */
    private String getGotoPageLink(int pageNum) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("����ִ�в�ѯ����");
        int start = pagelist.getStart();
        int pagesize = pagelist.getPageSize();
        String uri = pagelist.getURIAndParams();
        int newstart = (pageNum - 1) * pagesize;
        //�������һҳ������Ϊ���һҳ
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
         sb.append(" ��");
         sb.append(currpage);
         sb.append("/");
         sb.append(maxpage);
         sb.append("ҳ ��");
         sb.append(pagelist.getCount());
         sb.append("����¼ ");
         sb.append(" \nת����<input type=\"text\" name=\"gotoPageNum\" id=\"gotoPageNum\" size=\"3\" value=\"");
         sb.append((currpage));
         sb.append("\"/>\n");
         sb.append("<a href=\"#here\" onclick=\"subForm();\">ҳgo</a>");
         sb.append("\n</form>\n");
         return sb.toString();
    }

    public String getNumListPages(int most) {
        if (pagelist.getCount() < 0)
            throw new NullPointerException("����ִ�в�ѯ����");
        int start = pagelist.getStart();
        int pagesize = pagelist.getPageSize();
        int maxpage = pagelist.getMaxPage();
        int currpage = start / pagesize + 1;
        int ngroup = currpage / most + (currpage % most == 0 ? -1 : 0); //��ǰ��ʾ��1 2 3 4 5 6 7 8
        int firstPage = ngroup * most + 1;
        StringBuffer sb = new StringBuffer();
        sb.append("��");
        sb.append(pagelist.getCount());
        sb.append("����");
        sb.append(maxpage);
        sb.append("ҳ ���ҳ�� ");
        if (start > 0) {
            sb.append(this.getPreviousPage("<<��һҳ"));
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
            sb.append(this.getNextPage("��һҳ>>"));
        }
        return sb.toString();

    }
    public abstract List<T> getResultSet();
    public abstract List<T> getResultSet(Filter<T> filter);
    public abstract List<T> getResultSet(List<T> list);

  /**
   * ����ܽ����С��Ҫ�ص�pagelist.getCountSql()
   * @return
   */
    public int getCount() {
        int total = 0;
        return total;
    }
    /**
     * ��������ʱ�ر����ݿ�����
     */
    public void destroy() {
        this.close();
    }

    /**
     * �رձ������������ݿ��йص�����
     */
    public void close() {
    }
}
