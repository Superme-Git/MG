package fire.pb.state;


public interface IState{

	
	// 当状态被进入时，执行 这个动作接口 
	public boolean enter(int trigger);
	
	// 当有触发条件到来时，触发状态
	public boolean trigger(int trigger);
	
	//获取state
	public int getState();
	
	// 当状态退出时 执行这个动作接口
//	public boolean exit();
}

