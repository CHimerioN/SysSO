package sysos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class interpreter {
	public Memory m;
	public FileSystem f;
	interpreter(Memory m, FileSystem f)
	{
		this.m=m;
		this.f=f;
	}
void exe()
{
	if(Main.S.x==2){ Main.S.check(Main.T); Main.S.x=0; }
	Main.S.x++;
	String roz;
	roz=m.readUntilSpace(Main.S.runningProcess.counter);
	Main.S.runningProcess.counter+=roz.length()+1;
	System.out.println("Nazwa procesu: " + Main.S.runningProcess.name);
	System.out.println("Rozkaz: " + roz);
	switch(roz)
	{
	case "AD":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		if(roz.equals("R1")) idrej=1;
		if(roz.equals("R2")) idrej=2;
		if(roz.equals("R3")) idrej=3;
		if(roz.equals("R4")) idrej=4;
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 3);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readUntilSpace(p));
		}
		if(roz.equals("R1"))
			liczba=Main.S.runningProcess.A;
		else if(roz.equals("R2"))
			liczba=Main.S.runningProcess.B;
		else if(roz.equals("R3"))
			liczba=Main.S.runningProcess.C;
		else if(roz.equals("R4"))
			liczba=Main.S.runningProcess.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			Main.S.runningProcess.A+=liczba;
		if(idrej==2)
			Main.S.runningProcess.B+=liczba;
		if(idrej==3)
			Main.S.runningProcess.C+=liczba;
		if(idrej==4)
			Main.S.runningProcess.D+=liczba;
	} break;
	case "SU":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		if(roz.equals("R1")) idrej=1;
		if(roz.equals("R2")) idrej=2;
		if(roz.equals("R3")) idrej=3;
		if(roz.equals("R4")) idrej=4;
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 3);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readUntilSpace(p));
		}
		if(roz.equals("R1"))
			liczba=Main.S.runningProcess.A;
		else if(roz.equals("R2"))
			liczba=Main.S.runningProcess.B;
		else if(roz.equals("R3"))
			liczba=Main.S.runningProcess.C;
		else if(roz.equals("R4"))
			liczba=Main.S.runningProcess.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			Main.S.runningProcess.A-=liczba;
		if(idrej==2)
			Main.S.runningProcess.B-=liczba;
		if(idrej==3)
			Main.S.runningProcess.C-=liczba;
		if(idrej==4)
			Main.S.runningProcess.D-=liczba;
	} break;
	case "MU":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		if(roz.equals("R1")) idrej=1;
		if(roz.equals("R2")) idrej=2;
		if(roz.equals("R3")) idrej=3;
		if(roz.equals("R4")) idrej=4;
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 3);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readUntilSpace(p));
		}
		if(roz.equals("R1"))
			liczba=Main.S.runningProcess.A;
		else if(roz.equals("R2"))
			liczba=Main.S.runningProcess.B;
		else if(roz.equals("R3"))
			liczba=Main.S.runningProcess.C;
		else if(roz.equals("R4"))
			liczba=Main.S.runningProcess.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			Main.S.runningProcess.A*=liczba;
		if(idrej==2)
			Main.S.runningProcess.B*=liczba;
		if(idrej==3)
			Main.S.runningProcess.C*=liczba;
		if(idrej==4)
			Main.S.runningProcess.D*=liczba;
	} break;
	case "MO":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		if(roz.equals("R1")) idrej=1;
		if(roz.equals("R2")) idrej=2;
		if(roz.equals("R3")) idrej=3;
		if(roz.equals("R4")) idrej=4;
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 3);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readUntilSpace(p));
		}
		if(roz.equals("R1"))
			liczba=Main.S.runningProcess.A;
		else if(roz.equals("R2"))
			liczba=Main.S.runningProcess.B;
		else if(roz.equals("R3"))
			liczba=Main.S.runningProcess.C;
		else if(roz.equals("R4"))
			liczba =Main.S.runningProcess.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			Main.S.runningProcess.A=liczba;
		if(idrej==2)
			Main.S.runningProcess.B=liczba;
		if(idrej==3)
			Main.S.runningProcess.C=liczba;
		if(idrej==4)
			Main.S.runningProcess.D=liczba;
	} break;
	case "JP":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		Main.S.runningProcess.counter=Integer.valueOf(roz);
	} break;
	case "JZ":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		String rez=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=rez.length()+1;
		if(rez.equals("R1"))
			if(Main.S.runningProcess.A!=0)
				Main.S.runningProcess.counter=Integer.valueOf(roz);
		if(rez.equals("R2"))
			if(Main.S.runningProcess.B!=0)
				Main.S.runningProcess.counter=Integer.valueOf(roz);
		if(rez.equals("R3"))
			if(Main.S.runningProcess.C!=0)
				Main.S.runningProcess.counter=Integer.valueOf(roz);
		if(rez.equals("R4"))
			if(Main.S.runningProcess.D!=0)
				Main.S.runningProcess.counter=Integer.valueOf(roz);
	} break;
	case "IC":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		if(roz.equals("R1"))
			Main.S.runningProcess.A++;
		if(roz.equals("R2"))
			Main.S.runningProcess.B++;
		if(roz.equals("R3"))
			Main.S.runningProcess.C++;
		if(roz.equals("R4"))
			Main.S.runningProcess.D++;
	} break;
	case "DC":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		if(roz.equals("R1"))
			Main.S.runningProcess.A--;
		if(roz.equals("R2"))
			Main.S.runningProcess.B--;
		if(roz.equals("R3"))
			Main.S.runningProcess.C--;
		if(roz.equals("R4"))
			Main.S.runningProcess.D--;
	} break;
	case "SR":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		if(roz.equals("R1"))
			System.out.println("Wartosc rejestru " + Main.S.runningProcess.A);
		if(roz.equals("R2"))
			System.out.println("Wartosc rejestru " + Main.S.runningProcess.B);
		if(roz.equals("R3"))
			System.out.println("Wartosc rejestru " + Main.S.runningProcess.C);
		if(roz.equals("R4"))
			System.out.println("Wartosc rejestru " + Main.S.runningProcess.D);
	} break;
	case "CF":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		f.createFile(roz);
	} break;
	case "DF":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		f.deleteFile(roz);
	} break;
	case "RF":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		f.readFile(roz);
	} break;
	case "WF":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		String con=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=con.length()+1;
		if(con.equals("R1"))
			con=Integer.toString(Main.S.runningProcess.A);
		if(con.equals("R2"))
			con=Integer.toString(Main.S.runningProcess.B);
		if(con.equals("R3"))
			con=Integer.toString(Main.S.runningProcess.C);
		if(con.equals("R4"))
			con=Integer.toString(Main.S.runningProcess.D);
		f.writeFile(roz, con);
	} break;
	case "CP":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		Main.S.runningProcess.fork(roz);
		String naz=roz;
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		Scanner in=null;
		try {
			in = new Scanner(new FileReader(roz)).useDelimiter("\\n");
		} catch (FileNotFoundException e) {
			System.out.println("Nie znalesiono pliku o podanej nazwie");
		}
		StringBuilder sb = new StringBuilder();
		while(in.hasNext()) {
		    sb.append(in.next());
		}
		in.close();
		String out= sb.toString();
		int x = Main.T.find_name(naz);
		Main.T.find(x).exec(out, roz+"txt", out.length(),Main.S.runningProcess.pri);
	} break;
	case "DP":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		Main.S.runningProcess.kill(Main.T.find_name(roz));
	} break;
	case "WC":
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		if(roz.equals("R1"))
			Main.S.runningProcess.zapisdoprocesu(Integer.toString(Main.S.runningProcess.A));
		if(roz.equals("R2"))
			Main.S.runningProcess.zapisdoprocesu(Integer.toString(Main.S.runningProcess.B));
		if(roz.equals("R3"))
			Main.S.runningProcess.zapisdoprocesu(Integer.toString(Main.S.runningProcess.C));
		if(roz.equals("R4"))
			Main.S.runningProcess.zapisdoprocesu(Integer.toString(Main.S.runningProcess.D));
		if(!roz.equals("R1") && !roz.equals("R2") && !roz.equals("R3") && !roz.equals("R4"))
		{
			Main.S.runningProcess.zapisdoprocesu(roz);
		}
	} break;
	case "CC":
	{
		if(Main.S.runningProcess.next.equals(null))
			Main.S.runningProcess.fork(roz);
		potoki.pipe(Main.S.runningProcess);
	} break;
	case "SC":
	{
		int a=potoki.write(Main.S.runningProcess);
		if(a==0)
		System.out.println("Nie komunikatu do wyslania");
	} break;
	case "RC":
	{
		int a=potoki.read(Main.S.runningProcess);
		if(a==-1)
			Main.S.runningProcess.counter-=3;
		if(a==0)
			System.out.println("Potok pusty");
	} break;
	case "MS" :
	{
		roz="";
		roz=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=roz.length()+1;
		String rez=m.readUntilSpace(Main.S.runningProcess.counter);
		Main.S.runningProcess.counter+=rez.length()+1;
		if(rez.equals("R1"))
			m.writeMemory(Integer.valueOf(roz), Integer.toString(Main.S.runningProcess.A).charAt(0));
		if(rez.equals("R2"))
			m.writeMemory(Integer.valueOf(roz), Integer.toString(Main.S.runningProcess.B).charAt(0));
		if(rez.equals("R3"))
			m.writeMemory(Integer.valueOf(roz), Integer.toString(Main.S.runningProcess.C).charAt(0));
		if(rez.equals("R4"))
			m.writeMemory(Integer.valueOf(roz), Integer.toString(Main.S.runningProcess.D).charAt(0));
		if(rez.equals("MSG"))
		{
			String msg = new String();
			msg=Main.S.runningProcess.IO.toString();
			for(int i=0;i<msg.length();i++)
			m.writeMemory(Integer.valueOf(roz)+i, msg.charAt(i));
		}
		else
		for(int i=0;i<rez.length();i++)
		m.writeMemory(Integer.valueOf(roz)+i, rez.charAt(i));
	} break;
	case "PC":
	{
		String a=Main.S.runningProcess.IO.toString();
		if(!a.equals(""))
			System.out.println("Komunikat: " + a);
		else
			System.out.println("Brak komunikatu");
	}break;
	case "EX":
	{
		Main.S.runningProcess.kill(Main.S.runningProcess.PID);
		Main.S.runningProcess=Main.T.INIT;
		break;
	}
	default: break;
	}
	if(Main.S.runningProcess!=null || Main.S.runningProcess!=Main.T.INIT)
	Main.S.runningProcess.cpu+=60;
}
}
