package CA;

public class Data {
	int w=500; //房间长宽
	int fw=600;//窗口长宽
	
	int pw=20;//行人长 宽   每个小方格的长度
	
	int roomSize=w/pw; //一共有几行几列
	int exitY=5*pw;//出口y
	int exitX=0*pw;//出口x
	int exitL=pw;//出口长度
	int exitMun=5000;//出口点在矩阵中的值
	int speed=pw;//移动速度
	int view=3;//视野半径   视野半径定义为矩形 距离这个点i j 前后左右的距离  与下面是反的
	int viewMatrix=view*pw;//视野半径 对应矩阵上的值  与view差一个行人pw
	int viewMatrixNum=1000; //记忆点在矩阵中的值
	int inViewPeo=(view)*(view);
	int memPointNum=1;//记忆点数量
	double viewAngle=10;//半个视野角度  以行人和记忆点的轴线为中心线  顺时针逆时针旋转viewAngle的值，用角度表示 
	//视野像素  视野值
}