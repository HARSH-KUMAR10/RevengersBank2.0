package bankserver.repo;

import model.Account;
import model.Utilities;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class Accounts implements Operation
{

    private static Accounts accountsObj = null;

    public static ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    private Accounts()
    {
    }

    public static synchronized Accounts getInstance()
    {
        if (accountsObj == null)
        {
            accountsObj = new Accounts();
        }
        return accountsObj;
    }

    private static String getDate(){
        Date date = new Date();
        return date.getDate()+"-"+(date.getMonth()+1)+"-"+(date.getYear()+1900);
    }

    // Create new account
    @Override
    public String create(String... values)
    {

        Account account = new Account(values[0], Integer.parseInt(values[1]), values[2],
                values[3], values[4], Double.parseDouble(values[5]), accounts.size());

        return accounts.putIfAbsent(account.getEmail(), account) == null ?
                Utilities.Messages.ACCOUNT_CREATED : Utilities.Messages.ALREADY_CREATED;

    }

    // Login to account
    @Override
    public Account read(String... values)
    {
        Account account = accounts.get(values[0]);

        if (account != null && account.getPin().equals(values[1]))
        {
            return account;
        }
        else
        {
            return null;
        }
    }

    // deposit and withdrawal amount from account
    @Override
    public String update(String... values)
    {
        Account account = accounts.get(values[1]);

        if (account.getSessionId().equalsIgnoreCase(values[0]))
        {
            double amountToAdd = Double.parseDouble(values[2]);
            if (Boolean.parseBoolean(values[3]))
            {
                account.setBalance(account.getBalance() + amountToAdd);

                Transactions.getInstance().create(account.getEmail(),getDate()
                        ,"Amount deposit","",amountToAdd+"",account.getBalance()+"");

                return Utilities.Messages.DEPOSIT_SUCCESSFUL_BALANCE + account.getBalance();
            }
            else
            {
                if (account.getBalance() >= amountToAdd)
                {
                    account.setBalance(account.getBalance() - Double.parseDouble(values[2]));

                    Transactions.getInstance().create(account.getEmail(),getDate()
                            ,"Amount withdraw",amountToAdd+"","",account.getBalance()+"");

                    return Utilities.Messages.WITHDRAW_SUCCESSFUL_BALANCE + account.getBalance();
                }
                else
                {
                    return Utilities.Messages.INSUFFICIENT_BALANCE;
                }
            }
        }
        else
        {
            return Utilities.Messages.AUTH_FAIL;
        }
    }

    @Override
    public Object delete(String... args)
    {
        return null;
    }


    // Get account details
    public String read(String userSessionId, String userSessionEmail, boolean allDetails)
    {
        Account account = accounts.get(userSessionEmail);

        if (account.getSessionId().equalsIgnoreCase(userSessionId) && allDetails)
        {
            return Utilities.Messages.ACCOUNT_DETAILS + Utilities.Delimiters.NEW_LINE
                   + Utilities.Messages.ACC_NO + Utilities.Delimiters.COLON_DELIMITER
                   + Utilities.Delimiters.WHITE_SPACE + account.getAccountNumber()
                   + Utilities.Delimiters.NEW_LINE + Utilities.Keyword.NAME + Utilities.Delimiters.COLON_DELIMITER
                   + Utilities.Delimiters.WHITE_SPACE + account.getName()
                   + Utilities.Delimiters.NEW_LINE + Utilities.Keyword.EMAIL + Utilities.Delimiters.COLON_DELIMITER
                   + Utilities.Delimiters.WHITE_SPACE + account.getEmail()
                   + Utilities.Delimiters.NEW_LINE + Utilities.Keyword.AGE + Utilities.Delimiters.COLON_DELIMITER
                   + Utilities.Delimiters.WHITE_SPACE + account.getAge()
                   + Utilities.Delimiters.NEW_LINE + Utilities.Keyword.GENDER + Utilities.Delimiters.COLON_DELIMITER
                   + Utilities.Delimiters.WHITE_SPACE + account.getGender()
                   + Utilities.Delimiters.NEW_LINE + Utilities.Keyword.BALANCE + Utilities.Delimiters.COLON_DELIMITER
                   + Utilities.Delimiters.WHITE_SPACE + account.getBalance()
                   + Utilities.Delimiters.NEW_LINE + Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER;
        }
        else
        {
            return Utilities.Messages.AUTH_FAIL + Utilities.Delimiters.NEW_LINE
                   + Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER;
        }

    }

    // Transfer from one account to another
    public static String update(String userSessionId, String userSessionEmail,
                                String receiverAccountNumber, String receiverEmail, String amount)
    {
        Account fromAccount = accounts.get(userSessionEmail);

        Account toAccount = accounts.get(receiverEmail);

        if (fromAccount != null && toAccount != null
            && fromAccount.getSessionId().equals(userSessionId)
            && toAccount.getAccountNumber() == Integer.parseInt(receiverAccountNumber))
        {
            double amountTransfer = Double.parseDouble(amount);

            double fromAccountBalance = fromAccount.getBalance();

            double toAccountBalance = toAccount.getBalance();

            if (fromAccountBalance >= amountTransfer)
            {

                fromAccountBalance -= amountTransfer;

                toAccountBalance += amountTransfer;

                fromAccount.setBalance(fromAccountBalance);

                toAccount.setBalance(toAccountBalance);

                Transactions.getInstance().create(fromAccount.getEmail(),getDate()
                        ,"Fund transfer to "+toAccount.getEmail(),amountTransfer+"","",fromAccount.getBalance()+"");

                Transactions.getInstance().create(toAccount.getEmail(),getDate()
                        ,"Fund transfer from "+fromAccount.getEmail(),"",amountTransfer+"",toAccount.getBalance()+"");

                return Utilities.Messages.FUND_TRANSFER_UPDATE + Utilities.Delimiters.NEW_LINE
                       + Utilities.Keyword.FROM + Utilities.Delimiters.COLON_DELIMITER
                       + Utilities.Delimiters.WHITE_SPACE + userSessionEmail + Utilities.Delimiters.NEW_LINE
                       + Utilities.Keyword.TO + Utilities.Delimiters.COLON_DELIMITER
                       + Utilities.Delimiters.WHITE_SPACE + receiverEmail + Utilities.Delimiters.NEW_LINE
                       + Utilities.Keyword.AMOUNT + Utilities.Delimiters.COLON_DELIMITER
                       + Utilities.Delimiters.WHITE_SPACE + amount + Utilities.Delimiters.NEW_LINE
                       + Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER;
            }
            else
            {
                return Utilities.Messages.INSUFFICIENT_BALANCE
                       + Utilities.Delimiters.NEW_LINE + Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER;
            }
        }
        else
        {
            return Utilities.Messages.TRANSFER_FAILED
                   + Utilities.Delimiters.NEW_LINE + Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER;
        }
    }
}
