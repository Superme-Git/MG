package fire.pb.scene.config;

import fire.pb.scene.sPos.Position;

/**
 * 配置的NPC信息
 *
 */
public class NpcDesc {	
	@Override
	public String toString(){
		return id + "\t" + "("+ posx + "," +posy +")\t" + dir; 
	}
	public void setid(int id){
		this.id = id;
	}
	
	public void setposx(int posx){
		this.posx = posx;
	}
	
	public void setposy(int posy){
		this.posy = posy;
	}
	
	public void setdir(int dir){
		this.dir = dir;
	}
	
	public int getid(){
		return id;
	}
	
	public int getposx(){
		return posx;
	}
	
	public int getposy(){
		return posy;
	}
	
	public Position getPos(){
		return new Position(posx, posy);
	}
	
	public int getdir(){
		return dir;
	}
	
	private int id;
	private int posx;
	private int posy;
	private int dir;
}
