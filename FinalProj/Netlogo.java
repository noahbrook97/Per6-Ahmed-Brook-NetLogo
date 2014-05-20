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
	/*try {
	    PrintWriter writer = new PrintWriter ( "methods.java" , "UTF-8" );
	    writer.println ( "public static class methods {" );
	    //writer.println ( n.ans );
	    //writer.println ( "}" );
	    writer.close();
	    //methods m = new methods();
	    //m.setup();
	  
	} catch ( Exception e ) {
	    //System.out.println ( "you messed up" );
	    JOptionPane.showMessageDialog ( null , "you messed up" );
	    }*/

    }
}

class NetlogoBoard implements Runnable {
    // private JPanel f;
    // private JFrame f2;
    private Screen s;
    private Thread t;
    private ArrayList<Method> listOfMethods;

    public NetlogoBoard() {
	JFrame f = new JFrame();
	s = new Screen();
	listOfMethods = new ArrayList<Method>();
	f.add ( s );
	//System.out.println ( s.getSelectedIndex() );
	f.show();
	f.pack();
	t = new Thread ( this );
	t.start();
    }

    public void run() {
	while ( true ) {
	    String txt = s.code.getText();
	    //System.out.println ( txt );
	    //System.out.println ( "hi" );
	    if ( s.getSelectedIndex() == 0 ) {
		//System.out.println ( s.code.getText() );
		//t.stop();
		//s.iface.setText ( javafy ( s.code.getText() ) );
		javafy ( s.code.getText() );
	    }
	    //if ( s.getSelectedIndex() == 2 )
		//t.start();
	}
    }
    public String javafy ( String s1 ) {
	/*JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
	StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
	Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(s));
	JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
	boolean success = task.call();
	//fileManager.close();*/
	
	//if ( s.equals ( "ca" ) ) {
	//this.s.iface.f.setBackground ( Color.cyan );
	//}
	/*for ( Method method : listOfMethods ) {
	    System.out.println ( method );
	    }*/
	String s = s1;
	ArrayList<String> words = new ArrayList<String>();
	boolean inMethod = false;
	String ans = new String();
	while ( s.length() > 0 ) {
	    if ( s.indexOf ( " " ) != -1 ) {
		words.add ( s.substring ( 0 , s.indexOf ( " " ) ) );
		s = s.substring ( s.indexOf ( " " ) + 1 );
	    }
	    else {
		words.add ( s );
		s = "";
	    }
	}
	if ( words.size() > 0 )
	    System.out.println ( words );
	for ( int i = 0 ; i < words.size() ; i++ ) {
	    String word = words.get ( i );
	    if ( word.equals ( "to" ) ) {
		inMethod = true;
		ans = ans + "public static void " + words.get ( i + 1 ) + "() {\n";
		i = i + 1;
	    }
	    else if ( word.equals ( "end" ) ) {
		ans = ans + "}";
		inMethod = false;
	    }
	    else if ( inMethod ) {
		if ( word.equals ( "ca" ) )
		    //ans = ans + "s.iface.setText ( javafy ( s.code.getText() ) );\n";
		    ans = ans + "System.out.println ( \"hi world\" ); super.ca()";
	    }
	}
	//ans = "public void hi() {System.out.println ( \"hi\" ) }";
	//if ( words.size() > 0 )JOptionPane.showMessageDialog ( null , "hello" );
	/*	try {
	    PrintWriter writer = new PrintWriter ( "methods.java" , "UTF-8" );
	    writer.println ( "public class methods extends Netlogo {" );
	    writer.println ( ans );
	    writer.println ( "}" );
	    writer.close();
	    //try {
	    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
	    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
	    Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList("methods.java"));
	    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
	    boolean success = task.call();
	    fileManager.close();
	    methods m = new methods();
	    Method method = m.getClass().getMethod ( "setup" , null );
	    method.invoke ( m , null );
	    //}
	    //m.setup();
	    //catch ( Exception e ){ }//JOptionPane.showMessageDialog ( null , "almost" );}
	    
	} catch ( Exception e ) {
	    e.printStackTrace();
	    //JOptionPane.showMessageDialog ( null , "you messed up" );
	    }*/
	return s1;
    }
    public void ca() {
	s.iface.f.setBackground ( Color.RED );
    }

    /*
    public StandardJavaFileManager getStandardFileManager ( DiagnosticListener<? super JavaFileObject> dl , Locale l , Charset c ) {
	return null;
    }
    public JavaCompiler.CompilationTask getTask ( Writer w , JavaFileManager jfm , DiagnosticListener<? super JavaFileObject> dl , Iterable<String> i1 , Iterable<String> i2 , Iterable <? extends JavaFileObject> i3 ) {
	return null;
    }
    public Set<SourceVersion> getSourceVersions() {
	return null;
    }
    public int run ( InputStream in , OutputStream out , OutputStream err , String... s ) {
	return 0;
    }
    public int isSupportedOption ( String option ) {
	return 0;
	}*/
 //    public Dimension getPreferredSize() {
//      	return new Dimension ( 400 , 400 );
//     }
}

class Screen extends JTabbedPane implements ActionListener {
    IFace iface;
    Code code;
    public Screen() {
	iface = new IFace();
	this.add ( "Interface" , iface );
	this.add ( "Info" , new JPanel() );
	code = new Code();
	code.setPreferredSize ( new Dimension ( 300 , 300 ) );
	/*JPanel p = new JPanel();
	JButton b = new JButton ( "Type name of Button" );
	p.add ( b );
	p.add ( code );*/
	this.add ( "Code" , code );
	//this.add ( p );
    }
    public void actionPerformed ( ActionEvent e ) {
	System.out.println ( e );
    }
    //protected ChangeListener createChangeListener() {
    //return new bs();
    //}
}

class Code extends JTextField {
    /*public Code() {
	this.add ( new JTextField() );
	}*/
}

class IFace extends JPanel implements MouseListener , KeyListener , ActionListener {
    private JPanel buttons;
    //private JButton button;
    protected JPanel f;
    public IFace() {
	buttons = new JPanel();
	//button =  new JButton ( "hello" );
	//buttons.add ( button );
	f = new JPanel();
	f.setPreferredSize ( new Dimension ( 200 , 300 ) );
	f.setBackground ( Color.BLUE );
	this.add ( buttons );
	this.add ( f );
	this.setPreferredSize ( new Dimension ( 400 , 400 ) );
	addMouseListener ( this );
	//addActionListener ( this );
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
	//if ( e.getButton() == MouseButton.BUTTON3 )
	System.out.println ( "mouseClicked" );
	if ( SwingUtilities.isRightMouseButton ( e ) ) {
	    System.out.println ( "right clicked" );
	    JPopupMenu menu = new JPopupMenu();
	    myButton button = new myButton();
	    button.addActionListener ( this );
	    menu.add ( button );
	    //menu.add ( "Button" );
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
	//	System.out.println ( "yes" );
    }
    public void keyPressed ( KeyEvent e ) {
	//System.out.println ( "yes" );
    }
    public void keyTyped ( KeyEvent e ) {
	//System.out.println ( "yes" );
    }
    //THIS SHOULD BE ABSTRACT AND BE DIFFERENT IN EACH MYBUTTON MYSLIDER ETC
    public void actionPerformed ( ActionEvent e ) {
	System.out.println ( ( ( myButton ) e.getSource() ).getText() );
	if ( ( ( myButton ) e.getSource() ).getText().equals ( "Button" ) ) {
	    String s = JOptionPane.showInputDialog ( null , "Type name of button" );
	    buttons.add ( new JButton ( s ) );
	}
    }
    /*public void setText ( String s ) {
     	button.setText ( s );
	}*/
    // public void setBackground ( Color color ) {
    // 	f.setBackground ( color );
    // }
}

class myButton extends JMenuItem {
    public myButton() {
	super ( "Button" );
    }
}

class mySlider extends JMenuItem {
    public mySlider() {
	super( "Slider" );
    }

    public void actionPerformed ( ActionEvent e ) {
        System.out.println ( ( ( mySlider ) e.getSource() ).getText() );
        if ( ( ( mySlider ) e.getSource() ).getText().equals ( "Slider" ) ) {
            String s = JOptionPane.showInputDialog ( null , "Type name of slider" );
            slider.add ( new JSlider ( s ) );
        }
    }

}
