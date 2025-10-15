package fire.pb.fushi;

import mkdb.Procedure;

public class PRequestVipJiangli extends Procedure {
	long roleid;
	int bounusindex;
	
	public PRequestVipJiangli(long roleid, int bounusindex) {
		this.roleid = roleid;
		this.bounusindex = bounusindex;
	}
	
	@Override
	protected boolean process() throws Exception {
		fire.pb.fushi.SVipInfoConfig vipinfocfg = Module.getVipInfoConfig(bounusindex);
		if(roleid > 0 && vipinfocfg != null)
		{
			xbean.Vipinfo vipinfo = xtable.Vipinfo.get(roleid);
			if (null == vipinfo) {
				vipinfo = xbean.Pod.newVipinfo();
				xtable.Vipinfo.insert(roleid, vipinfo);
			}
			if(vipinfo.getViplevel() < bounusindex)
			{
				FushiManager.logger.error("错误的vip等级。roleId：" + roleid + "  奖励index = "+ bounusindex);
				return false;
			}
			int bitVar = (int)java.lang.Math.pow(2, bounusindex - 1);
			//有奖可领还未领
			if(((vipinfo.getBonus()&bitVar)>0) && ((vipinfo.getGotbonus()&bitVar)==0))
			{
				for(int i = 0; i < vipinfocfg.itemids.size() && i < vipinfocfg.itemcounts.size(); i++)
				{
					int itemid = vipinfocfg.itemids.get(i);
					int itemcount = vipinfocfg.itemcounts.get(i); 		
					int added = fire.pb.util.BagUtil.addItem(roleid, itemid, itemcount, fire.pb.skill.liveskill.LiveSkillManager.ChargetGrab, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chongzhiget, 1, true);
					if (itemcount != added)
					{
						FushiManager.logger.info("发放奖励失败可能是背包满了");
						fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 140655, null);
						return false;						
					}
				}
				vipinfo.setGotbonus(vipinfo.getGotbonus()|bitVar);
			}
			
			SSendVipInfo send = new SSendVipInfo();
			send.vipexp = vipinfo.getVipexp();
			send.viplevel = vipinfo.getViplevel();
			send.bounus = vipinfo.getBonus();
			send.gotbounus = vipinfo.getGotbonus();
			Procedure.psendWhileCommit(roleid, send);
			
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 190026, null);

			return true;
		}
		return false;
	}
}

