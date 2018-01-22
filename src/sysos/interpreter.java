package sysos;

import sysos.process_manager.process;

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
	process pr=Main.T.new process("");
	Main.S.check(pr, Main.T);
	String roz;
	roz=m.readUntilSpace(pr.counter);
	System.out.println(pr.counter);
	pr.counter+=roz.length()+1;
	switch(roz)
	{
	case "AD":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz.equals("R1")) idrej=1;
		if(roz.equals("R2")) idrej=2;
		if(roz.equals("R3")) idrej=3;
		if(roz.equals("R4")) idrej=4;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 3);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readUntilSpace(p));
		}
		if(roz.equals("R1"))
			liczba=pr.A;
		if(roz.equals("R2"))
			liczba=pr.B;
		if(roz.equals("R3"))
			liczba=pr.C;
		if(roz.equals("R4"))
			liczba=pr.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			pr.A+=liczba;
		if(idrej==2)
			pr.B+=liczba;
		if(idrej==3)
			pr.C+=liczba;
		if(idrej==4)
			pr.D+=liczba;
	} break;
	case "SU":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz.equals("R1")) idrej=1;
		if(roz.equals("R2")) idrej=2;
		if(roz.equals("R3")) idrej=3;
		if(roz.equals("R4")) idrej=4;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 3);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readUntilSpace(p));
		}
		if(roz.equals("R1"))
			liczba=pr.A;
		if(roz.equals("R2"))
			liczba=pr.B;
		if(roz.equals("R3"))
			liczba=pr.C;
		if(roz.equals("R4"))
			liczba=pr.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			pr.A-=liczba;
		if(idrej==2)
			pr.B-=liczba;
		if(idrej==3)
			pr.C-=liczba;
		if(idrej==4)
			pr.D-=liczba;
	} break;
	case "MU":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz.equals("R1")) idrej=1;
		if(roz.equals("R2")) idrej=2;
		if(roz.equals("R3")) idrej=3;
		if(roz.equals("R4")) idrej=4;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 3);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readUntilSpace(p));
		}
		if(roz.equals("R1"))
			liczba=pr.A;
		if(roz.equals("R2"))
			liczba=pr.B;
		if(roz.equals("R3"))
			liczba=pr.C;
		if(roz.equals("R4"))
			liczba=pr.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			pr.A*=liczba;
		if(idrej==2)
			pr.B*=liczba;
		if(idrej==3)
			pr.C*=liczba;
		if(idrej==4)
			pr.D*=liczba;
	} break;
	case "MO":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz.equals("R1")) idrej=1;
		if(roz.equals("R2")) idrej=2;
		if(roz.equals("R3")) idrej=3;
		if(roz.equals("R4")) idrej=4;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 3);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readUntilSpace(p));
		}
		if(roz.equals("R1"))
			liczba=pr.A;
		if(roz.equals("R2"))
			liczba=pr.B;
		if(roz.equals("R3"))
			liczba=pr.C;
		if(roz.equals("R4"))
			liczba =pr.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			pr.A=liczba;
		if(idrej==2)
			pr.B=liczba;
		if(idrej==3)
			pr.C=liczba;
		if(idrej==4)
			pr.D=liczba;
	} break;
	case "JP":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		pr.counter=Integer.valueOf(roz);
	} break;
	case "JZ":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz.equals("R1"))
			if(pr.A!=0)
				pr.counter=Integer.valueOf(roz);
		if(roz.equals("R2"))
			if(pr.B!=0)
				pr.counter=Integer.valueOf(roz);
		if(roz.equals("R3"))
			if(pr.C!=0)
			pr.counter=Integer.valueOf(roz);
		if(roz.equals("R4"))
			if(pr.D!=0)
				pr.counter=Integer.valueOf(roz);
	} break;
	case "IC":
	{
		if(roz.equals("R1"))
			pr.A++;
		if(roz.equals("R2"))
			pr.B++;
		if(roz.equals("R3"))
			pr.C++;
		if(roz.equals("R4"))
			pr.D++;
	} break;
	case "DC":
	{
		if(roz.equals("R1"))
			pr.A--;
		if(roz.equals("R2"))
			pr.B--;
		if(roz.equals("R3"))
			pr.C--;
		if(roz.equals("R4"))
			pr.D--;
	} break;
	case "SR":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz.equals("R1"))
			System.out.println("Wartosc rejestru" + pr.A);
		if(roz.equals("R2"))
			System.out.println("Wartosc rejestru" + pr.B);
		if(roz.equals("R3"))
			System.out.println("Wartosc rejestru" + pr.C);
		if(roz.equals("R4"))
			System.out.println("Wartosc rejestru" + pr.D);
	} break;
	case "CF":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		f.createFile(roz);
	} break;
	case "DF":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		f.deleteFile(roz);
	} break;
	case "RF":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		f.readFile(roz);
	} break;
	case "WF":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		String con=m.readUntilSpace(pr.counter);
		pr.counter+=con.length()+1;
		if(con.equals("R1"))
			con=Integer.toString(pr.A);
		if(con.equals("R2"))
			con=Integer.toString(pr.B);
		if(con.equals("R3"))
			con=Integer.toString(pr.C);
		if(con.equals("R4"))
			con=Integer.toString(pr.D);
		f.writeFile(roz, con);
	} break;
	case "CP":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		String ro=m.readUntilSpace(pr.counter);
		pr.counter+=ro.length()+1;
		pr.fork(roz);
		process p = Main.T.new process("");
		int i;
		i=Main.T.find_name(roz);
		p=Main.T.find(i);
		p.exec("", "", Integer.valueOf(ro));
	} break;
	case "DP":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		process_manager man= new process_manager(); 
		pr.kill(man.find_name(roz));
	} break;
	case "WC":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz.equals("R1"))
			pr.zapisdoprocesu(Integer.toString(pr.A));
		if(roz.equals("R2"))
			pr.zapisdoprocesu(Integer.toString(pr.B));
		if(roz.equals("R3"))
			pr.zapisdoprocesu(Integer.toString(pr.C));
		if(roz.equals("R4"))
			pr.zapisdoprocesu(Integer.toString(pr.D));
		if(!roz.equals("R1") && roz.equals("R2") && roz.equals("R3") && roz.equals("R4"))
		{
			pr.zapisdoprocesu(roz);
		}
	} break;
	case "CC":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(pr.next.equals(null))
			pr.fork(roz);
		potoki.pipe(pr);
	} break;
	case "SC":
	{
		potoki.write(pr);
	} break;
	case "RC":
	{
		potoki.read(pr.next);
	} break;
	case "EX":
	{
		pr.kill(pr.PID);
	}
	case "MS" :
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		String rez=m.readUntilSpace(pr.counter);
		pr.counter+=rez.length()+1;
		if(rez.equals("R1"))
			m.writeMemory(Integer.valueOf(roz), Integer.toString(pr.A).charAt(0));
		if(rez.equals("R2"))
			m.writeMemory(Integer.valueOf(roz), Integer.toString(pr.B).charAt(0));
		if(rez.equals("R3"))
			m.writeMemory(Integer.valueOf(roz), Integer.toString(pr.C).charAt(0));
		if(rez.equals("R4"))
			m.writeMemory(Integer.valueOf(roz), Integer.toString(pr.D).charAt(0));
		if(rez.equals("MSG"))
		{
			String msg = new String();
			msg=pr.IO.toString();
			for(int i=0;i<msg.length();i++)
			m.writeMemory(Integer.valueOf(roz)+i, msg.charAt(i));
		}
		else
		for(int i=0;i<rez.length();i++)
		m.writeMemory(Integer.valueOf(roz)+i, rez.charAt(i));
	} break;
	}
	pr.cpu+=50;
}
}
