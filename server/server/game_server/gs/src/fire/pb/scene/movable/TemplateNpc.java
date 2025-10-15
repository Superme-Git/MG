package fire.pb.scene.movable;

/**
 * 模板NPC
 * 这个类的目的是：npcbaseId同样的npc，需要一些共有的属性（例如状态）
 * 而这些共有的属性，还可以被动态的修改（只是读表不能满足这种需求）
 * @author nobody
 *
 */
public class TemplateNpc
{
	private final int npcBaseId;
	
	private int state = 0;
	
	private boolean miniMapShow;//是否在小地图上显示
	
	public static int NPC_STATE_SHOW = 0;

	public static int NPC_STATE_HIDE = 1;
	
	
	public TemplateNpc(int npcBaseId)
	{
		this.npcBaseId = npcBaseId;
	}

	public int getNpcBaseId()
	{
		return npcBaseId;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public boolean isMiniMapShow()
	{
		return miniMapShow;
	}

	public void setMiniMapShow(boolean miniMapShow)
	{
		this.miniMapShow = miniMapShow;
	}
	
}
