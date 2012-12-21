/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.engine.run;

import com.lumens.engine.TransformComponent;
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
