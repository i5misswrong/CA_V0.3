package CA;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Peo implements Runnable{
	Data data=new Data();
	int x;//行人坐标
	int y;
	int direction;//行人方向
	int speed=data.speed;//行人速度
	volatile int mapMat[][];//行人坐标数组 volatile锁死线程
	int viewMat[][];
	
	
	Vector<Peo> people =new Vector<Peo>();//行人集合
	Vector<Peo> peoMemPointVector=new Vector<Peo>();
	
	Map<Integer,Double> inCome=new HashMap<Integer,Double>();//收益参数
	public Peo(int x,int y){
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
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int[][] getMapMat() {
		return mapMat;
	}
	public void setMapMat(int[][] mapMat) {
		this.mapMat = mapMat;
	}
	public Vector<Peo> getPeople() {
		return people;
	}
	public void setPeople(Vector<Peo> people) {
		this.people = people;
	}
	public Map<Integer, Double> getInCome() {
		return inCome;
	}
	public void setInCome(Map<Integer, Double> inCome) {
		this.inCome = inCome;
	}
	public Vector<Peo> getPeoMemPointVector() {
		return peoMemPointVector;
	}
	public void setPeoMemPointVector(Vector<Peo> peoMemPointVector) {
		this.peoMemPointVector = peoMemPointVector;
	}
	public int[][] getViewMat() {
		return viewMat;
	}
	public void setViewMat(int[][] viewMat) {
		this.viewMat = viewMat;
	}
	//--------------行人移动方法---------------------
	public void moveLeftUp(){
		y-=speed;
		x-=speed;
	}
	public void moveUp(){
		y-=speed;
	}
	public void moveRightUp(){
		y-=speed;
		x+=speed;
	}
	public void moveLeft(){
		x-=speed;
	}
	public void moveOnSite(){
		
	}
	public void moveRight(){
		x+=speed;
	}
	public void moveLeftDown(){
		x-=speed;
		y+=speed;
	}
	public void moveDown(){
		y+=speed;
	}
	public void moveRightDown(){
		x+=speed;
		y+=speed;
	}
	public void peoMove(int direction){
		switch (direction){
		case 1:
			moveLeftUp();
			break;
		case 2:
			moveUp();
			break;
		case 3:
			moveRightUp();
			break;
		case 4:
			moveLeft();
			break;
		case 5:
			moveOnSite();
			break;
		case 6:
			moveRight();
			break;
		case 7:
			moveLeftDown();
			break;
		case 8:
			moveDown();
			break;
		case 9:
			moveRightDown();
			break;
		}	
	}
	//-------------------计算收益参数-------------
	
	
	//--------------获取视野半径的元胞----------
	public int [][] getViewRadiusCellula(){
		int viewRadiusCellula [][]=new int [data.view][data.view];
		
		return viewRadiusCellula;
	}
	//-------------计算角度----------------
	//计算行人点与记忆点的角度
	public double countPeoAndViewPoint(){
		int distanceStandard;//水平差  j和y
		int distanceVertical;//垂直差  i和x
		double thetaRadian=0; //所求得的反正切值 弧度值
		double thetaAngle=0;//反正切值 角度值
		for (int i=0;i<data.w/data.pw;i++){//循环记忆点矩阵
			for (int j=0;j<data.w/data.pw;j++){
				if(viewMat[i][j]==data.viewMatrixNum){//如果找到记忆点
					
					distanceStandard=Math.abs(j-y/data.pw);//水平差和垂直差的绝对值
					distanceVertical=Math.abs(i-x/data.pw);
					System.out.println(distanceStandard+"-------"+distanceVertical);
					if(i==x||j==y){//如果在坐标轴上
						System.out.println("在一条直线上");
					}
					//位于一三象限的角度为正常坐标下的角度
					else if((j-y<0&&i-x>0)||(j-y>0&&i-x<0)){//
						System.out.println("位于第一三象限");
						
						thetaRadian=Math.atan(distanceVertical/distanceStandard);//直接求反正切
					
					}
					//位于二四象限下的角度为正常坐标的角度-90度
					else{
						System.out.println("位于第二四象限");
						thetaRadian=Math.atan((distanceVertical/distanceStandard));//求反正切后去相反数
						thetaAngle=180/Math.PI*thetaRadian;
					}
				}
			}
		}
		return thetaAngle;
	}
	//计算视野角  其中 逆时针旋转的角
	public double countClockWise(double theta){
		double alpha=0;
		alpha=theta+data.viewAngle;
		return alpha;
	}
	//计算视野角  顺时针旋转的角
	public double countAntiClockWise(double theta){
		double alpha=0;
		alpha=theta-data.viewAngle;//这里需要考虑万一是负值怎么办
		return alpha;
	}
	//---------------计算所占区域百分比------------------
	public void countAreaPercentage(double alpha01,double alpha02){
		
	}
	public void run(){
		int memPointX[]=new int [data.memPointNum];
		int memPointY[]=new int [data.memPointNum];
		System.out.println("program start");
		try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		
		while(true){
			System.out.println("whill start");
			for (int i=0;i<data.w/data.pw;i++){
				for (int j=0;j<data.w/data.pw;j++){
					if(viewMat[i][j]==data.viewMatrixNum){
						int distanceX=Math.abs(x/data.pw-i);
						int distanceY=Math.abs(y/data.pw-j);
						double distanceXY=Math.sqrt((Math.pow(distanceX, 2)+Math.pow(distanceY, 2)));
						if(distanceXY<data.view){
							System.out.println("进入视野半径");
							countPeoAndViewPoint();
						}
						//int viewAndPeoDistance=Math.abs(arg0)
					}
				}
			}
			
			
			try {
	            Thread.sleep(1000);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		
	}
	
	
	
}
