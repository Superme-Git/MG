package fire.pb.npc;

public class NpcPreNameTable {
	public final int id;
	public final String preName;
	
	public NpcPreNameTable(SNpcPreNameRandom preName) {
		this.id = preName.id;
		this.preName = preName.preName;
	}

}
