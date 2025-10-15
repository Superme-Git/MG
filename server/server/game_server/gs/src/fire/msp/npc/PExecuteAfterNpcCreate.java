
package fire.msp.npc;

import java.util.List;

import mkdb.Procedure;


public class PExecuteAfterNpcCreate extends Procedure {

	private List<NpcInfo> npcs;

	public PExecuteAfterNpcCreate(List<NpcInfo> npcs) {

		this.npcs = npcs;
	}

	@Override
	protected boolean process() throws Exception {
		return true;
	}

	public List<NpcInfo> getNpcs() {
		return npcs;
	}

	public void setNpcs(List<NpcInfo> npcs) {
		this.npcs = npcs;
	}

	
}
