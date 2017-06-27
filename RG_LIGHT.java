package DIdi;
import java.util.*;
public class RG_LIGHT {
	//注：为1：
	private static int[][] lights;//6400个可能存在的红绿灯
	public RG_LIGHT(){
		//前置条件  无
		//副作用  所有lights值被赋值为0
		//后置条件  初始化所有红绿灯
		lights=new int [80][80];
		for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				lights[ii][i]=0;
			}
		}
	}
	public void Light(int x,int y){
		//前置条件  符合条件的坐标
		//副作用，改变了lights
		//后置条件  给红绿灯赋一个随机的值（1,2）
		Random rand=new Random();
		lights[x][y]=rand.nextInt(2)+1;
	}
	public static int get_RG(int x,int y){
		//前置条件  符合条件的坐标
		//副作用  无
		//后置条件  返回红绿灯的颜色   1为绿  2为红   南北方向
		return lights[x][y];
	}
	public static void RG_change(){
		//前置条件  无
		//副作用  改变了lights
		//后置条件  所有红绿灯颜色反转
		for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				if(lights[ii][i]==1){
					lights[ii][i]=2;
				}
				else{
					if(lights[ii][i]==2){
						lights[ii][i]=1;
						}
				}
			}
		}
	}
	public boolean repOK(){
		//前置条件 none
		//副作用 none
		//后置条件  如果满足不定式，返回true
		for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				if(lights[ii][i]<0||lights[ii][i]>2)return false;
			}
		}
		return true;
	}
}
