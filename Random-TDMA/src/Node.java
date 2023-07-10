import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Node {

    private int id; 
    private Queue<Packet> buffer ;
    private int bufferFails;
    public static ArrayList<Packet> transmittedpackets;

    public Node(int id){
        this.id = id; 
        buffer = new ArrayDeque<>(4); // create a queue(buffer) with a maximum capacity of four elements
        bufferFails = 0;
        transmittedpackets= new ArrayList<>(); //packages from all buffers that are successfully sent are stored  
    }
    public int getId(){
        return id;
    }
    public int getbufferFails(){
        return bufferFails ; 
    }
    public void setBufferFails(int value){
        bufferFails = value; 
    }
    public ArrayList<Packet> gettransmittedpackets() {
        return transmittedpackets;
    }
    public void clearBuffer(){ 
        //emptying the buffer for the next RUN-simulation 
        buffer.clear();
    }
    public void cleartransmittedpackets(){
        // emptying the transmittedpackets for the next RUN - simulation
        transmittedpackets.clear();
        buffer.clear();
    }
    public boolean isBufferFull(){
        //check if the buffer is full 
        if(buffer.size() == 4)
            return true ;
        else 
            return false; 
    }
    public void increasebufferFails(){
        bufferFails ++ ;
    }
    public void addPacketToBuffer(Packet packet){
        //add a packet on queue 
        buffer.add(packet);
    }
    public void findPacket(int channel, int slot){
        //if the buffer contains a packet destined for one of the two nodes listening to the channel, 
        //remove the packet from the queue and note that the packet has been sent and the slot to be sent 

        //nodes 2*channel-1 and 2*channel are listening on channel.
        int value1 = 2*channel - 1;
        int value2 = 2*channel;
        
        if (!buffer.isEmpty()) { 
            for (Packet packet : buffer) {
                if(packet.getDestination()==value1 ){
                    packet.setslotSend(slot);
                    transmittedpackets.add(packet);
                    buffer.remove(packet);
                    //System.out.println("Succesfull sending of a packet from node" + packet.getfrom() + " to node" + packet.getDestination()+ " with the channel " + channel );
                    break;
                }else if(packet.getDestination()==value2){
                    packet.setslotSend(slot);
                    transmittedpackets.add(packet);
                    buffer.remove(packet);
                    //System.out.println("Succesfull sending of a packet from node " + packet.getfrom() + " to node " + packet.getDestination()+ " with the channel " + channel );
                    break;
                }
            }   
        }
    }
}

