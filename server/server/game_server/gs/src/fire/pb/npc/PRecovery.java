package fire.pb.npc;

import mkdb.Procedure;

public abstract class PRecovery extends Procedure {
	protected final long roleid;
	protected final long npckey;
	public PRecovery( final long roleid, final long npckey ) { 
		this.roleid = roleid;
		this.npckey = npckey;
	}
	abstract boolean check();
	abstract void cost();
	abstract void recovery();
	@Override
	protected final boolean process() {
		if ( check() ) {
			cost();
			recovery();
			return true;
		}
		return false;
	}
}
