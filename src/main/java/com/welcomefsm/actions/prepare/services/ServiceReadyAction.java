package com.welcomefsm.actions.prepare.services;

import com.welcomefsm.Application.Events;
import com.welcomefsm.Application.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class ServiceReadyAction implements Action<States, Events>
{
    @Override
    public void execute(StateContext<States, Events> context)
    {
        StateMachine machine = context.getStateMachine();

        System.out.println("ServiceReadyAction: State " + machine.getState().getId() + "; Initial State: " + machine.getInitialState().getId());
    }
}
