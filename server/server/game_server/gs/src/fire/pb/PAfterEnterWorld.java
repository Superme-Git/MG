
package fire.pb;

//import gacd.ACOnlineStatus3;
//import gacd.ACStatus;
//import gacd.ACStatusAnnounce3;
//import gacd.GACDManager;

import java.util.Map;

import org.apache.http.client.methods.HttpGet;

import com.locojoy.base.Octets;

import fire.pb.activity.award.RewardMgr;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.PAutoAddCircleTask;
import fire.pb.fanpai.PPlayCardItemProc;
import fire.pb.fushi.DayPayManager;
import fire.pb.fushi.MonthCardManager;
import fire.pb.fushi.SRefreshChargeState;
import fire.pb.fushi.FushiManager;
import fire.pb.fushi.payday.SPayServerType;
import fire.pb.fushi.YybGetCurrencyHandler;
import fire.pb.game.POpenTraderBox;
import fire.pb.game.SRoleAccusation;
import fire.pb.instancezone.bingfeng.BingFengLandMgr;
import fire.pb.instancezone.bingfeng.BingFengRole;
import fire.pb.instancezone.bingfeng.SCanEnterBingFeng;
import fire.pb.instancezone.faction.ClanScheduleActivity;
import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.Module;
import fire.pb.item.SGetItemTips;
import fire.pb.main.Gs;
import fire.pb.state.StateManager;
import fire.pb.timer.GameTimeTask;
import fire.pb.util.DateValidate;
import mkdb.Procedure;

// 进入游戏
public class PAfterEnterWorld extends Procedure {

	private final int userId;
	private final long roleId;

	public PAfterEnterWorld(int userID, long roleid) {

		this.userId = userID;
		this.roleId = roleid;
	}
	

	/* (non-Javadoc)
	 * @see xdb.Procedure#process()
	 */
	protected boolean process() {

		xbean.User user = xtable.User.get(userId);
		if (user == null) {
			// 没有找到该USERID
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 没有找到该userId"));
			return false;
		}
		user.setPrevloginroleid(roleId);
		xbean.Properties pro = xtable.Properties.select(roleId);
		if(pro == null)
		{
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 没有找到该Property,roleid=").append(roleId));
			return false;
		}
		
		try{//向玩家发送自己身上的buff信息
			BuffRoleImpl buffrole = new BuffRoleImpl(roleId);
			buffrole.psendCBuffWhileOnline();
		}
		catch(Exception e){
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 向玩家发送自己身上的buff信错误：") , e);
		}
		
		try {// 通知时辰信息
			GameTimeTask.sendGameTimeToRole(roleId);
		}
		catch(Exception e){
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 通知时辰信息错误：") , e);
		}
		
		try {// 给客户端发送所有特殊任务的信息
			CircleTaskManager.getInstance().sendSpecialQuestWhileEnterWord(roleId);
		}
		catch(Exception e){
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 给客户端发送所有特殊任务的信息错误：") , e);
		}
		
		try {				
			if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
			{
//					SPayServerType msg = new SPayServerType();
//					msg.paytype = fire.pb.fushi.Module.GetPayServiceType();
//					mkdb.Procedure.psendWhileCommit(roleId, msg);	
				
				DayPayManager.getInstance().ProcessDayPay(userId, roleId);
			}		
			// 发送玩家的符石信息
			xbean.YbNums ybNums = xtable.Fushinum.select(userId);
			xbean.YbNum ybNum = null;
			if (ybNums!= null)
				ybNum = ybNums.getRoleyb().get(roleId);
			FushiManager.refreshRoleFushi(roleId, ybNum, true);
		}
		catch(Exception e){
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 发送玩家的符石信息错误：") , e);
		}
		
		//发送月卡信息 by changhao
		MonthCardManager.getInstance().SendMonthCardInfo(roleId);
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) //普通服务器 by changhao
		{
			MonthCardManager.getInstance().ProcessLess3Day(roleId);			
		}
		
		try {// 发送玩家的符石信息
			xbean.FirstCharge status = xtable.Firstchargenew.select(userId);
			if (status == null )
			{	//先把首冲去掉了 by changhao
				Procedure.psendWhileCommit(roleId, new SRefreshChargeState(0, 0));
			}
			else{
				xbean.RoleDoubleChareInfo multiCharge = xtable.Roledoublecharge.select(roleId);
				if (status.getChargestatus() == 1) {			
					Procedure.psendWhileCommit(roleId, new SRefreshChargeState(1, 0));
				}else if(status.getFirstchargeclearpresenttime() < FushiManager.FIRST_CHARGE_CLEAR_PRESENT_START_TIME){
					Procedure.psendWhileCommit(roleId, new SRefreshChargeState(0, 0));
				}else if (multiCharge == null || multiCharge.getActivetime() <= FushiManager.MULTICHARGE_CHARGE_START_TIME){
					Procedure.psendWhileCommit(roleId, new SRefreshChargeState(2, 0));
				}
			}
		}
		catch(Exception e){
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 发送玩家的首冲信息错误：" ), e);
		}
		try {// 发送存款
			fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.EnterWorldEvent(roleId));
		}
		catch(Exception e){
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 发送存款错误：" ), e);
		}
		
		
		try {// 发送新手引导
			final fire.pb.SShowedBeginnerTips sendTips = new fire.pb.SShowedBeginnerTips();
			final xbean.BeginnerTip bt = xtable.Beginnertip.select(roleId);
			if (bt != null) {
				for (final Map.Entry<Integer, Integer> e : bt.getTips().entrySet()) {
					if (e.getValue() == 1) {
						sendTips.tipid.add(e.getKey());
					}
				}
			}
			sendTips.pilottype = pro.getPilot();
			mkdb.Procedure.psendWhileCommit(roleId, sendTips);
		}
		catch(Exception e){
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 发送新手引导错误：" ), e);
		}
		
		try {
			ClanScheduleActivity.roleOnline(roleId);
		} catch (Exception e1) {
			StateManager.logger.error("工会副本上线" , e1);
		}
		
		try {
			if(RewardMgr.iwebMultiExp > 0)
				gnet.link.Onlines.getInstance().send(roleId, new SSendServerMultiExp(RewardMgr.iwebMultiExp));
		} catch (Exception e) {
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 服务器多倍上线：") , e);
		}
		
		try {
			pexecuteWhileCommit(new PAutoAddCircleTask(roleId, 1010000, 0, true));
		} catch (Exception e) {
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 上线发送自动循环师门任务异常："), e);
		}
		
		//发送serverID
		try {
			gnet.link.Onlines.getInstance().send(roleId, new SServerIDResponse(Integer.parseInt(Gs.serverid)));
		} catch (Exception e) {
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 上线发送服务器ID异常：" ), e);
		}
		
		//发送装备详细信息
		fire.pb.item.Equip equip = new fire.pb.item.Equip(roleId, false);
		for (fire.pb.item.ItemBase item : equip)
		{
			if (item instanceof EquipItem) {
				Octets tips = item.getTips();
				if (tips == null) {
					Module.logger.error("错误的tips");
					return false;
				}
				SGetItemTips send = new SGetItemTips(BagTypes.EQUIP, item.getKey(), tips);
				Procedure.psendWhileCommit(roleId, send);
			}
		}
		
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		for (fire.pb.item.ItemBase item : bag) {
			if (item instanceof EquipItem) {
				Octets tips = item.getTips();
				if (tips == null) {
					Module.logger.error("错误的tips");
					return false;
				}
				SGetItemTips send = new SGetItemTips(BagTypes.BAG, item.getKey(), tips);
				Procedure.psendWhileCommit(roleId, send);
			}
		}
		
		//发送翻牌数据
		Procedure.pexecute(new PPlayCardItemProc(roleId));
		//发送挖宝数据
//		Procedure.pexecute(new PEndDigTreasureMap(roleId));
		//发送血月商人数据
		Procedure.pexecute(new POpenTraderBox(roleId));
		//发送服务器开服信息
		SServerLevel sLevel = fire.pb.timer.Module.getServerLvData();
		Procedure.psendWhileCommit(roleId, sLevel);
		//发送是否被举报信息
		xbean.AccusationInfo aInfo = xtable.Accusationinfos.select(roleId);
		if(aInfo != null){
			Procedure.psendWhileCommit(roleId, new SRoleAccusation((byte)1));
		}
		
		/*BingFengRole bingFengRole = new BingFengRole(roleId, true);
		if (bingFengRole.getBingFengRoleBean().getTimes() <= 0) {
			xbean.RoleBFInfo rolebingFengInfo = BingFengLandMgr.getInstance().getRoleBingFengInfo(roleId, 0, false);
			bingFengRole.getBingFengRoleBean().setTimes(bingFengRole.getBingFengRoleBean().getTimes() - 1);
			long currentTime = System.currentTimeMillis();
			
			if (DateValidate.inTheSameDay(currentTime, rolebingFengInfo.getLastchangetime())) {
				SCanEnterBingFeng scanenter = new SCanEnterBingFeng();
				scanenter.finish = 1;
				
				Procedure.psendWhileCommit(roleId, scanenter);
			}
		}*/
		
		return true;
	}
}
