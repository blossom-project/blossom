package fr.mgargadennec.blossom.core.scheduler.job;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.Date;
import java.util.List;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
public class JobInfo {
    private JobKey key;
    private JobDetail detail;
    private List<? extends Trigger> triggers;
    private List<JobExecutionContext> jobExecutionContexts;

    public JobInfo(JobKey key) {
        this.key = key;
    }

    public JobKey getKey() {
        return key;
    }

    public JobDetail getDetail() {
        return detail;
    }

    public void setDetail(JobDetail detail) {
        this.detail = detail;
    }

    public List<? extends Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<? extends Trigger> triggers) {
        this.triggers = triggers;
    }

    public List<JobExecutionContext> getJobExecutionContexts() {
        return jobExecutionContexts;
    }

    public void setJobExecutionContexts(List<JobExecutionContext> jobExecutionContexts) {
        this.jobExecutionContexts = jobExecutionContexts;
    }

    public Date getLastExecutionTime(){
        return this.triggers.stream().map(t -> t.getPreviousFireTime()).max(Date::compareTo).orElse(null);
    }
}
