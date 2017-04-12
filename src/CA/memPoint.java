package CA;

import java.util.Vector;

public class memPoint {
	Data data=new Data();
	int x;
	int y;
	Vector<memPoint> mPointVector=new Vector<memPoint>();
	
	public memPoint(int x,int y){
		this.x=x;
		this.y=y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Vector<memPoint> getmPointVector() {
		return mPointVector;
	}
	public void setmPointVector(Vector<memPoint> mPointVector) {
		this.mPointVector = mPointVector;
	}
	
}
