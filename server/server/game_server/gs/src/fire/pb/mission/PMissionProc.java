package fire.pb.mission;

import mkdb.Procedure;

public abstract class PMissionProc extends Procedure {
	private final MissionLogs logs = new MissionLogs();
	
	protected final MissionLogs getLogs() { 
		return logs; 
	}
	
	protected void init() {
	}
	
	protected abstract boolean missionExecute();
	
	@Override
	protected final boolean process() {
		if (missionExecute()) {
			logs.writeLog();
			return true;
		}
		return false;
	}
}
