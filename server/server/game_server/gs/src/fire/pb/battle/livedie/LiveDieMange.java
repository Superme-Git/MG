package fire.pb.battle.livedie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import fire.pb.PropRole;
import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.util.DateValidate;

public class LiveDieMange {
	public static final Logger logger = Logger.getLogger("BATTLE");
	private static LiveDieMange instance=null;
	public static LiveDieMange getInstance(){
		if(instance==null){
			instance=new LiveDieMange();
		}
		return instance;
	}
	public  LiveDieMange(){
		
	}
	
	public static final int GIVE_ROSE_NUM_MAX=1;//点赞总次数
	
	
	public static ConcurrentHashMap<Long, Long> liveDieRoleids= new ConcurrentHashMap<Long, Long>();//参加生死战的玩家(这个在接受应战的时候记录，主要为了退还押金)
	public static ConcurrentHashMap<Long, Long> liveDieFightRoleids= new ConcurrentHashMap<Long, Long>();//参加生死战的玩家(这个在开始战斗的时候记录，主要是为了观战界面用)
	
	public static ConcurrentHashMap<String, xbean.LDVideoRoleInfoDes> ldvideoroleinfodeslistall= new ConcurrentHashMap<String, xbean.LDVideoRoleInfoDes>();//参加生死战的玩家所有玩家录像
	
	/**
	 * 启动的时候初始化数据库
	 * @param lDVideoRoleInfoDesList
	 */
	public static void initdb(xbean.LDVideoRoleInfoDesList lDVideoRoleInfoDesList){
		java.util.List<xbean.LDVideoRoleInfoDes> deslist=lDVideoRoleInfoDesList.getLdvideoroleinfodeslistall();
		if(deslist!=null){
			for(xbean.LDVideoRoleInfoDes des:deslist){
				LiveDieMange.ldvideoroleinfodeslistall.put(des.getVideoid(), des);
			}
		}
	}
	
	
	public static List<xbean.LDVideoRoleInfoDes> getLDVideoRoleInfoDesByType(int modeltype,long roleid){
		long time=System.currentTimeMillis();
		List<xbean.LDVideoRoleInfoDes> list=new ArrayList<xbean.LDVideoRoleInfoDes>();
		if(modeltype==LDmodelType.DAY_FIGHT){
			for(xbean.LDVideoRoleInfoDes des:ldvideoroleinfodeslistall.values()){
//				if(des!=null&&des.getSaveresult()==1){
				if(des!=null){//这里不过滤不能看的录像，回头在点击观看录像的地方提示无法播放录像
					if(DateValidate.inTheSameDay(des.getFighttime(), time)){
						list.add(des);
					}
				}
			}
		}else if(modeltype==LDmodelType.WEEK_FIGHT){
			for(xbean.LDVideoRoleInfoDes des:ldvideoroleinfodeslistall.values()){
//				if(des!=null&&des.getSaveresult()==1){
				if(des!=null){
					if(DateValidate.inTheSameWeek(des.getFighttime(), time)){
						list.add(des);
					}
				}
			}
		}else if(modeltype==LDmodelType.HIS_FIGHT){
			for(xbean.LDVideoRoleInfoDes des:ldvideoroleinfodeslistall.values()){
//				if(des!=null&&des.getSaveresult()==1){
				if(des!=null){
					list.add(des);
				}
			}
		}else if(modeltype==LDmodelType.SELF_FIGHT){
			for(xbean.LDVideoRoleInfoDes des:ldvideoroleinfodeslistall.values()){
//				if(des!=null&&des.getSaveresult()==1){
				if(des!=null){
					String ss[]=des.getVideoid().split("_");
					long roid1=Long.parseLong(ss[2]);
					long roid2=Long.parseLong(ss[3]);
					if(roid1==roleid||roid2==roleid){
						list.add(des);
					}
				}
			}
		}
		if(modeltype==LDmodelType.DAY_FIGHT){
			//排序
			Collections.sort(list, new LiveDieTimeComparator());
		}else if(modeltype==LDmodelType.WEEK_FIGHT){
			//排序
			Collections.sort(list, new LiveDieComparator());
		}else if(modeltype==LDmodelType.HIS_FIGHT){
			//排序
			Collections.sort(list, new LiveDieComparator());
		}else{
			//排序
			Collections.sort(list, new LiveDieTimeComparator());
		}
		
		return list;
		
	}
	
	/**
	 * 获得下战书需要的银币
	 * @return
	 */
	public static int getLiveDieCostMoney(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(290);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+290);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 获得奖励的id
	 * @return
	 */
	public static int getLiveDieAwardId(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(303);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+303);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 应战倒计时
	 * @return
	 */
	public static int getLiveDieTime(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(293);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+293);
			return 0;
		}
		return Integer.parseInt(conf.getValue())*60*1000;
	}
	
	/**
	 * 等级需求
	 * @return
	 */
	public static int getLiveDieLevel(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(291);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+291);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 战斗死亡损失的耐久
	 * @return
	 */
	public static int getLiveDieLoseDurable(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(292);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+292);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 死亡后地图
	 * @return
	 */
	public static int getLiveDieMap(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(298);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+298);
			return 1650;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 生死战称谓
	 * @return
	 */
	public static int getLiveDieTitle(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(300);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+300);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	
	
	/**
	 * 移除玩家下战书信息
	 * @param roleId
	 */
	public static void removePlayerLiveDieInfoLoginout(final long roleId){
		mkdb.Procedure prop=new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				xbean.LiveDieRoleInfo hostliveDieRoleInfo=xtable.Livedieroleinfotab.select(roleId);
				if(hostliveDieRoleInfo!=null){
					long geustid=hostliveDieRoleInfo.getObjectid();
					//加锁
					List<Long> list = new ArrayList<Long>();
					list.add(roleId);
					list.add(geustid);
					String name=new PropRole(roleId,true).getName();
					fire.pb.talk.MessageMgr.sendMsgNotify(geustid, 162083, Arrays.asList(name));
					xtable.Livedie2key.remove(geustid);
					xtable.Livedieroleinfotab.remove(roleId);
				}
				return true;
			}
		};
		mkdb.Procedure.pexecute(prop);
	}
	
	/**
	 * 战斗结束移除信息
	 * @param roleId
	 */
	public static void removePlayerLiveDieInfoBattleEnd(final long roleId){
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				xbean.LiveDieRoleInfo hostliveDieRoleInfo=xtable.Livedieroleinfotab.select(roleId);
				if(hostliveDieRoleInfo!=null){
					xtable.Livedie2key.remove(hostliveDieRoleInfo.getObjectid());
					xtable.Livedieroleinfotab.remove(roleId);
				}
				return true;
			}
		}.call();
	}
	
	
	
	

}
