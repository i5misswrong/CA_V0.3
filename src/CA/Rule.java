package CA;



import java.util.Vector;



public class Rule  {
	Data data=new Data();
//	volatile int [][] mapMat=new int [data.w/data.pw][data.w/data.pw];
//	public int[][] getMapMat() {
//		return mapMat;
//	}
//	public void setMapMat(int[][] mapMat) {
//		this.mapMat = mapMat;
//	}
	
	
	public synchronized int[][] changeMapMat(int x,int y,int px,int py,int peoId,int mapMat[][]){
		mapMat[px/data.pw][py/data.pw]=peoId;
		mapMat[x/data.pw][y/data.pw]=0;
		return mapMat;
	}
	
	public synchronized int[][] isGOExit(int x,int y,int mapMat[][]){
		mapMat[0][data.exitY/data.pw]=0;
		return mapMat;
	}
	public synchronized int rePeoSize(Vector Peo){
		return Peo.size();
	}
	public synchronized boolean isNextPoint(int px,int py,int x,int y,int mapMat[][]){
		boolean flag=false;
		if(mapMat[px/data.pw][py/data.pw]!=0){
			flag=true;
		}
		return flag;
	}

}

