package CA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.CheckedInputStream;
import java.util.Map.Entry;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class Peo implements Runnable{
	Data data=new Data();
	Rule rule=new Rule();
	commonFunction cof=new commonFunction();
	int x;//行人坐标
	int y;
	
	int direction=0;//行人方向
	int speed=data.speed;//行人速度
	volatile int mapMat[][]=new int [data.w/data.pw][data.w/data.pw];//行人坐标数组 volatile锁死线程
	int viewMat[][];//记忆点所在位置的矩阵
	
	double viewInComeK[][]=new double [data.w/data.pw][data.w/data.pw];//视野收益矩阵
	double viewInComeK1[][]=new double [data.w/data.pw][data.w/data.pw];//k1下的收益矩阵
	double viewInComeK2[][]=new double [data.w/data.pw][data.w/data.pw];//k2下的收益矩阵
	double viewAroundInComeK1[][]=new double [data.w/data.pw][data.w/data.pw];
	double viewAroundInComeK2[][]=new double [data.w/data.pw][data.w/data.pw];
	double viewAroundInComeK[][]=new double [data.w/data.pw][data.w/data.pw];
	double peoInComeArray[]=new double[9];
	double peoDirectionInCome[][]=new double[data.w/data.pw][data.w/data.pw];
	double blankInCome[][]=new double [data.w/data.pw][data.w/data.pw];
	double wallInCome[][] = new double[data.w / data.pw][data.w / data.pw];
	
	public volatile boolean exitflag = false; 
	
	Vector<Peo> people = new Vector<Peo>();// 行人集合
	Vector<Peo> peoMemPointVector = new Vector<Peo>();
	volatile int peoSize=rule.rePeoSize(people);
	Map<Integer, Double> inCome = new HashMap<Integer, Double>();// 收益参数
	public Peo(int x, int y) {
		this.x = x;
		this.y = y;
		
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
	public int[][] getViewMat() {
		return viewMat;
	}
	public void setViewMat(int[][] viewMat) {
		this.viewMat = viewMat;
	}
	public double[][] getViewInComeK() {
		return viewInComeK;
	}
	public void setViewInComeK(double[][] viewInComeK) {
		this.viewInComeK = viewInComeK;
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
	public double[] getPeoInComeArray() {
		return peoInComeArray;
	}
	public void setPeoInComeArray(double[] peoInComeArray) {
		this.peoInComeArray = peoInComeArray;
	}
	public double[][] getPeoDirectionInCome() {
		return peoDirectionInCome;
	}
	public void setPeoDirectionInCome(double[][] peoDirectionInCome) {
		this.peoDirectionInCome = peoDirectionInCome;
	}
	public double[][] getBlankInCome() {
		return blankInCome;
	}
	public void setBlankInCome(double[][] blankInCome) {
		this.blankInCome = blankInCome;
	}
	public Vector<Peo> getPeople() {
		return people;
	}
	public void setPeople(Vector<Peo> people) {
		this.people = people;
	}
	public Vector<Peo> getPeoMemPointVector() {
		return peoMemPointVector;
	}
	public void setPeoMemPointVector(Vector<Peo> peoMemPointVector) {
		this.peoMemPointVector = peoMemPointVector;
	}
	public Map<Integer, Double> getInCome() {
		return inCome;
	}
	public void setInCome(Map<Integer, Double> inCome) {
		this.inCome = inCome;
	}
	//--------------行人移动方法---------------------
	public void moveLeftUp(){
//		y-=speed;
//		x-=speed;
		y=y-speed;
		x=x-speed;
	}
	public void moveUp(){
//		y-=speed;
		x=x-speed;
	}
	public void moveRightUp(){
//		y-=speed;
//		x+=speed;
		x=x-speed;
		y=y+speed;
	}
	public void moveLeft(){
//		x-=speed;
		y=y-speed;
	}
	public void moveOnSite(){
		
	}
	public void moveRight(){
//		x+=speed;
		y=y+speed;
	}
	public void moveLeftDown(){
//		x-=speed;
//		y+=speed;
		x=x+speed;
		y=y-speed;
	}
	public void moveDown(){
//		y+=speed;
		x=x+speed;
	}
	public void moveRightDown(){
//		x+=speed;
//		y+=speed;
		x=x+speed;
		y=y+speed;
	}
	public void peoMove(int direction){
		switch (direction){
		case 1:
			x=x-speed;
			y=y-speed;
			break;
		case 2:
			x=x-speed;
			break;
		case 3:
			x=x-speed;
			y=y+speed;
			break;
		case 4:
			y=y-speed;
			break;
		case 5:
			//System.out.println("direction = 5 no move");
			break;
		case 6:
			y=y+speed;
			break;
		case 7:
			x=x+speed;
			y=y-speed;
			break;
		case 8:
			x=x+speed;
			break;
		case 9:
			x=x+speed;
			y=y+speed;
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
	
	//-------------计算与其他行人所产生的方向收益------------
	public void countOthersPostition(){
		Map<Integer,Double>peoDirection=new HashMap<Integer,Double>();
		peoDirection.put(1, 0.0);
		peoDirection.put(2, 0.0);
		peoDirection.put(3, 0.0);
		peoDirection.put(4, 0.0);
		peoDirection.put(5, 0.0);
		peoDirection.put(6, 0.0);
		peoDirection.put(7, 0.0);
		peoDirection.put(8, 0.0);
		peoDirection.put(9, 0.0);
		
		for(int p=0;p<peoSize;p++){
			//if(othersX>x-data.view & othersX<x+data.view & othersY>y-data.view & othersY<y+data.view){
			double va=0.0;
			
			if(people.get(p).getDirection()==0){
				int othersX=people.get(p).getX();
				int othersY=people.get(p).getY();
				int D=judgeOthersDirection(othersX, othersY);
					switch (D) {
					case 2:
						va++;
						peoDirection.put(2, va);
						break;
					case 4:
						va++;
						peoDirection.put(4, va);
						break;
					case 6:
						va++;
						peoDirection.put(6, va);
						break;
					case 8:
						va++;
						peoDirection.put(8, va);
						break;
					default:
						break;
					}
				//}
			}
			else{
				int D=people.get(p).getDirection();
				switch (D) {
				case 1:
					va++;
					peoDirection.put(1, va);
					break;
				case 2:
					va++;
					peoDirection.put(2, va);
					break;
				case 3:
					va++;
					peoDirection.put(3, va);
					break;
				case 4:
					va++;
					peoDirection.put(4, va);
					break;
				case 5:
					va++;
					peoDirection.put(5, va);
					break;
				case 6:
					va++;
					peoDirection.put(6, va);
					break;
				case 7:
					va++;
					peoDirection.put(7, va);
					break;
				case 8:
					va++;
					peoDirection.put(8, va);
					break;
				case 9:
					va++;
					peoDirection.put(9, va);
					break;
				default:
					break;
				}
			}
		}
		peoInComeArray[0]=(double)peoDirection.get(1)/peoSize;
		peoInComeArray[1]=(double)peoDirection.get(2)/peoSize;
		peoInComeArray[2]=(double)peoDirection.get(3)/peoSize;
		peoInComeArray[3]=(double)peoDirection.get(4)/peoSize;
		peoInComeArray[4]=(double)peoDirection.get(5)/peoSize;
		peoInComeArray[5]=(double)peoDirection.get(6)/peoSize;
		peoInComeArray[6]=(double)peoDirection.get(7)/peoSize;
		peoInComeArray[7]=(double)peoDirection.get(8)/peoSize;
		peoInComeArray[8]=(double)peoDirection.get(9)/peoSize;
	}
	//判断其他格子位于该行人的上下左右那个方向
	public int judgeOthersDirection(int OX,int OY){
		int k1=1;
		int k2=-1;
		int y1=k1*(x-OX)+OY;
		int y2=k2*(x-OX)+OY;
		int D=0;
		if(y>=y1 & y>y2){
			D=2;
		}
		else if(y<=y1 & y<y2){
			D=8;
		}
		else if(y>=y2 & y<y1){
			D=6;
		}
		else if(y>=y1 & y<y2){
			D=4;
		}
		return D;
	}
	
	//-------------计算空格收益-----------------
	public void countBlankInCome(){
		for(int p=0;p<peoSize;p++){
			int othersX=people.get(p).getX();
			int othersY=people.get(p).getY();
			for(int i=0;i<data.w/data.pw;i++){
				for (int j=0;j<data.w/data.pw;j++){
					blankInCome[i][j]=1;
				}
			}
			if(othersX==x & othersY==y){
				blankInCome[othersX/data.pw][othersY/data.pw]=0;
			}
			blankInCome[othersX/data.pw][othersY/data.pw]=-30;
		}
	}
	//-----------是否碰到墙--------
	public void isTouchWall(){
		for(int i=x/data.pw;i<data.w/data.pw;i++){
			for(int j=0;j<data.w/data.pw;j++){
				if(y==0){
					wallInCome[i][y/data.pw-1]=-50;
				}
			}
		}
	}
	//-----------计算总收益---------------
	public void countAllInCome(){
		//计算视野收益
//		for (int i=0;i<data.w/data.pw;i++){
//			for (int j=0;j<data.w/data.pw;j++){
//				double k=countPeoAndExit();
//				double k1=countClockWise(k);
//				double k2=countAntiClockWise(k);
//				if(viewInComeK1[i][j]!=0 & viewInComeK2[i][j]!=0){
//					viewInComeK[i][j]=viewInComeK1[i][j]+viewInComeK2[i][j]-1;
//				}
//				else if(isCompleteInAngle(k1, k2, i, j)){
//					viewInComeK[i][j]=1.001;
//				}
//				else {
//					viewInComeK[i][j]=viewInComeK1[i][j]+viewInComeK2[i][j];
//				}
//				if(viewAroundInComeK1[i][j]!=0 & viewAroundInComeK2[i][j]!=0){
//					viewAroundInComeK[i][j]=viewAroundInComeK1[i][j]+viewAroundInComeK2[i][j]-1;
//				}
//				
//				else {
//					viewAroundInComeK[i][j]=viewAroundInComeK1[i][j]+viewAroundInComeK2[i][j];
//				}
//				
//				
//			}
//		}
//		//计算空格收益
//		countBlankInCome();
//		//计算行人方向收益
//		countOthersPostition();
//		//-----将收益加起来---------------
//		inCome.put(1, (viewAroundInComeK[x/data.pw-1][y/data.pw-1]+viewInComeK[x/data.pw-1][y/data.pw-1]+blankInCome[x/data.pw-1][y/data.pw-1]+peoInComeArray[0])*10);
//		inCome.put(2, (viewAroundInComeK[x/data.pw-1][y/data.pw]+viewInComeK[x/data.pw-1][y/data.pw]+blankInCome[x/data.pw-1][y/data.pw]+peoInComeArray[1])*10);
//		inCome.put(3, (viewAroundInComeK[x/data.pw-1][y/data.pw+1]+viewInComeK[x/data.pw-1][y/data.pw+1]+blankInCome[x/data.pw-1][y/data.pw+1]+peoInComeArray[2])*10);
//		inCome.put(4, (viewAroundInComeK[x/data.pw][y/data.pw-1]+viewInComeK[x/data.pw][y/data.pw-1]+blankInCome[x/data.pw][y/data.pw-1]+peoInComeArray[3])*10);
//		inCome.put(5, (viewAroundInComeK[x/data.pw][y/data.pw]+viewInComeK[x/data.pw][y/data.pw]+blankInCome[x/data.pw][y/data.pw]+peoInComeArray[4])*10);
//		inCome.put(6, (viewAroundInComeK[x/data.pw][y/data.pw+1]+viewInComeK[x/data.pw][y/data.pw+1]+blankInCome[x/data.pw][y/data.pw+1]+peoInComeArray[5])*10);
//		inCome.put(7, (viewAroundInComeK[x/data.pw+1][y/data.pw-1]+viewInComeK[x/data.pw+1][y/data.pw-1]+blankInCome[x/data.pw+1][y/data.pw-1]+peoInComeArray[6])*10);
//		inCome.put(8, (viewAroundInComeK[x/data.pw+1][y/data.pw]+viewInComeK[x/data.pw+1][y/data.pw]+blankInCome[x/data.pw+1][y/data.pw]+peoInComeArray[7])*10);
//		inCome.put(9, (viewAroundInComeK[x/data.pw+1][y/data.pw+1]+viewInComeK[x/data.pw+1][y/data.pw+1]+blankInCome[x/data.pw+1][y/data.pw+1]+peoInComeArray[8])*10);
//		//对总收益排序
//		Comparator<Map.Entry<Integer, Double>> valueComparator = new Comparator<Map.Entry<Integer,Double>>() {
//	        
//			public int compare(Entry<Integer, Double> o1,
//	                Entry<Integer, Double> o2) {
//	            // TODO Auto-generated method stub
//	            return (int) (o1.getValue()-o2.getValue());
//	        }
//	    };
//	    // map转换成list进行排序
//	    List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer,Double>>(inCome.entrySet());
//	    // 排序
//	    Collections.sort(list,valueComparator);
//	    // 默认情况下，TreeMap对key进行升序排序
//	    //System.out.println("------------map按照value升序排序--------------------");
//	    int a=0;
//	    double b=0.0;
//	    for (Entry<Integer, Double> entry : list) {
//	        a=entry.getKey();
//	        b=entry.getValue();
//	    }
//	    return a;
	}
	
	//---------碰到出口怎么办--------
	public boolean isExit(){
		int cenX=x+data.pw/2;
		int cenY=y+data.pw/2;
		boolean flag=false;
//		double ping=Math.pow(cenX-data.exitX, 2)+Math.pow(cenY-data.exitY,2);
//		double exitDistance=Math.sqrt(ping);
//		if(exitDistance<data.pw){
//			flag=true;
//		}
		if(x==0 & y==data.exitY){
			flag=true;
		}
		return flag;
	}
	// judge next point is ok
	public boolean isNextOk(int direction){
		boolean flag=false;
		int px=0;
		int py=0;

		switch (direction){
		case 1:
			px=x-speed;
			py=y-speed;
			break;
		case 2:
			px=x-speed;
			break;
		case 3:
			px=x-speed;
			py=y+speed;
			break;
		case 4:
			py=y-speed;
			break;
		case 5:
			break;
		case 6:
			py=y+speed;
			break;
		case 7:
			px=x+speed;
			py=y-speed;
			break;
		case 8:
			px=x+speed;
			break;
		case 9:
			px=x+speed;
			py=y+speed;
			break;
		default:
			
			break;
		}	
//		if(px<0 | px>data.w | py<0 | py>data.w){
//			flag=true;
//		}
//		else{
////			if(mapMat[px/data.pw][py/data.pw]!=0){
////				flag=true;
////			}
//			if(chickCrash(px, py)){
//				flag=true;
//			}
//			else{
////				if(px==0 & py==data.exitY){
////					
////					flag=false;
////				}
////				else{
//					if(px<data.pw | py<data.pw | px>data.w-2*data.pw | py>data.w-2*data.pw){
//						flag=true;
//					}
////				}
//			}
//		}
	
		if(px<data.pw | py<data.pw | px>data.w-2*data.pw | py>data.w-2*data.pw){
			flag=true;
		}
		else{
			if(rule.isNextPoint(px, py, x, y, mapMat)){
				flag=true;
			}
			else{
				
			}
		}
		return flag;
	}
	//
	public boolean chickCrash(int px,int py){
		boolean flag=false;
		for(int i=0;i<peoSize;i++){
			if(px==people.get(i).getX() & py==people.get(i).getY()){
				flag=true;
			}
			//if(people.get(i)!=this){
				
				if(x==people.get(i).getX() & py==people.get(i).getY()){
					flag=true;
				}
			//}
		}
		return flag;
	}
	//count people and their's direction income
	public ArrayList countPeoInCome(){
		int cenx=x+data.pw/2;
		int ceny=y+data.pw/2;
		ArrayList<InComeSort> peoInCome=new ArrayList<InComeSort>();
		Map<Integer,Double> pni=new HashMap<Integer,Double>();
		Map<Integer,Double> pdi=new HashMap<Integer,Double>();
		for(int i=0;i<10;i++){
			pni.put(i,0.0);
			pdi.put(i,0.0);
		}
		int cc=peoSize;
		for(int i=0;i<peoSize;i++){
			if(people.get(i).getX()==x & people.get(i).getY()==y){
				
			}
			else{
				double parallel=Math.abs(people.get(i).getX()+data.pw/2-ceny+1);
				double vartical=Math.abs(people.get(i).getY()+data.pw/2-cenx+1);
				double k=vartical/parallel;
				int quadrat=cof.judgeQuadrat(cenx, ceny, people.get(i).getX()+data.pw/2, people.get(i).getY()+data.pw/2);
				double theta=0;
				switch (quadrat) {
				case 1:
					theta=Math.atan(k);
					break;
				case 2:
					theta=Math.PI-Math.atan(k);
					break;
				case 3:
					theta=Math.PI+Math.atan(k);
					break;
				case 4:
					theta=Math.PI*2-Math.atan(k);
					break;
				default:
					System.out.println("countAnglePeoAndExit err");
					break;
				}
				double theta2=Math.toDegrees(theta);
				int otherA=cof.judgeAngleAndTheta(theta2);
				switch (otherA) {
				case 1:
					pni.put(1, pni.get(1)+1);
					break;
				case 2:
					pni.put(2, pni.get(2)+1);
					break;
				case 3:
					pni.put(3, pni.get(3)+1);
					break;
				case 4:
					pni.put(4, pni.get(4)+1);
					break;
				case 5:
					pni.put(5, pni.get(5)+1);
					break;
				case 6:
					pni.put(6, pni.get(6)+1);
					break;
				case 7:
					pni.put(7, pni.get(7)+1);
					break;
				case 8:
					pni.put(8, pni.get(8)+1);
					break;
				case 9:
					pni.put(9, pni.get(9)+1);
					break;
				default:
					break;
				}
				int otherD=people.get(i).getDirection();
				switch (otherD) {
				case 1:
					pdi.put(1, pdi.get(1)+1);
					break;
				case 2:
					pdi.put(2, pdi.get(2)+1);
					break;
				case 3:
					pdi.put(3, pdi.get(3)+1);
					break;
				case 4:
					pdi.put(4, pdi.get(4)+1);
					break;
				case 5:
					pdi.put(5, pdi.get(5)+1);
					break;
				case 6:
					pdi.put(6, pdi.get(6)+1);
					break;
				case 7:
					pdi.put(7, pdi.get(7)+1);
					break;
				case 8:
					pdi.put(8, pdi.get(8)+1);
					break;
				case 9:
					pdi.put(9, pdi.get(9)+1);
					break;
				default:
					break;
				}
			}
			
		}
		for(int i=0;i<pdi.size();i++){
			InComeSort inC=new InComeSort();
			inC.setDirection(i);
			inC.setInCome((pni.get(i)+pdi.get(i))/peoSize);
			peoInCome.add(inC);
		}
		return peoInCome;
	}
	public void run(){
		int cenX=x+data.pw/2;
		int cenY=y+data.pw/2;
		System.out.println("program start");
		try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		//for(int hh=0;hh<1;hh++){
			while(true){
				try {
		            Thread.sleep(500);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
				
				CountViewInCome CVI=new CountViewInCome(x, y);
				
				int peoSize=rule.rePeoSize(people);
//				for(int i=0;i<data.w/data.pw;i++){
//					for(int j=0;j<data.w/data.pw;j++){
//						if(v[i][j]!=0){
//							System.out.println(i+"---"+j+"---"+v[i][j]);
//						}
//				}
//				}
				if(isExit()){
					mapMat=rule.isGOExit(x, y, mapMat);
				}
				else{
					int t=CVI.judgeQuadrat();
					double k=CVI.countAnglePeoAndExit();
					
					ArrayList<InComeSort> inComeAll=CVI.sortInCome();
					ArrayList<InComeSort> peoInComeAll=countPeoInCome();
					ArrayList<InComeSort> allInCome=new ArrayList<InComeSort>(10);
					
					for(int i=0;i<10;i++){
						InComeSort is=new InComeSort();
						allInCome.add(is);
					}
					for(int i=0;i<9;i++){
						double cc=inComeAll.get(i).getInCome();//+peoInComeAll.get(i).getInCome();
						InComeSort tp=new InComeSort();
						tp.setDirection(i);
						tp.setInCome(cc);
						allInCome.set(i, tp);
					}
					for(int i=0;i<allInCome.size();i++){
						if(allInCome.get(i)==null | allInCome.get(i).getDirection()==0){
							allInCome.remove(i);
							--i;
						}
					}
					for(int i=0;i<allInCome.size()-1;i++){//sort the list
						for(int j=0;j<allInCome.size()-1;j++){
							if(allInCome.get(j).getInCome()<allInCome.get(j+1).getInCome()){
								InComeSort tIn1=allInCome.get(j);
								InComeSort tIn2=allInCome.get(j+1);
								allInCome.set(j, tIn2);
								allInCome.set(j+1, tIn1);
							}
							
						}
					}
					
					
					int h=1;
					int d=0;
					boolean ff=false;
					if(x==data.pw & y==data.exitY){
						d=2;
					}
					else{
						while(isNextOk(allInCome.get(h).getDirection())){
							h++;
							if(h>7){
								ff=true;
								break;
							}
						}
						if(ff){
							d=5;
						}
						else{
							d=allInCome.get(h).getDirection();
						}
					}
					
					//int peoId=mapMat[x/data.pw][y/data.pw];
					int px=x;
					int py=y;
					switch (d){
					case 1:
						px=x-speed;
						py=y-speed;
						break;
					case 2:
						px=x-speed;
						break;
					case 3:
						px=x-speed;
						py=y+speed;
						break;
					case 4:
						py=y-speed;
						break;
					case 5:
						break;
					case 6:
						py=y+speed;
						break;
					case 7:
						px=x+speed;
						py=y-speed;
						break;
					case 8:
						px=x+speed;
						break;
					case 9:
						px=x+speed;
						py=y+speed;
						break;
					}
					if(x==0 & y==data.exitY){
						exitflag=true;
					}
					
//					mapMat[px/data.pw][py/data.pw]=peoId;
//					mapMat[x/data.pw][y/data.pw]=0;
//					setMapMat(mapMat);
					
					int peoId=mapMat[x/data.pw][y/data.pw];
					rule.changeMapMat(px, py, x, y, peoId, mapMat);
					setDirection(d);
					peoMove(d);
					
				}
				
				
			}
		//}
	}
	
	
	
}
