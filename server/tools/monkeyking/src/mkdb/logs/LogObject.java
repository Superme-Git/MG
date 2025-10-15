package mkdb.logs;

import mkdb.Log;
import mkdb.LogKey;

public abstract class LogObject<T> extends Note implements Log {
	protected LogKey logkey;
	protected T _xdb_saved;

	protected LogObject(LogKey logkey, T saved) {
		this.logkey = logkey;
		this._xdb_saved = saved;
	}

	@Override
	public void commit() {
		mkdb.Logs.logNotify(logkey.getXBean(), new LogNotify(logkey, this));
	}
}
