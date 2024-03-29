/**
 * Created by Black Beast on 9/29/2014.
 1
 CAP4630/CAP5605 Introduction to Artificial Intelligence
 Fall 2014
 Assignment 1
 Ching-- Hua Chuan (c.chuan@unf.edu)
 Due: October 6 (Monday)
 Total points: 100 (120)
 Consider the problem of finding the shortest path between two points on a place that
 has convex polygonal obstacles as shown in the following figure. This is an idealization
 of the problem that a robot has to solve to navigate in a crowded environment.
 a. Suppose the state space consists of all positions (x, y) in the plane. How many
 states are there? How many paths are there to the goal? [5 points]
 b. Explain briefly why the shortest path from one polygon vertex to any other in the
 scene must consist of straight-­line segments joining some of the vertices of the
 polygons. Define a good state space now. How large is this state space? [10
 points]
 c. Define the necessary functions to implement the search problem, including an
 ACTIONS function that takes a vertex as input and returns a set of vectors, each
 of which maps the current vertex to one of the vertices that can be reached in a
 2
 straight line. (Do not forget the neighbors on the same polygon.) Use the straight-­
 line distance for the heuristic function. [10 points]
 d. Based on the vertices and edges of the polygonal obstacles shown above,
 manually calculate the shortest path from the source (1, 3) to the destination (34,
 19). Show every step of your calculation in your submission. [30 points]
 e. Write a program to implement A* search to find the shortest path from the start
 point to the goal. Make sure your program generates the shortest path on any
 map, not just this particular one. [45 points]
 f. [Graduate Students Only] Supposed the robot is now navigating in the
 following environment: the percept will be a list of the positions, relative to the
 agent, of the visible vertices. The percept does not include the position of the
 robot. The robot must learn its own position from the map. [20 points]
 Discuss how this environment changes the problem definition, and how to
 search for the shortest path.
 Details about the program:
 • The name of your program: aixxx.java (xxx are the last 3 digits of your n-­number).
 • Input: a text file containing the coordinates of start point, goal point, and vertices
 of all polygons.
 For example, an input text file map1.txt contains:
 1, 3 ! (x, y) of the start point
 34, 19 ! (x, y) of the goal point
 0, 14; 6, 19; 9, 15; 7, 8; 1, 9 ! vertices of the 1st polygon, separating by semicolons
 2, 6; 17, 6; 17, 1; 2, 1 ! vertices of the 2nd polygon, separating by semicolons
 • Make sure your program can be compiled using the following command on
 osprey.unf.edu:
 javac aixxx.java
 • Your program will be tested on osprey as follows:
 java aixxx input_map_file.txt
 input_map_file: a text file similar to map1.txt
 • Output: a sequence of coordinates showing the shortest path from the start point
 to the goal point.
 • Your program will be graded and tested using another map.
 3
 Submission
 Materials that you are required to submit for this assignment include:
 • aixxx.java
 • a Microsoft word or pdf document containing answers to the questions in a, b, c,
 d for undergraduate and a, b, c, d, f for graduate students.
 All the files must be submitted through blackboard.unf.edu by the end of date on the
 due date.
 Note
 You are allowed to write the program in a programming language other than Java, but
 you will be asked to demonstrate the program in person as part of your submission.


 import java.awt.geom.Line2D;

 public class hw1demo
 {

 public static void main(String[] args)
 {

 // line1: from (2, 6) to (12, 15)
 Line2D line1 = new Line2D.Double(2, 6, 12, 15);
 // line2: from (1, 9) to (7, 8)
 Line2D line2 = new Line2D.Double(1, 9, 7, 8);

 if(line1.intersectsLine(line2))
 System.out.println("line1 and line2 have an intersection.");
 else
 System.out.println("line1 and line2 have no intersections.");

 Line2D line3 = new Line2D.Double(1, 3, 2, 4);
 Line2D line4 = new Line2D.Double(1, 2, 2, 3);

 if(line3.intersectsLine(line4))
 System.out.println("line3 and line4 have an intersection.");
 else
 System.out.println("line3 and line4 have no intersections.");

 Line2D line5 = new Line2D.Double(1, 3, 2, 4);
 Line2D line6 = new Line2D.Double(3, 6, 2, 4);

 if(line5.intersectsLine(line6))
 System.out.println("line5 and line6 have an intersection.");
 else
 System.out.println("line5 and line6 have no intersections.");

 /* Note: if line5 represents the robot's next traveling path and
 line6 is an edge of a polygonal obstacle, the robot is able to travel
 from its current place (1, 3) to the destination (2, 4) even line5
 and line6 intersect at (2, 4)

 1, 3
 34, 19
 0, 14; 6, 19; 9, 15; 7, 8; 1, 9
 2, 6; 17, 6; 17, 1; 2, 1
 12, 15; 14, 8; 10, 8
 14, 19; 18, 20; 20, 17; 14, 13
 18, 10; 23, 6; 19, 3
 22, 19; 28, 19; 28, 9; 22, 9
 25, 6; 29, 8; 31, 6; 31, 2; 28, 1; 25, 2
 31, 19; 34, 16; 32, 8; 29, 17



 }

 }


 */

import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.geom.Line2D;

public class A_search {


    String[] inputArray;
    String[][] inputFile;
    String targetFile;
    String[] myargs;

    //ArrayList<String> ltorgs = new ArrayList<String>();
    String output="";

    ArrayList<Node> myNodes = new ArrayList<Node>();
    ArrayList<Line2D.Double> myEdges = new ArrayList<Line2D.Double>();
    ArrayList<Polygon> myPolygons = new ArrayList<Polygon>();

    //todo Constructor to pass the

    public A_search (String[] args)
    {
        myargs=args;
    }

    public static void main (String[] args)
    {
        A_search mySearch = new A_search(args);
        //Load the file
        mySearch.loadFile();
        mySearch.getNodes();
        mySearch.check_adjacency();
        Node finish=mySearch.myNodes.get(1);
        /*
        for (Node everyNode: mySearch.myNodes)
        {
            everyNode.distance=Math.sqrt((everyNode.x-finish.x)*(everyNode.x-finish.x)+(everyNode.y-finish.y)*(everyNode.y-finish.y));
            
            
      
        }

        for (Node everyNode:mySearch.myNodes)
        {
         System.out.println("("+everyNode.x+","+everyNode.y+") Distance:"+everyNode.distance);
         System.out.println("Neighbors");
         for (Node everyAjacent:everyNode.neighbors)
            {
               System.out.println("("+everyAjacent.x+","+everyAjacent.y+") Distance:"+everyNode.getDistance(everyAjacent));    
            }
         System.out.println("\n");   
        
        }
        */

        mySearch.aStar();
        //mySearch.testComprable();

        //A* search


    }
    public void testComprable()
    {
        Collections.sort(myNodes, new Comparator<Node>(){
            @Override
            public int compare(Node node1, Node node2)
            {
                return node1.compareTo(node2);

            }
        });
        System.out.println("Ordered Nodes:");
        for(int i=0; i<myNodes.size(); i++)
        {
            System.out.println(myNodes.get(i).toString()+ "\nFscore:"+myNodes.get(i).f_score+"\n Camefrom:"+myNodes.get(i).cameFrom.toShortString()+"\n\n");
        }

    }

    public void aStar()
    {
        Node start=myNodes.get(0);
        Node finish=myNodes.get(1);
        Node current;
        ArrayList<Node> closedset = new ArrayList<Node>();
        List<Node> openset = new ArrayList<Node>();
        ArrayList<Node> camefrom;
        start.g_score =0;
        Double fscore=start.g_score +start.distance;
        Double gscore=0.0;
        Double tenGscore;
        for (Node everyNode: myNodes)
        {
            everyNode.distance=Math.sqrt((everyNode.x-finish.x)*(everyNode.x-finish.x)+(everyNode.y-finish.y)*(everyNode.y-finish.y));

        }
        current=start;
        openset.add(start);
        while (!openset.isEmpty()) {
            Collections.sort(openset, new Comparator<Node>(){
                @Override
                public int compare(Node node1, Node node2)
                {
                    return node1.compareTo(node2);

                }
            });

            current=openset.get(0);
            System.out.println(" Mt Bond is considering "+current.toString()+"It's score is: "+current.f_score);

            if (current==finish)
            {
                reconstruct_path();
                return;
            }


            openset.remove(current);
            closedset.add(current);
            for (Node neighbor: current.neighbors)
            {
                if (closedset.contains(neighbor))continue;
                tenGscore=current.g_score+current.getDistance(neighbor);

                if (!openset.contains(neighbor)||tenGscore<current.g_score)
                {

                    neighbor.cameFrom=current;
                    neighbor.g_score=tenGscore;
                    neighbor.f_score=neighbor.g_score+neighbor.distance;
                    if (!openset.contains(neighbor))
                    {
                        openset.add(neighbor);
                        System.out.println("Mr Bond went from "+current.toString()+" to "+neighbor.toString()+"\n Tenative score is:"+tenGscore);

                    }
                }

            }
        }
        //reconstruct_path();
    }

    public void reconstruct_path()
    {
        Node current=myNodes.get(1);
        Node start=myNodes.get(0);
        ArrayList<Node> reverse_path= new ArrayList<Node>();
        ArrayList<Node> forward_path= new ArrayList<Node>();
        while (current.x!=start.x&&current.y!=start.y)
        {
            reverse_path.add(current);
            current=current.cameFrom;
        }
        reverse_path.add(current);

        for (int i=reverse_path.size()-1; i>=0; i--)
        {
            forward_path.add(reverse_path.get(i));
        }
        System.out.println("the path is:");
        for (Node step:forward_path)
        {
            System.out.print(step.toString());
        }

    }

    public class Node implements Comparable{
        int x;
        int y;
        double distance;
        double g_score;
        double f_score;
        ArrayList<Node> neighbors;
        Node cameFrom;

        public Node(int x_coordinate, int y_coordinate) {
            x=x_coordinate;
            y=y_coordinate;
            neighbors = new ArrayList<Node>();
            f_score=0;
            cameFrom=this;
        }

        public int compareTo(Object other)
        {

            if (this.f_score == ((Node) other).f_score)
                return 0;
            else if (this.f_score > ((Node) other).f_score)
                return 1;
            else
                return -1;

        }

        public boolean is_neighbor(Node othernode)
        {
            boolean neighbor=false;
            for (Node test: neighbors)
            {
                if (test.x==othernode.x&&test.y==othernode.y)
                    neighbor=true;
            }
            return neighbor;
        }

        public Double getDistance(Node othernode)
        {
            Double distance=Math.sqrt((this.x-othernode.x)*(this.x-othernode.x)+(this.y-othernode.y)*(this.y-othernode.y));
            return distance;
        }


        @Override
        public String toString()
        {
            String output=new String("");
            output+=new String("Node: ("+this.x+","+this.y+")\n)");
            output+=new String("Neighors:");
            for (Node myNeighbor: this.neighbors)
            {
                output+=new String("(,"+myNeighbor.x+","+myNeighbor.y+")\n)");
                //output+=new String("\n");
            }
            output+="\n\n";
            return (output);
        }
        public String toShortString()
        {
            String output=new String("");
            output+=new String("Node: ("+this.x+","+this.y+")\n)");
            return (output);
        }

    }

    public class Lineprinter
    {
        public String printline(Line2D.Double eachLine)
        {
            String output;
            output=("("+eachLine.getX1()+","+eachLine.getY1()+") ("+eachLine.getX2()+","+eachLine.getY2()+")");
            return output;
        }

        public boolean are_same_line(Line2D.Double linea,Line2D.Double lineb){
            Boolean areSame=false;
            if (linea.getX1()==lineb.getX1()&&linea.getY1()==lineb.getY1()&&linea.getX2()==lineb.getX2()&&linea.getY2()==lineb.getY2()) areSame=true;
            if (linea.getX2()==lineb.getX1()&&linea.getY2()==lineb.getY1()&&linea.getX1()==lineb.getX2()&&linea.getY1()==lineb.getY2()) areSame=true;
            return areSame;
        }

        public boolean share_endpoint (Line2D.Double linea, Line2D.Double lineb){
            Boolean shareEndpoint=false;
            if (linea.getX1()==lineb.getX1()&&linea.getY1()==lineb.getY1()) shareEndpoint=true;
            if (linea.getX2()==lineb.getX1()&&linea.getY2()==lineb.getY1()) shareEndpoint=true;
            if (linea.getX1()==lineb.getX2()&&linea.getY1()==lineb.getY2()) shareEndpoint=true;
            if (linea.getX2()==lineb.getX2()&&linea.getY2()==lineb.getY2()) shareEndpoint=true;
            return shareEndpoint;
        }
    }

    public class Polygon {
        ArrayList<Node> poly_nodes;
        ArrayList<Line2D.Double> blockinglines;
        //creates a set of lines
        public Polygon (ArrayList<Node> nodes) {

            blockinglines= new ArrayList<Line2D.Double>();
            poly_nodes= new  ArrayList<Node>();

            for (Node thisnode : nodes)
            {
                for (Node othernode:nodes)
                {
                    //check if same node
                    if (thisnode.x==othernode.x&&thisnode.y==othernode.y)
                        continue;
                    else if (thisnode.is_neighbor(othernode))
                        continue;
                    else {
                        blockinglines.add(new Line2D.Double(thisnode.x,thisnode.y,othernode.x, othernode.y));
                    }

                    //check if adjacent node

                    //add a line to the node


                }
            }

            for (Node thisnode: nodes)
            {
                poly_nodes.add(thisnode);
            }
        }

        @Override
        public String toString()
        {
            String output=new String("");
            output+=("Polygon: \n");
            output+=("Node: ");
            for(Node thisnode:poly_nodes) {
                output+=("(,"+thisnode.x+","+thisnode.y+")\n)");
            }
            for (Line2D.Double blockingLine: blockinglines)
            {
                output+=("Blocking Lines:");
                output+=("("+blockingLine.getX1()+","+blockingLine.getY1()+") ("+blockingLine.getX2()+","+blockingLine.getY2()+") \n");
            }
            return(output);
        }

    }

    public void testMap()
    {
        for (Node eachNode: myNodes)
        {System.out.println(eachNode.toString());}
        System.out.println("\n");
        for (Polygon eachPolygon: myPolygons)
        {System.out.println(eachPolygon.toString());}
        System.out.println("\n Edges: \n");

        for (Line2D.Double eachLine: myEdges)
        {
            System.out.println("("+eachLine.getX1()+","+eachLine.getY1()+") ("+eachLine.getX2()+","+eachLine.getY2()+")");
        }

    }

    public void check_adjacency()
    {
        Boolean adjacent=true;
        Line2D.Double thispath;
        Lineprinter printer = new Lineprinter();
        for (Node thisNode:myNodes)
        {
            adjacent=true;
            for (Node anotherNode: myNodes)
            {
                adjacent=true;
                if (thisNode.x==anotherNode.x&&thisNode.y==anotherNode.y)continue;
                if (thisNode.is_neighbor(anotherNode))continue;
                if (!adjacent)continue;

                thispath=new Line2D.Double(thisNode.x,thisNode.y,anotherNode.x,anotherNode.y);
                for (Line2D.Double eachEdges:myEdges)
                {
                    //If the points are the same then the lines don't cross
                    if (thispath.getX1()==eachEdges.getX1()&&thispath.getY1()==eachEdges.getY1())continue;
                    if (thispath.getX1()==eachEdges.getX2()&&thispath.getY1()==eachEdges.getY2())continue;
                    if (thispath.getX2()==eachEdges.getX1()&&thispath.getY2()==eachEdges.getY1())continue;
                    if (thispath.getX2()==eachEdges.getX2()&&thispath.getY2()==eachEdges.getY2())continue;

                    if (thispath.intersectsLine(eachEdges))
                    {
                        adjacent=false;
                        continue;
                    }
                }
                if (anotherNode.x==0 && anotherNode.y==14 && thisNode.x==1 &&thisNode.y==3)
                {
                    System.out.println("1, 3 adjacency to 0,14 test: ."+adjacent+" after edge check\n");
                }
                for (Polygon eachPolygon:myPolygons)
                {

                    if (!adjacent)continue;



                    for (Line2D.Double eachBlockingEdge: eachPolygon.blockinglines) {
                        boolean sharepoint=false;

                        if (printer.are_same_line(eachBlockingEdge,thispath)) {
                            adjacent=false;
                            continue;
                        }
                        if (printer.share_endpoint(eachBlockingEdge,thispath)){
                            sharepoint=true;
                            continue;
                        }
                        if (!sharepoint) {
                            if (thispath.intersectsLine(eachBlockingEdge)) {
                                if (anotherNode.x == 0 && anotherNode.y == 14 && thisNode.x == 1 && thisNode.y == 3) {
                                    System.out.println("1, 3 adjacency to 0,14 test:  failed on: " + printer.printline(thispath) + " and " + printer.printline(eachBlockingEdge) + " in after polygon check\n");
                                }
                                adjacent = false;
                                continue;
                            }
                        }
                    }

                }
                if (anotherNode.x==0 && anotherNode.y==14 && thisNode.x==1 &&thisNode.y==3)
                {
                    System.out.println("1, 3 adjacency to 0,14 test: ."+adjacent+" after polygon check\n");
                }

                if (adjacent)
                {
                    thisNode.neighbors.add(anotherNode);
                }

            }
        }
    }


    public void loadFile()
    {
        // This block of code converts the input filestream into an array of strings.
        targetFile=myargs[0];

        try
        {
            FileInputStream inputStream = new FileInputStream(targetFile);
            DataInputStream dataInput = new DataInputStream(inputStream);
            BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(dataInput));
            String inputString;

            ArrayList<String> inputList = new ArrayList<String>();

            while ((inputString = inputBuffer.readLine()) != null)
            {
                //inputString = inputString.trim();
                if ((inputString.length()!=0))
                {
                    inputList.add(inputString.toUpperCase());
                }
            }
            inputArray = (String[]) inputList.toArray(new String[inputList.size()]);
            inputFile = new String[inputArray.length][];
            for (int i=0; i<inputArray.length; i++)
            {
                inputFile[i]=inputArray[i].split("\\s+");
                // System.out.print("Split a line");
            }

            //int pants=0;
        }
        catch (Exception e)
        {
            // Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void getNodes()
    {
        Node start=null;
        Node end;
        Node last;
        Node now;
        ArrayList<Node> this_polygon = new ArrayList<Node>();
        // i is rows
        for (int i=0; i<inputFile.length; i++) {
            now = null;
            last = null;

            //todo  first and last line will be single node lines: need to set those to
            for (int j = 0; j < inputFile[i].length; j += 2) {
                // all  even 0,2, 4 ... indexed strings end in comma


                //inputFile[i][j] = inputFile[i][j].substring(0, inputFile[i][j].length() - 1);  // this line removes appended commas

                if (inputFile[i][j + 1].charAt(inputFile[i][j + 1].length() - 1) == ';') {
                    inputFile[i][j + 1] = inputFile[i][j + 1].substring(0, inputFile[i][j + 1].length() - 1);
                }
                if (inputFile[i][j + 1].charAt(inputFile[i][j + 1].length() - 1) == ',') {
                    inputFile[i][j + 1] = inputFile[i][j + 1].substring(0, inputFile[i][j + 1].length() - 1);
                }
                if (inputFile[i][j].charAt(inputFile[i][j].length() - 1) == ';') {
                    inputFile[i][j] = inputFile[i][j].substring(0, inputFile[i][j].length() - 1);
                }
                if (inputFile[i][j].charAt(inputFile[i][j].length() - 1) == ',') {
                    inputFile[i][j] = inputFile[i][j].substring(0, inputFile[i][j].length() - 1);
                }
                last = now;

                now = new Node(Integer.parseInt(inputFile[i][j]), Integer.parseInt(inputFile[i][j + 1]));
                myNodes.add(now);
                if (last != null&&i>1) {
                    now.neighbors.add(last);
                    last.neighbors.add(now);
                    myEdges.add(new Line2D.Double(now.x, now.y, last.x, last.y));
                }
                if (j == 0) {
                    start=now;
                }
                if (j == inputFile[i].length - 1) {
                    last=now;
                    last.neighbors.add(start);
                    start.neighbors.add(now);
                    myEdges.add(new Line2D.Double(start.x, start.y, last.x, last.y));
                }
                this_polygon.add(now);


            }
            //System.out.print("\n");
            if (i>1)
            {
                myPolygons.add(new Polygon(this_polygon));
            }
            this_polygon.clear();
        }
    }

   /* public void test()
    {

        for (int i=0; i<inputFile.length; i++)
        {
            for (int j=0; j<inputFile[i].length; j+=2)
            {
                System.out.print(inputFile[i][j]);
            }
            System.out.print("\n");


        }

    }
    */

}