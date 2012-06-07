/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.model;

import java.util.Iterator;

/**
 *
 * @author washaofe
 */
public interface Path
{
  public Iterator<String> iterator();

  public Path left(int count);

  public Path right(int count);

  public Path removeLeft(int count);

  public Path removeRight(int count);
}
