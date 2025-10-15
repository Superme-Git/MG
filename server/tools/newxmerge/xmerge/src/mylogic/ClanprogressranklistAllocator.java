package mylogic;

import java.util.Random;

import xmerge.IAllocator;
import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class ClanprogressranklistAllocator implements IAllocator {

	@Override
	public Object allocateKey(String tableName, Object oldKey) {
		// TODO Auto-generated method stub
		return new Random().nextInt();
	}
	
}