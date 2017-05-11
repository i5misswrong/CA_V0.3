package CA;

public class commonFunction {
	Data data=new Data();
	public int judgeQuadrat(int x,int y,int otherx,int othery){
		int cenx=x+data.pw/2;
		int ceny=y+data.pw/2;
		int quadrat=0;
		int parallel=othery-ceny+1;
		int vartical=otherx-cenx+1;
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
	
	public int judgeAngleAndTheta(double theta){
		int d=0;
		if(theta<157.5 & theta >=112.5){
			d=1;
		}
		if(theta<112.5 & theta >=67.5){
			d=2;
		}
		if(theta<67.5 & theta >=22.5){
			d=3;
		}
		if(theta<202.5 & theta >=157.5){
			d=4;
		}
		if(theta<22.5 & theta >=0){
			d=6;
		}
		if(theta<360 & theta >=337.5){
			d=6;
		}
		if(theta<247.5 & theta >=202.5){
			d=7;
		}
		if(theta<292.5 & theta >=247.5){
			d=8;
		}
		if(theta<337.5 & theta >=292.5){
			d=9;
		}
		return d;
	}
}
