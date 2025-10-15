package fire.pb.circletask;


public class PSubmitItemPetQuest extends mkdb.Procedure{
	private long roleid;
	private int questid;
	private long npckey;
	private java.util.ArrayList<fire.pb.npc.SubmitUnit> things;
	
	public PSubmitItemPetQuest(long roleid, int questid, long npckey, java.util.ArrayList<fire.pb.npc.SubmitUnit> things){
		this.roleid = roleid;
		this.questid = questid;
		this.npckey = npckey;
		this.things = things;
	}
	
	public boolean process(){
		return new PSubmitCircleTask(questid, roleid, npckey, things).call();
	}
}
