

package fire.pb.clan;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.clan.srv.ClanAuthManager;
import fire.pb.clan.srv.ClanManage;
import fire.pb.talk.MessageMgr;



public class PRequestImpeachMent extends Procedure {
	public static final Logger logger = Logger.getLogger("CLAN");
	private final long roleid;
	private final byte cmdtype;
	
	public static final int IMPEACHMENT_TIME =60*60*1000;//弹劾有效期 1小时
	public static final int IMPEACHMENT_SEVEN_TIME =7*24*60*60*1000;//7天内上过线的玩家
	public static final int IMPEACHMENT_TEN_TIME =10*24*60*60*1000;//10天内上过线的玩家
	public static final double IMPEACHMENT_NUM_1 =0.5;//50%*7天内登陆过的玩家人数
	public static final double IMPEACHMENT_NUM_2 =0.3;//人数不能低于公会总人数的30%
	
	public static final int DAY_IMPEACHMENT_MAX_NUM=1;//每天弹劾最大次数
	
	public static final byte STATE_REQUEST = 0;//弹劾状态  0发起弹劾    1响应弹劾
	public static final byte STATE_ACCEPT = 1;//弹劾状态  0发起弹劾    1响应弹劾
	
	
	public static ConcurrentHashMap<Long,Long> impeachmentclans = new ConcurrentHashMap<Long, Long>();//有发起弹劾的公会   key=公会id vlaue=发起人id
	
	
	public PRequestImpeachMent(long roleid, byte cmdtype) {
		this.roleid = roleid;
		this.cmdtype = cmdtype;
	}
	@Override
	protected boolean process() throws Exception {
		// 0请求弹劾界面   1发起弹劾   2响应弹劾
		if(cmdtype<0||cmdtype>2){
			return false;
		}
		if(cmdtype==0){
			return sendRequestImpeachMentView();
		}else if(cmdtype==1){//1发起弹劾
			return sendRequestImpeachMent();
		}else{
			return sendAcceptImpeachMent();
		}
	}
	
	/**
	 * 发起弹劾
	 * @return
	 */
	public boolean sendRequestImpeachMent(){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		String mastername=xtable.Properties.selectRolename(clanInfo.getClanmaster());
		String membername=xtable.Properties.selectRolename(roleid);
		if(mastername==null||membername==null){
			return false;
		}
		//判断玩家是否可以发起弹劾
		xbean.ClanMemberInfo memberInfo = clanInfo.getMembers().get(roleid);
		if(memberInfo==null){
			MessageMgr.psendMsgNotify(roleid, 160311, null);//提示目标玩家不是公会成员
			return false;
		}
		int memberPosition = memberInfo.getClanposition();
		//1.是否为会长
		if(memberPosition==ClanPositionType.ClanMaster){
			MessageMgr.psendMsgNotify(roleid, 172042, null);
			return false;
		}
		//2.判断是否入会7天以上
		if(System.currentTimeMillis() - memberInfo.getJointime()<IMPEACHMENT_SEVEN_TIME){
			MessageMgr.psendMsgNotify(roleid, 172043, null);
			return false;
		}
		//3.判断玩家今天是否发起过弹劾
		xbean.BuyMedicItemNum buyMedicItemNum=xtable.Buymedicitemnum.get(roleid);
		if(buyMedicItemNum==null){
			buyMedicItemNum=xbean.Pod.newBuyMedicItemNum();
			xtable.Buymedicitemnum.insert(roleid, buyMedicItemNum);
		}
		if(buyMedicItemNum.getImpeachdaynum()>=DAY_IMPEACHMENT_MAX_NUM){
			MessageMgr.psendMsgNotify(roleid, 172044, null);
			return false;
		}
		//判断当前是否有弹劾
		xbean.ImpeachMent impeachMent = clanInfo.getImpeachment();
		if(impeachMent==null){
			impeachMent = xbean.Pod.newImpeachMent();
		}
		if(impeachMent.getImpeachroleid()>0 && System.currentTimeMillis()-impeachMent.getImpeachtime()<IMPEACHMENT_TIME){
			//提示玩家，当前无法弹劾
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172044, null);
			return false;
		}else{
			//计算弹劾需要的响应人数(响应人数50%向下取整)
			int num=0;
			int allnum=clanInfo.getMembers().size();
			long curtime=System.currentTimeMillis();
			for(Map.Entry<Long, xbean.ClanMemberInfo> entry : clanInfo.getMembers().entrySet()){
				long roleId = entry.getKey();
				Long lasttime=xtable.Properties.selectOnlinetime(roleId);
				if(lasttime!=null){
					if(curtime-lasttime<IMPEACHMENT_SEVEN_TIME){
						num++;
					}
				}
			}
			short mustnum=(short) (num*IMPEACHMENT_NUM_1);
			short num2=(short) (allnum*IMPEACHMENT_NUM_2);
			if(mustnum<num2){
				//提示玩家，当前无法弹劾，人数不足
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172041, null);
				return false;
			}
			//添加弹劾次数
			buyMedicItemNum.setImpeachdaynum(buyMedicItemNum.getImpeachdaynum()+1);
			impeachMent.getAcceptimpeachroleids().clear();
			impeachMent.setImpeachroleid(roleid);
			impeachMent.setImpeachallnum(mustnum);
			impeachMent.setImpeachtime(System.currentTimeMillis());
			impeachmentclans.put(clanInfo.getKey(), impeachMent.getImpeachtime());
			//当前有人弹劾，返回响应弹劾界面
			sendAcceptData(impeachMent);
			//发送公会频道消息
			//<T t="$parameter1$" c="ff00ff00"/><T t="对会长" c="ff261407"/><T t="$parameter2$" c="ff00ff00"/><T t="发起了弹劾。" c="ff261407"/>
			MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 172045, 0, Arrays.asList(membername,mastername));
			//处理帮派事件
			ClanManage.addDealClanEventInfo(clanInfo,ClanManage.EVENT_IMPEACH_START,roleid,membername,"","");
			//更新弹劾,显示发起弹劾界面
			Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), sendSRequestImpeachMentView(impeachMent,STATE_ACCEPT));
			logger.info("roleid\t"+roleid+"\tclankey\t"+clanInfo.getKey()+"\t发起弹劾，需要总人数\t"+mustnum+"\t公会总人数"+allnum);
		}
		return true;
	}
	
	/**
	 * 响应弹劾
	 * @return
	 */
	public boolean sendAcceptImpeachMent(){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		//判断当前是否有弹劾
		xbean.ImpeachMent impeachMent = clanInfo.getImpeachment();
		if(impeachMent==null){
			impeachMent = xbean.Pod.newImpeachMent();
		}
		if(impeachMent.getImpeachroleid()>0 && System.currentTimeMillis()-impeachMent.getImpeachtime()<IMPEACHMENT_TIME){
			//可以响应弹劾
			//1该弹劾是你自己发起的，无需响应
			if(impeachMent.getImpeachroleid()==roleid){
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172046, null);
				return false;
			}
			//2您已经响应过弹劾，无需重复响应
			if(impeachMent.getAcceptimpeachroleids().contains(roleid)){
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172047, null);
				return false;
			}
			String membername=xtable.Properties.selectRolename(roleid);
			//添加弹劾的人
			impeachMent.getAcceptimpeachroleids().add(roleid);
			int curnum=impeachMent.getAcceptimpeachroleids().size();
			//当前有人弹劾，返回响应弹劾界面
			sendAcceptData(impeachMent);
			//发送公会消息172048
			//<T t="$parameter1$" c="ff00ff00"/><T t="响应弹劾，目前弹劾人数" c="ff261407"/><T t="$parameter2$" c="ff00ff00"/><T t="/" c="ff261407"/><T t="$parameter3$" c="ff00ff00"/><T t="人。" c="ff261407"/>
			MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 172048, 0, Arrays.asList(membername,curnum+"",impeachMent.getImpeachallnum()+""));
			logger.info("roleid\t"+roleid+"\tclankey\t"+clanInfo.getKey()+"\t响应了弹劾");
			//判断是否弹劾成功
			if(curnum>=impeachMent.getImpeachallnum()){
				//处理弹劾成功逻辑
				impeachmentclans.remove(clanInfo.getKey());//从内存中移除
				// 现任会长任命下任会长
				long oldmasterid=clanInfo.getClanmaster();
				long impeachroleid=impeachMent.getImpeachroleid();
				//判断发起的人是否已经成为会长
				if(clanInfo.getClanmaster()==impeachMent.getImpeachroleid()){
					//清理弹劾逻辑
					clearImpeachroleInfo(impeachMent);
					logger.info("roleid\t"+impeachroleid+"\tclankey\t"+clanInfo.getKey()+"\t弹劾成功"+"原来会长\t"+oldmasterid);
					MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 172056, 0,Arrays.asList(membername));//发送弹劾成功的消息
					//更新弹劾,显示发起弹劾界面
					Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), sendSRequestImpeachMentView(impeachMent,STATE_REQUEST));
					return true;
				}
				xbean.ClanMemberInfo masterInfo = clanInfo.getMembers().get(oldmasterid);
				xbean.ClanMemberInfo memberInfo = clanInfo.getMembers().get(impeachMent.getImpeachroleid());
				clanInfo.setClanmaster(impeachMent.getImpeachroleid());
				masterInfo.setClanposition(ClanPositionType.ClanMember);
				// 称谓变更为成员
				ClanUtils.changeClanTitleByPosition(oldmasterid, ClanPositionType.ClanMaster,ClanPositionType.ClanMember);
				Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), new SRefreshPosition(oldmasterid, ClanPositionType.ClanMember));
				//这里需要处理原来的职位是不是副会长，如果是需要把副会长标志设置为空
				int oldposition=memberInfo.getClanposition();
				if(oldposition==ClanPositionType.ClanViceMaster){
					clanInfo.setClanvicemaster(0);
				}
				//职位变化后的一些逻辑处理
				String membername2=xtable.Properties.selectRolename(impeachMent.getImpeachroleid());
				ClanAuthManager.getInstance().changeImpeachMentPosition(oldmasterid,impeachMent.getImpeachroleid(),ClanPositionType.ClanMaster,oldposition,memberInfo,membername2,clanInfo);
				//清理弹劾逻辑
				clearImpeachroleInfo(impeachMent);
				logger.info("roleid\t"+impeachroleid+"\tclankey\t"+clanInfo.getKey()+"\t弹劾成功"+"原来会长\t"+oldmasterid);
				//更新弹劾,显示发起弹劾界面
				Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), sendSRequestImpeachMentView(impeachMent,STATE_REQUEST));
				return true;
			}
			//更新弹劾,显示发起弹劾界面
			Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), sendSRequestImpeachMentView(impeachMent,STATE_ACCEPT));
		}else{
			//没有人发起弹劾172058
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 172058, null);
			return false;
		}
		return true;
	}
	
	public static void clearImpeachroleInfo(xbean.ImpeachMent impeachMent){
		impeachMent.setImpeachroleid(0);
		impeachMent.setImpeachallnum((short) 0);
		impeachMent.setImpeachtime(0);
		impeachMent.getAcceptimpeachroleids().clear();
	}
	
	/**
	 * 处理弹劾超时逻辑
	 * @param clankey
	 */
	public static boolean dealImpeachTimeout(long clankey){
		xbean.ClanInfo clanInfo = xtable.Clans.get(clankey);
		if (clanInfo == null) {
			return false;
		}
		//判断当前是否有弹劾
		xbean.ImpeachMent impeachMent = clanInfo.getImpeachment();
		if(impeachMent==null){
			return false;
		}
		long roleId=impeachMent.getImpeachroleid();
		//发送公告
		MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 172057, 0,null);
		clearImpeachroleInfo(impeachMent);
		logger.info("roleid\t"+roleId+"\tclankey\t"+clanInfo.getKey()+"\t弹劾超时失败");
		//更新弹劾,显示发起弹劾界面
		Procedure.psendWhileCommit(clanInfo.getMembers().keySet(), sendSRequestImpeachMentView(impeachMent,STATE_REQUEST));
		return true;
	}
	
	
	/**
	 * 发送请求弹劾界面
	 * @return
	 */
	public boolean sendRequestImpeachMentView(){
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		//判断当前是否有弹劾
		xbean.ImpeachMent impeachMent = clanInfo.getImpeachment();
		if(impeachMent==null){
			impeachMent = xbean.Pod.newImpeachMent();
		}
		if(impeachMent.getImpeachroleid()>0 && System.currentTimeMillis()-impeachMent.getImpeachtime()<IMPEACHMENT_TIME){
			//当前有人弹劾，返回响应弹劾界面
			sendAcceptData(impeachMent);
		}else{
			//发起弹劾界面
			SRequestImpeachMentView sRequestImpeachMentView = new SRequestImpeachMentView();
			sRequestImpeachMentView.impeachstate=STATE_REQUEST;// 弹劾状态  0发起弹劾    1响应弹劾
			//计算弹劾需要的响应人数(响应人数50%向下取整)
			int num=0;
			long curtime=System.currentTimeMillis();
			for(Map.Entry<Long, xbean.ClanMemberInfo> entry : clanInfo.getMembers().entrySet()){
				long roleId = entry.getKey();
				Long lasttime=xtable.Properties.selectOnlinetime(roleId);
				if(lasttime!=null){
					if(curtime-lasttime<IMPEACHMENT_SEVEN_TIME){
						num++;
					}
				}
			}
			short mustnum=(short) (num*IMPEACHMENT_NUM_1);
			sRequestImpeachMentView.maxnum=mustnum; // 弹劾成功需要的人数
			Procedure.psendWhileCommit(roleid, sRequestImpeachMentView);
		}
		
		return true;
	}
	
	/**
	 * 发送界面数据(响应弹劾)
	 * @param impeachMent
	 */
	public void sendAcceptData(xbean.ImpeachMent impeachMent){
		Procedure.psendWhileCommit(roleid, sendSRequestImpeachMentView(impeachMent,STATE_ACCEPT));
	}
	
	public static SRequestImpeachMentView sendSRequestImpeachMentView(xbean.ImpeachMent impeachMent,byte state){
		SRequestImpeachMentView sRequestImpeachMentView = new SRequestImpeachMentView();
		sRequestImpeachMentView.impeachstate=state;// 弹劾状态  0发起弹劾    1响应弹劾
		sRequestImpeachMentView.maxnum=impeachMent.getImpeachallnum(); // 弹劾成功需要的人数
		String name=xtable.Properties.selectRolename(impeachMent.getImpeachroleid());
		if(name==null){
			name="";
		}
		sRequestImpeachMentView.impeachname=name; // 发起弹劾人的名称
		sRequestImpeachMentView.impeachtime=impeachMent.getImpeachtime(); // 发起弹劾时间
		sRequestImpeachMentView.curnum=(short) impeachMent.getAcceptimpeachroleids().size(); // 当前响应人数
		return sRequestImpeachMentView;
	}
	
}

