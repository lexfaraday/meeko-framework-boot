package org.meeko.sit.workflow;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper used to return all threads flow in executor
 * @author Alej4ndro G0m3z.
 *
 */
public class MeekoTestFlowWrapper {
    private List<MeekoTestFlowService> meekoFlowServices;

    public MeekoTestFlowWrapper() {
        meekoFlowServices = new ArrayList<MeekoTestFlowService>();
    }

    public List<MeekoTestFlowService> getMeekoFlowServices() {
        return meekoFlowServices;
    }
}
