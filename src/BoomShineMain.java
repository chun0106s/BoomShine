import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;


public class BoomShineMain extends GameFrameWork {
	
	ArrayList balls = new ArrayList();
	ArrayList bakuhatsus = new ArrayList();
	long stagetimer;
	boolean mousereleased;
	boolean click_flag = false;
	int bakuhatsu_count = 0;
	
	public int SCREEN_WIDTH = 600;
	public int SCREEN_HEIGHT = 400;
	
	int bakuhatsu_time;
	int BICHOUSEI = 22;
	int BAKUHATSU_SIZE = 100;
	
	int stage = 1;
	int stage_clear_line[] = {1,2,5,9,13,20,28,35,45,56,72,90};
	int ball_number[] = {20,24,29,35,42,50,58,64,72,80,90,100};
	
	public BoomShineMain() {
		super(600, 400, "BoomShine");
		goStartGamen();
	}

	@Override
	public void keyPressedGameMain(int keycode) {
		// This method is not used.

	}

	@Override
	public void keyReleasedGameMain(int keycode) {
		// This method is not used.

	}
	
	public void mouseReleasedGameMain(int mouse_state) {
		
		if(mouse_state == 1){
			mousereleased = true;
		}

	}
	
	public void mouseClickedGameMain(int mouse_state){
		// This method is not used.
	}

	@Override
	public void runStartGamen(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif", Font.BOLD,50));
		drawStringCenter("Boom Shine", 150, g);
		g.setFont(new Font("SansSerif", Font.BOLD,25));
		drawStringCenter("Click to start", 250, g);

	}

	@Override
	public void runStageStart(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif", Font.BOLD,50));
		drawStringCenter("Stage " + stage + " Start", 200, g);
		g.setFont(new Font("SansSerif", Font.BOLD, 25));
		drawStringCenter(stage_clear_line[stage - 1] + "/" + ball_number[stage - 1], 240, g);

	}

	@Override
	public void runStageClear(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif", Font.BOLD,50));
		drawStringCenter("Congraturations !", 200, g);

	}

	@Override
	public void runGameMain(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		
		//　要素Aと要素Bの当たり判定を行うときは、入れ子構造にする
		// A・B　だから
		Iterator it = balls.iterator();
		while(it.hasNext()){
			Ball bl = (Ball)it.next();
			bl.move(SCREEN_WIDTH, SCREEN_HEIGHT);
			bl.draw(frame1, g);
			
			if(!click_flag){
				if(bl.isAtariClick(release_x, release_y, bakuhatsu_time)){
					
					bakuhatsus.add(new Bakuhatsu(bl.char_x, bl.char_y, bl.vx, bl.vy));
					it.remove();
					break;
				}
			}
			
			Iterator it2 = bakuhatsus.iterator();
			while(it2.hasNext()){
				Bakuhatsu bkht = (Bakuhatsu)it2.next();
				if(bl.isAtariBakuhatsu(bkht.char_x, bkht.char_y ,bkht.bakuhatsu_size)){
					bakuhatsus.add(new Bakuhatsu(bl.char_x, bl.char_y, bl.vx, bl.vy));
					bakuhatsu_count++;
					it.remove();
					if(bakuhatsu_count >= stage_clear_line[stage - 1]){
						if(stage<12){
							goStageClear();
						}else{
							goAllClear();
						}
					}
					
					break;
				}
			}
			
		}
		
		it = bakuhatsus.iterator();
		while(it.hasNext()){
			Bakuhatsu bakuhatsu = (Bakuhatsu)it.next();
			bakuhatsu.draw(frame1, g);
			bakuhatsu.move();
			
			if(bakuhatsu.syometsu){
				it.remove();
			}
			
		}
		
		
		if(mousereleased){
			clickBakuhatsu(g,release_x,release_y);
		}

		if (System.currentTimeMillis() - stagetimer > 15000) goGameOver();
		
	}

	@Override
	public void runGameOver(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif", Font.BOLD,50));
		drawStringCenter("Game Over", 150, g);
		g.setFont(new Font("SansSerif", Font.BOLD,25));
		drawStringCenter("Click to restart", 250, g);
		
		

	}
	
	public void runAllClear(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif", Font.BOLD,50));
		drawStringCenter("Congraturations !", 150, g);
		g.setFont(new Font("SansSerif", Font.BOLD,25));
		drawStringCenter("You've cleared all stages !", 250, g);
	}

	@Override
	public void initStageStart() {
		
		balls = new ArrayList();
		bakuhatsus = new ArrayList();
		for(int i=0;i<ball_number[stage - 1];i++){
			balls.add(new Ball(SCREEN_WIDTH, SCREEN_HEIGHT, i));
		}
		
		click_flag = false;
		bakuhatsu_count = 0;
		stagetimer = System.currentTimeMillis();
		

	}

	@Override
	public void initStageClear() {
		// temporary this method is blank
		click_flag = false;
		mousereleased = false;
		bakuhatsu_time = 0;
		stage++;

	}

	@Override
	public void initGameOver() {
		// temporary this method is blank
		click_flag = false;
		mousereleased = false;
		bakuhatsu_time = 0;

	}
	
	public void initAllClear(){
		click_flag = false;
		mousereleased = false;
		bakuhatsu_time = 0;
		stage = 1;
	}
	
	public void clickBakuhatsu(Graphics g, int pos_x, int pos_y){
		
		if(mousereleased && !click_flag){
			bakuhatsu_time = bakuhatsu_time + 2;
			g.setColor(Color.RED);
			g.fillOval(pos_x - bakuhatsu_time / 2, pos_y - bakuhatsu_time / 2 - BICHOUSEI, bakuhatsu_time, bakuhatsu_time);
		
			if(bakuhatsu_time >= BAKUHATSU_SIZE) click_flag = true;
			
		}
	}
	
	public static void main(String[] args) {
		BoomShineMain bsm = new BoomShineMain();

	}

}
