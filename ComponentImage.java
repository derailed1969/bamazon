
import java.awt.*;

//This class is just a CustomComponent that supports drawing images
public class ComponentImage extends CustomComponent{
    private Image image;
    private int drawMode=0;//Modes: 0: fit to fill the component's rectangle, 1: center the image in the component's rectangle, 2:center at the top


    protected void drawUnique(Graphics g, TwoPoint p){
        drawImage(g, p,drawMode);
    }
    //Draws the image in a different positioning depending on the drawMode
    protected void drawImage(Graphics g, TwoPoint p, int mode){
        switch(mode){
            case 0:
                g.drawImage(getImage(), p.getX(), p.getY(), getSize().getX(), getSize().getY(),null);
                break;
            case 1:
                g.drawImage(getImage(), p.getX()+getSize().getX()/2 - getImage().getWidth(null)/2, p.getY()+getSize().getY()/2 - getImage().getHeight(null)/2,null);
                break;
        }
    }
    //Getters
    public Image getImage(){
        return image;
    }
    public int getDrawScaled(){
        return drawMode;
    }
    //Setters
    public void setDrawMode(int b){
        drawMode=b;
    }
    public void setImage(Image i){
        image=i;
    }

}
