package com.cms.scaffold.route.operate.job.init;

import com.cms.scaffold.common.constant_manual.BasicsConstantManual;
import com.cms.scaffold.job.jobManager.ao.JobInfoAO;
import com.cms.scaffold.job.jobManager.bo.JobInfoBO;
import com.cms.scaffold.job.jobManager.service.JobInfoService;
import com.cms.scaffold.route.operate.job.utils.JobTaskUtil;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author zhangjiaheng@gmail.com
 * @Description 程序启动之后 开始执行所有的定时任务
 **/

@Component
//@Order(2) 如果有多个类需要启动后执行 order注解中的值为启动的顺序
public class StartAllJobInit implements CommandLineRunner {

    protected Logger logger = LoggerFactory.getLogger(getClass().getName());
    @Autowired
    JobInfoService jobInfoService;

    @Autowired
    JobTaskUtil jobTaskUtil;

    @Value("${job.start.env}")
    String jobEnv;

    @Override
    public void run(String... args) {
        JobInfoAO jobInfoAO = new JobInfoAO();
        jobInfoAO.setJobEnv(jobEnv);
        jobInfoAO.setStartWithrun(1);
        List<JobInfoBO> list = jobInfoService.findList(jobInfoAO);
        for (JobInfoBO jobinfo :list) {
            try {
                if("0".equals(jobinfo.getStartWithrun())){
                    logger.info("任务{}未设置自动启动。", jobinfo.getJobName());
                    jobInfoService.updateJobStatus(jobinfo.getId(), BasicsConstantManual.BASICS_SYS_JOB_STATUS_STOP);
                }else{
                    logger.info("任务{}设置了自动启动。", jobinfo.getJobName());
                    jobTaskUtil.addOrUpdateJob(jobinfo);
                    jobInfoService.updateJobStatus(jobinfo.getId(), BasicsConstantManual.BASICS_SYS_JOB_STATUS_STARTING);
                }
            } catch (SchedulerException e) {
                logger.error("执行定时任务出错，任务名称 {} ", jobinfo.getJobName());
            }
        }
    }
}
