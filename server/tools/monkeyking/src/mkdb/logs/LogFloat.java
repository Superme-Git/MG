package mkdb.logs;

import mkdb.Log;
import mkdb.LogKey;

public abstract class LogFloat extends Note implements Log {
	protected LogKey logkey;
	protected float _xdb_saved;

	protected LogFloat(LogKey logkey, float saved) {
		this.logkey = logkey;
		this._xdb_saved = saved;
	}

	@Override
	public void commit() {
		mkdb.Logs.logNotify(logkey.getXBean(), new LogNotify(logkey, this));
	}

	@Override
	public String toString() {
		return String.valueOf(this._xdb_saved);
	}
}
