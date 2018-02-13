package sql;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 * This is a linked list. I think.
 * @author Nick
 */
public class SQLNode
{
  public boolean distinct;    //DISTINCT
  public ArrayList<Column> columns;   //column_ref
  public ArrayList<String[]> relations;  //FROM
  // List of String[2] array objects
  // [0] element contains relation name
  // [1] element contains relation alias, if any
  public ArrayList<WhereNode> whereNodes; //WHERE
  // contains individual conjuncts in where clause
  public SQLNode parent;
  //To clone an SQLNode.
  public SQLNode(SQLNode another)
  {
    this.distinct = another.distinct;
    this.columns = new ArrayList();
    columns.addAll(another.columns);
    this.relations = new ArrayList();
    relations.addAll(another.relations);
    this.whereNodes = new ArrayList();
    whereNodes.addAll(another.whereNodes);
    this.parent = another.parent;
  }
  public SQLNode()
  {
    //Do Nothing.
  }

}
