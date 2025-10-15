package xmerge;

import java.io.File;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;
/**
 * 
 * ��ʱû���Ƕ��߳�֧��
 */


public class CheckDupKey implements ICheckDupKey {

	private String _errDB = XmergeConf.ERRKEYDIR;//���·���ķ���
	private TableStatistics _checkStatis= null;
	private boolean _hasDupKey = false;

	@Override
	public boolean checkDB(String srcDB, String dstDB) {
		//check srcDb dstDb is directory

		File src = new File(srcDB);
		File dst = new File(dstDB);
		if (srcDB.isEmpty() || !src.isDirectory() || !src.exists())
			throw new Error("SrcDb : " + srcDB + " (!isDirectory || !exists())");//��ʲô��������
		if (dstDB.isEmpty() || !dst.isDirectory() || !dst.exists())
			throw new Error("DstDb : " + dstDB + " (!isDirectory || !exists())");//��ʲô��������
		File err = new File(_errDB);
		if ( err.isFile() )
			throw new Error("ErrDb : " + _errDB + " is File???");
		if ( !err.exists() )
		{
			err.mkdir();//��������һ��errĿ¼
			new File(_errDB+"/dbdata").mkdir();
			new File(_errDB+"/dblogs").mkdir();
		}
		else
			if ( !xmerge.Util.clearDir(err) )//���������Ŀ¼�е������ļ�
			{
				throw new Error("ErrDb : " + _errDB + " can't delete???");
			}

		//ÿ��check�����³�ʼ����ͳ����Ϣ
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
				if ( XmergeConf.getInstance().isKnownConflict(tablename) )			
					continue;

				Dbx.Table stb = sdb.openTable(tablename);
				Dbx.Table dtb = ddb.openTable(tablename);
				Dbx.Table etb = errdb.openTable(tablename);


				//��getFirstKey(), getNextKey()�������
				try{
					Walker walker = new Walker(dtb, etb );
					stb._walk(walker); //stb������ʱ���Ƿ���Ҫ����checkpoint?
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

		private int _cnt = 0;
		private int _recordcnt = 0;
		final int _checkpoint = 100000;
		
		//OctetsStream value = new OctetsStream().marshal(0);//���е�errdb��value = 0;
		OctetsStream value = null;
		Walker(Dbx.Table dtb, Dbx.Table etb) {
			//this.tableMetaData = tableMetaData;
			_dtb = dtb;
			_etb = etb;			
		}

		@Override
		public boolean onRecord(byte[] _key, byte[] _data) {
			_cnt++;
			if ( _cnt >= _checkpoint )//����һ���޸ģ��ͷ�һ���ڴ�
			{
				_cnt = 0;
				_dtb.save();
			}
			
			
			OctetsStream key = OctetsStream.wrap(Octets.wrap(_key));
			value = _dtb._find(key);			
			if ( value != null )			
			{
				//record();				
				if ( _etb._insert(key, value ) == false )				
					throw new Error("onRecord : insert key to etb false???on panic����");
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
