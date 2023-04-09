package bankserver;

import bankserver.api.APIs;
import model.SocketControllers;
import model.Utilities;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankServer
{

    static int clientCount = 0;

    static ServerSocket bankServer;

    private static final String SERVER_STARTED = "Server started at ";

    private static final String SERVER_NOT_STARTED = "Unable to create server, please try again.";

    private static final String UNKNOWN_REQ = "Unknown request:";

    private static final String CLOSE_CONN = "Closing connection ...";

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private BankServer()
    {
        createServer();
        startConnections();
    }

    public void createServer()
    {
        try
        {
            bankServer = new ServerSocket(Utilities.Dependencies.PORT);

            System.out.println(SERVER_STARTED + Utilities.Dependencies.IP
                               + Utilities.Delimiters.COLON_DELIMITER + Utilities.Dependencies.PORT);
        }
        catch (Exception exception)
        {
            System.out.println(SERVER_NOT_STARTED);
        }
    }

    public void startConnections()
    {
        try
        {
            new Thread(() -> {
                while (true)
                {
                    try
                    {
                        Socket socket = bankServer.accept();

                        System.out.println(Utilities.Messages.NEW_CONNECTION + socket.getRemoteSocketAddress());

                        clientCount++;

                        executorService.execute(() -> {
                            try
                            {
                                startReadingClient(new SocketControllers(socket));
                            }
                            catch (Exception exception)
                            {
                                System.out.println("Internal server error: error in socket creation");
                            }
                        });

                    }
                    catch (Exception exception)
                    {
                        System.out.println(Utilities.Messages.SERVER_ERROR_RESTART);
                        break;
                    }
                }
            }, Utilities.Messages.READ_CONNECTIONS_THREAD).start();
        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.SERVER_ERROR_RESTART);
        }
    }

    public void startReadingClient(SocketControllers socketControllers)
    {
        try
        {
            while (true)
            {
                String request = socketControllers.getReader().readLine();

                if (request != null && request.contains(Utilities.Delimiters.DOUBLE_EQUAL_DELIMITER)
                    && request.contains(Utilities.Delimiters.ARROW_DELIMITER)
                    && request.contains(Utilities.Delimiters.DOUBLE_COLON_DELIMITER))
                {

                    String[] requestSplit = request.split(Utilities.Delimiters.DOUBLE_EQUAL_DELIMITER);

                    String remoteSocketAddress = requestSplit[0];

                    String[] apiContext = requestSplit[1].split(Utilities.Delimiters.ARROW_DELIMITER);

                    String[] api = apiContext[0].split(Utilities.Delimiters.DOUBLE_COLON_DELIMITER);

                    if (requestSplit.length != 2 && apiContext.length != 2 && api.length != 2)
                    {
                        socketControllers.getWriter().println(Utilities.Messages.BAD_REQUEST);
                    }
                    else
                    {
                        System.out.println(remoteSocketAddress + Utilities.Delimiters.ARROW_DELIMITER
                                           + Arrays.toString(api));
                        new APIs().route(api[0], apiContext[1], socketControllers, api[1]);
                    }

                }
                else
                {
                    if (request != null)
                        System.out.println(UNKNOWN_REQ + request + Utilities.Delimiters.NEW_LINE + CLOSE_CONN);

                    socketControllers.getWriter()
                            .println(Utilities.Messages.UNABLE_TO_PARSE);

                    break;
                }
            }
        }
        catch (Exception exception)
        {
            System.out.println(Utilities.Messages.INTERNAL_SERVER_ERROR);

            socketControllers.getWriter().println(Utilities.Messages.INTERNAL_SERVER_ERROR);
        }

    }

    public static void main(String[] args)
    {
        System.out.println(Utilities.Messages.WELCOME);
        new BankServer();
    }
}
