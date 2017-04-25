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
	int viewMat[][];//记忆点所在位置的矩阵
	double viewInComeK[][]=new double [data.w/data.pw][data.w/data.pw];//视野收益矩阵
	double viewInComeK1[][]=new double [data.w/data.pw][data.w/data.pw];//k1下的收益矩阵
	double viewInComeK2[][]=new double [data.w/data.pw][data.w/data.pw];//k2下的收益矩阵
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
	public double[][] getViewInComeK1() {
		return viewInComeK1;
	}
	public void setViewInComeK1(double[][] viewInComeK1) {
		this.viewInComeK1 = viewInComeK1;
	}
	public double[][] getViewInComeK2() {
		return viewInComeK2;
	}
	public void setViewInComeK2(double[][] viewInComeK2) {
		this.viewInComeK2 = viewInComeK2;
	}
	public double[][] getViewInComeK() {
		return viewInComeK;
	}
	public void setViewInComeK(double[][] viewInComeK) {
		this.viewInComeK = viewInComeK;
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
	//-------------------计算视野收益参数-------------
	//---------判断格子位于行人和出口的相对位置   只取格子位于行人和出口之间的
	public boolean isGridAndExitPeo(int i,int j){
		boolean flag=false;
		if (data.exitX==0 && data.exitY==0){
			//出口位于右下角
		}
		else if(data.exitX==data.w && data.exitY==data.w){
			//出口位于左上角
		}
		else if(data.exitX==0){
			if(i<x/data.pw){
				flag=true;
			}
		}
		else if(data.exitY==0){
			if(j<y/data.pw){
				flag=true;
			}
		}
		else if(data.exitX==data.w){
			if(i>x/data.pw){
				flag=true;
			}
		}
		else if(data.exitY==data.w){
			if(j>y/data.pw){
				flag=true;
			}
		}
		return flag;
	}
	//------------判断出口在行人的什么位置 第几象限-----
	public int judgePeoAndExit(){
		int quadrant=0;
		if(x<data.exitX & y<data.exitY){
			quadrant=1;//第一象限
		}
		else if(x<data.exitX & y>data.exitY){
			quadrant=2;//第二象限
		}
		else if(x>data.exitX & y>data.exitY){
			quadrant=3;//第三象限
		}
		else if(x<data.exitX & y>data.exitY){
			quadrant=4;//第四象限
		}
		return quadrant;
	}
	//----------------计算行人点与记忆点的角度
	public double countPeoAndExit(){
		double distanceStandard=data.exitX-x;//水平差  x和i
		double distanceVertical=data.exitY-y;//垂直差  y和j
		double thetaRadian; //所求得的反正切值 弧度值
		double thetaAngle;//反正切值 角度值
		double k;//行人与出口构成的直线的斜率
		int quadrant=0;
		k=distanceVertical/distanceStandard;
		thetaRadian=Math.atan(k);
		thetaAngle=180/Math.PI*thetaRadian;
		quadrant=judgePeoAndExit();
		switch (quadrant) {
		case 1:
			thetaAngle=180/Math.PI*thetaRadian;
			break;
		case 2:
			thetaAngle=180/Math.PI*thetaRadian+90;
			break;
		case 3:
			thetaAngle=180/Math.PI*thetaRadian+180;
			break;
		case 4:
			thetaAngle=180/Math.PI*thetaRadian+270;
			break;

		default:
			//其他象限
			break;
		}
		return thetaAngle;
	}
	//计算视野角  其中 逆时针旋转的角  的斜率
	public double countClockWise(double theta){
		double alpha=0;
		double alphaRadian=0;
		double k1=0;//逆时针增加角度后的斜率
		alpha=theta+data.viewAngle;
		alphaRadian=Math.PI/180*alpha;
		k1=Math.tan(alphaRadian);
		return k1;
	}
	//计算视野角  顺时针旋转的角 的斜率
	public double countAntiClockWise(double theta){
		double alpha=0;
		double k2=0;//顺时针减小角度后的斜率
		double alphaRadian=0;
		alpha=theta-data.viewAngle;//这里需要考虑万一是负值怎么办
		alphaRadian=Math.PI/180*alpha;
		k2=Math.tan(alphaRadian);
		return k2;
	}
	//------k1 计算 角 的线 与周围格子 相交与那些边--------
	public void countAngleAndNearbyGrid(double k1){
		double resultY1; //R 直线与格子的交点 Y1为带入格子的 x左的值 即4边上的y值
		double resultY3; //2边上的y值
		double resultX1;//1边上的x值
		double resultX3;//3边上的x值
		for (int i=x/data.pw-data.view+1;i<x/data.pw+data.view;i++){//遍历视野内的矩阵
			for (int j=y/data.pw-data.view+1;j<y/data.pw+data.view;j++){
				if(i<0 || j<0 || i>=data.w/data.pw || j>=data.w/data.pw){
					//此处越界
				}
				else {
					int D1=0; //D1234 为 直线与格子的那条边相交
					int D2=0;
					int D3=0;
					int D4=0;
					double upD=0;//梯形上底
					double downD=0;//梯形下底
					double LDx=0;//三角形 水平方向差
					double LDy=0;//三角形 垂直方向差
					if(i==x/data.pw & j==y/data.pw){//格子与行人点重合
						//System.out.println("这里是行人点");
					}
					else {
						//计算R 格子与直线的相交坐标
						resultY1=k1*(i-x/data.pw)+y/data.pw;
						resultY3=k1*(i-x/data.pw+1)+y/data.pw;
						resultX1=(j-y/data.pw)/k1+x/data.pw;
						resultX3=(j-y/data.pw+1)/k1+x/data.pw;
						if(resultY1<=j+1 & resultY1>j){
							D4=1;//4号边
						}
						if(resultY3<=j+1 & resultY3>j){
							D2=1;//2号边
						}
						if(resultX1>=i & resultX1<i+1){
							D3=1;//3号边
						}
						if(resultX3>=i & resultX3<i+1){
							D1=1;//1号边
						}
						//--------------k1对边--------------------
						if(D1==1 & D3==1 || D2==1 & D4==1){// 13 和24 对边情况下
							if(isGridAndExitPeo(i, j)){//判断该点是否位于行人点和出口点之间
								if(i<x/data.pw){
									//-----------------k1对边格子位于左边----------
									if(D1==1 & D3==1){//13对边
										if(k1<0){
											upD=Math.abs(i+1-resultX1);
											downD=Math.abs(i+1-resultX3);
											viewInComeK1[i][j]=countTrapeS(upD, downD);
										}
										else if(k1>0){
											upD=Math.abs(i+1-resultX1);
											downD=Math.abs(i+1-resultX3);
											viewInComeK1[i][j]=1-countTrapeS(upD, downD);
										}
										else{
											System.out.println("13对边左边k=0");
										}
									}
									else if(D2==1 & D4==1){//24对边
										upD=Math.abs(j+1-resultY1);
										downD=Math.abs(j+1-resultY3);
										viewInComeK1[i][j]=countTrapeS(upD, downD);
									}
									else{//其他？
										System.out.println("对边左边有问题");
									}
								}
								//------------------k1对边格子位于右边-----------------
								else {
									if(D1==1 & D3==1){//13对边
										if(k1<0){
											upD=Math.abs(i+1-resultX1);
											downD=Math.abs(i+1-resultX3);
											viewInComeK1[i][j]=1-countTrapeS(upD, downD);
										}
										else if(k1>0){
											upD=Math.abs(i+1-resultX1);
											downD=Math.abs(i+1-resultX3);
											viewInComeK1[i][j]=countTrapeS(upD, downD);
										}
									}
									else if(D2==1 & D4==1){//24对边
										upD=Math.abs(j+1-resultY1);
										downD=Math.abs(j+1-resultY3);
										viewInComeK1[i][j]=1-countTrapeS(upD, downD);
									}
									else{//其他？
										System.out.println("对边右边有问题");
									}
								}
							}
							else{
								//System.out.println("这是对边反方向");
							}
						}
						//--------------k1邻边--------------------
						else if(D1==1 & D2==1 || D2==1 & D3==1 || D3==1 & D4==1 || D4==1 & D1==1){
							if(isGridAndExitPeo(i, j)){//判断格子是否位于行人点和出口之间
								//-------k1领边格子位于左边---------------
								if(i<x/data.pw){//格子位于左边
									if(D1==1 & D2==1){//12领边
										LDx=Math.abs(i+1-resultX3);
										LDy=Math.abs(j-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK1[i][j]=1-trS;
									}
									else if(D2==1 & D3==1){//23邻边
										LDx=Math.abs(i+1-resultX3);
										LDy=Math.abs(j+1-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK1[i][j]=trS;
									}
									else if(D3==1 & D4==1){//34领边
										LDx=Math.abs(i-resultX3);
										LDy=Math.abs(j+1-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK1[i][j]=trS;
										
									}
									else if(D4==1 & D1==1){//41领边
										LDx=Math.abs(i-resultX3);
										LDy=Math.abs(j-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK1[i][j]=1-trS;
										
									}
									else{//其他？
										System.out.println("领边左边有问题");
									}
								}
								//-------k1领边格子位于右边---------------
								else {//格子位于右边
									if(D1==1 & D2==1){//12领边
										LDx=Math.abs(i+1-resultX3);
										LDy=Math.abs(j-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK1[i][j]=trS;
									}
									else if(D2==1 & D3==1){//23邻边
										LDx=Math.abs(i+1-resultX3);
										LDy=Math.abs(j+1-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK1[i][j]=1-trS;
									}
									else if(D3==1 & D4==1){//34领边
										LDx=Math.abs(i-resultX3);
										LDy=Math.abs(j+1-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK1[i][j]=1-trS;
									}
									else if(D4==1 & D1==1){//41领边
										LDx=Math.abs(i-resultX3);
										LDy=Math.abs(j-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK1[i][j]=trS;
									}
									else{//其他？
										System.out.println("领边右边有问题");
									}
								}
							}
							else{
								//System.out.println("这是邻边反方向");
							}
						}
					}
				}
			}
		}
	}
	//------k2 计算 角 的线 与周围格子 相交与那些边--------
	public void countAngleAndNearbyGridK2(double k2){
		//此方法 为计算k2 既顺时针旋转后的角的线   与k1相反
		double resultY1;
		double resultY3; 
		double resultX1;
		double resultX3;
		for (int i=x/data.pw-data.view+1;i<x/data.pw+data.view;i++){//遍历矩阵
			for (int j=y/data.pw-data.view+1;j<y/data.pw+data.view;j++){
				if(i<0 || j<0 || i>=data.w/data.pw || j>=data.w/data.pw){
					//越界
				}
				else {
					int D1=0; //D1234 为 直线与格子的那条边相交
					int D2=0;
					int D3=0;
					int D4=0;
					double upD=0;//梯形上底
					double downD=0;//梯形下底
					double LDx=0;//三角形 水平方向差
					double LDy=0;//三角形 垂直方向差
					if(i==x/data.pw & j==y/data.pw){//格子与行人点重合
						//System.out.println("这里是行人点");
						viewInComeK2[i][j]=0;
					}
					else {
						//计算R 格子与直线的相交坐标
						resultY1=k2*(i-x/data.pw)+y/data.pw;
						resultY3=k2*(i-x/data.pw+1)+y/data.pw;
						resultX1=(j-y/data.pw)/k2+x/data.pw;
						resultX3=(j-y/data.pw+1)/k2+x/data.pw;
						if(resultY1<=j+1 & resultY1>j){//4号边
							D4=1;
						}
						if(resultY3<=j+1 & resultY3>j){//2号边
							D2=1;
						}
						if(resultX1>=i & resultX1<i+1){//3号边
							D3=1;
						}
						if(resultX3>=i & resultX3<i+1){//1号边
							D1=1;
						}
						//------------k2对边--------------------
						if(D1==1 & D3==1 || D2==1 & D4==1){// 13 和24 对边情况下
							if(isGridAndExitPeo(i, j)){
								//System.out.println("这是对边");
								//--------k2对边格子位于左边-------------
								if(i<x/data.pw){
									if(D1==1 & D3==1){//13对边
										if(k2<0){
											upD=Math.abs(i+1-resultX1);
											downD=Math.abs(i+1-resultX3);
											viewInComeK2[i][j]=1-countTrapeS(upD, downD);
										}
										else if(k2>0){
											upD=Math.abs(i+1-resultX1);
											downD=Math.abs(i+1-resultX3);
											viewInComeK2[i][j]=countTrapeS(upD, downD);
										}
										else{
											System.out.println("13对边左边k=0");
										}
									}
									else if(D2==1 & D4==1){//24对边
										upD=Math.abs(j-resultY1);
										downD=Math.abs(j-resultY3);
										viewInComeK2[i][j]=countTrapeS(upD, downD);
									}
									else{//其他？
										System.out.println("对边左边有问题");
									}
								}
								//-*--------k2对边格子位于右边
								else {
									if(D1==1 & D3==1){//13对边
										if(k2<0){
											upD=Math.abs(i-resultX1);
											downD=Math.abs(i-resultX3);
											viewInComeK2[i][j]=1-countTrapeS(upD, downD);
										}
										else if(k2>0){
											upD=Math.abs(i+1-resultX1);
											downD=Math.abs(i+1-resultX3);
											viewInComeK2[i][j]=1-countTrapeS(upD, downD);
										}
									}
									else if(D2==1 & D4==1){//24对边
										upD=Math.abs(j+1-resultY1);
										downD=Math.abs(j+1-resultY3);
										viewInComeK2[i][j]=countTrapeS(upD, downD);
									}
									else{//其他？
										System.out.println("对边右边有问题");
									}
								}
							}
							else{
								//System.out.println("这是对边反方向");
							}
							
						}
						//------------k2领边---------------------
						else if(D1==1 & D2==1 || D2==1 & D3==1 || D3==1 & D4==1 || D4==1 & D1==1){
							if(isGridAndExitPeo(i, j)){
								//----------k2领边格子位于左边--------
								if(i<x/data.pw){//格子位于左边
									if(D1==1 & D2==1){//12领边
										LDx=Math.abs(i+1-resultX3);
										LDy=Math.abs(j-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK2[i][j]=trS;
									}
									else if(D2==1 & D3==1){//23邻边
										LDx=Math.abs(i+1-resultX3);
										LDy=Math.abs(j+1-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK2[i][j]=1-trS;
									}
									else if(D3==1 & D4==1){//34领边
										LDx=Math.abs(i-resultX3);
										LDy=Math.abs(j+1-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK2[i][j]=1-trS;
									}
									else if(D4==1 & D1==1){//41领边
										LDx=Math.abs(i-resultX3);
										LDy=Math.abs(j-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK2[i][j]=trS;
									}
									else{//其他？
										System.out.println("领边左边有问题");
									}
								}
								//-----------k2领边格子位于右边---------------
								else {//格子位于右边
									if(D1==1 & D2==1){//12领边
										LDx=Math.abs(i+1-resultX3);
										LDy=Math.abs(j-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK2[i][j]=trS;
									}
									else if(D2==1 & D3==1){//23邻边
										LDx=Math.abs(i+1-resultX3);
										LDy=Math.abs(j+1-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK2[i][j]=1-trS;
									}
									else if(D3==1 & D4==1){//34领边
										LDx=Math.abs(i-resultX3);
										LDy=Math.abs(j+1-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK2[i][j]=1-trS;
									}
									else if(D4==1 & D1==1){//41领边
										LDx=Math.abs(i-resultX3);
										LDy=Math.abs(j-resultY3);
										double trS=countTrianS(LDy, LDx);
										viewInComeK2[i][j]=trS;
									}
									else{//其他？
										System.out.println("领边右边有问题");
									}
								}
							}
							else{
								//System.out.println("这是邻边反方向");
							}
						}
					}
				}
			}
		}
	}
	//----------计算梯形面积---------------
	public double countTrapeS(double up,double down){
		double S=(up+down)/2;
		return S;
	}
	//---------计算三角形面积--------------
	public double countTrianS(double Ly,double Lx){
		double S=Ly*Lx/2;
		return S;
	}
	//--------判断格子是否完全位于k1和k2之间----------
	public boolean isCompleteInAngle(double k1,double k2,int i,int j){
		boolean flag=false;
		boolean f1=false;
		boolean f2=false;
		if (isGridAndExitPeo(i, j)){
			if(i>x/data.pw-data.view & i<x/data.pw+data.view & j>y/data.pw-data.view & j<y/data.pw+data.view){
				if(i<x/data.pw){
					if(j>k1*(i-x/data.pw)+y/data.pw & j>k1*(i+1-x/data.pw)+y/data.pw){
						f1=true;
					}
					if(j<k2*(i-x/data.pw)+y/data.pw-1 & j<k2*(i+1-x/data.pw)+y/data.pw-1){
						f2=true;
					}
				}
				else{
					if(j<k1*(i-x/data.pw)+y/data.pw-1 & j<k1*(i+1-x/data.pw)+y/data.pw-1){
						f1=true;
					}
					if(j>k2*(i-x/data.pw)+y/data.pw & j>k2*(i+1-x/data.pw)+y/data.pw){
						f2=true;
					}
				}
				if(f1 & f2){
					flag=true;
				}
			}
		}
		return flag;
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
		System.out.println(x+"pp"+y);
		
//		while(true){
		for (int ppp=0;ppp<1;ppp++){
			System.out.println("whill start");
			for (int i=0;i<data.w/data.pw;i++){//此处是检测是否到达记忆点 
				for (int j=0;j<data.w/data.pw;j++){
					if(viewMat[i][j]==data.viewMatrixNum){
						int distanceX=Math.abs(x/data.pw-i);
						int distanceY=Math.abs(y/data.pw-j);
						double distanceXY=Math.sqrt((Math.pow(distanceX, 2)+Math.pow(distanceY, 2)));
						if(distanceXY<data.view){
							System.out.println("进入视野半径");
							double k=countPeoAndExit();
							double k1=countClockWise(k);
							double k2=countAntiClockWise(k);
							countAngleAndNearbyGrid(k1);
							countAngleAndNearbyGridK2(k2);
							if(viewInComeK1[i][j]!=0 & viewInComeK2[i][j]!=0){
								viewInComeK[i][j]=viewInComeK1[i][j]+viewInComeK2[i][j]-1;
							}
							else if(isCompleteInAngle(k1, k2, i, j)){
								viewInComeK[i][j]=1;
							}
							else {
								viewInComeK[i][j]=viewInComeK1[i][j]+viewInComeK2[i][j];
							}
						}
						else{
							System.out.println("视野半径外");
						}
						//int viewAndPeoDistance=Math.abs(arg0)
					}
				}
			}
			//System.out.println("---------------");
			
			
			for (int i=0;i<data.w/data.pw;i++){
				for (int j=0;j<data.w/data.pw;j++){
					double k=countPeoAndExit();
					double k1=countClockWise(k);
					double k2=countAntiClockWise(k);
					countAngleAndNearbyGrid(k1);
					countAngleAndNearbyGridK2(k2);
					if(viewInComeK1[i][j]!=0 & viewInComeK2[i][j]!=0){
						viewInComeK[i][j]=viewInComeK1[i][j]+viewInComeK2[i][j]-1;
						
					}
					else if(isCompleteInAngle(k1, k2, i, j)){
						viewInComeK[i][j]=1;
						
					}
					else {
						viewInComeK[i][j]=viewInComeK1[i][j]+viewInComeK2[i][j];
						if(i== 5 & j==8){
							System.out.println(viewInComeK1[i][j]+"***"+viewInComeK2[i][j]);
						}
					}
					
					
					if(viewInComeK[i][j]!=0){
						System.out.println(i+"----"+j+"---"+viewInComeK[i][j]);
					}
					
				}
			}
		
			
			try {
	            Thread.sleep(1000);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
//		}
			
		
		
	}
	
	
	
}
