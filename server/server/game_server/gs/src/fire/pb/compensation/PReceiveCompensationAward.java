
package fire.pb.compensation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.PAddExpProc;
import fire.pb.PropRole;
import fire.pb.activity.award.RewardMgr;
import fire.pb.main.ConfigManager;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

public class PReceiveCompensationAward extends Procedure {

	private final long roleid;

	private final int serviceid;

	public PReceiveCompensationAward(long roleid, int serviceid) {

		super();
		this.roleid = roleid;
		this.serviceid = serviceid;
	}

	@Override
	protected boolean process() throws Exception {

		long curTime = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strTime = dateFormat.format(curTime);

		// 获得符合服务ID的补偿
		List<CompensationConfig> configList = CompensationManager.getInstance().getConfigFromServiceid(roleid,serviceid);
		if (configList != null && !configList.isEmpty()) {
			CompensationConfig config = getConfig(configList, curTime);
			if (config != null) {
				xbean.Compensation compensation = xtable.Compensation.get(roleid);
				if (compensation == null) {
					compensation = xbean.Pod.newCompensation();
					xtable.Compensation.insert(roleid, compensation);
				}
				xbean.CompensationInfo compensationInfo = compensation.getCompensationmap().get(config.getCompensationid());
				if (compensationInfo != null && config.isInCompensationTime(compensationInfo.getReceiveawardtime())) {// 如果已经领过了
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 142532, 10963, null);
					return false;
				}
				// 给奖励,并记录到数据库中,防止下次再领
				int itemid = config.getItemid();
				
				Map<String, Object> sparas = new HashMap<String, Object>();
				Team team=TeamManager.selectTeamByRoleId(roleid);
				if(team==null){
					sparas.put(RewardMgr.TEAM_NUM,  1);//添加队伍人数信息
				}else{
					sparas.put(RewardMgr.TEAM_NUM,  team.getNormalMemberIds().size());//添加队伍人数信息
				}
				
				RewardMgr.getInstance().distributeAllAward(roleid, itemid, sparas, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_buchangget, itemid,
						PAddExpProc.OTHER, "系统补偿", true);
				List<String> paras = MessageUtil.getMsgParaList(config.getServiceMsg());
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 142520, paras);
				if (compensationInfo == null) {
					compensationInfo = xbean.Pod.newCompensationInfo();
					compensation.getCompensationmap().put(config.getCompensationid(), compensationInfo);
				}
				compensationInfo.setReceiveawardtime(curTime);

				// 记录日志
				if (Module.logger.isInfoEnabled()) {
					Module.logger.info("[Mail.PReceiveCompensationAward(serviceId)] roleId:" + roleid
							+ " kind:" + 0
							+ " id:" + serviceid
							+ " time:" + strTime
							+ " awardId:" + itemid
							+ " title:" + config.getServiceMsg());
				}
				// 记录运营日志
				List<fire.log.beans.ItemBaseBean> items = new ArrayList<fire.log.beans.ItemBaseBean>();
				items.add(new fire.log.beans.ItemBaseBean(itemid, 1));
				fire.log.beans.MailBean mailLog = new fire.log.beans.MailBean((byte) 0, serviceid,
						config.getServiceMsg(), "", strTime, items);
				fire.log.YYLogger.mailLog(roleid, mailLog);
			}
		}

		// 获得符合服务ID和角色ID的补偿
		List<SpecialCompensationConfig> sccs = CompensationManager.getInstance().getSpecialConfigFromServiceid(serviceid, roleid);
		if (sccs!=null && !sccs.isEmpty()) {
			SpecialCompensationConfig config = getSpecialConfig(sccs,curTime);
			if (config != null) {
				xbean.Compensation compensation = xtable.Compensation.get(roleid);
				if (compensation == null) {
					compensation = xbean.Pod.newCompensation();
					xtable.Compensation.insert(roleid, compensation);
				}
				xbean.CompensationInfo compensationInfo = compensation.getCompensationmap().get(config.getId());
				if (compensationInfo != null && config.isInTime(compensationInfo.getReceiveawardtime())) {// 如果已经领过了
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 142532, 10963, null);
					return false;
				}
				// 给奖励,并记录到数据库中,防止下次再领
				int itemid = config.getAwardid();
				RewardMgr.getInstance().distributeAllAward(roleid, itemid, null, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_buchangget, itemid,
						PAddExpProc.OTHER, "系统补偿", true);
				List<String> paras = MessageUtil.getMsgParaList(config.getServiceName());
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 142520, paras);
				if (compensationInfo == null) {
					compensationInfo = xbean.Pod.newCompensationInfo();
					compensation.getCompensationmap().put(config.getId(), compensationInfo);
				}
				compensationInfo.setReceiveawardtime(curTime);

				// 记录日志
				if (Module.logger.isInfoEnabled()) {
					Module.logger.info("[Mail.PReceiveCompensationAward(serviceId,roleId)] roleId:" + roleid
							+ " kind:" + 0
							+ " id:" + serviceid
							+ " time:" + strTime
							+ " awardId:" + itemid
							+ " title:" + config.getServiceName());
				}
				// 记录运营日志
				List<fire.log.beans.ItemBaseBean> items = new ArrayList<fire.log.beans.ItemBaseBean>();
				items.add(new fire.log.beans.ItemBaseBean(itemid, 1));
				fire.log.beans.MailBean mailLog = new fire.log.beans.MailBean((byte) 0, serviceid,
						config.getServiceName(), "", strTime, items);
				fire.log.YYLogger.mailLog(roleid, mailLog);
			}
		}

		// 获得符合服务ID和UserID的补偿
		String useridStr = String.valueOf(new PropRole(roleid, true).getUserid());
		List<SpecialCompensationConfig> sccs2 = CompensationManager.getInstance().getUseridSpecialConfigFromServiceid(roleid,serviceid, useridStr);
		if (sccs2!=null && !sccs2.isEmpty()) {
			SpecialCompensationConfig config = getUseridSpecialConfig(sccs2,curTime);
			if (config != null) {
				xbean.Compensation compensation = xtable.Usercompensation.get(useridStr);
				if (compensation == null) {
					compensation = xbean.Pod.newCompensation();
					xtable.Usercompensation.insert(useridStr, compensation);
				}
				xbean.CompensationInfo compensationInfo = compensation.getCompensationmap().get(config.getId());
				if (compensationInfo != null && config.isInTime(compensationInfo.getReceiveawardtime())) {// 如果已经领过了
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 142532, 10963, null);
					return false;
				}
				// 给奖励,并记录到数据库中,防止下次再领
				int itemid = config.getAwardid();
				RewardMgr.getInstance().distributeAllAward(roleid, itemid, null, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_buchangget, itemid,
						PAddExpProc.OTHER, "系统补偿", true);
				List<String> paras = MessageUtil.getMsgParaList(config.getServiceName());
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 142520, paras);
				if (compensationInfo == null) {
					compensationInfo = xbean.Pod.newCompensationInfo();
					compensation.getCompensationmap().put(config.getId(), compensationInfo);
				}
				compensationInfo.setReceiveawardtime(curTime);

				// 记录日志
				if (Module.logger.isInfoEnabled()) {
					Module.logger.info("[Mail.PReceiveCompensationAward(serviceId,userId)] roleId:" + roleid
							+ " userId:" + useridStr
							+ " kind:" + 0
							+ " id:" + serviceid
							+ " time:" + strTime
							+ " awardId:" + itemid
							+ " title:" + config.getServiceName());
				}
				// 记录运营日志
				List<fire.log.beans.ItemBaseBean> items = new ArrayList<fire.log.beans.ItemBaseBean>();
				items.add(new fire.log.beans.ItemBaseBean(itemid, 1));
				fire.log.beans.MailBean mailLog = new fire.log.beans.MailBean((byte) 0, serviceid,
						config.getServiceName(), "", strTime, items);
				fire.log.YYLogger.mailLog(roleid, mailLog);
			}
		}

		return true;
	}

	private SpecialCompensationConfig getSpecialConfig(List<SpecialCompensationConfig> sccs, long curTime) {

		for (SpecialCompensationConfig scc : sccs) {
			if (scc.isInTime(curTime))
				if (CompensationManager.getInstance().checkCondition(scc.getCondtions(), roleid)) 
					if (CompensationManager.getInstance().checkZoneids(scc.getZoneids(), ConfigManager.getGsZoneId())) 
					     return scc;
		}
		return null;
	}

	private SpecialCompensationConfig getUseridSpecialConfig(List<SpecialCompensationConfig> sccs,long curTime){
		for (SpecialCompensationConfig scc : sccs) {
			if (scc.isInTime(curTime)) 
				if (CompensationManager.getInstance().checkZoneids(scc.getZoneids(), ConfigManager.getGsZoneId())) 
				       return scc;
		}
		return null;
	}

	private CompensationConfig getConfig(List<CompensationConfig> configList, long time) {

		for (CompensationConfig config : configList) {
			if (config.isInCompensationTime(time))
				return config;
		}
		return null;
	}

}
