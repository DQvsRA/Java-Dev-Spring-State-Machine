package com.welcomefsm.actions;

import com.welcomefsm.Application.Events;
import com.welcomefsm.Application.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class StartupAction implements Action<States, Events>
{
    @Override
    public void execute(StateContext<States, Events> context)
    {
        System.out.println("StartupAction: Send event PREPARE");
        context.getStateMachine().sendEvent(Events.PREPARE);
    }
}
