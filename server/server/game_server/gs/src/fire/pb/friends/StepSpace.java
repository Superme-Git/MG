package fire.pb.friends;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mkdb.Transaction;
import net.sf.json.JSONObject;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import fire.pb.RoleConfigManager;
import fire.pb.common.SCommon;
import fire.pb.http.HttpCallBackHandler;
import fire.pb.main.Gs;
import fire.pb.mission.activelist.RoleLivenessManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.FireProp;
import fire.pb.util.MessageUtil;

public class StepSpace{
	public static final Logger logger = Logger.getLogger("FRIEND");
	public static final String address = FireProp.getStringValue("sys", "sys.weibo.address");
	public static final String func = "/bbs/add_popularity";
	
	private final long roleId;
	private final long spaceRoleId; //空间角色id

	public StepSpace(long roleId, long spaceRoleId) {
		this.roleId = roleId;
		this.spaceRoleId = spaceRoleId;
	}

	public boolean process() {
		try {
			if(roleId == spaceRoleId)
				return false;

			String serverkey = Gs.serverid;
			String roleid = String.valueOf(roleId);
			String to_roleid = String.valueOf(spaceRoleId);
			
			int getgift = -1;
			final long now = Calendar.getInstance().getTimeInMillis();
			Long getgifttime = xtable.Rolespaces.selectGetgifttime(roleId);
			long lastgetgifttime = (getgifttime == null ? 0L : getgifttime.longValue());
			if (fire.pb.util.DateValidate.inTheSameDay(lastgetgifttime, now)){
				final SCommon cc = RoleConfigManager.getRoleCommonConfig(344);
				int maxnum = Integer.parseInt(cc.getValue());//每天的最大获得礼物次数
				//是同一天,检测次数
				int alreadynum = xtable.Rolespaces.selectGetgiftnum(roleId);
				if(alreadynum < maxnum) {
					//没有超过次数,随机是否获得礼物
					getgift = Module.getRandomSpaceDropGift(spaceRoleId);
				} else {
					logger.info("PStepSpace.角色[" + roleId + "]准备踩角色[" + spaceRoleId + "]的空间，但今天已经超过获得礼物上限(" + maxnum + ")，不会获得礼物！");
					fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 162160, null);
				}
			} else {
				//不是同一天，直接随机礼物
				getgift = Module.getRandomSpaceDropGift(spaceRoleId);
				//清空历史获得礼物数量
				new mkdb.Procedure() {
    				@Override
    				protected boolean process() {
    					logger.info("StepSpace.角色[" + roleId + "]准备踩角色[" + spaceRoleId + "]的空间，跨天了，清空历史获得礼物数量！");
    					xbean.RoleSpace srcrs = xtable.Rolespaces.get(roleId);
						if(srcrs == null){
							logger.error("StepSpace.角色[" + roleId + "]空间xdb数据null!");
    						return false;
						}
						srcrs.setGetgiftnum(0);
    					return true;
    				}
				}.submit().get();
			}
			String is_get = (getgift == -1 ? String.valueOf(0) : String.valueOf(1));			
			final String buildurl = String.format(
					"http://%1$s%2$s?serverkey=%3$s&roleid=%4$s&to_roleid=%5$s&is_get=%6$s", 
					address, func, serverkey, roleid, to_roleid, is_get);
			logger.info("PStepSpace.buildurl:" + buildurl);
			final HttpGet request = new HttpGet(buildurl);
			logger.info("PStepSpace.request:" + request.getURI() + " thread:" + Thread.currentThread().getId());
			Gs.getHttpClient().execute(request, new StepSapceHttpHandler(roleId, spaceRoleId, getgift, request));
			logger.info("PStepSpace.request over! thread:" + Thread.currentThread().getId());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private static class StepSapceHttpHandler extends HttpCallBackHandler {
		private HttpGet request;
		private final long roleId;
		private final long spaceRoleId; //空间角色id
		private final int gift;
		
		public StepSapceHttpHandler(long roleId, long spaceRoleId, int gift, HttpGet request) {
			this.request = request;
			this.roleId = roleId;
			this.gift = gift;
			this.spaceRoleId = spaceRoleId;
		}
		
		@Override
		protected boolean process(JSONObject json) {
			final String errno = json.getString("errno");
            if(errno.isEmpty() || errno.equals("")) {
            	new mkdb.Procedure() {
    				@Override
    				protected boolean process() {
    					xbean.RoleSpace rs = xtable.Rolespaces.get(spaceRoleId);
    					if(rs == null)
    						return false;
    					rs.setPopularity(rs.getPopularity() + 1);
    					logger.info("PStepSpace.角色[" + roleId + "]踩了角色[" + spaceRoleId + "]的空间，为其增加了一点人气！");
    					SStepSpace send = new SStepSpace();
    					if(gift > 0){
    						fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
    						if(bag.doAddItem(gift, 1, 0, 0, "踩空间奖励礼物", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huodongjiangli, gift) != 1){
    							MessageMgr.psendMsgNotifyWhileCommit(roleId, 144875, null);//背包满
    							send.result = 1;
    							//MessageMgr.psendMsgNotifyWhileRollback(roleId, 144875, null);
    							//return false;
    							logger.info("PStepSpace.角色[" + roleId + "]踩了角色[" + spaceRoleId + "]的空间,随机获得礼物,但是背包满了!");
    						} else {
    							List<String> paras = new ArrayList<String>(4);
    							paras.add(MessageUtil.getItemName(gift));
    							fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 162153, paras);
    							rs.setGift(rs.getGift() - 1);
    							send.result = 0;
    							xbean.RoleSpace srcrs = xtable.Rolespaces.get(roleId);
    							if(srcrs == null)
    	    						return false;
    							srcrs.setGetgiftnum(srcrs.getGetgiftnum() + 1);
    							final long now = Calendar.getInstance().getTimeInMillis();
    							srcrs.setGetgifttime(now);
    							logger.info("PStepSpace.角色[" + roleId + "]踩了角色[" + spaceRoleId + "]的空间,随机获得礼物" + gift + "!" + roleId + "已经获得" + srcrs.getGetgiftnum() + "次礼物.");
    						}
    					} else {
    						fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 162152, null);
    						logger.info("PStepSpace.角色[" + roleId + "]踩了角色[" + spaceRoleId + "]的空间,没有获得随机礼物!");
    					}
    					psendWhileCommit(roleId, send);
    					return true;
    				}
    			}.submit();
            } else if (errno.equalsIgnoreCase("bbs_addpopularity_repeat")) {
            	//今天踩过了
        		if (Transaction.current() == null) {
        			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 162146, null);
        		} else {
        			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 162146, null);
        		}
            	
            	logger.info("PStepSpace.角色[" + roleId + "]今天已经踩过了角色[" + spaceRoleId + "]的空间！");
            } else {
            	logger.error("StepSpace.httpReqCallBack errno=" + errno + "!");
            }
			return true;
		}
	}
}
