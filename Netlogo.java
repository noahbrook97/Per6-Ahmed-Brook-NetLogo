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
	System.setProperty ( "java.util.Arrays.useLegacyMergeSort" , "true" );
	NetlogoBoard n = new NetlogoBoard();
    }
}

class NetlogoBoard implements Runnable {
    protected Screen s;
    private Thread t;

    public NetlogoBoard() {
	JFrame f = new JFrame();
	s = new Screen();
	f.add ( s );
	f.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
	f.show();
	f.pack();
	t = new Thread ( this );
	t.start();
    }

    public void run() {
	boolean alreadyDone = false;
	int count = 0;
	while ( true ) {
	    String txt = s.code.getText();
	    if ( s.getSelectedIndex() == 0 ) {
		while ( !alreadyDone ) {
		    s.iface.javafy ( s.code.getText() );
		    alreadyDone = true;
		}
	    }
	    if ( s.getSelectedIndex() != 0 )
		alreadyDone = false;
	    for ( ForeverButton b : s.getForeverButtons() ) {
		if ( b.getBackground().equals ( Color.BLACK ) ) {
		    //call method
		    s.iface.callMethod ( b.getText() );
		}
	    }
	    HashMap<String , Integer> globals = s.iface.getGlobals();
	    ArrayList<JLabel> addLabels = new ArrayList<JLabel>();
	    for ( JLabel m : s.getMonitors() ) {
		String key = m.getText().substring ( 0 , m.getText().indexOf ( ":" ) );
		Integer value = Integer.parseInt ( m.getText().substring ( m.getText().indexOf ( ":" ) + 2 ) );
		if ( ! globals.get ( key ).equals ( value ) ) {
		    count--;
		    //System.out.println ( "changed" );
		    //JOptionPane.showMessageDialog ( null , "changed" );
		    //System.out.println ( "monitor should read: " + key + ": " + globals.get ( key ) );
		    String newValue = key + ": " + globals.get ( key ).toString();
		//m.setText ( key + ": " + 0 );
		//System.out.println ( "new value: " + value );
		//m.setText ( m.getText().substring ( 0 , m.getText().length() - 1 ) + newValue );
		    //m.setText ( newValue );
		    //m.setText ( "a: " + count );
		    //s.remMonitor ( m );
		    //addLabels.add ( m );
		    m.setText ( key + ": " + globals.get ( key ) );
		}
		//else System.out.println ( "text and global are same:\nvalue: " + globals.get ( key ) + "text: " + value );
	    }
	    /*for ( JLabel m : addLabels )
		s.addMonitor ( m );
	    for ( JLabel m : addLabels )
		s.remMonitor ( m );*/
	}
    }
}

class Screen extends JTabbedPane {
    IFace iface;
    JTextArea code;
    public Screen() {
	iface = new IFace();
	this.add ( "Interface" , iface );

	String txt = "Methods Tutorial \n \n" +                                                                                               
                      "All methods must start with to and end with end \n" +                                                             
	              "ask: used to call on the type of object you wish to command, whether it be a patch, turtle, or breed \n" +
	              "set: used to change the attributes of an object \n" +          
	              "with: keyword used after ask or set to specify restrictions object must have to be acted on \n" +
                      "every: repeats a function a certain amount of times\n" +
                      "crt: creates a turtle \n" +
                      "die: DEATH TO TURTLE!!! \n" +
                      "fd/bk: moves turtle forward and back respectively \n \n" +
                      "random: randomly selects integer value\n" +
                      "wait: wait to perform function for period of time\n" +
                      "user-message \n" +
                      "Attributes Tutorial \n \n" +
                      "xcor/ycor: Display a turtles (x, y) coordinates \n" +
                      "pxcor/pycor: Display a patches (x, y) coordinates \n" +
                      "color: Displays turtles color \n" +
                      "pcolor: Displays patches color \n" +
                      "heading: Displays the direction a turtle is facing [0-360] \n \n" +
                      "Other Stuff \n \n" +
                      "count: counts number of objects present\n" +
	              "breed: \n" +
                      "globals: \n \n" +
                      "Operators: \n \n" +
                      "Standard = , != , > , < , >= , <= \n" +
                      " \t and , or \n" +
                      "if , ifelse , else \n" +
	              "If you master this syntax, son someday this will all be yours";                                       

	JTextArea info = new JTextArea(txt);

	this.add ( "Info" , info );

	//code = new JTextArea ( "globals [ a ] to setup user-message ( \"hey dude man\" count turtles with [ color = red ] ) ask patches [ set pcolor red ] end to move ask turtles [ fd 1 ] end to change ask turtles [ die ] end to create crt 1 end to changeGlobal set a a + 1 crt 1 end" );
	code = new JTextArea ( "globals [ lives sbutton ]\n" +
			       "breed [ plural singular ]\n" +
			       //"to setup ca ask patches with [ pycor < -55 or pycor > 55 ] [ set pcolor blue ] " +
			       //"to setup if count turtles with [ color = red ] < 2 or 2 > 0 [ ask patches with [ pycor < -10 or pycor > 10 ] [ set pcolor brown ] ask patches with [ pxcor > 5 ] [ set pcolor yellow ] ]\n" +
			       //"to setup ca " + 
			       //"ask patches with [ pycor >= -20 and pycor <= 20 ] [ set pcolor white ]\n" +
			       //"set lives 3 set sbutton 0 end\n" +
			       //"to setup ask turtles [ ask patch-here [ set pcolor red ] ] end " +
			       //"to setup if random 4 > 2 [ ask patches [ set pcolor green ] ] end " +
			       "to setup ask patch 0 0 [ set pcolor red ] end " +
			       //"to change if 2 = 2 [ crt 1 ] ask turtles with [ who > 1 ] [ set xcor 5 ] set lives lives - 1 end\n" +
			       "to change ask turtles [ ask patch-at 5 1 [ set pcolor blue ] ] end " +
			       //"to change ask turtles with [ who > 1 ] [ set xcor 5 ] set lives lives - 1 end\n" +
			       "to create crt 1 end\n" +
			       "to move ask turtles [ bk 1 ] end " +
			       "to mouse if mouse-Down? [ ask patch mouse-xcor mouse-ycor [ set pcolor red ] ] end" );
	code.setPreferredSize ( new Dimension ( 355 , 355 ) );
	this.add ( "Code" , code );
    }
    public ArrayList<ForeverButton> getForeverButtons() {
	return iface.getForeverButtons();
    }
    public ArrayList<JLabel> getMonitors() {
	return iface.getMonitors();
    }
    /*public void remMonitor ( JLabel m ) {
	iface.remMonitor ( m );
    }
    public void addMonitor ( JLabel m ) {
	iface.addMonitor ( m );
	}*/
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
    HashMap<String , ArrayList<String>> methods;
    ArrayList<String[]> breeds;

    public IFace() {
	space = new JPanel();
	f = new myPanel();
	foreverButtons = new ArrayList<ForeverButton>();
	monitors = new ArrayList<JLabel>();
	breeds = new ArrayList<String[]>();
	f.setPreferredSize ( new Dimension ( 500 , 500 ) );
	JButton setup = new JButton ( "setup" );
	setup.addActionListener ( this );
	space.add ( setup );
	JButton move = new JButton ( "move" );
	move.addActionListener ( this );
	space.add ( move );
	JButton change = new JButton ( "change" );
	change.addActionListener ( this );
	space.add ( change );
	JButton create = new JButton ( "create" );
	create.addActionListener ( this );
	space.add ( create );
	ForeverButton mouse = new ForeverButton ( "mouse" );
	mouse.addActionListener ( this );
	space.add ( mouse );
	foreverButtons.add ( mouse );
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
    /*public void remMonitor ( JLabel m ) {
	monitors.remove ( m );
    }
    public void addMonitor ( JLabel m ) {
	monitors.add ( m );
	}*/
    public HashMap<String , Integer> getGlobals() {
	return f.getGlobals();
    }
    public void checkSyntax ( ArrayList<String> words ) {
	for ( int i = 0 ; i < words.size() ; i++ ) {
	    String word = words.get ( i );
	    System.out.println ( "words in checkSyntax: " + word );
	    if ( word.equals ( "globals" ) ) {
		if ( ! words.get ( i + 1 ).equals ( "[" ) )
		    JOptionPane.showMessageDialog ( null , "GLOBALS EXPECTED [" );
		i = i + 1;
		while ( ! word.equals ( "]" ) ) {
		    i = i + 1;
		    word = words.get ( i );
		    System.out.println ( "words in globals: " + word );
		    try { 
			Double.parseDouble ( word );
			JOptionPane.showMessageDialog ( null , "GLOBALS EXPECTED NAME OR ]" );
		    } catch ( NumberFormatException e ) {
			if ( word.contains ( "\"" ) )
			    JOptionPane.showMessageDialog ( null , "CAN'T HAVE QUOTES HERE" );
		    }
		}
	    }
	    else if ( word.equals ( "breed" ) ) {
		System.out.println ( "breed" );
		if ( ! words.get ( i + 1 ).equals ( "[" ) )
		    JOptionPane.showMessageDialog ( null , "BREED EXPECTED [" );
		/*while ( ! word.equals ( "]" ) ) {
		    i = i + 1;
		    word = words.get ( i );
		    }*/
		try {
		    Double.parseDouble ( words.get ( i + 2 ) );
		    JOptionPane.showMessageDialog ( null , "BREED EXPECTED NAME OR ]" );
		} catch ( NumberFormatException e ) {
		    try {
			Double.parseDouble ( words.get ( i + 3 ) );
			JOptionPane.showMessageDialog ( null , "BREED EXPECTED NAME OR ]" );
		    } catch ( Exception ex ) {
		    }
		}
		if ( ! words.get ( i + 3 ).equals ( "]" ) && ! words.get ( i + 4 ).equals ( "]" ) )
		    JOptionPane.showMessageDialog ( null , "BREED ONLY TAKES 1 OR 2 INPUTS" + words.get ( i + 4 ) );
		else if ( words.get ( i + 3 ).equals ( "]" ) )
		    i = i + 3;
		else if ( words.get ( i + 4 ).equals ( "]" ) )
		    i = i + 4;
	    }
	    else if ( word.equals ( "to" ) ) {
		word = words.get ( i + 1 );
		i = i + 1;
		if ( word.equals ( "crt" ) || word.equals ( "set" ) || word.equals ( "die" ) || word.equals ( "ask" ) || word.equals ( "every" ) || word.equals ( "wait" ) || word.equals ( "turtles" ) || word.equals ( "turtle" ) || word.equals ( "patches" ) || word.equals ( "patch" ) || word.equals ( "xcor" ) || word.equals ( "ycor" ) || word.equals ( "heading" ) || word.equals ( "count" ) )
			JOptionPane.showMessageDialog ( null , "YOU CAN'T USE " + word.toUpperCase() + " TO NAME A PROCEDURE" );
		while ( ! word.equals ( "end" ) ) {
		    i = i + 1;
		    word = words.get ( i );
		    if ( word.equals ( "to" ) )
			JOptionPane.showMessageDialog ( null , "TO DOESN'T MAKE SENSE HERE" );
		}
		//i = i + 1;
	    }
	    else {
		JOptionPane.showMessageDialog ( null , "ERROR IN YOUR CODE, FIX IT- \"" + words.get ( i ) + "\" IS NOT VALID THERE" );
		break;
	    }
	}
    }
    public String condenseWhiteSpace ( String s ) {
	String ans = new String();
	for ( int i = 0 ; i < s.length() ; i++ ) {
	    String chr = s.substring ( i , i + 1 );
	    if ( chr.equals ( " " ) || chr.equals ( "\n" ) ) {
		while ( s.substring ( i + 1 , i + 2 ).equals ( " " ) || s.substring ( i + 1 , i + 2 ).equals ( "\n" ) ) {
		    i = i + 1;
		    //chr = s.substring ( i , i + 1 );
		}
	    }
	    ans = ans + chr;
	}
	return ans;
    }
    public void javafy ( String s1 ) {
	//JOptionPane.showMessageDialog ( null, "\\n size: " + "\n".substring ( 0 , 1 ).equals ( "\n" ) );
	/*String s = new String();
	for ( int i = 0 ; i < s1.length() ; i++ )
	s = s + s1.substring ( i , i + 1 );*/
	String s = condenseWhiteSpace ( s1 );
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
	System.out.println ( "words: " + words );
	checkSyntax ( words );
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
		//System.out.println ( "ans at end: " + ans + " for method " + methodName );
		methods.put ( methodName , ans );
		ans = new ArrayList<String>();
	    }
	    else if ( inMethod ) {
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//if boolean [ ask turtles [ set color red ] ask patches [ set color yellow ] ]
		// if;boolean; [ ask;turtles; [ set;color;red; ] ask;patches; [ set;color;yellow; ] ]

		if (word.equals("if") ) {
		    /*   if ( words.get( i + 1 ).equals( "[" )) {
		    ans.add ( word + ";" + words.get ( i + 1) );
		    i += 1;
		    System.out.println("running if statement"); */
		    //i += 1;
                    //if ( words.get (i + 1).equals ( "[" )) {
		    int inBrackets = -1;
		    String addLine = "IF" + ";";
		    i = i + 1;
		    word = words.get ( i );
		    //boolean skipBracket = false;
		    int skipBracket = 0;
		    while (!word.equals ( "]" ) || inBrackets != 0 || skipBracket != 0 ) {
			if ( word.equals ( "[" ) && skipBracket == 0 ) {
			    inBrackets++;
			}
			else if ( word.equals ( "[" ) && skipBracket != 0 )
			    skipBracket--;
			    //JOptionPane.showMessageDialog ( null , "skipbracket" );
			if ( word.equals ( "]" ) && skipBracket == 0)
			    inBrackets--;
			else if ( word.equals ( "]" ) && skipBracket != 0 )
			    skipBracket--;
			if ( word.equals ( "count" ) || word.equals ( "ask" ) ) {
			    //JOptionPane.showMessageDialog ( null , "not with, but " + words.get ( i + 2 ) );
			    if ( words.get ( i + 2 ).equals ( "with" ) ) {
				skipBracket = skipBracket + 2;
				//System.out.println ( "methods: " + methods );
				//inBrackets--;
				//inBrackets++;
				//JOptionPane.showMessageDialog ( null , "with" );
			    }
			}
  			if ( word.equals ( "ask" ) ) {
			    skipBracket = skipBracket + 2;
			    //inBrackets++;
			}
			//else JOptionPane.showMessageDialog ( null , "word is: " + word );
			addLine = addLine + word + ";";
			i+= 1;
			try { 
			    word = words.get (i);
			} catch ( IndexOutOfBoundsException a ) {
			    JOptionPane.showMessageDialog ( null , "inBrackets: " + inBrackets + "\nword: " + word );
			}
		    }
		    ans.add ( addLine + word );
		    System.out.println ( "calling if: " + ans );
			//}
			//else {
                        //ans.add ( word + ";" + words.get ( i ) );
			//String addLine = word + ";";
		}
		
		
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		else if ( word.equals ( "crt" ) ) {
		    //ans.add ( word + ";" + words.get ( i + 1 ) );
		    //if ( word.equals ( "crt" ) ) {
		    //String addLine = word + ";" + words.get ( i + 1 );
		    i = i + 1;
		    if ( words.get ( i + 1 ).equals ( "[" ) ) {
			int inBrackets = -1;
			String addLine = word + ";" + words.get ( i );
			while ( ! word.equals ( "]" ) || inBrackets != 0 ) {
			    if ( word.equals ( "[" ) )
				inBrackets++;
			    if ( word.equals ( "]" ) )
				inBrackets--;
			    i = i + 1;
			    word = words.get ( i );
			    addLine = addLine + word + ";";
			}
			ans.add ( addLine );
			System.out.println ( "calling crt with : " + ans );
		    }
		    else 
			ans.add ( word + ";" + words.get ( i ) );
			//i = i + 1;
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
		    ans.add ( addLine.substring ( 0 , addLine.length() - 2 ) + "]" );//+ "fd;1;bk;1;];" );
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
		    }
		    ans.add ( addLine );
		}

		else if ( word.equals ( "set" ) ) {
		    System.out.println ( "globals: " + f.globals.entrySet() );
		    String addThis = word + ";";
		    i = i + 1;
		    word = words.get ( i );
		    while ( "1234567890+-*/".contains ( word )  || f.globals.containsKey ( word ) ) {
			addThis = addThis + word + ";";
			//System.out.println ( "addthis: " + addThis );
			i = i + 1;
			word = words.get ( i );
		    }
		    i = i - 1;
		    //System.out.println ( "addThis in set: " + addThis );
		    ans.add ( addThis );
		}
		else if ( word.equals ( "wait" ) ) {
		    ans.add ( word + ";" + words.get ( i + 1 ) );
		    i = i + 1;
		}
		else if ( word.equals ( "user-message" ) ) {
		    String addThis = "userMessage" + ";";
		    i = i + 1;
		    word = words.get ( i );
		    while ( ! word.equals ( ")" ) ) {
			addThis = addThis + word + ";";
			i = i + 1;
			word = words.get ( i );
		    }
		    ans.add ( addThis + word + ";" );
		}
		else {
		    ans.add ( word );
		    System.out.println ( "word: " + word );
		}
	    }
	    else if ( word.equals ( "breed" ) ) {
		String[] ad = { words.get ( i + 2 ) , words.get ( i + 3 ) };
		breeds.add ( ad );
		System.out.println ( "added to breeds: " + ad );
		i = i + 4;
	    }
	    else if ( word.equals ( "globals" ) ) {
		i = i + 2;
		while ( ! words.get ( i ).equals ( "]" ) ) {
		    System.out.println ( "add to globals: " + words.get ( i ) );
		    f.globals.put ( words.get ( i ) , 0 );
		    i = i + 1;
		}
	    }
	}
	System.out.println ( "methods: " + methods.entrySet() );
	f.methods = methods;
	//System.out.println ( "method keys: " + methods.keySet() );
	//System.out.println ( "method values: " + methods.values() );
    }

    public void mouseExited ( MouseEvent e ) {
	//System.out.println ( "mouseExited" );
    }
    public void mouseEntered ( MouseEvent e ) {
	System.out.println ( "mouseEntered" );
    }
    public void mouseReleased ( MouseEvent e ) {
	//System.out.println ( "mouseReleased" );
    }
    public void mousePressed ( MouseEvent e ) {
	//System.out.println ( "mousePressed" );
    }
    public void mouseClicked ( MouseEvent e ) {
	//System.out.println ( "mouseClicked" );
	if ( SwingUtilities.isRightMouseButton ( e ) ) {
	    System.out.println ( "right clicked" );

	    //create options menu with their buttons and stuff
	    JPopupMenu menu = new JPopupMenu();
	    JMenuItem button = new JMenuItem("Button" );
	    //JSlider slider = new JSlider();
	    JMenuItem swtch = new JMenuItem("Switch");
	    JMenuItem monitor = new JMenuItem ( "Monitor" );

	    button.addActionListener ( this );
	    swtch.addActionListener  ( this );
	    monitor.addActionListener ( this );
	    //slider.addActionListener ( this );

	    menu.add ( button );
	    //menu.add ( "Slider");
	    menu.add ( swtch );
	    //menu.add ( "Chooser" );
	    //menu.add ( "Input" );
	    menu.add ( monitor );
	    //menu.add ( "Plot" );
	    //menu.add ( "Output" );
	    //menu.add ( "Note" );
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
	    if ( ( ( JMenuItem ) e.getSource() ).getText().equals ( "Button" ) ) {
		String[] options = { "option 1" , "option 2" };
		JCheckBox forever = new JCheckBox ( "forever" );
		Object[] params = { "Type name of button" , forever };
		String s = JOptionPane.showInputDialog ( null , params );
		//if user types something
		if ( s != null && !s.equals("") ) {
		    //if statement here saying if s does not equal a known method name in code
		    //                  print screw you wrong name
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
	    //System.out.println ( "created monitor: " + m.getText() );
	    //JButton bottom = new JButton ( "" + f.globals.get ( s ) );
	    //whole.add ( top );
	    //whole.add ( bottom );
	    if ( ! s.equals ( null ) ) {
		space.add ( m );
		monitors.add ( m );
	    }
	}
	}//end try
	catch ( ClassCastException ex ) {
	    //System.out.println("clicked JButton: " + ( (JButton) e.getSource() ).getText() );
	    try {
		//mthds is name of method that's being called by the button- like setup, move, etc.- 
		//not built in methods, but ones that are created
		String mthds = ( ( JButton ) e.getSource() ).getText();
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
		    try {
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
		    } catch ( NoSuchMethodException excep ) {
			System.out.println ( "call method: " + mthd );
			ArrayList<String> listMethods = methods.get ( mthd );
			for ( String mtd : listMethods ) {
			    if ( mtd.contains ( ";" ) ) {
				Method m = f.getClass().getMethod ( mtd.substring ( 0 , mtd.indexOf ( ";" ) ) , String.class );
				m.invoke ( f , mtd.substring ( mtd.indexOf ( ";" ) + 1 ) );
			    }
			    else {
				Method m = f.getClass().getMethod ( mtd , null );
				m.invoke ( f , null );
			    }
			}
		    }
		}
	    } catch ( Exception exc ) {
	    System.out.println ( "nahh bro" );
	    System.out.println ( methods );
	    String methd = ( (JButton) e.getSource() ).getText();
	    //methods is empty- user didn't type methods in code tab
	    if ( methods.isEmpty() ) {
		//System.out.println ( isNew );
		System.exit(0);
	    }
	    //methods is not empty
	    else System.out.println ( "no" );
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
	this.setBackground ( Color.WHITE );
    }
}

class myPanel extends JLayeredPane implements MouseListener {
    private Patch[][] patches;
    private JPanel patchSpace; //layer of patches in black screen
    private JPanel turtleSpace; //layer of turtles
    private ArrayList<Turtle> turtles;
    HashMap<String , Integer> globals;
    HashMap<String , ArrayList<String>> methods;
    private Color backgroundColor;
    private boolean mouseDown;
    private int mouseX , mouseY;
    public myPanel() {
	this.setPreferredSize ( new Dimension ( 355 , 355 ) );
	patches = new Patch [ 31 ] [ 31 ];
	patchSpace = new JPanel();
	patchSpace.setLayout ( new GridLayout ( 31 , 31 ) );	
	patchSpace.setPreferredSize ( new Dimension ( 355 , 355 ) );
	turtles = new ArrayList<Turtle>();
	globals = new HashMap<String , Integer>();
	turtleSpace = new JPanel();
	turtleSpace.setPreferredSize ( new Dimension ( 355 , 355 ) );
	mouseDown = false;
	//backgroundColor = Color.BLACK;
	//array of patches, 25x25
	for ( int r = 0 ; r < patches.length ; r++ ) {
	    for ( int c = 0 ; c < patches [ r ].length ; c++ ) {
		Patch p = new Patch ( c , r );
		p.setBackground ( Color.BLACK );
		p.setPreferredSize ( new Dimension ( 5 , 5 ) );
		patchSpace.add ( p );
		p.addMouseListener ( this );
		patches [ r ] [ c ] = p;
	    }
	}
	patchSpace.setBounds ( 25 , 25 , 355 , 355 );
	this.add ( patchSpace );
	turtleSpace.setBounds ( 25 , 25 , 355 , 355 );
	this.add ( turtleSpace );
	//addMouseListener ( this );
	//turtleSpace.addMouseListener ( this );
    }
    public void mouseExited ( MouseEvent e ) {
	System.out.println ( "mouseExited" );
    }
    public void mouseEntered ( MouseEvent e ) {
	System.out.println ( "mouseEntered" );
	//System.out.println ( MouseInfo.getPointerInfo().getLocation() );
    }
    public void mouseReleased ( MouseEvent e ) {
	mouseDown = false;
	//System.out.println ( "mouseReleased" );
    }
    public void mousePressed ( MouseEvent e ) {
	mouseDown = true;
	mouseX = ( (Patch) e.getSource() ).getXcor();
	mouseY = ( (Patch) e.getSource() ).getYcor();
	//System.out.println ( "mousePressed, coors: " + mouseX + ", " + mouseY );
	//System.out.println ( ( (Patch) e.getSource() ).getXcor() + " should be the patch's xcor" );
    }
    public void mouseClicked ( MouseEvent e ) {
    }
    public HashMap<String , Integer> getGlobals() {
	return globals;
    }
    //clear all
    public void ca() {
	backgroundColor = Color.BLACK;
	turtles.clear();
	//turtleSpace = new JPanel();
	turtleSpace.removeAll();
	turtleSpace.setPreferredSize ( new Dimension ( 355 , 355 ) );
	//turtlespace starts at 25, 25 on the interface tab, and is 355x355 big
	turtleSpace.setBounds ( 25 , 25 , 355 , 355 );
	for ( Patch[] patchRows : patches ) {
	    for ( Patch patch : patchRows ) {
		//System.out.println ( "ca loop" );
		patch.setBackground ( Color.BLACK );
	    }
	}
    }
    //create a new turtle in middle of grid, s is integer of how many turtle you want to create
    public void crt ( String s ) {
	System.out.println ( "crt called with string: " + s );
	int nums = Integer.parseInt ( s.substring ( 0 , 1 ) );
	if ( ! s.contains ( "]" ) ) {
	for ( int i = 0 ; i < nums ; i++ ) {
	    Turtle turtle = new Turtle ( patches.length / 2, patches [ patches.length / 2 ].length / 2 , (int) ( Math.random() * 360 ) , Color.RED , new String() , 1 , turtles.size() );
	    turtle.setXcor ( 0 );
	    turtle.setYcor ( 0 );
	    turtles.add ( turtle );
	    //try {
	    turtleSpace.add ( turtle );
	    //I HAVE NO IDEA WHY 135 SETS THE TURTLE AT THE RIGHT SPOT- FIX THIS LATER!!! DON'T BE LAZY/FORGET TO DO THIS!!!
	    System.out.println ( "testing setbounds number: " + patches.length / 2 );
	    //turtle.setBounds ( /*25 + 12 * patches.length / 2*/135 , /*25 + patches [ patches.length / 2 ].length / 2*/135 , turtle.getIcon().getIconWidth() , turtle.getIcon().getIconHeight() );
	    turtle.setBounds ( patches.length / 2 * (int) patches [ 0 ] [ 0 ].size().getWidth() , patches [ patches.length / 2 ].length / 2 * (int) patches [ 0 ] [ 0 ].size().getHeight() , turtle.getIcon().getIconWidth() , turtle.getIcon().getIconHeight() );
	    //System.out.println ( "turtle at : " + patches.length / 2 );
	    //} catch ( Exception e ) {
	    //System.out.println ( "come on man" );
	    //}
	}
	}
	else {
	    ArrayList<String> params = new ArrayList<String>();
	    int turtlesSize = turtles.size();
	    int inBrackets = 0;
	    if ( ! s.substring ( 0 , 2 ).equals ( "[" ) ) {
		System.out.println ( "got heer " + s.substring ( s.indexOf ( "[" ) ) );
		//s = s.substring ( s.indexOf ( "[" ) + 2 );
		System.out.println ( "crt string: " + s );
		crt ( "" + nums );
		ask ( "turtles;with;[;who;>;" + ( turtlesSize - 1 ) + ";];" + s.substring ( s.indexOf ( "[" ) , s.indexOf ( "]" ) + 1 ) );
	    }
	    else throw new IllegalStateException();
	    System.out.println ( "crt params: " + params );
	}
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private ArrayList with ( String agentType , ArrayList<String> agents ) {
	//String agentType = agents.get ( 0 );
	if ( agentType.equals ( "turtles" ) ) {
	//if ( agents.get ( 1 ).equals ( "with" ) ) {
		String[] restrictions = new String [ agents.size() - 4 ];
		for ( int i = 3 ; !agents.get ( i ).equals ( "]" ) ; i++ )
		    restrictions [ i - 3 ] = agents.get ( i );
		ArrayList<Turtle> callTurtles = new ArrayList<Turtle>(); //turtles on which the methods are being called
		if ( restrictions [ 0 ].equals ( "who" ) ) {
		    int who = Integer.parseInt ( restrictions [ 2 ] );
		    if ( restrictions [ 1 ].equals ( "=" ) ) {
			for ( int i = 0 ; i < turtles.size() ; i++ ) {
			    if ( who == turtles.get ( i ).getWho() )
				callTurtles.add ( turtles.get ( i ) );
			}
		    }
		    else if ( restrictions [ 1 ].equals ( "!=" ) ) {
			for ( int i = 0 ; i < turtles.size() ; i++ ) {
			    if ( who != turtles.get ( i ).getWho() )
				callTurtles.add ( turtles.get ( i ) );
			}
		    }
		    else if ( restrictions [ 1 ].equals ( ">" ) ) {
			for ( int i = 0 ; i < turtles.size() ; i++ ) {
			    if ( turtles.get ( i ).getWho() > who )
				callTurtles.add ( turtles.get ( i ) );
			}
		    }
		    else if ( restrictions [ 1 ].equals ( "<" ) ) {
			for ( int i = 0 ; i < turtles.size() ; i++ ) {
			    if ( turtles.get ( i ).getWho() < who )
				callTurtles.add ( turtles.get ( i ) );
			}
		    }
		    else if ( restrictions [ 1 ].equals ( ">=" ) ) {
			for ( int i = 0 ; i < turtles.size() ; i++ ) {
			    if ( turtles.get ( i ).getWho() >= who )
				callTurtles.add ( turtles.get ( i ) );
			}
		    }
		    else if ( restrictions [ 1 ].equals ( "<=" ) ) {
			for ( int i = 0 ; i < turtles.size() ; i++ ) {
			    if ( turtles.get ( i ).getWho() <= who )
				callTurtles.add ( turtles.get ( i ) );
			}
		    }
		}
		if ( restrictions [ 0 ].equals ( "color" ) ) {
		    System.out.println ( "restrict based on color" );
		    try {
			Field field = Class.forName ( "java.awt.Color" ).getField ( restrictions [ 2 ] );
			Color color = (Color) field.get ( null );
			for ( Turtle turtle : turtles ) {
			    if ( restrictions [ 1 ].equals ( "=" ) ) {
				if ( turtle.getColor().equals ( color ) )
				    callTurtles.add  ( turtle );
			    }
			    else if ( restrictions [ 1 ].equals ( "!=" ) ) {
				if ( !turtle.getColor().equals ( color ) )
				    callTurtles.add ( turtle );
			    }
			}
		    }
		    catch ( Exception e ) {
			System.out.println ( "messed up here" );
			System.out.println ( e );
		    }
		}
		if ( restrictions [ 0 ].equals ( "breed" ) ) {
		}
		
		if ( restrictions [ 0 ].equals ( "xcor" ) ) {
		    for ( Turtle turtle : turtles ) {
			if ( restrictions [ 1 ].equals ( "=" ) ) {
			    if ( turtle.getXcor() == Double.parseDouble ( restrictions [ 2 ] ) )
				callTurtles.add ( turtle );
			}
			else if ( restrictions [ 1 ].equals ( "!=" ) ) {
			    if ( turtle.getXcor() != Double.parseDouble ( restrictions [ 2 ] ) )
				callTurtles.add ( turtle );
			}
			else if ( restrictions [ 1 ].equals ( ">" ) ) {
			    if ( turtle.getXcor() > ( Double.parseDouble ( restrictions [ 2 ] ) ) )
				callTurtles.add ( turtle );
			}
			else if ( restrictions [ 1 ].equals ( "<" ) ) {
			    if ( turtle.getXcor() < ( Double.parseDouble ( restrictions [ 2 ] ) ) )
				callTurtles.add ( turtle );
			}		
		    }
		}
		
		else if ( restrictions [ 0 ].equals ( "ycor" ) ) {
		    for ( Turtle turtle : turtles ) {
			if ( restrictions [ 1 ].equals ( "=" ) ) {
			    if ( turtle.getYcor() == Double.parseDouble ( restrictions [ 2 ] ) )
				callTurtles.add ( turtle );
			}
			else if ( restrictions [ 1 ].equals ( "!=" ) ) {
			    if ( turtle.getYcor() != Double.parseDouble ( restrictions [ 2 ] ) )
				callTurtles.add ( turtle );
			}
			else if ( restrictions [ 1 ].equals ( ">" ) ) {
			    if ( turtle.getYcor() > ( Double.parseDouble ( restrictions [ 2 ] ) ) )
				callTurtles.add ( turtle );
			}
			else if ( restrictions [ 1 ].equals ( "<" ) ) {
			    if ( turtle.getYcor() < ( Double.parseDouble ( restrictions [ 2 ] ) ) )
				callTurtles.add ( turtle );
			}		
		    }
		}

		else if ( restrictions [ 0 ].equals ( "heading" ) ) {
		    System.out.println ( "restrict based on heading" );
		    for(Turtle turtle : turtles) {
			System.out.println ( "turtle heading: " + turtle.getDir() );
			if(restrictions[1].equals( "=" ) ) {
			    if(turtle.getDir() == Integer.parseInt(restrictions[2] )) 
				callTurtles.add ( turtle );
			}
			else if(restrictions[1].equals( "!=" )) {
			    if(turtle.getDir() != Integer.parseInt(restrictions[2] ))
				callTurtles.add( turtle );
			}
			else if(restrictions[1].equals( "<")) {
			    if(turtle.getDir() < Integer.parseInt(restrictions[2] ))
				callTurtles.add( turtle );
			}
			else if(restrictions[1].equals( ">" )) {
			    if(turtle.getDir() > Integer.parseInt(restrictions[2] ))
				callTurtles.add( turtle );
			}
			else if(restrictions[1].equals( "<=" )) {
			    if(turtle.getDir() <= Integer.parseInt(restrictions[2] ))
				callTurtles.add( turtle );
			}
			else if(restrictions[1].equals( ">=" )) {
			    if(turtle.getDir() >= Integer.parseInt(restrictions[2] ))
				callTurtles.add( turtle );
			}
			else
			    System.out.println("not a valid operator");
		    }
		}
	    return callTurtles;
	}
	else if ( agentType.equals ( "patches" ) ) {
	    String[] restrictions1 = new String [ agents.size() - 4 ];
	    for ( int i = 3 ; !agents.get ( i ).equals ( "]" ) ; i++ )
		restrictions1 [ i - 3 ] = agents.get ( i );
	    ArrayList<String> restrictions = new ArrayList<String>();
	    //makes things within parentheses one item in restrictions, for example= patches with [ xcor > ( 5 + 1 ) ] becomes [ xcor , > , 5;+;1; ]
	    for ( int i = 0 ; i < restrictions1.length ; i++ ) {
		if ( !restrictions1 [ i ].equals ( "(" ) ) {
		    restrictions.add ( restrictions1 [ i ] );
		}
		else {
		    String parentheses = new String();
		    i = i + 1;
		    while ( ! restrictions1 [ i ].equals ( ")" ) ) {
			parentheses = parentheses + restrictions1 [ i ] + ";";
			i = i + 1;
		    }
		    restrictions.add ( parentheses );
		}
	    }
	    //System.out.println ( "restrictions for patches: " + Arrays.toString ( restrictions ) );
	    System.out.println ( "restrictions for patches: " + restrictions );
	    ArrayList<int[]> callPatches = new ArrayList<int[]>(); //turtles on which the methods are being called
	    ArrayList<String> operators = new ArrayList<String>(); //ands and ors are added here
	    for ( int i = 0 ; i < restrictions.size() ; i = i + 4 ) {
		if ( i + 3 < restrictions.size() ) {
		    if ( restrictions.get ( i + 3 ).equals ( "and" ) ) {
			System.out.println ( "and" );
			operators.add ( "and" );
		    }
		    else if ( restrictions.get ( i + 3 ).equals ( "or" ) ) {
			operators.add ( "or" );
		    }
		    else System.out.println ( "failed- restrictions: " + restrictions );
		}
	    }
	    if ( operators.size() == 0 ) {
		for ( int r = 0 ; r < patches.length ; r++ ) {
		    for ( int c = 0 ; c < patches [ r ].length ; c++ ) {
			int[] patch = { r , c };
			if ( satisfiesCondition ( patch , restrictions.get ( 0 ) + "-" + restrictions.get ( 1 ) + "-" + restrictions.get ( 2 ) ) )
			    callPatches.add ( patch );
		    }
		}
	    }
	    for ( int j = 0 ; j < operators.size() ; j++ ) {
		System.out.println ( "restrictions: " + restrictions );
		System.out.println ( "operators: " + operators.get ( j ) );
		if ( operators.get ( j ).equals ( "and" ) ) {
		    for ( int r = 0 ; r < patches.length ; r++ ) {
			for ( int c = 0 ; c < patches [ r ].length ; c++ ) {
			    int[] patch = { r , c };
			    if ( satisfiesCondition ( patch , restrictions.get ( j * 4 ) + "-" + restrictions.get ( j * 4 + 1 ) + "-" + restrictions.get ( j * 4 + 2 ) ) && satisfiesCondition ( patch , restrictions.get ( j * 4 + 4 ) + "-" + restrictions.get ( j * 4 + 5 ) + "-" + restrictions.get ( j * 4 + 6 ) ) ) {
				int[] add = { r , c };
				callPatches.add ( add );
			    }
			}
		    }
		}
		else if ( operators.get ( j ).equals ( "or" ) ) {			
		    for ( int r = 0 ; r < patches.length ; r++ ) {
			for ( int c = 0 ; c < patches [ r ].length ; c++ ) {
			    int[] patch = { r , c };
			    if ( satisfiesCondition ( patch , restrictions.get ( j * 4 ) + "-" + restrictions.get ( j * 4 + 1 ) + "-" + restrictions.get ( j * 4 + 2 ) ) || satisfiesCondition ( patch , restrictions.get ( j * 4 + 4 ) + "-" + restrictions.get ( j * 4 + 5 ) + "-" + restrictions.get ( j * 4 + 6 ) ) ) {
				int[] add = { r , c };
				callPatches.add ( add );
			    }
			}
		    }
		}
	    }
	    //}
	    return callPatches;
	}
	return null;
    } 

    //~~~~~NEEDS TESTING~~~~~~~~~~~~~~~~~~~~~~~~~~                                                                                                       

    //ADD STUFF FOR PARENTHASES                                                                                                                           
    public int random(String s) {
        System.out.println(s);
        int num = Integer.parseInt(s);
        int val = (int) (Math.random() * num);
        return val;
    }

	//determine if condition true for if and ifelse                                                                                                 
    public boolean condition(String condition) {
        //boolean isokay = false;
	//JOptionPane.showMessageDialog ( null , "condition: " + condition);
	//System.out.println ( "condition: " + condition );
	if ( condition.equals ( "mouse-Down?;" ) )
	     return mouseDown;
	int firstInt;
	String firstString = condition.substring ( 0 , condition.indexOf ( ";" ) );
	condition = condition.substring ( condition.indexOf ( ";" ) + 1 );
	try {
	    firstInt = Integer.parseInt( firstString );
	} catch ( NumberFormatException e ) {
	    if ( globals.containsKey ( firstString ) ) {
		firstInt = globals.get ( firstString );
	    }
	    else if ( firstString.equals ( "count" ) ) {
		String countParam = new String();
		String conditionBeginning = condition.substring ( 0 , condition.indexOf ( ";" ) );
		String comparisons = "<>=!";
		int inBrackets = 0;
		while ( ! comparisons.contains ( conditionBeginning ) || inBrackets != 0 ) {
		    if ( conditionBeginning.equals ( "[" ) )
			inBrackets++;
		    else if ( conditionBeginning.equals ( "]" ) )
			inBrackets--;
		    countParam = countParam + conditionBeginning + ";";
		    condition = condition.substring ( condition.indexOf ( ";" ) + 1 );
		    conditionBeginning = condition.substring ( 0 , condition.indexOf ( ";" ) );
		}
		firstInt = count ( countParam );
	    }
	    else if ( firstString.equals ( "random" ) ) {
		firstInt = random ( condition.substring ( 0 , condition.indexOf ( ";" ) ) );
		condition = condition.substring ( condition.indexOf ( ";") + 1 );
	    }
	    else {
		System.out.println ( "firstString: " + firstString );
		throw new NumberFormatException();
	    }
	}
	String operator = condition.substring ( 0 , condition.indexOf ( ";" ) );
	condition = condition.substring ( condition.indexOf ( ";" ) + 1 );
	int secondInt;
	String secondString = condition.substring ( 0 , condition.indexOf ( ";" ) );
	condition = condition.substring ( condition.indexOf ( ";" ) + 1 );
	try {
	    secondInt = Integer.parseInt( secondString );
	} catch ( NumberFormatException e ) {
	    if ( globals.containsKey ( secondString ) ) {
		secondInt = globals.get ( secondString );
	    }
	    else if ( secondString.equals ( "count" ) ) {
		String countParam = new String();
		String conditionBeginning = condition.substring ( 0 , condition.indexOf ( ";" ) );
		String comparisons = "<>=!";
		int inBrackets = 0;
		while ( ! comparisons.contains ( conditionBeginning ) || inBrackets != 0 ) {
		    if ( conditionBeginning.equals ( "[" ) )
			inBrackets++;
		    else if ( conditionBeginning.equals ( "]" ) )
			inBrackets--;
		    countParam = countParam + conditionBeginning + ";";
		    condition = condition.substring ( condition.indexOf ( ";" ) + 1 );
		    conditionBeginning = condition.substring ( 0 , condition.indexOf ( ";" ) );
		}
		secondInt = count ( countParam );
	    }
	    else {
		throw new NumberFormatException();
	    }
	}
	//JOptionPane.showMessageDialog ( null , "if: " + firstInt + operator + secondInt );

			/*try {
			  if ( words.get ( i + 2 ).equals ( "with" ) ) {
			  String addLine = new String();
			  int j = 0;
			  while ( ! word.equals ( "]" ) ) {
			  word = words.get ( i + j + 1 );
			  System.out.println ( "WORD CAPS TO STAND OUT: " + word );
				    addLine = addLine + word + ";";
				    j = j + 1;
				}
				i = i + j;
				//System.out.println ( "i is now: " + i );
				//call method count with with
				message = message + count ( addLine );
			    }
			} catch ( Exception ex ) {
			    i = i + 1;
			    System.out.println ( "words: " + words );
			    e.printStackTrace();
			    System.out.println ( "exception: " + e );
			    message = message + count ( words.get ( i ) );
			    //call method count without with
			}
		    }
			*/
		//}
	    /*s = s.substring(s.indexOf(";") + 1);
	    String operator = s.substring(0, s.indexOf(";"));
	    s = s.substring(s.indexOf(";") + 1);
	    int secondval = Integer.parseInt(s.substring(0, s.indexOf(";")));
	    JOptionPane.showMessageDialog ( null , "comparison: " + firstval + operator + secondval );*/
	    if(operator.equals("=")) {
		if(firstInt == secondInt)
		    return true;
		    //isokay = true;
		return false;
	    }
	    else if(operator.equals("!=")) {
                if (firstInt != secondInt)
		    return true;
                    //isokay = true;
		return false;
            }
            else if(operator.equals("<")) {
                if(firstInt < secondInt)
		    return true;
                    //isokay = true;
		return false;
            }
            else if(operator.equals(">")) {
                if(firstInt > secondInt)
		    return true;
		//isokay = true;
		return false;
            }
            else if(operator.equals("<=")) {
                if(firstInt <= secondInt)
		    return true;
                    //isokay = true;
		return false;
            }
            else {
                System.out.println("Not correct operator");
		throw new UnsupportedOperationException();
	    }
	/*else if (condition.equals("random")) {
	    int firstval = random(s.substring(0, s.indexOf(";")));
            s = s.substring(s.indexOf(";") + 1);
	    String operator = s.substring(0, s.indexOf(";"));
            s = s.substring(s.indexOf(";") + 1);
            int secondval = Integer.parseInt(s.substring(0, s.indexOf(";")));
            if(operator.equals("=")) {
                if (firstval == secondval)
                    isokay = true;
            }
            else if(operator.equals("!=")) {
                if (firstval != secondval)
                    isokay = true;
            }
            else if(operator.equals("<")) {
                if(firstval < secondval)
                    isokay = true;
            }
            else if(operator.equals(">")) {
                if(firstval > secondval)
                    isokay = true;
            }
            else if(operator.equals("<=")) {
                if(firstval <= secondval)
                    isokay = true;
            }
            else
                System.out.println("Not correct operator");

        }

        else if(condition.equals("count")) {

        }*/

	    //else
            //System.out.println("Improper use of if statement");

	    //return isokay;
}

    public void IF(String s1) {
        //if condition is true run                                                                                                                        
        //System.out.println("if statement: " + s1);
        String s = new String();
        for(int i = 0; i < s1.length(); i++)
            s = s + s1.substring(i, i+1 );
	String condition = new String();
	//JOptionPane.showMessageDialog ( null , "s: " + s );
	while ( s.indexOf ( "[" ) != 0 ) {
	    String addWord = s.substring ( 0 , s.indexOf ( ";" ) + 1 );
	    s = s.substring ( s.indexOf ( ";" ) + 1 );
	    if ( addWord.equals ( "with;" ) ) {
		addWord = addWord + s.substring ( 0 , s.indexOf ( "]" ) );
		s = s.substring ( s.indexOf ( "]" ) );
	    }
	    condition = condition + addWord;
	}
	//System.out.println ( "condition: " + condition );
	ArrayList<String> conditions = new ArrayList<String>();
	ArrayList<String> booleanOperators = new ArrayList<String>();
	//boolean containsAndOr = false;
	//for ( int i = 0 ; i < condition.length() - 3 ; i++ ) {
	//if ( condition.substring ( i , i + 2 ).equals ( "or" ) || condition.substring ( i , i + 3 ).equals ( "and" ) )
	//containsAndOr = true;
	//break;
	//}
	if ( condition.indexOf ( ";and;" ) != -1 || condition.indexOf ( ";or;" ) != -1 ) {
	    while ( condition.indexOf ( ";and;" ) != -1 || condition.indexOf ( ";or;" ) != -1 ) {
	        //containsAndOr = false;
		//for ( int i = 0 ; i < condition.length() - 3 ; i++ ) {
		//if ( condition.substring ( i , i + 2 ).equals ( "or" ) || condition.substring ( i , i + 3 ).equals ( "and" ) )
		//containsAndOr = true;
		//break;
		//}
		System.out.println ( "testing while loop " + condition );
		if ( condition.indexOf ( ";and;" ) < condition.indexOf ( ";or;" ) && condition.indexOf ( ";and;" ) != -1 && condition.indexOf ( ";or;" ) != -1 ) {
		    conditions.add ( condition.substring ( 0 , condition.indexOf ( ";and;" ) + 1 ) );
		    condition = condition.substring ( condition.indexOf ( ";and;" ) + 5 );
		    booleanOperators.add ( "and" );
		}
		else if ( condition.indexOf ( ";or;" ) == -1 ) {
		    System.out.println ( "condition where exception thrown: " + condition + " , index is : " + condition.indexOf ( ";and;" ) );
		    conditions.add ( condition.substring ( 0 , condition.indexOf ( ";and;" ) + 1 ) );
		    condition = condition.substring ( condition.indexOf ( ";and;" ) + 5 );
		    booleanOperators.add ( "and" );
		}
		else {
		    conditions.add ( condition.substring ( 0 , condition.indexOf ( ";or;" ) + 1 ) );
		    condition = condition.substring ( condition.indexOf ( ";or;" ) + 4 );
		    booleanOperators.add ( "or" );
		}
		//break;
	    }
	    conditions.add ( condition );
	}
	else conditions.add ( condition );
	//System.out.println ( "conditions: " + conditions );
	//MIGHT NEED TO SUBSTRING IF OUT OF S1 BEFORE RUNNING CONDITION(S)
	boolean ifBoolean = condition ( conditions.get ( 0 ) );
	for ( int i = 0 ; i < booleanOperators.size() ; i++ ) {
	    if ( booleanOperators.get ( i ).equals ( "and" ) ) {
		if ( ifBoolean && condition ( conditions.get ( i + 1 ) ) )
		    ifBoolean = true;
		else ifBoolean = false;
	    }
	    else {
		if ( ifBoolean || condition ( conditions.get ( i + 1 ) ) )
		    ifBoolean = true;
		else ifBoolean = false;
	    }
	}
        if (ifBoolean) {
            System.out.println("condition true, running command");
	    //make s just the command portion
	    //s = s.substring(s.indexOf("[") + 1 , s.length() - 1);
	    //JOptionPane.showMessageDialog ( null , "condition is true, call methods: " + s );
	    //ask(s);
	    ArrayList<String> words = new ArrayList<String>();
	    ArrayList<String> ans = new ArrayList<String>();
	    while ( s.length() > 0 ) {
		System.out.println ( "testing loop" + s ) ;
		while ( s.indexOf ( ";" ) > -1 ) {
		    words.add ( s.substring ( 0 , s.indexOf ( ";" ) ) );
		    s = s.substring ( s.indexOf ( ";" ) + 1 );
		}
		words.add ( s );
		s = "";
	    }
	    for ( int i = 0 ; i < words.size() ; i++ ) {
		String word = words.get ( i );
		System.out.println ( "word in IF: " + word );
		if ( word.equals ( "crt" ) ) {
		    //ans.add ( word + ";" + words.get ( i + 1 ) );
		    //if ( word.equals ( "crt" ) ) {
		    //String addLine = word + ";" + words.get ( i + 1 );
		    i = i + 1;
		    if ( words.get ( i + 1 ).equals ( "[" ) ) {
			int inBrackets = -1;
			String addLine = word + ";" + words.get ( i );
			while ( ! word.equals ( "]" ) || inBrackets != 0 ) {
			    if ( word.equals ( "[" ) )
				inBrackets++;
			    if ( word.equals ( "]" ) )
				inBrackets--;
			    i = i + 1;
			    word = words.get ( i );
			    addLine = addLine + word + ";";
			}
			ans.add ( addLine );
			System.out.println ( "calling crt with : " + ans );
		    }
		    else 
			ans.add ( word + ";" + words.get ( i ) );
			//i = i + 1;
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
		    ans.add ( addLine.substring ( 0 , addLine.length() - 2 ) + "]" );//+ "fd;1;bk;1;];" );
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
		    }
		    ans.add ( addLine );
		}

		else if ( word.equals ( "set" ) ) {
		    System.out.println ( "globals: " + globals.entrySet() );
		    String addThis = word + ";";
		    i = i + 1;
		    word = words.get ( i );
		    while ( "1234567890+-*/".contains ( word )  || globals.containsKey ( word ) ) {
			addThis = addThis + word + ";";
			//System.out.println ( "addthis: " + addThis );
			i = i + 1;
			word = words.get ( i );
		    }
		    i = i - 1;
		    //System.out.println ( "addThis in set: " + addThis );
		    ans.add ( addThis );
		}
		else if ( word.equals ( "wait" ) ) {
		    ans.add ( word + ";" + words.get ( i + 1 ) );
		    i = i + 1;
		}
		else if ( word.equals ( "user-message" ) ) {
		    String addThis = "userMessage" + ";";
		    i = i + 1;
		    word = words.get ( i );
		    while ( ! word.equals ( ")" ) ) {
			addThis = addThis + word + ";";
			i = i + 1;
			word = words.get ( i );
		    }
		    ans.add ( addThis + word + ";" );
		}
		else {
		    ans.add ( word );
		    System.out.println ( "word: " + word );
		}
	    }
	    System.out.println ( "methods in IF: " + ans );
	    if ( ! ans.get ( 0 ).equals ( "[" ) || ! ans.get ( ans.size() - 1 ).equals ( "]" ) )
		throw new IllegalStateException();
	    ans.remove ( 0 );
	    ans.remove ( ans.size() - 1 );
	    for ( String mthd : ans ) {
		try {
		try {
		if ( mthd.contains ( ";" ) ) {
		    //System.out.println ( "mthd: " + mthd );
		    Method m = this.getClass().getMethod 
			(mthd.substring(0, mthd.indexOf( ";" )) , String.class );
		    m.invoke ( this , mthd.substring ( mthd.indexOf ( ";" ) + 1 ) );
		}
		else {
		    //call method with no parameters
		    Method m = this.getClass().getMethod ( mthd , null );
		    m.invoke ( this , null );
		}
		} catch ( NoSuchMethodException excep ) {
		System.out.println ( "call method: " + mthd );
		ArrayList<String> listMethods = methods.get ( mthd );
		for ( String mtd : listMethods ) {
		    if ( mtd.contains ( ";" ) ) {
			Method m = this.getClass().getMethod ( mtd.substring ( 0 , mtd.indexOf ( ";" ) ) , String.class );
			m.invoke ( this , mtd.substring ( mtd.indexOf ( ";" ) + 1 ) );
		    }
		    else {
			Method m = this.getClass().getMethod ( mtd , null );
			m.invoke ( this , null );
		    }
		}
		}
		} catch ( Exception e ) {
		    System.out.println ( "exception " + e + " was thrown, " + mthd + " is not a supported method" );
		    //e.printStackTrace();
		}
		/*} catch ( IllegalAccessException bleh ) {
		    System.out.println ( "not permitted" );
		} catch ( NoSuchMethodException aaa ) {
		    System.out.println ( "not a method" );
		} catch ( InvocationTargetException lastOne ) {
		    System.out.println ( "idk what this exception means" );
		    }*/
	    }
	}
        //else
            //System.out.println("condition false, not running command");
    }

     //if condition true, run command 1. if false, run command 2                                                                                           
    public void IFELSE(String s1) {
        System.out.println("ifelse: " + s1);
        String s = new String();
        for (int i = 0; i < s1.length(); i++)
            s = s + s1.substring(i, i + 1);

        if(condition(s)) {
            System.out.println("condition true, running command 1");

        }
        else{
            System.out.println("condition false, running command 2");
        }

    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   
    
    

    //ask commands
    public void ask ( String s1 ) {
	System.out.println ( "ask: " + s1 );
	ArrayList<String> agents = new ArrayList<String>();
	ArrayList<String> commands = new ArrayList<String>();
	String s = new String();
	for ( int i = 0 ; i < s1.length() ; i++ )
	    s = s + s1.substring ( i , i + 1 );
	boolean insideWith = false; //if there is "with" (turtles with who > 1, etc.)
	//System.out.println ( "s: " + s );
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
	}
	s = s.substring ( 2 );
	//System.out.println ( "agents: " + agents );
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
	//System.out.println ( "commands: " + commands );
	//System.out.println ( agents.size() );
	if ( agents.size() == 1 ) {
		if ( agents.get ( 0 ).equals ( "patches" ) ) { //ask patches to do things 
		    ArrayList<int[]> patchesList = new ArrayList<int[]>();
		    for ( int r = 0 ; r < patches.length ; r++ ) {
			for ( int c = 0 ; c < patches [ r ].length ; c++ ) {
			    int[] a = { r , c };
			    patchesList.add ( a );
			}
		    }
		    patchCommands ( patchesList , commands );
		    //ask ( "turtles;[;fd1;bk1;];" );
		    ArrayList<String> fdbk = new ArrayList<String>();
		    fdbk.add ( "fd" );
		    fdbk.add ( "1" );
		    fdbk.add ( "bk" );
		    fdbk.add ( "1" );
		    callCommands ( turtles , fdbk );
		    ask ( "turtles;[;fd;1;bk;1;];" );
		}
		if ( agents.get ( 0 ).equals ( "turtles" ) ) { //ask turtles to do things
		    callCommands ( turtles , commands );
		    //ask ( "turtles;[;fd1;bk1;];" );
	    }
	}
	else if ( agents.size() > 1 ) {
	    String agentType = agents.get ( 0 );
	    if ( agents.contains ( "with" ) ) {
	    if ( agentType.equals ( "turtles" ) ) {
		if ( agents.get ( 1 ).equals ( "with" ) ) {
		    //call methods on selected turtles
		    callCommands ( with ( "turtles" , agents ) , commands );
		    ask ( "turtles;[;fd;1;bk;1;];" );
		}
	    }
	    else if ( agentType.equals ( "patches" ) ) {
		//do things
		if ( agents.get ( 1 ).equals ( "with" ) ) {
		    patchCommands ( with ( "patches" , agents ) , commands );
		    ask ( "turtles;[;fd;1;bk;1;];" );
		}
	    }
	    }
	    else {
		if ( agentType.equals ( "patch" ) ) {
		    int x , y;
		    if ( agents.get ( 1 ).equals ( "mouse-xcor" ) )
			x = mouseX;// + patches.length / 2;
		    else x = Integer.parseInt ( agents.get ( 1 ) );
		    if ( agents.get ( 2 ).equals ( "mouse-ycor" ) )
			y = mouseY;// + patches.length / 2;
		    else y = Integer.parseInt ( agents.get ( 2 ) );
		    ArrayList<int[]> callPatch = new ArrayList<int[]>();
		    int[] addPatch = { y , x };
		    callPatch.add ( addPatch );
		    patchCommands ( callPatch , commands );
		    ask ( "turtles;[;fd;1;bk;1;];" );
		}
	    }
	}
    }//end ask

    //called to see if patch fits in with statement in ask-- patches with [ pxcor > 5 ] - go through each patch and see if its xcor is greater that 5
    public boolean satisfiesCondition ( int[] coors , String conditions1 ) {
	//System.out.println ( "conditions in satisfiesCondition: "+ conditions1 );
	//System.out.println ( "coors in satisfiesCondition: " + Arrays.toString ( coors ) );
	String conditions = new String();
	conditions = conditions1.toString();
	String a = conditions.substring ( 0 , conditions.indexOf ( "-" ) );
	conditions = conditions.substring ( conditions.indexOf ( "-" ) + 1 );
	String b = conditions.substring ( 0 , conditions.indexOf ( "-" ) );
	conditions = conditions.substring ( conditions.indexOf ( "-" ) + 1 );
	String c = conditions;
	int first , second , newC;
	if ( c.contains ( ";" ) ) {
	String[] ops = new String [ 3 ];
	for ( int i = 0 ; i < 3 ; i++ ) {
	    ops [ i ] = c.substring ( 0 , c.indexOf ( ";" ) );
	    c = c.substring ( c.indexOf ( ";" ) + 1 );
	}
	try {
	    first = Integer.parseInt ( ops [ 0 ] );
	} catch ( Exception e ) {
	    first = globals.get ( ops [ 0 ] );
	}
	try {
	    second = Integer.parseInt ( ops [ 2 ] );
	} catch ( Exception e ) {
	    second = globals.get ( ops [ 2 ] );
	}
	if ( ops [ 1 ].equals ( "+" ) )
	    newC =  first + second;
	else if ( ops [ 1 ].equals ( "-" ) )
	    newC = first - second;
	else if ( ops [ 1 ].equals ( "*" ) )
	    newC = first * second;
	else if ( ops [ 1 ].equals ( "/" ) )
	    newC = first / second;
	else throw new UnsupportedOperationException();
	} else newC = Integer.parseInt ( c );
	if ( a.equals ( "pxcor" ) ) {
	    if ( b.equals ( ">" ) ) {
		if ( coors [ 1 ] > newC + patches.length / 2 ) {
		    return true;
		}
		int aa = newC + patches.length / 2;
		//System.out.println ( "returned false with x: " + coors [ 1 ] + "\npatches.length/2: " + patches.length / 2 + "\nnewC: " + newC );
		return false;
	    }
	    else if ( b.equals ( "<" ) ) {
		if ( coors [ 1 ] < newC + patches.length / 2 ) {
		    return true;
		}
		return false;
	    }
	    else if ( b.equals ( "=" ) ) {
		if ( coors [ 1 ] == newC + patches.length / 2 ) {
		    return true;
		}
		return false;
	    }
	    else if ( b.equals ( "<=" ) ) {
		if ( coors [ 1 ] <= newC + patches.length / 2 ) {
		    return true;
		}
		return false;
	    }
	    else if ( b.equals ( ">=" ) ) {
		if ( coors [ 1 ] >= newC + patches.length / 2 ) {
		    return true;
		}
		return false;
	    }
	}
	else if ( a.equals ( "pycor" ) ) {
	    //System.out.println ( "pycor called" );
	    if ( b.equals ( ">" ) ) {
		if ( coors [ 0 ] < patches.length / 2 - newC ) {
		    return true;
		}
		return false;
	    }
	    else if ( b.equals ( "<" ) ) {
		if ( coors [ 0 ] > patches.length / 2 - newC ) {
		    System.out.println ( "true " + coors [ 0 ] );
		    return true;
		}
		System.out.println ( "returned false with y: " + coors [ 0 ] + " , newC=" + newC );
		return false;
	    }
	    else if ( b.equals ( "=" ) ) {
		if ( coors [ 0 ] == patches.length / 2 - newC ) {
		    return true;
		}
		return false;
	    }
	    else if ( b.equals ( ">=" ) ) {
		if ( coors [ 0 ] <= patches.length / 2 - newC ) {
		    return true;
		}
		return false;
	    }
	    else if ( b.equals ( "<=" ) ) {
		if ( coors [ 0 ] >= patches.length / 2 - newC ) {
		    return true;
		}
		//System.out.println ( "returned false with y: " + coors [ 0 ] );
		return false;
	    }
	}
	return false;
    }
    public void patchCommands ( ArrayList<int[]> patches , ArrayList<String> commands ) {
	System.out.println ( "patchcommands called" );
	for ( int i = 0 ; i < commands.size() ; i++ ) {
	    if ( commands.get ( i ).equals ( "set" ) ) {
		i = i + 1;
		if ( commands.get ( i ).equals ( "pcolor" ) ) {
		    i = i + 1;
		    Color newColor;
		    String color = commands.get ( i );
		    System.out.println ( "color changing to: " + color );
		    if( color.equals("red")) {
			newColor = Color.RED;
		    }
		    else if( color.equals("green")) {
			newColor = Color.GREEN;
		    }
		    else if( color.equals("blue")) {
			newColor = Color.BLUE;
		    }
		    else if( color.equals("yellow")) {
			newColor = Color.YELLOW;
		    }
		    else if ( color.equals ( "white" ) ) {
			newColor = Color.WHITE;
		    }
		    else if ( color.equals ( "brown" ) ) {
			newColor = new Color ( 139 , 80 , 14 );
		    }
		    else {
			newColor = null;
			System.out.println ( "invalid color" );
		    }
		    for ( int[] coors : patches ) {
			System.out.println ( "made patch " + coors [ 0 ] + ", " + coors [ 1 ] + " into newcolor" );
			if ( coors [ 0 ] > 0 && coors [ 0 ] < this.patches.length && coors [ 1 ] > 0 && coors [ 1 ] < this.patches [ 0 ].length )
			    this.patches [ coors [ 0 ] ] [ coors [ 1 ] ].setBackground ( newColor );
		    }
		    this.update ( this.getGraphics() );
		}
	    }
	}
    }
    public void callCommands ( ArrayList<Turtle> turtles , ArrayList<String> commands ) {
	System.out.println ( "methods called on turtles: " + turtles );
	//System.out.println ( "commands in callCommands: " + commands );
	for ( int i = 0 ; i < commands.size() ; i++ ) {
	    //forward + back
	    //System.out.println ( "commands.get: " + commands.get ( i ) );
	    if ( commands.get ( i ).equals ( "fd" ) || commands.get ( i ).equals ( "bk" ) ) {
		//JOptionPane.showMessageDialog ( null , "fd/bk called" );
		int j = 0;
		ArrayList<Turtle> removeTurtles = new ArrayList<Turtle>();
		ArrayList<Turtle> turtles1 = new ArrayList<Turtle>();
		for ( Turtle turtle : turtles )
		    turtles1.add ( turtle );
		while ( j < turtles1.size() ) {
		    Turtle turtle = turtles1.get ( j );
		    j = j + 1;
		    double xcor = turtle.getXcor();
		    double ycor = turtle.getYcor();
		    int dir = turtle.getDir();
		    //System.out.println ( "moving fd/bk, turtle at: " + xcor + ", " + ycor );
		    //System.out.println ( "x: " + xcor + "\ny: " + ycor + "\ndir: " + dir + "\nsin dir: " + round ( Math.sin ( Math.toRadians ( -1 * dir ) ) ) + "\ncos dir: " + round ( Math.cos ( Math.toRadians ( -1 * dir ) ) ) );
		    double steps = Integer.parseInt ( commands.get ( i + 1 ) ) * Math.sqrt ( patches [ 0 ] [ 0 ].size().getWidth() + patches [ 0 ] [ 0 ].size().getHeight() ) * 4;
		    //System.out.println ( "steps: " + steps );
		    //I DON'T KNOW WHY IT'S 4- CHANGE TO SIZE OF EACH PATCH LATER!!!- now it's based on size of patches, but still doesn't move enough
		    if ( commands.get ( i ).equals ( "fd" ) ) {
			//JOptionPane.showMessageDialog ( null , "called fd" );
			xcor = xcor + steps * round ( Math.cos ( Math.toRadians ( -1 * dir ) ) );
			ycor = ycor + steps * -1 * round ( Math.sin ( Math.toRadians ( -1 * dir ) ) );
		    }
		    else {
			xcor = xcor + steps * -1 * round ( Math.cos ( Math.toRadians ( -1 * dir ) ) );
			ycor = ycor + steps * round ( Math.sin ( Math.toRadians ( -1 * dir ) ) );
		    }
		    //System.out.println ( "x: " + xcor + "\ny: " + ycor + "\ndir: " + dir + "\nsin dir: " + round ( Math.sin ( Math.toRadians ( -1 * dir ) ) ) + "\ncos dir: " + round ( Math.cos ( Math.toRadians ( -1 * dir ) ) ) );
		    removeTurtles.add ( turtle );
		    Turtle t = new Turtle ( xcor , ycor , turtle.getDir() , turtle.getColor() , turtle.getBreed() , turtle.getTurtleSize() , turtle.getWho() );
		    System.out.println ( "new turtle created with size " + turtle.getTurtleSize() );
		    turtles.add ( t );
		    turtleSpace.add ( t );
		    //t.setBounds ( (int) xcor + 135 , (int) ycor + 135 , turtle.getIcon().getIconHeight(),  turtle.getIcon().getIconHeight() );
		    t.setBounds ( patches.length / 2 * (int) patches [ 0 ] [ 0 ].size().getWidth() + (int) xcor , patches [ patches.length / 2 ].length / 2 * (int) patches [ 0 ] [ 0 ].size().getHeight() + (int) ycor , turtle.getIcon().getIconWidth() , turtle.getIcon().getIconHeight() );
		}
		for ( Turtle turtle : removeTurtles ) {
		    turtleSpace.remove ( turtle );
		    turtles.remove ( turtle );
		}
		System.out.println ( "array of turtles: " + turtles );
		i = i + 1;
		this.update ( this.getGraphics() );
	    }
	    else if ( commands.get ( i ).equals ( "set" ) ) {
		System.out.println ( "set called" );
		i = i + 1;
		if ( commands.get ( i ).equals ( "color" ) ) {
		    i = i + 1;
		    String color = commands.get ( i );
		    Color newColor;
 		    if( color.equals("red")) {
			newColor = Color.RED;
		    }
		    else if( color.equals("green")) {
			newColor = Color.GREEN;
		    }
		    else if( color.equals("blue")) {
			newColor = Color.BLUE;
		    }
		    else if( color.equals("yellow")) {
			newColor = Color.YELLOW;
		    }
		    // else if ( color.equals ( "gray" ) ) {
		    // 	newColor = Color.BROWN;
		    // }
		    else {
			newColor = null;
			System.out.println ( "invalid color" );
		    }
		    for ( Turtle turtle : turtles ) {
			turtle.setColor ( newColor );
			//turtle.setBounds ( (int) turtle.getXcor() + 135 , (int) turtle.getYcor() + 135 , turtle.getIcon().getIconWidth() , turtle.getIcon().getIconHeight() );
			turtle.setBounds ( patches.length / 2 * (int) patches [ 0 ] [ 0 ].size().getWidth() + (int) turtle.getXcor() , patches [ patches.length / 2 ].length / 2 * (int) patches [ 0 ] [ 0 ].size().getHeight() + (int) turtle.getYcor() , turtle.getIcon().getIconWidth() , turtle.getIcon().getIconHeight() );
			turtleSpace.add ( turtle );
		    }
		    ask ( "turtles;[;fd;1;bk;1;];" );
		    //ADD OTHER COLORS OF RAINBOW
		}
		else if ( commands.get ( i ).equals ( "xcor" ) ) {
		    for ( Turtle turtle : turtles ) {
			System.out.println ( "changed xcor" );
			turtle.setXcor ( Double.parseDouble ( commands.get ( i + 1 ) ) * 10 );
			//turtle.setBounds ( /*25 + 12 * patches.length / 2*/135 + (int)turtle.getXcor() , /*25 + patches [ patches.length / 2 ].length / 2*/135 , /*( (BufferedImage) turtle.getImage() ).getWidth() , ( (BufferedImage) turtle.getImage() ).getHeight()*/turtle.getIcon().getIconWidth() , turtle.getIcon().getIconHeight() );
			turtle.setBounds ( patches.length / 2 * (int) patches [ 0 ] [ 0 ].size().getWidth() + (int) turtle.getXcor(), patches [ patches.length / 2 ].length / 2 * (int) patches [ 0 ] [ 0 ].size().getHeight() + (int) turtle.getYcor(), turtle.getIcon().getIconWidth() , turtle.getIcon().getIconHeight() );
		    }
		    i = i + 1;
		}
		else if ( commands.get ( i ).equals ( "ycor" ) ) {
		    for ( Turtle turtle : turtles ) {
			System.out.println ( "changed ycor" );
			turtle.setYcor ( Double.parseDouble ( commands.get ( i + 1 ) ) * -10 );
			//turtle.setBounds ( /*25 + 12 * patches.length / 2*/135 , /*25 + patches [ patches.length / 2 ].length / 2*/135 + (int)turtle.getYcor() , /*( (BufferedImage) turtle.getImage() ).getWidth() , ( (BufferedImage) turtle.getImage() ).getHeight()*/turtle.getIcon().getIconWidth() , turtle.getIcon().getIconHeight() );
			turtle.setBounds ( patches.length / 2 * (int) patches [ 0 ] [ 0 ].size().getWidth() + (int) turtle.getXcor(), patches [ patches.length / 2 ].length / 2 * (int) patches [ 0 ] [ 0 ].size().getHeight() + (int) turtle.getYcor(), turtle.getIcon().getIconWidth() , turtle.getIcon().getIconHeight() );
		    }
		    i = i + 1;
		}
		
		else if( commands.get( i ).equals( "heading" ) ) {
		    for( Turtle turtle : turtles ) {
			System.out.println("changed heading");
			turtle.setDir ( Integer.parseInt( commands.get( i + 1 ) ));
			//			System.out.println("COMMAND I + 1" + commands.get(i + 1));
		    }			
		    i+= 1;
		}    
		else if ( commands.get ( i ).equals ( "size" ) ) {
		    i = i + 1;
		    int size = Integer.parseInt ( commands.get ( i ) );
		    for ( Turtle turtle : turtles ) {
			System.out.println ( "set size to " + size );
			turtle.setTurtleSize ( size );
			turtle.setColor ( turtle.getColor() );
		    }
		    /*for ( Turtle turtle : turtles ) {
			System.out.println ( "set size to " + size );
			turtle.setTurtleSize ( size );
			}*/
		    //ask ( "turtles;[;fd;2;bk;2;];" );
		    ArrayList<String> cmds = new ArrayList<String>();
		    cmds.add ( "set" );
		    cmds.add ( "size" );
		    cmds.add ( "" + size );
		    //callCommands ( turtles , cmds );
		    //update ( getGraphics() );
		    //turtleSpace.repaint();
		    //patchSpace.repaint();
		}
		else {
		    System.out.println("You are setting incorrectly");
		/*Turtle[] moveTurtles = new Turtle [ turtles.size() ];
		Turtle[] removeTurtles = new Turtle [ turtles.size() ];
		int index = 0;
		for ( Turtle turtle : turtles ) {
		    removeTurtles [ index ] = turtle;
		    Turtle t = new Turtle ( turtle.getXcor() , turtle.getYcor() , turtle.getDir() , turtle.getColor() , turtle.getBreed() );
		    moveTurtles [ index ] = t;
		    index++;
		    turtles.add ( t );
		    turtleSpace.add ( t );
		    t.setBounds ( (int) t.getXcor() + 124 , (int) t.getYcor() + 124 , turtle.getIcon().getIconHeight(),  turtle.getIcon().getIconHeight() );
		}
		for ( Turtle turtle : removeTurtles ) {
		    turtleSpace.remove ( turtle );
		    turtles.remove ( turtle );
		}
		*/
		}
		ArrayList<String> fdbk = new ArrayList<String>();
		fdbk.add ( "fd" );
		fdbk.add ( "1" );
		fdbk.add ( "bk" );
		fdbk.add ( "1" );
		//callCommands ( turtles , fdbk );
		//ask ( "turtles;[;fd;1;bk;1;];" );
		//ask ( "turtles;[;fd;1;bk;1;];" );
		/*ask ( "patches;[;set;pcolor;red;];" );*/
		this.update ( this.getGraphics() );
	    }
	    else if ( commands.get ( i ).equals ( "die" ) ) {
		System.out.println ( "ask turtles to die: " + turtles );
		ArrayList<Turtle> removeTurtles = new ArrayList<Turtle>();
		for ( Turtle turtle : turtles )
		    removeTurtles.add ( turtle );
		for ( Turtle turtle : removeTurtles ) {
		    turtleSpace.remove ( turtle );
		    this.turtles.remove ( turtle );
		}
		this.update ( this.getGraphics() );
		    //this.turtles.remove ( turtle );
		    //System.out.println ( "ask turtle to die" );
		//for ( Turtle turtle : turtles )
		    //turtles.remove ( turtle );
	    }
	    else if ( commands.get ( i ).equals ( "ask" ) ) {
		System.out.println ( "ask in turtle" );
		i = i + 1;
		int iPlaceHolder = i;
		if ( commands.get ( i ).equals ( "patch-here" ) ) {
		    for ( Turtle turtle : turtles ) {
			i = iPlaceHolder;
			System.out.println ( "ask patch-here coors: " + turtle.getXcor() + ", " + turtle.getYcor() );
			ArrayList<int[]> patch = new ArrayList<int[]>();
			int[] addPatch = { (int) ( turtle.getYcor() / 10 ) + ( patches.length / 2 ) - 1 , (int) ( turtle.getXcor() / 10 ) + ( patches.length / 2 ) - 1 };
			patch.add ( addPatch );
			ArrayList<String> callPatchCommands = new ArrayList<String>();
			i = i + 2;
			System.out.println ( "commands: " + commands + " , i: " + i );
			while ( ! commands.get ( i ).equals ( "]" ) ) {
			    callPatchCommands.add ( commands.get ( i ) );
			    i = i + 1;
			}
			patchCommands ( patch , callPatchCommands );
			System.out.println ( "commmands: " + callPatchCommands );
		    }
		    ask ( "turtles;[;fd;1;bk;1;];" );
		}
		else if ( commands.get ( i ).equals ( "patch-at" ) ) {
		    for ( Turtle turtle : turtles ) {
			i = iPlaceHolder;
			System.out.println ( "ask patch-at coors: " + turtle.getXcor() + ", " + turtle.getYcor() );
			ArrayList<int[]> patch = new ArrayList<int[]>();
			int[] addPatch = { (int) ( turtle.getYcor() / 10 ) + ( patches.length / 2 ) - 1 - Integer.parseInt ( commands.get ( i + 2 ) ) , (int) ( turtle.getXcor() / 10 ) + ( patches.length / 2 ) - 1 + Integer.parseInt ( commands.get ( i + 1 ) ) };
			patch.add ( addPatch );
			ArrayList<String> callPatchCommands = new ArrayList<String>();
			i = i + 2;
			System.out.println ( "commands: " + commands + " , i: " + i );
			while ( ! commands.get ( i ).equals ( "]" ) ) {
			    callPatchCommands.add ( commands.get ( i ) );
			    i = i + 1;
			}
			patchCommands ( patch , callPatchCommands );
			System.out.println ( "commmands: " + callPatchCommands );
		    }
		    ask ( "turtles;[;fd;1;bk;1;];" );
		}
		else System.out.println ( "commands i: " + commands.get ( i ) );
	    }
	}
    }

    public void every ( String s1 ) {
	System.out.println ( "every called with parameter: " + s1 );
	double waitTime = Double.parseDouble ( s1.substring ( 0 , s1.indexOf ( ";" ) ) );
	System.out.println ( "wait: " + waitTime );
	String mthd = new String();
	mthd = s1.substring ( s1.indexOf ( ";" ) + 1 );
	mthd = mthd.substring ( mthd.indexOf ( ";" ) + 1 , mthd.length() - 2 );
	//split up different methods within every- only works with one method right now

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
    public void wait ( String s ) {
	double waitTime = Double.parseDouble ( s );
	try {
	    Thread.sleep ( (int) ( waitTime * 1000 ) );
	} catch ( Exception e ) {
	    System.out.println ( "wait failed, waitTime is: " + waitTime );
	}
    }
    public void userMessage ( String s1 ) {
	String s = new String();
	s = s1;
	//int size = 0;
	ArrayList<String> words = new ArrayList<String>();
	while ( s.length() > 0 ) {
	    words.add ( s.substring ( 0 , s.indexOf ( ";" ) ) );
	    s = s.substring ( s.indexOf ( ";" ) + 1 );
	}
	System.out.println ( "userMessage words: " + words );
	System.out.println ( words.get ( words.size() - 1 ) );
	if ( ( ! words.get ( 0 ).equals ( "(" ) ) || ( ! words.get ( words.size() - 1 ).equals (  ")" ) ) )
	    throw new IllegalStateException();
	words.remove ( 0 );
	words.remove ( words.size() - 1 );
	System.out.println ( "after remove words: " + words );
	if ( words.size() == 1 ) {
	    String word = words.get ( 0 );
	    if ( word.substring ( 0 , 1 ).equals ( "\"" ) && word.substring ( word.length() - 1 , word.length() ).equals ( "\"" ) )
		JOptionPane.showMessageDialog ( null, word.substring ( 1 , word.length() - 1 ) );
	    else throw new IllegalStateException();
	}
	else {
	    boolean inQuotes = false;
	    String message = new String();
	    //for ( String word : words ) {
	    for ( int i = 0 ; i < words.size() ; i++ ) {
		String word = words.get ( i );
		System.out.println ( "words in loop in userMessage: " + word );
		if ( word.contains ( "\"" ) && word.indexOf ( "\"" ) == word.lastIndexOf ( "\"" ) ) {
		    if ( inQuotes )
			message = message + word.substring ( 0 , word.length() - 1 );
		    else message = message + word.substring ( 1 )+ " ";
		    inQuotes = !inQuotes;
		}
		if ( inQuotes ) {
		    //System.out.println ( "in quotes: " + word );
		    if ( ! word.contains ( "\"" ) )
			message = message + word + " ";
		}
		else {
		    System.out.println ( word );
		    if ( words.size() > i + 1 ) {
		    i = i + 1;
		    word = words.get ( i );
		    if ( ! globals.containsKey ( message ) && ! word.equals ( "count" ) ) {
			System.out.println ( "throw exception for word: " + word );
			throw new IllegalStateException();
		    }
		    else if ( word.equals ( "count" ) ) {
			try {
			    if ( words.get ( i + 2 ).equals ( "with" ) ) {
				String addLine = new String();
				int j = 0;
				while ( ! word.equals ( "]" ) ) {
				    word = words.get ( i + j + 1 );
				    System.out.println ( "WORD CAPS TO STAND OUT: " + word );
				    addLine = addLine + word + ";";
				    j = j + 1;
				}
				i = i + j;
				//System.out.println ( "i is now: " + i );
				//call method count with with
				message = message + count ( addLine );
			    }
			} catch ( Exception e ) {
			    i = i + 1;
			    System.out.println ( "words: " + words );
			    e.printStackTrace();
			    System.out.println ( "exception: " + e );
			    message = message + count ( words.get ( i ) );
			    //call method count without with
			}
		    }
		}
		//else System.out.println ( "out of quotes: " + word );
	    }
	    }
	    System.out.println ( "message: " + message );
	    JOptionPane.showMessageDialog ( null , message );
	}
    }
    public int count ( String s ) {
	System.out.println ( "count called with string: " + s );
	if ( ! s.contains ( ";" ) ) {
	    if ( s.equals ( "patches" ) )
		return patches.length * patches [ 0 ].length;
	    else if ( s.equals ( "turtles" ) )
		return turtles.size();
	}
	else {
	    String s1 = new String();
	    s1 = s;
	    String agent = s1.substring ( 0 , s1.indexOf ( ";" ) );
	    ArrayList<String> restrictions = new ArrayList<String>();
	    while ( s1.contains ( ";" ) ) {
		restrictions.add ( s1.substring ( 0 , s1.indexOf ( ";" ) ) );
		s1 = s1.substring ( s1.indexOf ( ";" ) + 1 );
	    }
	    if ( agent.equals ( "patches" ) ) {
		return with ( "patches" , restrictions ).size();
	    }
	    else if ( agent.equals ( "turtles" ) ) {
		return with ( "turtles" , restrictions ).size();
	    }
	}
	return 0;
    }
    public void set ( String s ) {
	System.out.println ( "set: " + s );
	String change = s.substring ( 0 , s.indexOf ( ";" ) );
	if ( globals.containsKey ( change ) ) {
	    System.out.println ( "get globals: " + globals.get ( change ) );
	    String operations = s.substring ( s.indexOf ( ";" ) + 1 );
	    System.out.println ( "do this to global: " + operations );
	    String[] ops = new String [ 3 ];
	    if ( operations.indexOf ( ";" ) == operations.length() - 1 ) {
		int first;
		try {
		    first = Integer.parseInt ( operations.substring ( 0 , 1 ) );
		} catch ( Exception e ) {
		    first = globals.get ( operations.substring ( 0 , 1 ) );
		}
		globals.put ( change , first );
	    }
	    else {
	    // if ( operations.indexOf ( ";" ) < operations.length() - 1 ) {
	    // 	System.out.println ( "operations index: " + operations.indexOf ( ";" ) + "\noperations.length: " + operations.length() );
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
	    //globals.remove ( change );
	    if ( ops [ 1 ].equals ( "+" ) )
		globals.put ( change , first + second );
	    else if ( ops [ 1 ].equals ( "-" ) )
		globals.put ( change , first - second );
	    else if ( ops [ 1 ].equals ( "*" ) )
		globals.put ( change , first * second );
	    else if ( ops [ 1 ].equals ( "/" ) )
		globals.put ( change , first / second );
	    //ask ( "turtles;[;fd;1;bk;1;];" );
	    }
	    // else {
	    // 	int first;
	    // 	try {
	    // 	    first = Integer.parseInt ( ops [ 0 ] );
	    // 	} catch ( Exception e ) {
	    // 	    first = globals.get ( ops [ 0 ] );
	    // 	}
	    // 	globals.put ( change , first );
	    // }
	}
	if ( change.equals ( "breed" ) ) {
	    //do things here
	}
	if ( change.equals ( "color" ) ) {
	    String color = s.substring ( s.indexOf ( ";" ) + 1 );
	    System.out.println ( "color: " + color );
	}
	//ask ( "turtles;[;fd;1;bk;1;];" );
    }
    //round double to smaller double, bc doubles are slightly off
    public double round ( double x ) {
	return (int) ( x * 100 ) / (double) 100;
    }
}

class Patch extends JPanel/* implements MouseListener*/ {
    private Image image;
    private int x , y;
    public Patch ( int x , int y ) {
	image = null;
	this.x = x;
	this.y = y;
	super.setPreferredSize ( new Dimension ( 2 , 2 ) );
	super.setMaximumSize ( new Dimension ( 2 , 2 ) );
	super.setMinimumSize ( new Dimension ( 2 , 2 ) );
    }
    public int getXcor() {
	return x;
    }
    public int getYcor() {
	return y;
    }
    /*public void mouseExited ( MouseEvent e ) {
	System.out.println ( "mouseExited" );
    }
    public void mouseEntered ( MouseEvent e ) {
	System.out.println ( "mouseEntered" );
	//System.out.println ( MouseInfo.getPointerInfo().getLocation() );
    }
    public void mouseReleased ( MouseEvent e ) {
	//mouseDown = false;
	//System.out.println ( "mouseReleased" );
    }
    public void mousePressed ( MouseEvent e ) {
	//mouseDown = true;
	//mouseX = e.getX();
	//mouseY = e.getY();
	//System.out.println ( "mousePressed, coors: " + mouseX + ", " + mouseY );
    }
    public void mouseClicked ( MouseEvent e ) {
    }*/
}

class Turtle extends JLabel {
    private double xcor , ycor;
    private int dir , size , who;
    private String breed;
    private Color color;
    private ImageIcon image;
    /*public Turtle ( double xcor , double ycor  ) {
	this ( xcor , ycor , (int) ( Math.random() * 360 ) , Color.RED , new String() , 1 ,);
	/*this.setOpaque ( true );
	this.xcor = xcor;
	this.ycor = ycor;
	breed = new String();
	setPreferredSize ( new Dimension ( 13 , 13 ) );
	/*
	int colorprob = (int) (Math.random() * 4);
	if(colorprob < 1)
	    color = Color.RED;
	else if(1 < colorprob && colorprob < 2)
	    color = Color.GREEN;
	else if(2 < colorprob && colorprob < 3)
	    color = Color.BLUE;
	else
	color = Color.YELLOW; 
	color = Color.RED;
	setIcon ( new ImageIcon ( "red_arrow.png" ) );
	size = 1;
	//setIcon ( null );
	//this.setBackground ( null );
	this.repaint();*/
    //}
    public Turtle ( double xcor , double ycor , int dir , Color color , String breed , int size , int who ) {
	System.out.println ( "turtle created at " + xcor + ", " + ycor + " facing " + dir );//+ " with who: " + who );
	this.xcor = xcor;
	this.ycor = ycor;
	this.dir = dir;
	setColor ( color );
	this.breed = breed;
	setTurtleSize ( size );
	this.who = who;
    }
    public void setTurtleSize ( int size ) {
	System.out.println ( "setting size to " + size );
	//this.setPreferredSize ( new Dimension ( size , size ) );
	//Image img = mgIcon.getImage();
	Image img = image.getImage();
	Image newImg = rotate ( resizeImage ( img , 13 * size , 13 * size ) , dir );
	//Image newImg = resizeImage ( img , 13 * size , 13 * size );
	super.setIcon ( new ImageIcon ( newImg ) );
	//this.setSize ( new Dimension ( 13 * size , 13 * size ) );
	//setIcon ( new ImageIcon ( newImg ) , dir );
	//this.setBackground ( Color.GRAY );
	//super.setPreferredSize ( new Dimension ( 13 * size , 13 * size ) );
	this.size = size;
	//this.setSize ( new Dimension ( size , size ) );
	System.out.println ( "size of panel: " + this.getSize() );
	//this.repaint();
	//ask ( "turtles;[;fd;1;bk;1;];" );
	}
    public int getTurtleSize() {
	return size;
    }
    public int getWho() {
	return who;
    }
    public void setIcon ( ImageIcon imgIcon ) {
	setIcon ( imgIcon , (int) ( Math.random() * 360 ) );
	//setIcon ( imgIcon , 90 );
    }
    public void setIcon ( ImageIcon imgIcon , int rotation ) {
	System.out.println ( "rotation in setIcon: " + rotation );
	Image img = imgIcon.getImage(); //image of parameter
	//Image img = image.getImage();
	Image newImg = rotate ( resizeImage ( img , 13 , 13 ) , rotation );
	super.setIcon ( new ImageIcon ( newImg ) );
    }
    public String toString() {
	//return "Turtle at: " + xcor + ", " + ycor + " facing: " + dir + "Color: " + color + "who: " + who;
	return "who: " + who + "\n";
    }
    public double getXcor() {
	return xcor;
    }
    public double getYcor() {
	return ycor;
    }
    public String getBreed() {
	return breed;
    }
    public void setXcor ( double newX ) {
	xcor = newX;
    }
    public void setYcor ( double newY ) {
	ycor = newY;
    }
    
    public void setColor(Color color) {
	System.out.println ( "setting color to: " + color );
	this.color = color;
	if ( color.equals ( Color.RED ) ) {
	    image = new ImageIcon ( "red_arrow.png" );
	    setIcon ( image , dir );
	}
	else if ( color.equals ( Color.BLUE ) ) {
	    image = new ImageIcon ( "blue_arrow.png" );
	    setIcon ( image , dir );
	}
	else if ( color.equals ( Color.GREEN ) ) {
	    image = new ImageIcon ( "green_arrow.png" );
	    setIcon ( image , dir );
	}
	else if ( color.equals ( Color.YELLOW ) ) {
	    image = new ImageIcon ( "yellow_arrow.png" );
	    setIcon ( image , dir );
	}
	else System.out.println ( "not color" );
    }
    public Color getColor() {
	return color;
    }
    //resize to size of patch
    public BufferedImage resizeImage ( Image image , int width , int height ) {
	BufferedImage buffImage = new BufferedImage ( width , height , BufferedImage.TYPE_INT_RGB );
	Graphics2D gr = buffImage.createGraphics();
	gr.setComposite ( AlphaComposite.Src );
	gr.drawImage ( image , 0 , 0 , width , height , null );
	gr.dispose();
	return buffImage;
    }
    public void setDir ( int dir ) {
	this.dir = dir;
    }
    public int getDir() {
	return dir;
    }
    //rotate image
    public BufferedImage rotate ( BufferedImage image , int rotation ) {
	dir = rotation;
	System.out.println ( "rotate" + rotation );
	int w = image.getWidth();
	int h = image.getHeight();
	BufferedImage buffImage = new BufferedImage ( w , h , image.getType() );
	Graphics2D g = buffImage.createGraphics();
	g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON );
	Integer r = new Integer ( rotation );
	g.rotate ( Math.toRadians ( rotation ) , w / 2 , h / 2 );
	g.drawImage ( image , null , 0 , 0 );
	return buffImage;
    }
}