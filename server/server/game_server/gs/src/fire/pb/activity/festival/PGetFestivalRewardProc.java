
package fire.pb.activity.festival;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.log4j.Logger;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.activity.FestivalConf;
import fire.pb.activity.SFestivalConf;
import fire.pb.activity.SFestivalConf4D;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;

public class PGetFestivalRewardProc extends mkdb.Procedure {
	public final static Logger logger = Logger.getLogger("ACTIVE");
	
	private final long roleid;
	private final int rewardid;
	private Map<Integer, FestivalConf> tempcfg = new HashMap<Integer, FestivalConf>();

	public PGetFestivalRewardProc(final long roleid, final int rewardid) {
		super();
		this.roleid = roleid;
		this.rewardid = rewardid;
		tempcfg.clear();
	}

	public boolean process() {
		FestivalConf fc = null;
		if(fire.pb.fushi.Module.GetPayServiceType() == 0) {
			fc = ConfigManager.getInstance().getConf(SFestivalConf.class).get(rewardid);
			if (fc == null) {
				logger.error("角色["+roleid+"]未找到节日奖励id=" + rewardid + "的配置!");
				return false;
			}
			NavigableMap<Integer, SFestivalConf> confs = ConfigManager.getInstance().getConf(SFestivalConf.class);
			for (SFestivalConf conf : confs.values()) {
				if (conf.getId() / 100 == fc.id / 100) {
					tempcfg.put(conf.getId(), conf);
				}
			}
		} else {
			fc = ConfigManager.getInstance().getConf(SFestivalConf4D.class).get(rewardid);
			if (fc == null) {
				logger.error("角色["+roleid+"]未找到节日奖励id=" + rewardid + "的配置!");
				return false;
			}
			NavigableMap<Integer, SFestivalConf4D> confs = ConfigManager.getInstance().getConf(SFestivalConf4D.class);
			for (SFestivalConf4D conf : confs.values()) {
				if (conf.getId() / 100 == fc.id / 100) {
					tempcfg.put(conf.getId(), conf);
				}
			}
		}	
		//检查配置时间
		long now = System.currentTimeMillis();
		if(!checkDate(now, fc)) {
			logger.error("角色["+roleid+"]节日奖励id=" + rewardid + "的当前时间不符合配置时间!");
			return false;
		}
		xbean.FestivalData rfd = xtable.Rolesfestivaldata.get(roleid);
		boolean isCanGet = false;
		if(rfd == null) {
			rfd = xbean.Pod.newFestivalData();
			rfd.getRewardmap().put(new Integer(rewardid), new Long(now));
			xtable.Rolesfestivaldata.insert(new Long(roleid), rfd);
			isCanGet = true;
		} else {
			if(rfd.getRewardmap().containsKey(rewardid)){
				logger.error("角色["+roleid+"]节日奖励id=" + rewardid + "已经领取过该奖励了!");
			} else {
				rfd.getRewardmap().put(new Integer(rewardid), new Long(now));
				isCanGet = true;
			}
		}
		if(isCanGet) {
			//策划嫌麻烦不想配奖励表,只能我特么手动发奖励
			dealReward(roleid, fc.getItem1(), fc.getItem1num(), fc.getItem1bind());
			dealReward(roleid, fc.getItem2(), fc.getItem2num(), fc.getItem2bind());
			dealReward(roleid, fc.getItem3(), fc.getItem3num(), fc.getItem3bind());
		}
		
		SQueryFestivalData snd = new SQueryFestivalData();
		for(Map.Entry<Integer, Long> et : rfd.getRewardmap().entrySet()) {
			snd.rewards.add(et.getKey());
		}
		psendWhileCommit(roleid, snd);
		return true;
	}
	
	private boolean checkDate(long now, FestivalConf fc) {
		long choosetime = DateValidate.parseDate(fc.getDate());
		boolean isInFestival = false;
		int fid = 0;
		long realtime = 0L; 
		for(Map.Entry<Integer, FestivalConf> e : tempcfg.entrySet()) {
			long date = DateValidate.parseDate(e.getValue().getDate());
			final Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			long endtime = cal.getTimeInMillis();
			if(now >= date && now <= endtime){
				isInFestival = true;
				fid = e.getKey();
				realtime = date;
				break;
			}
		}
		if(isInFestival) {
			if(fc.getId() / 100 == fid /100 && (fc.getId() <= fid || choosetime <= realtime))
				return true;
		}
		return false;
	}
	
	private void dealReward(long roleid, int itemid, int itemnum, int itembind) {
		if(itemid <= 0)
			return;
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
		if(bag.doAddItem(itemid, itemnum, 0, itembind > 0 ? fire.pb.Item.BIND : 0, "节日奖励", 
				YYLoggerTuJingEnum.tujing_Value_huodongjiangli, rewardid) != itemnum){
			MessageMgr.psendMsgNotifyWhileCommit(roleid, 144875, null);
			logger.warn("角色[" + roleid + "]获得节日奖励:" + rewardid + ",但背包添加物品:" + itemid + "*" + itemnum + ",bind=" + itembind + "失败！");
		} else {
			fire.pb.item.ItemShuXing ia = fire.pb.item.Module.getInstance().getItemManager().getAttr(itemid);
			if (ia != null) {
				ArrayList<String> parameters = new ArrayList<String>();
				parameters.add(String.valueOf(1));
				parameters.add(ia.unit);
				parameters.add(ia.name);
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 141037, parameters);
			}	
			logger.info("角色[" + roleid + "]获得节日奖励:" + rewardid + ",给予物品:" + itemid + "*" + itemnum + ",bind=" + itembind);
		}
	}
}
