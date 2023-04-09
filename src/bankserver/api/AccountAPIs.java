package bankserver.api;

import bankserver.service.ServerAccountServices;
import model.SocketControllers;
import model.Utilities;

public class AccountAPIs implements API
{
    public void route(String route, String values,SocketControllers socketControllers,String... noUse){
        switch (route)
        {
            case Utilities.Keyword.CREATE -> socketControllers.getWriter()
                    .println(ServerAccountServices.createAccount(values));

            case Utilities.Keyword.READ -> socketControllers.getWriter()
                    .println(ServerAccountServices.loginAccount(values));

            case Utilities.Keyword.LOGOUT -> socketControllers.getWriter()
                    .println(ServerAccountServices.logoutAccount(values));

            default -> socketControllers.getWriter()
                    .println(Utilities.Keyword.API_ACTION_ACCOUNT+Utilities.Messages.ROUTE_UNSUPPORTED);
        }
    }
}
