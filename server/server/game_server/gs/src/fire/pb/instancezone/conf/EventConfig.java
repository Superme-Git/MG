package fire.pb.instancezone.conf;

import java.util.Map;

import fire.pb.instance.SInstaceEvent;

public class EventConfig {
	public final int eventId;

	public final int landId;

	public final static int TYPE_OTHER = 0;
	public final static int TYPE_NPC_SERVICE = 1;
	public final static int TYPE_ANSWER_QUESTION = 2;
	public final static int TYPE_BATTLE_END = 3;
	public final static int TYPE_KILL_MONSTER = 4;
	public final static int TYPE_USE_ITEM = 5;
	public final static int TYPE_USE_BATTLE_ITEM = 6;
	public final static int TYPE_COMPLETE_TASK = 7;
	public final static int TYPE_SUBMIT_ITEMS = 8;

	public final int type;
	public final Map<String, Object> triggers;

	public final Map<String, Object> results;

	public EventConfig(SInstaceEvent sFubenthing) {
		this.eventId = sFubenthing.id;
		this.landId = sFubenthing.belongfuben;
		this.type = sFubenthing.type;
		this.triggers = ConfigParamName
				.parseConfigParams(sFubenthing.thingcondition);
		this.results = ConfigParamName
				.parseConfigParams(sFubenthing.thingresult);
	}

}
