package fire.pb.gm;

public class GM_selectfushi extends GMCommand {

	@Override
	boolean exec(String[] args) {

		final int userid = getGmUserid();
		xbean.YbNums ybNums = xtable.Fushinum.select(userid);
		xbean.YbNum ybNum = null;
		if (ybNums != null)
			ybNum = ybNums.getRoleyb().get(getGmroleid());

		int cashyb = 0;
		int bindyb = 0;
		int sysyb = 0;
		if (ybNum != null) {
			cashyb = ybNum.getNum();
			sysyb = ybNum.getSysnum();
		}
		sendToGM("非绑定符石:" + cashyb + "绑定符石:" + bindyb + "赠送符石:" + sysyb);
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
