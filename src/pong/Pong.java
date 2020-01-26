package pong;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import java.util.Timer;

public class Pong extends Canvas
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Point delta;
	static Ellipse2D.Double ball;
	static int speed = 3;
	static Rectangle paddle1;
	static Rectangle paddle2;
	static int p1Score = -1;
	static int p2Score = -1;
	static int[] winSize = new int[2];
	static boolean winner = false;
	static JFrame win;
	static String result = "";
	
	public static void main( String[] args )
	{
		win = new JFrame("Pong");
		win.setSize(1900,1000);
		win.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		win.add( new Pong() );
		win.setVisible(true);
		winSize = getWinSize();
	}

	public Pong()
	{
		enableEvents(java.awt.AWTEvent.KEY_EVENT_MASK);
		requestFocus();
		
		ball = new Ellipse2D.Double((winSize[0]/2),(winSize[1]/2),40,40);
		delta = new Point(speed,-speed);
		paddle1 = new Rectangle(50,450,20,150);
		paddle2 = new Rectangle(1800,450,20,150);
		
		
		Timer t = new Timer(true);
		
		t.schedule( new java.util.TimerTask(){
		
			public void run()
			{
				paddle2.x = winSize[0] - 100;
				doStuff();
				
			
				
				
				
				repaint();
				
				
				
				
				winSize = getWinSize();
				win.setTitle(scoreToString());
				
			}
		}, 10, 1);

	}
	
	
	public void paint(Graphics g) {
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
	    
		Graphics2D g2 = (Graphics2D)g;
		


		g2.setColor(Color.black);
		g2.setFont(new Font("Arial",Font.BOLD,60));
		g2.drawString("" + p1Score,400,60);
		g2.drawString("" + p2Score,winSize[0]-500,60);
		g2.drawString(result, (win.getWidth()/2)-240, win.getHeight()/2);
		
		
		g2.fill(ball);
		
		
		g2.fill(paddle1);
		g2.fill(paddle2);  
		
		
	
	    
	}

	public void update(Graphics g) {
	   
		Graphics offgc;
	    Image offscreen = null;
	    Dimension size = getSize();
		Dimension d = size;

	    // create the offscreen buffer and associated Graphics
	    offscreen = createImage(d.width, d.height);
	    offgc = offscreen.getGraphics();
	    // clear the exposed area
	    offgc.setColor(getBackground());
	    offgc.fillRect(0, 0, d.width, d.height);
	    offgc.setColor(getForeground());
	    // do normal redraw
	    paint(offgc);
	    // transfer offscreen to window
	    g.drawImage(offscreen, 0, 0, this);
	    }
		
		
	
	/*
	
	public void paint( Graphics g )
	{
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.black);
		g2.setFont(new Font("Arial",Font.BOLD,60));
		g2.drawString("" + p1Score,400,60);
		g2.drawString("" + p2Score,winSize[0]-500,60);
		g2.drawString(result, (win.getWidth()/2)-240, win.getHeight()/2);
		
		
		g2.fill(ball);
		
		
		g2.fill(paddle1);
		g2.fill(paddle2);
		
		
	} */

	public void processKeyEvent(KeyEvent e)
	{
		if ( e.getID() == KeyEvent.KEY_PRESSED )
		{
			if ( e.getKeyCode() == KeyEvent.VK_W )
			{
				if (paddle1.y > 20) {
				paddle1.y -= 20; 
				}
				else if(paddle1.y <= 20 ) {
					paddle1.y = 0;
				}
			}
			
			if ( e.getKeyCode() == KeyEvent.VK_S )
			{
				if (paddle1.getY() < winSize[1]-200)
				paddle1.y += 20;
			}
			
			if ( e.getKeyCode() == KeyEvent.VK_UP)
			{
				if (paddle2.y > 20) {
				paddle2.y -= 20; 
				}
				else if(paddle2.y <= 20 ) {
					paddle2.y = 0;
				}
			}
			
			if ( e.getKeyCode() == KeyEvent.VK_DOWN )
			{
				if (paddle2.getY() < winSize[1]-200)
				paddle2.y += 20;
			}
			
			if ( e.getKeyCode() == KeyEvent.VK_EQUALS )
			{
				speed ++;
			}
			
			if ( e.getKeyCode() == KeyEvent.VK_MINUS)
			{
				speed --;
			}
		}
	}
	
	public static void doStuff(){
		
			
			
			ball.x += delta.x;
			ball.y += delta.y;

			// and bounce if we hit a wall
			if ( ball.y < 0 || ball.y > winSize[1] -100 )
				delta.y = -delta.y;
			
			
			// check if the ball is hitting the paddle
			if ( ball.intersects(paddle1) )
				delta.x = -delta.x;
			
			if ( ball.intersects(paddle2) )
				delta.x = -delta.x;
			
			// check for scoring
			isGoal();
			
			if(p1Score == 10|| p2Score == 10) {
				speed = 0;
				ball.height = 0;
				ball.width = 0;
				result = displayResult();
			}
			
			
		
	}
	
	private static String displayResult() {
		String result;
		if(p1Score > p2Score) {
			result = "Player 1 Wins!";
		}
		else {
			result = "Player 2 Wins!";
		}
		return result;
	}

	static int[] getWinSize() {
	
		int[] result = new int[2];
		result[0] = win.getWidth();
		result[1] = win.getHeight();
		
		return result;
		
	}
	
	static String scoreToString() {
		
		if (p1Score > 10) {
			p1Score = 10;
		}
		if (p2Score > 10) {
			p2Score = 10;
		}
		
		return String.format("PONG                            %d                        "
				                    + "Speed = %d"
				                    + "                                %d",p1Score,speed,p2Score);                         
			
	}
	public static void isGoal() {
		if ( ball.getX() > winSize[0] ) {
			p1Score ++;
			ball.x = (winSize[0]/2);
			ball.y = (winSize[1]/2);
			delta = new Point(-speed,speed);
		}
		
		if( ball.getX() < 0) {
			p2Score ++;
			ball.x = (winSize[0]/2);
			ball.y = (winSize[1]/2);
			delta = new Point(-speed,speed);
		}
	}

	
	public boolean isFocusable() { return true;	}
}
