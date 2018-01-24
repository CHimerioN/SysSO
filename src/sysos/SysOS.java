/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sysos;

import java.util.Scanner;

import sysos.process_manager.process;

/**
 *
 * @author Matitam
 */
public class SysOS {
	static int OBECNY_PROCES = 1;
	static process_manager T = new process_manager();

	public static void main(String[] args) {
		// TODO code application logic here
		Scanner s = new Scanner(System.in);
		String str = s.nextLine();
		System.out.println(str);

		Memory m = new Memory();
		FileSystem F = new FileSystem();
	}

}
