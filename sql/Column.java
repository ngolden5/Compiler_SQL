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
public class Column
{
    public String prefix; // contains prefix, if any
    public String name; // contains column name
    public SQLNode sn; //contains SQLNode where column resolves
    public int ci; //contains index of relations where column resolves
}
   
