package cn.com.zc.db.bean;

import java.util.ArrayList;
import java.util.List;

public class RoseListBean {
	
	private String tick;
	
	private List<RoseRecordBean> roseRankList = new ArrayList<RoseRecordBean>();
	
	public String getTick() {
		return tick;
	}

	public void setTick(String tick) {
		this.tick = tick;
	}

	public List<RoseRecordBean> getRoseRankList() {
		return roseRankList;
	}

	public void setRoseRankList(List<RoseRecordBean> roseRankList) {
		this.roseRankList = roseRankList;
	}
	
	private static RoseListBean _instance = new RoseListBean();
	
	public static RoseListBean getInstance(){
		return _instance;
	}

}
