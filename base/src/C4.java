/*
 * Autor: Carlos Eduardo Saraiva.
 * 
 * Esse projeto � baseado no jogo Connect4, usando matrizes de bot�es, o WindowBuilder e classes espec�ficas para
 * desenvolvimento de um ambiente gr�fico em java. Foi um projeto proposto pelo professor de Programa��o estruturada modular.
 * Tentei fazer usando o paradigma de programa��o a qual estou acos-
 * 
 * 
 * tumado mesmo porque n�o sei nada de orienta��o a objetos. Todos os m�todos criados foi pensando em fun��es e pro-
 * cedimentos na programa��o estruturada modular. Tamb�m usei vari�veis em ingles, para treinar a lingua e com a inten
 * ��o de tornar algo leg�vel para qualquer pessoa de qualquer outro idioma.
 * 
 * Tamb�m disponilizei o projeto no GitHub: https://github.com/CarlosSaraiva/Connect4 , para quem sabe no futuro
 * ampliar mas inten��o agora � entender como funciona o esquema do 'git'.
 * 
 * H� alguns bugs que n�o consegui solucionar, mas o mais cr�tico � a qual o programa em algumas vezes n�o reconhece
 * um jogador quando este fez uma diagonal. Pensei em um algoritimo recursivo, para detectar as diagonais e na maioria das
 * vezes funciona. S� n�o consegui identificar porque as vezes o programa passa batido em certos casos. Mas suspeito que 
 * n�o s�o os metodos para diagonais em si, e sim em rela��o ao m�todo 'verifyWinner'. 
 * 
 * N�o tive muito tmepo de comentar o c�digo, mas as principais partes deixei em destaque.
 * 
 * Por enquanto � isto. Divirta-se. (=
 
 */

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
//import java.awt.Component;
//import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class C4  extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JFrame frame;
	private static JButton[][] slot = new JButton[7][7]; 
	private static int Player = 1 ;
	private static int arrayState[][] = new int[7][7];
	private static  Icon empty = new ImageIcon("img/vazio.jpg");
	private static  Icon red = new ImageIcon("img/vermelho.jpg");
	private static  Icon blue = new ImageIcon("img/azul.jpg");
	private static int linFound , colFound ;
	private static JLabel lblStatus = new JLabel("");
	private static int verDrawn = 0;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					C4 window = new C4();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

				
	}

	/**
	 * Create the application.
	 */
	public C4() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("img/C4ICO.jpg"));
		
		frame.setBounds(100, 100, 461, 401);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(7, 7, 0, 0));
		
		JPanel panelSouth = new JPanel();
		frame.getContentPane().add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new FlowLayout());
		
		JLabel lblNewLabel = new JLabel("");
		panelSouth.add(lblNewLabel);
		lblStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			
		panelSouth.add(lblStatus);
			
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnGame = new JMenu("Game");
		menuBar.add(mnGame);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gameStart();
			}
		});
		mnGame.add(mntmNew);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
		mnGame.add(mntmQuit);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		int slotCounter = 0;

		for (int lin = 0 ;lin < slot.length; lin++ )
		{
			for (int col = 0; col < slot[0].length; col++)
			{
				slot[lin][col] = new JButton();
				slot[lin][col].setIcon(empty);
				slot[lin][col].setName(Integer.toString(slotCounter));

				panel.add(slot[lin][col]);

				slot[lin][col].addActionListener(this);
			}
		}
		gameStart();
	}
//
	public static void gameStart()
	{
		
		Player = 1;
		lblStatus.setText("Player 1 turn");
		verDrawn = 0;
		
		for (int i = 0; i < arrayState.length;i++)
		{
			for (int j = 0; j < arrayState[0].length;j++)
			{
					arrayState[i][j] = 0;
					slot[i][j].setIcon(empty);
				}
			}
	
		System.out.println("The state from all buttons are setted to '0'");
			
	}
		
	public void actionPerformed(ActionEvent event)
	{
		//JButton btnPressed = (JButton)event.getSource();
		
		linFound = indexSearch(event,"lin");
		colFound = indexSearch(event,"col");	
		
		System.out.println("");
		System.out.println("The button on line: " + (linFound + 1) + " and columm: " +  (colFound + 1)  + " were clicked.");		
		
		
		boolean playerStored = setArrayState(linFound, colFound, Player,event);
		
		
		int lineChecked = linScan(Player);
		int colUpChecked = colScanUp(Player,0,(arrayState.length - 1) ,colFound);
		int colDownChecked = colScanUp(Player,0,(arrayState.length - 1) ,colFound);
		int diagUpChecked = diagScanUp(linFound, colFound,0, Player) ;
		int diagDownChecked = diagScanDown(linFound, colFound,0, Player) ;
		int diagInvUpChecked = diagInvScanUp(linFound, colFound, 0, Player);
		int diagInvDownChecked = diagInvScanDown(linFound, colFound, 0, Player);
		
		boolean winner = verifyWinner(lineChecked, colUpChecked,colDownChecked,diagUpChecked,diagDownChecked, diagInvUpChecked,diagInvDownChecked,  Player);

		verDrawn = verDrawn + 1;
		
		if (verDrawn == 49 && winner == false)
		{
			lblStatus.setText("Drawn!");
		}
		
		
		if (winner == true)
		{
			JOptionPane.showMessageDialog(null,"Player " + Player + " wins");
			lblStatus.setText("Player "  + Integer.toString(Player) + " wins!");
			gameStart();

		}	

			else if (playerStored == true)
			{
				Player = setPlayer(Player);
				lblStatus.setText("Player "  + Integer.toString(Player) + " turn");
	
			}

		
		
		//printArrayState();
		
	}

	//Método responsável pela mudança de ícone nos botões.

	public static void buttonIcons(int player, int lin, int linClicked, int col)
	{


			if (player == 1)
			{
				slot[lin][col].setIcon(blue);
			}
			else
			{
				slot[lin][col].setIcon(red);
			}

	}


	public static int zeroLineSearch(int lin, int col)
	{
		if(lin < 0)
		{
			System.out.println("No more zero's values in this columms");
			
			return -1;
		}
		
		if (arrayState[lin][col] == 0)
		{
			int linFound = lin;
			lin = 0;
			System.out.println("Zero found in line: " + linFound + " from this collum " +"("+ col + ").");
			return linFound;
		}
		
		else
		{			
			return zeroLineSearch(lin - 1,col);
		}
		
	}

	//Imprime uma tabela da matriz que armazena os jogadores no console.
	
	public static void printArrayState()
	{
		for (int i =0; i < arrayState.length; i++)
		{
			for (int j = 0; j < arrayState[0].length; j++)
			{
				System.out.print(arrayState[i][j] + "\t");
				
			}
			System.out.println("");
		}
	}

	public static int setPlayer(int Player)
	{
		if (Player == 1)
		{
			System.out.println("Player 2  is now playing.");
			
			return 2;	
		}
		
		else
		{
			System.out.println("Player 1  is now playing.");
			
			return 1;
		}
	}

	public static boolean setArrayState(int lin, int col, int player,ActionEvent event)
	{
		
		
		boolean V =false;
		

		if(arrayState[lin][col] != 0) 
		{

			System.out.println("This button already have a value: " + arrayState[lin][col]+". Select another one. " );
			System.out.println("Player " + player + " still playing.");

			return false;
		}
		
		int zeroLineFound = zeroLineSearch((arrayState.length - 1), col);

		if (zeroLineFound < 0) 
		{

			V = false;
		}
		
		else if(arrayState[zeroLineFound][col] == 0)
		{
			arrayState[zeroLineFound][col] = player;
			buttonIcons(player,zeroLineFound,lin,col);
			System.out.println("Player " + player + " was stored on array: " + zeroLineFound + " columm: " + col);
			V = true;	
		}		
		
		return V;
		
		
	}

	public static int indexSearch(ActionEvent event, String indexSelect)
	{
		JButton btnPressed = (JButton)event.getSource();

		int  found = 0;

		for (int lin = 0; lin < slot.length; lin++)
		{
			for (int col = 0; col < slot.length; col++)
			{

				if (btnPressed == slot[lin][col])
				{
					if (indexSelect == "lin")
					{
						found = lin;
					}
					else
					{
						found = col;
					}


				}


			}
		}
		return found;

	}

	public static int colScanUp(int player, int count, int lin, int col)
	{
		if(lin >= arrayState.length || col >=arrayState.length || col < 0 || lin < 0)
		{
			return -1;
		}
		
		if(count == 4)
		{
			
			return player;
		}

		
		if (arrayState[lin][col] == 0)
			
		{
			return colScanUp(player, 0, lin - 1, col);
		}
		
		if(arrayState[lin][col] == player)
						
		{
			
			return colScanUp(player, count + 1, lin - 1, col);
			
		}
					
		if (arrayState[lin][col] != player)
							
		{
			count = 0;
			player = arrayState[lin][col];
			return colScanUp(player, count + 1,lin - 1, col);
		}
		else
	
			return -1;
		
	}
	
	public static int colScanDown(int player, int count, int lin, int col)
	{
		if(lin >= arrayState.length || col >=arrayState.length || col < 0 || lin < 0)
		{
			return -1;
		}
		
		if(count == 4)
		{
			
			return player;
		}

		
		if (arrayState[lin][col] == 0)
			
		{
			return colScanDown(player, 0, lin + 1, col);
		}
		
		if(arrayState[lin][col] == player)
						
		{
			
			return colScanDown(player, count + 1, lin + 1, col);
			
		}
					
		if (arrayState[lin][col] != player)
							
		{
			count = 0;
			player = arrayState[lin][col];
			return colScanDown(player, count + 1,lin + 1, col);
		}
		else
	
			return -1;
		
	}
		
	
	public static int linScan(int player)
	{
		int count = 0;		
		
		for (int i = 0; i < arrayState.length; i++)
		{
			for (int j = 0; j < arrayState[0].length; j++)

			{
				if (count == 4)
				{
					 break;
				}
				
				if (arrayState[i][j] == 0)
				{
					
					count = 0;
				}
				
				if (arrayState[i][j] == player)
				{
					count = count + 1;
					
				}
				if (arrayState[i][j]  != player)
				{	
					
					count = 0;
										
				}				

			}

		}
		if (count == 4)
		{
			return player;
		}
		else
			return -1;
	}

	public static int diagScanUp(int lin, int col, int count, int player)

	{

		if (count == 4)
		{

			return player;	
		}
		
		if(lin >= arrayState.length || col >= arrayState.length || lin < 0 || col < 0)
		{

			return   -1;

		}

		if (arrayState[lin][col] == player)
		{												
			return diagScanUp(lin - 1, col + 1 , count + 1, player);				
		}

		if (arrayState[lin][col] != player)
		{
			count = 0;
			player = arrayState[lin][col];								

			return diagScanUp(lin - 1, col + 1, count + 1, player);					
		}

		if (arrayState[lin][col] == 0)
		{

			return diagScanUp(lin - 1, col + 1 , 0, player);

		}						 

		return -1;

	}

	public static int diagScanDown(int lin, int col, int count, int player)

	{
	
	if (count == 4)
	{
		
		return player;	
	}	
	
	if(lin >= arrayState.length || col >= arrayState.length || lin < 0 || col < 0)
	{
		return   -1;
	}
							
	if (arrayState[lin][col] == player)
	{												
		return diagScanDown(lin + 1, col - 1 , count + 1, player);				
	}
			
	 if (arrayState[lin][col] != player)
	{
		count = 0;
		player = arrayState[lin][col];								
		
		return diagScanDown(lin + 1, col - 1, count + 1, player);					
	}
				
	  if (arrayState[lin][col] == 0)
	{					
		return diagScanDown(lin + 1, col - 1 , 0, player);
	}
	  else									
	return -1;
	
}

	public static boolean verifyWinner(int lin, int colUp,int colDown, int diagUp, int diagDown, int diagInvUp, int diagInvDown, int player)
	{
		if ((lin == player) || (colUp== player) || (colDown == player) || (diagUp == player) ||  (diagDown == player) || (diagInvUp == player) || (diagInvDown == player))
		{
			return true;			
		}
		else
			return false;

		
	}

	public static int diagInvScanDown(int lin, int col, int count, int player)

	{
		if (count == 4)
		{	

			return player;	
		}
		
		if(lin >= arrayState.length || col >= arrayState.length || lin < 0 || col < 0)
		{
			
			return   -1;
			
		}

		if (arrayState[lin][col] == 0)
		{			
			return diagInvScanDown(lin + 1, col + 1 , 0, player);
		}				

		if (arrayState[lin][col] == player)
		{												

			return diagInvScanDown(lin + 1, col + 1 , count + 1, player);
			
		}

		if (arrayState[lin][col] != player)
		{
			
			count = 0;
			player = arrayState[lin][col];								
			return diagInvScanDown(lin + 1, col + 1, count + 1, player);
			
		}
		
		else
		{

			return -1;

		}
	}

	public static int diagInvScanUp(int lin, int col, int count, int player)

	{
		if (count == 4)
		{	

			return player;
			
		}	

		if(lin >= arrayState.length || col >= arrayState.length || lin < 0 || col < 0)
		{
			
			return   -1;
			
		}	
		
		if (arrayState[lin][col] == 0)

		{	
			
			return diagInvScanUp(lin - 1, col - 1 , 0, player);
			
		}				

		if (arrayState[lin][col] == player)

		{												

			return diagInvScanUp(lin - 1, col - 1 , count + 1, player);
			
		}

		if (arrayState[lin][col] != player)
		{
			count = 0;
			player = arrayState[lin][col];								

			return diagInvScanUp(lin - 1, col - 1, count + 1, player);						
		}

		else

		{
			return -1;
		}

	}

}

