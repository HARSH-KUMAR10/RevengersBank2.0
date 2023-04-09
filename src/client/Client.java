package client;

import client.service.ClientAccountServices;
import model.SocketControllers;
import model.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client
{
    private SocketControllers socketControllers;

    public Client()
    {
        try
        {
//            socketControllers = new SocketControllers(new Socket(Utilities.Dependencies.IP, Utilities.Dependencies.PORT));

            start();
        }
        catch (Exception exception)
        {
            System.out.println("Unable to connect to server, please restart."+Utilities.Messages.UNABLE_TO_CONN_SERVER);
        }
    }

    private void start()
    {
        try
        {
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            boolean loopFlag = true;

            while (loopFlag)
            {
                System.out.print("1. Login\n2. Create Account\n0. Exit\nEnter your choice: ");

                switch (userInputReader.readLine())
                {
                    case Utilities.Keyword.ONE -> ClientAccountServices.login();

                    case Utilities.Keyword.TWO -> ClientAccountServices.signUp();

                    case Utilities.Keyword.ZERO -> loopFlag=false;

                    default -> System.out.println(Utilities.Messages.WRONGINPUT);
                }
            }
        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.CLIENT_ERROR_RESTART);
        }

    }
}
