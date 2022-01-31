import java.awt.*;

//This panel displays help text to the user
public class HelpPanel extends Panel{
    private String[] message;
    public HelpPanel(String[] text){
        this.message=text;
    }
    public void paint(Graphics g) {
        //This font is capable of displaying the characters in the message
        g.setFont(new Font("Dialog.plain",0,14));
        for(int i=0;i<message.length;i++){
            g.drawString(message[i],5,i*20+17);
        }
    }
}
