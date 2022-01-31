//This class is for points in 2d space, it is useful for graphics and GUIs as there are many methods than can be performed with them
public class TwoPoint{
    private int x;
    private int y;
    //Constructor
    public TwoPoint(int a, int b){
        x=a;
        y=b;
    }

    //Subtract a point from another
    public TwoPoint subtract(TwoPoint b){
        return new TwoPoint(x-b.getX(),y-b.getY());
    }
    //Add two points
    public TwoPoint add(TwoPoint b){
        return new TwoPoint(x+b.getX(),y+b.getY());
    }
    //Check if this point is located within a rectangle given its location and size
    public boolean inRectangle(TwoPoint location, TwoPoint size){
        return (x>=location.x && y>=location.y&& x<=location.x+size.x&&y<=location.y+size.y);
    }
    //This is useful for debugging
    public String toString(){
        return "("+x+", "+y+")";
    }
    //Getters
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    //Setters
    public void setX(int newX){
        x=newX;
    }
    public void setY(int newY){
        y=newY;
    }
}