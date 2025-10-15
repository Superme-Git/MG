

package fire.pb.timer;


//用来记录那些要在小地图上显示的动态增加或消失的npc.不是所有的动态npc都需要在小地图上显示 这个只是针对需要在小地图上显示的动态npc
public class MiniMapNpc {

	private final int baseid;

	private final int sceneid;

	private final int posx;

	private final int posy;

	public MiniMapNpc(int baseid, int sceneid, int posx, int posy) {

		super();
		this.baseid = baseid;
		this.sceneid = sceneid;
		this.posx = posx;
		this.posy = posy;
	}

	
	public int getBaseid() {
	
		return baseid;
	}

	
	public int getSceneid() {
	
		return sceneid;
	}

	
	public int getPosx() {
	
		return posx;
	}

	
	public int getPosy() {
	
		return posy;
	}

	@Override
	public boolean equals(Object obj) {
	  MiniMapNpc npc = (MiniMapNpc) obj;
      if (baseid==npc.getBaseid()&&sceneid==npc.getSceneid()&&posx==npc.getPosx()&&posy==npc.getPosy())
    	  return true;
		
		return false;
	}

	@Override
	public int hashCode() {

		
		return super.hashCode();
	}

}
