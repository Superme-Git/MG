package fire.script;

import java.util.LinkedList;
import java.util.List;

import fire.pb.scene.sPos.Position;

public class SearchIsland
{
	
	public static void main(String[] args)
	{
		byte[][] blockinfo = new byte[20][30];
		for (int i = 0; i < blockinfo.length; i++)
		{
			for (int j = 0; j < blockinfo[i].length; j++)
			{
				if(Math.random() < 0.5)
					blockinfo[i][j] = 0;
				else
					blockinfo[i][j] = -1;
			}
		}
		printArray(blockinfo);
		processBlockInfo(blockinfo);
		printArray(blockinfo);
	}
	/**处理阻挡，找出孤岛和刷的不合法的单层斜向阻挡点*/
	public static void processBlockInfo(byte[][] blockinfo)
	{
		byte islandindex = 1;
		for(int x = 0; x < blockinfo.length;x++)
		{
			for(int y = 0; y < blockinfo[x].length;y++)
			{
				if(blockinfo[x][y] != 0)
					continue;
				blockinfo[x][y] = islandindex;
				//processIsland(blockinfo,x,y,islandindex);
				List<Position> poses = new LinkedList<Position>();
				poses.add(new Position(x, y));
				while(!poses.isEmpty())
				{
					poses = processAdjacentPoses(blockinfo, poses, islandindex);
				}
				islandindex++;
			}
		}
		System.out.println("本地图一共有 "+(islandindex - 1)+" 个孤岛");
	}
	
	/**从一个非阻挡点深度遍历寻找该点所处的岛*/
	public static void processIsland(byte[][] blockinfo,int x, int y, byte islandindex)
	{
		int maxx = blockinfo.length;
		int maxy = blockinfo[x].length;
		for(Position pos : getAdjacentPos(x, y, maxx, maxy))
		{
			int tmpx = pos.getX();
			int tmpy = pos.getY();
			if(blockinfo[tmpx][tmpy] != 0)
				continue;
			if(!validPos(blockinfo, x, y, tmpx, tmpy))
				continue;
			blockinfo[tmpx][tmpy] = islandindex;
			processIsland(blockinfo, tmpx, tmpy, islandindex);
		}
	}
	
	/**不合法的单层斜向阻挡点，即如下的阻挡是非法的，不应该出现的情况*/
	public static boolean validPos(byte[][] blockinfo, int x, int y, int tmpx, int tmpy)
	{
		if(x == tmpx || y == tmpy)
			return true;
		if(blockinfo[x][tmpy] == -1 && blockinfo[tmpx][y] == -1)
		{
			System.out.println("坐标("+ x +","+ y+")与坐标(" +tmpx +","+ tmpy +")位置不合法");
			return false;
		}
		return true;
	}
	
	/**获取邻近点，最多八个*/
	public static java.util.List<Position> getAdjacentPos(int x, int y,int maxx,int maxy){
		final java.util.List<Position> ret  = new java.util.ArrayList<Position>();
		for(int i=x-1; i<=x+1; i++){
			for(int j=y-1; j<=y+1; j++)
			{
				if(i>=0 && i < maxx && j>=0  && j < maxy)
				{
					if(i==x && j==y)
						continue;
					ret.add(new Position(i, j));
				}
			}
		}
		return ret;
	}
	
	public static java.util.List<Position> processAdjacentPoses(byte[][] blockinfo, java.util.List<Position> poses, byte islandindex)
	{
		int maxx = blockinfo.length;
		int maxy = blockinfo[0].length;
		final java.util.List<Position> adjposes  = new java.util.ArrayList<Position>();
		for(Position pos : poses)
		{
			for(Position adjpos : getAdjacentPos(pos.getX(), pos.getY(), maxx, maxy))
			{
				int tmpx = adjpos.getX();
				int tmpy = adjpos.getY();
				if(blockinfo[tmpx][tmpy] != 0)
					continue;
				if(!validPos(blockinfo, pos.getX(), pos.getY(), tmpx, tmpy))
					continue;
				blockinfo[tmpx][tmpy] = islandindex;
				adjposes.add(adjpos);
			}
		}
		return adjposes;
	}
	
	/**打印数组*/
	public static void printArray(byte[][] array)
	{
		for(int j = 0; j < array[0].length; j++)
		{
			String str = "";
			for(int i = 0; i < array.length; i++)
			{
				if(array[i][j] >= 0 && array[i][j] < 10)
					str += " " + array[i][j] +" ";
				else
					str += array[i][j] +" ";
			}
			System.out.println(str);
		}
	}
}
