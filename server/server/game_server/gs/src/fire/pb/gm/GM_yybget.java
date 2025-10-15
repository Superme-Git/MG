package fire.pb.gm;

import mkdb.Procedure;

import org.apache.http.client.methods.HttpGet;

import fire.pb.fushi.FushiManager;
import fire.pb.fushi.SReqFushiInfo;
import fire.pb.fushi.YybGetCurrencyHandler;
import fire.pb.main.Gs;

public class GM_yybget extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final int userID = getGmUserid();
		final long roleID = getGmroleid();
		
		@SuppressWarnings("unused")
		int addNum = FushiManager.getInstance().getYybGenBalance(120);
		FushiManager.logger.info("CReqFushiInfo:userid:" + userID + ",roleid:" + roleID);
		if(fire.pb.fushi.Module.getIsYYBUser(userID)){
			try {
				long nowtime = System.currentTimeMillis();
				final HttpGet request = FushiManager.makeYybGetCurrencyRequest(userID, roleID);
				if(request != null) {
					Gs.getHttpClient().execute(request, 
						new YybGetCurrencyHandler(userID, roleID){
						@Override
						protected boolean executeHandler(int balance, int genBalance, int firstSave, int saveAmt){
							//FushiManager.refreshRoleFushi(roleID, balance, saveAmt, true);
							FushiManager.logger.info("用去的时间:" + (System.currentTimeMillis() - nowtime));
							if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
							{
								fire.pb.fushi.DayPayManager.getInstance().ProcessDayPay(userID, roleID);
							}
							
							SReqFushiInfo snd = new SReqFushiInfo();
							snd.balance = balance;
							snd.genbalance = genBalance;
							snd.firstsave = firstSave;
							snd.saveamt = saveAmt;
							Procedure.psendWhileCommit(roleID, snd);
							FushiManager.logger.info("CReqFushiInfo:userid:" + userID + ",roleid:" + roleID + 
									",balance:" + balance +
									",genbalance:" + genBalance +
									",firstsave:" + firstSave +
									",saveamt:" + saveAmt);
							return true;
						}
					});
				} 
				else {
					FushiManager.logger.error(new StringBuilder("roleId=").append(roleID).append("获取应用宝数据失败！"));
				}
			}
			catch(Exception e){
				FushiManager.logger.error(new StringBuilder("roleId=").append(roleID).append("应用宝 发送玩家的符石信息错误：") , e);
			}
		}
		
		return true;
	}

	@Override
	String usage() {
		return "//yybget";
	}

}
