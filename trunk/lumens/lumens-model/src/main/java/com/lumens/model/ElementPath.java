/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class ElementPath implements Path
{
  private List<String> tokens;

  public ElementPath(String path)
  {
    parse(path);
  }

  public ElementPath(List<String> tokens)
  {
    this.tokens = tokens;
  }

  @Override
  public Iterator<String> iterator()
  {
    if (tokens == null)
      return null;
    return tokens.iterator();
  }

  @Override
  public Path left(int count)
  {
    List<String> subTokens = null;
    if (count > 0)
      subTokens = tokens.subList(0, count);
    return new ElementPath(subTokens);
  }

  @Override
  public Path right(int count)
  {
    List<String> subTokens = null;
    if (count > 0)
    {
      int size = tokens.size();
      subTokens = tokens.subList(size - count, size - 1);
    }
    return new ElementPath(subTokens);
  }

  @Override
  public Path removeLeft(int count)
  {
    List<String> removed = null;
    if (count > 0)
      removed = new LinkedList<String>();
    while (0 < count--)
      removed.add(tokens.remove(0));
    return new ElementPath(removed);
  }

  @Override
  public Path removeRight(int count)
  {
    LinkedList<String> removed = new LinkedList<String>();
    while (0 < count--)
      removed.addFirst(tokens.remove(tokens.size() - 1));
    return new ElementPath(removed);
  }

  @Override
  public String toString()
  {
    if (tokens == null)
      return "";
    StringBuilder builder = new StringBuilder();
    for (String token : tokens)
    {
      if (builder.length() > 0)
        builder.append('.');
      if (token.indexOf('.') > 0)
        builder.append('\'').append(token).append('\'');
      else
        builder.append(token);
    }
    return builder.toString();
  }

  private void parse(String path)
  {
    if (path == null || path.isEmpty())
      return;
    List<String> pathTokens = new LinkedList<String>();
    int length = path.length();
    int found = 0, index = 0;
    boolean quoteFound = false;
    while (index < length)
    {
      char c = path.charAt(index);
      if (c == '.' && !quoteFound)
      {
        if (index == 0 || index + 1 == length)
          throw new IllegalArgumentException("Error path format \"" + path + "\"");
        pathTokens.add(removeQuote(path.substring(found, index)));
        found = index + 1;
      }
      else if (c == '\'')
        quoteFound = !quoteFound;
      ++index;
    }
    if (found < length && index == length)
      pathTokens.add(removeQuote(path.substring(found, index)));
    if (pathTokens.size() > 0)
      tokens = pathTokens;
  }

  private String removeQuote(String token)
  {
    if (token.charAt(0) == '\'' && token.charAt(token.length() - 1) == '\'')
      return token.substring(1, token.length() - 1);
    return token;
  }
}
