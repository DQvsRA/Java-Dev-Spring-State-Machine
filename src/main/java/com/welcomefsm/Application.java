package com.welcomefsm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.statemachine.StateMachine;

public class Application
{
    static public enum States {
        INITIAL, STARTUP, PREPARING, READY,
        STARTUP_SERVICES, SERVICES_READY,
        STARTUP_WORKER, WORKER_READY,
        END
    }

    static public enum Events {
        STARTUP, PREPARE,
        WORKER_READY, SERVICES_READY
    }

    static public void main(String[] args) throws Exception
    {
        Thread.currentThread().setName("Bootstrap");
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
    }

    private static class Starter implements ApplicationListener<ContextRefreshedEvent>
    {
        @Autowired
        @Qualifier("applicationStateMachine")
        StateMachine<States, Events> stateMachine;

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event)
        {
            System.out.println("onApplicationEvent: Starter");
            stateMachine.start();
        }
    }

    public static class ShutdownThread extends Thread {
        public void run() {
            System.out.println("Shutted down");
        }
    }
}

