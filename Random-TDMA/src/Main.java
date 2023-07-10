import java.util.ArrayList;
import java.util.Random;

public class Main {

    private static final int N = 8; // number of nodes 
    private static final int W=4; //number of channels 
    private static final int RUNS= 50; // number of simulations   
    private static final int NO_OF_SLOTS = 1000000; // number of timeslots that each simulation will run  
    public static void main(String[] args){

        Node[] node = new Node[N]; // creating a table o 8 Node-type objects 
        ArrayList<Integer> listOfChannels = new ArrayList<>(); // list of channels
        ArrayList<Integer> listOfNodes = new ArrayList<>(); // list of nodes  
        ArrayList<Integer> trans = new ArrayList<>(); //list with channel allocation to nodes  
        double prob = 0.1 ; // packet creation probability 
        Integer noOfPackets=0; // total packages generated 
        int slot=0; // counter for slot time 
        
        //creation and initialization of the 8 Node-type objects, with each object 
        //having a unique id value from 1 o 8 
        for(int i=0;i<N;i++){
            node[i] = new Node(i+1);
        }

        for(int runs=0; runs<RUNS; runs++){ // for loop of 50 simulations 
            
            prob= 0.001+0.8*(runs/(double)RUNS); // the probabilities are calculated according to the RUNS
            
            for(int time_slot = 0; time_slot < NO_OF_SLOTS; time_slot++){ // repeat for consecutive  timeslot 
                
                // initialize Arraylists
                for(int i=0;i<N;i++){   //initialize the trans list with 0 
                    trans.add(0); 
                }
                initialValues(listOfNodes, N); //creation of the node list {1,2,3,4,5,6,7,8}
                initialValues(listOfChannels, W); //creation of channel list {1,2,3,4}
                
                //each node at the begginig of each time slot generates a packet with probability li
                for(int i=0; i<N; i++){ 
                    double r = Math.random();
                    if(r<prob){ // if r < prob generates a packet
                        int n;  // randomly selects a receiver from {1,2,3,4,5,6,7,8} by calling the function randomReceiver 
                        n = randomReceiver(i); 
                        /*create a new Packet object and assigns it to the variable packet. 
                         * The Packet constructor is invoked with three arguments: the time_slot, i+1, and n+1
                         * time_slot represents the time slot at which the packet is being created
                         * i+1 represend the transmitter
                         * n+1 represent the receiver  */
                        Packet packet = new Packet(time_slot,i+1,n+1); 
                        noOfPackets++; 
                        //packet.printPacket();  
                        if(node[i].isBufferFull()){  //if the buffer of node i is full then the bufferFails variable is increased by one  
                            node[i].increasebufferFails();
                            //System.out.println("Buffer "+ node[i].getId() + " is full");
                        }else{                      //if buffer i is not full it puts the packet in the buffer
                            node[i].addPacketToBuffer(packet);
                            //System.out.println("put the packet intended for the receiver " + packet.getDestination() + " in the buffer " + node[i].getId());
                        }
                    }
                }
                slot++;
                //implementation of function transmitionSchedule algorithm for channel allocation 
                for(int i=0;i<W;i++){
                    transmitionSchedule(listOfChannels,listOfNodes,trans);
                }
                
                //if the node is transmitting a packet it checks in its queue if it has a packet for the channel k = trans[i]
                for (int i=0;i<N;i++){
                    if (trans.get(i)!=0){
                        node[i].findPacket(trans.get(i),slot);
                    }
                }
                //clear listOfNodes and trans for next time slot 
                listOfNodes.clear();
                trans.clear();
            } // end of NO_OF_SLOTS
            //calculate average Delay 
            ArrayList<Packet> list = node[1].gettransmittedpackets(); //list of all packets transmitted from all nodes buffers 
            double averageDelay = calcAvgDelay(list); 
            //calculate throughput
            double throughput = (double)list.size()/slot;
            System.out.println("average Delay " + averageDelay + " throughtput "  + throughput);
            
            // clear arrays and buffers for the next run 
            list.clear();
            node[1].cleartransmittedpackets();
            for(int i=0;i<N;i++){
                node[i].clearBuffer();
                //System.out.println("Buffer " + node[i].getId() + " fails: " + node[i].getbufferFails());
                node[i].setBufferFails(0);
            }
            slot = 0;
        }
    }
    public static void initialValues(ArrayList<Integer> list, int num){
        for(int i=0; i<num; i++){
            list.add(i+1);
        }
    }
    public static int randomReceiver(int transiever){
        /*randomly selects a receiver from {1,2,3,4,5,6,7,8} while the transiever != receiver and returns 
         * the receiver. */
        int n; 
        int N=8;
        do {
            n = (int)(Math.random()*N);
        }while(n==transiever);
        return n;
    }
    public static void transmitionSchedule(ArrayList<Integer> channels, ArrayList<Integer> node, ArrayList<Integer> transmision){
        //The method essentially randomly selects a channel and receiver, removes them from their respective lists to avoid
        //duplicate selections, and updates the transmision list with the selected channel for the corresponding receiver. 
        Random random = new Random();
        int randomIndex = random.nextInt(channels.size()); // Retrieve the random channel
        Integer randomChannel = channels.get(randomIndex);
        channels.remove(randomIndex);

        randomIndex = random.nextInt(node.size());  // Retrieve the random transiever
        Integer randomNode = node.get(randomIndex);
        node.remove(randomIndex); 
        transmision.set(randomNode-1,randomChannel);
        //System.out.println("transiever: " + randomNode + " selected channel: " + randomChannel);
    }
    private static double calcAvgDelay(ArrayList<Packet> list) { //calculate average Delay 
        double avg;
        int sum=0;
        for(Packet p:list) {
            sum += p.getDelay();
        }
        avg = (double)sum/list.size();
        return avg;
    }
}

