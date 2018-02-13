
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Standard libraries.
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import sql.*;
import dbengine.*;


public class SQL
{
    static public void main(String argv[])
        {

            System.out.print("SQL> ");
            do
            {
                SQLNode result = null;
                SQLNode tree;
    		String input = readInput().trim();
		input +=";";
   		if (input.equals("exit;;") | input.equals("exit;"))
                    //Handles both cases. Just to be safe.
                    break;
    		        try
                {
                    StringReader reader = new StringReader(input);
                    parser p = new parser(new Lexer(reader));
                    //System.out.println("No syntax error");
                    result = (SQLNode) p.parse().value;
                    
    		        }
                catch (Exception e)
                {
                    System.out.println("Syntax Error");
                    e.printStackTrace();
                }
                if(result != null)
                {
                    Database database = generateDatabase();
                    testDatabase(database);
                    doubleLink(result); //Give us some ease of traversal.
                    System.out.println("No Syntax Error");
                    boolean[] flags = SemanticAnalysis(database, result);
                    if(flags[0])    System.out.print("Nested - ");
                    else    System.out.print("Not Nested - ");
                    if(flags[1])    System.out.println("Correlated.");
                    else    System.out.println("Not Correlated.");
                }
            }
            while (true);
	}

    static String readInput()
        {
            try
            {
    		StringBuffer buffer = new StringBuffer();
   		System.out.flush();
    		int c = System.in.read();
    		while(c != -1)
                {
                    if (c != '\n')
       			buffer.append((char)c);
                    else
                    {
                        buffer.append(" ");
        		System.out.print("SQL> ");
        		System.out.flush();
                    }

                    c = System.in.read();

                    if (c == ';')
                    {
                        System.out.flush();
        		break;
                    }
                }

    		return buffer.toString().trim();

            }

            catch (IOException e)
            {
                 return "";
            }
        }

    static boolean[] SemanticAnalysis(Database db, SQLNode tree)
    {
        //SQLNode
        columnCheck(db, tree);
        relationsCheck(db, tree);
        boolean[] flags = correlationCheck(tree);
        //whereNode
        for(WhereNode node: tree.whereNodes)
        {
            ambiguityCheck(node);
            if(node.subQuery != null) //Base Case
                SemanticAnalysis(db, node.subQuery); //Check to see if exists.
        }
        
        return flags;
    }

    static Database generateDatabase()
    {
        String dir = "db\\";
        Database relation = new Database();
        relation.initializeDatabase(dir);
        return relation;
    }
    
    //Singular check.
    static boolean ambiguityCheck(WhereNode node)
    {
        if(node.leftOperandType.equals("col") && node.rightOperandType.equals("col"))
        {//Both = Column check.
            if(node.leftOperandName.equals(node.rightOperandName))
            {//Both = Name check.
                if(node.leftOperandPrefix.equals("") || node.rightOperandPrefix.equals(""))
                {//Either = Blank check.
                    System.out.println("Semantic Error - " + node.rightOperandName + " is ambigious.");
                    return true;
                }
                else if(node.leftOperandPrefix.equals(node.rightOperandPrefix))
                {//Both = Prefix check.
                    System.out.println("Semantic Error - " + node.rightOperandPrefix + "." + node.rightOperandName + " is ambigious.");
                    return true;
                }
            }
        }

        return false;  
    }
    
    //Branch check.
    static boolean relationsCheck(Database db, SQLNode node)
    {
        for(String[] relation: node.relations)
        {   //Checks to see if relation exists in database. 
            if(db.relationExists(relation[0]) == false){}
                System.out.println("Semantic Error - Relation " + relation[0] + " Not Defined.");
        }
        
        return false;
    }
    
    //Branch Check
    //Checks to see if column exists in database.
    static boolean columnCheck(Database db, SQLNode node)
    {
        //Direct check.
        
        for(Column col: node.columns)
        {   
                //Checks all relations for this column.
                for(String[] relation: node.relations)
                {            //Checks to see if relation exists in database.
                    if(db.relationExists(relation[0]) == true)
                    {   //Gets relation.
                        Relation table = db.getRelation(relation[0]);
                        //Checks to see if a column matches in this relation.
                        if(table.attributeExists(col.name))
                            return true;
                    }
                }
                System.out.println("Semantic Error - Column " + col.name + " Not Defined.");
        }
        
        return false;
    }

    //Branch Check. [nest,correlate]
    static boolean[] correlationCheck(SQLNode node)
    {
        boolean[] flags = new boolean[2];
        flags[0] = true;
        flags[1] = true;
        //flags[0] is nestedCheck.
        //flags[1] is correlationCheck.
        //ArrayList<String[]> totalCols = new ArrayList();
        //totalCols.addAll(node.relations);
        if(node.whereNodes != null)
        {
            for(WhereNode in_node: node.whereNodes)
            {
                flags[0] = flags[0] && in_node.nested;  //nest check.
                for(String[] table: node.relations)
                {
                    boolean leftcheck = table[0].equals(in_node.leftOperandPrefix);
                    boolean rightcheck = table[0].equals(in_node.rightOperandPrefix);
                    flags[1] = flags[1] && leftcheck && rightcheck; //Correlation check.
                    if(in_node.subQuery != null)    //Base case.
                        correlationCheck(in_node.subQuery);
                }
            }
        }
        return flags;
    }
    
    //Start with the parent and keep three in memory, backwards, current, forwards.
    static void doubleLink(SQLNode node)
    {
        //Assumes that the resolution takes place exactly one node before.
        for(WhereNode wn: node.whereNodes)
        {
            wn.parent = node;
            wn.subQuery.parent = node;
            if(wn.subQuery != null)
                doubleLink(wn.subQuery);
            //else = base case.
        }
    }
    
    static void testDatabase(Database db)
    {
        Relation relation = db.getRelation("PARTS");
        relation.displayRelation();
        relation.displaySchema();
        db.displaySchema();
    }
    
}
