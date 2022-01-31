import java.awt.*;


//This class has a component for each book that appears in the menu, with an image preview and title label
public class ComponentBook extends CustomComponent{
    private Book book;
    private ComponentBooksMenu container;
    private ComponentImage image;
    private CustomComponent label;

    //Click is overwritten so the contained components cannot be selected
    public CustomComponent click(TwoPoint where){
        return this;
    }
    public CustomComponent getComponentAtPoint(TwoPoint where){
        return this;
    }
    //Constructor
    public ComponentBook(Book b, ComponentBooksMenu selector){
        book=b;
        this.container=selector;
        image=new ComponentImage();
        image.setDrawMode(1);
        getComponents().add(image);
        label = new CustomComponent();
        label.setText(book.getTitle());
        label.setWordWrap(true);
        getComponents().add(label);
        setSize(new TwoPoint(0,0));
    }

    public void drawUnique(Graphics g, TwoPoint p){
        //Due to the way I designed the components, the image isn't loaded in memory until after the components are created
        //So its image needs to be checked assigned here instead
        if(image.getImage()==null){
            image.setImage(book.getPreviewImage());
        }
        if(container.getChosenBook()!=null && container.getChosenBook().equals(this)){
            g.setColor(getFill());
            fillRect(g, p);
        }
    }
    //Rearrange contained components
    public void rearrangeUnique(){
        image.setLocation(new TwoPoint(0,5));
        image.setSize(new TwoPoint(getSize().getX(),BamazonGui.bookIconMaxSize.getY()));
        int extraMargin=5;
        label.setSize(new TwoPoint(BamazonGui.bookIconMaxSize.getX()+extraMargin*2,getSize().getY()-image.getLocation().getY()-image.getSize().getY()-10));
        label.setLocation(new TwoPoint((getSize().getX()-label.getSize().getX())/2-extraMargin, image.getLocation().getY()+image.getSize().getY()+5));
    }
    //Return the Book object this component is based on
    public Book getBook(){
        return book;
    }

}
