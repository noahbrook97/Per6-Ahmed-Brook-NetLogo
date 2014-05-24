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
	    //for forever buttons- if on, call them
	    //need: arraylist forever buttons, change background, call methods
	    for ( ForeverButton b : s.getForeverButtons() ) {
		if ( b.getBackground().equals ( Color.BLACK ) ) {
		    System.out.println ( "call method here" );
		    s.iface.callMethod ( b.getText() );
		    //call method
		}
	    }
	    //System.out.println ( "testing" );
	    HashMap<String , Integer> globals = s.iface.getGlobals();
	    for ( JLabel m : s.getMonitors() ) {
	    //for ( monitors ) : if globals = different, monitors.setText globals
		//System.out.println ( "monitors: " + m.getText() );
		String key = m.getText().substring ( 0 , m.getText().indexOf ( ":" ) );
		Integer value = Integer.parseInt ( m.getText().substring ( m.getText().indexOf ( ":" ) + 2 ) );
		if ( ! globals.get ( key ).equals ( value ) ) {
		    //System.out.println ( "changed" );
		    //JOptionPane.showMessageDialog ( null , "changed" );
		System.out.println ( "monitor should read: " + key + ": " + globals.get ( key ) );
		String newValue = globals.get ( key ).toString();
		//m.setText ( key + ": " + 0 );
		//System.out.println ( "new value: " + value );
		//m.setText ( m.getText().substring ( 0 , m.getText().length() - 1 ) + newValue );
		m.setText ( key + ": " + newValue );
		//m.setText ( key + ": " + globals.get ( key ) );
		}
		//else JOptionPane.showMessageDialog ( null , "unchanged" );
	    }
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
	code = new JTextArea("globals [ a ] to setup ca crt 1 end to move ask turtles with [ who = 0 ] [ fd 1 ] set a a + 1 end");
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
    public ArrayList<ForeverButton> getForeverButtons() {
	return iface.getForeverButtons();
    }
    public ArrayList<JLabel> getMonitors() {
	return iface.getMonitors();
    }
    public HashMap<String , Integer> getGlobals() {
	return iface.getGlobals();
    }
}

class Code extends JTextArea {
}

class IFace extends JPanel implements MouseListener , KeyListener , ActionListener {
    private JPanel space;
    protected myPanel f;
    private ArrayList<ForeverButton> foreverButtons;
    private ArrayList<JLabel> monitors;
    //private boolean isNew;
    HashMap<String , ArrayList<String>> methods;
    ArrayList<String> breeds;

    public IFace() {
	space = new JPanel();
	f = new myPanel();
	foreverButtons = new ArrayList<ForeverButton>();
	monitors = new ArrayList<JLabel>();
	breeds = new ArrayList<String>();
	//isNew = true;
	//methods = new HashMap<String , ArrayList<String>>();
	f.setPreferredSize ( new Dimension ( 300 , 300 ) );
	//f.setBackground ( Color.BLUE );
	JButton setup = new JButton ( "setup" );
	setup.addActionListener ( this );
	space.add ( setup );
	JButton move = new JButton ( "move" );
	move.addActionListener ( this );
	space.add ( move );
	this.add ( space );
	this.add ( f );
	this.setPreferredSize ( new Dimension ( 400 , 400 ) );
	addMouseListener ( this );
    }

    public ArrayList<ForeverButton> getForeverButtons() {
	return foreverButtons;
    }
    public ArrayList<JLabel> getMonitors() {
	return monitors;
    }
    public HashMap<String , Integer> getGlobals() {
	return f.getGlobals();
    }

    public void javafy ( String s1 ) {
	String s = new String();
	for ( int i = 0 ; i < s1.length() ; i++ )
	    s = s + s1.substring ( i , i + 1 );

	methods = new HashMap<String , ArrayList<String>>();
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
		System.out.println ( "word: " + word );
		if ( word.equals ( "crt" ) ) {
		    ans.add ( word + ";" + words.get ( i + 1 ) );
		    i = i + 1;
		    if ( words.get ( i + 1 ).equals ( "[" ) ) {
			System.out.println ( "crt with conditions" );
		    }
		}
		else if ( word.equals ( "ask" ) ) {
		    boolean insideWith = false;
		    int inBrackets = -1;
		    String addLine = word + ";";
		    while ( ! word.equals ( "]" ) || insideWith || inBrackets != 0 ) {
			if ( word.equals ( "[" ) )
			    inBrackets++;
			if ( word.equals ( "]" ) )
			    inBrackets--;
			if ( word.equals ( "with" ) )
			    insideWith = true;
			if ( insideWith && word.equals ( "]" ) )
			    insideWith = false;
			i = i + 1;
			word = words.get ( i );
			addLine = addLine + word + ";";
		    }
		    System.out.println ( "ask ans: " + addLine );
		    ans.add ( addLine );
		}
		else if ( word.equals ( "every" ) ) {
		    String addLine = word + ";";
		    int inBrackets = -1;
		    while ( ! word.equals ( "]" ) || inBrackets != 0 ) {
			if ( word.equals ( "[" ) )
			    inBrackets++;
			if ( word.equals ( "]" ) )
			    inBrackets--;
			i = i + 1;
			word = words.get ( i );
			addLine = addLine + word + ";";
			System.out.println ( "addLine: " + addLine );
		    }
		    ans.add ( addLine );
		}
		else if ( word.equals ( "set" ) ) {
		    System.out.println ( "globals: " + f.globals.entrySet() );
		    String addThis = word + ";";
		    i = i + 1;
		    word = words.get ( i );
		    //System.out.println ( "word in set: " + word );
		    //System.out.println ( "aaa " + "123456789/*-+".contains ( word ) );
		    
		    while ( "123456789+-*/".contains ( word )  || f.globals.containsKey ( word ) ) {
			addThis = addThis + word + ";";
			i = i + 1;
			word = words.get ( i );
		    }
		    i = i - 1;
		    System.out.println ( "addThis: " + addThis );
		    ans.add ( addThis );
		}
		else ans.add ( word );
	    }
	    else if ( word.equals ( "breed" ) ) {
		breeds.add ( words.get ( i + 2 ) + "," + words.get ( i + 3 ) );
		i = i + 4;
	    }
	    else if ( word.equals ( "globals" ) ) {
		i = i + 2;
		while ( ! words.get ( i ).equals ( "]" ) ) {
		    System.out.println ( "add to globals: " + words.get ( i ) );
		    f.globals.put ( words.get ( i ) , 0 );
		    i = i + 1;
		}
		/*//System.out.println ( "globals" );
		for ( i = i + 2 ; !word.equals ( "]" ) ; i++ ) {
		    System.out.println ( "words.get ( i ): " + words.get ( i ) + "\nword: " + word );
		    word = words.get ( i );
		    //globals.put ( words.get ( i ) , 0 );
		}
		System.out.println ( "i: " + i );
		//i = i + 3;*/
	    }
	    //else
	    //JOptionPane.showMessageDialog(null, "That doesn't match any methods");
	}
	
	System.out.println ( "methods: " + methods.entrySet() );
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

	    //create options menu with their buttons and stuff
	    JPopupMenu menu = new JPopupMenu();
	    JMenuItem button = new JMenuItem("Button" );
	    JSlider slider = new JSlider();
	    JMenuItem swtch = new JMenuItem("Switch");
	    JMenuItem monitor = new JMenuItem ( "Monitor" );

	    button.addActionListener ( this );
	    swtch.addActionListener  ( this );
	    monitor.addActionListener ( this );
	    //slider.addActionListener ( this );

	    menu.add ( button );
	    menu.add ( "Slider");
	    menu.add ( swtch );
	    menu.add ( "Chooser" );
	    menu.add ( "Input" );
	    //menu.add ( "Moniter" );
	    menu.add ( monitor );
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
	    //choose button, make button
	    System.out.println("clicked jmenuitem; " + ((JMenuItem) e.getSource() ) .getText());
	    if ( ( ( JMenuItem ) e.getSource() ).getText().equals ( "Button" ) ) {
 		//String s = JOptionPane.showInputDialog ( null , "Type name of button" );
		String[] options = { "option 1" , "option 2" };
		JCheckBox forever = new JCheckBox ( "forever" );
		Object[] params = { "Type name of button" , forever };
		String s = JOptionPane.showInputDialog ( null , params );

		
		System.out.println ( forever.isSelected() );
		//if user types something
		if ( s != null && !s.equals("") ) {
		    //if statement here saying if s does not equal a known method name in code
		    //                  print fuck you wrong name
		    //if forever is chosen
		    if ( !forever.isSelected() ) {
			JButton button = new JButton ( s );
			space.add ( button );
			button.addActionListener ( this );
		    }
		    //if forever is not chosen
		    else {
			ForeverButton button = new ForeverButton ( s );
			space.add ( button );
			button.addActionListener ( this );
			foreverButtons.add ( button );
		    }
		}
	    }

	//make switch
	
	else if(((JMenuItem) e.getSource() ).getText().equals("Switch")) {
	    String s = JOptionPane.showInputDialog(null, "Type name of switch");
	    if(s != null && !s.equals("") ) {
		JButton button = new JButton( s );
		space.add( button );
		//Says swtch can't be found??
		//	swtch.addActionListener ( this );
		//	swtch.setBackground(Color.RED);
	    }
	}
	//monitor
	    //System.out.println ( ( (JMenuItem) e.getSource() ).getText() );
	else if(((JMenuItem) e.getSource() ).getText().equals("Monitor")) {
	    String s = JOptionPane.showInputDialog ( null , "Type monitor variable" );
	    //JPanel whole = new JPanel ( new GridLayout ( 1 , 2 ) );
	    Integer value = f.globals.get ( s );
	    JLabel m = new JLabel ( s + ": " + value );
	    System.out.println ( "created monitor: " + m.getText() );
	    //JButton bottom = new JButton ( "" + f.globals.get ( s ) );
	    //whole.add ( top );
	    //whole.add ( bottom );
	    space.add ( m );
	    monitors.add ( m );
	}
	}//end try
	catch ( ClassCastException ex ) {
	    System.out.println("Catch");
	    try {
		//mthds is name of method that's being called by the button- like setup, move, etc.- 
		//not built in methods, but ones that are created
		String mthds = ( ( JButton ) e.getSource() ).getText();
		System.out.println ( "e: " + e.getModifiers()/*.getSource() instanceof JButton*/ );
		//try for if button is forever
		if ( ( ( JButton ) e.getSource() ).getBackground().equals ( Color.WHITE ) ) {
		    ( ( JButton ) e.getSource() ).setBackground ( Color.BLACK );
		}
		else if ( ( ( JButton ) e.getSource() ).getBackground().equals ( Color.BLACK ) ) {
		    ( ( JButton ) e.getSource() ).setBackground ( Color.WHITE );
		}
		ArrayList<String> listOfMethods = methods.get ( mthds );
		for ( String mthd : listOfMethods ) {
		    //mthd has ; if it has parameters- call method with parameters
		    if ( mthd.contains ( ";" ) ) {
			//System.out.println ( "mthd: " + mthd );
			Method m = f.getClass().getMethod 
			                        (mthd.substring(0, mthd.indexOf( ";" )) , String.class );
			m.invoke ( f , mthd.substring ( mthd.indexOf ( ";" ) + 1 ) );
		}
		else {
		    //call method with no parameters
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
    public void callMethod ( String s ) {
	ArrayList<String> listOfMethods = methods.get ( s );
	try {
	for ( String mthd : listOfMethods ) {
	    //mthd has ; if it has parameters- call method with parameters
	    if ( mthd.contains ( ";" ) ) {
		//System.out.println ( "mthd: " + mthd );
		Method m = f.getClass().getMethod 
		    (mthd.substring(0, mthd.indexOf( ";" )) , String.class );
		m.invoke ( f , mthd.substring ( mthd.indexOf ( ";" ) + 1 ) );
	    }
	    else {
		//call method with no parameters
		Method m = f.getClass().getMethod ( mthd , null );
		m.invoke ( f , null );
	    }
	}
	} catch ( Exception e ) {
	    System.out.println ( "you messed up the call method call" );
	}
    }
}
class ForeverButton extends JButton {
    private boolean selected;
    public ForeverButton ( String s ) {
	super ( s );
	selected = false;
	//this.setIcon ( new ImageIcon ( "forever" ) );
	this.setBackground ( Color.WHITE );
    }
}

class myPanel extends JLayeredPane {
    private Patch[][] patches;
    private JPanel patchSpace; //layer of patches in black screen
    private JPanel turtleSpace; //layer of turtles
    //private TurtleFrame;
    private ArrayList<Turtle> turtles;
    HashMap<String , Integer> globals;
    private Color backgroundColor;
    //private int xcor = 13 , ycor = 13;
    public myPanel() {
	//super.setLayout ( new GridLayout ( 25 , 25 ) );
	this.setPreferredSize ( new Dimension ( 300 , 300 ) );
	//super.setMinimumSize ( new Dimension ( 25 , 25 ) );
	//super.setMaximumSize ( new Dimension ( 25 , 25 ) );
	patches = new Patch [ 25 ] [ 25 ];
	patchSpace = new JPanel();
	patchSpace.setLayout ( new GridLayout ( 25 , 25 ) );	
	patchSpace.setPreferredSize ( new Dimension ( 300 , 300 ) );
	turtles = new ArrayList<Turtle>();
	globals = new HashMap<String , Integer>();
	turtleSpace = new JPanel();
	turtleSpace.setPreferredSize ( new Dimension ( 300 , 300 ) );
	//TurtleFrame = new TurtleFrame();
	backgroundColor = Color.BLACK;
	//array of patches, 25x25
	for ( int r = 0 ; r < 25 ; r++ ) {
	    for ( int c = 0 ; c < 25 ; c++ ) {
		Patch p = new Patch();
		p.setBackground ( Color.BLACK ); 
		patchSpace.add ( p );
		patches [ r ] [ c ] = p;
	    }
	}
	patchSpace.setBounds ( 25 , 25 , 300 , 300 );
	this.add ( patchSpace );
	turtleSpace.setBounds ( 25 , 25 , 300 , 300 );
	this.add ( turtleSpace );
    }
    public HashMap<String , Integer> getGlobals() {
	return globals;
    }
    //clear all
    public void ca() {
	backgroundColor = Color.BLACK;
	for ( Patch[] patchRows : patches ) {
	    for ( Patch patch : patchRows ) {
		patch.setBackground ( Color.BLACK );
		//patch.setImage ( null );
	    }
	}
	//turtles.clear();
	/*turtleSpace = new JPanel();
	turtleSpace.setPreferredSize ( new Dimension ( 300 , 300 ) );
	turtleSpace.setBounds ( 25 , 25 , 300 , 300 );
	this.add ( turtleSpace );*/
    }
    //create a new turtle in middle of grid, s is integer of how many turtle you want to create
    public void crt ( String s ) {
	int nums = Integer.parseInt ( s );
	Turtle turtle = new Turtle ( patches.length / 2 , patches [ patches.length / 2 ].length / 2 );
	turtles.add ( turtle );
	try {
	    Image image = ImageIO.read ( getClass().getResource ( "green_arrow.png" ) );
	    turtle.setImage ( image );
	    //patches [ patches.length / 2 ] [ patches [ patches.length / 2 ].length / 2 ].setImage ( turtle.getImage() );
	    turtleSpace.add ( turtle );
	    //I HAVE NO IDEA WHY 135 SETS THE TURTLE AT THE RIGHT SPOT- FIX THIS LATER!!! DON'T BE LAZY/FORGET TO DO THIS!!!
	    turtle.setBounds ( /*25 + 12 * patches.length / 2*/135 , /*25 + patches [ patches.length / 2 ].length / 2*/135 , ( (BufferedImage) image ).getWidth() , ( (BufferedImage) image ).getHeight() );
	    //System.out.println ( "adasheight: " + ( (BufferedImage) turtle.getImage() ).getHeight() + "\niwfwidth: " + ( (BufferedImage) turtle.getImage() ).getHeight() );
	    System.out.println ( "turtle at : " + patches.length / 2 );
	} catch ( Exception e ) {
	    System.out.println ( "come on man" );
	}
	//panels [ panels.length / 2 ] [ panels [ panels.length / 2 ].length / 2 ].setBackground ( Color.GREEN );
    }
    //ask commands
    public void ask ( String s1 ) {
	ArrayList<String> agents = new ArrayList<String>();
	ArrayList<String> commands = new ArrayList<String>();
	String s = new String();
	
	for ( int i = 0 ; i < s1.length() ; i++ )
	    s = s + s1.substring ( i , i + 1 );
	boolean insideWith = false; //if there is "with" (turtles with who > 1, etc.)
	System.out.println ( "s: " + s );
	//add to agents when beginning of string is not "["
	while ( s.indexOf ( "[" ) != 0 || insideWith ) {
	    String word = s.substring ( 0 , s.indexOf ( ";" ) );
	    //System.out.println ( "word: " + word );
	    if ( word.equals ( "with" ) )
		insideWith = true;
	    if ( insideWith && word.equals ( "]" ) )
		insideWith = false;
	    agents.add ( word );
	    s = s.substring ( s.indexOf ( ";" ) + 1 );
	    //if ( s.substring ( 0 , s.indexOf ( ";" ) ).equal
	}
	s = s.substring ( 2 );
	System.out.println ( "agents: " + agents );
	//add to commands when beginning of string is not "]"
	int inBrackets = 0;
	while ( s.indexOf ( "]" ) != 0 || inBrackets != 0 ) {
	    String word = s.substring ( 0 , s.indexOf ( ";" ) );
	    if ( word.equals ( "[" ) )
		inBrackets++;
	    if ( word.equals ( "]" ) )
		inBrackets--;
	    commands.add ( word );
	    s = s.substring ( s.indexOf ( ";" ) + 1 );
	}
	System.out.println ( "commands: " + commands );
	System.out.println ( agents.size() );
	if ( agents.size() == 1 ) {
		System.out.println ( "hi" );
		if ( agents.equals ( "patches" ) ) //ask patches to do things
		System.out.println ( "here" );
		if ( agents.get ( 0 ).equals ( "turtles" ) ) { //ask turtles to do things
		for ( int i = 0 ; i < commands.size() ; i++ ) {
		    //forward + back
		    if ( commands.get ( i ).equals ( "fd" ) || commands.get ( i ).equals ( "bk" ) ) {
			System.out.println ( "command: " + commands.get ( i ) );
			System.out.println ( turtles );
			for ( Turtle turtle : turtles ) {
			    double xcor = turtle.getXcor();
			    double ycor = turtle.getYcor();
			    int dir = turtle.getDir();
			    int steps = Integer.parseInt ( commands.get ( i + 1 ) ) * 10;
			    //I DON'T KNOW WHY IT'S 10- CHANGE TO SIZE OF EACH PATCH LATER!!!
			    if ( commands.get ( i ).equals ( "fd" ) ) {
				xcor = xcor + steps * round ( Math.cos ( Math.toRadians ( dir ) ) );
				ycor = ycor + steps * -1 * round ( Math.sin ( Math.toRadians ( dir ) ) );
			    }
			    else {
				xcor = xcor + steps * -1 * round ( Math.cos ( Math.toRadians ( dir ) ) );
				ycor = ycor + steps * round ( Math.sin ( Math.toRadians ( dir ) ) );
			    }
			    //System.out.println ( "x: " + xcor + "\ny: " + ycor + "\ndir: " + dir + "\nsin dir: " + Math.sin ( dir ) + "\ncos dir: " + Math.cos ( dir ) );
			    turtle.setXcor ( xcor );
			    turtle.setYcor ( ycor );
			    turtle.setBounds ( (int)xcor + 124 , (int)ycor + 124 , ( (BufferedImage) turtle.getImage() ).getWidth() , ( (BufferedImage) turtle.getImage() ).getHeight() );
			    //System.out.println ( "height: " + ( (BufferedImage) turtle.getImage() ).getHeight() + "\nwidth: " + ( (BufferedImage) turtle.getImage() ).getHeight() );
			    turtleSpace.setBackground ( Color.BLACK );
			    turtleSpace.add ( turtle );
			}
			i = i + 1;
		    }
		}
	    }
	}
	else if ( agents.size() > 1 ) { //agents has properties, like "with" or "at"- not complete yet
	    String agentType = agents.get ( 0 );
	    if ( agentType.equals ( "turtles" ) ) {
	    if ( agents.get ( 1 ).equals ( "with" ) ) {
		String[] restrictions = new String [ agents.size() - 4 ];
		for ( int i = 3 ; !agents.get ( i ).equals ( "]" ) ; i++ )
		    restrictions [ i - 3 ] = agents.get ( i );
		//System.out.println ( "restrictions: " + Arrays.toString ( restrictions ) );
		ArrayList<Turtle> callTurtles = new ArrayList<Turtle>(); //turtles on which the methods are being called
		if ( restrictions [ 0 ].equals ( "who" ) ) {
		    int who = Integer.parseInt ( restrictions [ 2 ] );
		    if ( restrictions [ 1 ].equals ( "=" ) ) {
			//System.out.println ( "turtles size: " + turtles.size() );
			for ( int i = 0 ; i < turtles.size() ; i++ ) {
			    if ( who == i )
				callTurtles.add ( turtles.get ( i ) );
			}
		    }
		    else if ( restrictions [ 1 ].equals ( "!=" ) ) {
			//System.out.println ( "not equals" );
			for ( int i = 0 ; i < turtles.size() ; i++ ) {
			    if ( who != i )
				callTurtles.add ( turtles.get ( i ) );
			}
			//System.out.println ( "callturtles size: " + callTurtles.size() );
		    }
		}
	    //call methods on selected turtles
		for ( int i = 0 ; i < commands.size() ; i++ ) {
		    //forward + back
		    //System.out.println ( "commands.get: " + commands.get ( i ) );
		    if ( commands.get ( i ).equals ( "fd" ) || commands.get ( i ).equals ( "bk" ) ) {
			//System.out.println ( "command: " + commands.get ( i ) );
			//System.out.println ( turtles );
			for ( Turtle turtle : callTurtles ) {
			    double xcor = turtle.getXcor();
			    double ycor = turtle.getYcor();
			    int dir = turtle.getDir();
			    int steps = Integer.parseInt ( commands.get ( i + 1 ) ) * 10;
			    //I DON'T KNOW WHY IT'S 10- CHANGE TO SIZE OF EACH PATCH LATER!!!
			    if ( commands.get ( i ).equals ( "fd" ) ) {
				System.out.println ( "called fd" );
				xcor = xcor + steps * round ( Math.cos ( Math.toRadians ( dir ) ) );
				ycor = ycor + steps * -1 * round ( Math.sin ( Math.toRadians ( dir ) ) );
			    }
			    else {
				xcor = xcor + steps * -1 * round ( Math.cos ( Math.toRadians ( dir ) ) );
				ycor = ycor + steps * round ( Math.sin ( Math.toRadians ( dir ) ) );
			    }
			    System.out.println ( "x: " + xcor + "\ny: " + ycor + "\ndir: " + dir + "\nsin dir: " + Math.sin ( dir ) + "\ncos dir: " + Math.cos ( dir ) );
			    turtle.setXcor ( xcor );
			    turtle.setYcor ( ycor );
			    turtle.setBounds ( (int)xcor + 124 , (int)ycor + 124 , ( (BufferedImage) turtle.getImage() ).getWidth() , ( (BufferedImage) turtle.getImage() ).getHeight() );
			    //System.out.println ( "height: " + ( (BufferedImage) turtle.getImage() ).getHeight() + "\nwidth: " + ( (BufferedImage) turtle.getImage() ).getHeight() );
			    turtleSpace.setBackground ( Color.BLACK );
			    turtleSpace.add ( turtle );
			}
			i = i + 1;
		    }
		    /*		    else if ( commands.get ( i ).equals ( "every" ) ) {
			System.out.println ( "commands in every: " + commands );
			String everyParam = new String();
			for ( String command : commands ) {
			    if ( command.equals ( "every" ) ) {
			    }
			    else {
			    everyParam = everyParam + command + ";";
			    if ( command.equals ( "]" ) )
				break;
			    }
			}
			}*/
		    else System.out.println ( "command not called: " + commands.get ( i ) );
		}
	    }
	    //if ( agent.get ( 1 ).equals ( "at" ) ) {
	    //}
	    }
	}
	//while ( 
    }
    public void every ( String s1 ) {
	System.out.println ( "every called with parameter: " + s1 );
	double waitTime = Double.parseDouble ( s1.substring ( 0 , s1.indexOf ( ";" ) ) );
	System.out.println ( "wait: " + waitTime );
	String mthd = new String();
 	mthd = s1.substring ( s1.indexOf ( ";" ) + 1 );
	mthd = mthd.substring ( mthd.indexOf ( ";" ) + 1 , mthd.length() - 2 );
	//split up different methods within every- only works with one method right now

	/*String[] words = new String [ StringUtils.countMatches ( s1 , ";" ) ];
	for ( int j = 0 ; j < words.length ; j++ ) {
	    words [ j ] = s.substring ( 0 , s.indexOf ( ";" ) );
	    s = s.substring ( s.indexOf ( ";" ) + 1 );
	}
	for ( int i = 0 , i < words.length ; i++ ) {
	if ( word.equals ( "ask" ) ) {
	    String addLine = word + ";";
	    while ( ! word.equals ( "]" ) ) {
		i = i + 1;
		word = words [ i ];
		addLine = addLine + word + ";";
	    }
	    ans.add ( addLine );
	}
	}
*/
	//ArrayList<String> listOfMethods = methods.get ( mthds );
	//for ( String mthd : listOfMethods ) {
	    //mthd has ; if it has parameters- call method with parameters


	try {
	    if ( mthd.contains ( ";" ) ) {
		System.out.println ( "mthd: " + mthd );
		Method m = this.getClass().getMethod 
		    ( mthd.substring ( 0 , mthd.indexOf ( ";" ) ) , String.class );
		System.out.println ( "wait millis: " + (int) ( waitTime * 1000 ) );
		Thread.sleep ( (int) ( waitTime * 1000 ) );
		m.invoke ( this , mthd.substring ( mthd.indexOf ( ";" ) + 1 ) );
	    }
	    else {
		//int wait = waitTime * 1000;
		Thread.sleep ( (int) ( waitTime * 1000 ) );
		//Thread.sleep ( wait );
		//call method with no parameters
		Method m = this.getClass().getMethod ( mthd , null );
		m.invoke ( this , null );
	    }	
	} catch ( Exception e ) {
	    System.out.println ( "method call failed in every" );
	    System.out.println ( mthd );
	}
    }
    public void set ( String s ) {
	System.out.println ( "set: " + s );
	String change = s.substring ( 0 , s.indexOf ( ";" ) );
	if ( globals.containsKey ( change ) ) {
	    System.out.println ( "get globals: " + globals.get ( change ) );
	    String operations = s.substring ( s.indexOf ( ";" ) + 1 );
	    System.out.println ( "do this to global: " + operations );
	    String[] ops = new String [ 3 ];
	    for ( int i = 0 ; i < 3 ; i++ ) {
		ops [ i ] = operations.substring ( 0 , operations.indexOf ( ";" ) );
		operations = operations.substring ( operations.indexOf ( ";" ) + 1 );
	    }
	    System.out.println ( "ops: " + Arrays.toString ( ops ) );
	    int first , second;
	    try { 
		first = Integer.parseInt ( ops [ 0 ] );
	    } catch ( Exception e ) {
		first = globals.get ( ops [ 0 ] );
	    }
	    try {
		second = Integer.parseInt ( ops [ 2 ] );
	    } catch ( Exception e ) {
		second = globals.get ( ops [ 2 ] );
		//System.out.println ( e );
	    }
	    if ( ops [ 1 ].equals ( "+" ) )
		globals.put ( change , first + second );
	    //System.out.println ( "changed global: " + globals.get ( change ) );
	    else if ( ops [ 1 ].equals ( "-" ) )
		globals.put ( change , first - second );
	    else if ( ops [ 1 ].equals ( "*" ) )
		globals.put ( change , first * second );
	    else if ( ops [ 1 ].equals ( "/" ) )
		globals.put ( change , first / second );
	    /*while ( operations.length() > 0 ) {
		if ( operations.substring ( 2 , 3 ).equals ( "+" ) ) {
		    System.out.println ( "addition" );
		    //change = change + operations.substring ( 
		}
		operations = operations.substring ( 1 );
	    }*/
	    //globals.put ( change , 
	}
	if ( change.equals ( "breed" ) ) {
	    //do things here
	}
    }
    //round double to smaller double, bc double are slightly off
    public double round ( double x ) {
	return (int) ( x * 100 ) / (double) 100;
    }
}

class Patch extends JPanel {
    private Image image;
    public Patch() {
	image = null;
	super.setPreferredSize ( new Dimension ( 2 , 2 ) );
	super.setMaximumSize ( new Dimension ( 2 , 2 ) );
	super.setMinimumSize ( new Dimension ( 2 , 2 ) );
    }
    //setImage not needed- remove later when sure not called anywhere
    public void setImage ( Image image ) {
	this.image = image;
	update ( this.getGraphics() );
	//System.out.println ( "patch update here" );
    }
    //since setImage not needed, neither is this
    public void paintComponent ( Graphics g ) {
	super.paintComponent ( g );
	g.drawImage ( image , 0 , 0 , null );
    }
}

class Turtle extends JPanel {
    private double xcor , ycor;
    private int dir;
    private Image image;
    private String breed;
    public Turtle ( double xcor , double ycor  ) {
	this.xcor = xcor;
	this.ycor = ycor;
	dir = (int) ( Math.random() * 100 );
	breed = new String();
    }
    /*public void paintComponent ( Graphics g ) {
	
      }*/
    public double getXcor() {
	return xcor;
    }
    public double getYcor() {
	return ycor;
    }
    public void setXcor ( double newX ) {
	xcor = newX;
    }
    public void setYcor ( double newY ) {
	ycor = newY;
    }
    public void setColor(Color color) {
	if(color.equals(Color.RED)){

	}
    }  
    //image of turtle- currently only a green arrow
    public void setImage ( Image image ) {
	//this.getContentPane().add ( image );
	int rotation = (int) ( Math.random() * -100 );
	this.dir = -1 * rotation;
	System.out.println ( "rotate " + rotation + " degrees" );
	this.image = rotate ( (BufferedImage) image , ( rotation ) );
	update ( this.getGraphics() );
	//System.out.println ( "update here" );
    }
    public Image getImage() {
	return image;
    }
    public void setDir ( int dir ) {
	this.dir = dir;
    }
    public int getDir() {
	return dir;
    }
    public void paintComponent ( Graphics g ) {
	/*BufferedImage image = (BufferedImage) this.image;
	int rotation = (int) Math.random() * 100;
	this.image = rotate ( image , rotation );*/
	super.paintComponent ( g );
	g.drawImage ( image , 0 , 0 , null );
    }
    //rotate image
    public BufferedImage rotate ( BufferedImage image , int rotation ) {
	System.out.println ( "rotate" + rotation );
	int w = image.getWidth();
	int h = image.getHeight();
	BufferedImage buffImage = new BufferedImage ( w , h , image.getType() );
	Graphics2D g = buffImage.createGraphics();
	g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON );
	//rotation = -1 * rotation;
	g.rotate ( Math.toRadians ( rotation ) , w / 2 , h / 2 );
	g.drawImage ( image , null , 0 , 0 );
	return buffImage;
    }
}
