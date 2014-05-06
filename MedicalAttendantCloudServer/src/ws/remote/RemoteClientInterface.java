package ws.remote;


/**	
 * 	S14 18641
 *  Medical Attendant.
 * 	
 * 	@author Sang Rok Shin, Inho Yong
 **/

public interface RemoteClientInterface {
	final boolean DEBUG = true;
	public boolean openConnection();
    public void handleSession();
    public void closeSession();
    public void run();
    public void sendOutput(Message output);
    public void handleInput(Message input);
}
