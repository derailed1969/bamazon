import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;



//This class displays the graphics of the program, as well as do most of the processing of graphics-related features
public class BamazonPanel extends Panel  implements MouseListener, KeyListener, MouseWheelListener{
    private CustomComponent selected;//There can only be one selected component, it recieves the input of any keypresses
    private CustomComponent allComponentsContainer;//This is the component that contains all other components
    private CustomComponent helpButton;//This is a universal button that is always shown so it doesn't fit in any other class
    private TwoPoint size;
    private javax.swing.Timer t;//Used for refreshing and drawing
    private User user=null;//This is set to the user that is currently logged in
    private Thinker bamazonManager=new Thinker();//Does most of the processing of non-graphics related features
    private int imagesLoadingSequenceBegun=-1;//Used when loading images, as it is a slow step and there needs to be an extra frame to display "loading" before the loading begins and the program temporarily freezes
    private CustomComponent loader;//I didn't want to have references to other components in this class but unfortunately it seems necessary due to the way I designed the components, in order to load across multiple timer ticks.

    //Constructor
    public BamazonPanel(TwoPoint size){
        this.size=size;
        //First large task when starting the program is to load the books and users
        try{
            bamazonManager.setBooks(bamazonManager.loadBooks());
        }catch(Exception e){
            System.out.println("Error loading books");
            javax.swing.JOptionPane.showMessageDialog(null, "This program must be run from a directory with folder \"files\"  which contains \"books55.txt\".", "Error loading files", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        try{
            bamazonManager.setUsers(bamazonManager.loadUsers());    
        }catch(Exception e){
            System.out.println("error loading users");
            javax.swing.JOptionPane.showMessageDialog(null, "This program must be run from a directory with folder \"files\"  which contains \"ratings55V3.txt\".", "Error loading files", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        //Used for controls
        addKeyListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
        //Components are generated once, they are never deleted, they only have their values modified
        generateComponents();
        setBackground(BamazonGui.bamazonGrey);

        //This timer refreshes every 50 milliseconds (20 times per second)
        t=new javax.swing.Timer(50,new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Re-draw on every timer refresh
                repaint();
            }
        });
        //Begin the program
        t.start();
    }

    //This method is called every frame
    public void update(Graphics g){
        //Used during the image loading on the first login.
        //First it does nothing for a frame to update the "loading" text, next frame it loads the images, and next frame it logs in
        if(imagesLoadingSequenceBegun<3){
            if(imagesLoadingSequenceBegun==2){
                imagesLoadingSequenceBegun++;
                logIn(loader);
                loader=null;
            }else if(imagesLoadingSequenceBegun==1){
                imagesLoadingSequenceBegun++;
                loadBookImages();
            }else if(imagesLoadingSequenceBegun==0){
                //do nothing this tick to allow the "loading" text to display and the panel to update
                imagesLoadingSequenceBegun++;
            }
        }

        //Graphics are double buffered to prevent flickering
        Image offscreen=createImage(size.getX(),size.getY());
        Graphics gOffscreen=offscreen.getGraphics();
        gOffscreen.setColor(getBackground());
        gOffscreen.fillRect(0,0,size.getX(),size.getY());
        paint(gOffscreen);
        g.drawImage(offscreen,0,0,this);
    }

    public void paint(Graphics g){
        //Checks if the window's size has changed, if it has then it calls a rearrange for all components.
        TwoPoint newSize= new TwoPoint(this.getWidth(),this.getHeight());
        if(!(size.getX()==newSize.getX() && size.getY()==newSize.getY())){
            size=newSize;
            allComponentsContainer.setSize(size);
            rearrange();
        }
        allComponentsContainer.draw(g, new TwoPoint(0,0));
    }

    //rearrange all components
    private void rearrange(){
        helpButton.setSize(new TwoPoint(30,18));
        helpButton.setLocation(new TwoPoint(size.getX()-5-helpButton.getSize().getX(),18));
        allComponentsContainer.rearrange();
    }

    //Generates the components when loading. Further components are generated by their derived classes
    private void generateComponents(){
        allComponentsContainer=new CustomComponent();

        helpButton= new CustomComponent();
        helpButton.setText("Help");
        helpButton.setFill(BamazonGui.bamazonOrange);
        helpButton.setAction(new int[]{10});

        ComponentLogin loginScreen=new ComponentLogin();
        ComponentMainMenu mainMenu=new ComponentMainMenu();
        loginScreen.generate(mainMenu);
        mainMenu.generate(loginScreen, bamazonManager);
        
        mainMenu.setAutoFull(true);
        mainMenu.setVisible(false);
        loginScreen.setAutoFull(true);

        allComponentsContainer.getComponents().add(mainMenu);
        allComponentsContainer.getComponents().add(loginScreen);
        allComponentsContainer.getComponents().add(helpButton);
        allComponentsContainer.setLocation(new TwoPoint(0, 0));
        //The size of all components is reduced by 1 pixel so they don't occupy the window's border
        allComponentsContainer.setSize( new TwoPoint(size.getX()-1, size.getY()-1));

        //At this point all components have been generated now that the login screen and main menu are generated, so this will rearrange all of them to fit the screen
        rearrange();
    }
    //Loads the images for the book covers
    private void loadBookImages(){
        for(int i=0;i<bamazonManager.getBooks().length;i++){
            BufferedImage original=null;
            original=(BufferedImage)BamazonGui.loadImage("files\\" +i+".jpg");
            //Sets the full-sized image
            bamazonManager.getBooks()[i].setFullImage(original);

            //Calculates the width:height ratio of the image in order to set the low-definition image
            float widthRatio=(((float)BamazonGui.bookIconMaxSize.getX()/original.getWidth(null)));
            float heightRatio=(((float)BamazonGui.bookIconMaxSize.getY()/original.getHeight(null)));
            float shrinkRatio;
            //Determines how much the low-definition image should be shrunk by in order to fit within the max size
            if(widthRatio<heightRatio){
                shrinkRatio=widthRatio;
            }else{
                shrinkRatio=heightRatio;
            }
            //Shrinks the image and sets it as the book's preview image. This will make the program run much faster as many preview images are drawn every frame.
            int newWidth=(int)Math.min(BamazonGui.bookIconMaxSize.getX(),shrinkRatio*original.getWidth(null));
            int newHeight=(int)Math.min(BamazonGui.bookIconMaxSize.getY(),shrinkRatio*original.getHeight(null));
            bamazonManager.getBooks()[i].setPreviewImage(bamazonManager.getBooks()[i].getFullImage().getScaledInstance(newWidth,newHeight, Image.SCALE_FAST));
        }
    }
    //Performs the custom action of a CustomComponent depending on it's "action" int[] and "target" CustomComponent[]
    private void doAction(CustomComponent b){
        switch(b.getAction()[0]){
            case 0://log in
                String nam=b.getTarget()[1].getText();
                String pass=b.getTarget()[2].getText();
                if(bamazonManager.searchUser(nam)!=null){
                    if(bamazonManager.searchUser(nam).getPass().equals("") || bamazonManager.searchUser(nam).getPass().equals(pass)){
                        if(imagesLoadingSequenceBegun<0){
                            b.getTarget()[3].setText("Logging in, Loading Book Images... (This may take a moment)");
                            loader=b;
                            imagesLoadingSequenceBegun=0;
                            break;
                        }else if(imagesLoadingSequenceBegun>2){
                            logIn(b);
                        }    
                    }else{
                        b.getTarget()[3].setText("Incorrect password");
                    }
                }else{
                    b.getTarget()[3].setText("User does not exist. (Case-sensitive)");
                }
                b.getTarget()[2].setText("");
                break;
            case 1://invert visibility of compoennts
                for(int i=0;i<b.getTarget().length;i++){
                    if(b.getTarget()[i].getVisible()){
                        b.getTarget()[i].setVisible(false);
                    }else{
                        b.getTarget()[i].setVisible(true);
                    }
                }
                break;
            case 2://Switch tabs
                b.rearrange();
                b.getTarget()[1].setText("");
                b.getTarget()[0].rearrange();
                b.getTarget()[0].setVisible(true);
                for(int i=2;i<b.getTarget().length;i++){
                    b.getTarget()[i].setVisible(false);
                }
                break;
            case 3://do action of another component
                doAction( b.getTarget()[0]);
                break;
            case 4://select another component
                select(b.getTarget()[0]);
                break;
            case 5://search in the book menu
                for(int i=0;i<b.getTarget().length;i++){
                    if(b.getTarget()[i].getVisible()){
                        ((ComponentBooksMenu)b.getTarget()[i]).rearrange();
                        ((ComponentBooksMenu)b.getTarget()[i]).setChosenBook(null);
                    }
                }
                break;
            case 6://select a book
                ((ComponentBooksMenu)(b.getTarget()[0])).setChosenBook((ComponentBook)b);
                break;
            case 7://try to create an account
                nam=b.getTarget()[0].getText();
                pass=b.getTarget()[1].getText();
                String passCheck=b.getTarget()[2].getText();
                if(!nam.equals("")){
                    if(bamazonManager.searchUser(nam)==null){
                        if(pass.length()>2){
                            if(pass.equals(passCheck)){
                                User u=new User(bamazonManager.getBooks().length);
                                u.setName(nam);
                                u.setPass(pass);
                                bamazonManager.getUsers().add(u);
                                bamazonManager.saveUsers();
                                b.getTarget()[3].setText("User successfully registered. You can now log in");
                            }else{
                                b.getTarget()[3].setText("The password and confirmation do not match.");
                            }
                        }else{
                            b.getTarget()[3].setText("That password is not long enough.");
                        }
                    }else{
                        b.getTarget()[3].setText("That username is already in use.");
                    }
                }else{
                    b.getTarget()[3].setText("Please enter a username");
                }
                break;
            case 8://scroll in the book menu
                ((ComponentBooksMenu)b.getTarget()[0]).scroll(b.getAction()[1]);
                break;
            case 9://rate a book
                int starClicked=b.getAction()[1]+1;
                int newAmount;
                if(((ComponentStars)b).getFilledAmount()==starClicked){
                    newAmount=0;
                }else{
                    newAmount=starClicked;
                }
                ((ComponentStars)b).setFilledAmount(newAmount);
                user.setRating(((ComponentBookDesc)b.getTarget()[0]).getBook().getIndex(), ((ComponentStars)b).getRatingValue());
                bamazonManager.saveUsers();
                ((ComponentMainMenu)b.getTarget()[1]).ratingUpdate();
                break;
            case 10://Open the help button
                new HelpGui(BamazonGui.bamazonGrey);
                break;
        }
    }
    //Log in action
    private void logIn(CustomComponent button){
        user=bamazonManager.searchUser(button.getTarget()[1].getText());
        ((ComponentMainMenu)button.getTarget()[0]).setUsername(user.getName());
        button.getTarget()[4].setVisible(false);
        button.getTarget()[0].setVisible(true);
    }
    //When a new component is selected the previous gets deselected
    public void select(CustomComponent c){
        if(selected!=null){
            selected.setSelected(false);
        }
        selected = c;
        c.setSelected(true);
    }
    //Check which component got clicked and set it as selected
    public void mousePressed (MouseEvent e)
    {
        select(allComponentsContainer.click(new TwoPoint(e.getX(), e.getY())));
    }
    //The mouse must be clicked and released on the same component for its action to be performed
    public void mouseReleased(MouseEvent e)
    {   
        CustomComponent released =allComponentsContainer.release(new TwoPoint(e.getX(), e.getY()));
        if(released.equals(selected)){
            //If a component can be typed on the click action is instead used to select it and type in it, and when you press enter its actual action is performed
            if(selected.getAction()!=null && !selected.getTypeable()){
                doAction(selected);
            }
        }
    }
    //When a key is pressed, it will input it to a selected typeable component
    public void keyPressed(KeyEvent e) {
        if(selected!=null && selected.getTypeable()){
            int key=e.getKeyCode();
            //Enter button performs actions
            if(key==10){
                doAction(selected);
            }
            //Backspace button removes characters
            if(key==8){
                if(selected.getText().length()>0){
                    selected.setText(selected.getText().substring(0,selected.getText().length()-1));
                }
            //These key ID numbers correspond to actual characters so their value is added to the selected component
            }else if(key==32||(key>40&&key<112)||(key>149&&key<154)){
                selected.setText(selected.getText() + e.getKeyChar());
            }
        }
    }
    //Scroll the hovered component
    public void mouseWheelMoved(MouseWheelEvent e) {
        allComponentsContainer.scroll(e.getUnitsToScroll(), new TwoPoint(e.getX(),e.getY()));
    }
    //Unused
    public void mouseDragged(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}