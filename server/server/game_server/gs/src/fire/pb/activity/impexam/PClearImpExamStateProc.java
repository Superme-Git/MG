package fire.pb.activity.impexam;


/**
 * 全省统考全国统考开始前预处理
 * @author cn
 *
 */
public class PClearImpExamStateProc extends mkdb.Procedure {

	public PClearImpExamStateProc(){
		
	}

	@Override
	protected boolean process() throws Exception {
		
		xbean.ImpExamStateRecord  iesRecord = xtable.Impexamstatetbl.get(1);
		if(iesRecord == null){
			iesRecord = xbean.Pod.newImpExamStateRecord();
			xtable.Impexamstatetbl.insert(1, iesRecord);
		}
		iesRecord.getIdslist().clear();
		iesRecord.getPassidslist().clear();
		iesRecord.getPassrolelist().clear();
		
		
		return true;
	}
}
