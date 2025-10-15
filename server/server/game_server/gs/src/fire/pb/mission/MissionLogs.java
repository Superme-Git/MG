package fire.pb.mission;

import java.util.ArrayList;

public class MissionLogs {

	private ArrayList<MissionLog> logs = new ArrayList<MissionLog>();

	public void addLog( final MissionLog log ) {
		logs.add( log );
	}
	
	public void writeLog() {
		for ( MissionLog log : logs ) {
			log.write();
		}
	}
	
}
