package org.meeko.sit.context;

import java.util.concurrent.Callable;

import org.meeko.sit.annotation.MeekoTestFlow;
import org.meeko.sit.workflow.MeekoTestFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Context for each test with custom environment
 * @author Alej4ndro G0m3z.
 *
 */
public abstract class MeekoTestContext implements Callable<Object> {

    @Autowired
    public ApplicationContext applicationContext;

    @MeekoTestFlow
    public MeekoTestFlowService workFlow;

    private String  environment;
    private boolean trace;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }
}
