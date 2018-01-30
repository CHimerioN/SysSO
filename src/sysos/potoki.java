package sysos;

import java.util.LinkedList;
import java.util.Queue;

import sysos.process_manager.process;

public class potoki {
	Queue<Character> myQueue = new LinkedList<Character>();
	int qfreespace = 256;// max rozmiar na dane
	int readbytes = 0;// zapisane bity
	int writebytes = 0;// odczytane bity
	int open = 0;// czy potok jest wykorzystywany przez potok
       public Boolean ojciec_do_syn=true;//kierunek zapisu
      public int wskojciec;
       
	// funkcje odczytu z potoku
	static public int read(process p) {
            
            Synchro s1=new Synchro("name");//obiekt synchronizacji       
            Synchro s2=new Synchro("name");
        	int index = p.des;
		potoki ref = Main.P.tab[index];
                /////////////////////
                /////////////////////
                if(p.PID==ref.wskojciec)
                {
                    ref.ojciec_do_syn=false;
                }
                else
                {
                    ref.ojciec_do_syn=true;
                }
                ////////////////////
	           if (ref.myQueue.peek() == null) {
                if (ref.ojciec_do_syn) {
                    s1.lock = true;
                    s2.lock = false;
                    s1.TO_CRITICAL_SECTION_TAS(p);//zawieszamy syna
                    s2.TO_CRITICAL_SECTION_TAS(p.previous);//odwieszamy ojca by mógł zapisac info     
                } else {//odwrotny kierunek komunikacji z syna->ojca
                    s1.lock = true;
                    s2.lock = false;
                    s1.TO_CRITICAL_SECTION_TAS(p);//zawieszamy ojca
                    s2.TO_CRITICAL_SECTION_TAS(p.next);//odwieszamy syna by mógł zapisac info     
                }
                return 0;   //zwracamy 0 w przypadku braku tekstu do odczytu                  
            }
                else {//sa dane do odczytu
			while (ref.myQueue.peek() != null) {
				p.IO.offer(ref.myQueue.poll());
				ref.readbytes++;
				ref.qfreespace++;
                }               
                return 1;//zwracamy 1 po odczytaniu tekstu
            }
	}

    // funcja zapisu do potoku, zwraca 0 w przypadku braku miejsca i 1 po udanym zapisie do potoku 2 w przypadku przepelnienia potoku
    static public int write(process p) {
        Character znak;// buffor znaku
        int index = p.des;
        potoki ref = Main.P.tab[index];
        Synchro s1 = new Synchro("name");//obiekt synchronizacji dla procesu p1   
        Synchro s2 = new Synchro("name");//obiekt synchronizacji dla procesu p2
        ////////////////////
        ////////////////////
        if (p.PID == ref.wskojciec) {
            ref.ojciec_do_syn = true;
        } else {
            ref.ojciec_do_syn = false;
        }
        /////////////////////////
        
        if (ref.qfreespace == 0) {//jesli nie ma miejsca w potoku
            if (ref.ojciec_do_syn) {
                s1.lock = true;
                s2.lock = false;
                s1.TO_CRITICAL_SECTION_TAS(p);//zawieszamy ojca
                s2.TO_CRITICAL_SECTION_TAS(p.next);//odwieszamy syna by mogl odczytac info     
            } else {//odwrotny kierunek komunikacji z syna->ojca
                s1.lock = false;
                s2.lock = true;
                s1.TO_CRITICAL_SECTION_TAS(p);//zawieszamy syna
                s2.TO_CRITICAL_SECTION_TAS(p.previous);//odwieszamy ojca by mogl odczytac info     
            }
            return 0;//brak miejsca na dane
        } else {//jesli w potoku jest miejsce na dane
            while (p.IO.size() > 0) {
                znak = p.IO.poll();
                ref.myQueue.offer(znak);
                ref.qfreespace--;
                if (ref.qfreespace == 0) {// jezeli komunikat ktory chcemy przeslac jest za duzy                 
                    if (ref.ojciec_do_syn) {
                        s1.lock = true;//dla ojca
                        s2.lock = false;//dla syna
                        s1.TO_CRITICAL_SECTION_TAS(p);//zawieszamy ojca
                        s2.TO_CRITICAL_SECTION_TAS(p.next);//odwieszamy syna by mógł odczytać info     
                    } else {//odwrotny kierunek komunikacji z syna->ojca;syn pisze ojciec czyta
                        s1.lock = false;
                        s2.lock = true;
                        s1.TO_CRITICAL_SECTION_TAS(p);//zawieszamy syna
                        s2.TO_CRITICAL_SECTION_TAS(p.previous);//odwieszamy ojca by mógł odczytać info     
                    }
                    return 2;//informacja o przepelnieniu potoku
                }
            }                   
                if (ref.ojciec_do_syn) {
                    s1.lock = false;//dla ojca
                    s2.lock = false;//dla syna
                    s1.TO_CRITICAL_SECTION_TAS(p);//zawieszamy ojca
                    s2.TO_CRITICAL_SECTION_TAS(p.next);//odwieszamy syna by mógł odczytać info     
                } else {//odwrotny kierunek komunikacji z syna->ojca;syn pisze ojciec czyta
                    s1.lock = false;
                    s2.lock = false;
                    s1.TO_CRITICAL_SECTION_TAS(p);//zawieszamy syna
                    s2.TO_CRITICAL_SECTION_TAS(p.previous);//odwieszamy ojca by mógł odczytać info     
                }
                return 1;//wiadomosc przeslana bez problemu
        }
    }

	// funkcja odpowiedzialna za utworzenie potoku
	static void pipe(process p)// sÄąâ€šuÄąÄ˝y do utworzenia potoku
	{
		// proces znajduje wolny deskryptor inicjalizuje swoje indexy deskryptora;
		
                int index = Main.P.finddes();// od obiektu file
		if (index == -1) {
			System.out.println("BÄąâ€šĂ„â€¦d deskryptora");
		} else {
			Main.P.tab[index].open = 1;
			p.des = index;
			p.next.des = index;			
		}
               Main.P.tab[index].wskojciec=p.PID;
	}

}
