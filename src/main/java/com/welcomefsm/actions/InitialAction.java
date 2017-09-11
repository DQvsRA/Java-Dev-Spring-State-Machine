package com.welcomefsm.actions;

import com.welcomefsm.Application.Events;
import com.welcomefsm.Application.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class InitialAction implements Action<States, Events>
{
    @Override
    public void execute(StateContext<States, Events> context)
    {
        System.out.println("InitialAction: Send event STARTUP");
        context.getStateMachine().sendEvent(Events.STARTUP);
    }
}
