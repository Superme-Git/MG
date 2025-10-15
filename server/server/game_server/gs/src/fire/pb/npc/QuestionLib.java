package fire.pb.npc;

import java.util.HashMap;
import java.util.Map;

import fire.pb.mission.Squestions;
import fire.pb.util.Misc;

public class QuestionLib
{
	public final int libId;
	
	public final Map<Integer,Squestions> questions = new HashMap<Integer, Squestions>();
	
	QuestionLib(int libId)
	{
		this.libId = libId;
	}
	
	public Squestions randomQuestion()
	{
		return Misc.getRandom(questions.values());
	}
	
}
