package sysos;
//xd
import sysos.process_manager.process;

public class plik1 {
	public potoki a=new potoki();
    public potoki[] tab= {a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a};
    static int rozmiar=16;
    
       public int finddes()
       {
           int number=-1;
           for(int i=0;i<16;i++)
           {
               if(tab[i].open==0)
               {
                   tab[i].open=1;
                   number=i;
                   break;
               }
           }        
           return number;
       }
       
      public void closedes(process p)
       {
           int numb=p.des;
           if(tab[numb].readbytes==tab[numb].writebytes)
           {
           tab[numb].open=0;
           for(int i=tab[numb].qfreespace;i>=0;i--)
               tab[numb].myQueue.poll();
           }
       }     
    
}
