package fire.pb.instancezone.conf;

import java.util.LinkedList;
import java.util.List;

import fire.pb.instance.SInstanceChildSave;

public class SubSaveConfig
{
	public final SInstanceChildSave sFubenchildrenjindu;	
	public final int subSaveId;	
	public final int belongInstzoneId;	
	public final int belongSaveId;	
	public final int maxcount;	
	public final List<Integer> conflictSubSaveIds = new LinkedList<Integer>();

	public SubSaveConfig(SInstanceChildSave sFubenchildrenjindu)
	{
		this.sFubenchildrenjindu = sFubenchildrenjindu;
		this.subSaveId = sFubenchildrenjindu.childrenjindu;
		this.belongSaveId = sFubenchildrenjindu.belongjindu;
		this.belongInstzoneId = sFubenchildrenjindu.belongfuben;
		this.maxcount = sFubenchildrenjindu.num;
		if(sFubenchildrenjindu.YNchildrenjindu != null && !sFubenchildrenjindu.YNchildrenjindu.equals(""))
		{
			String[] subsaves = sFubenchildrenjindu.YNchildrenjindu.split(";");
			for(String str : subsaves)
				conflictSubSaveIds.add(Integer.valueOf(str));
		}
	}	
}
