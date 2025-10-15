package fire.pb.activity.impexam;

import java.util.Comparator;


public class ImpExamStateComparator implements Comparator<xbean.ImpExamStatePassRole> {
	@Override
	public int compare(xbean.ImpExamStatePassRole o1, xbean.ImpExamStatePassRole o2) {
		
		int iRetVal = o1.getAccrightnum() - o2.getAccrightnum();
		if(iRetVal > 0){
			return -1;
		}else if(iRetVal < 0){
			return 1;
		}

		long lRetVal = o2.getStateexamtime() - o1.getStateexamtime();
		if(lRetVal > 0){
			return -1;
		}else if(lRetVal < 0){
			return 1;
		}
		
		lRetVal = o2.getRoleid() - o1.getRoleid();
		if(lRetVal > 0){
			return -1;
		}else if(lRetVal < 0){
			return 1;
		}
		
		return 0;
	}

}
