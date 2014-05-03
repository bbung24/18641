package ws.remote;

public interface RemoteClientInterface {
	public void openConnection();
    public void handleSession();
    public void closeSession();
    public void run();
    public void sendOutput(Message output);
    public void handleInput(Message input);
    public boolean ready();
    public Message readInput();
}
