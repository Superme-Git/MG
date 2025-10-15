package xmerge;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;
/**
 * 
 * @author Cai jiacheng
 * @note 暂时没考虑多线程支持
 */


public class CheckDupKey implements ICheckDupKey {

	private String _errDB = XmergeConf.ERRKEYDIR;//相对路径的访问
	private TableStatistics _checkStatis= null;
	private boolean _hasDupKey = false;

	@Override
	public boolean checkDB(String srcDB, String dstDB) {
		//check srcDb dstDb is directory

		File src = new File(srcDB);
		File dst = new File(dstDB);
		if (srcDB.isEmpty() || !src.isDirectory() || !src.exists())
			throw new Error("SrcDb : " + srcDB + " (!isDirectory || !exists())");//扔什么东东进来
		if (dstDB.isEmpty() || !dst.isDirectory() || !dst.exists())
			throw new Error("DstDb : " + dstDB + " (!isDirectory || !exists())");//扔什么东东进来
		File err = new File(_errDB);
		if ( err.isFile() )
			throw new Error("ErrDb : " + _errDB + " is File???");
		if ( !err.exists() )
		{
			err.mkdir();//不存在则建一个err目录
			new File(_errDB+"/dbdata").mkdir();
			new File(_errDB+"/dblogs").mkdir();
		}
		else
			if ( !xmerge.Util.clearDir(err) )//存在则清空目录中的所有文件
			{
				throw new Error("ErrDb : " + _errDB + " can't delete???");
			}

		//每次check都重新初始化下统计信息
		_checkStatis = new TableStatistics();
		_hasDupKey = false;
		
		//check the tables
		
		//Dbx.start(".");
		Dbx sdb = Dbx.open(src, DatabaseMetaData.getInstance());
		Dbx ddb = Dbx.open(dst, DatabaseMetaData.getInstance());
		Dbx errdb = Dbx.open(err, DatabaseMetaData.getInstance());

		try
		{
			//for table : src_tables
			for (DatabaseMetaData.Table tableMetaData : sdb.getMetaData().getTables()) {
				if (!tableMetaData.isPersistence())
					continue;
				//open src
				String tablename = tableMetaData.getName();

				//check is know-conflict				
				if ( XmergeConf.getInstance().isKnownConflict(tablename) )	{
					System.out.println("known conflict table:"+tablename);
					continue;
				}

				Dbx.Table stb = sdb.openTable(tablename);
				Dbx.Table dtb = ddb.openTable(tablename);
				Dbx.Table etb = errdb.openTable(tablename);

				//用getFirstKey(), getNextKey()更方便吧
				try{
					Walker walker = new Walker(dtb, etb);
					stb._walk(walker); //stb遍历是时候是否需要考虑checkpoint?
					walker.finish();
				}finally
				{
					stb.close();
					dtb.close();
					etb.close();
				}

			}

		}finally
		{
			sdb.close();
			ddb.close();
			errdb.close();					
			
		}
	
		return _hasDupKey;
	}

	@Override
	public String getDupKeyDBPath() {
		return _errDB;
	}

	private class Walker implements mkdb.Storage.IWalk {

		private Dbx.Table _dtb = null;
		private Dbx.Table _etb = null;
		
		private Set<String> conflictTableNames = new HashSet<String>();

		private int _cnt = 0;
		private int _recordcnt = 0;
		final int _checkpoint = 100000;
		
		//OctetsStream value = new OctetsStream().marshal(0);//所有的errdb的value = 0;
		OctetsStream value = null;
		Walker(Dbx.Table dtb, Dbx.Table etb) {
			//this.tableMetaData = tableMetaData;
			_dtb = dtb;
			_etb = etb;			
		}

		@Override
		public boolean onRecord(byte[] _key, byte[] _data) {
			_cnt++;
			if ( _cnt >= _checkpoint )//保存一下修改，释放一下内存
			{
				_cnt = 0;
				_dtb.save();
			}
			
			
			OctetsStream key = OctetsStream.wrap(Octets.wrap(_key));
			value = _dtb._find(key);			
			if ( value != null )			
			{
				//record();		
				if (!conflictTableNames.contains(_dtb.getName())) {
					conflictTableNames.add(_dtb.getName());
					
					try {
						long keyvalue=key.unmarshal_long();
						System.out.println(_dtb.getName()+"111_duplicatekey:"+keyvalue);
					} catch (Exception e) {
						int keyvalue;
						try {
							keyvalue = key.unmarshal_int();
							System.out.println(_dtb.getName()+"222_duplicatekey:"+keyvalue);
						} catch (MarshalException e1) {
							e1.printStackTrace();
						}
					}
				}
				if ( _etb._insert(key, value ) == false )				
					throw new Error("onRecord : insert key to etb false???on panic……");
				_recordcnt++;
				try {
					
					CheckDupKey.this._checkStatis.incrementCount(_dtb.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if ( _recordcnt >= _checkpoint )
				{
					_recordcnt = 0;
					_etb.save();
				}
			}
			return true;
		}

		public void finish() {
			
			try {
				if( CheckDupKey.this._checkStatis.getCount(_dtb.getName())!= 0 ) 
				{
					CheckDupKey.this._hasDupKey = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			_dtb = null;
			_etb = null;

					
		}

	}

	@Override
	public long getDupKeyCount(String tablename) throws Exception {
		return _checkStatis.getCount(tablename);
	}

	@Override
	public void setDupKeyDBPath(String path) {
		_errDB = path;
		
	} 

}
