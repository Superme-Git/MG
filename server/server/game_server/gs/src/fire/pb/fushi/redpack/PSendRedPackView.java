package fire.pb.fushi.redpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PSendRedPackView extends Procedure {
	public static final Logger logger = Logger.getLogger("REDPACK");
	private final long roleid;
	private final int modeltype;
	private final String redpackid;

	public PSendRedPackView(long roleid,int modeltype,String redpackid) {
		this.roleid = roleid;
		this.modeltype = modeltype;
		this.redpackid = redpackid;
	}
	
	
	public static final int MAX_NUM=100;//每次请求发送的最大数量
	
	
	public static ConcurrentHashMap<String, RedPackInfoBean> recordsmem=new ConcurrentHashMap<String, RedPackInfoBean>();//红包信息缓存
	public static ConcurrentHashMap<Long, ArrayList<String>> recordsrolegetinfo=new ConcurrentHashMap<Long, ArrayList<String>>();//玩家领取过的红包信息缓存
	

	@Override
	protected boolean process() throws Exception {
		Team team = TeamManager.selectTeamByRoleId(roleid);
		SSendRedPackView sSendRedPackView=new SSendRedPackView();
		sSendRedPackView.modeltype=modeltype;
		
		xbean.RedPackInfoViewList redPackInfoList=xtable.Redpackinfoviewlisttab.select(1);
		if(redPackInfoList==null){
			sendNoSRRedPackNumList(sSendRedPackView);
			return true;
		}
		List<xbean.RedPackBaseInfo> templist=new ArrayList<xbean.RedPackBaseInfo>();//处理世界红包
		List<RedPackInfoBean> tempclanteamlist=new ArrayList<RedPackInfoBean>();//处理公会和队伍红包
		if(modeltype==RedPackType.TYPE_WORLD){
			for(xbean.RedPackBaseInfo redPackBaseInfo:redPackInfoList.getRedpackinfoviewlist().values()){
				if(redPackBaseInfo==null){
					continue;
				}
				//过滤掉过期的红包
				if(System.currentTimeMillis()-redPackBaseInfo.getSendtime()>=RedPackManage.getValidRedPackTime()){
					continue;
				}
				String idinfo[] = redPackBaseInfo.getRedpackid().split("_");
				if(Integer.parseInt(idinfo[0])!=modeltype){
					continue;
				}			
				templist.add(redPackBaseInfo);
			}
		}else if(modeltype==RedPackType.TYPE_CLAN){
			//判断有没有工会
			Long clankey=xtable.Properties.selectClankey(roleid);
			if (clankey == null || clankey<=0) {
				sendNoSRRedPackNumList(sSendRedPackView);
				return true;
			}
			dealTeamOrClanRed(tempclanteamlist,redPackInfoList,clankey);
		}else if(modeltype==RedPackType.TYPE_TEAM){
			//判断
			if(team==null){
				sendNoSRRedPackNumList(sSendRedPackView);
				return true;
			}
			dealTeamOrClanRed(tempclanteamlist,redPackInfoList,team.getTeamId());
		}
		
		if(modeltype==RedPackType.TYPE_WORLD){
			if(templist.size()==0){
				sendNoSRRedPackNumList(sSendRedPackView);
				return true;
			}
			//根据时间排序
			Collections.sort(templist,new RedPackStateTimeComparator());

			int min=Math.min(MAX_NUM, templist.size());
			for(int i=0;i<min;i++){
				xbean.RedPackBaseInfo xbeaninfo=templist.get(i);
				if(xbeaninfo==null){
					continue;
				}
				RedPackInfo info=createRedPackInfo(xbeaninfo);
				if(info==null){
					continue;
				}
				sSendRedPackView.redpackinfolist.add(info);
			}
		}else{
			if(tempclanteamlist.size()==0){
				sendNoSRRedPackNumList(sSendRedPackView);
				return true;
			}
			//根据时间排序
			Collections.sort(tempclanteamlist,new RedPackStateTimeComparator2());

			int min=Math.min(MAX_NUM, tempclanteamlist.size());
			for(int i=0;i<min;i++){
				RedPackInfoBean xbeaninfo=tempclanteamlist.get(i);
				if(xbeaninfo==null){
					continue;
				}
				RedPackInfo info=createRedPackInfo2(xbeaninfo);
				if(info==null){
					continue;
				}
				sSendRedPackView.redpackinfolist.add(info);
			}
		}
		sSendRedPackView.firstpageflag=0;

		//设置收发数量
		xbean.SRRedPackNumList sRRedPackNumList=xtable.Srredpacknumlisttab.select(roleid);
		if(sRRedPackNumList!=null){
			xbean.SRRedPackNum entry=sRRedPackNumList.getSrredpacknumlist().get(modeltype);
			if(entry!=null){
				SRRedPackNum sRRedPackNum=new SRRedPackNum();
				sRRedPackNum.modeltype=entry.getModeltype();
				sRRedPackNum.redpacksendnum=entry.getRedpacksendnum();
				sRRedPackNum.redpackreceivenum=entry.getRedpackreceivenum();
				sRRedPackNum.redpackreceivefushinum=entry.getRedpacksendfushinum();
				sSendRedPackView.daysrnum=sRRedPackNum;
			}
		}
		psendWhileCommit(roleid, sSendRedPackView);
		return true;
	}
	
	
	/**
	 * 处理组队和公会的红包删选逻辑
	 * @param redPackInfoList
	 * @param keyvalue
	 */
	public void dealTeamOrClanRed(List<RedPackInfoBean> tempclanteamlist,xbean.RedPackInfoViewList redPackInfoList,long keyvalue){
		for(xbean.RedPackBaseInfo redPackBaseInfo:redPackInfoList.getRedpackinfoviewlist().values()){
			if(redPackBaseInfo==null){
				continue;
			}
			//过滤掉过期的红包
			if(System.currentTimeMillis()-redPackBaseInfo.getSendtime()>=RedPackManage.getValidRedPackTime()){
				continue;
			}
			String idinfo[] = redPackBaseInfo.getRedpackid().split("_");
			if(Integer.parseInt(idinfo[0])!=modeltype){
				continue;
			}
			//判断内存有没有
			RedPackInfoBean redPackInfo_cache = recordsmem.get(redPackBaseInfo.getRedpackid());
			if(redPackInfo_cache!=null){
				//这里需要过滤不是一个帮派或者队伍的
				if(redPackInfo_cache.getValuekey()!=keyvalue){
					continue;
				}
				//这个缓存是相对红包的，所有需要额外处理下这个玩家对于这个红包的领取状态
				if(redPackInfo_cache.getRedpackstate()!=RedPackState.STATE_NONE){
					String canget = RedPackManage.rolegetredpackrecords.get(redPackBaseInfo.getRedpackid());
					if(canget != null){
						redPackInfo_cache.setRedpackstate(RedPackState.STATE_NONE);//设置被抢光的状态，如果红包没有抢光，玩家显示的状态可能会有问题，为了效率，没有办法了
					}else{
						//判断玩家是否领取过了
						ArrayList<String> haveget=recordsrolegetinfo.get(roleid);
						if(haveget!=null&&haveget.contains(redPackBaseInfo.getRedpackid())){
							redPackInfo_cache.setRedpackstate(RedPackState.STATE_HAVE);//设置被抢光的状态，如果红包没有抢光，玩家显示的状态可能会有问题，为了效率，没有办法了
						}else{
							redPackInfo_cache.setRedpackstate(RedPackState.STATE_CANGET);
						}
					}
				}
			}else{
				xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.select(redPackBaseInfo.getRoleid());
				if(redPackRoleInfoList==null){
					continue;
				}
				xbean.RedPackInfo redpackinfo=redPackRoleInfoList.getRedpackinfolist().get(redPackBaseInfo.getRedpackid());
				if(redpackinfo==null){
					continue;
				}
				if(redpackinfo.getValuekey()!=keyvalue){
					continue;
				}
				String name=xtable.Properties.selectRolename(redpackinfo.getSendroleid());
				int fushi=0;
				if(RedPackManage.isSendGold(redpackinfo.getModeltype())){//是否是发送金币的
					fushi=redpackinfo.getRedpackallmoney();
				}else{
					fushi=redpackinfo.getRedpackallmoney()/RedPackManage.CHANGE_NUM;
				}
				redPackInfo_cache=new RedPackInfoBean(redpackinfo.getRedpackid(),redpackinfo.getSendroleid(),name,redpackinfo.getRedpackdes(),redpackinfo.getRedpackstate()
						,fushi,redpackinfo.getValuekey(),redpackinfo.getSendtime());
				recordsmem.put(redpackinfo.getRedpackid(), redPackInfo_cache);//添加到缓存
				
			}
			tempclanteamlist.add(redPackInfo_cache);
		}
	}
	
	
	
	
	
	/**
	 * 如果没有数据也发送信息
	 * @param sSendRedPackView
	 */
	public void sendNoSRRedPackNumList(SSendRedPackView sSendRedPackView){
		sSendRedPackView.firstpageflag=0;
		xbean.SRRedPackNumList sRRedPackNumList=xtable.Srredpacknumlisttab.select(roleid);
		if(sRRedPackNumList!=null){
			xbean.SRRedPackNum entry=sRRedPackNumList.getSrredpacknumlist().get(modeltype);
			if(entry!=null){
				SRRedPackNum sRRedPackNum=new SRRedPackNum();
				sRRedPackNum.modeltype=entry.getModeltype();
				sRRedPackNum.redpacksendnum=entry.getRedpacksendnum();
				sRRedPackNum.redpackreceivenum=entry.getRedpackreceivenum();
				sRRedPackNum.redpackreceivefushinum=entry.getRedpacksendfushinum();
				sSendRedPackView.daysrnum=sRRedPackNum;
			}
		}
		psendWhileCommit(roleid, sSendRedPackView);
	}
	
	

	public RedPackInfo createRedPackInfo(xbean.RedPackBaseInfo redPackBaseInfo){
		//判断缓存是否有这个信息
		RedPackInfo redPackInfo=new RedPackInfo();
		RedPackInfoBean redPackInfo_cache = recordsmem.get(redPackBaseInfo.getRedpackid());
		if(redPackInfo_cache!=null){
			//这个缓存是相对红包的，所有需要额外处理下这个玩家对于这个红包的领取状态
			if(redPackInfo_cache.getRedpackstate()!=RedPackState.STATE_NONE){
				String canget = RedPackManage.rolegetredpackrecords.get(redPackBaseInfo.getRedpackid());
				if(canget != null){
					redPackInfo_cache.setRedpackstate(RedPackState.STATE_NONE);//设置被抢光的状态，如果红包没有抢光，玩家显示的状态可能会有问题，为了效率，没有办法了
				}else{
					//判断玩家是否领取过了
					ArrayList<String> haveget=recordsrolegetinfo.get(roleid);
					if(haveget!=null&&haveget.contains(redPackBaseInfo.getRedpackid())){
						redPackInfo_cache.setRedpackstate(RedPackState.STATE_HAVE);//设置被抢光的状态，如果红包没有抢光，玩家显示的状态可能会有问题，为了效率，没有办法了
					}else{
						redPackInfo_cache.setRedpackstate(RedPackState.STATE_CANGET);
					}
				}
			}
			redPackInfo.redpackid = redPackInfo_cache.getRedpackid();
			redPackInfo.fushi=redPackInfo_cache.getFushi();
			redPackInfo.roleid = redPackInfo_cache.getRoleid();
			redPackInfo.rolename = redPackInfo_cache.getRolename();
			redPackInfo.redpackdes = redPackInfo_cache.getRedpackdes();
			redPackInfo.redpackstate = redPackInfo_cache.getRedpackstate();
		}else{
			xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.select(redPackBaseInfo.getRoleid());
			if(redPackRoleInfoList==null){
				return null;
			}
			xbean.RedPackInfo xbeaninfo=redPackRoleInfoList.getRedpackinfolist().get(redPackBaseInfo.getRedpackid());
			if(xbeaninfo==null){
				return null;
			}
			redPackInfo.redpackid = xbeaninfo.getRedpackid();
			if(RedPackManage.isSendGold(xbeaninfo.getModeltype())){//是否是发送金币的
				redPackInfo.fushi=xbeaninfo.getRedpackallmoney();
			}else{
				redPackInfo.fushi=xbeaninfo.getRedpackallmoney()/RedPackManage.CHANGE_NUM;
			}
			redPackInfo.roleid = xbeaninfo.getSendroleid();
			String prop=xtable.Properties.selectRolename(xbeaninfo.getSendroleid());
			if(prop!=null){
				redPackInfo.rolename = prop;
			}
			redPackInfo.redpackdes = xbeaninfo.getRedpackdes();
			
			//这里需要处理玩家是否领取过
			int flag=xbeaninfo.getRedpackstate();
			if(flag!=RedPackState.STATE_NONE){
				for(xbean.RedPackRoleHisInfo hisinfo:xbeaninfo.getRedpackrolehisinfolist()){
					if(hisinfo.getRoleid()==roleid){
						flag=RedPackState.STATE_HAVE;
						break;
					}
				}
			}
			redPackInfo.redpackstate = flag;
			
			redPackInfo_cache=new RedPackInfoBean(redPackInfo.redpackid,redPackInfo.roleid,redPackInfo.rolename,redPackInfo.redpackdes,redPackInfo.redpackstate
					,redPackInfo.fushi,xbeaninfo.getValuekey(),xbeaninfo.getSendtime());
			recordsmem.put(redPackInfo.redpackid, redPackInfo_cache);//添加到缓存
		}
		return redPackInfo;
		
	}
	
	public RedPackInfo createRedPackInfo2(RedPackInfoBean xbeaninfo){
		RedPackInfo redPackInfo=new RedPackInfo();
		redPackInfo.redpackid = xbeaninfo.getRedpackid();
		redPackInfo.fushi=xbeaninfo.getFushi();
		redPackInfo.roleid = xbeaninfo.getRoleid();
		redPackInfo.rolename = xbeaninfo.getRolename();
		redPackInfo.redpackdes = xbeaninfo.getRedpackdes();
		redPackInfo.redpackstate = xbeaninfo.getRedpackstate();
		return redPackInfo;
		
	}

}
