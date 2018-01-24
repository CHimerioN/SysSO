package sysos;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class process_manager {

    //protezy
    String code;
    int a, b, c, d, co;

    boolean free_m(String name) {
        boolean success = true;
        return success;
    }

    boolean reserve_m(String file, String name, String c) {
        boolean success = true;
        if (c.isEmpty()) {
            code = "old code";
        } else {
            code = c;
        }
        return success;
    }

    //globalne
    public process INIT, pip;
    public int last_PID = 1;

    public class exit {

        public int who, res;
    }

    public class wait {

        public int who, for_who;
    }

    public ArrayList<exit> ex = new ArrayList<>();
    public ArrayList<wait> wa = new ArrayList<>();
    public ArrayList<process> ready = new ArrayList<>();

    //typ enum stanu procesu
    public enum status {

        NEW, ACTIVE, WAITING, READY, TERMINATED, ZOMBIE, INIT
    }

    public process find(int pid) {
        if (pid == 0) {
            return INIT;
        }
        process p = INIT;
        while (p.next != null) {
            p = p.next;
            if (p.PID == pid) {
                return p;
            }
        }
        System.out.println("Nie ma procesu o podanym PID!");
        return null;
    }

    public int find_name(String n) {
        if ("INIT".equals(n)) {
            return INIT.PID;
        }
        process p = INIT;
        while (p.next != null) {
            p = p.next;
            if (p.name.equals(n)) {
                return p.PID;
            }
        }
        System.out.println("Nie ma procesu o podanej nazwie!");
        return -1;
    }

    public ArrayList<process> ready_processes() {

        ArrayList<process> x = new ArrayList<>();
        for (wait i : wa) {
            ready.add(find(i.who));
        }
        return x;
    }

    public void init() {
        INIT = new process("INIT");
        INIT.s = status.INIT;
        INIT.PID = 0;
        INIT.PPID = 0;
        INIT.father = null;
        INIT.child = null;
        INIT.little_bro = null;
        INIT.big_bro = null;
        INIT.previous = null;
        INIT.next = null;
        INIT.pri = 127;
        INIT.usrpri=127;
        INIT.cpu = 0;
        INIT.counter=0;
        INIT.swapFileBeginning = 0;
        INIT.programSize = 4;
        INIT.createPageTable(4);
        
    }

    //klasa zagnieÄ‚â€šÄąÄ˝dÄ‚â€šÄąÄ˝ona procesu
    public class process {

        //wskaĂ„Ä…Ă‚Â¸niki na procesy pokrewne
        public process father, big_bro, little_bro, child;

        //ID
        public int PPID;
        public String name;

        //szeregowanie
        public status s;
        public int pri, cpu;
        public Integer usrpri, PID;
        public int id, move;
        public String code;
        public int programSize;
        public final Queue<Character> IO = new LinkedList<>();
        public boolean input;
        public boolean output;
        public int des;
        public boolean res_flag;
        public process previous, next;
        public boolean Lock;
        //kontekst procesu
        public int A, B, C, D, counter;

        public process(String n) {

            this.name = n;
            this.father = null;
            this.big_bro = null;
            this.little_bro = null;
            this.child = null;
            this.PID = 0;
            this.PPID = 0;
            this.s = status.NEW;
            this.pri = 0;
            this.cpu = 0;
            this.previous = null;
            this.next = null;
            this.A = 0;
            this.B = 0;
            this.C = 0;
            this.D = 0;
            this.counter = 0;
            this.Lock = true;
        }

        public void change_process_state(status status) {
            this.s = status;
        }

        public int free_PID() {
            if (!(last_PID >= 1 && last_PID <= 0xffff8000)) {
                last_PID = 1;
            }
            process p = INIT;
            while (p.next != null) {
                p = p.next;
            }
            last_PID = p.PID;
            return (last_PID + 1);
        }

        public void read_context() {
            this.A = a;
            this.B = b;
            this.C = c;
            this.D = d;
            this.counter = co;
        }

        public void zapisdoprocesu(String napis) {
            for (int i = 0; i < napis.length(); i++) {
                IO.offer(napis.charAt(i));
            }
        }

        public int fork(String nazwa) {
        	
            //utworzenie nowego procesu i nadanie mu nazwy
        	
            //String n_name = this.name + 'c';
            process p = new process(nazwa);
            p.PID = free_PID();
            //pamiĂ„â€šÄąĹľĂ„â€šĂ‚Â¦
            //if (reserve_m(this.name, this.name, "") != false) {
            p.s = status.READY;
            Random gen = new Random();
            int i = gen.nextInt(127);
            p.A = this.A;
            p.B = this.B;
            p.C = this.C;
            p.D = this.D;
            p.counter = this.counter;
            p.cpu = 0;
            p.pri = i;
            p.usrpri=i;
            p.PPID = this.PID;
            p.father = this;
            //rodzina
            if (this.child == null) {
                this.child = p;
            } else {
                process p1 = this.child;
                while (p1.little_bro != null) {
                    p1 = p1.little_bro;
                }
                p1.little_bro = p;
                p.big_bro = p1;
            }
	            //dodanie do listy
	            process p2 = this;
	            while (p2.next != null) {
	                p2 = p2.next;
	            }
            p2.next = p;
            p.next = null;
            p.previous = p2;
            //jeĂ„Ä…Ă˘â‚¬Ĺ›li proces zostaÄ‚â€šÄąâ€š poprawnie utworzony
            ready.add(p);
            System.out.println("Utworzono proces potomny o PID: " + p.PID);
            //show_process(p.PID);
            return p.PID;
            //} //jeĂ„Ä…Ă˘â‚¬Ĺ›li nie zostaÄ‚â€šÄąâ€š
            /*else {
             System.out.println("Nie utworzono procesu potomnego!");

             return -1;
             }*/
        }

        public boolean exec(String code, String path, int size) {
        			
        	process backup = Main.S.runningProcess;
        	Main.S.runningProcess = this;
            Main.M.allocateMemory(code, size);
        	Main.S.runningProcess = backup;
            
            this.A = 0;
            this.B = 0;
            this.C = 0;
            this.D = 0;
            this.counter = 0;
            this.code = code;
            this.s = status.READY;
            System.out.println("Proces o PID: " + this.PID + " otrzymal nowy kod do wykonania.");
            return true;
        }

        public boolean wait_PID() {
            int temp = -1;
            exit e = new exit();
            if (this.s == status.ZOMBIE) {
                System.out.println("Proces z PID: " + this.PID + " nie istnieje, wiĂ„â€šÄąĹľc nie moÄ‚â€šÄąÄ˝na wywoÄ‚â€šÄąâ€šaĂ„â€šĂ‚Â¦ tej metody na jego dziecku.");
            } else {
                if (this.child != null) {
                    //sprawdzenie czy proces jest na liĂ„Ä…Ă˘â‚¬Ĺ›cie zakoĂ„â€šĂ‚Â±czonych
                    for (int i = 0; i < ex.size(); i++) {
                        e = ex.get(i);
                        if (e.who == this.child.PID) {
                            temp = i;
                        }
                    }
                    //jeĂ„Ä…Ă˘â‚¬Ĺ›li jest
                    if (temp >= 0) {
                        e.res = 1;
                        int x = this.child.PID;
                        //usuniĂ„â€šÄąĹľcie go z listy
                        process p = INIT;
                        while (p.next != null) {
                            p = p.next;
                            if (p.PID == this.child.PID) {
                                System.out.println(p.PID);
                                process p1 = p.previous;
                                if (p.next != null) {
                                    process p2 = p.next;
                                    System.out.println(p1.PID);
                                    System.out.println(p2.PID);
                                    p1.next = p2;
                                    p2.previous = p1;
                                } else {
                                    p1.next = null;
                                }
                                p.previous = null;
                                p.next = null;
                                for (int i = 0; i < ex.size(); i++) {
                                    if (ex.get(i).who == this.child.PID) {
                                        ex.remove(i);
                                        break;
                                    }
                                }
                            }
                        }
                        this.child = null;
                        this.s = status.ACTIVE;
                        System.out.println("UsuniĂ„â€šÄąĹľto proces potomny o PID: " + x + " bo byÄ‚â€šÄąâ€š w stanie ZOMBIE," +
                                " a proces o PID: " + this.PID + " nie zmieniÄ‚â€šÄąâ€š stanu.");
                        //jeĂ„Ä…Ă˘â‚¬Ĺ›li nie ma
                    } else {
                        //jeĂ„Ä…Ă˘â‚¬Ĺ›li ACTIVE zczytujemy rejestry i licznik
                        if (this.s == status.ACTIVE) {
//OJJJJ
//OJJJJ
//OJJJJ
//OJJJJ
//OJJJJ
                            //read_context();
                        }
                        //zmieniamy stan na WAITING jeĂ„Ä…Ă˘â‚¬Ĺ›li to nie init
                        if (this.PID != 0) {
                            this.s = status.WAITING;
                        }
                        System.out.println("Proces o PID: " + this.PID + " zmienia stan an WAITING " +
                                "i czeka na zakoĂ„â€šĂ‚Â±czenie potomka o PID: " + this.child.PID + ".");
                        //dodanie do listy oczekujÄ‚â€šĂ„â€¦cych
                        wait w = new wait();
                        w.who = this.PID;
                        w.for_who = this.child.PID;
                        wa.add(w);
                        return false;
                    }
                }
            }
            //NWM MAX
            return false;
        }

        public boolean exit(int stat) {
            boolean del = false;
            int temp = -1;
            if (this.child != null) {
                if (this.child.s == status.ZOMBIE) {
                    this.child.s = status.TERMINATED;
                    process p5 = this.child.previous;
                    if (this.child.next != null) {
                        process p6 = this.child.next;
                        p6.previous = p5;
                        p5.next = p6;
                    } else {
                        p5.next = null;
                    }
                    this.child.previous = null;
                    this.child.next = null;
                    System.out.println("Dziecko procesu o PID: " + this.PID + " czyli " + this.child.PID + "zmieniĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąË‡ stan na TERMINATED.");
                }
                for (int i = 0; i < ex.size(); i++) {
                    if (ex.get(i).who == this.child.PID) {
                        ex.remove(i);
                        break;
                    }
                }
            }
            wait w = new wait();
            //czy wykonano wait_PID
            for (int i = 0; i < wa.size(); i++) {
                w = wa.get(i);
                if (w.for_who == this.PID) {
                    temp = i;
                }
            }
            //jeĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąĹşli wykonano wait_PID
            if (temp >= 0) {
                boolean only = true;
                //czy tylko na ten
                for (int i = 0; i < wa.size(); i++) {
                    wa.set(i, w);
                    if (w.who == this.PPID && w.for_who != this.PID) {
                        only = false;
                    }
                }
                //jeĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąĹşli jeszcze na jakiĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąĹş
                if (only == false) {
                    Main.M.deallocateMemory(this.PID);
                    if (free_m(this.name)) {
                        for (int i = 0; i < wa.size(); i++) {
                            if (wa.get(i).for_who == this.PID) {
                                wa.remove(i);
                            }
                        }
                        //zmiana statusu na ended
                        if (this.father.child == this) {
                            if (this.little_bro != null) {
                                this.father.child = this.little_bro;
                            } else {
                                this.father.child = null;
                            }
                        }
                        this.s = status.TERMINATED;
                        //zmiana wskaĂ„â€šĂ˘â‚¬Â¦Ä‚â€šÄąĹşnikĂ„â€šÄŹĹĽËťÄ‚â€šÄąâ€šw na liĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąĹşcie
                        process p1 = this.next;
                        process p2 = this.previous;
                        p1.previous = p2;
                        p2.next = p1;
                        this.previous = null;
                        this.next = null;
                        p1 = p2 = null;
                        if (this.little_bro != null) {
                            this.little_bro.big_bro = this.big_bro;
                        }
                        if (this.big_bro != null) {
                            this.big_bro.little_bro = this.little_bro;
                        }
                        //jeĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąĹşli ma dzieci
                        if (this.child != null) {
                            process p = this.child;
                            p.PPID = 0;
                            //doĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąË‡Ă„â€šĂ˘â‚¬ĹľÄ‚Ë�Ă˘â€šÂ¬Ă‚Â¦czenie do dzieci inita
                            if (INIT.child != null) {
                                //doĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąË‡Ă„â€šĂ˘â‚¬ĹľÄ‚Ë�Ă˘â€šÂ¬Ă‚Â¦czenie do najmĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąË‡odszego
                                process p4 = INIT.child;
                                while (p4.little_bro != null) {
                                    p4 = p4.little_bro;
                                }
                                p4.little_bro = p;
                                p.big_bro = p4;
                                //jeĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąĹşli ma rodzeĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąÄľstwo
                                if (this.big_bro != null) {
                                    process p3 = this.big_bro;
                                    p3.little_bro = this.little_bro;
                                }
                                if (this.little_bro != null) {
                                    process p3 = this.little_bro;
                                    p3.big_bro = this.big_bro;
                                }
                            }
                            //zmiana PPID dzieci
                            while (p.little_bro != null) {
                                p = p.little_bro;
                                p.PPID = 0;
                            }
                        }
                        System.out.println("UsuniĂ„â€šĂ˘â‚¬ĹľÄ‚Ë�Ă˘â‚¬ĹľĂ‹ďż˝to proces o PID: " + this.PID + ".");
                        //FINALIZE
                        del = true;
                        return del;
                    } else {
                        System.out.println("BĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąË‡Ă„â€šĂ˘â‚¬ĹľÄ‚Ë�Ă˘â€šÂ¬Ă‚Â¦d zwalniania pamiĂ„â€šĂ˘â‚¬ĹľÄ‚Ë�Ă˘â‚¬ĹľĂ‹ďż˝ci!");
                    }
                }
                //jeĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąĹşli nie wykonano wait_PID
            } else {
                Main.M.deallocateMemory(this.PID);
                if (free_m(this.name)) {
                    for (int i = 0; i < wa.size(); i++) {
                        if (wa.get(i).for_who == this.PID) {
                            process p = INIT;
                            while (p.next != null) {
                                p = p.next;

                            }

                        }
                    }
                    this.s = status.ZOMBIE;
                    if (this.child != null) {
                        process p = this.child;
                        p.PPID = 0;
                        while (p.little_bro != null) {
                            p = p.little_bro;
                            p.PPID = 0;
                        }
                        p = this.child;
                        if (INIT.child != null) {
                            //do najmĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąË‡odszego
                            process p2 = INIT.child;
                            while (p2.little_bro != null) {
                                p2 = p2.little_bro;
                            }
                            p2.little_bro = p;
                            p.big_bro = p2;
                            //jeĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąĹşli ma rodzeĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąÄľstwo
                            if (this.big_bro != null) {
                                process p3 = this.big_bro;
                                p3.little_bro = this.little_bro;
                            }
                        }
                    }
                    exit e = new exit();
                    e.who = this.PID;
                    e.res = stat;
                    ex.add(e);
                    System.out.println("Na procesie nie wykonano jeszcze metody wait_PID, wiĂ„â€šĂ˘â‚¬ĹľÄ‚Ë�Ă˘â‚¬ĹľĂ‹ďż˝c zostaĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąË‡ dodany do listy procesĂ„â€šÄŹĹĽËťÄ‚â€šÄąâ€šw ZOMBIE.");
                    del = true;
                    return del;
                } else {
                    System.out.println("Na procesie nie wykonano metody wait_PID, jednak wystĂ„â€šĂ˘â‚¬ĹľÄ‚Ë�Ă˘â€šÂ¬Ă‚Â¦piĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąË‡ bĂ„â€šĂ˘â‚¬Â¦Ä‚Ë�Ă˘â€šÂ¬ÄąË‡Ă„â€šĂ˘â‚¬ĹľÄ‚Ë�Ă˘â€šÂ¬Ă‚Â¦d pamiĂ„â€šĂ˘â‚¬ĹľÄ‚Ë�Ă˘â‚¬ĹľĂ‹ďż˝ci!");
                }
            }
            //NWM MAX
            return false;
        }

        public boolean kill(int pid) {
            if (pid == 0) {
                System.out.println("Ta operacja spowoduje zamkniĂ„â€šÄąĹľcie systemu!\nCzy na pewno chcesz jÄ‚â€šĂ„â€¦ wykonaĂ„â€šĂ‚Â¦?\n1- Tak/0 - Nie");
                int c;
                Scanner s = new Scanner(System.in);
                c = s.nextInt();
                if (c == 1) {
                    System.out.println("Zamykanie systemu...");
                    //FUNKCJA ZWALNIAJÄ‚â€šĂ„â€žCA CAÄ‚â€šÄąďż˝Ä‚â€šĂ„â€ž PAMIĂ„â€šÄąÂ Ă„â€šĂ˘â‚¬Â 
                    System.exit(0);
                }
                if (c == 0) {
                    System.out.println("Anulowano.");
                } else {
                    System.out.println("Wprowadzono zÄ‚â€šÄąâ€še dane!");
                }
                s.close();
            }
            process p = INIT;
            boolean is = false;
            //czy jest taki proces
            while (p.next != null) {
                p = p.next;
                if (p.PID == pid) {
                	if (find(pid)==Main.S.runningProcess) {
                        Main.S.runningProcess=INIT;
                    }
                    else {
                        Main.S.qs.get(find(pid).usrpri).remove(find(pid));
                        Main.S.whichqs.set(find(pid).usrpri,false);
                    }
                    for (int i = 0; i < wa.size(); i++) {
                        if (wa.get(i).who == p.PID) {
                            wa.remove(i);
                            break;
                        }
                    }
                    if (p.child != null) {
                        if (p.child.s == status.ZOMBIE) {
                            p.child.s = status.TERMINATED;
                            process p5 = p.child.previous;
                            if (p.child.next != null) {
                                process p6 = p.child.next;
                                p6.previous = p5;
                                p5.next = p6;
                            } else {
                                p5.next = null;
                            }
                            p.child.previous = null;
                            p.child.next = null;
                            System.out.println("Dziecko procesu " + p.PID + "zostaÄ‚â€šÄąâ€šo usuniĂ„â€šÄąĹľte.");
                        }
                        for (int i = 0; i < ex.size(); i++) {
                            if (ex.get(i).who == p.child.PID) {
                                ex.remove(i);
                                break;
                            }
                        }
                    }
                    //jeĂ„Ä…Ă˘â‚¬Ĺ›li ma stan zombie
                    if (p.s == status.ZOMBIE) {
                        System.out.println("Ten proces jest w stanie ZOMBIE, wiĂ„â€šÄąĹľc usuniĂ„â€šÄąĹľtĂ„â€šÄąĹľ zostanÄ‚â€šĂ„â€¦ tylko jego powiÄ‚â€šĂ„â€¦zania.");
                        process p2 = p.previous;
                        if (p.next != null) {
                            process p1 = p.next;
                            p1.previous = p2;
                            p2.next = p1;
                        } else {
                            p2.next = null;
                        }
                        p.previous = null;
                        p.next = null;
                        return false;
                    }
                    is = true;
                    break;
                }
            }

            if (is == true) {
                boolean del = false;
                int temp = -1;
                wait w = new wait();
                //sprawdzamy czy wykonano wait_PID
                for (int i = 0; i < wa.size(); i++) {
                    w = wa.get(i);
                    if (w.for_who == p.PID) {
                        temp = i;
                    }
                }

                //jeĂ„Ä…Ă˘â‚¬Ĺ›li tak
                if (temp >= 0) {
                    boolean only = true;

                    //czy tylko na niego
                    for (int i = 0; i < wa.size(); i++) {
                        wa.set(i, w);
                        if (w.who == p.PPID && w.for_who != p.PID) {
                            only = false;
                        }
                    }
                    //jeĂ„Ä…Ă˘â‚¬Ĺ›li na kogoĂ„Ä…Ă˘â‚¬Ĺ› jeszcze
                    if (only == false) {
                        if (free_m(p.name)) {
                            for (int i = 0; i < wa.size(); i++) {
                                if (wa.get(i).for_who == p.PID) {
                                    wa.remove(i);
                                }
                            }
                            //zmiana statusu
                            if (p.father.child == p) {
                                if (p.little_bro != null) {
                                    p.father.child = p.little_bro;
                                } else {
                                    p.father.child = null;
                                }
                            }
                            p.s = status.TERMINATED;
                            //zmiana wskaĂ„Ä…Ă‚Â¸nikĂ„â€šÄąâ€šw
                            process p2 = p.previous;
                            if (p.next != null) {
                                process p1 = p.next;
                                p1.previous = p2;
                                p2.next = p1;
                            } else {
                                p2.next = null;
                            }
                            p.previous = null;
                            p.next = null;
                            if (p.little_bro != null) {
                                p.little_bro.big_bro = p.big_bro;
                            }
                            if (p.big_bro != null) {
                                p.big_bro.little_bro = p.little_bro;
                            }
                            //jeĂ„Ä…Ă˘â‚¬Ĺ›li ma dzieci
                            if (p.child != null) {
                                process t = p.child;
                                t.PPID = 0;
                                //doÄ‚â€šÄąâ€šÄ‚â€šĂ„â€¦czenie do dzieci inita
                                if (INIT.child != null) {
                                    //doÄ‚â€šÄąâ€šÄ‚â€šĂ„â€¦czenie mÄ‚â€šÄąâ€šodszego brata
                                    process p4 = INIT.child;
                                    while (p4.little_bro != null) {
                                        p4 = p4.little_bro;
                                    }
                                    p4.little_bro = t;
                                    t.big_bro = p4;
                                    //jeĂ„Ä…Ă˘â‚¬Ĺ›li ma rodzieĂ„â€šĂ‚Â±stwo
                                    if (p.big_bro != null) {
                                        process p3 = p.big_bro;
                                        p3.little_bro = p.little_bro;
                                    }
                                    if (p.little_bro != null) {
                                        process p3 = p.little_bro;
                                        p3.big_bro = p.big_bro;
                                    }
                                }
                                //zmiana PPID dzieci
                                while (t.little_bro != null) {
                                    t = t.little_bro;
                                    t.PPID = 0;
                                }
                            }
                            System.out.println("UsuniĂ„â€šÄąĹľto proces o PID: " + p.PID + ".");
                            //FINALIZE
                            //p.finalize();
                            //p = null;
                            del = true;
                            return del;
                        } else {
                            System.out.println("BÄ‚â€šÄąâ€šÄ‚â€šĂ„â€¦d zwalniania pamiĂ„â€šÄąĹľci!");
                        }
                    } else {
                        if (free_m(p.name)) {
                            for (int i = 0; i < wa.size(); i++) {
                                if (wa.get(i).for_who == p.PID) {
                                    wa.remove(i);
                                    break;
                                }
                            }
                            //zmiana statusu
                            p.s = status.TERMINATED;
                            //zmiana statusu ojca na READY, bo tylko na ten proces czekaÄ‚â€šÄąâ€š
//TUTAJJJJJ
//TUTAJJJJJ
//TUTAJJJJJ
//TUTAJJJJJ
//TUTAJJJJJ
                            p.father.s = status.READY;
                            if (p.father.child == p) {
                                if (p.little_bro != null) {
                                    p.father.child = p.little_bro;
                                } else {
                                    p.father.child = null;
                                }
                            }
                            //zmiana wskaĂ„Ä…Ă‚Â¸nikĂ„â€šÄąâ€šw na liĂ„Ä…Ă˘â‚¬Ĺ›cie
                            process p2 = p.previous;
                            if (p.next != null) {
                                process p1 = p.next;
                                p1.previous = p2;
                                p2.next = p1;
                            } else {
                                p2.next = null;
                            }
                            p.previous = null;
                            p.next = null;
                            //jeĂ„Ä…Ă˘â‚¬Ĺ›li nie ma dzieci
                            if (p.child != null) {
                                process t = p.child;
                                t.PPID = 0;
                                //doÄ‚â€šÄąâ€šÄ‚â€šĂ„â€¦czenie do dzieci inita
                                if (INIT.child != null) {
                                    //doÄ‚â€šÄąâ€šÄ‚â€šĂ„â€¦czenie mÄ‚â€šÄąâ€šodszego brata
                                    p2 = INIT.child;
                                    while (p2.little_bro != null) {
                                        p2 = p2.little_bro;
                                    }
                                    p2.little_bro = t;
                                    t.big_bro = p2;
                                    //jeĂ„Ä…Ă˘â‚¬Ĺ›li ma rodzieĂ„â€šĂ‚Â±stwo
                                    if (p.big_bro != null) {
                                        process p3 = p.big_bro;
                                        p3.little_bro = p.little_bro;
                                    }
                                    if (p.little_bro != null) {
                                        process p3 = p.little_bro;
                                        p3.big_bro = p.big_bro;
                                    }
                                }
                                //zmiana PPID dzieci
                                while (t.little_bro != null) {
                                    t = t.little_bro;
                                    t.PPID = 0;
                                }
                            }

                            System.out.println("Proces o PID: " + p.PID + "zostaÄ‚â€šÄąâ€š usuniĂ„â€šÄąĹľty.");
                            //FINALIZE
                            del = true;
                            return del;
                        } else {
                            System.out.println("BÄ‚â€šÄąâ€šÄ‚â€šĂ„â€¦d zwalniania pamiĂ„â€šÄąĹľci!");
                        }
                    }
                    //jeĂ„Ä…Ă˘â‚¬Ĺ›li nie wykonano jeszcze wait_PID
                } else {
                    if (free_m(p.name)) {
                        if (p.father.child == p) {
                            if (p.little_bro != null) {
                                p.father.child = p.little_bro;
                            } else {
                                p.father.child = null;
                            }
                        }
                        //zmiana statusu
                        p.s = status.TERMINATED;
                        process p2 = p.previous;
                        if (p.next != null) {
                            process p1 = p.next;
                            p1.previous = p2;
                            p2.next = p1;
                        } else {
                            p2.next = null;
                        }
                        p.previous = null;
                        p.next = null;
                        if (p.little_bro != null) {
                            p.little_bro.big_bro = p.big_bro;
                        }
                        if (p.big_bro != null) {
                            p.big_bro.little_bro = p.little_bro;
                        }
                        if (p.child != null) {
                            process t = p.child;
                            t.PPID = 0;
                            //doÄ‚â€šÄąâ€šÄ‚â€šĂ„â€¦czenie dzieci do inita
                            if (INIT.child != null) {
                                //doÄ‚â€šÄąâ€šÄ‚â€šĂ„â€¦czenie mÄ‚â€šÄąâ€šodszego brata
                                p2 = INIT.child;
                                while (p2.little_bro != null) {
                                    p2 = p2.little_bro;
                                }
                                p2.little_bro = t;
                                t.big_bro = p2;
                                //jeĂ„Ä…Ă˘â‚¬Ĺ›li ma rodzeĂ„â€šĂ‚Â±stwo
                                if (p.big_bro != null) {
                                    process p3 = p.big_bro;
                                    p3.little_bro = p.little_bro;
                                }
                                if (p.little_bro != null) {
                                    process p3 = p.little_bro;
                                    p3.big_bro = p.big_bro;
                                }
                            }
                            //zmiana PID dzieci
                            while (t.little_bro != null) {
                                t = t.little_bro;
                                t.PPID = 0;
                            }
                        }
                        //dodanie procesu do listy zakoĂ„â€šĂ‚Â±czonych
                        exit e = new exit();
                        e.who = p.PID;
                        e.res = 0;
                        ex.add(e);
                        System.out.println("Na procesie nie wykonano jeszcze metody wait_PID, wiĂ„â€šÄąĹľc zostaÄ‚â€šÄąâ€š dodany do listy procesĂ„â€šÄąâ€šw ZOMBIE.");
                        del = true;
                        return del;
                    } else {
                        System.out.println("Na procesie nie wykonano metody wait_PID, jednak wystÄ‚â€šĂ„â€¦piÄ‚â€šÄąâ€š bÄ‚â€šÄąâ€šÄ‚â€šĂ„â€¦d pamiĂ„â€šÄąĹľci!");
                    }
                }

            }
            if (is == false) {
                System.out.println("Nie istnieje taki proces.");
            }
            //NWM MAX
            return false;
        }

        public process find_process(int pid) {
            if (pid == 0) {
                return INIT;
            }
            process p = INIT;
            while (p.next != null) {
                p = p.next;
                if (p.PID == pid) {
                    return p;
                }
            }
            System.out.println("Nie ma procesu o podanym PID!");
            return null;
        }

        public void show_list() {
            process i = INIT;
            System.out.println("PID/PPID/STATUS\n" + i.PID + "/" + i.PPID + "/" + i.s);
            while (i.next != null) {
                i = i.next;
                System.out.println(i.PID + "/" + i.PPID + "/" + i.s);
            }
        }

        public void show_process(int pid) {
            process p1;
            process p;

            if (find_process(pid) != null) {

                p1 = find_process(pid);
                System.out.println("PID: " + p1.PID + "\nPPID: " + p1.PPID);

                System.out.println("Status: " + p1.s + "\nPriorytet: " + p1.pri + "\nRejestr A: " + p1.A
                        + "\nRejestr B: " + p1.B + "\nRejestr C: " + p1.C + "\nLicznik: " + p1.counter);

                System.out.println("Rodzina:");
                if (p1.child != null) {
                    System.out.println("PID dziecka: " + p1.child.PID);
                    p = p1.child;
                    while (p.little_bro != null) {
                        p = p.little_bro;
                        System.out.println(p.PID);
                    }
                } else {
                    System.out.println("Proces nie ma dziecka.");
                }

                if (p1.little_bro != null) {
                    System.out.println("PID mÄ‚â€šÄąâ€šodszego brata: " + p1.little_bro.PID);
                } else {
                    System.out.println("Proces nie ma mÄ‚â€šÄąâ€šodszego brata.");
                }
                if (p1.big_bro != null) {
                    System.out.println("PID starszego brata: " + p1.big_bro.PID);
                } else {
                    System.out.println("Proces nie ma starszego brata.");
                }

            } else {
                System.out.println("Nie ma procesu o podanym PID!");
            }
        }

        public void show_waiting() {
            for (int i = 0; i < wa.size(); i++) {
                System.out.println(wa.get(i).who + " - " + wa.get(i).for_who);
            }
        }

        public void show_zombie() {
            for (int i = 0; i < ex.size(); i++) {
                System.out.println(ex.get(i).who + " - " + ex.get(i).res);
            }
        }

        ////////////// TUTAJ_PROSZE_NIE_RUSZAC_~FILIP////////////////
        public class Pair {

            public boolean isInRam = false;
            public int inWhichFrame = -1;
        }

        public int swapFileBeginning; // gdize w pliku wymiany zaczyna sie porgram
        public Pair[] pageTable;

        public void createPageTable(int SIZE) {
            pageTable = new Pair[(SIZE + 15) / 16];
            for (int i = 0; i < pageTable.length; i++) {
                pageTable[i] = new Pair();
            }
        }

        public int pageCheck(int what) {
            if (pageTable[what].isInRam) {
                return pageTable[what].inWhichFrame;
            } else {
                return -1;
            }
        }

        public void pageDisable(int what) {

            for (int i = 0; i < pageTable.length; i++) {
                if (pageTable[i].inWhichFrame == what) {
                    pageTable[i].isInRam = false;
                    pageTable[i].inWhichFrame = -1;
                }
            }

        }

        public void pageEnable(int what, int where) {
            if (!pageTable[what].isInRam) {
                pageTable[what].isInRam = true;
                pageTable[what].inWhichFrame = where;
            } else //	System.out.println("STRONICA ZNAJDUJE SIE JUZ W RAMIE");
            ///////////////////////TUTAJ_JUZ_SPOKO_MOZNA/////////////////////////
            {

            }
        }

    }
}

