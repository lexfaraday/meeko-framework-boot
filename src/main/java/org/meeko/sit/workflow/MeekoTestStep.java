package org.meeko.sit.workflow;

import java.util.UUID;

/**
 * Atomic description for each action
 * @author Alej4ndro G0m3z.
 *
 */
public class MeekoTestStep {
    private UUID   testId;
    private String caseName;
    private String description;
    private String status;

    protected MeekoTestStep() {

    }

    public MeekoTestStep(String description, UUID caseId, String caseName, String status) {
        this.description = description;
        this.testId = caseId;
        this.caseName = caseName;
        this.status = status;
    }

    public UUID getCaseId() {
        return testId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getDescription() {
        return description;
    }

    public String setDescription(String description) {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MeekoTestStep [testId=" + testId + ", caseName=" + caseName + ", description=" + description + "]";
    }

}
