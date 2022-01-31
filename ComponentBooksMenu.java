import java.awt.*;

//This class contains all the books in the menu for browsing and viewing recommendations, as well as the descriptino at the side
public class ComponentBooksMenu extends CustomComponent{
    private ComponentBookDesc side;
    private ComponentBook[] booksComponents;
    private Book[] books;
    private TwoPoint minMargin=new TwoPoint(15,50);//Used when fitting Book Components into the menu
    private ComponentBook chosenBook=null;
    private CustomComponent lblStuff;
    private CustomComponent searchTextbox;
    private CustomComponent btnScrollUp;
    private CustomComponent btnScrollDown;
    private int scrollValue=0;//You can scroll through this menu to view more books
    private int maxScroll=0;
    private int margin=25;
    private int mode;//0=browse, 1=recommend
    private Book[] recommendedBooks=null;
    private User user=null;
    private ComponentMainMenu menu;
    private Thinker manager;

    //Constructor
    public ComponentBooksMenu(int mode, CustomComponent searchBar, Thinker manager, ComponentMainMenu menu){
        this.menu=menu;
        this.manager=manager;
        this.mode=mode;
        searchTextbox=searchBar;
        this.books=manager.getBooks();
        this.booksComponents=new ComponentBook[books.length];
        //Create book components
        for(int i=0;i<booksComponents.length;i++){
            booksComponents[i]=new ComponentBook(books[i],this);
            booksComponents[i].setFill(new Color(200,200,200));
            booksComponents[i].setTarget(new CustomComponent[]{ this});
            booksComponents[i].setAction(new int[]{6});
            getComponents().add(booksComponents[i]);
        }

        side=new ComponentBookDesc(manager, menu,this);
        getComponents().add(side);
        lblStuff=new CustomComponent();
        lblStuff.setVisible(false);
        getComponents().add(lblStuff);
        btnScrollDown=new CustomComponent();
        btnScrollUp=new CustomComponent();
        btnScrollDown.setText("▼");
        btnScrollUp.setText("▲");
        btnScrollDown.setFill(new Color(220,220,220));
        btnScrollUp.setFill(new Color(220,220,220));
        btnScrollDown.setAction(new int[]{8,10});
        btnScrollUp.setAction(new int[]{8,-10});
        btnScrollDown.setTarget(new CustomComponent[]{this});
        btnScrollUp.setTarget(new CustomComponent[]{this});
        getComponents().add(btnScrollDown);
        getComponents().add(btnScrollUp);
    }
    //Rearrange contained components
    protected void rearrangeUnique(){
        side.setLocation(new TwoPoint(getSize().getX()-200 -(Math.max(0,getSize().getX()/3-200)),-1));
        side.setSize(new TwoPoint(getSize().getX()- side.getLocation().getX(), getSize().getY()+2));
        btnScrollDown.setSize(new TwoPoint(margin-10,22));
        btnScrollUp.setSize(new TwoPoint(margin-10,22));
        btnScrollDown.setLocation(new TwoPoint(side.getLocation().getX()-btnScrollDown.getSize().getX()-5, getSize().getY()-btnScrollDown.getSize().getY()-5));
        btnScrollUp.setLocation(btnScrollDown.getLocation().subtract(new TwoPoint(0,btnScrollUp.getSize().getY()+5)));
        String search="";
        if(searchTextbox!=null && !searchTextbox.getText().equals("")){
            search=searchTextbox.getText();
        }
        
        //If it is browsing mode, all books are visible by default
        if(getMode()==0){
            for(int i=0;i<books.length;i++)
            {
                booksComponents[i].setVisible(true);
            }
        }
        else if(getMode()==1){//If it is recommendation mode, all books are non-visible by default
            for(int i=0;i<books.length;i++)
            {
                booksComponents[i].setVisible(false);
            }
            if(menu.getUsername()!=null){//If the recommended book list is not generated, generate it now
                if(recommendedBooks==null || !manager.searchUser(menu.getUsername()).equals(user)){
                    user=manager.searchUser(menu.getUsername());
                    setRecommendedBooks(manager.topTenBooks(user, manager.getUsers(), books));  
                }
            }
            if(recommendedBooks!=null){//Show all recommended books
                for(int i=0;i<recommendedBooks.length;i++)
                {
                    booksComponents[recommendedBooks[i].getIndex()].setVisible(true);
                }
            }
        }
        for(int i=0;i<books.length;i++)//Books that don't match the search criteria are hidden
        {
            if(!books[i].getTitle().toLowerCase().contains(search.toLowerCase())){
                booksComponents[i].setVisible(false);
            }
        }
        int a = arrangeBooks();//Arrange the visible books in the menu

        if(a==0){//If no books are visible, display a message to the user
            lblStuff.setLocation(new TwoPoint(5,5));
            if(getMode()==0){
                lblStuff.setText("No results found for \""+ search+'"');
            }else if(getMode()==1){
                lblStuff.setText("No recommendations match your search.");
            }
            lblStuff.setSize(getSize());
            lblStuff.setVisible(true);
        }else{
            lblStuff.setVisible(false);
        }
    }
    //This method arranges the visible book components in the menu and returns how many were visible
    private int arrangeBooks(){
        int xWidth=side.getLocation().getX()-margin;
        int xAmount=xWidth/(BamazonGui.bookIconMaxSize.getX()+ minMargin.getX()*2);
        int ySize=(BamazonGui.bookIconMaxSize.getY()+ minMargin.getX()+minMargin.getY());
        int xSize=xWidth/xAmount;
        int visibleBooks=0;

        for(int i=0;i<booksComponents.length;i++){
            if(booksComponents[i].getVisible()){
                visibleBooks++; 
            }
        }
        //Calculates how long the page should be, and if the user is currently scrolled past that point, the scroll is reduced to the maximum
        maxScroll= Math.max(0,(int)Math.ceil((float)visibleBooks/(float)xAmount)*ySize-this.getSize().getY());
        if(scrollValue>maxScroll){
            scrollValue=maxScroll;
        }
        int visibleCounter=0;
        //Set the location of all book components into a grid
        for(int i=0;i<booksComponents.length;i++){
            ComponentBook thisComponent=booksComponents[menu.getSortedBooks()[i].getIndex()];
            if(thisComponent.getVisible()){
                thisComponent.setSize(new TwoPoint(xSize,ySize));
                thisComponent.setLocation(new TwoPoint(xSize*(visibleCounter%xAmount),ySize*(visibleCounter/xAmount)-scrollValue));   
                visibleCounter++; 
            }
        }
        return visibleBooks;
    }
    //Scroll the menu up or down
    public void scroll(int amount){
        scroll(amount, null);
    }
    public void scroll(int amount, TwoPoint where){
            scrollValue+=amount*10;
            scrollValue=Math.max(0, Math.min(scrollValue, maxScroll));
            arrangeBooks();
    }
    //When the book menu is closed, the currently selected book is deselected
    public void resetUnique(){
        chosenBook=null;
    }
    //set the side menu's display to the newly selected book
    public void setChosenBook(ComponentBook b){
        if(b==null){
            reset();
        }else{
            chosenBook=b;
            side.setBook(chosenBook);    
        }
    }
    //Getters
    public ComponentBook getChosenBook(){
        return chosenBook;
    }
    public int getMode(){
        return mode;
    }
    public Book[] getRecommendedBooks(){
        return recommendedBooks;
    }
    //Setters
    public void setRecommendedBooks(Book[] r){
        recommendedBooks=r;
    }
    

} 
