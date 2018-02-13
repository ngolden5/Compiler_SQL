/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

/**
 *
 * @author Nick
 */
public class WhereNode 
{
    public boolean negated;
    public boolean nested;
    // if nested, the following two variables are relevant
    public String nestingType; // "in" or "exists"
    public SQLNode subQuery;  //nesting types are above.
    // if not nested the following variables are relevant
    // in case of nested in-predicate, left Operand will be used.
    public SQLNode parent;
    public String leftOperandType; // "col" or "num" or "str"
    public String leftOperandPrefix; // used when operand type is "col"
    public String leftOperandName; // used when operand type is "col"
    public SQLNode leftResolveNode; // for "col", points to SQLNode where column is resolved
    public int leftResolveIndex; // for "col", index into relations list
    public String leftOperandValue; // used when operand type is "num" or "str"
    public String comparison;
    public String rightOperandType; // "col" or "num" or "str"
    public String rightOperandPrefix; // used when operand type is "col"
    public String rightOperandName; // used when operand type is "col"
    public SQLNode rightResolveNode; // for "col", points to SQLNode where column is resolved
    public int rightResolveIndex; // for "col", index into relations list
    public String rightOperandValue; // used when operand type is "num" or "str"
}