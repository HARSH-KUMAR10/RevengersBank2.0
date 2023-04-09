package bankserver.service;

import bankserver.repo.Accounts;
import bankserver.repo.Logins;
import model.Account;
import model.Utilities;
import validation.AccountValidation;

public class ServerAccountServices
{

    private static final String DEFAULT_SESSION_ID = "-1";

    private static final String DEFAULT_LOGGED_ID = "-2";

    private static final String DOUBLE_DEFAULT_STRING = "0.0";

    // Account::Create -> /10.20.40.194:89765==Account::Create->name;age;gender;email;pin
    public static String createAccount(String valuesString)
    {
        try
        {
            String[] values = valuesString.split(Utilities.Delimiters.SEMI_COLON_DELIMITER);

            if (values.length == 5 && AccountValidation.validateName(values[0])
                && AccountValidation.validateAge(values[1]) && AccountValidation.validateGenderString(values[2])
                && !AccountValidation.validateEmail(values[3]) && !AccountValidation.validatePin(values[4]))
            {
                return  Accounts.getInstance().create(values[0], values[1],
                        values[2], values[3], values[4], DOUBLE_DEFAULT_STRING);
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

    // Account::Read -> /10.20.40.194:89765==Account::Read->email;pin
    public static String loginAccount(String valuesString)
    {
        try
        {
            String[] values = valuesString.split(Utilities.Delimiters.SEMI_COLON_DELIMITER);

            if (values.length == 2
                && !AccountValidation.validateEmail(values[0])
                && !AccountValidation.validatePin(values[1]))
            {
                Account account = Accounts.getInstance().read(values[0], values[1]);
                if (account != null)
                {
                    Logins logins = Logins.getInstance();
                    if (!logins.read(values[0]))
                    {
                        logins.create(values[0]);
                        return account.getSessionId();
                    }
                    else
                    {
                        return DEFAULT_LOGGED_ID;
                    }
                }
                else
                {
                    return DEFAULT_SESSION_ID;
                }
            }
            else
            {
                return DEFAULT_SESSION_ID;
            }
        }
        catch (Exception exception)
        {
            return DEFAULT_SESSION_ID;
        }
    }

    public static String logoutAccount(String valuesString)
    {
        try{
            String[] values = valuesString.split(Utilities.Delimiters.SEMI_COLON_DELIMITER);

            if (values.length == 2
                && !AccountValidation.validateEmail(values[0])
                && AccountValidation.validateSessionId(values[1]))
            {
                Logins logins = Logins.getInstance();
                if(logins.read(values[0])){
                    logins.delete(values[0]);
                }
                return Utilities.Messages.LOGOUT_SUCCESS;
            }else{
                return Utilities.Messages.WRONG_INPUT+", "+Utilities.Messages.RESTART_SERVER;
            }
        }catch (Exception exception){
            return Utilities.Messages.INTERNAL_SERVER_ERROR;
        }
    }
}
