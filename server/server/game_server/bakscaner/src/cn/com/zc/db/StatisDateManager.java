package cn.com.zc.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import cn.com.zc.db.bean.mbean.ChargeUserBean;
import cn.com.zc.db.bean.mbean.ConsumeRoleBean;
import cn.com.zc.db.bean.mbean.export.ActiveRole;
import cn.com.zc.db.bean.mbean.export.ActiveUser;
import cn.com.zc.db.bean.mbean.export.ChargeUser;
import cn.com.zc.db.bean.mbean.export.ConsumeRole;
import cn.com.zc.db.bean.mbean.export.TotalValueState;

public class StatisDateManager {
	
	private static StatisDateManager _instance;
	
	private StatisDateManager(){}
	
	private AtomicInteger historyUserNum = new AtomicInteger(0);
	private AtomicInteger historyRoleNum = new AtomicInteger(0);
	
	private AtomicInteger increaseUserNum = new AtomicInteger(0);
	private AtomicInteger increaseRoleNum = new AtomicInteger(0);
	
	private Map<Integer, List<Integer>> activeUserMap = new HashMap<Integer, List<Integer>>();
	private Map<Integer, Map<Integer, ChargeUserBean>> chargeUserMap = new HashMap<Integer, Map<Integer, ChargeUserBean>>();
	
	private Map<Integer, Map<Long, Integer>> activeRoleMap = new HashMap<Integer, Map<Long, Integer>>();
	private Map<Long, ConsumeRoleBean> consumeRoleMap = new HashMap<Long, ConsumeRoleBean>();
	
	public static StatisDateManager getInstance(){
		synchronized(StatisDateManager.class){
			if(null == _instance){
				_instance = new StatisDateManager();
			}
			
			return _instance;
		}
	}
	
	public void setActiveRoleMap(int type, long combinatiom){
		Map<Long, Integer> counter = activeRoleMap.get(type);
		if(null == counter){
			counter = new HashMap<Long, Integer>();
			activeRoleMap.put(type, counter);
		}
		
		Integer oldValue = counter.get(combinatiom);
		if(null == oldValue){
			counter.put(combinatiom, 1);
		}else{
			counter.put(combinatiom, oldValue + 1);
		}
		
	}
	
	public Map<Long, Integer> getActiveRoleByType(int type){
		return activeRoleMap.get(type);
	}
	
	public ConsumeRoleBean getConsumeRoleByType(int type){
		return consumeRoleMap.get(type);
	}
	
	public void setConsumeRole(long combinatiom, int num, int consumeType){
		ConsumeRoleBean counter = consumeRoleMap.get(combinatiom);
		if(null == counter){
			counter = new ConsumeRoleBean();
			counter.addRoleNum(1);
			counter.addConsumeTotal(num);
			counter.setConsumeType(consumeType);
			
			consumeRoleMap.put(combinatiom, counter);
			return;
		}
		
		counter.addConsumeTotal(num);
		counter.addRoleNum(1);
	}
	
	public void setChargeUser(int type, int regionValue, int chargeNum){
		Map<Integer, ChargeUserBean> userList = chargeUserMap.get(type);
		if(null == userList){
			userList = new HashMap<Integer, ChargeUserBean>();
			ChargeUserBean bean = new ChargeUserBean();
			bean.addChargeTotal(chargeNum);
			bean.addUserNum(1);
			
			userList.put(regionValue, bean);
			chargeUserMap.put(type, userList);
			
			return;
		}
		
		ChargeUserBean bean = userList.get(regionValue);
		if(null == bean){
			bean = new ChargeUserBean();
			bean.addChargeTotal(chargeNum);
			bean.addUserNum(1);
			userList.put(regionValue, bean);
		}else{
			bean.addChargeTotal(chargeNum);
			bean.addUserNum(1);
		}
	}
	
	public Map<Integer, ChargeUserBean> getChargeUserByType(int type){
		return chargeUserMap.get(type);
	}
	
	public void setActiveUser(int type, int userId){
		List<Integer> userList = activeUserMap.get(type);
		if(null == userList){
			userList = new ArrayList<Integer>();
			userList.add(userId);
			activeUserMap.put(type, userList);
			return;
		}
		
		userList.add(userId);
	}
	
	public List<Integer> getActiveUserByType(int type){
		return activeUserMap.get(type);
	}
	
	public void addHistoryUserNum(int addNum){
		historyUserNum.addAndGet(addNum);
	}
	
	public int getHistoryUserNum(){
		return historyUserNum.get();
	}

	public void addHistoryRoleNum(int addNum){
		historyRoleNum.addAndGet(addNum);
	}
	
	public int getHistoryRoleNum(){
		return historyRoleNum.get();
	}
	
	public void addIncreaseUserNum(int addNum){
		increaseUserNum.addAndGet(addNum);
	}
	
	public int getIncreaseUserNum(){
		return increaseUserNum.get();
	}

	public void addIncreaseRoleNum(int addNum){
		increaseRoleNum.addAndGet(addNum);
	}
	
	public int getIncreaseRoleNum(){
		return increaseRoleNum.get();
	}
	
	public TotalValueState fillExportBean(){
		TotalValueState stateResult = new TotalValueState();
		
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy年M月d日 HH:mm");
		String currentTime = fomat.format(System.currentTimeMillis());
		stateResult.setTick(currentTime);
		
		stateResult.setHistoryUserNum(getHistoryUserNum());
		stateResult.setIncreaseUserNum(getIncreaseUserNum());
		
		stateResult.setHistoryRoleNum(getHistoryRoleNum());
		stateResult.setIncreaseRoleNum(getIncreaseRoleNum());
		
		stateResult.setActiveUsersList(fillActiveUserList());
		
		stateResult.setActiveRoleList(fillActiveRoleList());
		
		stateResult.setChargeUsersList(fillChargeUserList());
		
		stateResult.setConsumeRolesList(fillConsumeRoleList());
		
		return stateResult;
	}

	private List<ConsumeRole> fillConsumeRoleList() {
		Iterator<Entry<Long, ConsumeRoleBean>> iterator = consumeRoleMap.entrySet().iterator();
		List<ConsumeRole> result = new ArrayList<ConsumeRole>();
		
		while(iterator.hasNext()){
			Entry<Long, ConsumeRoleBean> current = iterator.next();
			ConsumeRole consumeRole = new ConsumeRole();
			consumeRole.setConsumeType(current.getValue().getConsumeType());
			consumeRole.setTotalNum(current.getValue().getConsumeTotal());
			consumeRole.setRoleNum(current.getValue().getRoleNum());
			Long key = current.getKey();
			//Long型的低32位是key的有效位
			// 8位                6位            6位     6位          6位
			// 等级段+消耗额度段+门派+主角+在线时长
			consumeRole.setLevel((int) (key >> 24));
			consumeRole.setConsumeValue((int) ((key >> 18) & 63));
			consumeRole.setSchool((int) ((key >> 12) & 63));
			consumeRole.setShape((int) ((key >> 6) & 63));
			consumeRole.setOnlineTime((int) (key & 63));
			
			result.add(consumeRole);
		}
		
		return result;
	}

	private List<ChargeUser> fillChargeUserList() {
		Iterator<Entry<Integer, Map<Integer, ChargeUserBean>>> iterator = chargeUserMap.entrySet().iterator();
		List<ChargeUser> result = new ArrayList<ChargeUser>();
		
		while(iterator.hasNext()){
			Entry<Integer, Map<Integer, ChargeUserBean>> current = iterator.next();
			int chargeType = current.getKey();
			Iterator<Entry<Integer,ChargeUserBean>>detail = current.getValue().entrySet().iterator();
			while(detail.hasNext()){
				Entry<Integer,ChargeUserBean> thisNode = detail.next();
				ChargeUser chargeUser = new ChargeUser();
				chargeUser.setChargeType(chargeType);
				chargeUser.setChargeValue(thisNode.getKey());
				chargeUser.setTotalNum(thisNode.getValue().getChargeTotal());
				chargeUser.setUserNum(thisNode.getValue().getUserNum());
				
				result.add(chargeUser);
			}
			
		}
		
		return result;
	}

	private List<ActiveRole> fillActiveRoleList() {
		Iterator<Entry<Integer, Map<Long, Integer>>> iterator = activeRoleMap.entrySet().iterator();
		List<ActiveRole> result = new ArrayList<ActiveRole>();
		
		while(iterator.hasNext()){
			Entry<Integer, Map<Long, Integer>> current = iterator.next();
			int activeType = current.getKey();
			Iterator<Entry<Long, Integer>> detail = current.getValue().entrySet().iterator();
			while(detail.hasNext()){
				Entry<Long, Integer> thisNode = detail.next();
				Long key = thisNode.getKey();
				ActiveRole activeRole = new ActiveRole();
				 //Long型的低32位是key的有效位
				 //每8位标示一个字段：等级段+门派+主角+在线时长
				activeRole.setActiveType(activeType);
				activeRole.setLevel((int) (key >> 24));
				activeRole.setSchool((int) ((key >> 16)&255));
				activeRole.setShape((int) ((key >> 8)&255));
				activeRole.setOnlineTime((int) (key&255));
				activeRole.setTotalNum(thisNode.getValue());
				
				result.add(activeRole);
			}
		
		}
		
		return result;
	}

	private List<ActiveUser> fillActiveUserList() {
		Iterator<Entry<Integer, List<Integer>>> iterator = activeUserMap.entrySet().iterator();
		List<ActiveUser> result = new ArrayList<ActiveUser>();
		
		while(iterator.hasNext()){
			Entry<Integer, List<Integer>> current = iterator.next();
			ActiveUser activeUser = new ActiveUser();
			activeUser.setActiveType(current.getKey());
			activeUser.setTotalNum(current.getValue().size());
		
			result.add(activeUser);
		}
		return result;
	}
	
	public static void main(String[] args) {
		TotalValueState state = getInstance().fillExportBean();
		XStream xStream = new XStream(new DomDriver());
		String xml = xStream.toXML(state);
		System.out.println(xml);
	}

	
}
