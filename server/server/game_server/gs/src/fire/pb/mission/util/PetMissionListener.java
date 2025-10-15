package fire.pb.mission.util;

import fire.pb.team.TeamManager;

public class PetMissionListener {
	public PetMissionListener() {
	}
	
	public void onPetColumnChange( final long roleid, final int petid ) {
		mkdb.Procedure.pexecuteWhileCommit( new mkdb.Procedure() {
			@Override
			protected boolean process() {
//				new TaskScenarioColumn( roleid, this ).onEvent( 
//						new TaskScenarioColumn.PetAddEvent( petid ) );
				TeamManager.getTeamByRoleId(roleid);//强行锁队伍
				new fire.pb.circletask.CircleTask( roleid, false ).onPetColumnChange( petid );
				return true;
			}
		});
	}

}
