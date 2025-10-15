package fire.pb.scene.npcai;

public final class AIConfig 
{
	public AIConfig()
	{
	}
	
	AIConfig(int aid, final java.util.List<Config> configs)
	{
		this.id = aid;
		this.configs = configs;
	}
	
	public String toString()
	{
		String val = id + "\t";
		for(Config c : configs)
		{
			val += c + "\t";
		}
		
		return val;
	}
	
	final java.util.List<Config> getConfigs()
	{
		return configs;
	}
	
	final int getAID()
	{
		return id;
	}
	
	private int id;
	private java.util.List<Config> configs;
}

