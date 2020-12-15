package ru.kpekepsalt.ruvik.dto;

public class NetworkMessageDto {

    private NetworkAction networkAction;
    private NetworkOrigin networkOrigin;
    private NetworkStatus networkStatus;
    private MessageDto data;

    public NetworkAction getNetworkAction() {
        return networkAction;
    }

    public void setNetworkAction(NetworkAction networkAction) {
        this.networkAction = networkAction;
    }

    public NetworkOrigin getNetworkOrigin() {
        return networkOrigin;
    }

    public void setNetworkOrigin(NetworkOrigin networkOrigin) {
        this.networkOrigin = networkOrigin;
    }

    public MessageDto getData() {
        return data;
    }

    public void setData(MessageDto data) {
        this.data = data;
    }

    public NetworkStatus getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(NetworkStatus networkStatus) {
        this.networkStatus = networkStatus;
    }
}
