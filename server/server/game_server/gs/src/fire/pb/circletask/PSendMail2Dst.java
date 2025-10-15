package fire.pb.circletask;

import fire.pb.npc.NpcServices;


public class PSendMail2Dst extends mkdb.Procedure{
	private long roleid;
	private int npcid;
	private int serviceid;
	
	public PSendMail2Dst(long roleid, int npcid){
		this.roleid = roleid;
		this.npcid = npcid;
		this.serviceid = NpcServices.SEND_MAIL;
	}
	
	public PSendMail2Dst(long roleid, int npcid, int serviceid){
		this.roleid = roleid;
		this.npcid = npcid;
		this.serviceid = serviceid;
	}
	
	public boolean process(){
		CircleTask sq = new CircleTask(roleid, false);
		if(serviceid == NpcServices.SEND_MAIL)
			return sq.sendCTSchoolNpcMail(npcid);
		else
			return sq.sendCTSchoolNpcMail(npcid, serviceid);
	}
}
