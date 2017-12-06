package practice.ele_sms_eshan.db;

/**
 * Created by Eshan on 12/5/17.
 */

public class BankTrans
{

    public String id;
    public String bankName;
    public String accountNumber;
    public String transAmount;
    public String transTime;
    public String receiveTime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getBankName()
    {
        return bankName;
    }

    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getTransAmount()
    {
        return transAmount;
    }

    public void setTransAmount(String transAmount)
    {
        this.transAmount = transAmount;
    }

    public String getTransTime()
    {
        return transTime;
    }

    public void setTransTime(String transTime)
    {
        this.transTime = transTime;
    }

    public String getReceiveTime()
    {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime)
    {
        this.receiveTime = receiveTime;
    }
}
