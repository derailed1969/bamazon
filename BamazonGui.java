import java.awt.*;

//This class has the GUI window for the program to run
public class BamazonGui extends Frame{
    BamazonPanel p;

    //some constants for various reused GUI values to stay consistent
    public static final Font defaultFont = new Font("Arial",0,12);
    public static final Color defaultColour = new Color(15,17,17);//#0F1111
    public static final Color fakeColour = new Color(150,150,150);
    public static final Color selectedColour= new Color(255,153,0);
    public static final Color bamazonGrey=new Color(234,237,237);
    public static final Color bamazonOrange=new java.awt.Color( 254,189,105);
    public static final TwoPoint bookIconMaxSize=new TwoPoint(85,125);


    public BamazonGui(){
        super("Bamazon™ Electronic Book Catalogue");
        setSize(700,700);
        //Close the window when you click the X button
        addWindowListener(new java.awt.event.WindowAdapter() {    
            public void windowClosing (java.awt.event.WindowEvent e) {    
                dispose();    
            }    
        });    
        //Instantiate a BamazonPanel to show the program
        p=new BamazonPanel(new TwoPoint((int)getSize().getWidth(),(int)getSize().getHeight()));
        add(p);

        setVisible(true);

    }

    public static Image loadImage(String path){
        try {
            return javax.imageio.ImageIO.read(new java.io.File( path));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Could not find "+path, "Error loading image", javax.swing.JOptionPane.ERROR_MESSAGE);
            System.out.println("error loading image");
            return null;
        }
    }
    
}
