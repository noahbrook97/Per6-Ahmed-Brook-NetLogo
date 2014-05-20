import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.event.*;
import javax.tools.*;
import java.nio.charset.*;
import javax.lang.model.SourceVersion;
import java.lang.reflect.*;

public class Netlogo {
    public static void main ( String[] args ) {
	NetlogoBoard n = new NetlogoBoard();
	/*if ( n.s.getSelectedIndex() == 0 ) {	    
	    JOptionPane.showMessageDialog ( null , "test" );
	    }*/
    }
}

class NetlogoBoard implements Runnable {
    protected Screen s;
    private Thread t;
    //HashMap<String , ArrayList<String>> methods;

    public NetlogoBoard() {
	JFrame f = new JFrame();
	s = new Screen();
	//methods = new HashMap<String , ArrayList<String>>();
	f.add ( s );
	f.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
	f.show();
	f.pack();
	t = new Thread ( this );
	t.start();
    }

    public void run() {
	boolean alreadyDone = false;
	/*if ( s.getSelectedIndex() == 0 )
	  JOptionPane.showMessageDialog ( null , "HELLO" );*/
	while ( true ) {
	    String txt = s.code.getText();
	    if ( s.getSelectedIndex() == 0 ) {
		while ( !alreadyDone ) {
		    s.iface.javafy ( s.code.getText() );
		    alreadyDone = true;
		    //JOptionPane.showMessageDialog ( null , "first tab selected" );
		}
	    }
	    if ( s.getSelectedIndex() != 0 )
		alreadyDone = false;
	}
    }
}

class Screen extends JTabbedPane implements ActionListener {
    IFace iface;
    JTextArea code;
    public Screen() {
	iface = new IFace();
	this.add ( "Interface" , iface );
	this.add ( "Info" , new JPanel() );
	code = new JTextArea("to setup ca crt 1 end to move ask turtles [ fd 1 ] end");
	code.setPreferredSize ( new Dimension ( 300 , 300 ) );
	this.add ( "Code" , code );
    }
    public void actionPerformed ( ActionEvent e ) {
	try {
	    String mthd = ( ( JButton ) e.getSource() ).getText();
	    Method m = this.getClass().getMethod ( mthd , null );
	} catch ( Exception ex ) {
	    ex.printStackTrace();
	}
	System.out.println ( "this place" );
    }
}

class Code extends JTextArea {
}

class IFace extends JPanel implements MouseListener , KeyListener , ActionListener {
    private JPanel space;
    protected myPanel f;
    //private boolean isNew;
    HashMap<String , ArrayList<String>> methods;

    public IFace() {
	space = new JPanel();
	f = new myPanel();
	//isNew = true;
	//methods = new HashMap<String , ArrayList<String>>();
	f.setPreferredSize ( new Dimension ( 200 , 300 ) );
	//f.setBackground ( Color.BLUE );
	//space.add ( new JButton ( "setup" ) );
	//space.add ( new JButton ( "move" ) );
	this.add ( space );
	this.add ( f );
	this.setPreferredSize ( new Dimension ( 400 , 400 ) );
	addMouseListener ( this );
    }

    public void javafy ( String s1 ) {
	String s = new String();
	for ( int i = 0 ; i < s1.length() ; i++ )
	    s = s + s1.substring ( i , i + 1 );
	//String s = s1;
	//System.out.println ( s );
	methods = new HashMap<String , ArrayList<String>>();
	//System.out.println ( "new now" );
	//isNew = true;
	ArrayList<String> words = new ArrayList<String>();
	boolean inMethod = false;
	ArrayList<String> ans = new ArrayList<String>();
	while ( s.length() > 0 ) {
	    //make all newlines into spaces
	    while ( s.indexOf ( "\n" ) != -1 ) {
		int index = s.indexOf ( "\n" );
		s = s.substring ( 0 , index ) + " " + s.substring ( index + 1 );
	    }
	    if ( s.indexOf ( " " ) != -1 ) {
		words.add ( s.substring ( 0 , s.indexOf ( " " ) ) );
		s = s.substring ( s.indexOf ( " " ) + 1 );
	    }
	    else {
		words.add ( s );
		s = "";
	    }
	}
	String methodName = new String();
	for ( int i = 0 ; i < words.size() ; i++ ) {
	    String word = words.get ( i );
	    if ( word.equals ( "to" ) ) {
		methodName = words.get ( i + 1 );
		inMethod = true;		
		i = i + 1;
	    }
	    else if ( word.equals ( "end" ) ) {
		inMethod = false;
		methods.put ( methodName , ans );
		//System.out.println ( "not new now" );
		//isNew = false;
		ans = new ArrayList<String>();
	    }
	    else if ( inMethod ) {
		if ( word.equals ( "crt" ) ) {
		    ans.add ( word + ";" + words.get ( i + 1 ) );
		    i = i + 1;
		}
		else if ( word.equals ( "ask" ) ) {
		    String addLine = word + ";";
		    while ( ! word.equals ( "]" ) ) {
			i = i + 1;
			word = words.get ( i );
			addLine = addLine + word + ";";
		    }
		    ans.add ( addLine );
		}
		else ans.add ( word );
	    }
	}
	//System.out.println ( "methods: " + methods.entrySet() );
	//System.out.println ( "method keys: " + methods.keySet() );
	//System.out.println ( "method values: " + methods.values() );
    }

    public void mouseExited ( MouseEvent e ) {
	System.out.println ( "mouseExited" );
    }
    public void mouseEntered ( MouseEvent e ) {
	System.out.println ( "mouseEntered" );
    }
    public void mouseReleased ( MouseEvent e ) {
	System.out.println ( "mouseReleased" );
    }
    public void mousePressed ( MouseEvent e ) {
	System.out.println ( "mousePressed" );
    }
    public void mouseClicked ( MouseEvent e ) {
	System.out.println ( "mouseClicked" );
	if ( SwingUtilities.isRightMouseButton ( e ) ) {
	    System.out.println ( "right clicked" );
	    JPopupMenu menu = new JPopupMenu();
	    JMenuItem button = new JMenuItem ( "Button" );
	    button.addActionListener ( this );
	    menu.add ( button );
	    menu.add ( "Slider" );
	    menu.add ( "Switch" );
	    menu.add ( "Chooser" );
	    menu.add ( "Input" );
	    menu.add ( "Moniter" );
	    menu.add ( "Plot" );
	    menu.add ( "Output" );
	    menu.add ( "Note" );
	    menu.show ( e.getComponent() , e.getX() , e.getY() );
	}
    }
    public void keyReleased ( KeyEvent e ) {
    }
    public void keyPressed ( KeyEvent e ) {
    }
    public void keyTyped ( KeyEvent e ) {
    }
    public void actionPerformed ( ActionEvent e ) {
	try {
	if ( ( ( JMenuItem ) e.getSource() ).getText().equals ( "Button" ) ) {
	    String s = JOptionPane.showInputDialog ( null , "Type name of button" );
	    JButton button = new JButton ( s );
	    space.add ( button );
	    button.addActionListener ( this );
	}
	}
	catch ( ClassCastException ex ) {
	    try {
	    String mthds = ( ( JButton ) e.getSource() ).getText();
	    ArrayList<String> listOfMethods = methods.get ( mthds );
	    for ( String mthd : listOfMethods ) {
		if ( mthd.contains ( ";" ) ) {
		    Method m = f.getClass().getMethod ( mthd.substring ( 0 , mthd.indexOf ( ";" ) ) , String.class );
		    m.invoke ( f , mthd.substring ( mthd.indexOf ( ";" ) + 1 ) );
		}
		else {
		    Method m = f.getClass().getMethod ( mthd , null );
		    m.invoke ( f , null );
		}
	    }
	    } catch ( Exception exc ) {
	    System.out.println ( "nahh bro" );
	    System.out.println ( methods );
	    String methd = ( (JButton) e.getSource() ).getText();
	    if ( methods.isEmpty() ) {
		//System.out.println ( isNew );
		JOptionPane.showMessageDialog ( null , "yes" );
		System.exit(0);
	    }
	    else System.out.println ( "no" );
	    /*for ( int i = 0 ; i < 9999 ; i++ ) {
		System.out.println ( "here" );
		System.out.println ( methods );
		System.out.println ( methd );
		for ( String mthd : methods.get ( methd ) ) {
		    System.out.println ( mthd );
		    }
		    }*/
	    exc.printStackTrace();
	    }
	}
    }
}

class myPanel extends JPanel {
    private Patch[][] panels;
    private ArrayList<Turtle> turtles = new ArrayList<Turtle>();
    private Color backgroundColor;
    //private int xcor = 13 , ycor = 13;
    public myPanel() {
	super.setLayout ( new GridLayout ( 25 , 25 ) );
	panels = new Patch [ 25 ] [ 25 ];
	backgroundColor = Color.BLACK;
	for ( int r = 0 ; r < 25 ; r++ ) {
	    for ( int c = 0 ; c < 25 ; c++ ) {
		Patch p = new Patch();
		p.setBackground ( Color.BLACK ); 
		this.add ( p );
		panels [ r ] [ c ] = p;
	    }
	}
    }
    public void ca() {
	backgroundColor = Color.BLACK;
	for ( JPanel[] panelRows : panels ) {
	    for ( JPanel panel : panelRows ) {
		panel.setBackground ( Color.BLACK );
	    }
	}
    }
    public void crt ( String s ) {
	int nums = Integer.parseInt ( s );
	Turtle turtle = new Turtle ( panels.length / 2 , panels [ panels.length / 2 ].length / 2 );
	turtles.add ( turtle );
	try {
	    Image image = ImageIO.read ( getClass().getResource ( "arrow.gif" ) );
	    panels [ panels.length / 2 ] [ panels [ panels.length / 2 ].length / 2 ].setImage ( image );
	    turtle.setImage ( image );
	} catch ( Exception e ) {
	    System.out.println ( "come on man" );
	}
	//panels [ panels.length / 2 ] [ panels [ panels.length / 2 ].length / 2 ].setBackground ( Color.GREEN );
    }
    public void ask ( String s1 ) {
	ArrayList<String> agents = new ArrayList<String>();
	ArrayList<String> commands = new ArrayList<String>();
	String s = new String();
	for ( int i = 0 ; i < s1.length() ; i++ )
	    s = s + s1.substring ( i , i + 1 );
	boolean insideWith = false;
	while ( s.indexOf ( "[" ) != 0 ) {
	    agents.add ( s.substring ( 0 , s.indexOf ( ";" ) ) );
	    s = s.substring ( s.indexOf ( ";" ) + 1 );
	    //if ( s.substring ( 0 , s.indexOf ( ";" ) ).equal
	}
	s = s.substring ( 2 );
	System.out.println ( agents );
	while ( s.indexOf ( "]" ) != 0 ) {
	    commands.add ( s.substring ( 0 , s.indexOf ( ";" ) ) );
	    s = s.substring ( s.indexOf ( ";" ) + 1 );
	}
	System.out.println ( commands );
	System.out.println ( agents.size() );
	if ( agents.size() == 1 ) {
		System.out.println ( "hi" );
	    if ( agents.equals ( "patches" ) )
		System.out.println ( "here" );
	    if ( agents.get ( 0 ).equals ( "turtles" ) ) {
		for ( int i = 0 ; i < commands.size() ; i++ ) {
		    if ( commands.get ( i ).equals ( "fd" ) ) {
			System.out.println ( "fd" );
			for ( Turtle turtle : turtles ) {
			    int xcor = turtle.getXcor();
			    int ycor = turtle.getYcor();
			    int steps = Integer.parseInt ( commands.get ( i + 1 ) );
			    panels [ ycor ] [ xcor ].setImage ( null );
			    i = i + 1;
			    ycor = ycor + steps;
			    System.out.println ( "x: " + xcor + "\ny: " + ycor );
			    panels [ ycor ] [ xcor ].setImage ( turtle.getImage() );
			    turtle.setXcor ( xcor );
			    turtle.setYcor ( ycor );
			}
		    }
		}
	    }
	}
	else if ( agents.size() > 1 ) {
	    String agentType = agents.get ( 0 );
	    if ( agents.get ( 1 ).equals ( "with" ) ) {
		String[] restrictions = new String [ agents.size() - 3 ];
		for ( int i = 2 ; !agents.get ( i ).equals ( "]" ) ; i++ )
		    restrictions [ i - 2 ] = agents.get ( i );
		System.out.println ( Arrays.toString ( restrictions ) );
	    }
	    //if ( agent.get ( 1 ).equals ( "at" ) ) {
	    //}
	}
	//while ( 
    }
}

class Patch extends JPanel {
    private Image image;
    public Patch() {
	image = null;
    }
    public void setImage ( Image image ) {
	this.image = image;
	update ( this.getGraphics() );
	System.out.println ( "patch update here" );
    }
    public void paintComponent ( Graphics g ) {
	super.paintComponent ( g );
	g.drawImage ( image , 0 , 0 , null );
    }
}

class Turtle extends JPanel {
    private int xcor;
    private int ycor;
    private Image image;
    public Turtle ( int xcor , int ycor  ) {
	this.xcor = xcor;
	this.ycor = ycor;
    }
    public int getXcor() {
	return xcor;
    }
    public int getYcor() {
	return ycor;
    }
    public void setXcor ( int newX ) {
	xcor = newX;
    }
    public void setYcor ( int newY ) {
	ycor = newY;
    }
    public void setImage ( Image image ) {
	this.image = image;
	update ( this.getGraphics() );
	System.out.println ( "update here" );
    }
    public Image getImage() {
	return image;
    }
    public void paintComponent ( Graphics g ) {
	super.paintComponent ( g );
	g.drawImage ( image , 0 , 0 , null );
    }
}
