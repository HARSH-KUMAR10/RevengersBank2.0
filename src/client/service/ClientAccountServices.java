package client.service;

import model.SocketControllers;
import model.UserSession;
import model.Utilities;
import validation.AccountValidation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientAccountServices
{
    private static SocketControllers socketControllers;

    private static int chanceLeft = 3;

    public static void signUp()
    {
        try
        {
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            // Taking user name input

            System.out.print("Enter name: ");

            String name = userInputReader.readLine();

            while (!AccountValidation.validateName(name) && chanceLeft > 0)
            {
                chanceLeft--;

                System.out.print("Wrong input, please enter name again: ");

                name = userInputReader.readLine();
            }

            if (chanceLeft == 0) return;

            chanceLeft = 3;

            // Taking user age input

            System.out.print("Enter age: ");

            String age = userInputReader.readLine();

            while (!AccountValidation.validateAge(age) && chanceLeft > 0)
            {
                chanceLeft--;

                System.out.print("Wrong input, please enter age(12-99) again: ");

                age = userInputReader.readLine();
            }

            if (chanceLeft == 0) return;

            chanceLeft = 3;

            // Taking user gender input

            System.out.print("1. Female\n2. Male\nEnter choice: ");

            String genderChoice = userInputReader.readLine();

            while (!AccountValidation.validateGender(genderChoice) && chanceLeft > 0)
            {
                System.out.print("Wrong input, please enter gender(1. Female, 2. Male) again: ");

                genderChoice = userInputReader.readLine();

                chanceLeft--;
            }

            if (chanceLeft == 0) return;

            chanceLeft = 3;

            // Taking user email input

            System.out.print("Enter email: ");

            String email = userInputReader.readLine();

            while (AccountValidation.validateEmail(email) && chanceLeft > 0)
            {
                chanceLeft--;

                System.out.print("Wrong input, please enter email again: ");

                email = userInputReader.readLine();
            }

            if (chanceLeft == 0) return;

            chanceLeft = 3;

            // Taking user pin input

            System.out.print("Enter PIN(4 digit): ");

            String pin = userInputReader.readLine();

            while (AccountValidation.validatePin(pin) && chanceLeft > 0)
            {
                chanceLeft--;

                System.out.print("Wrong input, please enter PIN(4 digit) again: ");

                pin = userInputReader.readLine();
            }


            if (chanceLeft == 0) return;

            chanceLeft = 3;

            socketControllers = new SocketControllers(new Socket(Utilities.Dependencies.IP, Utilities.Dependencies.PORT));

            socketControllers.getWriter()
                    .println(socketControllers.getSocket().getLocalSocketAddress().toString()
                             + "==Account::Create->" + name + ";" + age + ";"
                             + (genderChoice.equals("1") ? "Female" : "Male") + ";" + email + ";" + pin);

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
            exception.printStackTrace();
            System.out.println(Utilities.Messages.CLIENT_ERROR_RESTART);
        }
    }

    public static void login()
    {
        try
        {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter email: ");

            String email = bufferedReader.readLine();

            while (AccountValidation.validateEmail(email) && chanceLeft > 0)
            {
                chanceLeft--;

                System.out.print("Wrong input, please enter email again: ");

                email = bufferedReader.readLine();

            }


            if (chanceLeft == 0) return;

            chanceLeft = 3;

            System.out.print("Enter pin: ");

            String pin = bufferedReader.readLine();

            while (AccountValidation.validatePin(pin) && chanceLeft > 0)
            {
                chanceLeft--;

                System.out.print("Wrong input, please enter pin again: ");

                pin = bufferedReader.readLine();

            }

            if (chanceLeft == 0) return;

            chanceLeft = 3;

            socketControllers = new SocketControllers(new Socket(Utilities.Dependencies.IP, Utilities.Dependencies.PORT));

            socketControllers.getWriter()
                    .println(socketControllers.getSocket().getLocalSocketAddress().toString()
                             + Utilities.Delimiters.DOUBLE_EQUAL_DELIMITER + Utilities.Keyword.API_ACTION_ACCOUNT
                             + Utilities.Delimiters.DOUBLE_COLON_DELIMITER + Utilities.Keyword.READ
                             + Utilities.Delimiters.ARROW_DELIMITER + email
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + pin);

            String response = socketControllers.getReader().readLine();

            socketControllers.getReader().close();

            socketControllers.getWriter().close();

            socketControllers.getSocket().close();

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

            if (!response.equalsIgnoreCase("-1") && !response.equalsIgnoreCase("-2"))
            {

                UserSession userSession = new UserSession(email, response);

                System.out.println(Utilities.Messages.LOGIN_SUCCESS);

                boolean menuFlag = true;

                while (menuFlag)
                {

                    System.out.print("1. Deposit\n2. Withdrawal\n3. Account Details\n4. Fund Transfer\n5. Print passbook\n0. Logout\nEnter your choice: ");

                    switch (bufferedReader.readLine())
                    {
                        case Utilities.Keyword.ONE -> ClientBankServices.deposit(userSession);

                        case Utilities.Keyword.TWO -> ClientBankServices.withdrawal(userSession);

                        case Utilities.Keyword.THREE -> ClientBankServices.getAccountDetail(userSession);

                        case Utilities.Keyword.FOUR -> ClientBankServices.fundTransfer(userSession);

                        case Utilities.Keyword.FIVE -> ClientBankServices.passbook(userSession);

                        case Utilities.Keyword.ZERO ->
                        {
                            if (logout(userSession))
                            {
                                userSession = null;

                                menuFlag = false;

                                System.out.println(Utilities.Messages.LOGOUT_MESSAGE);
                            }
                            else
                            {
                                System.out.println(Utilities.Messages.UNABLE_TO_LOGOUT);
                            }
                        }
                        default -> System.out.println(Utilities.Messages.WRONGINPUT);
                    }
                }
                System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

            }
            else
            {
                if (response.equalsIgnoreCase("-1"))
                {
                    System.out.println("Login failed, check credentials");
                }
                else if (response.equalsIgnoreCase("-2"))
                {
                    System.out.println("Already logged in");
                }
                else
                {
                    System.out.println("Login failed");
                }
            }

            System.out.println(Utilities.Messages.OUTPUT_DIVIDER);

        }
        catch (SocketTimeoutException socketTimeoutException)
        {
            System.out.println(Utilities.Messages.SERVER_BUSY);
        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.CLIENT_ERROR_RESTART);
        }
    }


    // Account::Logout -> /10.20.40.194:89765==Account::Logout->email;sessionId
    public static boolean logout(UserSession userSession)
    {
        try
        {

            socketControllers = new SocketControllers(new Socket(Utilities.Dependencies.IP, Utilities.Dependencies.PORT));

            socketControllers.getWriter()
                    .println(socketControllers.getSocket().getLocalSocketAddress().toString()
                             + Utilities.Delimiters.DOUBLE_EQUAL_DELIMITER + Utilities.Keyword.API_ACTION_ACCOUNT
                             + Utilities.Delimiters.DOUBLE_COLON_DELIMITER + Utilities.Keyword.LOGOUT
                             + Utilities.Delimiters.ARROW_DELIMITER + userSession.getEmail()
                             + Utilities.Delimiters.SEMI_COLON_DELIMITER + userSession.getSessionId());

            String response = socketControllers.getReader().readLine();

            socketControllers.getWriter().close();

            socketControllers.getReader().close();

            socketControllers.getSocket().close();

            if (response.equalsIgnoreCase(Utilities.Messages.LOGOUT_SUCCESS))
            {
                return true;
            }
            else
            {
                System.out.println(response);
                return false;
            }
        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.CLIENT_ERROR_RESTART);
            return false;
        }
    }

}
