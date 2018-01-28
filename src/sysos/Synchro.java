/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sysos;

import sysos.process_manager.process;

/**
 *
 * @author Matitam
 */
public class Synchro {

    public String nazwa;
    
    public int PROCESS_FROM_MEMORY = 0;
    public int PROCESS_FROM_QUEUE_OF_PROCESSESS = 0;
    
    public boolean lock;
    
    public Synchro(String nazwa)
    {
        this.nazwa = nazwa;
        this.lock = Main.S.runningProcess.Lock;
    }
    public boolean Test_and_Set(Boolean lock)
    {
        if(lock == true)
        {
            System.out.println("INSIDE");
            return true;
        }
        System.out.println("OUTSIDE");
        return false;
    }
    public boolean Compare_And_Swap(Boolean OLD_VALUE, boolean EXPECTED_VALUE, boolean NEW_VALUE)
    {
        boolean TEMP = OLD_VALUE;
        if(OLD_VALUE==EXPECTED_VALUE)
        {
            System.out.println("INSIDE LOOP");
            OLD_VALUE=NEW_VALUE;
        }
        System.out.println("OUTSIDE LOOP");
        return TEMP;
    }
    
    
    public void TO_CRITICAL_SECTION_TAS(process p)  
    {
        
        System.out.println(nazwa+" GOT INSIDE CRITICAL SECTION");
        if(Test_and_Set(lock)==true)
        {
             p.change_process_state(process_manager.status.WAITING);         
        }
        else{
             p.change_process_state(process_manager.status.READY);
        }
        
    }
    
    public void TO_CRITICAL_SECTION_CAS() throws InterruptedException 
    {
        System.out.println(nazwa+" GOT INSIDE CRITICAL SECTION");
        while(Compare_And_Swap(lock,true,false))
        {
            Thread.sleep(500);
            System.out.println("BREAK");
            break;
        }
        lock = false;
    }
    
    public void OFF_CRITICAL_SECTION()
    {
        System.out.println(nazwa+" GOT OFF CRITICAL SECTION");
        lock = false;
    }
}
