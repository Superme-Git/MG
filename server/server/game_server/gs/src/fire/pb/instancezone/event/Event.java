package fire.pb.instancezone.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.instancezone.conf.EventConfig;
import fire.pb.instancezone.faction.FactionInstZone;

public abstract class Event {
	private long landkey;

	private int eventId = -1;

	final Map<String, Object> params = new HashMap<String, Object>();

	EventConfig eventcfg;

	public long getInstzonekey() {
		return landkey;
	}

	public void setInstzonekey(long landkey) {
		this.landkey = landkey;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public abstract int getType();

	public boolean match(EventConfig eventcfg) {
		try {
			if (eventId == eventcfg.eventId) {
				this.eventcfg = eventcfg;
				return true;
			}
			if (eventcfg.type != getType())
				return false;
			if (eventcfg.triggers.isEmpty())
				return false;
			for (Map.Entry<String, Object> entry : eventcfg.triggers.entrySet()) {
				Object o = params.get(entry.getKey());
				if (entry.getValue() instanceof List<?>) {
					boolean contain = false;
					for (Object oo : (List<?>) entry.getValue()) {
						if (((List<?>) o).contains(oo)) {
							contain = true;
							break;
						}
					}
					if (!contain)
						return false;

				} else if (!entry.getValue().equals(o))
					return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		this.eventId = eventcfg.eventId;
		this.eventcfg = eventcfg;
		return true;
	}

	public boolean process(InstanceZone instzone) {
		return processDefaultResults(instzone);
	}

	@SuppressWarnings("unchecked")
	public boolean processDefaultResults(InstanceZone instzone) {
		if (instzone instanceof FactionInstZone) {
			Object saveId = params.get(ConfigParamName.SAVE_ID);
			Object roleIds = params.get(ConfigParamName.ROLE_IDS);
			Object roleId = params.get(ConfigParamName.ROLE_ID);
			Object npcKey = params.get(ConfigParamName.NPC_KEY);

			if (roleIds != null)
				return ((FactionInstZone) instzone).updateSubSaveByNpcKey(
						(List<Long>) roleIds, (Long) npcKey, (Integer) saveId,
						0, 0);
			else if (roleId != null)
				return ((FactionInstZone) instzone).updateSubSaveByNpcKey(
						(Long) roleId, (Long) npcKey, (Integer) saveId, 0, 0);
			return true;
		} else {
			Object saveId = eventcfg.results.get(ConfigParamName.SAVE_ID);
			Object subsaveId = eventcfg.results
					.get(ConfigParamName.SUB_SAVE_ID);
			Object subsavecount = eventcfg.results
					.get(ConfigParamName.SUB_SAVE_COUNT);
			if (saveId == null || subsaveId == null || subsavecount == null)
				return false;
			Object roleIds = params.get(ConfigParamName.ROLE_IDS);
			Object roleId = params.get(ConfigParamName.ROLE_ID);
			if (roleIds != null)
				return instzone.updateSubSave((List<Long>) roleIds,
						(Integer) saveId, (Integer) subsaveId,
						(Integer) subsavecount);
			else if (roleId != null)
				return instzone.updateSubSave((Long) roleId, (Integer) saveId,
						(Integer) subsaveId, (Integer) subsavecount);
			else
				return instzone.updateSubSave((Integer) saveId,
						(Integer) subsaveId, (Integer) subsavecount);
		}
	}
}
