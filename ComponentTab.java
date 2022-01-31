import java.awt.*;

//This class is just a component to switch between the browse books tab and the recommend books tab
public class ComponentTab extends CustomComponent{
    private Color fillUnselected;
    protected void drawUnique(Graphics g, TwoPoint location){
        //Just draws a box without the bottom part if its tab is currently visible
        g.drawLine(location.getX(), location.getY(), location.getX()+getSize().getX(), location.getY());
        g.drawLine(location.getX(), location.getY(), location.getX(), location.getY()+getSize().getY()-1);
        g.drawLine(location.getX()+getSize().getX(), location.getY(), location.getX()+getSize().getX(),location.getY()+getSize().getY()-1);

        if(getTarget()[0].getVisible()){
            g.setColor(getFill());
        }else{
            g.setColor(fillUnselected);
        }
        g.fillRect(location.getX(), location.getY(), getSize().getX(), getSize().getY());
        //If its tab isn't currently visibly, the bottom side is drawn too
        if(!getTarget()[0].getVisible()){
            g.setColor(getColour());
            g.drawLine(location.getX(),location.getY()+getSize().getY()-1,location.getX()+getSize().getX(),location.getY()+getSize().getY()-1);
        }
        drawText(g, location);
    }

    //This is because the tab is filled with a different colour depending on whether its tab is currently visible or not
    public void setFillTwo(Color f){
        fillUnselected=f;
    }
}
