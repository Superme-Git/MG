
package fire.pb.title;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import mkdb.Procedure;


//称谓的添加删除，佩戴卸下，查询等称谓相关操作的封装
public class Title {
	private long roleid;
	private final xbean.Properties prop;
	private boolean readonly;
	private static final int maxtitlesnum = 200;
	
	public static ConcurrentHashMap<Long, Long> titleroleids= new ConcurrentHashMap<Long, Long>();//有限时称号的玩家
	
	
	public Title(long roleid, boolean readonly){
		this.roleid = roleid;
		this.readonly = readonly;
		if(!readonly){
			this.prop = xtable.Properties.get(roleid);
		} 
		else {
			this.prop = xtable.Properties.select(roleid);
		}
		
		this.roleid = roleid;
		this.readonly = readonly;
	}
	

	//判断角色是否含有titleid标识的称谓.
	public boolean roleHaveTitle(int titleid){
		java.util.Map<Integer, xbean.TitleInfo> titles = prop.getTitles();
		if (null == titles || !titles.containsKey(titleid))
			return false;
		return true;
	}
	
	//判断角色是否佩戴着称谓
	public boolean roleOnTitle(){
		if (prop.getTitle() < 0)
			return false;
		return true;
	}
	
	//判断角色是否佩戴着指定的称谓
	public boolean roleOnTitle(int titleid){
		if (titleid < 0)
			return false;
		if (prop.getTitle() == titleid )
			return true;
		return false;
	}
	
	public int getRoleOnTitle(){
		return roleOnTitle() ? prop.getTitle(): 0;
	}
	
	//获得称谓信息，用于发送到地图模块
	public fire.msp.showtitleinfo getShowTitleInfo(int titleid){
		final fire.msp.showtitleinfo info = new fire.msp.showtitleinfo();
		if (titleid < 0)
			return info;
		
		if (null == prop.getTitles())
			return info;
		
		final xbean.TitleInfo titleinfo = prop.getTitles().get(titleid);
		if (null == titleinfo)
			return info;
		
		info.titleid = titleinfo.getTitleid();
		info.titlename = titleinfo.getTitlename();
		info.validtime = titleinfo.getAvailtime();
		return info;
	}
	
	//判断当前角色是否还可以加称谓
	public boolean canAddTitle(){
		if (null != prop.getTitles())
			return prop.getTitles().size() < maxtitlesnum;
		return false;
	}
	

	//添加称谓的具体实现
	public boolean addTitle(int titleid, String name, long time){
		if (readonly)
			throw new RuntimeException("不能在存储过程外调用此函数!");
		if (time < -1)
			throw new RuntimeException("调用加称谓接口，参数不合法");
		
		STitleConfig titleconfig = fire.pb.title.TitleManager.getTitleConfigById(titleid);
		if (titleconfig == null){
			psendTitleErrWhileRollback(TitleError.Title_Err_Unexist);
			return false;
		}
		if (!canAddTitle()){
			psendTitleErrWhileRollback(TitleError.Title_Err_Full);
			return false;
		}
		if (roleHaveTitle(titleid))
			return false;
		
		final xbean.TitleInfo value = xbean.Pod.newTitleInfo();
		value.setTitleid(titleid);
		
		if (name != null){
			value.setTitlename(name);
		}
		else
			value.setTitlename("");
		
		long now = Calendar.getInstance().getTimeInMillis();
		if (time <=0){
			if (titleconfig.availtime == -1)
				value.setAvailtime(-1);
			else{
				value.setAvailtime(titleconfig.availtime * 1000L + now);// 配置的单位是秒。
			}
		}
		else{
			value.setAvailtime(now + time);
		}
		prop.getTitles().put(titleid, value);
		
		//TODO:开称谓计时器吗？
		//如果是限时称号，添加到队列
		if(titleconfig.availtime>0){
			Title.titleroleids.put(roleid, roleid);
		}
		
		//通知客户端的是称谓的剩余时间
		final SAddTitle addTitle = new SAddTitle();
		final fire.pb.title.TitleInfo info = new fire.pb.title.TitleInfo(titleid, value.getTitlename(), value.getAvailtime());
		addTitle.info = info;
		Procedure.psendWhileCommit(roleid, addTitle);
		return true;
	}
	

	//留给其他模块的接口，添加称谓
	public static void addTitle(long roleid, int titleid, String name, long time){
		final PAddTitleProc addproc = new PAddTitleProc(roleid, titleid, name, time);
		if ( mkdb.Transaction.current() == null ) {
			addproc.submit();
		}
		else{
			addproc.call();
		}
		
	}
	
	public static boolean hasTitle(long roleid,int titleid){
		xbean.Properties properties = xtable.Properties.select(roleid);
		if (properties!=null){
			java.util.Map<Integer, xbean.TitleInfo> titles = properties.getTitles();
			if (null == titles || !titles.containsKey(titleid))
				return false;
			return true;
		}
		return false;
	}

	//删除角色称谓的具体实现
	public boolean removeTitle(int titleid){
		if (readonly)
			throw new RuntimeException("不能在存储过程外调用此函数!");
		
		if (!prop.getTitles().containsKey(titleid))
			return true;
		
		if (prop.getTitle() == titleid){
			prop.setTitle(-1);
			
			//TODO: 有buff吗？ 删除称谓buff
			fire.msp.title.GNotifyTitle send = new fire.msp.title.GNotifyTitle();
			send.roleid = roleid;
			fire.pb.GsClient.pSendWhileCommit(send);
		}
		
		prop.getTitles().remove(titleid);
		
		final SRemoveTitle removeSend = new SRemoveTitle(titleid);
		Procedure.psendWhileCommit(roleid, removeSend);
		return true;
		
	}

	//留给其他模块的接口，删除称谓
	public static void removeTitle(long roleid, int titleid){
		final PRemoveTitleProc removeproc = new PRemoveTitleProc(roleid, titleid);
		if ( mkdb.Transaction.current() == null ) {
			removeproc.submit();
		}
		else{
			removeproc.call();
		}
	}
	
	public static void changeTitle(long roleid, int titleid, String name, long time, boolean isOnTitle){
		Title title = new Title(roleid, false);
		title.removeTitle(titleid);
		title.addTitle(titleid, name, time);
		
		if(isOnTitle){
			title.onTitle(titleid);
		}
	}
	
	//佩戴称谓 ,如果已经佩戴其他称谓，则卸下.
	public boolean onTitle(int titleid){
		if (readonly)
			throw new RuntimeException("不能在存储过程外调用此函数");
		final xbean.TitleInfo titleinfo = prop.getTitles().get(titleid);
		if (titleinfo == null){
			psendTitleErrWhileRollback(TitleError.Title_Err_Nuowned);
			return false;
		}
		
		final int oldtitle = prop.getTitle();
		if (oldtitle > 0){
			if (prop.getTitle() == titleid)
				return false;
		}
	
		prop.setTitle(titleid);
		
		// 通知地图模块，玩家称谓改变
		fire.msp.title.GNotifyTitle send = new fire.msp.title.GNotifyTitle();
		send.roleid = roleid;
		send.title = this.getShowTitleInfo(titleid);
		fire.pb.GsClient.pSendWhileCommit(send);
		return true;
	}
	
	//卸下称谓的具体实现
	public boolean offTitle(){
		if (readonly)
			throw new RuntimeException("不能在存储过程外使用此函数");
		if (prop.getTitle() < 0)
			return false;

		prop.setTitle(-1);
		
		// 通知地图模块，玩家称谓改变
		fire.msp.title.GNotifyTitle send = new fire.msp.title.GNotifyTitle();
		send.roleid = roleid;
		fire.pb.GsClient.pSendWhileCommit(send);
		return true;
	}
	
	//判断能否更换称谓- 战斗中和观战中不能更换称谓，选择时透明框提示：战斗/观战中不能更换称谓
	public boolean checkUpdateTitle(){
		if (readonly)
			return false;
		//生死战的称号不能卸下
		fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(role != null){
			if(COffTitle.isLiveDieTitle(prole)){
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 162120, null);
				return false;
			}
		}

		return true;
	}
	
	//在存储过程回滚时发送客户端错误信息代号
	public void psendTitleErrWhileRollback(int errType){
		final STitleErr senderr = new STitleErr(errType);
		Procedure.psendWhileRollback(roleid, senderr);	
	}
}
