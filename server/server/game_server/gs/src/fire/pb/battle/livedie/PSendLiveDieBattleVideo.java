package fire.pb.battle.livedie;

import org.apache.log4j.Logger;

import mkdb.Procedure;
import fire.pb.battle.SSendCameraUrl;

public class PSendLiveDieBattleVideo extends Procedure {
	public static final Logger logger = Logger.getLogger("BATTLE");
	
	private final long roleid;
	private final String vedioid;

	public PSendLiveDieBattleVideo(long roleid, String vedioid) {
		this.roleid = roleid;
		this.vedioid = vedioid;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.LDVideoRoleInfoDes ld=LiveDieMange.ldvideoroleinfodeslistall.get(vedioid);
		if(ld==null){
			//提示录像不存在
			return false;
		}
		//判断录像是否保存成功
		if(ld.getSaveresult()==0){
			//提示录像不存在
			logger.info("生死战录像没有保存成功，录像id"+vedioid);
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 162123, null);
			return false;
		}
		SSendCameraUrl send = new SSendCameraUrl(0,(byte) 1, ld.getBeforevideosize(), ld.getAftervideosize(),ld.getVideourl());
		psendWhileCommit(roleid, send);
		return true;
	}


}
