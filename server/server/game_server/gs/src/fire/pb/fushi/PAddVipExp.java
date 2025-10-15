package fire.pb.fushi;

import java.util.Map;

import fire.log.beans.OpLvlBean;
import fire.pb.item.BagTypes;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

public class PAddVipExp extends Procedure {
	public final static int REASON_ADD_VIP_EXP_GM = 0;				// GM命令获得
	public final static int REASON_ADD_VIP_EXP_MAIL_AWARD = 1;		// 邮件获得
	public final static int REASON_ADD_VIP_EXP_CHARGE = 2;			// 充值
	public final static int REASON_ADD_VIP_EXP_SYSTEM_RET = 3;		// 系统返还
	public final static int REASON_ADD_VIP_EXP_PRESENT = 4;			// 禮包赠送
	
	long roleid;
	int vipexp;
	int reason;
	
	public PAddVipExp(long roleid, int vipexp, int reason) {
		this.roleid = roleid;
		this.vipexp = vipexp;
		this.reason = reason;
	}
	
	@Override
	protected boolean process() throws Exception {
		if (roleid<=0)
			return false;
		
		// 增加充值经验时候检查下是否没首冲(需要在点卡服判断之前,因为点卡服也有奖励,只是没有经验)	add by yebin
		if(this.vipexp > 0){
			mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
				@Override
				protected boolean process() {
					Integer userid = xtable.Properties.selectUserid(roleid);
					if (userid == null) {
						return false;
					}
					
					xbean.FirstCharge firstCharge = xtable.Firstchargenew.get(userid);
					if (firstCharge == null){
						firstCharge = xbean.Pod.newFirstCharge();
						xtable.Firstchargenew.insert(userid, firstCharge);
					}
					
					if(firstCharge.getChargestatus() == 0){
						if (firstCharge.getFirstchargetime() < FushiManager.FIRST_CHARGE_START_TIME){
							firstCharge.setFirstchargetime(System.currentTimeMillis());
						}
						if (firstCharge.getFirstchargeclearpresenttime() < FushiManager.FIRST_CHARGE_CLEAR_PRESENT_START_TIME) {
							firstCharge.setFirstchargeclearpresenttime(System.currentTimeMillis());
							firstCharge.setChargestatus(1);
						}
						psendWhileCommit(roleid, new SRefreshChargeState((int)firstCharge.getChargestatus(), 0));
					}
					return true;
				}
			});
		}
		
		// 点卡服没有Vip经验
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
			return true;
		
		xbean.Vipinfo vipinfo = xtable.Vipinfo.get(roleid);
		if (null == vipinfo) {
			vipinfo = xbean.Pod.newVipinfo();
			xtable.Vipinfo.insert(roleid, vipinfo);
		}
		int exp = vipexp + vipinfo.getVipexp();
		int oldexp = vipinfo.getVipexp();
		vipinfo.setVipexp(exp);
		int oldlevel = vipinfo.getViplevel();
		int newlevel = oldlevel;
		int bonus = 0;
		for (fire.pb.fushi.SVipInfoConfig svipinfo : fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.fushi.SVipInfoConfig.class).values())
		{
			if(svipinfo.exp <= exp)
			{
				int aa = (int)(Math.pow(2,(svipinfo.id-1)));
				bonus=bonus+aa; 
				newlevel = svipinfo.id;
			}
			else
				break;
		}

		if (newlevel > oldlevel) {
			xbean.Properties prop = xtable.Properties.get(roleid);
			if(prop == null)
				throw new IllegalArgumentException("错误的roleId：" + roleid);
			prop.setViplevel(newlevel);
			vipinfo.setViplevel(newlevel);			
			vipinfo.setBonus(bonus);
			// 修改各种福利
			Map<Integer, SVipInfoConfig> vipMap = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.fushi.SVipInfoConfig.class);
			for (int i = oldlevel + 1; i < newlevel + 1; i++) {
				SVipInfoConfig vipval = vipMap.get(i);
				int addsize = vipval.getViprights().get(SSendVipInfo.RIGHT_STORAGE);
				if (addsize > 0)
					new fire.pb.item.PAddVipPack(roleid, BagTypes.DEPOT, addsize).call();
				
				addsize = vipval.getViprights().get(SSendVipInfo.RIGHT_PACKAGE);
				if (addsize > 0)
					new fire.pb.item.PAddVipPack(roleid, BagTypes.BAG, addsize).call();
				
				int huoban = vipval.getViprights().get(SSendVipInfo.RIGHT_HUOBAN);
				if (huoban > 0)
					fire.pb.huoban.Module.setVipHuoBanCount(roleid, huoban);
				
				int yingfu = vipval.getViprights().get(SSendVipInfo.RIGHT_EXT_YINFURATE);
				if (yingfu > 0) {
					prop.setYingfuxishu(yingfu);
				}
				
				//增加宠物栏数量
				fire.pb.pet.PetColumn petCol = new fire.pb.pet.PetColumn(roleid, PetColumnTypes.PET, false);
//				if (petCol.getCapacity() >= petCol.getPetColumnMaxCapacity()) {
//					continue;
//				}

				int addpetsize = vipval.getViprights().get(SSendVipInfo.RIGHT_PET_SLOT);
				if (addpetsize > 0) {
					// 增加容量
					petCol.incCapacity(addpetsize);
					petCol.refreshCapacity();
				}
			}
			
			java.util.List<String> parameters = new java.util.ArrayList<String>();
			String s = Integer.toString(newlevel);
			parameters.add(s);
			MessageMgr.psendMsgNotify(roleid, 190058, parameters);
			
			/*//发送商城购买信息
			fire.pb.fushi.SVipInfoConfig vipInfoCfg = Module.getVipInfoConfig(newlevel);
			if (vipInfoCfg == null) {
				return false;
			}
			if (vipInfoCfg.viprights.get(SSendVipInfo.RIGHT_DAILY_GOODS_COUNT) > 0 
					|| vipInfoCfg.viprights.get(SSendVipInfo.RIGHT_WEEKLY_DISCOUNT_COUNT) > 0) {
				new fire.pb.shop.PQueryLimit(roleid, fire.pb.shop.utils.LimitType.QUERY_BUY, null).call();
			}*/
			
			// 运营日志
			OpLvlBean opLvlBean = new OpLvlBean(OpLvlBean.Op_LvlBean_LvlType_Game, 
					oldlevel, newlevel, oldexp, exp, reason);
			fire.log.YYLogger.OpLvlLog(roleid, opLvlBean);
			
			new PAddVipSuppRegNumOfLv(roleid, oldlevel).call();
		}
		SSendVipInfo send = new SSendVipInfo();
		send.vipexp = vipinfo.getVipexp();
		send.viplevel = vipinfo.getViplevel();
		send.bounus = vipinfo.getBonus();
		send.gotbounus = vipinfo.getGotbonus();
		Procedure.psendWhileCommit(roleid, send);
		return true;
	}
}

