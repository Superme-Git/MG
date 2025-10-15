package fire.pb.activity.impexam;

import fire.pb.npc.ImpExamType;

/**
 * 放弃答题或者 确定开始答题
 * @author cn
 */
public class PConfirmProc extends mkdb.Procedure{
	private final long roleId;
	private final int operate;
	private final int ietype;
	
	public PConfirmProc(long roleId, int impexamtype, int operate){
		this.roleId = roleId;
		this.operate = operate;
		this.ietype = impexamtype;
	}

	@Override
	protected boolean process() throws Exception {
		/*int impExamType = ImpExamManager.getInstance().isInImpExamTime();
		if(impExamType == -1){
			return false;
		}*/
		
		xbean.ImpExamRecord record = xtable.Role2impexam.get(roleId);
		if(record == null){
			record =  xbean.Pod.newImpExamRecord();
			xtable.Role2impexam.insert(roleId, record);
		}
		
		//放弃智力试练
		if(operate == 1){
			if(ietype != ImpExamType.IMPEXAM_VILL){
				record.setFlag( 2);
				record.setLastanswertime(System.currentTimeMillis());
			}
		}else{
			new PApplyImpExamProc(roleId, ietype, operate).call();
		}
		return true;
	}

}
