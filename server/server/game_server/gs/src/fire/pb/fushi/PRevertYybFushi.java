package fire.pb.fushi;

/**
 * 恢复订单前的符石数据
 */

import mkdb.Procedure;

public class PRevertYybFushi extends Procedure {
	private final long billno;
	private final int userid;
	private final long roleid;
	
	public PRevertYybFushi(long billno, int userid, long roleid) {
		this.billno = billno;
		this.userid = userid;
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.YybOrder yybOrder = xtable.Yybchargeorder.select(billno);
		if (yybOrder == null) {
			FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleid).append(",userId:")
					.append(userid).append(",增加符石请求,yybOrder无效:").append(billno));
			return false;
		}
		
		//还原成订单之前的本地数据
		xbean.YybFushiNums yybFs = xtable.Yybfushi.get(userid);
		if (yybFs == null) {
			return false;
		}
		
		xbean.YybFushiNum yybFushiNum = yybFs.getRolefushi().get(roleid);
		if (yybFushiNum == null) {
			return false;
		}
		
		yybFushiNum.setSaveamt(yybOrder.getLocalsaveamt());
		
		yybFushiNum.setBalance(yybOrder.getLocalbalance());
		yybFushiNum.setGenbalance(yybOrder.getLocalgenbalance());
		
		xtable.Yybchargeorder.remove(billno);
		
		return true;
	}
}
