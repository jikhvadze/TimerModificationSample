package com.example;

import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("timerModifier")
public class TimerModifierDelegate implements JavaDelegate {

    private ManagementService managementService;

    @Autowired
    public void setManagementService(ManagementService managementService) {
        this.managementService = managementService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Job timerJob = managementService.createJobQuery()
                .processInstanceId(delegateExecution.getProcessInstanceId())
                .activityId("cancellationTimer")
                .timers()
                .singleResult();

        managementService.suspendJobById(timerJob.getId());
        managementService.setJobDuedate(timerJob.getId(), new Date(System.currentTimeMillis() + 2000));
    }
}
