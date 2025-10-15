package fire.pb;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.clan.srv.ClanManage;

public class PAddClanPointProc extends mkdb.Procedure {
	private final long roleid;
	private final int pointvalue;
	private final YYLoggerTuJingEnum way;
	private boolean showMsg = true;
	private String des="";

	public PAddClanPointProc(final long roleid, final int addvalue, YYLoggerTuJingEnum way) {
		super();
		this.roleid = roleid;
		this.pointvalue = addvalue;
		this.way = way;
	}


	public PAddClanPointProc(final long roleid, final int addcredit, YYLoggerTuJingEnum way,
			boolean show,String des) {
		super();
		this.roleid = roleid;
		this.pointvalue = addcredit;
		this.way = way;
		this.showMsg = show;
		this.des=des;
	}

	public boolean process() {
		if (pointvalue < 1){
			return false;
		}
		xbean.RoleClanPoint clanpoint = xtable.Roleclanpoint.get(roleid);
		if (clanpoint == null) {
			clanpoint = xbean.Pod.newRoleClanPoint();
			xtable.Roleclanpoint.insert(roleid, clanpoint);
		}
		//添加帮贡
		mkdb.Procedure proc=new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				ClanManage.addContribution(roleid, pointvalue, des, way,showMsg);
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
		return true;
	}
}
