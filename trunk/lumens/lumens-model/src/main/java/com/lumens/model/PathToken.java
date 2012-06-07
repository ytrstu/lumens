/*
 * Lumens developer shaofeng.cjpw@gmail.com
 */
package com.lumens.model;

/**
 *
 * @author shaofeng wang
 */
public class PathToken
{
  private String token;
  private int index;

  public PathToken(String token)
  {
    this.token = token;
    parseToken(token);
  }

  public boolean isIndexed()
  {
    return index != -1;
  }

  public int index()
  {
    return index;
  }

  @Override
  public String toString()
  {
    return token;
  }

  private void parseToken(String token)
  {
    if (token.charAt(token.length() - 1) != ']')
      index = -1;
  }
}
