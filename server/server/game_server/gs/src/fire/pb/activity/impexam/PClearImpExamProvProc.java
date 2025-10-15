package fire.pb.activity.impexam;

import fire.pb.npc.ImpExamType;

/**
 * 全省统考全国统考开始前预处理
 * @author cn
 *
 */
public class PClearImpExamProvProc extends mkdb.Procedure {

	public PClearImpExamProvProc(){
		
	}

	@Override
	protected boolean process() throws Exception {
		
		ImpExamManager.getInstance().impExamPull(ImpExamType.IMPEXAM_PROV);
		
		return true;
	}
}
