import java.awt.Color;
import java.awt.Graphics;

//This class is used to rate books by clicking on the stars
public class ComponentStars extends CustomComponent{
    private int numStars;//How many stars appear
    private int[] starValues;//What the rating number of each star is
    private int amountFilled;//How many stars are filled in
    private int starDiameter;//How many pixels wide are the stars
    private int[][] starPolygon;//A series of points that make the outline of the stars
    //Constructor
    public ComponentStars(){
        numStars=6;
        starValues=new int[]{-5,-3,-1,1,3,5};
    }
    //Stars are drawn in a special way
    protected void drawUnique(Graphics g, TwoPoint p) {
        for(int i=0;i<numStars;i++){
            //The int[][] for coordinates is adjusted to add to the position this star should be drawn at
            TwoPoint center=new TwoPoint((int)(p.getX() + getStarX(i)), p.getY()+starDiameter/2);
            int[][] adjustedStarPolygon=new int[2][10];
            for(int j=0;j<10;j++){
                adjustedStarPolygon[0][j]=starPolygon[0][j]+center.getX();
                adjustedStarPolygon[1][j]=starPolygon[1][j]+center.getY();
            }
            if(amountFilled>0){
                //Filled stars are yellow, unfilled stars are white. Unrated books have nothing filled at all
                if(amountFilled>i){
                    g.setColor(Color.YELLOW);
                }else{
                    g.setColor(Color.WHITE);
                } 
                g.fillPolygon(adjustedStarPolygon[0], adjustedStarPolygon[1], 10);
            }
            g.setColor(BamazonGui.defaultColour);
            //Draw the star's outline
            g.drawPolygon(adjustedStarPolygon[0],adjustedStarPolygon[1],10);
        }
    }
    //Find which star was clicked on
    protected int getStarClicked(int x){
        int closest=-1;
        int dist=10000;
        for(int i=0;i<numStars;i++){
            if(Math.abs(x-getStarX(i))<dist){
                closest=i;
                dist=(int)Math.abs(x-getStarX(i));
            }
        }
        return closest;
    }
    //Set this star's on-click action depending on which star was clicked
    public CustomComponent click(TwoPoint where){
        if(getAction()!=null){
            getAction()[1]=getStarClicked(where.getX());
        }
        return this;
    }
    //trigonometry!
    private int[][] generatePoly(int diameter){
        int[][] star=new int[2][10];
        double outerDist=diameter/2;
        double innerDist = outerDist/(Math.tan(2*Math.PI/5)*Math.sin(Math.PI/5)+Math.cos(Math.PI/5));
        for(int i=0;i<10;i++){
            double dist;
            //Rotate so the star points upwards
            double a= i*Math.PI*1/5-Math.PI/2;
            if(i%2==0){
                //Outer points
                dist=outerDist;
            }else{
                //Inner points
                dist=innerDist;
            }
            star[0][i]=(int)Math.ceil(dist*Math.cos(a));
            star[1][i]=(int)Math.ceil(dist*Math.sin(a));
        }
        return star;
    }
    //Returns the x location of one of the stars
    private float getStarX(int which){
        return (float)which/((float)numStars-1)*((float)getSize().getX()-(float)starDiameter)+starDiameter/2;
    }
    //Sets the visual amount of stars filled based on a rating number
    public void setFilledFromRating(int num){
        if(num==0){
            amountFilled=0;
        }else{
            //This is in case there is a rating number that doesn't match one of the pre-set ones, it chooses the closest star amount
            for(int i=0;i<starValues.length;i++){
                if(num<=starValues[i]){
                    amountFilled=i+1;
                    return;
                }
            }
            amountFilled=starValues[starValues.length-1];
        }
    }
    //getters
    public int getStarDiameter(){
        return starDiameter;
    }
    public int getFilledAmount(){
        return amountFilled;
    }
    public int getRatingValue(){
        if(amountFilled==0){
            return 0;
        }else{
            return starValues[amountFilled-1];
        }
    }
    
    //setters
    public void setFilledAmount(int num){
        amountFilled=num;
    }
    //Sets the diamater and re-generates the star polygon
    public void setStarDiameter(int d){
        if(starDiameter!=d){
            starDiameter=d;
            starPolygon=generatePoly(d);    
        }
    }



}
