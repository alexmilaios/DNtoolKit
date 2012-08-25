package dntoolkit;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import parser.Token;

public class JTreeMethods {

	
	public static String visitTree( JTree tree, String type) {
		String output = "";
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		output = visitAllNodes(root,0,type);
		return output;
	}

	private static String visitAllNodes(TreeNode node,int index, String type) {
		String result = "";
		if(index != 0) {
			result  = "\n" + type + " " ;
			StringTokenizer tokens = new StringTokenizer(node.toString(),"/");
			result += tokens.nextToken() + "(";
			int numOfArg =  Integer.parseInt(tokens.nextToken());
			for(int i = 0; i < numOfArg-1; i++) {
				result += "X"+(i+1)+",";
			}
			result += "X" + numOfArg + ").";
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				result += visitAllNodes(n,++index,type);
			}
		}
		return result;
	}
	
	public static String visitNodeTree( JTree tree) {
		String output = "";
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		output = visitAllNodeNodes(root,0);
		return output;
	}

	private static String visitAllNodeNodes(TreeNode node,int index) {
		String result = "";
		if(index != 0) {
			result  = "\nnode(" + node.toString() +").";
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				result += visitAllNodeNodes(n,++index);
			}
		}
		return result;
	}
	
	public static String visitLinkTree( JTree tree) {
		String output = "";
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		output = visitAllLinkNodes(root,0);
		return output;
	}

	private static String visitAllLinkNodes(TreeNode node,int index) {
		String result = "";
		if(index != 0) {
			result  = node.toString();
			result = "\n" + result.substring(0,result.length()-1) + ",0).";
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				result += visitAllLinkNodes(n,++index);
			}
		}
		return result;
	}
	
	public static String visitSimpleTree( JTree tree) {
		String output = "";
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		output = visitAllSimpleNodes(root,0);
		return output;
	}

	private static String visitAllSimpleNodes(TreeNode node,int index) {
		String result = "";
		if(index != 0) {
			result  = "\n" + node.toString();
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				result += visitAllSimpleNodes(n,++index);
			}
		}
		return result;
	}
	
	public static String visitSimpleTree2( JTree tree,String type) {
		String output = "";
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		output = visitAllSimpleNodes2(root,0,type);
		return output;
	}

	private static String visitAllSimpleNodes2(TreeNode node,int index,String type) {
		String result = "";
		if(index != 0) {
			if(type.equals("transport")){
				result  = "receive_"+(node.toString().split("/") [0]) + " ";
				result  += "send_"+(node.toString().split("/") [0]) + " ";
			}else
			result  = node.toString().split("/") [0] + " ";
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				result += visitAllSimpleNodes2(n,++index, type);
			}
		}
		return result;
	}
	
	public static String visitSimpleShowTree( JTree tree,String type) {
		String output = "";
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		output = visitAllSimpleShow(root,0,type);
		return output;
	}

	private static String visitAllSimpleShow(TreeNode node,int index, String type) {
		String result = "";
		if(index != 0) {
			StringTokenizer tokens = new StringTokenizer(node.toString(),"/");
			String name = tokens.nextToken();
			int args = Integer.parseInt(tokens.nextToken());
			if(type.equals("transport"))
				result  = "\n#show receive_" + name + "/"+ (args + 4)+ ".";
			else
				result  = "\n#show "+ name + "/"+ (args + 2)+ ".";
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				result += visitAllSimpleShow(n,++index,type);
			}
		}
		return result;
	}
	
	public static void visitDeleteLastElementTree( JTree tree,String target) {
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		TreeNode root = (TreeNode) model.getRoot();
		
		List<TreePath> paths = new ArrayList<TreePath>();
		
		visitAllDeleteNodes(root,0,target,tree,paths);
		Iterator<TreePath> itr = paths.iterator();
		while(itr.hasNext()) {
			TreePath path = itr.next();
			MutableTreeNode node = (MutableTreeNode) path.getLastPathComponent();
			model.removeNodeFromParent(node);
		}
		
	}

	private static void visitAllDeleteNodes(TreeNode node,int index, String target,JTree tree,List<TreePath> paths) {
		String result = "";
		if(index != 0) {
			StringTokenizer tokens = new StringTokenizer(node.toString());
			int lenght = tokens.countTokens();
			for(int i = 0; i < lenght; i++) {
				result = tokens.nextToken();
			}
			if(result.equals(target)){
				TreePath path = tree.getNextMatch(node.toString(), 0, Position.Bias.Forward);
				paths.add(path);
			}
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				visitAllDeleteNodes(n,++index,target,tree,paths);
			}
		}
	}
	
	public static void deleteLinksTree( JTree tree,String target,EastPanel east) {
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		TreeNode root = (TreeNode) model.getRoot();
		
		List<TreePath> paths = new ArrayList<TreePath>();
		
		deleteLinks(root,0,target,tree,paths);
		Iterator<TreePath> itr = paths.iterator();
		while(itr.hasNext()) {
			TreePath path = itr.next();
			MutableTreeNode node = (MutableTreeNode) path.getLastPathComponent();
			model.removeNodeFromParent(node);
			visitDeleteLastElementTree(east.initTranTree, node.toString());
		}
	}

	private static void deleteLinks(TreeNode node,int index, String target,JTree tree,List<TreePath> paths) {
		String result = "";
		if(index != 0) {
			StringTokenizer tokens = new StringTokenizer(node.toString(),"(");
			int lenght = tokens.countTokens();
			result = tokens.nextToken(); 
			StringTokenizer tokens2 = new StringTokenizer(tokens.nextToken(),",");
			String src = tokens2.nextToken();
			String dest = tokens2.nextToken();
			dest = dest.substring(0,dest.length()-1);
			if(src.equals(target) || dest.equals(target)){
				TreePath path = tree.getNextMatch(node.toString(), 0, Position.Bias.Forward);
				paths.add(path);
			}
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				deleteLinks(n,++index,target,tree,paths);
			}
		}
	}
	
	public static String visitTranTree( JTree tree) {
		String output = "";
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		output = visitAllTranNodes(root,0);
		return output;
	}

	private static String visitAllTranNodes(TreeNode node,int index) {
		String result = "";
		if(index != 0) {
			result  = "";
			StringTokenizer tokens = new StringTokenizer(node.toString()," ");
			StringTokenizer tokens2 = new StringTokenizer(tokens.nextToken(),"(");
			String nodeTarget = "";
			for(int i = 1; i < 5; i++)
				 nodeTarget = tokens.nextToken();
			StringTokenizer tokens3 = new StringTokenizer(nodeTarget, "(");
			String seconsPart = tokens3.nextToken();
			seconsPart = tokens3.nextToken();
			StringTokenizer tokens4 = new StringTokenizer(seconsPart,",");
			String src = tokens4.nextToken();
			String dest = tokens4.nextToken();
			dest = dest.substring(0,dest.length()-1);
			
			result = "\nsend_" + tokens2.nextToken() + "(" + src +",";
			String tmp = tokens2.nextToken();
			result += tmp.substring(0,tmp.length()-1) +"," + dest +  ",0).";
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				result += visitAllTranNodes(n,++index);
			}
		}
		return result;
	}
	
	public static String visitPerTree( JTree tree) {
		String output = "";
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		output = visitAllPerNodes(root,0);
		return output;
	}

	private static String visitAllPerNodes(TreeNode node,int index) {
		String result = "";
		if(index != 0) {
			result  = "";
			StringTokenizer tokens = new StringTokenizer(node.toString()," ");
			StringTokenizer tokens2 = new StringTokenizer(tokens.nextToken(),"(");
			String nodeTarget = "";
			for(int i = 1; i < 4; i++)
				 nodeTarget = tokens.nextToken();
			result = "\n" + tokens2.nextToken() + "(" + nodeTarget +",";
			String tmp = tokens2.nextToken();
			result += tmp.substring(0,tmp.length()-1) + ",0).";
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				result += visitAllPerNodes(n,++index);
			}
		}
		return result;
	}
	
	public static String visitInTree( JTree tree) {
		String output = "";
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		output = visitAllInNodes(root,0);
		return output;
	}

	private static String visitAllInNodes(TreeNode node,int index) {
		String result = "";
		if(index != 0) {
			result  = "\n";
			StringTokenizer tokens = new StringTokenizer(node.toString()," ");
			result += tokens.nextToken() +".";
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode)e.nextElement();
				result += visitAllInNodes(n,++index);
			}
		}
		return result;
	}
	
	private static boolean searchAllNodes(TreeNode node, String target) {

		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode) e.nextElement();
				if(n.toString().equals(target))
					return true;
			}
		}
		return false;
	}
	
	public static boolean isMember(JTree tree, String target){
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		return searchAllNodes(root,target);
	}
	
	private static boolean searchAllNodesInit(TreeNode node, String target,String nameIn) {

		if (node.getChildCount() >= 0) {
			for (Enumeration e=node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode) e.nextElement();
				
				String nodeTarget = "";
				StringTokenizer tokens = new StringTokenizer(n.toString()," ");
				String name = tokens.nextToken();
				for(int i = 1; i < 4; i++)
				 nodeTarget = tokens.nextToken();
				if(nodeTarget.equals(target) && name.equals(nameIn))
					return true;
			}
		}
		return false;
	}
	
	public static boolean isMemberInit(JTree tree, String target, String name){
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		return searchAllNodesInit(root,target,name);
	}
	
	public static void addNChildrenNode(JTree tree, String input, EastPanel eastPanel) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		int startRow = 0;
		String prefix = "Nodes";
		TreePath path = tree.getNextMatch(prefix, startRow, Position.Bias.Forward);
		MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

		MutableTreeNode newNode = new DefaultMutableTreeNode(input);

		model.insertNodeInto(newNode, node, node.getChildCount());
		eastPanel.src.addItem(input);
		eastPanel.dest.addItem(input);
		eastPanel.nodePer.addItem(input);
	}
	
	public static void addNChildrenLink(JTree tree, String input, EastPanel eastPanel) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		int startRow = 0;
		String prefix = "Links";
		TreePath path = tree.getNextMatch(prefix, startRow, Position.Bias.Forward);
		MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

		MutableTreeNode newNode = new DefaultMutableTreeNode(input);

		model.insertNodeInto(newNode, node, node.getChildCount());

		eastPanel.linkTran.addItem(input);
	}

	public static void addNChildren(JTree tree, String line , String root) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		int startRow = 0;
		String prefix = root;
		TreePath path = tree.getNextMatch(prefix, startRow, Position.Bias.Forward);
		MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

		MutableTreeNode newNode = new DefaultMutableTreeNode(line);

		model.insertNodeInto(newNode, node, node.getChildCount());
		
	}
}
