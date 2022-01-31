import java.util.ArrayList;
import java.awt.*;

//This class is the base of all GUI components in the program, it can contain other CustomComponents and uses recursive methods to perform most actions
public class CustomComponent {
    private TwoPoint location;//Relative to its container component
    private TwoPoint size;
    private Color colour;//Outline colour
    private Color fill=null;//Fill colour (null means it isn't filled in)
    private Color textColour;
    private String text;
    private String fakeText;//This will display if text does not contain any characters
    private java.awt.Font font;
    private ArrayList<CustomComponent> components;//These components are located inside this one
    private boolean visible=true;
    private boolean canType=false;//This allows the text to be edited like a textbox, it isn't a separate class because I wanted all inherited classes to have the ability to be typed in
    private boolean passwordMode=false;//Censors characters into ******
    private int animationTicks=0;//Can be used for animations like a textbox's typing cursor's blinking
    private boolean resetText=false;//When true this component's text is resetted when it is hidden
    private boolean wordWrap;
    private boolean autoFull;//Automatically takes up the entire space of container when rearranged
    private int[] onClickAction;//This has an ID which is read by BamazonPanel to perform a certain task when clicked
    private CustomComponent[] actionTarget;//These components will be affected by the onClickAction
    private boolean selected=false;//A component is selected when clicked on, and enables special features/drawing

    //Constructor
    public CustomComponent(){
        location=new TwoPoint(0,0);
        font = BamazonGui.defaultFont;
        colour=BamazonGui.defaultColour;
        components=new ArrayList<CustomComponent>();
    }

    //Recursively find which component got clicked on (can be overwritten by derived classes)
    public CustomComponent click(TwoPoint where){
        int find=whichComponent(where);
        if(find>=0){
            return(components.get(find).click(where.subtract(components.get(find).location)));
        }
        return this;
    }
    //Recursively find which component got released (can be overwritten by derived classes)
    public CustomComponent release(TwoPoint where)
    {
        int find=whichComponent(where);
        if(find>=0){
            return(components.get(find).click(where.subtract(components.get(find).location)));
        }
        return this;
    }
    //Recursively find which component got scrolled on (can be overwritten by derived classes)
    public void scroll(int amount, TwoPoint where){
        int find=whichComponent(where);
        if(find>=0){
            components.get(find).scroll(amount,where.subtract(components.get(find).location));
        }
    }
    //This is my version of layout managers, it can recursively rearrange components in a custom way when they are resized
    public void rearrange(){
        rearrangeUnique();
        for(int i=0;i<components.size();i++){
            if(components.get(i).autoFull){
                components.get(i).setSize(size);
                components.get(i).setLocation(new TwoPoint(0,0));
            }
            components.get(i).rearrange();
        }
    }
    //This method is overwritten by derived classes to rearrange in a special way
    protected void rearrangeUnique(){
    }

    //This method is also recursive, it is called when a component is hidden (for example, to remove a user's data after they log out)
    public void reset(){
        resetUnique();
        for(int i=0;i<components.size();i++){
            components.get(i).reset();
        }
    }
    //Also overwritten by derived classes
    public void resetUnique(){
        if(resetText){
            text="";
        }
    }

    //Finds which component got clicked out of the ones this component contains, or returns -1 (meaning itself) if there isn't any component located there 
    private int whichComponent(TwoPoint where){
        for(int i=components.size()-1;i>=0;i--){
            if(components.get(i).visible){
                if(where.inRectangle(components.get(i).location, components.get(i).size)){
                    //not visible components can't get selected, so nothing can be clicked during animation when both sides are set to invis.
                    return i;
                }
            }
        }
        return -1;
    }
    //Another recursive method to draw this component and all the components it contains, and all the components those components contain, etc.
    public void draw(Graphics g, TwoPoint p){
        drawUnique(g,p);
        for(int i=0;i<components.size();i++){
            if(components.get(i).getVisible()){
                components.get(i).draw(g, p.add(components.get(i).location));
            }
        }
    }
    //Also gets overwritten by derived classes. By default it draws the components text, a rectangle if it is clickable, and fills it in if the fill is set.
    protected void drawUnique(Graphics g, TwoPoint p){
        if(fill!=null){
            g.setColor(fill);
            fillRect(g, p);
        }
        drawText(g,p);
        g.setColor(colour);
        if(selected&&canType){
            g.setColor(BamazonGui.selectedColour);
        }
        if((onClickAction!=null)||canType){
            drawRect(g,p);
        }
    }
    protected void fillRect(Graphics g, TwoPoint p){
        g.fillRect(p.getX(),p.getY(),size.getX(),size.getY());
    }
    protected void drawRect(Graphics g, TwoPoint p){
        g.drawRect(p.getX(),p.getY(),size.getX(),size.getY());//buttons get a box
    }
    //This method draws this components text using graphics
    protected void drawText(Graphics g, TwoPoint p){
        if(text!=null || fakeText!=null){
            if(textColour!=null){
                g.setColor(textColour);
            }else{
                g.setColor(colour);
            }
            g.setFont(font);
            int textHeight =g.getFontMetrics().getHeight();
            String toDraw=text;
            if(passwordMode){
                toDraw="";
                for(int i=0;i<text.length();i++){
                    toDraw+='*';
                }
            }
            if(canType&&selected){
                if(animationTicks<=10){
                    toDraw+="|";
                }
                if(animationTicks>20){
                    animationTicks=0;
                }
                animationTicks++;
            }else{
                if(getTypeable()){
                    animationTicks=0;
                }
                if(toDraw.equals("") && fakeText!=null){
                    g.setColor(BamazonGui.fakeColour);
                    toDraw=fakeText;
                }
            }
            int i=0;
            if(wordWrap){
                drawTextWrapped(g, p);
            }else{
                //Cut off the text fi it is too long for this component, and put "..." instead
                while(g.getFontMetrics().stringWidth(toDraw)>size.getX()){
                    if(canType){
                        toDraw=toDraw.substring(0,toDraw.length()-1);
                    }else if(fakeText==null || fakeText.equals("")){
                        toDraw=text.substring(0, text.length()-i )+"...";
                        i++;
                    }
                }    
                g.drawString(toDraw, p.getX()+2,p.getY()+textHeight);
            }
        }
    }
    //This method draws the component's text onto multiple lines, and returns how many lines tall the finished drawing is
    protected int drawTextWrapped(Graphics g, TwoPoint p){
        String[] words = text.split(" ");
        int lines=0;
        String toDraw=words[0];
        int i=1;
        java.awt.font.FontRenderContext frc=new java.awt.font.FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        int textHeight=(int)font.getLineMetrics(toDraw, frc).getHeight();
        do{
            if(i==words.length||font.getStringBounds(toDraw + " "+words[i], frc).getWidth()>size.getX()){
                if(p != null){
                    g.drawString(toDraw,p.getX()+2, p.getY()+textHeight*(1+lines));
                }
                lines++;
                if(i<words.length){
                    toDraw=words[i];
                }
            }else{
                toDraw+= " " +words[i];
            }
            i++;
        }while(i<=words.length);
        return textHeight*lines;
    }

    //Getters
    public TwoPoint getSize(){
        return size;
    }
    public TwoPoint getLocation(){
        return location;
    }
    public java.awt.Font getFont(){
        return font;
    }
    public ArrayList<CustomComponent> getComponents(){
        return components;
    }
    public Boolean getVisible(){
        return visible;
    }
    public Color getColour(){
        return colour;
    }
    public Boolean getTypeable(){
        return canType;
    }
    public String getText(){
        return text;
    }
    public int[] getAction(){
        return onClickAction;
    }
    public CustomComponent[] getTarget(){
        return actionTarget;
    }
    public boolean getSelected(){
        return selected;
    }
    public String getFakeText(){
        return fakeText;
    }
    public Color getFill(){
        return fill;
    }
    public boolean getAutoFull(){
        return autoFull;
    }

    //Setters
    public void setSelected(boolean b){
        selected=b;
    }
    public void setVisible(boolean b){
        visible=b;
        //reset this components(and all contained components) when hidden 
        if(b==false){
            reset();
        }
    }
    public void setSize(TwoPoint newSize){
        size=newSize;
    }
    public void setLocation(TwoPoint newLocation){
        location=newLocation;
    }
    public void setColour(Color c){
        colour=c;
    }
    public void setTypeable(boolean b){
        canType=b;
    }
    public void setFont(Font f){
        this.font=f;
    }
    public void setText(String s){
        this.text=s;
    }
    public void setAction(int[] t){
        onClickAction=t;
    }
    public void setTarget(CustomComponent[] target){
        this.actionTarget=target;
    }
    public void setFakeText(String f){
        fakeText=f;
    }
    public void setPasswordMode(Boolean mode){
        passwordMode=mode;
    }
    public void setFill(Color f){
        fill=f;
    }
    public void setTextColour(Color t){
        textColour=t;
    }
    public void setResetText(boolean b){
        resetText=b;
    }
    public void setWordWrap(boolean b){
        wordWrap=b;
    }
    public void setAutoFull(boolean b){
        autoFull=b;
    }
    //Can be used for animation
    public static Color darkenColour(Color c, float percentage){
        float r=c.getRed();
        float g=c.getGreen();
        float b=c.getBlue();
        r*=(1.00f-percentage/100f);
        g*=(1.00f-percentage/100f);
        b*=(1.00f-percentage/100f);
        r=Math.max(0, Math.min(r,255));
        g=Math.max(0, Math.min(g,255));
        b=Math.max(0, Math.min(b,255));
        return new Color((int)r,(int)g,(int)b);
    }
}
