package CA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CountViewInCome {
	Data data=new Data();
	commonFunction cof=new commonFunction();
	int x;
	int y;
	int dx;
	int dy;
	int cenx;
	int ceny;
	double theta;
	double theta1;
	double theta2;
	Map<Integer,Double> viewInCome1=new HashMap<Integer,Double>();
	Map<Integer,Double> viewInCome2=new HashMap<Integer,Double>();
	Map<Integer,Double> viewInComeCom=new HashMap<Integer,Double>();
	Map<Integer,Double> seeExitInCome=new HashMap<Integer,Double>();
	public CountViewInCome(int x,int y){
		this.x=x;
		this.y=y;
		dx=this.x/data.pw;
		dy=this.y/data.pw;
		cenx=this.x+data.pw/2;
		ceny=this.y+data.pw/2;
		theta=countAnglePeoAndExit();
		theta1=anticlock(theta);
		theta2=clock(theta);
		for(int i=0;i<9;i++){
			viewInCome1.put(i, 0.0);
			viewInCome2.put(i, 0.0);
			viewInComeCom.put(i, 0.0);
			seeExitInCome.put(i, 0.0);
		}
	}
	// judge the exit's position to people
	public int judgeQuadrat(){
		int quadrat=0;
		int parallel=data.exitY+data.exitL/2-ceny+1;
		int vartical=data.exitX-cenx+1;
		if(vartical<0 & parallel>0){
			quadrat=1;
		}
		else if(vartical<0 & parallel<0){
			quadrat=2;
		}
		else if(vartical>0 & parallel<0){
			quadrat=3;
		}
		else if(vartical>0 & parallel>0){
			quadrat=4;
		}
		else{
			System.out.println("the peo on the axis");
		}
		return quadrat;
	}
	//count angle that people and exit
	public double countAnglePeoAndExit(){
		double parallel=Math.abs(data.exitY+data.pw/2-ceny);
		double vartical=Math.abs(data.exitX+data.pw-cenx);
		double k=vartical/parallel;
		int quadrat=cof.judgeQuadrat(cenx, ceny, data.exitX, data.exitY);
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
		return Math.toDegrees(theta);
	}
	//count angle of add anticlock 
	public double anticlock(double theta){
		double thetaDame=theta+data.viewAngle;
		double theta1 ;
		if(thetaDame>=360){
			theta1=thetaDame-360;
		}
		else{
			theta1=thetaDame;
		}
		return theta1;
	}
	//count angle of subtract clock
	public double clock(double theta){
		double thetaDame=theta-data.viewAngle;
		double theta2 ;
		if(thetaDame<0){
			theta2=360-thetaDame;
		}
		else{
			theta2=thetaDame;
		}
		return theta2;
	}
	//count the income of theta1
	public void countTheta1(){
		if(theta1<157.5 & theta1 >=112.5){
			viewInCome1.put(1, (theta1-112.5)/45);
		}
		if(theta1<112.5 & theta1 >=67.5){
			viewInCome1.put(2, (theta1-67.5)/45);
		}
		if(theta1<67.5 & theta1 >=22.5){
			viewInCome1.put(3, (theta1-22.5)/45);
		}
		if(theta1<202.5 & theta1 >=157.5){
			viewInCome1.put(4, (theta1-157.5)/45);
		}
		if(theta1<22.5 & theta1 >=0){
			viewInCome1.put(6, (theta1+22.5)/45);
		}
		if(theta1<360 & theta1 >=337.5){
			viewInCome1.put(6, (theta1-337.5)/45);
		}
		if(theta1<247.5 & theta1 >=202.5){
			viewInCome1.put(7, (theta1-202.5)/45);
		}
		if(theta1<292.5 & theta1 >=247.5){
			viewInCome1.put(8, (theta1-247.5)/45);
		}
		if(theta1<337.5 & theta1 >=292.5){
			viewInCome1.put(9, (theta1-292.5)/45);
		}
	}
	//count the income of theta2
	public void countTheta2(){
		if(theta2<157.5 & theta2 >=112.5){
			viewInCome2.put(1, (157.5-theta2)/45);
		}
		if(theta2<112.5 & theta2 >=67.5){
			viewInCome2.put(2, (112.5-theta2)/45);
		}
		if(theta2<67.5 & theta2 >=22.5){
			viewInCome2.put(3, (67.5-theta2)/45);
		}
		if(theta2<202.5 & theta2 >=157.5){
			viewInCome2.put(4, (202.5-theta2)/45);
		}
		if(theta2<22.5 & theta2 >=0){
			viewInCome2.put(6, (22.5-theta2)/45);
		}
		if(theta2<360 & theta2 >=337.5){
			viewInCome2.put(6, (382.5-theta2)/45);
		}
		if(theta2<247.5 & theta2 >=202.5){
			viewInCome2.put(7, (247.5-theta2)/45);
		}
		if(theta2<292.5 & theta2 >=247.5){
			viewInCome2.put(8, (292.5-theta2)/45);
		}
		if(theta2<337.5 & theta2 >=292.5){
			viewInCome2.put(9, (337.5-theta2)/45);
		}
	}
	//count the gird complete in theta1 and  theta2
	public void isCompleteInTheta(){
		if(theta1>=157.5 & theta2<=112.5){
			viewInComeCom.put(1,1.01);
		}
		if(theta1>=112.5 & theta2<=67.5){
			viewInComeCom.put(2,1.01);
		}
		if(theta1>=67.5 & (theta2<=22.5 | theta2>theta1)){
			viewInComeCom.put(3,1.01);
		}
		if(theta1>=202.5 & theta2<=157.5){
			viewInComeCom.put(4,1.01);
		}
		if(theta1>=22.5 & theta2>=337.5){
			viewInComeCom.put(6,1.01);
		}
		if(theta1>=247.5 & theta2<=202.5){
			viewInComeCom.put(7,1.01);
		}
		if(theta1>=292.5 & theta2<=247.5){
			viewInComeCom.put(8,1.01);
		}
		if(theta1>=337.5 & theta2<=292.5){
			viewInComeCom.put(9,1.01);
		}
	}
	//when people around the exit how do?
	public void isSeeExit(){
		double exitDistance=Math.sqrt((cenx-data.exitX)^2+(ceny-data.exitY)^2);
		if(exitDistance<data.viewMatrix){
			double theta=countAnglePeoAndExit();
			if(theta<157.5 & theta >=112.5){
				seeExitInCome.put(1, 1.02);
				seeExitInCome.put(4, 0.2);
				seeExitInCome.put(2, 0.2);
			}
			if(theta<112.5 & theta >=67.5){
				seeExitInCome.put(2, 1.02);
				seeExitInCome.put(1, 0.2);
				seeExitInCome.put(3, 0.2);
			}
			if(theta<67.5 & theta >=22.5){
				seeExitInCome.put(3, 1.02);
				seeExitInCome.put(2, 0.2);
				seeExitInCome.put(6, 0.2);
			}
			if(theta<202.5 & theta >=157.5){
				seeExitInCome.put(4, 1.02);
				seeExitInCome.put(1, 0.2);
				seeExitInCome.put(7, 0.2);
			}
			if(theta<22.5 & theta >=0){
				seeExitInCome.put(6, 1.02);
				seeExitInCome.put(3, 0.2);
				seeExitInCome.put(9, 0.2);
			}
			if(theta<360 & theta >=337.5){
				seeExitInCome.put(6,1.02);
				seeExitInCome.put(3, 0.2);
				seeExitInCome.put(9, 0.2);
			}
			if(theta<247.5 & theta >=202.5){
				seeExitInCome.put(7, 1.02);
				seeExitInCome.put(4, 0.2);
				seeExitInCome.put(8, 0.2);
			}
			if(theta<292.5 & theta >=247.5){
				seeExitInCome.put(8, 1.02);
				seeExitInCome.put(7, 0.2);
				seeExitInCome.put(9, 0.2);
			}
			if(theta<337.5 & theta >=292.5){
				seeExitInCome.put(9, 1.02);
				seeExitInCome.put(8, 0.2);
				seeExitInCome.put(6, 0.2);
			}
		}
	}
	// sort the list
	public ArrayList sortInCome(){
		isSeeExit();
		countTheta1();//count three income
		countTheta2();
		isCompleteInTheta();
		
		ArrayList<InComeSort> inComeAll=new ArrayList<InComeSort>();
		for(int i=0;i<viewInCome1.size();i++){//put the three income to all income
			InComeSort inC=new InComeSort();
			inC.setDirection(i);
			inC.setInCome(viewInCome1.get(i)+viewInCome2.get(i)+viewInComeCom.get(i)+seeExitInCome.get(i));
			inComeAll.add(inC);
		}
		
//		for(int i=0;i<inComeAll.size();i++){
//			System.out.println(inComeAll.get(i).getDirection()+"---"+inComeAll.get(i).getInCome());
//		}
		
		return inComeAll;
	}
}
