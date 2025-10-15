package fire.pb.mbean;

import fire.log.Module;
import fire.log.beans.TotalValueState;

public class LogAnalyzer implements LogAnalyzerMXBean{

	@Override
	public TotalValueState getLogAnalysis()
	{
		return Module.getLogAnalyser();
		
	}	
	
}
