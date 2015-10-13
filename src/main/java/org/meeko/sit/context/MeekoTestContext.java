package org.meeko.sit.context;

import java.util.concurrent.Callable;

import org.meeko.sit.annotation.MeekoTestFlow;
import org.meeko.sit.commons.ContextBean;
import org.meeko.sit.workflow.MeekoTestFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Context for each test with custom environment
 * @author Alej4ndro G0m3z.
 *
 */
public abstract class MeekoTestContext extends ContextBean implements Callable<Object> {

    @Autowired
    public ApplicationContext applicationContext;

    @MeekoTestFlow
    public MeekoTestFlowService workflow;

}
