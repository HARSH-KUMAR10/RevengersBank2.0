package bankserver.api;

import model.SocketControllers;
import model.Utilities;

public class APIs implements API
{
    public void route(String route, String values, SocketControllers socketControllers, String... operation)
    {
        switch (route)
        {
            case Utilities.Keyword.API_ACTION_ACCOUNT ->
                new AccountAPIs().route(operation[0], values, socketControllers);

            case Utilities.Keyword.API_ACTION_BANK ->
                new BankAPIs().route(operation[0], values, socketControllers);

            default -> socketControllers.getWriter().println(Utilities.Messages.ROUTE_UNSUPPORTED);

        }
    }
}
