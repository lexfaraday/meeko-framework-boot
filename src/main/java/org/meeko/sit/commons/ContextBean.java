package org.meeko.sit.commons;

public class ContextBean {

    protected String  environment;
    private String  dataSource;
    private boolean useTopic = false;
    protected boolean trace    = false;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public boolean isUseTopic() {
        return useTopic;
    }

    public void setUseTopic(boolean useTopic) {
        this.useTopic = useTopic;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
