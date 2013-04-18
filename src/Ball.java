import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;


public class Ball {
	int char_x, char_y;//　Ball's x-y coordinates.
	final public int BALL_SIZE = 20;//　Ball's size.
	int vx, vy;//　Ball's moving vector.
	int BICHOUSEI = 22;
	boolean changed = false;
	
	Ball(int tmp_width, int tmp_height, int index){
		//　Method to create balls.
		create(tmp_width, tmp_height); 
	}
	
	public void create(int SCREEN_WIDTH, int SCREEN_HEIGHT){
		// Balls appear at random position. Moving vector is also at random.
		char_x = (int)(Math.random() * SCREEN_WIDTH / 2 + SCREEN_WIDTH / 4);
		char_y = (int)(Math.random() * SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 4);
		vx = (int)((3 * Math.random() + 1) * Math.pow(-1, Math.ceil(Math.random() *  10)));
		vy = (int)((3 * Math.random() + 1) * Math.pow(-1, Math.ceil(Math.random() *  10)));
	}
	
	public void move(int screen_width,int screen_height){
		// Define the movement of the balls and balls reflect at borders.
		if(char_x + BALL_SIZE / 2 > screen_width) vx *= -1;
		if(char_y + BALL_SIZE / 2 > screen_height) vy *= -1;
		if(char_x < BALL_SIZE / 2) vx *= -1;
		if(char_y < BALL_SIZE / 2) vy *= -1;
		
		char_x += vx;
		char_y += vy;
	}
	
	void draw(ImageObserver io, Graphics g){
		if(changed){
		g.setColor(Color.BLUE);
		g.fillOval(char_x - BALL_SIZE / 2, char_y - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE);
		}else{
			g.setColor(Color.WHITE);
			g.fillOval(char_x - BALL_SIZE / 2, char_y - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE);
		}
	}
	
	boolean isAtariClick(int bakuhatsu_epi_center_x,int bakuhatsu_epi_center_y,double bakuhatsu_radius){
		if(Math.sqrt(Math.pow((bakuhatsu_epi_center_x - char_x), 2) + Math.pow((bakuhatsu_epi_center_y - BICHOUSEI - char_y), 2)) < ((bakuhatsu_radius + BALL_SIZE / 2) / 2)){
			return true;
		}else return false;
	}
	
	boolean isAtariBakuhatsu(int bakuhatsu_epi_center_x,int bakuhatsu_epi_center_y,double bakuhatsu_radius){
		if(Math.sqrt(Math.pow((bakuhatsu_epi_center_x - char_x), 2) + Math.pow((bakuhatsu_epi_center_y - char_y), 2)) < ((bakuhatsu_radius + BALL_SIZE / 2) / 2)){
			return true;
		}else return false;
	}
	
}
