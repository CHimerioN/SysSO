package sysos;

import java.util.ArrayList;
import sysos.process_manager.process;
import static sysos.process_manager.status.ACTIVE;
import static sysos.process_manager.status.READY;
import static sysos.process_manager.status.WAITING;

public class schedulerr {
	public int readyp;
	private Boolean eh=false;
	private Boolean eh2=false;
	public int x = 1;
	public ArrayList<Boolean> whichqs = new ArrayList<Boolean>();
	public schedulerr() {
		for(int i=0;i<128;i++){
			whichqs.add(false);
		}
		readyp=0;
		for(int i=0;i<128;i++){
			ArrayList<process> p=new ArrayList<process>();
			qs.add(p);
		}
	}
	public ArrayList<ArrayList<process>> qs = new ArrayList<ArrayList<process>>(128);

	public process runningProcess = Main.T.INIT;
	
	
	public void add_to_q(process x) {
		if(x.usrpri==null) {
			x.usrpri=x.pri;
		}
                
		qs.get(x.usrpri).add(x);
		whichqs.set(x.usrpri, true);
	}
	
	public void divide_cpu() {
		runningProcess.cpu = runningProcess.cpu/2;
		for(int i=0; i<127;i++) {
			for(int j=0;j<qs.get(i).size();j++) {
				if(qs.get(i).get(j).cpu>0)
                           qs.get(i).get(j).cpu /= 2;
				if((qs.get(i).get(j).pri+(qs.get(i).get(j).cpu/2))<127){
					qs.get(i).get(j).usrpri=qs.get(i).get(j).pri+(qs.get(i).get(j).cpu/2);
				} else {
					qs.get(i).get(j).usrpri=126;
				}
			}
		}
	}
	
	private void change_q() {
		qs.get(runningProcess.usrpri).add(runningProcess);
		whichqs.set(runningProcess.usrpri, true);
	}
	
	private void runProcess(process_manager p) {
		//clear_q();s
		eh=false;
		eh2=false;
		for(;readyp < Main.T.ready.size();readyp++) {
			add_to_q(Main.T.ready.get(readyp));
		}
		
		int i=0;
		int y=0;
		while(whichqs.get(i)==false) {							//szuka kolejki o najnizszym priorytecie
			if(whichqs.get(i+1)==true) {
				eh2=true;
				for(y=0; y<qs.get(i+1).size();y++) {
					if(!qs.get(i+1).get(y).s.equals(WAITING)) {eh=true;break;}
				}
			}
			
			if(eh==true) {i++;break;}
			if(eh2==true) {i++; eh2=false;}
			i++;
			if(i==127) return;
		}
																//ustawia pierwszy proces o najnizszym priorytecie jako running
		runningProcess=qs.get(i).get(y);
		runningProcess.change_process_state(ACTIVE);
		runningProcess.Lock=false;
		qs.get(i).remove(y);	
		if(qs.get(i).size()<1) whichqs.set(i, false);
	}

	public void check(process_manager p) {
        if(runningProcess == null || runningProcess.PID==0) {                        //jesli zaden proces nie jest running wchodzi do runProcess gdzie wlacza sie proces z najnizszym priorytetem
            runProcess(p);
            return;
        }
        if(runningProcess != null || runningProcess.PID!=0) {                        //zmienia wartoĂ„Ä…Ă˘â‚¬Ĺźci cpu procesu running i odkĂ„Ä…Ă˘â‚¬Ĺˇada go do kolejki procesĂ„â€šÄąâ€šw
            if(!runningProcess.s.equals(WAITING))
        	runningProcess.change_process_state(READY);
            runningProcess.Lock=true;
            change_q();
            runProcess(p);
            return;
        }
	}
}
