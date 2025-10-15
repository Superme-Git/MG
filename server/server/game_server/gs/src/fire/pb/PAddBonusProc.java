package fire.pb;

import fire.pb.clan.srv.ClanManage;

/**
 * 添加公会分红
 * @author Administrator
 *
 */
public class PAddBonusProc extends mkdb.Procedure {
	private final long roleid;
	private final int pointvalue;
	private boolean showMsg = true;
	private String des="";

	public PAddBonusProc(final long roleid, final int addvalue) {
		super();
		this.roleid = roleid;
		this.pointvalue = addvalue;
	}


	public PAddBonusProc(final long roleid, final int addcredit,
			boolean show,String des) {
		super();
		this.roleid = roleid;
		this.pointvalue = addcredit;
		this.showMsg = show;
		this.des=des;
	}

	public boolean process() {
		if (pointvalue < 0){
			return false;
		}
		//添加公会的分红
		ClanManage.addClanBonus(roleid, pointvalue, des,showMsg);
		return true;
	}
}
