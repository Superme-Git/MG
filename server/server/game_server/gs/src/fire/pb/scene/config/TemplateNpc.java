package fire.pb.scene.config;

/**
 * NPC　样本，这里给出一些NPC默认的
 * 这个类的目的是：npcbaseId同样的npc，需要一些共有的属性（例如状态）
 * 而这些共有的属性，还可以被动态的修改（只是读表不能满足这种需求）
 * 
 * @author nobody
 *
 */
public class TemplateNpc
{

	public TemplateNpc(int npcBaseId)
	{
		this.npcBaseId = npcBaseId;
	}

	public int getNpcBaseId()
	{
		return npcBaseId;
	}

	public boolean checkVisible()
	{
		return visible;
	}
	
	public void setVisible(boolean tf)
	{
		visible = tf;
	}
	
	public boolean checkMiniMapVisible()
	{
		return miniMapVisble;
	}
	
	public void setMiniMapVisible(boolean tf)
	{
		miniMapVisble = tf;
	}
	
	private final int npcBaseId;
	private boolean visible = true;
	private boolean miniMapVisble = true;
}
