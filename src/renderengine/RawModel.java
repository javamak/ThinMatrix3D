package renderengine;

public class RawModel {

	private int vaoID;
	private int vectorCount;

	public RawModel(int vaoID, int vectorCount) {
		super();
		this.vaoID = vaoID;
		this.vectorCount = vectorCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVectorCount() {
		return vectorCount;
	}

}
