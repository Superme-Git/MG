package fire.pb.shop.srv.market.jdbc;

/**
 * 翻页逻辑封装类
 * 
 * @author liangyanpeng
 *
 */
public class Page {
	/**
	 * 总记录数
	 */
	private int totalRow = 0;
	
	/**
	 * 每页记录数
	 */
	public int pageSize = 8;
	
	/**
	 * 当前页码
	 */
	private int currPage = 0;
	
	/**
	 * 总页数
	 */
	private int totalPage = 0;
	
	/**
	 * 起始记录下标
	 */
	private int beginIndex = 0;
	
	/**
	 * 截止记录下标
	 */
	private int endIndex = 0;
	
	/**
	 * 使用总记录数,当前页码和每页记录数构造
	 * @param totalRow 总记录数
	 * @param currPage 页码从1开始
	 * @param pageSize 默认8条
	 */
	public Page(int totalRow, int currPage, int pageSize) {
		this.totalRow = totalRow;
		this.currPage = currPage;
		this.pageSize = pageSize;
		this.calculate();
	}
	
	private void calculate() {
		totalPage = totalRow / pageSize + ((totalRow % pageSize) > 0 ? 1 : 0);
		if (currPage > totalPage) {
			currPage = totalPage;
		} else if (currPage < 1) {
			currPage = 1;
		}
		
		beginIndex = (currPage - 1) * pageSize;
		endIndex = beginIndex + pageSize;
		if (endIndex > totalRow) {
			endIndex = totalRow;
		}
	}

	public int getTotalRow() {
		return totalRow;
	}


	public int getPageSize() {
		return pageSize;
	}


	public int getCurrPage() {
		return currPage;
	}


	public int getTotalPage() {
		return totalPage;
	}


	public int getBeginIndex() {
		return beginIndex;
	}


	public int getEndIndex() {
		return endIndex;
	}
	
}
