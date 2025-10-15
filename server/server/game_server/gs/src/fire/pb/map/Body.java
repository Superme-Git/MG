package fire.pb.map;

import fire.pb.map.Position;

public abstract class Body {

	private final long uniqueID;
	private final int kind;
	private long sceneId;
	private Position pos;

	public Body(long uniqueID, int kind) {
		this.uniqueID = uniqueID;
		this.kind = kind;
	}

	public long getUniqueID() {
		return uniqueID;
	}

	public int getKind() {
		return kind;
	}

	public boolean equals(Body o) {
		if (null == o)
			return false;

		if (kind == o.kind && uniqueID == o.uniqueID)
			return true;

		return false;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public long getScene() {
		return sceneId;
	}

	public int getMapId() {
		return (int) sceneId;
	}

	public void setScene(long sceneId) {
		this.sceneId = sceneId;
	}

	public void updateMapInfo(long sceneId, int posx, int posy, int posz) {
		synchronized (this) {
			setScene(sceneId);
			setPos(new Position(posx, posy, posz));
		}
	}

	public boolean checkDistance(Body o, int distance) {

		if (getScene() == 0 || o.getScene() == 0) {
			return false;
		}

		if (getScene() != o.getScene()) {
			return false;
		}

		if (getPos().getX() < 0 || o.getPos().getX() < 0) {
			return false;
		}

		if (getPos().getZ() != o.getPos().getZ()) {
			return false;
		}

		if (getPos().getDistance(o.getPos()) > distance) {
			return false;
		}

		return true;
	}

	public static final int TYPE_ROLE = 1;
	public static final int TYPE_NPC = 2;
	public static final int TYPE_PET = 3;
	public static final int TYPE_PICKUP = 4;
}
