package bankserver.repo;

import java.util.ArrayList;

public class Logins implements Operation
{
    private static Logins logins = null;

    private static final ArrayList<String> loggedInAccounts = new ArrayList<>();

    private Logins()
    {
    }

    public static synchronized Logins getInstance()
    {
        if (logins == null)
        {
            logins = new Logins();
        }
        return logins;
    }

    @Override
    public Boolean create(String... values)
    {
        return loggedInAccounts.add(values[0]);
    }

    @Override
    public Boolean delete(String... values)
    {
        return loggedInAccounts.remove(values[0]);
    }

    @Override
    public Boolean read(String... values)
    {
        return loggedInAccounts.contains(values[0]);
    }

    @Override
    public Object update(String... args)
    {
        return null;
    }

}
