package com.welcomefsm;

import com.welcomefsm.Application.Events;
import com.welcomefsm.Application.States;
import com.welcomefsm.actions.InitialAction;
import com.welcomefsm.actions.ReadyAction;
import com.welcomefsm.actions.StartupAction;
import com.welcomefsm.actions.services.ServiceReadyAction;
import com.welcomefsm.actions.services.ServiceStartupAction;
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
    @Autowired private InitialAction initialAction;
    @Autowired private StartupAction startupAction;
    @Autowired private ReadyAction readyAction;
    @Autowired private ServiceStartupAction servicesStartupAction;
    @Autowired private ServiceReadyAction servicesReadyAction;

    @Bean(name = "applicationStateMachine")
    public StateMachine<States, Events> buildMachine() throws Exception
    {
        Builder<States, Events> builder = StateMachineBuilder.builder();

        builder.configureStates()
            .withStates()
                .initial(States.INITIAL)
                .state(States.INITIAL, initialAction)
                .state(States.STARTUP, startupAction)
                .state(States.PREPARING)
                .and()
            .withStates()
                .parent(States.PREPARING)
                    .initial(States.READY)
                    .state(States.READY, readyAction)
                    .and()
            .withStates()
                .parent(States.PREPARING)
                    .initial(States.SERVICES_STARTUP)
                    .state(States.SERVICES_STARTUP, servicesStartupAction)
                    .state(States.SERVICES_READY)
                    .and()
            .withStates()
                .end(States.END)
        ;

        builder.configureTransitions()
            .withExternal()
                .source(States.INITIAL).event(Events.STARTUP).target(States.STARTUP)
                .and()
            .withExternal()
                .source(States.STARTUP).event(Events.PREPARE).target(States.PREPARING)
                .and()
            .withExternal()
                .source(States.SERVICES_STARTUP).event(Events.SERVICES_READY).target(States.SERVICES_READY)
        ;


        builder.configureConfiguration().withConfiguration().beanFactory(new StaticListableBeanFactory());  //see https://stackoverflow.com/questions/37925352/java-lang-illegalstateexception-bean-factory-must-be-instance-of-listablebeanfa/37926670#37926670
        return builder.build();
    }
}