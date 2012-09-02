package graphics;

import graph.Connection;
import graph.Node;

import java.util.List;
import java.util.Vector;


import sort.SenderReceiverPairs;

public class ThreadedUniverse implements Runnable{

	List<List<Node>> levels;
	Vector<SenderReceiverPairs> messages; 
	List<Connection> connections;
	int numOfNodes;
	String [] persistenPredicates, transientPredicates,transportPredicates,nodes; 
	String trace;


	public ThreadedUniverse(List<List<Node>> levels, Vector<SenderReceiverPairs> messages, 
			List<Connection> connections, int numOfNodes,String [] persistenPredicates, 
			String [] transientPredicates, String [] transportPredicates,String trace, String[] nodes){

		this.levels = levels;
		this.messages  = messages;
		this.connections = connections;
		this.numOfNodes = numOfNodes;
		this.persistenPredicates = persistenPredicates;
		this.transientPredicates = transientPredicates;
		this.transportPredicates = transportPredicates;
		this.trace = trace;
		this.nodes = nodes;
	}

	@Override
	public void run() {
		new Universe(levels, messages, connections, numOfNodes, 
				persistenPredicates, transientPredicates, transportPredicates, trace,nodes);
	}

}
