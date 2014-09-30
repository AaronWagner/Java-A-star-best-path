/**
 * Created by Black Beast on 9/29/2014.
 1
 CAP4630/CAP5605 Introduction to Artificial Intelligence
 Fall 2014
 Assignment 1
 Ching-­‐‑Hua Chuan (c.chuan@unf.edu)
 Due: October 6 (Monday)
 Total points: 100 (120)
 Consider the problem of finding the shortest path between two points on a place that
 has convex polygonal obstacles as shown in the following figure. This is an idealization
 of the problem that a robot has to solve to navigate in a crowded environment.
 a. Suppose the state space consists of all positions (x, y) in the plane. How many
 states are there? How many paths are there to the goal? [5 points]
 b. Explain briefly why the shortest path from one polygon vertex to any other in the
 scene must consist of straight-­‐‑line segments joining some of the vertices of the
 polygons. Define a good state space now. How large is this state space? [10
 points]
 c. Define the necessary functions to implement the search problem, including an
 ACTIONS function that takes a vertex as input and returns a set of vectors, each
 of which maps the current vertex to one of the vertices that can be reached in a
 2
 straight line. (Do not forget the neighbors on the same polygon.) Use the straight-­‐‑
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
 • The name of your program: aixxx.java (xxx are the last 3 digits of your n-­‐‑number).
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

import javax.swing.JOptionPane;
import java.util.Scanner;
import java.lang.*;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.regex.*;

public class A_search {
    String[] inputArray;
    String[][] inputFile;
    String targetFile;
    String[] myargs;

    //ArrayList<String> ltorgs = new ArrayList<String>();
    String output="";

    ArrayList<Node> myNodes;

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
        mySearch.test();
        //Load the nodes
        //generate the adjacency map
        //A* search
    }

    public class Node {
        int x;
        int y;
        double distance;
        ArrayList<Nodes> neigbors;

        public Node(int x_coordinate, int y_coordinate) {
            x=x_coordinate;
            y=y_coordinate;
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
        Node start;
        Node end;
        Node last;
        Node now;

        for (int i=0; i<inputFile.length; i++)
        {
            now=null;
            last=null;
            for (int j=0; j<inputFile[i].length; j+=2)
            {
                // all  even 0,2, 4 ... indexed strings end in comma
                inputFile[i][j]=inputFile[i][j].substring(0,inputFile[i][j].length()-2);  // this line removes appended commas

                if (inputFile[i][j+1].charAt(inputFile[i][j+1].length()-1)==';')
                {
                    inputFile[i][j+1]=inputFile[i][j+1].substring(0,inputFile[i][j+1].length()-2);
                }
                last=now;
                now=new Node(Integer.parseInt(inputFile[i][j]),Integer.parseInt(inputFile[i][j+1]) )
                if (last!=null)
                    {
                        now.neigbors.add(last);
                        last.neigbors.add(now);
                    }
                if (j==0)
                    {
                        now=start;
                    }
                if (j==inputFile[i].length-1)
                    {
                        now=last;
                        last.neigbors.add(start);
                        start.neigbors.add(now);
                    }
                myNodes.add(now);

            }
            System.out.print("\n");
    }
    public void test()
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


}
