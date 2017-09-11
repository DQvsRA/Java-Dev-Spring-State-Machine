package com.welcomefsm.actions;

import com.welcomefsm.Application.Events;
import com.welcomefsm.Application.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class ReadyAction implements Action<States, Events>
{
    @Override
    public void execute(StateContext<States, Events> context)
    {
        System.out.println("ReadyAction: State " + context.getStateMachine().getState().getId());
    }
}
