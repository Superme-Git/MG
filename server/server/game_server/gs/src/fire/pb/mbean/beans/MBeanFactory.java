package fire.pb.mbean.beans;

public class MBeanFactory {
	
	private static MBeanFactory _instance;
	
	private MBeanFactory(){}
	
	public static MBeanFactory getInstance(){
		synchronized (MBeanFactory.class) {
			if(null == _instance){
				_instance = new MBeanFactory();
			}
			
			return _instance;
		}
	}
	
	public ActiveRoleStateMXBean getActiveRoleState(){
		return new ActiveRoleState();
	}

	public ActiveUserStateMXBean getActiveUserState(){
		return new ActiveUserState();
	}
	
	public ChargeUserStateMXBean getChargeUserState(){
		return new ChargeUserState();
	}
	
	public ConsumeRoleStateMXBean getConsumeRoleState(){
		return new ConsumeRoleState();
	}
	
	public OnlinesNumStateMXBean getOnlinesNumState(){
		return new OnlinesNumState();
	}
	
	public DoActiveStateMXBean getForceLogActiveState(){
		return new DoActiveState();
	}
	
	public CacheInfoMXBean getCacheInfoBean(){
		return new CacheInfo();
	}
	
	public RankListMXBean getRankList(){
		return new RnakListImpl();
	}
	
	public GMProcMXBean getGMProcMXBeant(){
		return new GMProcMXBean.GMProc();
	}
	
	public SearchMXBean getSearchMXBean(){
		return new SearchMXBean.DBSearchMXBean();
	}
	
	
	
}
