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

	// funkcje odczytu z potoku
	static public int read(process p) {
            if(p.Lock1==true){         
            return -1;//zwracam w przypadku zamkniętego procesu z powodu braku komunikacji
            }
		int index = p.des;
		potoki ref = Main.P.tab[index];
		if (ref.myQueue.peek() == null) {
			
			return 0;
		} else {
			while (ref.myQueue.peek() != null) {
				p.IO.offer(ref.myQueue.poll());
				ref.readbytes++;
				ref.qfreespace++;
			}
			return 1;
		}
	}

	// funcja zapisu do potoku, zwraca 0 dla bĹ‚Ä™du 1 dla zapisania wszystkich
	// info,2 dla przepaĹ‚nienia
	static public int write(process p) {
		Character znak;// buffor znaku
		int index = p.des;
		potoki ref = Main.P.tab[index];
		if (ref.qfreespace == 0) {
			return 0;
		} else {
			while (p.IO.peek() != null) {
				znak = p.IO.poll();
				ref.myQueue.offer(znak);
				ref.qfreespace--;
				if (ref.qfreespace == 0) {// zapeĹ‚nienie caĹ‚ego potoku tutaj powinna byÄ‡ synchronizacja
					p.next.Lock=false;//ustawiam na false by odplokowa�
					return 2;
				}
			}
			p.next.Lock1=false;
                        p.next.change_process_state(process_manager.status.READY);//nie jestem 100% pewny ale tak się chyba budzi
			return 1;
		}
	}

	// funkcja odpowiedzialna za utworzenie potoku
	static void pipe(process p)// sĹ‚uĹĽy do utworzenia potoku
	{
		// proces znajduje wolny deskryptor inicjalizuje swoje indexy deskryptora;
		int index = Main.P.finddes();// od obiektu file
		if (index == -1) {
			System.out.println("BĹ‚Ä…d deskryptora");
		} else {
			Main.P.tab[index].open = 1;
			p.des = index;
			p.next.des = index;
			p.next.Lock1=true;
		}
	}

}
