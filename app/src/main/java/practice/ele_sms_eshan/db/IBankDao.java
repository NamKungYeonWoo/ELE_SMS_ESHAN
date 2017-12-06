package practice.ele_sms_eshan.db;

import java.util.List;

/**
 * Created by Eshan on 12/5/17.
 */

public interface IBankDao
{

    public BankTrans fetchBankById(int bankTransId);
    public List<BankTrans> fetchAllBanks();
    // add user
    public boolean addBank(BankTrans bank);
    // add users in bulk
    public boolean addBanks(List<BankTrans> banks);
    public boolean deleteAllBanks();
}
