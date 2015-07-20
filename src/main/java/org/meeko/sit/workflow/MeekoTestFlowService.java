package org.meeko.sit.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.meeko.sit.SocketConfig;
import org.meeko.sit.utils.PrettyFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Meeko Test flow structure to follow everything
 * Each step publish to WebSocket topic, you can change it or deactivate it
 * @see org.meeko.sit.workflow.MeekoTestFlowService setTopic("/yourtopic/everything");
 * @see org.meeko.sit.workflow.MeekoTestFlowService setUseTopic(false); by default is activated
 * @author Alej4ndro G0m3z.
 *
 */
@Component
@Scope("prototype")
public class MeekoTestFlowService implements Cloneable {

    public static final String  STATUS_OK                = "OK";
    public static final String  STATUS_KO                = "KO";
    private static final String START_WORKFLOW           = "Starting meekoflow for: ";
    private static final String DEFAULT_TEST_FAILED_DESC = "Your test has failed.";

    private String              name;
    private String              status;
    private List<MeekoTestStep> steps;
    private UUID                testId;
    private long                time;
    private String              topic = SocketConfig.MEEKO_WEBSOCKET_TOPIC + "/flow";

    @Autowired
    private SimpMessagingTemplate template;

    public void start(String className) throws Exception {
        testId = UUID.randomUUID();
        steps = new ArrayList<MeekoTestStep>();
        if (className != null) {
            String[] noClassPath = className.split(".");
            if (noClassPath.length > 1) {
                addStep(START_WORKFLOW + className, noClassPath[noClassPath.length - 1]);
            } else {
                addStep(START_WORKFLOW + className, className);
            }
        } else {
            addStep(START_WORKFLOW + "UNKNOWN", "UNKNOWN");
        }
        setTime(System.currentTimeMillis());
        status = STATUS_OK;
        name = className;
    }

    public void end() {
        if (steps == null) {
            addStep("WARNING: Please use start before use end!");
        }
        addStep("STATUS:" + status);
        setTime((System.currentTimeMillis() - time));
    }

    /**
     * Starting case
     * @param caseName
     */
    public void startCase(String caseName) {
        addStep("STARTING CASE:" + caseName, caseName);
    }

    /**
     * Provide a custom description about your current step or next.
     * This send to topic @see org.meeko.sit.SocketConfig if this is enabled
     * @see org.meeko.sit.workflow.PresenceChannelInterceptor
     * @param description
     */
    public void addStep(String description) {
        addStep(description, null);
    }

    private void addStep(String description, String caseName) {
        addStep(description, caseName, null);
    }

    private void addStep(String description, String caseName, String status) {
        MeekoTestStep step = new MeekoTestStep(description, testId, caseName, status);

        if (steps == null) {
            try {
                start("Please use meekoTestFlowService.start('Your class name') method");
            } catch (Exception e) {
                StringBuffer exception = new StringBuffer("Unexpected error: ");
                exception.append(topic);
                exception.append(" exception: ");
                exception.append(e.getMessage());
                exception.append(", last step: ");
                exception.append(description);
                step.setDescription(exception.toString());
                // We can not create steps in start method (Fail UUID? xD)
                if (steps == null) {
                    steps = new ArrayList<MeekoTestStep>();
                }
            }
        }
        steps.add(step);
        try {
            template.convertAndSend(topic, PrettyFormat.formatJSON(step));
        } catch (Exception e) {
            StringBuffer exception = new StringBuffer("Error publishing at topic: ");
            exception.append(topic);
            exception.append(" exception: ");
            exception.append(e.getMessage());
            exception.append(", last step: ");
            exception.append(description);
            step.setDescription(exception.toString());
        }
    }

    public List<MeekoTestStep> getSteps() {
        return steps;
    }

    public void setSteps(List<MeekoTestStep> steps) {
        this.steps = steps;
    }

    public void testFailed() {
        this.addStep(DEFAULT_TEST_FAILED_DESC, null, STATUS_KO);
        this.status = STATUS_KO;
    }

    public void testFailed(String description) {
        this.addStep(description, null, STATUS_KO);
        this.status = STATUS_KO;
    }

    public void testSuccess() {
        // Only if the last step are success
        if (status == STATUS_OK) {
            status = STATUS_OK;
        }
    }

    public String getStatus() {
        return status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTime() {
        return time + " ms";
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "MeekoTestFlowService [status=" + status + ", steps=" + steps + ", testId=" + testId + ", topic=" + topic + ", template=" + template + "]";
    }
}
