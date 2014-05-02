package ws.remote;

public interface RemoteClientInterface {
	public boolean openConnection();
    public void handleSession();
    public void closeSession();
    public void run();
    public void sendOutput(Message output);
    public void handleInput(Message input);
}
