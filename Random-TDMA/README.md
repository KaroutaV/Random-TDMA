## Random TDMA
Implemantation of a WDM transmissive star network, in which each node has one tunable transmitter with limited tuning capability and multiple ked receivers

## Protocol description

Each node follows a slot transmission schedule trans, with trans[i] determining the channel on wich node i will be allowed to transmit. if node i is not scheduled for transmission, trans[i] = 0.
At the beginning of each slot, a busy node i with trans[i] = k > 0 is given permission to transmit on channel k to any of the nodes that receive on channel k. 
Node i can choose from its buffer a packet destined for one of the two nodes listening to the channel.

## References 
Ganz, Aura, and Zahava Koren. "WDM passive star-protocols and performance analysis." IEEE INFCOM'91-Communications Societies Proceedings. IEEE Computer Society, 1991.
