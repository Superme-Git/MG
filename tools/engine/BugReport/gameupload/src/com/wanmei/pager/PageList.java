package com.locojoy.pager;

/**
 * ҳ���б���������Ķ�����Ҫ�Ľӿ�
 * 
 * @author DevUser
 * 
 */
public class PageList {
	private int pageSize = 15;// ��ҳ��С

	private int maxPage = 1;//��ҳ��
	
	private int start = 1;// ��ʼҳ

	private int count = -1;

	private String sortField = null;// �����ֶ�

	private boolean desc = false;// �Ƿ�����

	private String relativeURI = "";// ���·��

	private String URIAndParams = null;// ����
	
	public PageList(int pageSize,int count){
		this.pageSize = pageSize;
		this.count = count;
		this.maxPage = count / pageSize + (count % pageSize == 0 ? 0 : 1);
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public int getStart() {
		return this.start;
	}

	public void setStart(int n) {
		if(n<0)
			this.start = 1;
		else if(n >= 0 && n<=this.maxPage)
			this.start = n;
		else if(n > this.maxPage)
			this.start = this.maxPage;
	}

	public int getCount() {
		return this.count;
	}

	public String getRelativeURI() {
		return this.relativeURI;
	}

	public void setRelativeURI(String s) {
		if (s != null)
			this.relativeURI = s;
		if (this.getURIAndParams() != null
				&& !this.getURIAndParams().startsWith(s)) {
			this.setURIAndParams(s + this.getURIAndParams());
		}
	}

	public String getURIAndParams() {
		if (this.URIAndParams != null)
			return this.URIAndParams;
		StringBuffer sb = new StringBuffer();
		if (this.getRelativeURI() != null) {
			sb.append(this.getRelativeURI());
		}

		sb.append("?start=");
		sb.append(start);

		if (this.sortField != null) {
			sb.append("&sortField=");
			sb.append(sortField);
			sb.append("&desc=");
			sb.append(desc);
		}
		if (this.pageSize != 15) {
			sb.append("&pageSize=");
			sb.append(pageSize);
		}
		return sb.toString();
	}

	public void setURIAndParams(String URIAndParams) {
		this.URIAndParams = URIAndParams;
	}

	public String getSortField() {
		return this.sortField;
	}

	public void setSortField(String s) {
		if (s != null) {
			this.sortField = s;
		}

	}

	public boolean isDesc() {
		return this.desc;
	}

	public void setDesc(boolean b) {
		this.desc = b;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
}
