package handleConnections;

public interface SocketClientInterface {
	boolean openConnection();
    void handleSession();
    void closeSession();
}
