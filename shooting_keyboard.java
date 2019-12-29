import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;

public class shooting_keyboard extends JFrame{
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public shooting_keyboard() {
		setSize(800,500);	
		setTitle("Game Example");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		final MyJPanel myJPanel = new MyJPanel();
		final Container c = getContentPane();
		c.add(myJPanel);
		setVisible(true);
	}

	public static void main(final String[] args) {
		new shooting_keyboard();
	}

	public class MyJPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
	
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		int my_x;
		int player_width, enemy_width;
		int player_height, enemy_height;
		int n = 14;
		int enemy_x[] = new int[n];
		int enemy_y[] = new int[n];
		int enemy_move[] = new int[n];
        int enemy_alive[] = new int[n];	
        
		int missile_flag;
        int my_missile_x;
        int my_missile_y;
        
        int enemy_missile_x[] = new int[n];
        int enemy_missile_y[] = new int[n];
        int enemy_missile_move[] = new int[n];
        int enemy_missile_flag[] = new int[n];
		
		int num_of_alive = 14;


		Image image, image2;
		Timer timer;

		public MyJPanel() {
			my_x = 250;
		
			final ImageIcon icon = new ImageIcon("player.jpg");
			image = icon.getImage();
			final ImageIcon icon2 = new ImageIcon("enemy.jpg");
			image2 = icon2.getImage();

			player_width = image.getWidth(this);
			player_height = image.getHeight(this);

			enemy_width = image2.getWidth(this);
			enemy_height = image2.getHeight(this);

            setBackground(Color.black);
            //mouse
			addMouseListener(this);
            addMouseMotionListener(this);
            //keyboard
            setFocusable(true);
            addKeyListener(this);

			timer = new Timer(50, this);
			timer.start();

			//placement of enemy 
			for(int i = 0;i < 7;i++)
			{
				enemy_x[i] = 70*(i+1) - 50;	
				enemy_y[i] = 50;
			}
			for(int i = 7;i < n; i++)
			{
				enemy_x[i] = 70*(i-6) - 50;
				enemy_y[i] = 100;
			}
			for(int i = 0; i < n; i++)
			{
				enemy_alive[i] = 1;
				enemy_move[i] = -10;
            }
            // enemy missile 
            for(int i = 0; i < n; i ++ )
            {
                enemy_missile_x[i] = enemy_x[i]+enemy_width/2;
                enemy_missile_y[i] = enemy_y[i]+enemy_height;
                enemy_missile_move[i] = 10 + (i%3);
                enemy_missile_flag[i] = 1;
            }
		}

		public void paintComponent(final Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, my_x, 400, this);
			for(int i = 0; i < n; i++)
			{
				if(enemy_alive[i] == 1)
				{
					g.drawImage(image2, enemy_x[i], enemy_y[i], this);
				}
			}
            //paint missile
            for(int i = 0; i < 3 ; i++)
            {
                g.setColor(Color.white);
                if(missile_flag == 1)
			    {
				    g.fillRect(my_missile_x, my_missile_y, 2, 5);
			    }
            }
            // enemy missile 
            for(int i=0;i<n;i++)
            {
                if(enemy_missile_flag[i] == 1)
                {
                    g.setColor(Color.red);
                    g.fillRect(enemy_missile_x[i],enemy_missile_y[i],2,5);
                }
             }
		}

		public void actionPerformed(final ActionEvent e) 
		{
			//move enemy at 10 pixel at regular interval
			final Dimension dim = getSize();
			for(int i=0; i < n; i++)
			{
				enemy_x[i] += enemy_move[i];
				if((enemy_x[i]<0)||(enemy_x[i]>(dim.width-enemy_width)))
				{
					enemy_move[i] = - enemy_move[i];
                }
                //projection of enemy missile
                if(enemy_alive[i] == 1)
                {
                    if(enemy_missile_flag[i]==1)
                    {
                        enemy_missile_y[i] += enemy_missile_move[i];
                        if (enemy_missile_y[i] > 500)
                        {
                            enemy_missile_flag[i] = 0;
                        }
                    }
                    else
                    {
                        enemy_missile_x[i] = enemy_x[i]+ enemy_width/2;
                        enemy_missile_y[i] = enemy_y[i];
                        enemy_missile_flag[i] = 1;
                    }
                }
                if( ((enemy_missile_x[i]+2) >= my_x) && ((my_x+player_width) > enemy_missile_x[i]) && ( (enemy_missile_y[i]+5) >= 400 ) &&  ((400 + player_height) > enemy_missile_y[i]) )
                {
                    System.out.println("===Game End===");
                    System.exit(0);
                }  
			}
            //project missile up 15pixel 
            if(missile_flag == 1)
                {
                    my_missile_y -= 15;
                    if(0 > my_missile_y)
                    {
                        missile_flag = 0;
                    }
                    // collision
                    for(int i = 0; i < n; i++)
                    {
                        if(enemy_alive[i] == 1)
                        {
                            if(my_missile_x >= enemy_x[i] && my_missile_x < enemy_x[i]+enemy_width && my_missile_y+5 > enemy_y[i] && my_missile_y <= enemy_y[i]+enemy_height)
                            {
                                enemy_alive[i] = 0;
                                enemy_missile_flag[i] = 0;
                                missile_flag = 0;
                                num_of_alive--;
                                if (num_of_alive == 0)
                                {
                                    System.out.println("===Game Clear===");
                                    System.exit(0);
                                }
                                    
                            }
                        }
                    }
                }
			repaint();
		}

		public void mouseClicked(final MouseEvent me) {
		}

		public void mousePressed(final MouseEvent me) {
            //set player missile
                if(missile_flag == 0)
                {
                   my_missile_x = my_x + player_width /2;
                   my_missile_y = 400;
                   missile_flag = 1;
                }
                
		}

		public void mouseReleased(final MouseEvent me) {
		}

		public void mouseExited(final MouseEvent me) {
		}

		public void mouseEntered(final MouseEvent me) {
		}

		public void mouseMoved(final MouseEvent me) {
			my_x = me.getX();
		}

		public void mouseDragged(final MouseEvent me){

        }
        public void keyPressed(KeyEvent e){
            Dimension dim = getSize();
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_RIGHT:
                    my_x = (my_x + 10)%dim.width;
                    break;
                case KeyEvent.VK_LEFT:
                if(my_x > 0)
                {
                    my_x = my_x - 10;
                }
                else
                {
                    my_x = dim.width;
                } 
                    break;
                 case KeyEvent.VK_X:
                    if(missile_flag == 0)
                    {
                        my_missile_x = my_x + player_width / 2;
                        my_missile_y = 400;
                        missile_flag = 1; 
                    }
                    break;
            }
        }
        public void keyReleased(KeyEvent e){

        }
        public void keyTyped(KeyEvent e){

        }
	}
}