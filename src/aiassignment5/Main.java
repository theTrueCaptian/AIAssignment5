package aiassignment5;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * Maeda Hanafi
 * Assignment 5
 * CSC481 AI
 */
public class Main {
    public Main(){
        start();
    }

    public static void main(String[] args) {
        new Main();
    }
    Node root1;
    Node root2;
   int index = 0;
    public void start(){
        //ask user to enter an expression in infix notation
        System.out.println("Enter in the expression in infix notation:");
        String expression = new Scanner(System.in).nextLine();
        root1 = createTree(expression);
        //ask user to enter an expression in infix notation
        System.out.println("Enter in the expression in infix notation:");
        String expression2 = new Scanner(System.in).nextLine();
        root2 = createTree(expression2);

        System.out.println("Tree 1:");
        setIndexAndParent(root1, 0);
        int num1 = index+1;

        System.out.println("Tree 2:");
        index = 0;
        setIndexAndParent(root2, 0);
        int num2 = index+1;
        //crossover tree
       GPcrossover(root1, num1, root2, num2);
    }
    public Node GPcrossover(Node inTree1, int numberOfNodes1, Node inTree2, int numberOfNodes2){
        Random rand = new Random();
        int rand1 = rand.nextInt(numberOfNodes1-2)+1;
        int rand2 = rand.nextInt(numberOfNodes2-2)+1;
        System.out.println("Random index for 1:"+rand1);
        System.out.println("Random index for 2:"+rand2);
        
        //subtrees based on indexes
        this.findLastIndices(inTree1, rand1, 'o');
        //System.out.println("lastIndex:"+this.lastIndex);
        Node subTree1 = this.getSubtree(inTree1, rand1, this.lastIndex);
        System.out.println("1st subtree:");
        this.preorder(subTree1, 0);

        lastIndex = -1;
        this.findLastIndices(inTree2, rand2,'o');
        //System.out.println("lastIndex:"+this.lastIndex);
        Node subTree2 = this.getSubtree(inTree2, rand2, this.lastIndex);
        System.out.println("2st subtree:");
        this.preorder(subTree2, 0);

        System.out.println("Crossover-----------------------------------------------------------");
        inTree1 = crossOver(inTree1, subTree2, rand1);
        System.out.println("New tree 1:");
        this.preorder(inTree1, 0);
        
        inTree1 = crossOver(inTree2, subTree1, rand2);
        System.out.println("New tree 2:");
        this.preorder(inTree1, 0);
        
        return null;
    }

    int lastIndex = -1;
    public void findLastIndices(Node tree, int index, char dir){
        if(tree!=null){
            if( lastIndex==-1){
                if(tree.index==index && (tree.getChild1()==null && tree.getChild2()==null)){//copying leaf node?
                    lastIndex = tree.index;
                    return;
                }
                if(tree.index>index && (tree.getChild1()==null && tree.getChild2()==null)&& dir=='r'){
                    lastIndex = tree.index;
                    return;
                }
            }
                findLastIndices(tree.getChild1(), index, 'l');
                findLastIndices(tree.getChild2(), index, 'r');
            
        }
        return;
    }
    public Node getSubtree(Node tree, int index, int lastIndex){
       Node cpyTree = null;
        if(tree!=null){
            //System.out.println(tree.getData()+": "+"tree index: "+tree.index+", index: "+index);

            if(index<=tree.index && tree.index<=lastIndex){
                //System.out.println(tree.getData());
                cpyTree = new Node(tree.getData(), getSubtree(tree.getChild1(), index, lastIndex), getSubtree(tree.getChild2(), index, lastIndex));
                
                return cpyTree;

            }else{

                Node traverse1 = getSubtree(tree.getChild1(), index, lastIndex);
                Node traverse2 = getSubtree(tree.getChild2(), index, lastIndex);
                if(traverse1!=null && traverse2==null)
                    return traverse1;
                if(traverse2!=null && traverse1==null)
                    return traverse2;
            }

        }

        return null;
    }
 
     public void setIndexAndParent(Node node, int depth) {
        if(node!=null ){
            node.index = index++;
            if(node.getChild1()!=null)
                node.getChild1().setParent(node);
            if(node.getChild2()!=null)
                node.getChild2().setParent(node);
            
            System.out.println("depth:"+depth+" data:"+node.getData()+" index:"+node.index);
            setIndexAndParent(node.getChild1(), depth+1);
            setIndexAndParent(node.getChild2(), depth+1);
        }
        return;
    }
     public void preorder(Node node, int depth) {
        if(node!=null ){
            System.out.println("depth:"+depth+" data:"+node.getData()+" ");
            preorder(node.getChild1(), depth+1);
            preorder(node.getChild2(), depth+1);
        }
        return;
    }
     public Node crossOver(Node inTree, Node subTree, int index){
        if(inTree!=null){
            //System.out.println(inTree.getData()+": "+"tree index: "+inTree.index+", index: "+index);

            if(index==inTree.index){
                //System.out.println(inTree.getData());
                return subTree;
            }else{
                Node traverse1 = crossOver(inTree.getChild1(), subTree, index);
                Node traverse2 = crossOver(inTree.getChild2(), subTree, index);
                inTree.setChild1(traverse1);
                inTree.setChild2(traverse2);
            }
        }
        return inTree;
     }
    
    public Node createTree(String expression){
        //find root first
        String left = "";
        String operator = "";
        String right = "";
        boolean foundRoot = false;
        Node root = null;
        int parantheses = 0;
        //System.out.println("-------------------------------------------------operate on "+expression);
        if(expression.length()==1){
            //System.out.println("CHILD NODE:"+expression+";");
            return new Node(expression.charAt(0), null, null);
        }else{
            //go through each char in expression
            int i=0;
            while(i<expression.length()){
                if(expression.charAt(i)=='('){
                    parantheses++;
                    if(!foundRoot){
                        left = left+expression.charAt(i)+"";
                        //System.out.println("found a ( placed it in left");
                    }else if(foundRoot){
                        right = right+expression.charAt(i);
                        //System.out.println("found a ( placed it in right");
                    }
                }else if(expression.charAt(i)==')'){
                    parantheses--;
                    if(!foundRoot){ //not the root. add it to the left string
                        left = left+expression.charAt(i)+"";
                        //System.out.println("found a ) placed it in left");
                    }else if(foundRoot){
                        right = right+expression.charAt(i);
                        //System.out.println("found a ) placed it in right");
                    }
                }else if((parantheses==0 ) && !foundRoot && (expression.charAt(i)=='+'|| expression.charAt(i)=='-' || expression.charAt(i)=='*' || expression.charAt(i)=='/' || expression.charAt(i)=='e')){
                    //found root
                    operator = expression.charAt(i)+"";
                    //System.out.println("Root:"+expression.charAt(i));
                    foundRoot = true;
                }else if((parantheses==0) && !foundRoot && (expression.charAt(i)=='s' && expression.charAt(i+1)=='i' && expression.charAt(i+2)=='n')){
                    operator = expression.charAt(i) + expression.charAt(i+1) +expression.charAt(i+2)+"";
                    operator = expression.charAt(i)+"";
                    //System.out.println("Root:"+expression.charAt(i));
                    foundRoot = true;
                    i=i+2;
                }else if(!foundRoot){ //not the root. add it to the left string
                    left = left+expression.charAt(i)+"";
                    //System.out.println("left:"+left);
                }else if(foundRoot){
                    right = right+expression.charAt(i);
                    //System.out.println("right:"+right);
                }
                i++;
            }
            //System.out.println("left:"+left);
            //System.out.println("right:"+right);

            left = cleanString(left);
            right = cleanString(right);
            //System.out.println("Operate on "+left);
            //System.out.println("Operate on "+right);
            root = new Node(operator.charAt(0), createTree(left), createTree(right));
            return root;
        }

    }
    public String cleanString(String inString){
        String newString="";
        for(int i=0;i<inString.length(); i++){
            if(inString.charAt(i)!=' '){
                newString+=inString.charAt(i);
            }
        }
        //System.out.println("sub clean string:"+newString);
        if(newString.charAt(0)=='(' && newString.charAt(newString.length()-1)==')'){
            newString = newString.substring(1, newString.length()-1);
        }

        //System.out.println("clean string:"+newString);
        return newString;
    }
}
