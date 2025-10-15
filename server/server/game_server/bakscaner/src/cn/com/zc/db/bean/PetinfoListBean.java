package cn.com.zc.db.bean;

import java.util.ArrayList;
import java.util.List;


public class PetinfoListBean {

	private String tick;
	
	public String getTick() {
		return tick;
	}

	public void setTick(String tick) {
		this.tick = tick;
	}

	public List<PetInfoBean> getPetBeanList() {
		return petBeanList;
	}

	public void setPetBeanList(List<PetInfoBean> petBeanList) {
		this.petBeanList = petBeanList;
	}

	private static PetinfoListBean _instance = new PetinfoListBean();
	
	public static PetinfoListBean getInstance(){
		return _instance;
	}
	
	private List<PetInfoBean> petBeanList = new ArrayList<PetInfoBean>();
	
	
}
