package mkdb.logs;

import mkdb.Log;
import mkdb.LogKey;

public abstract class LogShort extends Note implements Log {
	protected LogKey logkey;
	protected short _xdb_saved;

	protected LogShort(LogKey logkey, short saved) {
		this.logkey = logkey;
		this._xdb_saved = saved;
	}

	@Override
	public void commit() {
		mkdb.Logs.logNotify(logkey.getXBean(), new LogNotify(logkey, this));
	}
}
