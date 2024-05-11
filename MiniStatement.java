import java.util.ArrayList;


public class MiniStatement
{
    String type;
    int amount;
    public MiniStatement(String t, int a)
    {
        type = t;
        amount = a;
    }
    
    public static String add(MiniStatement l){
        if(AccountLog.size() == 10){
            AccountLog.remove(0);
        }  
            AccountLog.add(l);
        return null;
    }
    
    
    public static ArrayList<MiniStatement> AccountLog = new ArrayList<MiniStatement>();
    
    public String getType(){
        Debug.trace("LocalBank: Addtype");
        return this.type;
    }
    
    public int getAmount(){
        Debug.trace("LOCALBANK: ADDamount");
        return this.amount;
    }
    
    public static String getLog(){
        String log = "";
        for(MiniStatement l : AccountLog){
            log += l.getType() + "" + l.getAmount() + "\n";
        }
        return log;
    }
}
