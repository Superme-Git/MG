

package fire.pb.compensation.condition;

import fire.pb.PropRole;
import fire.pb.compensation.CompensationCondition;
import fire.pb.compensation.ICompensationCondition;

public class RoleLevelCondition implements ICompensationCondition {

	@Override
	public boolean check(CompensationCondition cond,long roleid) {

		PropRole pRole = new PropRole(roleid, true);
		if (cond.getArgs().length == 2){
			if (cond.getArgs()[0] < pRole.getLevel() && pRole.getLevel() < cond.getArgs()[1])
				return true;
		}
		return false;
	}

}

