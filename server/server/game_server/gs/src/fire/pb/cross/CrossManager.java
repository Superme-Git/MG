package fire.pb.cross;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import fire.pb.battle.BattleField;
import fire.pb.battle.SSendAddFighters;
import fire.pb.battle.SSendBattleEnd;
import fire.pb.battle.SSendRoundScript;
import fire.pb.battle.SSendRoundStart;
import fire.pb.game.STransferRoleTable;
import fire.pb.game.STransferTable;
import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.PetItem;
import fire.pb.item.TaskItem;
import fire.pb.item.TimeOutItem;
import fire.pb.item.task.CanuseItem;
import fire.pb.main.ConfigManager;
import fire.pb.map.GridPos;
import fire.pb.map.Position;
import fire.pb.util.FireProp;
import fire.pb.util.Misc;
import fire.pb.util.OctetsUtil;
import gnet.DeliveryManager;
import gnet.link.Onlines;
import mkdb.Transaction;

import org.apache.log4j.Logger;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import cross.BroadcastData;
import cross.CommonDataTransfer;
import cross.SendUserInfoAndTicket;

/**
 * 跨服战的工具类    
 * @author dc
 */
public class CrossManager {
	public static Logger logger = Logger.getLogger("SYSTEM");
	public static final String crossip = FireProp.getStringValue("sys", "sys.cross.ip");
	public static final int crossport = FireProp.getIntValue("sys", "sys.cross.startport");
	public static final int crossnum = FireProp.getIntValue("sys", "sys.cross.portnum");
	
	static class SingletonHolder {
		static CrossManager singleton = new CrossManager();
	}
	
	public static CrossManager getInstance() {
		return SingletonHolder.singleton;
	}

	//正在跨服的角色,key:userid,value:roleid  
	public Map<Integer,Long> transferingUsers = new ConcurrentHashMap<Integer,Long>(500);
	public long getTransferRoleByUserId(int userId) {
		if (!transferingUsers.containsKey(userId)){
			return 0;
		}
		return transferingUsers.get(userId);
	}
	public long removeTransferRoleByUserId(int userId) {
		return transferingUsers.remove(userId);
	}
	
	//记录玩家的跨服门票
	private Map<Long,String> userTickets = new HashMap<Long,String>();
	
	//需要同步的数据表
	private Map<String,STransferTable> transferTableMap = new HashMap<String,STransferTable>();
	private Map<Integer,STransferRoleTable> transferRoleTableMap = new HashMap<Integer,STransferRoleTable>();
	public STransferTable getSTransferTable(String tableName){
		return transferTableMap.get(tableName);
	}
	public int getSTransferTableSize(){
		return transferRoleTableMap.size();
	}

	private CrossManager(){
		reload();
	}
	
	public void reload() {
		transferTableMap.clear();
		transferRoleTableMap.clear();
		Map<Integer, STransferTable> transfertablemap = ConfigManager.getInstance().getConf(STransferTable.class);
		if(transfertablemap == null){
			throw new NullPointerException("Exception:STransferTable表配置有问题");
		}	
		for (STransferTable tableInfo : transfertablemap.values()){
			transferTableMap.put(tableInfo.getTableName(), tableInfo);
		}
		transferRoleTableMap = ConfigManager.getInstance().getConf(STransferRoleTable.class);
		if(transferRoleTableMap == null){
			throw new NullPointerException("Exception:transferRoleTableMap表配置有问题");
		}
	}
	
	/**
	 * 判断是否是在跨服
	 */
	public boolean isInCrossServer() {	
		return ConfigManager.getIsCrossServer();
	}
	
	/**
	 * 原服发送协议到跨服服务器,原服调用
	 */
	public boolean sendToCrossServer(int dtype, mkio.Protocol protocol) {
		if(isInCrossServer()) {
			logger.info("跨服禁止调用sendToCrossServer！");
			return false;
		}
		CommonDataTransfer dataTransfer = new CommonDataTransfer();
		dataTransfer.flag = Constant.TO_CROSS;
		dataTransfer.dtype = dtype;
		dataTransfer.content = new OctetsStream().marshal(protocol);
		return DeliveryManager.getInstance().send(dataTransfer);
	}
	
	/**
	 * 跨服发送协议到指定服务器,跨服调用
	 */
	public boolean sendToServerByServerId(final int serverId, int dtype, mkio.Protocol protocol) {
		if(!isInCrossServer()) {
			logger.info("原服禁止调用sendToServerByServerId！");
			return false;
		}
		CommonDataTransfer dataTransfer = new CommonDataTransfer();
		dataTransfer.zoneid = serverId;
		dataTransfer.flag = Constant.FROM_CROSS;
		dataTransfer.dtype = dtype;
		dataTransfer.content = new OctetsStream().marshal(protocol);
		return DeliveryManager.getInstance().send(dataTransfer);
	}
	
	/**
	 * 跨服向所有服务器发送协议(广播),跨服调用
	 */
	public boolean broadcast(int dtype, mkio.Protocol protocol) {
		if(!isInCrossServer()) {
			logger.info("原服禁止调用sendToServerByServerId！");
			return false;
		}
		BroadcastData broadcast = new BroadcastData();
		broadcast.dtype = dtype;
		broadcast.content = new OctetsStream().marshal(protocol);
		DeliveryManager.getInstance().send(broadcast);
		return true;
	}
	
	/**
	 * 开始跨服,原服调用
	 */
	public void beginCross(final long roleId){
		if(isInCrossServer()) {
			logger.info("跨服禁止调用beginCross！");
			return;
		}
		try {
			logger.info("CrossManager.beginCross:roleId=" + roleId + "准备开始跨服！");
			final Integer userId = xtable.Properties.selectUserid(roleId);
			if (userId == null) {
				logger.error("CrossManager.beginCross:roleId=" + roleId + "的userId为空！");
				return;
			}
			Long transferingRole = transferingUsers.get(userId);
			if (transferingRole != null) {
				logger.error("CrossManager.beginCross:roleId=" + roleId + "正在跨服,如果失败,请20秒后再试...");
				return;
			}
			String ticketstr = Md5Util.encryptToMD5(String.valueOf(System.currentTimeMillis()));
			final Octets ticket = new Octets(ticketstr.getBytes("UTF-8"));
			userTickets.put(roleId, ticketstr);
			transferingUsers.put(userId, roleId);

			SendUserInfoAndTicket snd = new SendUserInfoAndTicket();
			snd.flag = Constant.TO_CROSS;
			snd.ticket = ticket;
			String accstr = Integer.toString(userId);
			final Octets acc = new Octets(accstr.getBytes("UTF-8"));
			snd.account = acc;
			xbean.AUUserInfo auInfo = xtable.Auuserinfo.select(userId);
			if (auInfo != null) {
				snd.userinfo.algorithm = auInfo.getAlgorithm();
				snd.userinfo.auth.addAll(auInfo.getAuth());
				snd.userinfo.blisgm = (byte) auInfo.getBlisgm();
				snd.userinfo.func = auInfo.getFunc();
				snd.userinfo.funcparm = auInfo.getFuncparm();
				snd.userinfo.gender = (byte)0;
				snd.userinfo.loginip = auInfo.getLoginip();
				snd.userinfo.nickname = auInfo.getNicknameOctets();
				snd.userinfo.userid = userId;
				DeliveryManager.getInstance().send(snd);
			} else {
				logger.error("CrossManager.beginCross:roleId=" + roleId + ",userId=" + userId + ",AUUserInfo为空！");
			}
			//29秒后检测一次跨服数据拷贝状态
			mkdb.Executor.getInstance().schedule(new CheckCopyDataResultTask(roleId), Constant.CROSS_MAX_TIME, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.error("CrossManager.beginCross:roleId=" + roleId + "开始跨服发生异常:" + e);
		}
	}
	
	/**
	 * 开始拷贝需要同步的数据,原服事物中调用
	 */
	public void beginSendRoleData(long roleId, xbean.CrossTempRole crole) {
		if(isInCrossServer()) {
			logger.info("跨服禁止调用beginSendRoleData！");
			return;
		}
		logger.info("CrossManager.beginSendRoleData:roleId=" + roleId + "准备开始同步角色数据！");
		long now = System.currentTimeMillis();
		if (now - crole.getLastcopytime() > Constant.CROSS_COPY_DATA_EXPIRE_TIME  || crole.getCopyresult() == Constant.COPY_DATA_RESULT_FAILED) {
			crole.setLastcopytime(now);
			crole.getFinishedtable().clear();
			crole.setCopyresult(Constant.COPY_DATA_RESULT_PROGRESSING);
		}
		
		for(STransferRoleTable transferRoleTable : transferRoleTableMap.values()){
			if (transferRoleTable.tableName == null || transferRoleTable.tableName.trim().equals("")) {
				logger.error("CrossManager.beginSendRoleData:roleId=" + roleId + "拷贝数据时，表名为空.id=" + transferRoleTable.id);
				return;
			}
			STransferTable transferTable = transferTableMap.get(transferRoleTable.tableName.trim());
			if (transferTable == null) {
				logger.error("CrossManager.beginSendRoleData:roleId=" + roleId + "拷贝数据时，表数据为空.name=" + transferRoleTable.tableName.trim());
				return;
			}	
			Object tableKey = roleId;
			String tableName = transferTable.tableName.trim();
			
			final Octets keyData = OctetsUtil.toOctets(roleId);
			Octets valueData = null;
		    if (tableName.equals("xxx")) {
		    	//有需要根据表名特殊处理的表在此处理
		    } else {
		    	//默认情况都走此处理
		    	valueData = TransferDataManager.getInstance().getTableData(tableName, transferTable.valueType.trim(), tableKey);
		    }
		    		
			SendRoleData snd = new SendRoleData();
			if (valueData == null || keyData == null) {
				snd.roleid = roleId;
				snd.tablename = transferRoleTable.tableName;
				snd.isemptytable = (byte)1;
			} else {
				snd.roleid = roleId;
				snd.tablename = transferRoleTable.tableName;
				snd.isemptytable = (byte)0;
				snd.keydata = keyData;
				snd.valuedata = valueData;
			}
			addRelativeData(roleId, transferTable.tableName.trim(), snd, crole.getFlag());
			sendToCrossServer(Constant.SendRoleData, snd);
			
			//打印一下发送数据及大小的log
			int size = 0;
			size += snd.keydata.size();
			size += snd.valuedata.size();
			for (TableData relateData : snd.relationdata) {
				size += relateData.keydata.size();
				size += relateData.valuedata.size();
			}
			logger.info("CrossManager.beginSendRoleData:发送角色roleId=" + roleId +"的数据表:" + tableName +"成功.总数据大小为:" + size);
		}
	}
	
	/**
	 * 通知客户端登录到跨服服务器
	 * @param roleId
	 */
	public void notifyClientCross(final long roleId) {
		if(isInCrossServer()) {
			logger.info("跨服禁止调用notifyClientCross！");
			return;
		}
		Integer userId = xtable.Properties.selectUserid(roleId);
		if (userId == null) return;
		long transferingRole = getTransferRoleByUserId(userId);
		if (transferingRole == roleId){
			transferingUsers.remove(userId);
		}
		SBeginCorssServer snd = new SBeginCorssServer();
		String ticket = userTickets.remove(roleId);
		if (ticket == null) {
			logger.error("roleId=" + roleId +" tickety is NULL!");
			return;
		}
		snd.account = Integer.toString(userId);;
		snd.ticket = ticket;
		snd.crossip = CrossManager.crossip;
		snd.crossport = CrossManager.crossport;
		snd.crossnum = CrossManager.crossnum;
		logger.info("通知客户端account&userid=" + userId + ",roleId=" + roleId + ",ticket=" + ticket + 
				",可以开始跨服至" + snd.crossip + "," + snd.crossport + "," + snd.crossnum);	
		if (Transaction.current() == null){
			Onlines.getInstance().send(roleId, snd);
		} else{
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		}
	}
	
	
	private void addRelativeData(final long roleId, String tableName, SendRoleData snd, int flag) {
		if (tableName == null) return;
		if (tableName.equals("bag")) {//包裹栏
			ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(roleId, BagTypes.BAG, true);
			for (ItemBase basicItem : bag){
				checkAddRelativeData(roleId, basicItem, snd.relationdata);
			}
		} else if (tableName.equals("equip")) {//装备栏
			ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(roleId, BagTypes.EQUIP, true);
			for (ItemBase basicItem : bag){
				checkAddRelativeData(roleId, basicItem, snd.relationdata);
			}
		} else if (tableName.equals("questbag")) {//任务包裹栏
			ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(roleId, BagTypes.QUEST, true);
			for (ItemBase basicItem : bag){
				checkAddRelativeData(roleId, basicItem, snd.relationdata);
			}
		} else if (tableName.equals("properties")) {
			xbean.Properties properties = xtable.Properties.select(roleId);
			if (properties == null) return;
			List<String> nameStrList = new ArrayList<String>();
			nameStrList.add(properties.getRolename());
			nameStrList.addAll(properties.getUsedname());
			for (String nameStr : nameStrList){
				snd.relationdata.add(new TableData("rolename2key", OctetsUtil.toOctets(roleId), OctetsUtil.toOctets(nameStr)));
			}
			if(flag == Constant.GO_CROSS_RESON_FACTION_TEAM_BATTLE){
				Position pp = new GridPos(Constant.PK_MAP_POSX, Constant.PK_MAP_POSY).toPosition();
				properties.setPosx(pp.getX());
				properties.setPosy(pp.getY());
				properties.setLaststaticsceneid(Constant.PK_MAP_ID);
				properties.setSceneid(Constant.PK_MAP_ID);
			}
			snd.valuedata = new OctetsStream().marshal(properties);
		}
	}
	
	private void checkAddRelativeData(final long roleId, ItemBase basicItem, LinkedList<TableData> relationdata) {
		if (basicItem instanceof EquipItem) {
			EquipItem equipItem = (EquipItem) basicItem;
			final long key = equipItem.getDataItem().getExtid();
			final xbean.Equip equip = xtable.Equips.select(key);
			if (equip == null)
				throw new RuntimeException("角色" + roleId + "拷贝物品道具至跨服服务器时，装备xbean.Equip为空");
			relationdata.add(TransferDataManager.getInstance().genTableData("equips", key, equip));
		} else if (basicItem instanceof GroceryItem) {
			GroceryItem groceryItem = (GroceryItem) basicItem;
			// TODO:
		} else if (basicItem instanceof TaskItem) {
			TaskItem taskItem = (TaskItem) basicItem;
			// TODO:
		} else if (basicItem instanceof PetItem) {
			// TODO:
		} else if (basicItem instanceof TimeOutItem) {
			TimeOutItem timeOutItem = (TimeOutItem) basicItem;
			ItemBase realBasicItem = timeOutItem.getEffectItem();
			checkAddRelativeData(roleId, realBasicItem, relationdata);
		} 
	}
}
