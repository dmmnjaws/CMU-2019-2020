package pt.ulisboa.tecnico.cmov.remote;

public class WaitInQueueStats {
    private String beacon;
    private String diningPlace;
    private int currentQueueSize;
    private double[] numberOfClients;
    private double[] acumWaitTimePerNumberOfClients;
    private double[] numberOfSamplesPerNumberOfClients;
    private int CURRENT_MAX_NUMBER_OF_CLIENTS = 0;

    public WaitInQueueStats(String beacon, String diningPlace){
        this.beacon = beacon;
        this.diningPlace = diningPlace;
        this.currentQueueSize = 0;

        initializeVectors();
    }

    public void initializeVectors(){
        this.numberOfClients = new double[1];
        this.acumWaitTimePerNumberOfClients = new double[1];
        this.numberOfSamplesPerNumberOfClients = new double[1];

        this.numberOfClients[0] = 0;
        this.numberOfSamplesPerNumberOfClients[0] = 0;
        this.acumWaitTimePerNumberOfClients[0] = 0;
    }

    public void resizeVectors(int clients){
        double[] auxAcum = this.acumWaitTimePerNumberOfClients;
        double[] auxNumber = this.numberOfSamplesPerNumberOfClients;

        this.numberOfClients = new double[clients];
        this.acumWaitTimePerNumberOfClients = new double[clients];
        this.numberOfSamplesPerNumberOfClients = new double[clients];

        for(int i = 0; i < this.currentQueueSize; i++){
            this.numberOfClients[i] = i;
            if(CURRENT_MAX_NUMBER_OF_CLIENTS <= i){
                this.acumWaitTimePerNumberOfClients[i] = auxAcum[i];
                this.numberOfSamplesPerNumberOfClients[i] = auxNumber[i];
            }else{
                this.numberOfSamplesPerNumberOfClients[i] = 0;
                this.acumWaitTimePerNumberOfClients[i] = 0;
            }
        }
    }

    public void addClientToQueue(){
        this.currentQueueSize++;
    }

    public void addStatisticData(UserInQueue userEntry, long exitTime){
        long timeInQueue = (exitTime - userEntry.getTimeOfArrival())/1000;

        if(CURRENT_MAX_NUMBER_OF_CLIENTS < userEntry.getClientsInQueue()){
            resizeVectors(userEntry.getClientsInQueue());
        }

        this.acumWaitTimePerNumberOfClients[userEntry.getClientsInQueue()] += timeInQueue;
        this.numberOfSamplesPerNumberOfClients[userEntry.getClientsInQueue()] ++;
        this.currentQueueSize--;
    }

    public int getCurrentQueueSize() { return currentQueueSize; }

    public String getDiningPlace(){ return this.diningPlace; }

    public String getCurrentWaitTime(){

        if(CURRENT_MAX_NUMBER_OF_CLIENTS == 0){
            if(this.acumWaitTimePerNumberOfClients[0] == 0){
                return "1 min";
            }else {
                return ((int) (this.acumWaitTimePerNumberOfClients[0]/this.numberOfSamplesPerNumberOfClients[0])/60) + " min";
            }
        }

        double[] avgWaitTimePerNumberOfClients = new double[CURRENT_MAX_NUMBER_OF_CLIENTS];

        for(int i = 0; i < CURRENT_MAX_NUMBER_OF_CLIENTS; i++){
            avgWaitTimePerNumberOfClients[i] = this.acumWaitTimePerNumberOfClients[i] / this.numberOfSamplesPerNumberOfClients[i];
        }

        LinearRegression linearRegression = new LinearRegression(this.numberOfClients, avgWaitTimePerNumberOfClients);

        return ((int) linearRegression.predict(this.currentQueueSize)/60) + " min";
    }
}
