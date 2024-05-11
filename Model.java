
// The model represents all the actual content and functionality of the app
// For the ATM, it keeps track of the information shown in the display
// (the title and two message boxes), and the interaction with the bank, executes
// commands provided by the controller and tells the view to update when
// something changes
public class Model 
{
    // the ATM model is always in one of three states - waiting for an account number, 
    // waiting for a password, or logged in and processing account requests. 
    // We use string values to represent each state:
    // (the word 'final' tells Java we won't ever change the values of these variables)
    final String ACCOUNT_NO = "account_no";
    final String PASSWORD = "password";
    final String LOGGED_IN = "logged_in";
    final String PASSCHANGE = "change_passwd";
    final String VERIFY_OLD_PASS = "verify_pass";
    final String NEWPASSWD = "passwd_changed";
    final String WITHDRAW1 = "Withdraw1";
    final String WITHDRAW2 = "Withdraw2";
    final String DEPOSIT = "Deposit";
    // variables representing the ATM model
    final String denieState = "";
    String state = ACCOUNT_NO;      // the state it is currently in
    int  number = 0;                // current number displayed in GUI (as a number, not a string)
    Bank  bank = null;              // The ATM talks to a bank, represented by the Bank object.
    int accNumber = -1;             // Account number typed in
    int accPasswd = -1;             // Password typed in
    // These three are what are shown on the View display
    String title = "Bank ATM";      // The contents of the title message
    
    String display1 = null;         // The contents of the Message 1 box (a single line)
    String display2 = null;         // The contents of the Message 2 box (may be multiple lines)

    // The other parts of the model-view-controller setup
    public View view;
    public Controller controller;

    // Model constructor - we pass it a Bank object representing the bank we want to talk to
    public Model(Bank b)
    {
        Debug.trace("Model::<constructor>");          
        bank = b;
    }

    // Initialising the ATM (or resetting after an error or logout)
    // set state to ACCOUNT_NO, number to zero, and display message 
    // provided as argument and standard instruction message
    public void initialise(String message) {
        setState(ACCOUNT_NO);
        number = 0;
        display1 = message; 
        display2 =  "Enter your account number\n" +
        "Followed by \"Ent\"";
    }

    // use this method to change state - mainly so we print a debugging message whenever 
    //the state changes
    public void setState(String newState) 
    {
        if ( !state.equals(newState) ) 
        {
            String oldState = state;
            state = newState;
            Debug.trace("Model::setState: changed state from "+ oldState + " to " + newState);
        }
    }

    // These methods are called by the Controller to change the Model
    // when particular buttons are pressed on the GUI
    
    // process a number key (the key is specified by the label argument)
    public void processNumber(String label)
    {
        // a little magic to turn the first char of the label into an int
        // and update the number variable with it
        char c = label.charAt(0);
        number = number * 10 + c-'0';           // Build number 
        // show the new number in the display
        display1 = "" + number;
        display();  // update the GUI
    }

    // process the Clear button - reset the number (and number display string)
    public void processClear()
    {
        // clear the number stored in the model
        number = 0;
        display1 = "";
        display();  // update the GUI
    }

    // process the Enter button
    // this is the most complex operation - the Enter key causes the ATM to change state
    // from account number, to password, to logged_in and back to account number
    // (when you log out)
    public void processEnter()
    {
        // Enter was pressed - what we do depends what state the ATM is already in
        switch ( state )
        {
            case ACCOUNT_NO:
                // we were waiting for a complete account number - save the number we have
                // reset the tyed in number to 0 and change to the state where we are expecting 
                // a password
                accNumber = number;
                number = 0;
                setState(PASSWORD);
                display1 = "";
                display2 = "Now enter your password\n" +
                "Followed by \"Ent\"";
                break;
            case PASSWORD:
                // we were waiting for a password - save the number we have as the password
                // and then cotnact the bank with accumber and accPasswd to try and login to
                // an account
                accPasswd = number;
                number = 0;
                display1 = "";
                // now check the account/password combination. If it's ok go into the LOGGED_IN
                // state, otherwise go back to the start (by re-initialsing)
                if ( bank.login(accNumber, accPasswd) )
                {
                    setState(LOGGED_IN);
                    display2 = "Accepted\n" +
                    "Now enter the transaction you require";
                } else {
                    initialise("Unknown account/password");
                }
                break;
            case LOGGED_IN:
                display2 = "Now enter the transaction you require";
                break;
            case VERIFY_OLD_PASS:
                display2 = "Verify your current password.\nPress \"Ent\" to continue";
                Debug.trace("model::processChangePassword: confirmed");
                if (number==accPasswd){
                    Debug.trace("model::VERIFY_OLD_PASS: Old pass verified");
                    display1 = "";
                    display2 = "Enter your new password.\n" +
                    "Followed by \"Ent\"";
                    number = 0;
                    setState(NEWPASSWD);
                }
                
                break;
            case NEWPASSWD:
                display1 = "";
                accPasswd += number;
                bank.changePassword(number);
                Debug.trace("model::processChangePassword:: Account: " + bank.getAccountNum() + " New password: " + bank.getPasswd());
                display2 = "Your new Password is \""+ bank.getPasswd() + "\"\nPress \"Ent\" to continue.";
                setState(LOGGED_IN);
                break;
            case WITHDRAW1:
                Debug.trace("model::Withdraw CheckBalance: Confirmed");
                processCheckBalance();
                break;
            case WITHDRAW2:
                processWithdraw2();
                break;
            case DEPOSIT:
                Debug.trace("model::processDeposit: Account: £" + bank.getAccountNum() + " Deposited: £" + number + " Balance: £" + bank.getBalance());
                bank.deposit( number );
                display1 = "";
                display2 = "Deposited: " + number + "\nPress \"Ent\" to continue";
                number = 0;            
                default:
                // do nothing in any other state (ie logged in)
        }  
        display();  // update the GUI
    }

    // Withdraw button - check we are logged in and if so try and withdraw some money from
    // the bank (number is the amount showing in the interface display)
    public void processWithdraw1()
    {
        if (state.equals(LOGGED_IN)){
            setState(WITHDRAW1);
            Debug.trace("model::processWithdraw1");
            display2 = "Do you want to check your balance? \nConfirm\"ent\"\nCancel \"No\""; 
        }
        display();// update the GUI 
        }
    
    public void processWithdraw2(){
        if(state.equals(WITHDRAW2)){
            if ( bank.withdraw( number ) )
            {
                display2 =   "Withdrawn: " + number + "\nPress \"Ent\" to continue";
            } else {
                display2 =   "You do not have sufficient funds";
            }
        }
        Debug.trace("model::processWithdraw: Account: " + bank.getAccountNum() + " Withdrew: " + number + " New balance: " + bank.getBalance());
        setState(LOGGED_IN);
        number = 0;
        display1 = "";           
        display();  // update the GUI
    }
    
    
    
    
    public void processDenie() {
        switch ( state ){
            case WITHDRAW1:
                Debug.trace("model::Withdraw checkBalance: Denied");
                number = 0;
                display2 = "Enter withdraw amount\nPress \"Ent\" to continue.";
                setState(WITHDRAW2);
                break;
            case VERIFY_OLD_PASS:
                setState(LOGGED_IN);
                break;
            
            
        }
        display();    
        }
        
    public void processBalance()
    {
        if (state.equals(LOGGED_IN) ) {
            number = 0;
            display2 = "Your balance is: " + bank.getBalance() + "\nTo continue press \"Ent\"";
            Debug.trace("model::processBalance");
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }
    
        
        
        
    // Deposit button - check we are logged in and if so try and deposit some money into
    // the bank (number is the amount showing in the interface display)
    public void processDeposit() 
    {
            if (state.equals(LOGGED_IN)) {
            
            Debug.trace("model::processDeposit");
            display2 = "Enter your deposit amount\nPress \"Ent\" to continue";
            setState(DEPOSIT);
            display();  // update the GUI
        }
    }
    
    
    // Balance button - check we are logged in and if so access the current balance
    // and display it
    public void processCheckBalance()
    {
            if ( state.equals(WITHDRAW1) ){
                Debug.trace("model::Withdraw processCheckBalance: Confirmed");
                display1 = "";
                display2 = "Your balance is: " + bank.getBalance() + "\n" +
                "Input the amount to withdraw\nTo continue press\"ent\"";
                setState(WITHDRAW2);
        } else{
                initialise("You are not logged in");
            }
        }
    // Finish button - check we are logged in and if so log out
    public void processFinish()
    {
        if (state.equals(LOGGED_IN) ) {
            // go back to the log in state
            setState(ACCOUNT_NO);
            number = 0;
            display2 = "Welcome: Enter your account number";
            bank.logout();
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }
    
    public void processChangePassword( int newPasswd)
    {
        if (state.equals(LOGGED_IN) ){
            Debug.trace("model:: ProcessChangePassword");
            display2 = "Do you want to change your password?\nPress \"Ent\" to continue\nPress \"No\" to stop."; 
            setState(VERIFY_OLD_PASS);
            number = 0;
           
        }else {
            initialise("You are not logged in");
        }
        if(number==(accPasswd)){
            setState(NEWPASSWD);
            number = 0;
            display2 = "please enter your new Password.";
            accPasswd = number;
            }
        display();
    }
    
    // Any other key results in an error message and a reset of the GUI
    public void processUnknownKey(String action)
    {
        // unknown button, or invalid for this state - reset everything
        Debug.trace("Model::processUnknownKey: unknown button \"" + action + "\", re-initialising");
        // go back to initial state
        initialise("Invalid command");
        display();
    }

    // This is where the Model talks to the View, by calling the View's update method
    // The view will call back to the model to get new information to display on the screen
    public void display()
    {
        Debug.trace("Model::display");
        view.update();
    }
}
