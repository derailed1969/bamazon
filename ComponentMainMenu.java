import java.awt.Color;
import java.io.IOException;
import java.awt.*;

//This component for the main menu, which is the main functional part of the program
public class ComponentMainMenu extends CustomComponent{
    private ComponentBooksMenu browse;
    private ComponentBooksMenu recommend;
    private CustomComponent logOut;
    private ComponentTab tabBrowse;
    private ComponentTab tabRecommend;
    private CustomComponent txtSearch;
    private ComponentImage btnSearch;
    private String username;
    private CustomComponent lblUser;
    private CustomComponent ribbon;
    private CustomComponent ribbon2;
    private ComponentImage littleLogo;
    private Thinker manager;
    private Book[] sortedBooks;
    public ComponentMainMenu(){

    }
    //This cannot be in the constructor because it and the login screen have to reference each other for their buttons to perform their actions
    //So they must be initialized first before their components can be created
    public void generate(CustomComponent loginScreen, Thinker manager){
        this.manager=manager;
        sortedBooks=manager.sortBooks(manager.getBooks(),manager.getUsers());

        txtSearch=new CustomComponent();
        txtSearch.setText("");
        txtSearch.setFakeText("Search Books");
        txtSearch.setFill(Color.WHITE);
        txtSearch.setFont(new Font("Arial", 0, 15));
        txtSearch.setTypeable(true);
        txtSearch.setAction(new int[]{3});
        txtSearch.setResetText(true);

        browse=new ComponentBooksMenu(0,txtSearch,manager,this);
        getComponents().add(browse);
        
        recommend=new ComponentBooksMenu(1,txtSearch,manager,this);
        recommend.setVisible(false);
        getComponents().add(recommend);

        ribbon=new CustomComponent();
        ribbon.setFill(new java.awt.Color(19,25,33));
        getComponents().add(ribbon);
        //add first to be at the bottom layer

        getComponents().add(txtSearch);
        //search bar needs to be created before the bookMenus so they can reference it, but must be added to the list after the ribbon so it is drawn on top

        ribbon2=new CustomComponent();
        ribbon2.setFill(new Color(35,47,62));
        getComponents().add(ribbon2);

        littleLogo=new ComponentImage();
        littleLogo.setImage(BamazonGui.loadImage("files\\whiteLogo.png"));
        getComponents().add(littleLogo);


        lblUser=new CustomComponent();
        lblUser.setText("oops");
        lblUser.setColour(Color.WHITE);
        getComponents().add(lblUser);

        logOut=new CustomComponent();
        logOut.setText("Sign out");
        logOut.setTarget(new CustomComponent[]{loginScreen,this});
        logOut.setAction(new int[]{1});
        logOut.setFill(BamazonGui.bamazonOrange);
        getComponents().add(logOut);
        
        btnSearch=new ComponentImage();
        try {
            btnSearch.setImage(javax.imageio.ImageIO.read(new java.io.File( "files\\magnify.png")));
        } catch (IOException e) {
            System.out.println("error loading image");
        }
        btnSearch.setAction(new int[]{5});
        btnSearch.setTarget(new CustomComponent[]{browse, recommend});
        btnSearch.setResetText(true);
        getComponents().add(btnSearch);

        txtSearch.setTarget(new CustomComponent[]{btnSearch});

        tabBrowse=new ComponentTab();
        tabBrowse.setText("Browse");
        tabBrowse.setColour(BamazonGui.defaultColour);
        tabBrowse.setFill(BamazonGui.bamazonGrey);
        tabBrowse.setFillTwo(BamazonGui.fakeColour);
        tabBrowse.setTarget(new CustomComponent[]{browse,txtSearch,recommend});
        tabBrowse.setAction(new int[]{2});
        getComponents().add(tabBrowse);

        tabRecommend=new ComponentTab();
        tabRecommend.setText("Recommend");
        tabRecommend.setColour(BamazonGui.defaultColour);
        tabRecommend.setFill(BamazonGui.bamazonGrey);
        tabRecommend.setFillTwo(BamazonGui.fakeColour);
        tabRecommend.setTarget(new CustomComponent[]{recommend,txtSearch,browse});
        tabRecommend.setAction(new int[]{2});
        getComponents().add(tabRecommend);
    }

    //Rearrange contained components
    protected void rearrangeUnique(){
        lblUser.setLocation(new TwoPoint(getSize().getX()-5-120,0));
        lblUser.setSize(new TwoPoint(getSize().getX()-lblUser.getLocation().getX()-10,20));
        logOut.setLocation(new TwoPoint(lblUser.getLocation().getX(),18));
        logOut.setSize(new TwoPoint(50,18));
        ribbon.setLocation(new TwoPoint(0,0));
        ribbon.setSize(new TwoPoint(getSize().getX()+1,40));
        littleLogo.setLocation(new TwoPoint(0, 0));
        littleLogo.setSize(new TwoPoint(3216/35,1352/35));
        txtSearch.setLocation(new TwoPoint(littleLogo.getSize().getX()+3,5));
        txtSearch.setSize(new TwoPoint(-txtSearch.getLocation().getX() +lblUser.getLocation().getX()-37,25));
        btnSearch.setLocation(new TwoPoint(txtSearch.getLocation().getX()+txtSearch.getSize().getX()+5,5));
        btnSearch.setSize(new TwoPoint(25,25));
        ribbon2.setLocation(new TwoPoint(0,ribbon.getSize().getY()));
        ribbon2.setSize(new TwoPoint(getSize().getX()+1,25));
        tabBrowse.setLocation(new TwoPoint(10,ribbon2.getLocation().getY()+7));
        tabBrowse.setSize(new TwoPoint( 48,18));
        tabRecommend.setLocation(new TwoPoint(tabBrowse.getLocation().getX()+tabBrowse.getSize().getX()+1,ribbon2.getLocation().getY()+7));
        tabRecommend.setSize(new TwoPoint( 80,18));
        browse.setLocation(new TwoPoint(0,ribbon2.getLocation().getY()+ribbon2.getSize().getY()));
        browse.setSize(new TwoPoint(getSize().getX(), getSize().getY()-browse.getLocation().getY()));
        recommend.setLocation(browse.getLocation());
        recommend.setSize(browse.getSize());
    }
    //When a rating is changed by the user, this method is called to refresh their displayed values
    public void ratingUpdate(){
        recommend.setRecommendedBooks(null);
        sortedBooks=manager.sortBooks(manager.getBooks(), manager.getUsers());
        rearrange();
    }
    //When the menu is left(logged out) the tabs are returned to their original state
    public void resetUnique(){
        browse.setVisible(true);
        recommend.setVisible(false);
    }


    //Getters
    public Book[] getSortedBooks(){
        return sortedBooks;
    }
    public String getUsername(){
        return username;
    }
    //Setters
    public void setUsername(String s){
        username=s;
        lblUser.setText("Hello, "+username);
    }
}
