
package fire.pb.mbean.beans;


import fire.pb.gm.GMInteface;

/**
 * gm执行接口
 */
public interface GMProcMXBean {
	public String execute(Integer userId,Long roleId,String cmd);
	
	public static class GMProc implements GMProcMXBean{
		public String execute(Integer userId,Long roleId,String cmd){
			/*
			 * 因为在控制台输入命令，比如invoke gs.counter:type=GMProcMXBeant 0 9882,4097,//addlevel 2   这里的空格在前期会被替换成“,”号，不符合现在的cmd表达式，所以
			 * 我们在输入命令的时候,用#号来替换空格，去掉// 写成这样invoke gs.counter:type=GMProcMXBeant 0 9882,4097,addlevel#2，然后在传递命令的时候做处理
			 * 
			 * 修改20150907 黄兆令
			 */
			
			cmd=cmd.replaceAll("#", " ");
			cmd="//"+cmd;
			
			int succ= GMInteface.execCommand(userId, roleId,0,cmd,false);
			if (succ==0) {
				return " command execute success "+ " userId "+ userId + " roleId "+roleId +" "+cmd;
			}
			return " error result is "+ succ + " userId "+ userId + " roleId "+roleId +" "+cmd ;
		}
	}
}
