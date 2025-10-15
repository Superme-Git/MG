package fire.pb.fushi;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import mkdb.Procedure;
import fire.pb.fushi.SAddCash;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
/***
 * 订单确认
 * @author changhao
 *
 */

public class PPayConfirm extends Procedure {
	private final long roleid;
	private final int userid;
	private final CConfirmCharge cConfirmCharge;
	
	public PPayConfirm(long roleid, int userid, CConfirmCharge cConfirmCharge) {
		this.roleid = roleid;
		this.userid = userid;
		this.cConfirmCharge = cConfirmCharge;
	}
	
	private boolean checkChargeLimit(ChargeConfig cfg) {
		xbean.RoleChargeLimit rcl = xtable.Roleschargelimit.get(roleid);
		if(rcl != null) {
			xbean.ChargeRecord cr = rcl.getChargeslimit().get(new Integer(cConfirmCharge.goodid));
			if(cr != null) {
				final long now = Calendar.getInstance().getTimeInMillis();
				final long lastchargetime = cr.getChargetime();
				if(fire.pb.util.DateValidate.inTheSameDay(lastchargetime, now)) {
					if(cr.getChargecount() >= cfg.chargecount) {
						FushiManager.logger.warn("角色[" + roleid + "]商品[" + cConfirmCharge.goodid + "]今天已经充值次数:" + cr.getChargecount() + 
								",超过上限" + cfg.chargecount);
						return false;
					}
					else
						FushiManager.logger.info("角色[" + roleid + "]商品[" + cConfirmCharge.goodid + "]今天已经充值次数:" + cr.getChargecount());
				} else {
					cr.setChargecount(0);
				}
			} else {
				xbean.ChargeRecord newCr = xbean.Pod.newChargeRecord();
				newCr.setChargecount(0);
				newCr.setChargetime(0L);
				rcl.getChargeslimit().put(new Integer(cConfirmCharge.goodid), newCr);
			}
		} else {
			xbean.RoleChargeLimit newRcl = xbean.Pod.newRoleChargeLimit();
			xbean.ChargeRecord newCr = xbean.Pod.newChargeRecord();
			newCr.setChargecount(0);
			newCr.setChargetime(0L);
			newRcl.getChargeslimit().put(new Integer(cConfirmCharge.goodid), newCr);
			xtable.Roleschargelimit.insert(roleid, newRcl);
		}
		return true;
	}
	
	@Override
	protected boolean process() throws Exception {
		if(fire.pb.fushi.Module.getIsYYBUser(userid)){
			FushiManager.logger.error("应用宝服务器，符石完全托管，禁用原来订单充值逻辑！");
			return true;
		}
		if(ConfigManager.CHARGEENABLE == 0) {
			FushiManager.logger.error("系统配置sys.properties未打开充值！");
			return true;
		}
		
		FushiManager.logger.info("角色["+roleid+"]确认充值.商品:" + cConfirmCharge.goodid + ",数量:" + cConfirmCharge.goodnum);
		ChargeConfig sChargeCfg = null;
		if(fire.pb.fushi.Module.GetPayServiceType() == 0){
			sChargeCfg = ConfigManager.getInstance().getConf(SAddCash.class).get(cConfirmCharge.goodid);
		}else{
			sChargeCfg = ConfigManager.getInstance().getConf(SAddCashPCard.class).get(cConfirmCharge.goodid);
		}
		if (sChargeCfg == null) {
			FushiManager.logger.error("角色["+roleid+"]确认充值.未找到商品id=" + cConfirmCharge.goodid + "的配置!");
			return false;
		}
		
		//检测今天该档位商品的充值次数
		if(ConfigManager.isChargeLimit && sChargeCfg.chargecount >= 0){
			if(!checkChargeLimit(sChargeCfg)) {
				//MessageMgr.psendMsgNotifyWhileRollback(roleid, 191031, null);
				SConfirmCharge sConfirmCharge = new SConfirmCharge();
				sConfirmCharge.extra = "191031";
				psendWhileRollback(roleid, sConfirmCharge);
				return false;
			}
		}
		
		if (!ConfigManager.OPEN_DY) {
			if (sChargeCfg.kind == 2) {
				FushiManager.logger.error("角色["+roleid+"]确认充值.kind=" + sChargeCfg.kind + ",但订阅类型已经关闭!");
				return false;
			}
		}
	    xbean.Properties properties = xtable.Properties.select(roleid);
	    if (properties == null) {
	    	FushiManager.logger.error("角色["+roleid+"]未找properties!");
			return false;
	    }
	    String nickname = xtable.Auuserinfo.selectNickname(properties.getUserid());
	    if (nickname == null) {
	    	FushiManager.logger.error("角色["+roleid+"]的Auuserinfo未找Nickname!");
	    	return false;
	    }
	    FushiManager.logger.info("角色["+roleid+"]确认充值.Nickname:" + nickname);
	    String platType;
	    if(nickname.isEmpty() || nickname.length() <= 0)
	    	platType = "xxxx";
	    else
	    	platType = nickname.substring(0,4);//ljpl
	    
	    //这里为了内网测试方便，通过帐号字符判断是否不走第三方sdk充值逻辑
	    if(!properties.getPlatformuid().contains("@@")){
	    	platType = "xxxx";
	    }
	    
	    FushiManager.logger.info("角色["+roleid+"]确认充值.platType:" + platType);
	    int rmbnum = sChargeCfg.sellpricenum * cConfirmCharge.goodnum;
	    if (rmbnum <= 0) {
	    	 FushiManager.logger.error("角色["+roleid+"]购买物品价格小于0！");
	    	return false;
	    }
	    //添加一个游戏内的新订单
	    xbean.ChargeOrder chargeOrder = xbean.Pod.newChargeOrder();
	    long chargeSn = xtable.Chargeorder.insert(chargeOrder);
	    chargeOrder.setStatus(1);
	    chargeOrder.setNum(rmbnum);
	    chargeOrder.setPlatformuid(properties.getPlatformuid());
	    chargeOrder.setPlattype(platType);
	    chargeOrder.setRoleid(roleid);
	    chargeOrder.setUserid(properties.getUserid());
	    chargeOrder.setCreatetime(System.currentTimeMillis());
	    chargeOrder.setGoodid(cConfirmCharge.goodid);
	    chargeOrder.setGoodnum(cConfirmCharge.goodnum);
	    FushiManager.logger.info("角色["+roleid+"],[" + chargeOrder.getPlatformuid() + "]请求订单号:" + chargeSn);
	    //添加一个玩家的新订单历史
	    xbean.ChargeHistory chargeHistory = xtable.Chargehistory.get(properties.getUserid());
	    if (chargeHistory == null){
	    	chargeHistory = xbean.Pod.newChargeHistory();
	    	xtable.Chargehistory.insert(properties.getUserid(), chargeHistory);
	    }
	    xbean.ChargeOrder chargeOrder2 = chargeOrder.copy();
	    chargeHistory.getCharges().put(chargeSn, chargeOrder2);
	    //回给客户端确认充值,包含游戏内订单号！
	    SConfirmCharge sConfirmCharge = new SConfirmCharge();
	    sConfirmCharge.billid = chargeSn;
	    sConfirmCharge.goodid = cConfirmCharge.goodid;
	    sConfirmCharge.goodnum = cConfirmCharge.goodnum;
	    sConfirmCharge.goodname = String.valueOf(sChargeCfg.sellnum) + sChargeCfg.name;
	    sConfirmCharge.price = rmbnum;
	    sConfirmCharge.serverid = ConfigManager.getGsZoneId(); //这个serverid到底尼玛有没有用啊！？日了狗了
	    sConfirmCharge.extra = "";
	    if(!platType.equals("xxxx")) {
		    //十秒后轮询一次,看是否充值成功
	    	if(ConfigManager.getChargeDirect()) {
	    		FushiManager.logger.info("角色["+roleid+"]确认充值.单号:" + chargeSn + ",等待平台直接回调端口" + ConfigManager.getCallBackPort() + "的接口!");
	    		psendWhileCommit(roleid, sConfirmCharge);
	    	} else {
	    		FushiManager.logger.info("角色["+roleid+"]确认充值.找到处理平台,开始十秒轮询,单号:" + chargeSn);
	 			mkdb.Executor.getInstance().schedule(new CheckCharge(chargeSn), 10, TimeUnit.SECONDS);
	 			FushiManager.logger.info("生成一个新的充值订单请求.roleid:"+roleid+",platformuid:"+properties.getPlatformuid()+",chargeGameSn:"+chargeSn+",rmbnum:"+rmbnum);
	 		    psendWhileCommit(roleid, sConfirmCharge);
	    	}
	    } else {
	    	//测试用,无法连接第三方绕过去,直接响应充值请求
	    	FushiManager.logger.info("角色["+roleid+"]确认充值.测试用绕过第三方直接响应充值请求,单号:" + chargeSn);
	    	gnet.QueryOrderResponse response = new gnet.QueryOrderResponse();
	 	    response.errorcode = 0;
	 	    response.orderserialgame = Long.toString(chargeSn);
	 	    response.orderserialplat = Long.toString(chargeSn);
	 	    response.platid = platType;
	 	    response.userid = properties.getUserid();
	 	    response.restype = 1;
	 	    response.vars.resize(4);
	 	    response.vars.setByte(0, (byte)0);
	 	    response.vars.setByte(1, (byte)0);
	 	    response.vars.setByte(2, (byte)0);
	 	    response.vars.setByte(3, (byte)0);	    
	 		new PQueryOrderResponseNew(response).call();
	    }    
		return true;
	}
	
}

