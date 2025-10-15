
package fire.msp.move;

import fire.pb.activity.DelayActivityManager;
import mkdb.Procedure;


//场景创建之后,可能需要触发某些事件,写在这个procedure里面
public class PTriggerEventAfterSceneCreate extends Procedure {

	private long sceneid;
	private long ownerid;

	private int scenetype;
	
	public PTriggerEventAfterSceneCreate(long sceneid, long ownerid, int scenetype) {
        this.sceneid = sceneid;
        this.ownerid = ownerid;
        this.scenetype = scenetype;
	}
	@Override
	protected boolean process() throws Exception {
      if (scenetype==0)//静态场景先不管
    	  return false;
		DelayActivityManager.getInstance().executeDelayActivity(sceneid,ownerid,scenetype);
		return true;
	}

}

