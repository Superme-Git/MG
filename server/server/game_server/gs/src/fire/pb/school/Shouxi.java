package fire.pb.school;

public enum Shouxi {
	WARRIOR(SchoolConst.WARRIOR_SHOUXI),
	PRIEST(SchoolConst.PRIEST_SHOUXI),
	PALADIN(SchoolConst.PALADIN_SHOUXI),
	 HUNTER(SchoolConst.HUNTER_SHOUXI),
	WARLOCK(SchoolConst.WARLOCK_SHOUXI),
	SAMAN(SchoolConst.SAMAN_SHOUXI),
	MAGIC(SchoolConst.MAGIC_SHOUXI),
	ROGUE(SchoolConst.ROGUE_SHOUXI),
	 DRUID(SchoolConst.DRUID_SHOUXI);
	
	private int value;
	private Shouxi(int value){
		this.value=value;
	}
	public int getValue(){
		return value;
	}
	public static Shouxi getShouxiByNpcid(int npcid){
		for (Shouxi shouxi : Shouxi.values()) {
			if(shouxi.getValue()==npcid)
				return shouxi;
		}
		return null;
	}
}
