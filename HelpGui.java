import java.awt.*;
public class HelpGui extends Frame{
    private HelpPanel p;
    private Color back;

    //This is just a simple window to display help to the user
    public HelpGui(Color backColour){
        super("Bamazon™ Customer Service");
        addWindowListener(new java.awt.event.WindowAdapter() {    
            public void windowClosing (java.awt.event.WindowEvent e) {    
                dispose();    
            }    
        });
        this.back=backColour;
        setBackground(backColour);
        String[] helpText=new String[]{
        "Welcome to the Bamazon™ Electronic Book Catalogue Customer Service MenuⓇ. ",
        "If you need help with viewing the Bamazon™ Electronic Book CatalogueⓇ, follow these simple step-by-step instructions:",
        "",
        "1. If you don't have a Bamazon™ AccountⓇ, click the Register ButtonⓇ in the bottom right to create a Bamazon™ Account™ ",
        "       Choose a unique username, and make sure your password is at least 3 characters long for maximumⓇ™ security.",
        "2. Once you have an account, return to the Bamazon™ Login ScreenⓇ, enter your username and password, and click the Login Button™",
        "       When logging in for the first time, it may take a moment for the Bamazon™ to load the BooksⓇ",
        "3. Once you are in the Main Menu™, you can scroll or search to browse the list of books",
        "       Note the size of the Bamazon™ Electronic Book CatalogueⓇ window can be adjusted for enjoyable viewing",
        "       Click on a book to select it, which allows you to view additional information and rate it",
        "       Click on a the stars and rate every book you have alread read, as it will inform Bamazon™ of your reading preferences",
        "       Whenever you rate a book its ranking will change depending on its average rating, and you can even affect other users' recommendations!",
        "       If you rate something by accident, simply click on the same star again to remove your rating",
        "       Don't forget to type in the search bar, and click the magnifying glass icon to narrow down your results",
        "4. After rating some books, you can switch to the Browse TabⓇ to view your Bamazon™ curated book recommendation list",
        "       Feel free to switch between the tabs and rate more books to improve your recommendations",
        "5. When you are done using the Bamazon™ Electronic Book CatalogueⓇ, you can click the Sign Out Button™ to log out of your account",
        "",
        "Thank you for using Bamazon™ software!",
        "",
        "©2021-2022, Bamazon Inc."};

        //Create a panel to display the text
        p=new HelpPanel(helpText);
        add(p);
        setSize(900,470);
        setVisible(true);
    }
}
