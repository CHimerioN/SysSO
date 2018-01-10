package sysos;

import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Queue;


/**
 *
 * @author boromir
 */
public class Potoki {

    /**
     * @param args the command line arguments
     */
    //struktura danych
    Queue<Character> myQueue = new LinkedList<Character>();
    int qfreespace = 16;//max rozmiar na dane
    int readbytes = 0;//zapisane bity
    int writebytes = 0;//odczytane bity
    int open = 0;//czy potok jest wykorzystywany przez potok

//funkcje odczytu z potoku
   static public int read(process p) {
        if (myQueue.peek() == null) {
            return 0;
        } else {
            while (myQueue.peek() != null) {
                p.next.IO.offer(myQueue.poll());
                readbytes++;
                qfreespace++;
            }
            return 1;
        }

    }

    //funcja zapisu do potoku, zwraca 0 dla błędu 1 dla zapisania wszystkich info,2 dla przepałnienia 
   static public int write(process p) {
       Character znak ;//buffor znaku
        if (qfreespace == 0) {
            return 0;
        } else {
            while (p.IO.peek() != null) {
                znak = p.IO.poll();
                myQueue.offer(znak);
                qfreespace--;
                 if (qfreespace == 0) {//zapełnienie całego potoku tutaj powinna być synchronizacja
            return 2;            
            }
        }
        return 1;
        }
    }

    //ta funkcja chyba powinna być w klasie procesu
   static void pipe(plik1 plik)//służy do utworzenia potoku
    {
        //proces znajduje wolny deskryptor inicjalizuje swoje indexy deskryptora;
        int index = plik.finddes();//od obiektu file
        if (index == -1) {
            System.out.println("Błąd deskryptora");
        } else {
            //this->des[1]=index;
            //this->next.des[0]=index;
        }
    }
    
}
