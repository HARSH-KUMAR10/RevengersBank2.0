package bankserver.api;

import bankserver.service.ServerBankServices;
import model.SocketControllers;
import model.Utilities;

public class BankAPIs implements API
{
    public void route( String route, String values, SocketControllers socketControllers, String... noUse){
        switch (route)
        {
            case Utilities.Keyword.DEPOSIT -> socketControllers.getWriter()
                    .println(ServerBankServices.deposit(values));

            case Utilities.Keyword.WITHDRAWAL -> socketControllers.getWriter()
                    .println(ServerBankServices.withdrawal(values));

            case Utilities.Keyword.DETAILS -> socketControllers.getWriter()
                    .println(ServerBankServices.details(values));

            case Utilities.Keyword.TRANSFER -> socketControllers.getWriter()
                    .println(ServerBankServices.fundTransfer(values));

            case Utilities.Keyword.PASSBOOK -> socketControllers.getWriter()
                    .println(ServerBankServices.passbook(values));

            default -> socketControllers.getWriter()
                    .println(Utilities.Keyword.API_ACTION_BANK+Utilities.Messages.ROUTE_UNSUPPORTED);
        }
    }
}
