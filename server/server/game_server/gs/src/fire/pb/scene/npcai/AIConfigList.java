package fire.pb.scene.npcai;
public final class AIConfigList 
{
	public AIConfigList()
	{
	}
	
	AIConfigList(final java.util.List<AIConfig> ais)
	{
		this.ais = ais;
	}
	
	final public java.util.List<AIConfig> getAis()
	{
		return ais;
	}
	
	private java.util.List<AIConfig> ais;
}

