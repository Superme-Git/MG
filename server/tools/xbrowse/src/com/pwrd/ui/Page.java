package com.pwrd.ui;

/**
 * ∑÷“≥œ‡πÿ°£
 * 
 * @author wp853
 *
 */
public class Page 
{
	private static final int DEFAULT_PAGE_ROWS = 20;
	
	private int currentPage;
	private int totalPages;
	private int totalRows;
	private int pageRows;
	
	public Page()
	{
		currentPage = 0;
		totalPages = 0;
		totalRows = 0;
		pageRows = DEFAULT_PAGE_ROWS;
	}

	public int getCurrentPage() 
	{
		return currentPage;
	}

	public void setCurrentPage(int currentPage) 
	{
		this.currentPage = currentPage;
	}

	public int getTotalPages() 
	{
		return totalPages;
	}

	public int getTotalRows() 
	{
		return totalRows;
	}

	public void setTotalRows(int totalRows) 
	{
		this.totalRows = totalRows;
		if(totalRows % pageRows != 0)
			totalPages = totalRows / pageRows + 1;
		else
			totalPages = totalRows / pageRows;
	}

	public int getPageRows() 
	{
		return pageRows;
	}

	public void setPageRows(int pageRows) 
	{
		this.pageRows = pageRows;
	}
	
	public int getPageStartIndex()
	{
		int startIndex = (currentPage - 1) * pageRows;
		if(startIndex < 0)
			startIndex = 0;
		return startIndex;
	}
	
	public int getPageEndIndex()
	{
		int endIndex = getPageStartIndex() + pageRows;
		if(endIndex > totalRows)
			endIndex = totalRows;
		return endIndex;			
	}
	
	public boolean hasNextPage()
	{
		return currentPage < totalPages;
	}
	
	public boolean hasPreviousPage()
	{
		return currentPage > 1;
	}
	
	public void toNextPage()
	{
		++ currentPage;
	}
	
	public void toPreviousPage()
	{
		-- currentPage;
	}
}
