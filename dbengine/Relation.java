package dbengine;

import java.io.*;
import java.util.*;

public class Relation {
  // Name of the relation.
  private String name;

  // Attribute names for the relation
  private ArrayList<String> attributes;

  // Domain classes or types of attributes; possible values: INTEGER, DECIMAL, VARCHAR
  private ArrayList<String> domains;

  // Actual data storage (list of tuples) for the relation.
  private ArrayList<Tuple> table;

  /*******************************************************************
   * Construct a relation with given domain names.
   * @param name name of the relation
   * @param attributes attribute names for the relation
   * @param dNames domain names (type names of attributes)
   */
  public Relation (String name, ArrayList<String> attributes, ArrayList<String> dNames) {
    this.name = name;
    this.attributes = attributes;
    this.domains = dNames;
    table = new ArrayList<Tuple>();
  };

  public boolean attributeExists(String aname) {
    for (int i=0; i<attributes.size(); i++) {
      String a = attributes.get(i);
      if (a.equals(aname))
        return true;
    }
    return false;
  }

  public String attributeType(String aname) {
    for (int i=0; i<attributes.size(); i++) {
      String a = attributes.get(i);
      if (a.equals(aname))
        return domains.get(i);
    }
    return null;
  }

  public void displaySchema() {
    System.out.print(name+"(");
    for (int i=0; i<attributes.size(); i++) {
      String aname = attributes.get(i);
      String atype = domains.get(i);
      System.out.print(aname+":"); 
      System.out.print(atype); 
      if (i<(attributes.size()-1))
        System.out.print(",");
    }
    System.out.println(")");
  }

  public void setName(String rname) {
    name = rname;
  }

  public void addTuple(Tuple tup) {
    table.add(tup);
  }

  public void displayRelation() {
    displaySchema();
    System.out.println("Number of tuples: "+table.size());
    System.out.println();
    for (int i=0; i<table.size(); i++)
      System.out.println(table.get(i));
    System.out.println();
  }

  public String toString() {
    String s = name+"(";
    for (int i=0; i<attributes.size(); i++) {
      String aname = attributes.get(i);
      String atype = domains.get(i);
      s += aname+":"+atype; 
      if (i<(attributes.size()-1))
        s+= ",";
    }
    s += ")\n";
    s += "Number of tuples: "+table.size()+"\n\n";
    for (int i=0; i<table.size(); i++)
      s += table.get(i);
    return s;
  }

  // Week 2
  public void removeDuplicates() {
    int nTuples = table.size();
    for (int i=0; i<nTuples; i++) {
      Tuple t1 = table.get(i);
      int j = i+1;
      while (j < nTuples) {
        Tuple t2 = table.get(j);
        if (t1.equals(t2)) {
          table.remove(j);
          nTuples--;
        }
        else
          j++;
      }
    }
  }

  // Week 3
  public Relation union(Relation r2) {
    ArrayList<String> attr = new ArrayList<String>();
    ArrayList<String> dom = new ArrayList<String>();
    int numAttr = attributes.size();
    for (int i=0; i<numAttr; i++) {
      String a = attributes.get(i);
      attr.add(a);
      String d = domains.get(i);
      dom.add(d);
    }
    Relation r = new Relation(null,attr,dom);
    // Now add tuples
    int numTuples = table.size();
    for (int i=0; i<numTuples; i++) {
      Tuple t = table.get(i).clone(attr);
      r.table.add(t);
    }
    int numTuples2 = r2.table.size();
    for (int i=0; i<numTuples2; i++) {
      Tuple t = r2.table.get(i).clone(attr);
      r.table.add(t);
    }
    r.removeDuplicates();
    return r;
  }

  public boolean member(Tuple t) {
    for (int i=0; i<table.size(); i++) {
      if (table.get(i).equals(t))
        return true;
    }
    return false;
  }

  public Relation intersect(Relation r2) {
    ArrayList<String> attr = new ArrayList<String>();
    ArrayList<String> dom = new ArrayList<String>();
    int numAttr = attributes.size();
    for (int i=0; i<numAttr; i++) {
      String a = attributes.get(i);
      attr.add(a);
      String d = domains.get(i);
      dom.add(d);
    }
    Relation r = new Relation(null,attr,dom);
    // Now lets add the tuples to r
    int numTuples = table.size();
    for (int i=0; i<numTuples; i++) {
      Tuple t = table.get(i).clone(attr);
      if (r2.member(t))
        r.table.add(t);
    }
    return r;
  }

  public Relation minus(Relation r2) {
    ArrayList<String> attr = new ArrayList<String>();
    ArrayList<String> dom = new ArrayList<String>();
    int numAttr = attributes.size();
    for (int i=0; i<numAttr; i++) {
      String a = attributes.get(i);
      attr.add(a);
      String d = domains.get(i);
      dom.add(d);
    }
    Relation r = new Relation(null,attr,dom);
    // Now lets add the tuples to r
    int numTuples = table.size();
    for (int i=0; i<numTuples; i++) {
      Tuple t = table.get(i).clone(attr);
      if (!r2.member(t))
        r.table.add(t);
    }
    return r;
  }

  // Week 4 
  public Relation rename(ArrayList<String> cnames) {
    ArrayList<String> attr = new ArrayList<String>(); 
    ArrayList<String> dom = new ArrayList<String>(); 
    int numAttr = cnames.size(); 
    for (int i=0; i<numAttr; i++) { 
      String a = cnames.get(i); 
      attr.add(a); 
      String d = domains.get(i); 
      dom.add(d); 
    } 
    Relation r = new Relation(null,attr,dom); 
    // Now lets add the tuples to r 
    int numTuples = table.size(); 
    for (int i=0; i<numTuples; i++) { 
      Tuple t = table.get(i).clone(attr); 
      r.table.add(t); 
    }
    return r;
  }

  public Relation times(Relation r) {
    // Construct Schema of output relation
    ArrayList<String> a = new ArrayList<String>();
    ArrayList<String> d = new ArrayList<String>();
    for (int i=0; i<this.attributes.size(); i++) {
      String aname = this.attributes.get(i);
      if (r.attributeExists(aname))
        a.add(this.name+"."+aname);
      else
        a.add(aname);
      String dname = this.domains.get(i);
      d.add(dname);
    }
    for (int i=0; i<r.attributes.size(); i++) {
      String aname = r.attributes.get(i);
      if (this.attributeExists(aname))
        a.add(r.name+"."+aname);
      else
        a.add(aname);
      String dname = r.domains.get(i);
      d.add(dname);
    }
    Relation trel = new Relation(null,a,d);
    // Now add tuples
    for (int i=0; i<this.table.size(); i++) {
      Tuple t1 = this.table.get(i);
      for (int j=0; j<r.table.size(); j++) {
        Tuple t2 = r.table.get(j);
        Tuple t3 = t1.concatenate(t2,a,d);
        trel.addTuple(t3);
      }
    } 
    return trel;
  }

  // Week 5
  public Relation project(ArrayList<String> cnames) {
    ArrayList<String> a = new ArrayList<String>();
    ArrayList<String> d = new ArrayList<String>();
    for (int i=0; i<cnames.size(); i++) {
      String cn = cnames.get(i);
      int index = attributes.indexOf(cn);
      if (index != -1) {
        String ct = domains.get(index);
        a.add(cn);
        d.add(ct);
      }
      else
        return null;
    }
    Relation r = new Relation(null,a,d);
    // Now add tuples
    for (int i=0; i<table.size(); i++) {
      Tuple t = table.get(i);
      Tuple tup = t.project(cnames);
      r.addTuple(tup);
    } 
    r.removeDuplicates();
    return r;
  }

  // Week 6
  // selection("num","1","=","num","1");
  public Relation select(String lopType, String lopValue, String comparison,
                         String ropType, String ropValue) {
    ArrayList<String> attr = new ArrayList<String>();
    ArrayList<String> dom = new ArrayList<String>();
    for (int i=0; i<attributes.size(); i++) {
      String aname = attributes.get(i);
      String atype = domains.get(i);
      attr.add(aname);
      dom.add(atype);
    }
    Relation r = new Relation(null,attr,dom);
    // Now lets add the tuples to r
    int numAttr = attr.size();
    int numTuples = table.size();
    for (int i=0; i<numTuples; i++) {
      Tuple t = table.get(i);
      if (t.select(lopType, lopValue, comparison, ropType, ropValue)) {
        Tuple tup = t.clone(attr);
        r.addTuple(tup);
      }
    }
    return r;
  }

  // Week 7
  public Relation join(Relation r2) {
    ArrayList<String> attr = new ArrayList<String>();
    ArrayList<String> dom = new ArrayList<String>();

    int numAttr1 = attributes.size();
    for (int i=0; i<numAttr1; i++) {
      String a = attributes.get(i);
      String d = domains.get(i);
      attr.add(a);
      dom.add(d);
    }
    int numAttr2 = r2.attributes.size();
    for (int i=0; i<numAttr2; i++) {
      String a = r2.attributes.get(i);
      String rdom = r2.domains.get(i);
      int index = attributes.indexOf(a);
      if (index == -1) {
        attr.add(a);
        dom.add(rdom);
      }
    }
    Relation r = new Relation(null,attr,dom);
    // Now lets add the tuples to r
    int numTuples = table.size();
    int numTuples2 = r2.table.size();
    for (int i=0; i<numTuples; i++) {
      Tuple t1 = table.get(i);
      for (int j=0; j<numTuples2; j++) {
        Tuple t2 = r2.table.get(j);
        Tuple t = t1.join(t2,attr,dom);
        if (t != null)
          r.table.add(t);
      }
    }
    return r;
  }

}
