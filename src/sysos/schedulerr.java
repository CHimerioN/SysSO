package sysos;

import java.util.ArrayList;
import sysos.process_manager.process;
import static sysos.process_manager.status.ACTIVE;
import static sysos.process_manager.status.READY;

public class schedulerr {
	public int readyp;
	private ArrayList<Boolean> whichqs = new ArrayList<Boolean>();
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
	private ArrayList<ArrayList<process>> qs = new ArrayList<ArrayList<process>>(128);

	public process runningProcess = Main.T.INIT;
	
	
	public void add_to_q(process x) {
		if(x.usrpri==null) {
			x.usrpri=x.pri;
		}
                
		qs.get(x.usrpri).add(x);
		whichqs.set(x.usrpri, true);
	}
	
	private void divide_cpu() {
		runningProcess.cpu /= 2;
		for(int i=0; i<128;i++) {
			for(int j=0;j<qs.get(i).size();j++) {
				if(qs.get(i).get(j).cpu>0)
                                qs.get(i).get(j).cpu /= 2;
			}
		}
	}
	
	private void change_q() {
		runningProcess.usrpri=runningProcess.pri+(runningProcess.cpu/2);
		qs.get(runningProcess.usrpri).add(runningProcess);
		whichqs.set(runningProcess.usrpri, true);
	}
	
	private void runProcess(process_manager p) {
		//ArrayList<process> ready = Main.T.ready;			//dodaje do kolejki gotowe procesy
		for(;readyp < Main.T.ready.size();readyp++) {
			add_to_q(Main.T.ready.get(readyp));
		}
		
		int i=0;
		while(whichqs.get(i)==false) {							//szuka kolejki o najnizszym priorytecie
			if(whichqs.get(i)==true) break;
			i++;
			if(i==127) return;
		}
																//ustawia pierwszy proces o najnizszym priorytecie jako running
		runningProcess=qs.get(i).get(0);
		runningProcess.change_process_state(ACTIVE);
		runningProcess.Lock=false;
		qs.get(i).remove(0);	
		whichqs.set(i, false);
	}

	public void check(process_manager p) {
		if(runningProcess == null) {						//jesli zaden proces nie jest running wchodzi do runProcess gdzie wlacza sie proces z najnizszym priorytetem			
			runProcess(p);										
			return;
		}
		if(runningProcess != null) {						//zmienia wartości cpu procesu running i odkłada go do kolejki procesów
			runningProcess.change_process_state(READY);		
			runningProcess.Lock=true;
			divide_cpu();
			change_q();
			runProcess(p);
			return;
		}
		else {
			runningProcess.change_process_state(READY);		
			runningProcess.Lock=true;
			return;
		}
	}
}
