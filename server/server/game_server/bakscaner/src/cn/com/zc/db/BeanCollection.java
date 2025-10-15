package cn.com.zc.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.com.zc.db.bean.RoleLevelAndMoneyBean;
import cn.com.zc.db.bean.RoleLevelBean;
import cn.com.zc.db.bean.RoleMoneyBean;


public class BeanCollection {
	
	private static BeanCollection _instance;
	
	private BeanCollection(){}

	private static List<RoleMoneyBean> roleMoneyList = new ArrayList<RoleMoneyBean>();
	
	private static List<RoleLevelBean> roleLevelList = new ArrayList<RoleLevelBean>();
	
	private static int tmpCounter = 0;
	
	
	public RoleLevelAndMoneyBean fillRoleLevelAndMoney(){
		RoleLevelAndMoneyBean levelAndMoneyBean = new RoleLevelAndMoneyBean();
		
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy年M月d日 HH:mm");
		String currentTime = fomat.format(System.currentTimeMillis());
		levelAndMoneyBean.setTick(currentTime);
		
		levelAndMoneyBean.setRoleLevelList(roleLevelList);
		
		levelAndMoneyBean.setRoleMoneyList(roleMoneyList);
		
		return levelAndMoneyBean;
	}
	
	public static BeanCollection getInstance(){
		synchronized(BeanCollection.class){
			if(null == _instance){
				_instance = new BeanCollection();
			}
			
			return _instance;
		}
	}
	
	public void toInsertRoleLevelRank(RoleLevelBean bean, List<RoleLevelBean> beanList) {
		int size = beanList.size();
		if(size == 0){
			beanList.add(bean);
		}
		
		for(int i=size-1; i>=0; i--){
			RoleLevelBean befor = beanList.get(i);
			
			if(!bean.isGreater(befor)){
				beanList.add(i+1, bean);
				break;
			}else{
				if(i == 0){
					beanList.add(i, bean);
				}else{
					continue;
				}
			}
			
		}
		
		if(beanList.size() > bean.getNumMax()){
			beanList.remove(beanList.size() - 1);
		}
	}
	
	public void toInsertRoleMoneyRank(RoleMoneyBean bean, List<RoleMoneyBean> beanList) {
		tmpCounter ++;
		int size = beanList.size();
		if(tmpCounter == 749){
			System.out.println("waiting....");
		}
		
		if(size == 0){
			beanList.add(bean);
		}
		
		for(int i=size-1; i>=0; i--){
			RoleMoneyBean befor = beanList.get(i);
			
			if(!bean.isGreater(befor)){
				beanList.add(i+1, bean);
				break;
			}else{
				if(i == 0){
					beanList.add(i, bean);
				}else{
					continue;
				}
			}
			
		}
		
		if(beanList.size() > bean.getNumMax()){
			beanList.remove(beanList.size() - 1);
		}
	}
	
	
	public static List<RoleMoneyBean> getRoleMoneyList() {
		return roleMoneyList;
	}

	public static List<RoleLevelBean> getRoleLevelList() {
		return roleLevelList;
	}
	

}
