package fire.pb.fushi;

import fire.log.Logger;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

/***
 * 加符石
 * @author changhao
 *
 */
public class PAddFuShi extends Procedure {
    private static final Logger logger = Logger.getLogger("RECHARGE");
	private final int userid;
	
	private final long roleid;

	private final int fushiNum;
	
	private final int fushiType;//符石类型,现在又三种类型的符石
	private final YYLoggerTuJingEnum way;

	public PAddFuShi(int userid,long roleid, int fushiNum,int fushiType,YYLoggerTuJingEnum way) {
		super();
		this.userid = userid;
		this.roleid = roleid;
		this.fushiNum = fushiNum;
		this.fushiType = fushiType;
		this.way = way;
	}

	@Override
	protected boolean process() throws Exception {
		logger.info("PAddFuShi.process.userid[" + userid + "]roleid[" + roleid + "]开始处理加符石" + fushiNum + "!");
		if (userid<=0||roleid<=0) {
			logger.error("PAddFuShi.userid=" + userid + ",roleid=" + roleid + ",错误！");
			return false;
		}
		
		xbean.YbNums ybNums = xtable.Fushinum.get(userid);
		if (ybNums == null) {
			ybNums = xbean.Pod.newYbNums();
			xtable.Fushinum.insert(userid, ybNums);
		}
		xbean.YbNum ybNum = ybNums.getRoleyb().get(roleid);
		if (ybNum == null){
			ybNum = xbean.Pod.newYbNum();
			ybNums.getRoleyb().put(roleid, ybNum);
		}

		//判断有没有达到上限
		/*
		if (fushiType == FushiConst.SYS_FUSHI){
			if (fushiNum > 0){
				if(!FushiManager.checkFushiDayLimit(ybNum, fushiNum)){
					List<String> paras = MessageUtil.getMsgParaList(String.valueOf(ybNum.getBindorsysnumtoday()));
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 143808, 0, paras);
					logger.info("Add Fushi failed.roleid:"+roleid+"cash Num:"+ybNum.getNum()+"bind Num:"+ybNum.getBindnum()+
							"sys num:"+ybNum.getSysnum()+"fushiinOneday:"+ybNum.getBindorsysnumtoday()+"fushiNum:"+fushiNum);
					throw new RuntimeException("add fushi failed:over day limit");
				}
			}
		}
		*/
		if (!FushiManager.addFushiToUser(userid, roleid, fushiNum, fushiType, way)) {
			logger.error("PAddFuShi.addFushiToUser[" + userid + "]roleid[" + roleid + "]调用失败！");
			return false;
		}
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			DayPayManager.getInstance().ProcessDayPay(userid, roleid);
		}	

		//刷新符石数量
		if (!fire.pb.fushi.Module.getIsYYBUser(userid)) {
			FushiManager.refreshRoleFushi(roleid, ybNum, true);
		}
		
		//发通用提示消息
		if (fushiType == FushiConst.SYS_FUSHI) {
			MessageMgr.psendMsgNotifyWhileCommit(roleid,143951, MessageUtil.getMsgParaList(String.valueOf(fushiNum)));
		}
		logger.info("PAddFuShi.process.userid[" + userid + "]roleid[" + roleid + "]结束处理加符石" + fushiNum + "!");
		return true;
	}

}
