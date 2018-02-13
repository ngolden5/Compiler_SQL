package dbengine;

import java.io.*;
import java.util.*;

public class Database {

  private Map<String,Relation> relations;

  // Week 1
  public Database() {
    relations = new HashMap<String,Relation>();
  }

  public boolean addRelation(String rname, Relation r) {
    if (!relations.containsKey(rname)) {
      r.setName(rname);
      relations.put(rname,r); 
      return true;
    }
    else
      return false;
  }

  public boolean deleteRelation(String rname) {
    if (relations.containsKey(rname)) {
      relations.remove(rname); 
      return true;
    }
    else
      return false;
  }

  public boolean relationExists(String rname) {
    return relations.containsKey(rname);
  }

  public Relation getRelation (String rname) {
    if (relations.containsKey(rname))
      return relations.get(rname);
    else
      return null;
  };

  public void displaySchema() {
    System.out.println("Database Schema");
    System.out.println("-------- ------");
    System.out.println();
    for (Map.Entry<String,Relation> entry : relations.entrySet()) {
      entry.getValue().displaySchema();
    }
    System.out.println();
  }

  // Week 2

  public void initializeDatabase(String dir) {
    FileInputStream fin1 = null;
    FileInputStream fin2 = null;
    BufferedReader infile1 = null;
    BufferedReader infile2 = null;

    try {
      fin1 = new FileInputStream(dir+"/catalog.dat");
      infile1 = new BufferedReader(new InputStreamReader(fin1));
      String s = infile1.readLine();
      int numRelations = Integer.parseInt(s);
      for (int i=0; i<numRelations; i++) {
        String rname = infile1.readLine();
        s = infile1.readLine();
        int numAttributes = Integer.parseInt(s);
        ArrayList<String> attrs = new ArrayList<String>();
        ArrayList<String> doms = new ArrayList<String>();
        for (int j=0; j<numAttributes; j++) {
          String aname = infile1.readLine();
          String atype = infile1.readLine();
          attrs.add(aname);
          doms.add(atype);
        }
        Relation r = new Relation(rname,attrs,doms);
        // Now lets add tuples
        String fname=dir+"/"+rname+".dat";
        fin2 = new FileInputStream(fname);
        infile2 = new BufferedReader(new InputStreamReader(fin2));
        s = infile2.readLine();
        int numTuples = Integer.parseInt(s);
        for (int k=0; k<numTuples; k++) {
          Tuple tuple = new Tuple(attrs,doms);
          for (int j=0; j<numAttributes; j++) {
            s = infile2.readLine();
            if (r.attributeType(attrs.get(j)).equals("VARCHAR")) // is varchar
              tuple.addStringComponent(s);
            else if (r.attributeType(attrs.get(j)).equals("INTEGER")) { // is integer
              Integer ival = new Integer(Integer.parseInt(s));
              tuple.addIntegerComponent(ival);
            } else { // is  decimal
                Double dval = new Double(Double.parseDouble(s));
                tuple.addDoubleComponent(dval);
              }
          }
          r.addTuple(tuple);
        }
        fin2.close();
        addRelation(rname,r);
      }
      fin1.close();
    } catch (IOException e) {
      }
  }

}
