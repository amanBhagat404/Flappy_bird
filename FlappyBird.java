package FlappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener,KeyListener {
	
	public static FlappyBird flappyBird;
	
	public final int WIDTH=900, HEIGHT=800;
	
	public Renderer renderer ;
	
	public Rectangle bird;
	
	ArrayList<Rectangle> columns;
	
	public boolean gameOver,gameStart=false;
	
	public int ticks=0,yMotion=0,score=0;
	
	public FlappyBird() {
		JFrame jframe =new JFrame();
		Timer timer =new Timer(20,this);
		
		renderer=new Renderer();
		jframe.add(renderer);
		jframe.addKeyListener(this);
		jframe.setBounds(500, 150, WIDTH, HEIGHT);
		jframe.setTitle("Flappy Bird");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		jframe.setSize(WIDTH,HEIGHT); 
		jframe.setResizable(false);
		jframe.setVisible(true);
		 
		bird=new Rectangle(WIDTH/2-20,HEIGHT/2-20,20,20);
		columns=new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
	}
	
	public void addColumn(boolean start) {
		int space =300;
		int width=100;
		Random rand=new Random();
		int height=50+rand.nextInt(300);
		
		if(start) {
			columns.add(new Rectangle(WIDTH+width+columns.size()*300,HEIGHT-height-120,width,height));
			columns.add(new Rectangle(WIDTH+width+(columns.size()-1)*300,0,width,HEIGHT-height-space));
		}
		else {
			columns.add(new Rectangle(columns.get(columns.size()-1).x+600,HEIGHT-height-120,width,height));
			columns.add(new Rectangle(columns.get(columns.size()-1).x,0,width,HEIGHT-height-space));
		}
		
	}
	
	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.black);
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	
	public void jump() {
		if(gameOver) {
			bird=new Rectangle(WIDTH/2-20,HEIGHT/2-20,20,20);
			columns.clear();
			yMotion=0;
//			bird.y=0;
			gameStart=false;
			gameOver=false;
			score=0;
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
		}
		if(!gameStart) {
			gameStart=true;
//			score=0;
		}
		else if(!gameOver) {
			if(yMotion >0) {
				yMotion=0;
			}
			yMotion-=10;
//			score+=10;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ticks++;
		if(gameStart) {
			int speed=10;
			for(int i=0;i<columns.size();i++) {
				Rectangle column=columns.get(i);
				column.x-=speed;
//				score+=10;
			}
			if(ticks%2==0&&yMotion < 15) {
				yMotion+=2;
			}
			for(int i=0;i<columns.size();i++) {
				Rectangle column=columns.get(i);
				if(column.x+ column.width <0) {
					columns.remove(column);
					if(column.y==0) {
						addColumn(false);
					}
				}
			}
			bird.y+=yMotion;
			
			for(Rectangle column : columns) {
				if(column.y==0 && bird.x+bird.width/2 > column.x+column.width/2-10 && bird.x+bird.width/2 < column.x+column.width/2+10) {
					score+=1;
				}
				if(column.intersects(bird)) {
					gameOver=true;
					bird.x=column.x-bird.width;
				}
//				score+=10;
			}
			if(bird.y > HEIGHT-120 || bird.y<0) {
				gameOver=true;
			}
			if(bird.y+yMotion >=HEIGHT-120) {
				 bird.y=HEIGHT-120-bird.height;
			 }
		}
		
		renderer.repaint();
	}
	 void repaint(Graphics g) {
		 g.setColor(Color.gray);
		 g.fillRect(0, 0, WIDTH, HEIGHT);
		 
		 g.setColor(Color.orange);
		 g.fillRect(0, HEIGHT-100, WIDTH, 100);
		 
		 g.setColor(Color.green);
		 g.fillRect( 0, HEIGHT-120, WIDTH, 20);
		 
		 g.setColor(Color.red);
		 g.fillRect(bird.x,bird.y, bird.width, bird.height);
		 
		 for(Rectangle column: columns) {
			 paintColumn(g, column);
//			 score+=10;
		 }
		 
		 g.setColor(Color.white);
		 g.setFont(new Font("Arial",1,100));
		 if(!gameStart) {
			 g.drawString("Click to start!", 120, HEIGHT/2-200);
		 }
		 if(gameOver) {
			 String s=Integer.toString(score);
			 g.drawString("Game Over!", 150, HEIGHT/2-100);
			 g.drawString("your score :"+s, 100, HEIGHT/2+0);
			 g.drawString("click to start!", 150, HEIGHT/2+100);
		 }
		 if(!gameOver && gameStart) {
			 g.drawString("score: "+String.valueOf(score), 250, 80);
		 }
	}
   
	


	public static void main(String[] args) {
		flappyBird=new FlappyBird();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		jump();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
 