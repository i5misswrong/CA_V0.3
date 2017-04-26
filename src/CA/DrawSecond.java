package CA;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Vector;
import javax.swing.JPanel;

public class DrawSecond extends JPanel implements Runnable{
	//创建画板类 完成行人、地图的绘制和重绘 完成行人的初始化
	Data data=new Data();//初始化数据类
	Vector<Peo> peoVector=new Vector<Peo>();//创建行人集合  保证线程安全 采用vector
	Vector<memPoint> mPointVector=new Vector<memPoint>();
	Vector<Peo> peoMemPointVector=new Vector<Peo>();
	volatile int mapMat[][]=new int[data.w/data.pw][data.w/data.pw];//创建行人坐标数组 使用volatile锁死线程
	int viewMat[][]=new int[data.w/data.pw][data.w/data.pw]; //行人视野矩阵
	public DrawSecond(){
		int peoNum=120;//行人数量
		int peoId=1;//行人ID
//		---------------------------------
//		----此处为测试用 在特定位置产生行人-----
		//坐标系旋转后 (i,j)i是y  j是x
		viewMat[3][3]=data.viewMatrixNum;
		//------------------------
		Peo peo1=new Peo(300,400);
		peo1.setPeople(peoVector);
		peo1.setViewMat(viewMat);
		Thread t1=new Thread(peo1);
		t1.start();
		mapMat[6][8]=peoId;
		peoVector.add(peo1);
		//-----------------------
		Peo peo2=new Peo(200,100);
		peo2.setPeople(peoVector);
		peo2.setViewMat(viewMat);
		Thread t2=new Thread(peo2);
		t2.start();
		mapMat[4][2]=peoId;
		peoVector.add(peo2);
		//-----------------------
		
		
		
//		---------------------------------
//		-------------这是随机生成行人-------------
//		for (int i=0;i<peoNum;i++){
//			int x=((int)(Math.random()*50)*data.pw);//在1-10产生随机数  X pw就得到确定坐标
//			int y=((int)(Math.random()*50)*data.pw);
//			peoOld peo=new peoOld(x,y);//新建行人
//			mapMat[y/data.pw][x/data.pw]=peoId;//设置行人坐标的值 唯一
//			peoId++;//Id++
//			peo.setMapMat(mapMat);//将数组设置到peo类
//			peo.setPeople(peoVector);//将集合设置到peo类
//			Thread t=new Thread(peo);//创建线程
//			t.start();//启动线程
//			peoVector.add(peo);//将行人添加到集合
//		}
//		-----------------------------------
		
		//--------------初始化记忆点---------------
//		memPoint mPoint1=new memPoint(300, 300);
//		mPoint1.setmPointVector(mPointVector);
//		mPointVector.add(mPoint1);
//		Peo peo01=new Peo(300,300);
//		peo01.setPeoMemPointVector(peoMemPointVector);
//		peoMemPointVector.add(peo01);
//		Thread m1=new Thread(peo01);
//		m1.start();
		//viewMat[i][j]  i 朝下  j 朝右
		
		
	}
	public void paint (Graphics g){//重写绘图方法
		g.setColor(Color.WHITE);//设置颜色
		//super.paint(g);
		g.fillRect(0, 0, data.w,data.w);//画出面板
		this.drawMap(g);//绘制地图
//		----------当行人在出口时的动作----------------
//		for(int i=0;i<peoVector.size();i++){
//			Peo peo=peoVector.get(i);//获取行人
//			if(peo.isGoExit()){//如果没有到达出口
//				this.drawPeo(g, peo.getX(), peo.getY());//画出行人
//			}
//			else{
//				//如果到达出口
//				mapMat[peo.getY()/data.pw][peo.getX()/data.pw]=0;//将行人坐标设置为0
//				peoVector.remove(i);//将行人从集合移除
//			}
//		}
		//-------绘制行人---------------
		for(int i=0;i<peoVector.size();i++){
			Peo peo=peoVector.get(i);
			this.drawPeo(g, peo.getX(), peo.getY()-data.pw);
		}
		//--------绘制记忆点---------------
		this.drawMem(g);
		
	}
	//----------绘制记忆点方法------------
	public void drawMem(Graphics g){
		g.setColor(Color.green);
		for (int i=0;i<data.w/data.pw;i++){
			for (int j=0;j<data.w/data.pw;j++){
				if(viewMat[i][j]==data.viewMatrixNum){
					g.fill3DRect(i*data.pw, j*data.pw, data.pw, data.pw, false);
				}
			}
		}
	}
	public void drawMap(Graphics g){
		//绘制地图方法
		
		g.setColor(Color.black);
		//绘制中间线条
		for (int i=1;i<(data.w/data.pw);i++){//竖线
			g.drawLine(data.pw*i, 0, data.pw*i, data.w);//每隔pw 便绘制一个线条 
		}
		for (int i=1;i<(data.w/data.pw);i++){//横线
			g.drawLine(0, data.pw*i, data.w, data.pw*i);
		}
		Graphics2D g2=(Graphics2D)g;//绘制宽线条
		Stroke stroke=new BasicStroke(5.0f);//设置线宽
		g2.setStroke(stroke);//？
		//绘制边框
		g2.drawLine(0,0, 0,data.w);
		g2.drawLine(0,data.w, data.w,data.w);
		g2.drawLine(data.w,0, data.w,data.w);
		g2.drawLine(0,0, data.exitY,0);//最上面左边边框
		g2.drawLine(data.exitY+data.exitL,0, data.w,0);//最上面右边边框
	}
	public void drawPeo(Graphics g,int x,int y){//绘制行人
			g.setColor(Color.red);
			g.fill3DRect(y+data.pw,x, data.pw, data.pw,false);
	}
	public void run(){
		while(true){
			this.repaint();//重绘
		}
	}
}

