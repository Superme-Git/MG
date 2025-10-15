
package fire.pb.buff.continual;


import fire.pb.buff.BuffAgent;
import fire.pb.skill.Result;
import xbean.Buff;


public class SchoolQuestBuff extends ConstantlyBuff {

	public SchoolQuestBuff(Buff buffBean) {super(buffBean);}
	public SchoolQuestBuff(ConstantlyBuffConfig buffConfig) {super(buffConfig);}

	@Override
	public Result closure(BuffAgent buffAgent,boolean timeout) {
		if (timeout){
			
		}
		return super.closure(buffAgent,timeout);
	}

	
	

}

