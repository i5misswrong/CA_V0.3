package CA;

public class Data {
	int w=500; //房间长宽
	int fw=600;//窗口长宽
	//int fwx=50; //jpanel 距离左上方坐标
	//int h=10; //寻欢次数
	int pw=10;//行人长 宽   每个小方格的长度
	//int hw=30;
	//int inpx=10;//行人初始x
	//int inpy=10;//行人初始y
	int roomSize=w/pw; //一共有几行几列
	int exitX=(roomSize/2)*pw;//出口x
	int exitY=0;//出口y
	int exitL=pw;//出口长度
	int speed=10;//移动速度
	int view=5*pw;//视野半径   视野半径定义为矩形 距离这个点i j 前后左右的距离
	int viewMatrixNum=1000; //记忆点在矩阵中的值
	int inViewPeo=(view)*(view);
	int memPointNum=1;//记忆点数量
	double viewAngle=30;//半个视野角度  以行人和记忆点的轴线为中心线  顺时针逆时针旋转viewAngle的值，用角度表示 
	//视野像素  视野值
}