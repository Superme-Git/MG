package fire.pb;


/**
 * 援助统计功能添加
 *
 */
public class PAddHelpCountProc extends mkdb.Procedure{
	private long roleid;
	private long addexpvalue=0; // 经验
	private int addshengwangvalue=0; // 声望
	private int addclanvalue=0; // 帮贡
	private int addclanhelpnum=0;
	private int addhelpgiveitemnum=0;
	private int addhelpitemnum=0;
	
	public PAddHelpCountProc(final long roleid, long addexpvalue,int addshengwangvalue, int addclanvalue){
		this.roleid = roleid;
		this.addexpvalue = addexpvalue; // 经验
		this.addshengwangvalue = addshengwangvalue; // 声望
		this.addclanvalue = addclanvalue; // 帮贡
	}
	
	public PAddHelpCountProc(final long roleid, long addexpvalue,int addshengwangvalue, int addclanvalue,int addhelpgiveitemnum,int addhelpitemnum){
		this.roleid = roleid;
		this.addexpvalue = addexpvalue; // 经验
		this.addshengwangvalue = addshengwangvalue; // 声望
		this.addclanvalue = addclanvalue; // 帮贡
		this.addhelpgiveitemnum = addhelpgiveitemnum; 
		this.addhelpitemnum = addhelpitemnum; 
	}
	
	public PAddHelpCountProc(final long roleid,int addclanhelpnum){
		this.roleid = roleid;
		this.addclanhelpnum = addclanhelpnum;
	}
	
	
	public boolean process(){
		xbean.helpcount helpcount = xtable.Helpcount.get(roleid);
		if (helpcount == null) {
			helpcount = xbean.Pod.newhelpcount();
			xtable.Helpcount.insert(roleid, helpcount);
		}
		// 经验
		if (addexpvalue > 0) {
			helpcount.setExpvalue(helpcount.getExpvalue() + addexpvalue);
			long max = fire.pb.HelpCountManage.getInstance().getExpValueMax(roleid);
			if (helpcount.getExpvalue() > max) {
				helpcount.setExpvalue(max);
			}
		}
		// 声望
		if (addshengwangvalue > 0) {
			helpcount.setShengwangvalue(helpcount.getShengwangvalue()+ addshengwangvalue);
			int max = fire.pb.HelpCountManage.getInstance().getShengWangValueMax(roleid);
			if (helpcount.getShengwangvalue() > max) {
				helpcount.setShengwangvalue(max);
			}
		}
		// 帮贡
		if (addclanvalue > 0) {
			helpcount.setClanvalue(helpcount.getClanvalue()+ addclanvalue);
			int max = fire.pb.HelpCountManage.getInstance().getClanValueMax(roleid);
			if (helpcount.getClanvalue() > max) {
				helpcount.setClanvalue(max);
			}
		}
		//援助物品次数
		if(addhelpgiveitemnum>0){
			helpcount.setHelpgiveitemnum(helpcount.getHelpgiveitemnum()+addhelpgiveitemnum);
			int max = fire.pb.HelpCountManage.getInstance().getHelpGiveItemNumMax(roleid);
			if(helpcount.getHelpgiveitemnum()>max){
				helpcount.setHelpgiveitemnum(max);
			}
		}
		//求助物品次数
		if(addhelpitemnum>0){
			helpcount.setHelpitemnum(helpcount.getHelpitemnum()+addhelpitemnum);
			int max = fire.pb.HelpCountManage.getInstance().getHelpItemNumMax(roleid);
			if(helpcount.getHelpitemnum()>max){
				helpcount.setHelpitemnum(max);
			}
		}
		if(addclanhelpnum>0){
			//处理公会援助
			helpcount.setClanhishelpnum(helpcount.getClanhishelpnum()+addclanhelpnum);
			mkdb.Procedure proc = new mkdb.Procedure(){
				@Override
				protected boolean process() {
					Long clankey=xtable.Properties.selectClankey(roleid);
					if(clankey!=null&&clankey>0){
						xbean.ClanInfo clanInfo = xtable.Clans.get(clankey);
						if(clanInfo!=null){
							xbean.ClanMemberInfo clanmember = clanInfo.getMembers().get(roleid);
							if(clanmember!=null){
								clanmember.setClanhelpnum(clanmember.getClanhelpnum()+addclanhelpnum);
							}
						}
					}
					return true;
				}
			};
			mkdb.Procedure.pexecuteWhileCommit(proc);
		}
		
		
		return true;
	}

}
