package com.welcomefsm;

import com.welcomefsm.Application.Events;
import com.welcomefsm.Application.States;
import com.welcomefsm.actions.InitialAction;
import com.welcomefsm.actions.ReadyAction;
import com.welcomefsm.actions.StartupAction;
import com.welcomefsm.actions.prepare.services.ServiceReadyAction;
import com.welcomefsm.actions.prepare.services.ServiceStartupAction;
import com.welcomefsm.actions.prepare.workers.WorkerReadyAction;
import com.welcomefsm.actions.prepare.workers.WorkerStartupAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;

@Configuration
public class Config
{
    @Autowired private InitialAction            initialAction;
    @Autowired private StartupAction            startupAction;
    @Autowired private ReadyAction              readyAction;

    @Autowired private ServiceStartupAction     servicesStartupAction;
    @Autowired private ServiceReadyAction       servicesReadyAction;

    @Autowired private WorkerStartupAction      workerStartupAction;
    @Autowired private WorkerReadyAction        workerReadyAction;

    @Bean(name = "applicationStateMachine")
    public StateMachine<States, Events> buildMachine() throws Exception
    {
        Builder<States, Events> builder = StateMachineBuilder.builder();

        builder.configureStates()
            .withStates().initial(States.INITIAL)
                .state(States.INITIAL, initialAction)
                .state(States.STARTUP, startupAction)
                .state(States.PREPARING)
                .state(States.READY, readyAction)
                .and()
            .withStates()
                .parent(States.PREPARING).initial(States.STARTUP_WORKER)
                    .state(States.STARTUP_WORKER, workerStartupAction)
                    .state(States.WORKER_READY)
                    .and()
            .withStates()
                .parent(States.PREPARING).initial(States.STARTUP_SERVICES)
                    .state(States.STARTUP_SERVICES, servicesStartupAction)
                    .state(States.SERVICES_READY)
                    .and()
            .withStates()
                .parent(States.READY)
                    .end(States.END)
        ;

        builder.configureTransitions()
        .withExternal().source(States.INITIAL)          .target(States.STARTUP)         .event(Events.STARTUP).and()
        .withExternal().source(States.STARTUP)          .target(States.PREPARING)       .event(Events.PREPARE).and()
        .withExternal().source(States.STARTUP_SERVICES) .target(States.SERVICES_READY)  .event(Events.SERVICES_READY)   .action(servicesReadyAction).and()
        .withExternal().source(States.STARTUP_WORKER)   .target(States.WORKER_READY)    .event(Events.WORKER_READY)     .action(workerReadyAction)
        ;


        builder.configureConfiguration().withConfiguration().beanFactory(new StaticListableBeanFactory());  //see https://stackoverflow.com/questions/37925352/java-lang-illegalstateexception-bean-factory-must-be-instance-of-listablebeanfa/37926670#37926670
        return builder.build();
    }
}