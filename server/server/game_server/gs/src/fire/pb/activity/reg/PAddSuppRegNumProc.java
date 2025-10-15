
package fire.pb.activity.reg;

import java.util.Calendar;

import fire.pb.util.singlefactory.SingleContainer;
import mkdb.Procedure;

public class PAddSuppRegNumProc extends mkdb.Procedure {
	private final long roleId;
	private final int suppRegNum;
	private boolean isQuery = true;
	private RegAssistant regAssistant;

	public PAddSuppRegNumProc(final long roleId, final int addSuppRegNum) {
		super();
		this.roleId = roleId;
		this.suppRegNum = addSuppRegNum;
		this.regAssistant = (RegAssistant) SingleContainer.getInstance(RegAssistant.class);
	}

	public PAddSuppRegNumProc(final long roleId, final int addSuppRegNum, boolean isQuery) {
		super();
		this.roleId = roleId;
		this.suppRegNum = addSuppRegNum;
		this.isQuery = isQuery;
		this.regAssistant = (RegAssistant) SingleContainer.getInstance(RegAssistant.class);
	}

	public boolean process() {
		if (suppRegNum < 1)
			return false;

		Calendar calen = Calendar.getInstance();
		int monthOfYear = regAssistant.getMonthOfYear(calen);

		xbean.RegRec record = regAssistant.getRecord(roleId);

		if (!record.getMonthmap().containsKey(monthOfYear)) {
			record.getMonthmap().put(monthOfYear, regAssistant.createMonth());
		}
		
		record.getMonthmap().get(monthOfYear).setSuppregnum(suppRegNum);

		if (isQuery) {
			Procedure.pexecuteWhileCommit(new PQueryRegRec(roleId));
		}

		return true;
	}
}
