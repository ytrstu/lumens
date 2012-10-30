/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.engine;

import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface Executor
{
    public List<Executor> execute();

    public TransformComponent getTransformComponent();

}
