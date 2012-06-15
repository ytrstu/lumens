/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class AccessPath implements Path
{
    private List<PathToken> tokens;

    public AccessPath(String path)
    {
        parse(path);
    }

    public AccessPath(List<PathToken> tokens)
    {
        this.tokens = tokens;
    }

    @Override
    public boolean isEmpty()
    {
        return tokens == null || tokens.isEmpty();
    }

    @Override
    public int tokenCount()
    {
        return tokens.size();
    }

    @Override
    public Iterator<PathToken> iterator()
    {
        if (tokens == null)
            return null;
        return tokens.iterator();
    }

    @Override
    public Path addLeft(String token)
    {
        if (tokens == null)
            tokens = new LinkedList<PathToken>();
        tokens.add(0, new PathToken(token));
        return this;
    }

    @Override
    public Path addRight(String token)
    {
        if (tokens == null)
            tokens = new LinkedList<PathToken>();
        tokens.add(new PathToken(token));
        return this;
    }

    @Override
    public Path left(int count)
    {
        List<PathToken> subTokens = null;
        if (count > 0)
            subTokens = tokens.subList(0, count);
        return new AccessPath(subTokens);
    }

    @Override
    public Path right(int count)
    {
        List<PathToken> subTokens = null;
        if (count > 0)
        {
            int size = tokens.size();
            subTokens = tokens.subList(size - count, size);
        }
        return new AccessPath(subTokens);
    }

    @Override
    public Path removeLeft(int count)
    {
        if (count >= tokens.size())
            return null;

        List<PathToken> removed = null;
        if (count > 0)
            removed = new LinkedList<PathToken>();
        while (0 < count--)
        {
            removed.add(tokens.remove(0));
        }
        return new AccessPath(removed);
    }

    @Override
    public Path removeRight(int count)
    {
        if (count >= tokens.size())
            return null;

        LinkedList<PathToken> removed = null;
        if (count > 0)
            removed = new LinkedList<PathToken>();
        while (0 < count--)
        {
            removed.addFirst(tokens.remove(tokens.size() - 1));
        }
        return new AccessPath(removed);
    }

    @Override
    public String toString()
    {
        if (tokens == null)
            return "";
        StringBuilder builder = new StringBuilder();
        for (PathToken token : tokens)
        {
            String strToken = token.toString();
            if (builder.length() > 0)
                builder.append('.');
            if (strToken.indexOf('.') > 0)
                builder.append('\'').append(strToken).append('\'');
            else
                builder.append(strToken);
        }
        return builder.toString();
    }

    private void parse(String path)
    {
        if (path == null || path.isEmpty())
            return;
        List<PathToken> pathTokens = new LinkedList<PathToken>();
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

    private PathToken removeQuote(String token)
    {
        if (token.charAt(0) == '\'' && token.charAt(token.length() - 1) == '\'')
            return new PathToken(token.substring(1, token.length() - 1));
        return new PathToken(token);
    }
}
