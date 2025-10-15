
package fire.pb.compensation;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mkdb.Procedure;
import fire.pb.activity.award.RewardMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class PCheckSendCompensationMsg extends Procedure {

	private final long roleid;

	private final long time;

	public PCheckSendCompensationMsg(long roleid, long time) {

		super();
		this.roleid = roleid;
		this.time = time;
	}

	@Override
	protected boolean process() throws Exception {

		boolean ret = false;
		fire.pb.item.SMailList sendMailList = new fire.pb.item.SMailList();

		long curr = System.currentTimeMillis();

		// 获得指定时间的补偿
		List<CompensationConfig> configs = CompensationManager.getInstance().getCompensationConfigsInTime(roleid,time);
		if (!configs.isEmpty()) {

			// 获取当前角色补偿信息
			xbean.Compensation compensation = xtable.Compensation.get(roleid);
			if (compensation == null) {
				compensation = xbean.Pod.newCompensation();
				xtable.Compensation.insert(roleid, compensation);
			}
			// 遍历补偿配置
			for (CompensationConfig config : configs) {
				int compensationid = config.getCompensationid();
				xbean.CompensationInfo compensationInfo = compensation.getCompensationmap().get(compensationid);
				if (compensationInfo == null) {
					compensationInfo = xbean.Pod.newCompensationInfo();
					compensation.getCompensationmap().put(compensationid, compensationInfo);
				}
				
				// 检查时间并查看是否已领取
				if (config.isInCompensationTime(curr)&&!CompensationManager.alreadyReceiveAward(compensation, config)) {
					//compensationInfo.setSendmsgtime(System.currentTimeMillis());
					//Message.psendSystemMessageToRole(roleid, config.getCompensationMsg(), null);

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String strTime = dateFormat.format(config.getStartTime());

					fire.pb.item.MailInfo mailInfo = new fire.pb.item.MailInfo();
					mailInfo.kind = 0;
					mailInfo.id = compensationid;
					mailInfo.readflag = (byte)compensationInfo.getReadflag();
					mailInfo.time = strTime;
					mailInfo.title = config.getServiceMsg();
					mailInfo.contentid = config.getCompensationMsg();
					mailInfo.awardid = config.getItemid();
					dealAwardNumType(mailInfo,config.getItemid());//处理数值型奖励
					mailInfo.npcid = 0;
					sendMailList.maillist.add(mailInfo);

					ret =  true;
				}
			}
		}

		// 获得角色的补偿
		List<SpecialCompensationConfig> sccs = CompensationManager.getInstance().getCompensationConfigsInTimeAndAvailable(time, roleid);
		if (!sccs.isEmpty()){

			// 获取当前角色补偿信息
			xbean.Compensation compensation = xtable.Compensation.get(roleid);
			if (compensation == null) {
				compensation = xbean.Pod.newCompensation();
				xtable.Compensation.insert(roleid, compensation);
			}
			// 遍历补偿配置
			for (SpecialCompensationConfig scc : sccs) {
				int serviceid = scc.getId();
				xbean.CompensationInfo compensationInfo = compensation.getCompensationmap().get(serviceid);
				if (compensationInfo == null) {
					compensationInfo = xbean.Pod.newCompensationInfo();
					compensation.getCompensationmap().put(serviceid, compensationInfo);
				}
				// 检查时间并查看是否已领取
				if (scc.isInTime(curr)&&!CompensationManager.alreadyReceiveAward(compensation, scc)) {
					//compensationInfo.setSendmsgtime(System.currentTimeMillis());
					//Message.psendSystemMessageToRole(roleid, scc.getCompensationMsgid(), null);

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String strTime = dateFormat.format(scc.getStartTime());

					fire.pb.item.MailInfo mailInfo = new fire.pb.item.MailInfo();
					mailInfo.kind = 0;
					mailInfo.id = serviceid;
					mailInfo.readflag = (byte)compensationInfo.getReadflag();
					mailInfo.time = strTime;
					mailInfo.title = scc.getServiceName();
					mailInfo.contentid = scc.getCompensationMsgid();
					mailInfo.awardid = scc.getAwardid();
					dealAwardNumType(mailInfo,scc.getAwardid());//处理数值型奖励
					mailInfo.npcid = 0;
					sendMailList.maillist.add(mailInfo);

					ret =  true;
				}
			}
		}

		// 获得UserId的补偿
		String useridStr = String.valueOf(xtable.Properties.selectUserid(roleid));
		List<SpecialCompensationConfig> sccs2 = CompensationManager.getInstance().getUseridCompensationConfigsInTimeAndAvailable(roleid,time,useridStr);
		if (!sccs2.isEmpty()){

			// 获取当前角色补偿信息
			xbean.Compensation compensation = xtable.Usercompensation.get(useridStr);
			if (compensation == null) {
				compensation = xbean.Pod.newCompensation();
				xtable.Usercompensation.insert(useridStr, compensation);
			}
			// 遍历补偿配置
			for (SpecialCompensationConfig scc : sccs2) {
				int serviceid = scc.getId();
				xbean.CompensationInfo compensationInfo = compensation.getCompensationmap().get(serviceid);
				if (compensationInfo == null) {
					compensationInfo = xbean.Pod.newCompensationInfo();
					compensation.getCompensationmap().put(serviceid, compensationInfo);
				}
				// 检查时间并查看是否已领取
				if (scc.isInTime(curr)&&!CompensationManager.alreadyReceiveAward(compensation, scc)) {
					//compensationInfo.setSendmsgtime(System.currentTimeMillis());
					//Message.psendSystemMessageToRole(roleid, scc.getCompensationMsgid(), null);

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String strTime = dateFormat.format(scc.getStartTime());

					fire.pb.item.MailInfo mailInfo = new fire.pb.item.MailInfo();
					mailInfo.kind = 0;
					mailInfo.id = serviceid;
					mailInfo.readflag = (byte)compensationInfo.getReadflag();
					mailInfo.time = strTime;
					mailInfo.title = scc.getServiceName();
					mailInfo.contentid = scc.getCompensationMsgid();
					mailInfo.awardid = scc.getAwardid();
					
					dealAwardNumType(mailInfo,scc.getAwardid());//处理数值型奖励
					
					mailInfo.npcid = 0;
					sendMailList.maillist.add(mailInfo);

					ret =  true;
				}
			}
		}

		// GM补偿
		CompensationRole compensRole = CompensationRole.getSingleCompensation(roleid, true);
		if (compensRole != null && !compensRole.getSinglecompensations().isEmpty()) {

			for (java.util.Map.Entry<Long, Long> e : compensRole.getSinglecompensations().entrySet()) {

				long compensKey = e.getKey();
				fire.pb.item.MailInfo mailInfo = CompensationToMail.SingleCompensation2Mail(compensRole, compensKey);
				if (mailInfo == null) {
					continue;
				}

				sendMailList.maillist.add(mailInfo);

				ret =  true;
			}
		}

		if (ret) {
			gnet.link.Onlines.getInstance().send(roleid, sendMailList);
		}
		return ret;
	}
	
	/**
	 * 处理数值型奖励
	 * @param mailInfo
	 * @param awardid
	 */
	public void dealAwardNumType(fire.pb.item.MailInfo mailInfo,int awardid){
		Map<String, Object> paras = new HashMap<String, Object>();
		Team team=TeamManager.selectTeamByRoleId(roleid);
		if(team==null){
			paras.put(RewardMgr.TEAM_NUM,  1);//添加队伍人数信息
		}else{
			paras.put(RewardMgr.TEAM_NUM,  team.getNormalMemberIds().size());//添加队伍人数信息
		}

		//经验
		long awardexp=RewardMgr.getInstance().getAwardExpdistributeAllAward(roleid, awardid, paras,true);
		if(awardexp>0){
			mailInfo.awardexp=awardexp;
		}
		//银币
		long awardmoney=RewardMgr.getInstance().getAwardMoneydistributeAllAward(roleid, awardid, paras,true);
		if(awardmoney>0){
			mailInfo.awardmoney=awardmoney;
		}
		//金币
		long awardgold = RewardMgr.getInstance().getAwardGolddistributeAllAward(roleid, awardid, paras,true);
		if(awardgold>0){
			mailInfo.awardgold=awardgold;
		}
		//奖励配置表没有符石奖励，所以这里不处理awardfushi; // 符石
	
	}
	
	

}
