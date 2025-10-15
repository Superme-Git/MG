package fire.pb.mbean.beans;

import fire.log.Module;
import fire.log.beans.OnlinesNunBean;
import fire.log.beans.TotalValueState;

public interface OnlinesNumStateMXBean {
	OnlinesNunBean getOnlinesNumState();
}

class OnlinesNumState implements OnlinesNumStateMXBean{

	@Override
	public OnlinesNunBean getOnlinesNumState() {
		OnlinesNunBean result = new OnlinesNunBean();
		TotalValueState state = Module.getLogAnalyser();
		
		result.setChargeTotal(state.getChargeTotal());
		result.setChargeUserNum(state.getChargeUserNum());
		result.setConsumeRoleNum(state.getConsumeRoleNum());
		result.setConsumeTotal(state.getConsumeTotal());
		result.setHistoryRoleNum(state.getHistoryRoleNum());
		result.setHistoryUserNum(state.getHistoryUserNum());
		result.setIncreaseRoleNum(state.getIncreaseRoleNum());
		result.setIncreaseUserNum(state.getIncreaseUserNum());
		result.setTick(state.getTick());
		
		return result;
	}
	
}