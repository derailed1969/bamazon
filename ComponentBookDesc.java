import java.awt.*;

//This class is for the menu on the side of the books browsing screen
class ComponentBookDesc extends CustomComponent{
    private CustomComponent title;
    private ComponentImage image; 
    private CustomComponent author;
    private ComponentBook chosenBook=null;
    private ComponentStars ratingStars;
    private ComponentMainMenu mainMenu;
    private Thinker manager;
    private ComponentBooksMenu bookMenu;

    //Constructor
    public ComponentBookDesc(Thinker manager,ComponentMainMenu mainMenu, ComponentBooksMenu booksMenu){
        this.manager=manager;
        this.mainMenu=mainMenu;
        this.bookMenu=booksMenu;
        title=new CustomComponent();
        title.setFont(new Font("Constantia", 0, 30));
        title.setWordWrap(true);
        getComponents().add(title);
        author = new CustomComponent();
        author.setWordWrap(true);
        getComponents().add(author);
        image= new ComponentImage();
        getComponents().add(image);
        ratingStars=new ComponentStars();
        ratingStars.setColour(Color.BLACK);
        ratingStars.setFill(new Color(255,71,0));
        ratingStars.setAction(new int[]{9,-1});
        ratingStars.setTarget(new CustomComponent[]{this,mainMenu});
        ratingStars.setVisible(false);
        ratingStars.setStarDiameter(0);
        getComponents().add(ratingStars);
        setFill(new Color(220,220,220));
        resetUnique();
    }
    //Fill in this area when drawn
    protected void drawUnique(Graphics g, TwoPoint p){
        g.setColor(this.getFill());
        fillRect(g, p);
        g.setColor(BamazonGui.defaultColour);
        drawRect(g, p);
    }
    //When a different book is selected this method is called to update the description
    public void setBook(ComponentBook b){
        chosenBook=b;
        title.setText(chosenBook.getBook().getTitle());
        image.setImage(chosenBook.getBook().getFullImage());
        author.setText("By: "+chosenBook.getBook().getAuthor());
        ratingStars.setFilledFromRating( manager.searchUser(mainMenu.getUsername()).getRating(chosenBook.getBook().getIndex()));
        rearrange();
    }
    //Rearrange contained components
    public void rearrangeUnique(){
        if(chosenBook!=null){
            //Stars are only visible on mode 0 (browsing)
            if(getMode()==0){
                ratingStars.setVisible(true);
                int starsSectionWidth=getSize().getX()-10;
                ratingStars.setStarDiameter((int)Math.max(30,starsSectionWidth/6*.85));
                ratingStars.setLocation(new TwoPoint(5,getSize().getY()-10-ratingStars.getStarDiameter()));
                ratingStars.setSize(new TwoPoint(starsSectionWidth,ratingStars.getStarDiameter()));    
            }
            title.setVisible(true);
            image.setVisible(true);
            author.setVisible(true);

            author.setSize(new TwoPoint(getSize().getX()-10,20));//adjust height for word wrapping
            title.setLocation(new TwoPoint(5,5));
            title.setSize(new TwoPoint(getSize().getX()-10,0));//adjust height for word wrapping
            title.setSize(new TwoPoint(title.getSize().getX(), title.drawTextWrapped(null, null)));
            
            //Depending on the size of book titles and author names, they may require extra vertical space to display when word-wrapped
            //So the other components are shifted downwards as a result
            int maxX=getSize().getX()-10;
            int maxY=Math.max(20, getSize().getY()-title.getSize().getY()-author.getSize().getY()-ratingStars.getStarDiameter()-35);
            //Image is the last thing resized, to take up all the remaining space
            float widthRatio=(((float)maxX/image.getImage().getWidth(null)));
            float heightRatio=(((float)maxY/image.getImage().getHeight(null)));
            float shrinkRatio;
            if(widthRatio<heightRatio){
                shrinkRatio=widthRatio;
            }else{
                shrinkRatio=heightRatio;
            }
            int newWidth=(int)Math.min(maxX,shrinkRatio*image.getImage().getWidth(null));
            int newHeight=(int)Math.min(maxY,shrinkRatio*image.getImage().getHeight(null));
            image.setLocation(new TwoPoint(5,title.getLocation().getY()+title.getSize().getY()+15));
            image.setSize(new TwoPoint(newWidth, newHeight));    
            
            author.setLocation(new TwoPoint(5,image.getLocation().getY()+image.getSize().getY()+5));
        }

    }
    //Reset components when the menu is closed
    public void resetUnique(){
        chosenBook=null;
        ratingStars.setVisible(false);
        image.setVisible(false);
        author.setVisible(false);
        title.setVisible(false);
        //animation is canceled completely here
    }
    //Getters
    public Book getBook(){
        return chosenBook.getBook();
    }
    public int getMode(){
        return bookMenu.getMode();
    }
}