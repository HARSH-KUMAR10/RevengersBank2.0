package client;

import model.Utilities;

public class Bootstrap
{
    public static void main(String[] args)
    {
        System.out.println(Utilities.Messages.WELCOME);
        new Client();
    }
}