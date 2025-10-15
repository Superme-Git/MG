package fire.pb.scene;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 地图上的布怪信息
 * @author nobody
 *
 */
public class MonsterInfo {
	
	MonsterInfo(int mapID){
		this.mapID = mapID;
	}
	
	public int getMonsterConfig(int x, int y){
		Integer ret = infos.get((x<<16) + y);
		if(null == ret)
			return 0;
		
		return ret;
	}
	
	public class Pos {
		public final int x;
		public final int y;
		public Pos( int x, int y ) {
			this.x = x;
			this.y = y;
		}
	}
	
	public void load(){
		final String fileName = getFileName();
		File f = new File(fileName);
		if(! f.exists())
			return;
		
		try
		{
			
			final DataInputStream in = new DataInputStream(
					new BufferedInputStream( new FileInputStream(
							fileName						
							))
					);			
			
			//此处读整数要按字节翻转
			final int headLen = Integer.reverseBytes(in.readInt());
			if(headLen != this.MIN_FILE_SIZE) {
				in.close();
				throw new Exception("初始化地图文件大小错误");
			}
			
			width = Integer.reverseBytes(in.readInt());
			highth = Integer.reverseBytes(in.readInt());
			
			if(width <1 || highth<1) 
			{
				in.close();
				throw new Exception("初始化布怪文件数值错误");		
			}
			final int count = Integer.reverseBytes(in.readInt());
			for(int i=0; i<count; i++)
			{
				int pos = Integer.reverseBytes(in.readInt());
				int val = Integer.reverseBytes(in.readInt());
				infos.put(pos, val);
				
				if(val < 0){  			// 明雷
					int pos_x = pos >>> 16;
					int pos_y = pos & 0x0000ffff;
					Set<Pos> area = monstershowArea.get( -val );
					if ( area == null ) {
						area = new HashSet<Pos>();
						monstershowArea.put( -val, area );
					}
					area.add( new Pos( pos_x, pos_y ) );
				}
			}
			in.close();
		}catch(final Exception e)	{
			Scene.LOG.info(e);	
		}		
		
	}

	private String getFileName(){
		return "gamedata/map/" + mapID + "/monster.dat";
	}
	
	private int mapID = 0;
	private int width = 0;
	private int highth = 0;
	
	/**
	 * 布怪数值管理, map<坐标索引值, 布怪类型值>
	 * 坐标索引值 为32位整型数, 高16位为x坐标值, 低16位为y坐标值
	 */
	private final java.util.Map<Integer, Integer> infos = new java.util.HashMap<Integer, Integer>();
	
	/**
	 * 地图文件头数据大小, 文件格式为
	 * 文件头大小(int) 地图宽度(int) 地图高度(int) 布怪坐标点个数(int) 坐标点索引值(int) 坐标点布怪类型(byte) 
	 */
	private final int MIN_FILE_SIZE = 16;
	
	private final Map<Integer, Set<Pos> > monstershowArea = 
		new HashMap<Integer, Set<Pos> >();// 明雷怪
	public Map<Integer, Set<Pos> > getMonstershowArea() { return monstershowArea;}
}
