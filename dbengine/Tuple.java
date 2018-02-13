package dbengine;

import java.util.*;

public class Tuple {

  private ArrayList<String> attributes;
  private ArrayList<String> domains;
  private ArrayList<Comparable> tuple;

  Tuple (ArrayList<String> attr, ArrayList<String> dom) {
    attributes = attr;
    domains = dom;
    tuple = new ArrayList<Comparable>();
  }

  public void addStringComponent(String s) {
    tuple.add(s);
  }

  public void addDoubleComponent(Double d) {
    tuple.add(d);
  }

  public void addIntegerComponent(Integer i) {
    tuple.add(i);
  }

  public String toString() {
    String answer = "";
    for (int i=0; i<tuple.size(); i++)
      answer += tuple.get(i)+":";
    return answer+"\n";
  }

  public boolean equals(Tuple compareTuple) {
    for (int i=0; i<domains.size(); i++)
      if (this.tuple.get(i).compareTo(compareTuple.tuple.get(i)) != 0)
        return false; 
    return true;
  }

  // Week 3
  public Tuple clone(ArrayList<String> attr) {
    Tuple tup = new Tuple(attr,domains);
    for (int i=0; i<domains.size(); i++) {
      if (domains.get(i).equals("VARCHAR"))
        tup.addStringComponent(new String((String) tuple.get(i)));
      else if (domains.get(i).equals("INTEGER"))
        tup.addIntegerComponent(new Integer((Integer) tuple.get(i)));
      else if (domains.get(i).equals("DECIMAL"))
        tup.addDoubleComponent(new Double((Double) tuple.get(i)));
    }
    return tup;
  }

  // Week 4
  public Tuple concatenate(Tuple t, ArrayList<String> attrs, ArrayList<String> doms) {
    Tuple tup = new Tuple(attrs,doms);
    for (int i=0; i<this.tuple.size(); i++) {
      if (this.domains.get(i).equals("VARCHAR"))
        tup.addStringComponent(new String((String) this.tuple.get(i)));
      else if (this.domains.get(i).equals("INTEGER"))
        tup.addIntegerComponent(new Integer((Integer) this.tuple.get(i)));
      else if (this.domains.get(i).equals("DECIMAL"))
        tup.addDoubleComponent(new Double((Double) this.tuple.get(i)));
    }
    for (int j=0; j<t.tuple.size(); j++) {
      if (t.domains.get(j).equals("VARCHAR"))
        tup.addStringComponent(new String((String) t.tuple.get(j)));
      else if (t.domains.get(j).equals("INTEGER"))
        tup.addIntegerComponent(new Integer((Integer) t.tuple.get(j)));
      else if (t.domains.get(j).equals("DECIMAL"))
        tup.addDoubleComponent(new Double((Double) t.tuple.get(j)));
    }
    return tup;
  }

  // Week 5
  public Tuple project(ArrayList<String> cnames) {
    ArrayList<String> doms = new ArrayList<String>();
    for (int i=0; i<cnames.size(); i++) {
      String cn = cnames.get(i);
      int index = attributes.indexOf(cn);
      String ctype = domains.get(index);
      doms.add(ctype);
    }
    Tuple tup = new Tuple(cnames,doms);
    for (int i=0; i<cnames.size(); i++) {
      String cn = cnames.get(i);
      int index = attributes.indexOf(cn);
      if (doms.get(i).equals("VARCHAR"))
        tup.addStringComponent(new String((String) this.tuple.get(index)));
      else if (doms.get(i).equals("INTEGER"))
        tup.addIntegerComponent(new Integer((Integer) this.tuple.get(index)));
      else if (doms.get(i).equals("DECIMAL"))
        tup.addDoubleComponent(new Double((Double) this.tuple.get(index)));
    }
    return tup;
  }

  // Week 6
  public boolean select(String lopType, String lopValue, String comparison,
                        String ropType, String ropValue) {
    if (lopType.equals("str") && ropType.equals("str")) {
      if (comparison.equals("="))
        return lopValue.equals(ropValue);
      else if (comparison.equals("<>"))
        return (!lopValue.equals(ropValue));
      else if (comparison.equals("<"))
        return (lopValue.compareTo(ropValue) < 0);
      else if (comparison.equals(">"))
        return (lopValue.compareTo(ropValue) > 0);
      else if (comparison.equals("<="))
        return (lopValue.compareTo(ropValue) <= 0);
      else if (comparison.equals(">="))
        return (lopValue.compareTo(ropValue) >= 0);
    }
    else if (lopType.equals("num") && ropType.equals("num")) {
      double lval = Double.parseDouble(lopValue);
      double rval = Double.parseDouble(ropValue);
      if (comparison.equals("="))
        return (lval == rval);
      else if (comparison.equals("<>"))
        return (lval != rval);
      else if (comparison.equals("<"))
        return (lval < rval);
      else if (comparison.equals(">"))
        return (lval > rval);
      else if (comparison.equals("<="))
        return (lval <= rval);
      else if (comparison.equals(">="))
        return (lval >= rval);
    } 
    else if (lopType.equals("col") && ropType.equals("num")) {
      int index = attributes.indexOf(lopValue);
      String lcolType = domains.get(index);
      if (lcolType.equals("INTEGER")) {
        Integer lvall = (Integer) tuple.get(index);
        double lval = lvall.doubleValue();
        double rval = Double.parseDouble(ropValue);
        if (comparison.equals("="))
          return (lval == rval);
        else if (comparison.equals("<>"))
          return (lval != rval);
        else if (comparison.equals("<"))
          return (lval < rval);
        else if (comparison.equals(">"))
          return (lval > rval);
        else if (comparison.equals("<="))
          return (lval <= rval);
        else if (comparison.equals(">="))
          return (lval >= rval);
      }
      else {
        Double lvall = (Double) tuple.get(index);
        double lval = lvall.doubleValue();
        double rval = Double.parseDouble(ropValue);
        if (comparison.equals("="))
          return (lval == rval);
        else if (comparison.equals("<>"))
          return (lval != rval);
        else if (comparison.equals("<"))
          return (lval < rval);
        else if (comparison.equals(">"))
          return (lval > rval);
        else if (comparison.equals("<="))
          return (lval <= rval);
        else if (comparison.equals(">="))
          return (lval >= rval);
      }
    }
    else if (lopType.equals("col") && ropType.equals("str")) {
      int index = attributes.indexOf(lopValue);
      String lcolType = domains.get(index);
      if (lcolType.equals("VARCHAR")) {
        String lval = (String) tuple.get(index);
        if (comparison.equals("="))
          return lval.equals(ropValue);
        else if (comparison.equals("<>"))
          return (!lval.equals(ropValue));
        else if (comparison.equals("<"))
          return (lval.compareTo(ropValue) < 0);
        else if (comparison.equals(">"))
          return (lval.compareTo(ropValue) > 0);
        else if (comparison.equals("<="))
          return (lval.compareTo(ropValue) <= 0);
        else if (comparison.equals(">="))
          return (lval.compareTo(ropValue) >= 0);
      }
    }
    else if (lopType.equals("num") && ropType.equals("col")) {
      int index = attributes.indexOf(ropValue);
      String rcolType = domains.get(index);
      if (rcolType.equals("INTEGER")) {
        Integer rvall = (Integer) tuple.get(index);
        double rval = rvall.doubleValue();
        double lval = Double.parseDouble(lopValue);
        if (comparison.equals("="))
          return (lval == rval);
        else if (comparison.equals("<>"))
          return (lval != rval);
        else if (comparison.equals("<"))
          return (lval < rval);
        else if (comparison.equals(">"))
          return (lval > rval);
        else if (comparison.equals("<="))
          return (lval <= rval);
        else if (comparison.equals(">="))
          return (lval >= rval);
      }
      else {
        Double rvall = (Double) tuple.get(index);
        double rval = rvall.doubleValue();
        double lval = Double.parseDouble(lopValue);
        if (comparison.equals("="))
          return (lval == rval);
        else if (comparison.equals("<>"))
          return (lval != rval);
        else if (comparison.equals("<"))
          return (lval < rval);
        else if (comparison.equals(">"))
          return (lval > rval);
        else if (comparison.equals("<="))
          return (lval <= rval);
        else if (comparison.equals(">="))
          return (lval >= rval);
      }
    }
    else if (lopType.equals("str") && ropType.equals("col")) {
      int index = attributes.indexOf(ropValue);
      String rcolType = domains.get(index);
      if (rcolType.equals("VARCHAR")) {
        String rval = (String) tuple.get(index);
        if (comparison.equals("="))
          return lopValue.equals(rval);
        else if (comparison.equals("<>"))
          return (!lopValue.equals(rval));
        else if (comparison.equals("<"))
          return (lopValue.compareTo(rval) < 0);
        else if (comparison.equals(">"))
          return (lopValue.compareTo(rval) > 0);
        else if (comparison.equals("<="))
          return (lopValue.compareTo(rval) <= 0);
        else if (comparison.equals(">="))
          return (lopValue.compareTo(rval) >= 0);
      }
    }
    else if (lopType.equals("col") && ropType.equals("col")) {
      int lIndex = attributes.indexOf(lopValue);
      int rIndex = attributes.indexOf(ropValue);
      String lcolType = domains.get(lIndex);
      String rcolType = domains.get(rIndex);
      if (lcolType.equals("VARCHAR") && rcolType.equals("VARCHAR")) {
        String lval = (String) tuple.get(lIndex);
        String rval = (String) tuple.get(rIndex);
        if (comparison.equals("="))
          return lval.equals(rval);
        else if (comparison.equals("<>"))
          return (!lval.equals(rval));
        else if (comparison.equals("<"))
          return (lval.compareTo(rval) < 0);
        else if (comparison.equals(">"))
          return (lval.compareTo(rval) > 0);
        else if (comparison.equals("<="))
          return (lval.compareTo(rval) <= 0);
        else if (comparison.equals(">="))
          return (lval.compareTo(rval) >= 0);
      }
      else { // both columns are numeric (INTEGER or DECIMAL)
        double lval = 0.0;
        if (lcolType.equals("INTEGER")) {
          Integer lvall = (Integer) tuple.get(lIndex);
          lval = lvall.doubleValue();
        } else {
            Double lvall = (Double) tuple.get(lIndex);
            lval = lvall.doubleValue();
          }
        double rval = 0.0;
        if (rcolType.equals("INTEGER")) {
          Integer rvall = (Integer) tuple.get(rIndex);
          rval = rvall.doubleValue();
        } else {
            Double rvall = (Double) tuple.get(rIndex);
            rval = rvall.doubleValue();
          }
        if (comparison.equals("="))
          return (lval == rval);
        else if (comparison.equals("<>"))
          return (lval != rval);
        else if (comparison.equals("<"))
          return (lval < rval);
        else if (comparison.equals(">"))
          return (lval > rval);
        else if (comparison.equals("<="))
          return (lval <= rval);
        else if (comparison.equals(">="))
          return (lval >= rval);
      }
    }
    return false;
  }

  // Week 7
  public Tuple join(Tuple t2, ArrayList<String> attr, ArrayList<String> dom) {
    // Collect information about join columns
    ArrayList<Integer> leftJoinCols = new ArrayList<Integer>();
    ArrayList<Integer> rightJoinCols = new ArrayList<Integer>();
    ArrayList<String> lJoinDoms = new ArrayList<String>();
    ArrayList<String> rJoinDoms = new ArrayList<String>();

    int numAttr2 = t2.attributes.size();
    for (int i=0; i<numAttr2; i++) {
      String a = t2.attributes.get(i);
      String rdom = t2.domains.get(i);
      int index = this.attributes.indexOf(a);
      if (index != -1) {
        leftJoinCols.add(new Integer(index));
        rightJoinCols.add(new Integer(i));
        String ldom = this.domains.get(index);
        lJoinDoms.add(ldom);
        rJoinDoms.add(rdom);
      }
    }
    // Verify if the two tuples can join; if not return null
    for (int i=0; i<leftJoinCols.size(); i++) {
      int ljcol = leftJoinCols.get(i).intValue();
      int rjcol = rightJoinCols.get(i).intValue();
      String ldom = lJoinDoms.get(i);
      String rdom = rJoinDoms.get(i);
      if (ldom.equals("VARCHAR")) {
        String lval = (String) this.tuple.get(ljcol);
        String rval = (String) t2.tuple.get(rjcol);
        if (!lval.equals(rval))
          return null;
      }
      else { // ldom and rdom are INTEGER or DECIMAL 
        double lval = 0.0;
        if (ldom.equals("INTEGER")) {
          Integer lvall = (Integer) this.tuple.get(ljcol);
          lval = lvall.doubleValue();
        } else {
            Double lvall = (Double) this.tuple.get(ljcol);
            lval = lvall.doubleValue();
          }
        double rval = 0.0;
        if (rdom.equals("INTEGER")) {
          Integer rvall = (Integer) t2.tuple.get(rjcol);
          rval = rvall.doubleValue();
        } else {
            Double rvall = (Double) t2.tuple.get(rjcol);
            rval = rvall.doubleValue();
          }
        if (lval != rval)
          return null;
      }
    }
    // Now join the two tuples and return the joined tuples
    Tuple tup = new Tuple(attr,dom);
    for (int i=0; i<this.attributes.size(); i++) {
      String ldom = this.domains.get(i);
      if (ldom.equals("VARCHAR"))
        tup.addStringComponent(new String((String) this.tuple.get(i)));
      else if (ldom.equals("INTEGER"))
        tup.addIntegerComponent(new Integer((Integer) this.tuple.get(i)));
      else if (ldom.equals("DECIMAL"))
        tup.addDoubleComponent(new Double((Double) this.tuple.get(i)));      
    }
    for (int i=0; i<t2.attributes.size(); i++) {
      String rattr = t2.attributes.get(i);
      if (this.attributes.indexOf(rattr) == -1) {
        String rdom = t2.domains.get(i);
        if (rdom.equals("VARCHAR"))
          tup.addStringComponent(new String((String) t2.tuple.get(i)));
        else if (rdom.equals("INTEGER"))
          tup.addIntegerComponent(new Integer((Integer) t2.tuple.get(i)));
        else if (rdom.equals("DECIMAL"))
          tup.addDoubleComponent(new Double((Double) t2.tuple.get(i)));
      }
    }
    return tup;  
  }

}
