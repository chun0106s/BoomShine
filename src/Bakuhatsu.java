import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;


public class Bakuhatsu {
	int char_x,char_y;// Bakuhatsu's x-y coordinates.
	final public int MAX_BAKUHATSU_SIZE = 100;// Max bakuhatsu's size
	int vx, vy;// Bakuhatsu's moving vector
	int bakuhatsu_time = 0;
	int bakuhatsu_size = 20;
	boolean syometsu = false;
	int BALLSIZE = 20;
	
	Bakuhatsu(int tmp_x, int tmp_y, int tmp_vx, int tmp_vy){
		// Method to create bakuhatsu's.
		create(tmp_x, tmp_y, tmp_vx, tmp_vy);
	}
	
	public void create(int x, int y, int v_x, int v_y){
		char_x = x;
		char_y = y;
		vx = v_x;
		vy = v_y;
	}
	
	public void move(){
		char_x += (int)vx / 2;
		char_y += (int)vy / 2;
		bakuhatsu_time++;
		bakuhatsu_size++;
	}
	
	void draw(ImageObserver io, Graphics g){
		g.setColor(Color.GRAY);
		g.fillOval(char_x - bakuhatsu_size / 2, char_y - bakuhatsu_size / 2, bakuhatsu_size, bakuhatsu_size);
		
		if(bakuhatsu_size > MAX_BAKUHATSU_SIZE){
			syometsu = true;
		}
	}
	
	/*
	boolean isAtari(int ball_center_x, int ball_center_y){

		bakuhatsu_size = bakuhatsu_radius;
		if(Math.sqrt(Math.pow((ball_center_x - char_x), 2) + Math.pow((ball_center_y - char_y), 2)) < ((bakuhatsu_radius + bakuhatsu_size / 2) / 2)){
			return true;
		}else return false;
	}*/

}
