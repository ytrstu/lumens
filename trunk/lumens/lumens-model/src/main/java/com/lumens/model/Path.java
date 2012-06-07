/*
 * Lumens developer shaofeng.cjpw@gmail.com
 */
package com.lumens.model;

import java.util.Iterator;

/**
 *
 * @author washaofe
 */
public interface Path
{
  public Iterator<PathToken> iterator();

  public Path left(int count);

  public Path right(int count);

  public Path removeLeft(int count);

  public Path removeRight(int count);
}
