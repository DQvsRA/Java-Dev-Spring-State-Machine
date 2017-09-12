package com.welcomefsm.actions.prepare.workers;

import com.welcomefsm.Application.Events;
import com.welcomefsm.Application.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class WorkerStartupAction implements Action<States, Events>
{
    @Override
    public void execute(StateContext<States, Events> context)
    {
        StateMachine machine = context.getStateMachine();

        System.out.println("WorkerStartupAction: State " + machine.getState().getId() + "; Initial State: " + machine.getInitialState().getId());
        System.out.println("WorkerStartupAction: Send event WORKER_READY");

        machine.sendEvent(Events.WORKER_READY);
    }
}
