package org.meeko.sit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.meeko.sit.annotation.MeekoTest;
import org.meeko.sit.commons.ContextBean;
import org.meeko.sit.commons.exceptions.ExceptionUtils;
import org.meeko.sit.context.MeekoTestContext;
import org.meeko.sit.utils.MeekoTestUtils;
import org.meeko.sit.utils.PrettyFormat;
import org.meeko.sit.workflow.MeekoTestFlowService;
import org.meeko.sit.workflow.MeekoTestFlowWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Executor to invoke all futures
 * mandatory fields are packageBase and environment 
 * (packageBase is your test package to execute like com.yourcompany.sit.issues)
 * (environment is your ID environment, you can choose anything like LIVE or PROD for example)
 * @author Alej4ndro G0m3z.
 *
 */
@Component
@Scope("prototype")
public class MeekoTestExecutor extends ContextBean {

    private String packageBase;
    private int    fixedThreadPool = 100;
    private String testToExecute;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Single execution test
     * @param testToExecute
     * @return
     */
    public Object run(String testToExecute) {
        this.testToExecute = testToExecute;
        return run();
    }

    /**
     * Multiple execution, supports single execution with testToExecute field.
     * @return
     */
    public Object run() {
        MeekoTestFlowWrapper meekoTestFlowWrapper = new MeekoTestFlowWrapper();
        MeekoTestFlowService meekoTestFlowService = applicationContext.getBean(MeekoTestFlowService.class);
        meekoTestFlowWrapper.getMeekoFlowServices().add(meekoTestFlowService);

        List<Future<?>> futureList = new ArrayList<Future<?>>();

        try {
            meekoTestFlowService.start(MeekoTestExecutor.class.getSimpleName());

            if (packageBase != null) {
                if (super.environment != null) {

                    ExecutorService executorService = null;

                    if (testToExecute == null) {
                        executorService = Executors.newFixedThreadPool(fixedThreadPool);
                        Map<Class<?>, MeekoTest> clazzes = MeekoTestUtils.findMeekoTestClasses(packageBase);
                        addClassToFuture(executorService, futureList, clazzes);
                    } else {
                        executorService = Executors.newFixedThreadPool(1);
                        Map<Class<?>, MeekoTest> clazz = MeekoTestUtils.findMeekoTestSingleClass(packageBase, testToExecute);
                        addClassToFuture(executorService, futureList, clazz);
                    }

                    // FIXME this appears in any time at execution read futureList two times?
                    meekoTestFlowService.addStep("EXECUTED:" + futureList.size());

                    for (Future<?> fut : futureList) {
                        try {
                            meekoTestFlowWrapper.getMeekoFlowServices().add((MeekoTestFlowService) fut.get());
                            if (((MeekoTestFlowService) fut.get()).getStatus() == MeekoTestFlowService.STATUS_KO) {
                                meekoTestFlowService.testFailed(((MeekoTestFlowService) fut.get()).getName());
                            }
                        } catch (Exception e) {
                            meekoTestFlowService.addStep(ExceptionUtils.getMessageException(e));
                        }
                    }
                    executorService.shutdown();
                } else {
                    meekoTestFlowService.addStep("Please provide your custom environment to mach with @MeekoTest(environments={\"YourEnvironment.Environment\"}");
                }
            } else {
                meekoTestFlowService.addStep("Please provide your base package to find your classes to launch with @MeekoTest");
            }
        } catch (Throwable e) {
            meekoTestFlowService.addStep(ExceptionUtils.getMessageException(e));
        }
        meekoTestFlowService.end();
        return PrettyFormat.formatJSON(meekoTestFlowWrapper);
    }

    private void addClassToFuture(ExecutorService executorService, List<Future<?>> futureList, Map<Class<?>, MeekoTest> clazzes) throws Exception {
        if (clazzes.keySet() != null) {
            MeekoTest meekoTestAnnotation = null;
            for (Class<?> clazz : clazzes.keySet()) {
                meekoTestAnnotation = clazzes.get(clazz);

                // Class can launch in this test profile
                if (MeekoTestUtils.matchEnvironment(meekoTestAnnotation, super.environment)) {

                    Object obj = applicationContext.getBean(clazz);

                    if (obj instanceof MeekoTestContext) {
                        MeekoTestContext meekoTestContext = (MeekoTestContext) obj;
                        meekoTestContext.setEnvironment(super.environment);
                        meekoTestContext.setTrace(super.trace);
                        meekoTestContext.workflow.start(clazz.getSimpleName());
                        Callable<?> callable = (Callable<?>) obj;
                        Future<?> future = executorService.submit(callable);
                        futureList.add(future);
                    }
                }
            }
        }
    }

    public String getPackageBase() {
        return packageBase;
    }

    public void setPackageBase(String packageBase) {
        this.packageBase = packageBase;
    }

    public int getFixedThreadPool() {
        return fixedThreadPool;
    }

    public void setFixedThreadPool(int fixedThreadPool) {
        this.fixedThreadPool = fixedThreadPool;
    }
}
