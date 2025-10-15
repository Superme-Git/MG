package com.locojoy.pager;

import java.util.List;
import com.world2.dao.Exceptions;
import com.world2.hql.*;

public class FilePageManager extends PageManager<Exceptions> {

	public FilePageManager(PageList pl){
		super(pl);
	}
	@Override
	public List<Exceptions> getResultSet() {
		int startpage = pagelist.getStart();
		if (startpage < 1)
			startpage = 1;
		int pagecount = pagelist.getPageSize();
		int start = pagecount * (startpage - 1);
		return GameUploadHql.getException(start,pagecount);
	}
	
	public List<Exceptions> getResultSet(String search){
		int startpage = pagelist.getStart();
		if (startpage < 1)
			startpage = 1;
		int pagecount = pagelist.getPageSize();
		int start = pagecount * (startpage - 1);
		return GameUploadHql.getException(start,pagecount,search);
	}

	@Override
	public List<Exceptions> getResultSet(Filter<Exceptions> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Exceptions> getResultSet(List<Exceptions> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
