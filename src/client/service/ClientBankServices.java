package client.service;

import model.SocketControllers;
import model.UserSession;
import model.Utilities;
import validation.AccountValidation;
import validation.BankValidation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientBankServices
{

    private static SocketControllers socketControllers;

    private static final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    private static final String DOT = ".";

    private static final String DEFAULT_DECIMAL = ".00";

    private static String getAmount()
    {
        try
        {

            System.out.print("Enter amount: ");

            String amount = bufferedReader.readLine();

            if (!amount.contains(DOT))
            {
                amount += DEFAULT_DECIMAL;
            }

            while (!BankValidation.validateAmount(amount))
            {
                System.out.print("Wrong input, please enter amount(ex: 10000.00) again: ");

                amount = bufferedReader.readLine();

                if (!amount.contains(DOT))
                {
                    amount += DEFAULT_DECIMAL;
                }
            }
            return amount;
        }
        catch (Exception exception)
        {
            return "0.0";
        }
    }

    public static void deposit(UserSession userSession)
    {
        try
        {

            socketControllers = new SocketControllers(new Socket(Utilities.Dependencies.IP, Utilities.Dependencies.PORT));

            socketControllers.getWriter()
                    .println(socketControllers.getSocket().getLocalSocketAddress().toString()
                             + Utilities.Delimiters.DOUBLE_EQUAL_DELIMITER + Utilities.Keyword.API_ACTION_BANK
                             + Utilities.Delimiters.DOUBLE_COLON_DELIMITER + Utilities.Keyword.DEPOSIT
                             + Utilities.Delimiters.ARROW_DELIMITER + userSession.getSessionId()
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + userSession.getEmail()
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + getAmount());

            String response = socketControllers.getReader().readLine();

            socketControllers.getWriter().close();

            socketControllers.getReader().close();

            socketControllers.getSocket().close();

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

            System.out.println(response);

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.CLIENT_ERROR_RESTART);
        }
    }

    public static void withdrawal(UserSession userSession)
    {
        try
        {

            socketControllers = new SocketControllers(new Socket(Utilities.Dependencies.IP, Utilities.Dependencies.PORT));

            socketControllers.getWriter()
                    .println(socketControllers.getSocket().getLocalSocketAddress().toString()
                             + Utilities.Delimiters.DOUBLE_EQUAL_DELIMITER + Utilities.Keyword.API_ACTION_BANK
                             + Utilities.Delimiters.DOUBLE_COLON_DELIMITER + Utilities.Keyword.WITHDRAWAL
                             + Utilities.Delimiters.ARROW_DELIMITER + userSession.getSessionId()
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + userSession.getEmail()
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + getAmount());

            String response = socketControllers.getReader().readLine();

            socketControllers.getReader().close();

            socketControllers.getWriter().close();

            socketControllers.getSocket().close();

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

            System.out.println(response);

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);


        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.CLIENT_ERROR_RESTART);
        }
    }

    public static void getAccountDetail(UserSession userSession)
    {
        try
        {

            socketControllers = new SocketControllers(new Socket(Utilities.Dependencies.IP, Utilities.Dependencies.PORT));

            socketControllers.getWriter()
                    .println(socketControllers.getSocket().getLocalSocketAddress().toString()
                             + Utilities.Delimiters.DOUBLE_EQUAL_DELIMITER + Utilities.Keyword.API_ACTION_BANK
                             + Utilities.Delimiters.DOUBLE_COLON_DELIMITER + Utilities.Keyword.DETAILS
                             + Utilities.Delimiters.ARROW_DELIMITER + userSession.getSessionId()
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + userSession.getEmail());

            String response;

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

            while (!(response = socketControllers.getReader().readLine())
                    .equalsIgnoreCase(Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER))
            {
                System.out.println(response);
            }

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

            socketControllers.getReader().close();

            socketControllers.getWriter().close();

            socketControllers.getSocket().close();

        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.CLIENT_ERROR_RESTART);
        }
    }

    public static void fundTransfer(UserSession userSession)
    {
        try
        {
            System.out.print("Enter Receiver AccNo.: ");

            String receiverAccountNo = bufferedReader.readLine();

            while (!BankValidation.validateAccountNumber(receiverAccountNo))
            {

                System.out.print("Wrong input, Please enter AccNo. again: ");

                receiverAccountNo = bufferedReader.readLine();

            }

            System.out.print("Enter receiver email: ");

            String receiverEmail = bufferedReader.readLine();

            while (AccountValidation.validateEmail(receiverEmail))
            {

                System.out.print("Wrong input, please enter email again: ");

                receiverEmail = bufferedReader.readLine();

            }

            socketControllers = new SocketControllers(new Socket(Utilities.Dependencies.IP, Utilities.Dependencies.PORT));

            socketControllers.getWriter()
                    .println(socketControllers.getSocket().getLocalSocketAddress().toString()
                             + Utilities.Delimiters.DOUBLE_EQUAL_DELIMITER + Utilities.Keyword.API_ACTION_BANK
                             + Utilities.Delimiters.DOUBLE_COLON_DELIMITER + Utilities.Keyword.TRANSFER
                             + Utilities.Delimiters.ARROW_DELIMITER + userSession.getSessionId()
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + userSession.getEmail()
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + receiverAccountNo
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + receiverEmail
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + getAmount());

            String response;

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

            while (!(response = socketControllers.getReader().readLine())
                    .equalsIgnoreCase(Utilities.Delimiters.DOUBLE_SEMI_COLON_DELIMITER))
            {
                System.out.println(response);
            }

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

            socketControllers.getReader().close();

            socketControllers.getWriter().close();

            socketControllers.getSocket().close();


        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.CLIENT_ERROR_RESTART);
        }
    }

    public static void passbook(UserSession userSession)
    {
        try
        {

            socketControllers = new SocketControllers(new Socket(Utilities.Dependencies.IP, Utilities.Dependencies.PORT));

            socketControllers.getWriter()
                    .println(socketControllers.getSocket().getLocalSocketAddress().toString()
                             + Utilities.Delimiters.DOUBLE_EQUAL_DELIMITER + Utilities.Keyword.API_ACTION_BANK
                             + Utilities.Delimiters.DOUBLE_COLON_DELIMITER + Utilities.Keyword.PASSBOOK
                             + Utilities.Delimiters.ARROW_DELIMITER + userSession.getSessionId()
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + userSession.getEmail());

            String response;

            while ((response = socketControllers.getReader().readLine()) != null && response.length() > 0)
            {
                System.out.println(response);
            }

            socketControllers.getReader().close();

            socketControllers.getWriter().close();

            socketControllers.getSocket().close();
        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.CLIENT_ERROR_RESTART);
        }
    }
}
