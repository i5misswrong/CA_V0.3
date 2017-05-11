package CA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.omg.CORBA.portable.ValueInputStream;

public class CountViewInComeOld {
	Data data=new Data();
	int x;
	int y;
	int cenX;
	int cenY;
	int DX;
	int DY;
	double k1;
	double k2;
	double absK1;
	double absK2;
	double viewInComeK1[][]=new double [data.w/data.pw][data.w/data.pw];
	double viewInComeK2[][]=new double [data.w/data.pw][data.w/data.pw];
	double viewAllInCome[][]=new double [data.w/data.pw][data.w/data.pw];
	public CountViewInComeOld(int x,int y){
		this.x=x;
		this.y=y;
		cenX=x+data.pw/2;
		cenY=y+data.pw/2;
		DX=x/data.pw;
		DY=y/data.pw;
		k1=countClockWise(countPeoAndExit());
		k2=countAntiClockWise(countPeoAndExit());
		absK1=Math.abs(countClockWise(countPeoAndExit()));
		absK2=Math.abs(countAntiClockWise(countPeoAndExit()));
	}
	//------------判断出口在行人的什么位置 第几象限-----
	public int judgePeoAndExit(){
		int quadrant=0;
		if(cenX<=data.exitX & cenY<=data.exitY){
			quadrant=1;
		}
		else if(cenX>=data.exitX & cenY<=data.exitY){
			quadrant=2;
		}
		else if(cenX>=data.exitX & cenY>=data.exitY){
			quadrant=3;
		}
		else if(cenX<=data.exitX & cenY>=data.exitY){
			quadrant=4;
		}
		return quadrant;
	}
	//----------------计算行人点与出口的角度------
	public double countPeoAndExit(){
		double distanceStandard=Math.abs(cenX-data.exitX);//水平差  x和i
		double distanceVertical=Math.abs(cenY-data.exitY);//垂直差  y和j
		double thetaRadian; //所求得的反正切值 弧度值
		double thetaAngle=0;//反正切值 角度值
		double k;//行人与出口构成的直线的斜率
		int quadrant=0;
		if(distanceStandard==0 | distanceVertical==0){
			//angle is 90 180 270 0
			if(distanceStandard==0){
				if(cenY>=data.exitY){
					thetaAngle=270;
				}
				else{
					thetaAngle=90;
				}
			}
			if(distanceVertical==0){
				if(cenX>=data.exitX){
					thetaAngle=180;
				}
				else{
					thetaAngle=0;
				}
			}
			else{
				System.out.println("countPeoAndExit have err");
			}
		}
		else{
			k=distanceVertical/distanceStandard;
			thetaRadian=Math.atan(k);
			quadrant=judgePeoAndExit();
			switch (quadrant) {
			case 1:
				thetaAngle=180/Math.PI*thetaRadian;
				break;
			case 2:
				thetaAngle=180-180/Math.PI*thetaRadian;
				break;
			case 3:
				thetaAngle=180+180/Math.PI*thetaRadian;
				break;
			case 4:
				thetaAngle=180/Math.PI*thetaRadian+270;
				break;

			default:
				//其他象限
				System.out.println("countPeoAndExit else  have err");
				break;
			}
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
	//count viewInCoem that the line of k1 intersect with around grid
	public void countViewInComeWithK1(){
		if(k1>=3){
			viewInComeK1[DX-1][DY]=countTrapeS(0.5-1.5/absK1, 0.5-0.5/absK1);
			viewInComeK1[DX+1][DY]=countTrapeS(0.5-1.5/absK1, 0.5-0.5/absK1);
		}
		if(k1>=1 & k1<=3){
			viewInComeK1[DX-1][DY+1]=1-countTrianS(1.5/absK1-0.5, 1.5-0.5*absK1);
			viewInComeK1[DX-1][DY]=countTrianS(0.5*absK1-0.5, 0.5-0.5*absK1);
			viewInComeK1[DX+1][DY-1]=1-countTrianS(1.5/absK1-0.5, 1.5-0.5*absK1);
			viewInComeK1[DX+1][DY]=countTrianS(0.5*absK1-0.5, 0.5-0.5*absK1);
			System.out.println(1-countTrianS(1.5/absK1-0.5, 1.5-0.5*absK1));
		}
		if(k1>=1/3 & k1<=1){
			viewInComeK1[DX-1][DY+1]=countTrianS(1.5*absK1-0.5, 1.5-0.5/absK1);
			viewInComeK1[DX][DY+1]=1-countTrianS(0.5-0.5*absK1,0.5/absK1-0.5 );
			viewInComeK1[DX+1][DY-1]=countTrianS(1.5*absK1-0.5, 1.5-0.5/absK1);
			viewInComeK1[DX][DY-1]=1-countTrianS(0.5-0.5*absK1,0.5/absK1-0.5 );
		}
		if(k1>=0 & k1<=1/3){
			viewInComeK1[DX][DY+1]=1-countTrapeS(0.5-1.5*absK1, 0.5-0.5*absK1);
			viewInComeK1[DX][DY-1]=1-countTrapeS(0.5-1.5*absK1, 0.5-0.5*absK1);
			System.out.println(viewInComeK1[DX][DY-1]+"this is direction 2 value");
		}
		if(k1<=0 & k1>=-1/3){
			viewInComeK1[DX][DY+1]=1-countTrapeS(0.5+1.5*absK1, 0.5+0.5*absK1);
			viewInComeK1[DX][DY-1]=1-countTrapeS(0.5+1.5*absK1, 0.5+0.5*absK1);
		}
		if(k1<=-1/3 & k1>=-1){
			viewInComeK1[DX][DY+1]=countTrianS(0.5/absK1-0.5, 0.5-0.5*absK1);
			viewInComeK1[DX+1][DY+1]=1-countTrianS(1.5-0.5/absK1, 1.5*absK1-0.5);
			viewInComeK1[DX][DY-1]=countTrianS(0.5/absK1-0.5, 0.5-0.5*absK1);
			viewInComeK1[DX-1][DY-1]=1-countTrianS(1.5-0.5/absK1, 1.5*absK1-0.5);
		}
		if(k1<=-1 & k1>=-3){
			viewInComeK1[DX+1][DY+1]=countTrianS(1.5-0.5*absK1, 1.5/absK1-0.5);
			viewInComeK1[DX+1][DY]=1-countTrianS(0.5*absK1-0.5, 0.5-0.5/absK1);
			viewInComeK1[DX-1][DY-1]=countTrianS(1.5-0.5*absK1, 1.5/absK1-0.5);
			viewInComeK1[DX-1][DY]=1-countTrianS(0.5*absK1-0.5, 0.5-0.5/absK1);
		}
		if(k1<=-3){
			viewInComeK1[DX+1][DY]=countTrapeS(1.5/absK1+0.5, 0.5/absK1+0.5);
			viewInComeK1[DX-1][DY]=countTrapeS(1.5/absK1+0.5, 0.5/absK1+0.5);
		}
		
		
	}

	//count viewInCoem that the line of k1 intersect with around grid
	public void countViewInComeWithK2(){
		if(k2>=3){
			viewInComeK2[DX-1][DY]=1-countTrapeS(0.5-1.5/absK2, 0.5-0.5/absK2);
			viewInComeK2[DX+1][DY]=1-countTrapeS(0.5-1.5/absK2, 0.5-0.5/absK2);
		}
		if(k2>=1 & k2<=3){
			viewInComeK2[DX-1][DY+1]=countTrianS(1.5/absK2-0.5, 1.5-0.5*absK2);
			viewInComeK2[DX-1][DY]=1-countTrianS(0.5*absK2-0.5, 0.5-0.5*absK2);
			viewInComeK2[DX+1][DY-1]=countTrianS(1.5/absK2-0.5, 1.5-0.5*absK2);
			viewInComeK2[DX+1][DY]=1-countTrianS(0.5*absK2-0.5, 0.5-0.5*absK2);
		}
		if(k2>=1/3 & k2<=1){
			viewInComeK2[DX-1][DY+1]=1-countTrianS(1.5*absK2-0.5, 1.5-0.5/absK2);
			viewInComeK2[DX][DY+1]=countTrianS(0.5-0.5*absK2,0.5/absK2-0.5 );
			viewInComeK2[DX+1][DY-1]=1-countTrianS(1.5*absK2-0.5, 1.5-0.5/absK2);
			viewInComeK2[DX][DY-1]=countTrianS(0.5-0.5*absK2,0.5/absK2-0.5 );
		}
		if(k2>=0 & k2<=1/3){
			viewInComeK2[DX][DY+1]=countTrapeS(0.5-1.5*absK2, 0.5-0.5*absK2);
			viewInComeK2[DX][DY-1]=countTrapeS(0.5-1.5*absK2, 0.5-0.5*absK2);
		}
		if(k2<=0 & k2>=-1/3){
			viewInComeK2[DX][DY+1]=countTrapeS(0.5+1.5*absK2, 0.5+0.5*absK2);
			viewInComeK2[DX][DY-1]=countTrapeS(0.5+1.5*absK2, 0.5+0.5*absK2);
		}
		if(k2<=-1/3 & k2>=-1){
			viewInComeK2[DX][DY+1]=1-countTrianS(0.5/absK2-0.5, 0.5-0.5*absK2);
			viewInComeK2[DX+1][DY+1]=countTrianS(1.5-0.5/absK2, 1.5*absK2-0.5);
			viewInComeK2[DX][DY-1]=1-countTrianS(0.5/absK2-0.5, 0.5-0.5*absK2);
			viewInComeK2[DX-1][DY-1]=countTrianS(1.5-0.5/absK2, 1.5*absK2-0.5);
		}
		if(k2<=-1 & k2>=-3){
			viewInComeK2[DX+1][DY+1]=1-countTrianS(1.5-0.5*absK2, 1.5/absK2-0.5);
			viewInComeK2[DX+1][DY]=countTrianS(0.5*absK2-0.5, 0.5-0.5/absK2);
			viewInComeK2[DX-1][DY-1]=1-countTrianS(1.5-0.5*absK2, 1.5/absK2-0.5);
			viewInComeK2[DX-1][DY]=countTrianS(0.5*absK2-0.5, 0.5-0.5/absK2);
		}
		if(k2<=-3){
			viewInComeK2[DX+1][DY]=1-countTrapeS(1.5/absK2+0.5, 0.5/absK2+0.5);
			viewInComeK2[DX-1][DY]=1-countTrapeS(1.5/absK2+0.5, 0.5/absK2+0.5);
		}
	}
	
	//delete the viewInCome that back exit
	public void deleteBackExit(){
		if(data.exitX==0){
			viewAllInCome[DX+1][DY]=-5;
			viewAllInCome[DX+1][DY-1]=-5;
			viewAllInCome[DX+1][DY+1]=-5;
		}
		if(data.exitX==data.w){
			viewAllInCome[DX-1][DY]=-5;
			viewAllInCome[DX-1][DY-1]=-5;
			viewAllInCome[DX-1][DY+1]=-5;
		}
		if(data.exitY==0){
			viewAllInCome[DX-1][DY+1]=-5;
			viewAllInCome[DX][DY+1]=-5;
			viewAllInCome[DX+1][DY+1]=-5;
		}
		if(data.exitY==data.w){
			viewAllInCome[DX-1][DY-1]=-5;
			viewAllInCome[DX][DY-1]=-5;
			viewAllInCome[DX+1][DY-1]=-5;
		}
	}
	//count the all income with k1 and k2
	public double[][] countAllViewInCome(){
		countViewInComeWithK1();
		countViewInComeWithK2();
		
		for(int i=0;i<data.w/data.pw;i++){
			for(int j=0;j<data.w/data.pw;j++){
				if(viewInComeK1[i][j]!=0 & viewInComeK2[i][j]!=0){
					viewAllInCome[i][j]=viewInComeK1[i][j]+viewInComeK2[i][j]-1;
					if(viewAllInCome[i][j]!=0){
						System.out.println(i+"---"+j);
					}
				}
			}
		}
		for(int i=0;i<data.w/data.pw;i++){
			for(int j=0;j<data.w/data.pw;j++){
				if(viewInComeK1[i][j]!=0){
					if(((i-data.exitX)^2+(j-data.exitY)^2)>((DX-data.exitX)^2+(DY-data.exitY)^2)){
						viewAllInCome[i][j]=0;
					}
				}
			}
		}
		deleteBackExit();
		
		return viewAllInCome;
	}
	//the income that add and sort rank
	public int  inComeAdd(){
		double viewAllInCome[][]=countAllViewInCome();
		Map<Integer, Double> inCome = new HashMap<Integer, Double>();
		inCome.put(1, (viewAllInCome[DX+1][DY-1])*100);
		inCome.put(2, (viewAllInCome[DX][DY-1])*100);
		inCome.put(3, (viewAllInCome[DX-1][DY-1])*100);
		inCome.put(4, (viewAllInCome[DX+1][DY])*100);
		inCome.put(5, (viewAllInCome[DX][DY])*100);
		inCome.put(6, (viewAllInCome[DX-1][DY])*100);
		inCome.put(7, (viewAllInCome[DX+1][DY+1])*100);
		inCome.put(8, (viewAllInCome[DX][DY+1])*100);
		inCome.put(9, (viewAllInCome[DX-1][DY+1])*100);
		Comparator<Map.Entry<Integer, Double>> valueComparator = new Comparator<Map.Entry<Integer,Double>>() {
			public int compare(Entry<Integer, Double> o1,
	                Entry<Integer, Double> o2) {
	            // TODO Auto-generated method stub
	            return (int) (o1.getValue()-o2.getValue());
	        }
	    };
	    // map转换成list进行排序
	    List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer,Double>>(inCome.entrySet());
	    // 排序
	    Collections.sort(list,valueComparator);
	    // 默认情况下，TreeMap对key进行升序排序
	    //System.out.println("------------map按照value升序排序--------------------");
	    int a=0;
	    double b=0.0;
	    for (Entry<Integer, Double> entry : list) {
	        a=entry.getKey();
	        b=entry.getValue();
	        System.out.println("key="+a+"----"+"value"+b);
	    }
	    if(b==0){
	    	System.out.println("key"+a+"---"+"there value == 0");
	    }
	    return a;
	}
		
		
}
