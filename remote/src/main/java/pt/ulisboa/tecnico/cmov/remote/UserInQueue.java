package pt.ulisboa.tecnico.cmov.remote;

public class UserInQueue {
    private String userName;
    private String beacon;
    private long timeOfArrival;
    private int clientsInQueue;


    public UserInQueue(String userName, String beacon, long timeOfArrival, int clientsInQueue){
        this.userName = userName;
        this.beacon = beacon;
        this.timeOfArrival = timeOfArrival;
        this.clientsInQueue = clientsInQueue;
    }

    public String getBeacon() { return beacon; }

    public long getTimeOfArrival() { return timeOfArrival; }

    public String getUserName() { return userName; }

    public int getClientsInQueue() { return clientsInQueue; }

}
