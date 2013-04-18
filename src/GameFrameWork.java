

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;



abstract public class GameFrameWork {
	public static final int GS_STARTGAMEN = 0;
	public static final int GS_STAGESTART = 1;
	public static final int GS_STAGECLEAR = 2;
	public static final int GS_GAMEOVER = 3;
	public static final int GS_GAMEMAIN = 4;
	public static final int GS_ALLCLEAR = 5;
	private int gamestate;
	private int waittimer;
	
	public BufferStrategy bstrategy;
	public JFrame frame1;
	public Sequencer midiseq = null;
	
	public boolean mouse_released;
	
	public int release_x;
	public int release_y;
	
	

	
	//　ここがコンストラクタ
	GameFrameWork(int w, int h, String title){
		frame1 = new JFrame(title);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setBackground(Color.WHITE);
		frame1.setResizable(false);
		
		frame1.setVisible(true);
		Insets insets = frame1.getInsets();
		frame1.setSize(w + insets.left + insets.right, h + insets.top + insets.bottom);
		frame1.setLocationRelativeTo(null);
		
		frame1.createBufferStrategy(2);
		bstrategy = frame1.getBufferStrategy();
		frame1.setIgnoreRepaint(true);
		 
		frame1.addKeyListener(new MyKeyAdapter());
		frame1.addMouseListener(new MyMouseAdapter());
	}
	
	void drawStringCenter(String str, int y, Graphics g){
		int fw = frame1.getWidth() / 2;
		FontMetrics fm = g.getFontMetrics();
		int strw = fm.stringWidth(str) / 2;
		g.drawString(str,  fw-strw,  y);
	}
	void midiYomikomi(String fname){
		if(midiseq == null){
			try{
				midiseq = MidiSystem.getSequencer();
				midiseq.open();
			}catch(MidiUnavailableException e1){
				e1.printStackTrace();
			}
		}
		try{
			Sequence seq = MidiSystem.getSequence(getClass().getResource(fname));
			midiseq.setSequence(seq);
		}catch(InvalidMidiDataException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	Clip otoYomikomi(String fname){
		Clip clip = null;
		
		try{
		AudioInputStream aistream = AudioSystem.getAudioInputStream(getClass().getResource(fname));
		DataLine.Info info = new DataLine.Info(Clip.class,  aistream.getFormat());
		clip = (Clip)AudioSystem.getLine(info);
		clip.open(aistream);
		return clip;
	}catch(UnsupportedAudioFileException e){
		e.printStackTrace();
	}catch(IOException e){
		e.printStackTrace();
	}catch(LineUnavailableException e){
		e.printStackTrace();
	}
		return clip;
	}
	
	public abstract void keyPressedGameMain(int keycode);
	
	public abstract void keyReleasedGameMain(int keycode);
	
	class MyKeyAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent ev){
			if(gamestate == GS_GAMEMAIN){
				keyPressedGameMain(ev.getKeyCode());
			}
		}
		public void keyReleased(KeyEvent ev){
			int keycode = ev.getKeyCode();
			switch(gamestate){
			case GS_GAMEMAIN:
				keyReleasedGameMain(keycode);
				break;
			case GS_STARTGAMEN:
				if(keycode == KeyEvent.VK_P) goStageStart();
				break;
			case GS_GAMEOVER:
				if(keycode == KeyEvent.VK_R) goStageStart();
			}
		}
	}
	
	public abstract void mouseClickedGameMain(int mouse_state); 
	
	public abstract void mouseReleasedGameMain(int mouse_state);
	
	class MyMouseAdapter extends MouseAdapter{
		
		public void mousePressed(MouseEvent ev){
			//mouse_pressed = true;
			//Point point = ev.getPoint();
			//click_x = point.x;
			//click_y = point.y;
		}
		
		public void mouseReleased(MouseEvent ev){
			int mouse_state = ev.getButton();
			// getButtonで左クリックだと１が代入される
			
			// We use this method only.
			
			mouse_released = true;
			Point point = ev.getPoint();
			release_x = point.x;
			release_y = point.y;
			
			switch(gamestate){
			case GS_GAMEMAIN:
				mouseReleasedGameMain(mouse_state);
				break;
			case GS_STARTGAMEN:
				if(mouse_state == 1) goStageStart();
				break;
			case GS_GAMEOVER:
				if(mouse_state == 1) goStageStart();
			case GS_ALLCLEAR:
				if(mouse_state == 1) goStageStart();
			}
			
		}
		
	
		
	}
	
	class MyTimerTask extends TimerTask{
		public void run(){
			Graphics g = bstrategy.getDrawGraphics();
			if(bstrategy.contentsLost()==false){
				Insets insets = frame1.getInsets();
				g.translate(insets.left,  insets.top);
				
				switch(gamestate){
				case GS_STARTGAMEN:
					runStartGamen(g);
					break;
				case GS_STAGESTART:
					runStageStart(g);
					waittimer = waittimer - 1;
					if(waittimer < 0) goGameMain();
					break;
				case GS_STAGECLEAR:
					runStageClear(g);
					waittimer = waittimer - 1;
					if(waittimer < 0) goStageStart();
					break;
				case GS_GAMEMAIN:
					runGameMain(g);
					break;
				case GS_GAMEOVER:
					runGameOver(g);
					break;
				case GS_ALLCLEAR:
					runAllClear(g);
					break;
				
				}
				bstrategy.show();
				g.dispose();
			}
		}
		
	}
	
	public abstract void runStartGamen(Graphics g);
	
	public abstract void runStageStart(Graphics g);
	
	public abstract void runStageClear(Graphics g);
	
	public abstract void runGameMain(Graphics g);
	
	public abstract void runGameOver(Graphics g);
	
	public abstract void runAllClear(Graphics g);
	
	void goStartGamen(){
		gamestate = GS_STARTGAMEN;
		Timer t = new Timer();
		t.schedule(new MyTimerTask(), 10, 30);
	}
	
	void goStageStart(){
		initStageStart();
		waittimer = 100;
		gamestate = GS_STAGESTART;
	}
	
	void goStageClear(){
		initStageClear();
		waittimer = 100;
		gamestate = GS_STAGECLEAR;
	}
	
	void goGameMain(){
		gamestate = GS_GAMEMAIN;
	}
	
	void goGameOver(){
		initGameOver();
		gamestate = GS_GAMEOVER;
	}
	
	void goAllClear(){
		initAllClear();
		gamestate = GS_ALLCLEAR;
	}
	
	public abstract void initStageStart();
	
	public abstract void initStageClear();
	
	public abstract void initGameOver();
	
	public abstract void initAllClear();
	
	
	
	
	
	

}
