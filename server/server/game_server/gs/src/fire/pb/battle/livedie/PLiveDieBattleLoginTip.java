package fire.pb.battle.livedie;

import mkdb.Procedure;
import fire.pb.PropRole;

public class PLiveDieBattleLoginTip extends Procedure {
	private final long guestid;

	public PLiveDieBattleLoginTip(long guestRoleID) {
		this.guestid = guestRoleID;
	}

	@Override
	protected boolean process() throws Exception {
		
		Long object=xtable.Livedie2key.get(guestid);
		if(object!=null){
			xbean.LiveDieRoleInfo hostliveDieRoleInfo=xtable.Livedieroleinfotab.select(object);
			if(hostliveDieRoleInfo!=null){
				if(System.currentTimeMillis()-hostliveDieRoleInfo.getInvitationtime()<LiveDieMange.getLiveDieTime()&&hostliveDieRoleInfo.getAcceptflag()==0){
					SInvitationLiveDieBattleOK sInvitationLiveDieBattleOK =new SInvitationLiveDieBattleOK();
					String hostname=new PropRole(object,true).getName();
					sInvitationLiveDieBattleOK.sourceid=object;
					sInvitationLiveDieBattleOK.sourcename=hostname;
					sInvitationLiveDieBattleOK.selecttype=hostliveDieRoleInfo.getSelecttype();
					gnet.link.Onlines.getInstance().send(guestid, sInvitationLiveDieBattleOK);
				}
			}
			
		}
		
		
		
		return true;
	}

	

}
