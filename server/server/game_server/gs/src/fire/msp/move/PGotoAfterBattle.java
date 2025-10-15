package fire.msp.move;

import fire.pb.GsClient;

public class PGotoAfterBattle extends mkdb.Procedure
{
	private final long roleId;
	private final long sceneId;
	private final int posx;
	private final int posy;
	private final int gototype;

	
	public PGotoAfterBattle(long roleId, long sceneId, int posx, int posy, int gototype)
	{
		this.roleId = roleId;
		this.sceneId = sceneId;
		this.posx = posx;
		this.posy = posy;
		this.gototype = gototype;
	}


	@Override
	protected boolean process() throws Exception
	{
		//DreamLandManager.gotoFromDreamland(roleId, MapUtil.getBaseMapIdBySceneId(sceneId));
		GsClient.pSendWhileCommit(new GGoto(roleId,sceneId,posx,posy,0,gototype));
		return true;
	}
}
