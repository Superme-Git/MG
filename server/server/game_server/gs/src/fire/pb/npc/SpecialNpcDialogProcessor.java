package fire.pb.npc;

import mkdb.Transaction;

public class SpecialNpcDialogProcessor {
	
	protected SVisitNpcContainChatMsg chatMsg = null;
	
	protected long roleid;
	protected long npcKey;
	
	public SpecialNpcDialogProcessor(long roleid, long npcKey){
		this.roleid = roleid;
		this.npcKey = npcKey;
	}
	
	public void onVisitNpc(){
		if(null != chatMsg){
			if(Transaction.current() != null){
				mkdb.Procedure.psendWhileCommit(roleid, chatMsg);
			}else{
				gnet.link.Onlines.getInstance().send(roleid, chatMsg);
			}
		}
	}
	
}
