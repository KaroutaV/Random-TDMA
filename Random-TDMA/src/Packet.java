public class Packet {
    private int slot; // the slot where the packet was created 
    private int from; // transiever 
    private int sendto; //receiver 
    private int slotSend; //in which slot was sent 

    public Packet(int slot, int from, int sendto){
        this.slot = slot;
        this.from = from ; 
        this.sendto = sendto;
        slotSend=0;
    }
    public int getDelay(){
        //calculates the delay of the packet
        int delay = slotSend - slot;
        return delay; 
    }
    public void setslotSend(int time){
        slotSend = time; 
    }
    public void printPacket(){
        System.out.println("slot " + slot + " from " + from + " sent to " + sendto);
    }
    public int getslot(){
        return slot;
    }
    public int getfrom(){
        return from;
    }
    public int getDestination(){
        return sendto;
    }
}

