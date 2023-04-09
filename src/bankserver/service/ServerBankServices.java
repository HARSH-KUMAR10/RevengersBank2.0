package bankserver.service;

import bankserver.repo.Accounts;
import bankserver.repo.Transactions;
import model.Transaction;
import model.Utilities;
import validation.AccountValidation;
import validation.BankValidation;

import java.util.ArrayList;
import java.util.Iterator;

public class ServerBankServices
{

    // Bank::Deposit -> /10.20.40.194:91729==Bank::Deposit->sessionId;email;amount
    public static String deposit(String valuesString)
    {
        try
        {
            String[] values = valuesString.split(Utilities.Delimiters.SEMI_COLON_DELIMITER);

            if (values.length == 3 && AccountValidation.validateSessionId(values[0])
                && !AccountValidation.validateEmail(values[1]) && BankValidation.validateAmount(values[2]))
            {

                return Accounts.getInstance().update(values[0], values[1], values[2], Utilities.Keyword.TRUE);
            }
            else
            {
                return Utilities.Messages.WRONG_INPUT;
            }
        }
        catch (Exception exception)
        {
            return Utilities.Messages.INTERNAL_SERVER_ERROR;
        }
    }

    // Bank::Withdraw -> /10.20.40.194:89174==Bank::Withdrawal->sessionId;email;amount
    public static String withdrawal(String valuesString)
    {
        try
        {
            String[] values = valuesString.split(Utilities.Delimiters.SEMI_COLON_DELIMITER);

            if (values.length == 3 && AccountValidation.validateSessionId(values[0])
                && !AccountValidation.validateEmail(values[1]) && BankValidation.validateAmount(values[2]))
            {
                return Accounts.getInstance().update(values[0], values[1], values[2], Utilities.Keyword.FALSE);
            }
            else
            {
                return Utilities.Messages.WRONG_INPUT;
            }
        }
        catch (Exception exception)
        {
            return Utilities.Messages.INTERNAL_SERVER_ERROR;
        }
    }

    // Bank::Details -> /10.20.40.194:89102==Bank::Details->sessionId;email
    // Response needs to be ended with ;;
    public static String details(String valuesString)
    {
        try
        {
            String[] values = valuesString.split(Utilities.Delimiters.SEMI_COLON_DELIMITER);

            if (values.length == 2 && AccountValidation.validateSessionId(values[0])
                && !AccountValidation.validateEmail(values[1]))
            {
                return Accounts.getInstance().read(values[0], values[1], true);
            }
            else
            {
                return Utilities.Messages.WRONG_INPUT + Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER;
            }
        }
        catch (Exception exception)
        {
            return Utilities.Messages.INTERNAL_SERVER_ERROR + Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER;
        }
    }


    // Bank::Transfer -> /10.20.40.194:89213==Bank::Transfer->sessionId;email;receiverAccNo;receiverEmail;amount
    public static String fundTransfer(String valuesString)
    {
        try
        {
            String[] values = valuesString.split(Utilities.Delimiters.SEMI_COLON_DELIMITER);

            if (values.length == 5 && AccountValidation.validateSessionId(values[0])
                && !AccountValidation.validateEmail(values[1]) && BankValidation.validateAccountNumber(values[2])
                && !AccountValidation.validateEmail(values[3]) && BankValidation.validateAmount(values[4]))
            {
                return Accounts.update(values[0], values[1], values[2], values[3], values[4]);
            }
            else
            {
                return Utilities.Messages.WRONG_INPUT;
            }
        }
        catch (Exception exception)
        {
            return Utilities.Messages.INTERNAL_SERVER_ERROR;
        }
    }

    // get all passbook data.
    public static String passbook(String valuesString)
    {
        try
        {
            String[] values = valuesString.split(Utilities.Delimiters.SEMI_COLON_DELIMITER);

            if (values.length == 2
                && AccountValidation.validateSessionId(values[0])
                && !AccountValidation.validateEmail(values[1]))
            {
                ArrayList<Transaction> transactions = Transactions.getInstance().read(values[1]);
                if(transactions!=null)
                {
                    String passbookEntries = "Date\t\t\t\tParticular\t\t\tDeposit\t\t\tWithdrawal\t\tBalance\n";
                    for (Transaction transaction : transactions)
                    {
                        passbookEntries += passbookEntry(transaction);
                    }
                    return passbookEntries;
                }else{
                    return "";
                }
            }
            else
            {
                return Utilities.Messages.WRONG_INPUT + Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return Utilities.Messages.INTERNAL_SERVER_ERROR;
        }
    }

    private static String passbookEntry(Transaction transaction)
    {
        return transaction.getDate() + Utilities.Delimiters.TAB_DELIMITER
               + Utilities.Delimiters.TAB_DELIMITER + Utilities.Delimiters.TAB_DELIMITER
               + transaction.getParticulars() + Utilities.Delimiters.TAB_DELIMITER
               + Utilities.Delimiters.TAB_DELIMITER + Utilities.Delimiters.TAB_DELIMITER
               + transaction.getDeposit() + Utilities.Delimiters.TAB_DELIMITER
               + Utilities.Delimiters.TAB_DELIMITER + Utilities.Delimiters.TAB_DELIMITER
               + transaction.getWithdrawal() + Utilities.Delimiters.TAB_DELIMITER
               + Utilities.Delimiters.TAB_DELIMITER + Utilities.Delimiters.TAB_DELIMITER
               + transaction.getBalance() + Utilities.Delimiters.NEW_LINE;
    }
}
