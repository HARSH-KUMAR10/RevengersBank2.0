package bankserver.api;

import model.SocketControllers;

public interface API
{
    void route( String route, String values,SocketControllers socketControllers,String... args);
}
