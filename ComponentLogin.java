import java.awt.Color;

//This component is for the entire login and registration screen
public class ComponentLogin extends CustomComponent
{
    //Main components
    private ComponentImage imgLabel;
    private CustomComponent loginContainer;
    private CustomComponent registerContainer;
    //Login components
    private CustomComponent txtName;
    private CustomComponent txtPass;
    private CustomComponent btnLogin;
    private CustomComponent lblLogin;
    private CustomComponent btnSwitchToRegister;
    //Registration components
    private CustomComponent btnSwitchToLogin;
    private CustomComponent txtRegName;
    private CustomComponent txtRegPass;
    private CustomComponent txtRegPassConfirm;
    private CustomComponent lblReg;
    private CustomComponent btnCreateAcc;

    public ComponentLogin(){

    }

    //This cannot be in the constructor because it and the main menu screen have to reference each other for their buttons to perform their actions
    //So they must be initialized first before their components can be created
    public void generate(CustomComponent mainMenu)
    {
        //add image first so it does not overlap the text boxes
        imgLabel=new ComponentImage();
        imgLabel.setImage(BamazonGui.loadImage("files\\Bamazon_logo2.png"));
        getComponents().add(imgLabel);
                
        loginContainer=new CustomComponent();
        getComponents().add(loginContainer);

        registerContainer=new CustomComponent();
        getComponents().add(registerContainer);
        registerContainer.setVisible(false);

        txtName=new CustomComponent();
        txtName.setTypeable(true);
        txtName.setFakeText("Enter Username");
        txtName.setText("");
        txtName.setFill(java.awt.Color.WHITE);
        txtName.setAction(new int[]{4});
        loginContainer.getComponents().add(txtName);

        txtPass=new CustomComponent();
        txtPass.setTypeable(true);
        txtPass.setFakeText("Enter Password");
        txtPass.setText("");
        txtPass.setPasswordMode(true);
        txtPass.setFill(java.awt.Color.WHITE);
        txtPass.setAction(new int[]{3});
        txtPass.setResetText(true);
        loginContainer.getComponents().add(txtPass);

        txtName.setTarget(new CustomComponent[]{ txtPass});

        lblLogin=new CustomComponent();
        lblLogin.setText("");
        lblLogin.setResetText(true);
        lblLogin.setWordWrap(true);
        loginContainer.getComponents().add(lblLogin);

        btnLogin=new CustomComponent();
        btnLogin.setText("Log in");
        btnLogin.setTarget(new CustomComponent[]{ mainMenu,txtName,txtPass,lblLogin,this});
        btnLogin.setAction(new int[]{0});
        btnLogin.setFill(BamazonGui.bamazonOrange);
        loginContainer.getComponents().add(btnLogin);

        txtPass.setTarget(new CustomComponent[]{btnLogin});

        btnSwitchToLogin=new CustomComponent();
        btnSwitchToLogin.setText("Already have an account? Log in");
        btnSwitchToLogin.setAction(new int[]{1});
        btnSwitchToLogin.setTarget(new CustomComponent[]{loginContainer,registerContainer});
        btnSwitchToLogin.setFill(new Color(220,220,220));
        registerContainer.getComponents().add(btnSwitchToLogin);

        btnSwitchToRegister=new CustomComponent();
        btnSwitchToRegister.setText("Register now");
        btnSwitchToRegister.setTarget(new CustomComponent[]{loginContainer,registerContainer});
        btnSwitchToRegister.setAction(new int[]{1});
        loginContainer.getComponents().add(btnSwitchToRegister);
        btnSwitchToRegister.setFill(btnSwitchToLogin.getFill());

        txtRegName=new CustomComponent();
        txtRegName.setTypeable(true);
        txtRegName.setFakeText("Enter Username");
        txtRegName.setText("");
        txtRegName.setFill(java.awt.Color.WHITE);
        txtRegName.setAction(new int[]{4});
        txtRegName.setResetText(true);
        registerContainer.getComponents().add(txtRegName);

        txtRegPass=new CustomComponent();
        txtRegPass.setTypeable(true);
        txtRegPass.setFakeText("Enter Password");
        txtRegPass.setText("");
        txtRegPass.setFill(java.awt.Color.WHITE);
        txtRegPass.setAction(new int[]{4});
        txtRegPass.setResetText(true);
        txtRegPass.setPasswordMode(true);
        registerContainer.getComponents().add(txtRegPass);

        txtRegName.setTarget(new CustomComponent[]{ txtRegPass});

        txtRegPassConfirm=new CustomComponent();
        txtRegPassConfirm.setTypeable(true);
        txtRegPassConfirm.setFakeText("Confirm Password");
        txtRegPassConfirm.setText("");
        txtRegPassConfirm.setFill(java.awt.Color.WHITE);
        txtRegPassConfirm.setAction(new int[]{3});
        txtRegPassConfirm.setResetText(true);
        txtRegPassConfirm.setPasswordMode(true);
        registerContainer.getComponents().add(txtRegPassConfirm);

        txtRegPass.setTarget(new CustomComponent[]{ txtRegPassConfirm});

        lblReg=new CustomComponent();
        lblReg.setText("");
        lblReg.setResetText(true);
        registerContainer.getComponents().add(lblReg);

        btnCreateAcc=new CustomComponent();
        btnCreateAcc.setAction(new int[]{7});
        btnCreateAcc.setText("Create Account");
        btnCreateAcc.setTarget(new CustomComponent[]{txtRegName,txtRegPass,txtRegPassConfirm,lblReg});//Creates an account and updates the label to tell the user
        btnCreateAcc.setFill(btnLogin.getFill());
        registerContainer.getComponents().add(btnCreateAcc);

        txtRegPassConfirm.setTarget(new CustomComponent[]{btnCreateAcc});//press enter to simulate clicking "create account" button (for lazy people)
    }
    //Rearrange contained components
    protected void rearrangeUnique(){
        int middleX=getSize().getX()/2;
        int middleY=getSize().getY()/2;
        //login
        loginContainer.setSize(getSize());
        imgLabel.setSize(new TwoPoint(3216/10,1352/10));
        imgLabel.setLocation(new TwoPoint(middleX-imgLabel.getSize().getX()/2,middleY-120));
        txtName.setSize(new TwoPoint(100,20));
        txtName.setLocation(new TwoPoint(middleX-txtName.getSize().getX()/2, middleY+5));
        txtPass.setSize(new TwoPoint(100,20));
        txtPass.setLocation(new TwoPoint(middleX-txtPass.getSize().getX()/2, middleY+30));
        btnLogin.setSize(new TwoPoint(40,20));
        btnLogin.setLocation(new TwoPoint(middleX-btnLogin.getSize().getX()/2,middleY+55));
        lblLogin.setSize(new TwoPoint(220,20));
        lblLogin.setLocation(new TwoPoint(middleX-lblLogin.getSize().getX()/2,middleY+80));
        btnSwitchToRegister.setSize(new TwoPoint(80,20));
        btnSwitchToRegister.setLocation(getSize().subtract(btnSwitchToRegister.getSize()).subtract(new TwoPoint(5,5)));
        //register
        registerContainer.setSize(getSize());
        btnSwitchToLogin.setSize(new TwoPoint(180,20));
        btnSwitchToLogin.setLocation(getSize().subtract(btnSwitchToLogin.getSize()).subtract(new TwoPoint(5,5)));
        txtRegName.setSize(new TwoPoint(110,20));
        txtRegName.setLocation(new TwoPoint(middleX-txtRegName.getSize().getX()/2, middleY+5));
        txtRegPass.setSize(new TwoPoint(110,20));
        txtRegPass.setLocation(new TwoPoint(middleX-txtRegPass.getSize().getX()/2, middleY+30));
        txtRegPassConfirm.setSize(new TwoPoint(110,20));
        txtRegPassConfirm.setLocation(new TwoPoint(middleX-txtRegPassConfirm.getSize().getX()/2, middleY+55));
        btnCreateAcc.setSize(new TwoPoint(90,20));
        btnCreateAcc.setLocation(new TwoPoint(middleX-btnCreateAcc.getSize().getX()/2, middleY+80));
        lblReg.setSize(new TwoPoint(300,20));
        lblReg.setLocation(new TwoPoint(middleX-lblReg.getSize().getX()/2,middleY+105));
    }


}