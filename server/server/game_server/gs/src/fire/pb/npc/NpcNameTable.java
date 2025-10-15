package fire.pb.npc;

import fire.pb.npc.SNpcNameRandom;

public class NpcNameTable {

	public final int id;
	public final String firstName;
	public final String secondName;
	
	public NpcNameTable(SNpcNameRandom npcNameRnd) {
		this.id = npcNameRnd.id;
		this.firstName = npcNameRnd.firstName;
		this.secondName = npcNameRnd.secondName;
	}
}
