package fire.pb.instancezone.conf;

import java.util.Map;

import fire.pb.instance.SInstanceAct;


public class ActionConfig
{
	public final SInstanceAct sFubenact;
	
	public final static int TYPE_MESSAGE = 1;
	public final static int TYPE_COMPLETE_SUBSAVE = 2;
	public final static int TYPE_TASK = 3;
	public final static int TYPE_ANSWER_QUESTION = 4;
	public final static int TYPE_BATTLE = 5;
	public final static int TYPE_SUBMIT_ITEM = 6;
	public final static int TYPE_GOTO = 7;	
	public final int servActId; 	
	public final int landId;
	
	public final int type;	
	public final Map<String,Object> params;	
	public ActionConfig(SInstanceAct sFubenact)
	{
		this.sFubenact = sFubenact;
		this.servActId = sFubenact.id;
		this.landId = sFubenact.belongfuben;
		this.type = sFubenact.type;
		this.params = ConfigParamName.parseConfigParams(sFubenact.parameters);
	}
}