// BankAccount class
// This class has instance variables for the account number, password and balance, and methods
// to withdraw, deposit, check balance etc.

// This class contains methods which you need to complete to make the basic ATM work.
// Tutors can help you get this part working in lab sessions. 

// If you choose the ATM for your project, you should make other modifications to 
// the system yourself, based on similar examples we will cover in lectures and labs.
public class BankAccount
{
    public int accNumber = 0;
    public int accPasswd = 0;
    public int balance = 0;
    
    public BankAccount()
    {
    }
    
    public BankAccount(int a, int p, int b)
    {
        accNumber = a;
        accPasswd = p;
        balance = b;
    }
    
    // withdraw money from the account. Return true if successful, or 
    // false if the amount is negative, or less than the amount in the account 
    public boolean withdraw( int amount ) 
    { 
        Debug.trace( "BankAccount::withdraw: amount =" + amount ); 
        if (amount <0){
            return false;
        }
        // CHANGE CODE HERE TO WITHDRAW MONEY FROM THE ACCOUNT
        this.balance -= amount;
        return true;
    }
    
    // deposit the amount of money into the account. Return true if successful,
    // or false if the amount is negative 
    public boolean deposit( int amount )
    { 
        Debug.trace( "LocalBank::deposit: amount = " + amount ); 
        // CHANGE CODE HERE TO DEPOSIT MONEY INTO THE ACCOUNT
        this.balance += amount;
        return true;
    }
    
    public boolean changePassword(int newPasswd)
    {
        if (this.accPasswd == accPasswd){
            this.accPasswd = newPasswd;
            return true;
        } else{
            return false;
        }
    }
        // Return the current balance in the account
    public int getBalance() 
    { 
        Debug.trace( "LocalBank::getBalance" ); 
        // CHANGE CODE HERE TO RETURN THE BALANCE
        return this.balance;
    }
    
    public int getPasswd() 
    { 
        Debug.trace( "LocalBank::getPasswd" ); 

        return this.accPasswd;
    }
    
    public int getAccountNum()
    {
        Debug.trace("LoacalBank::getAccountNum");
        return this.accNumber;
    }
}
