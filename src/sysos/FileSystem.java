package sysos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class File_ {
	public String name;
	public int blockIndex;
	String openedBy;
	
	public File_(String name_, int blockIndex_){
		name = name_;
		blockIndex = blockIndex_;
		openedBy = "";
	}
}


class Catalog {
	ArrayList<File_> catalog;
	
	public Catalog(){
		catalog = new ArrayList<>();
	}
}


public class FileSystem {
	private final int discSize = 1024, bitVectorSize = 32, nrOfBlocks = 32; // bo 32*32 = 1024
	final char emptySign = '^';
	char[] disc;
	boolean[] bitVector;
	Catalog root;

	public FileSystem() {
		disc = new char[discSize];
		for (int i = 0; i < discSize; i++) {
			disc[i] = emptySign;
		}
		bitVector = new boolean[bitVectorSize];
		root = new Catalog();
	}

	public void createFile(String name) {
		for (File_ f : root.catalog) {
			if (f.name.equals(name)) {
				System.out.println("Plik o podanej nazwie juz istnieje!");
				return;
			}
		}
		int tempIndex = assignIndex();
		if (tempIndex != -1) {
			File_ f = new File_(name, tempIndex);
			root.catalog.add(f);
		} else {
			System.out.println("Brak wolnego bloku!");
			return;
		}
	}
	
	public void openFile(String name) {
		for(File_ f : root.catalog) {
			if(f.name.equals(name)) {
				if(f.openedBy.equals("")) {
					f.openedBy = Main.S.runningProcess.name;
				}
				else {
					System.out.println("Plik jest juz otwarty");
				}
				return;
			}
		}
		System.out.println("Plik o podanej nazwie nie istnieje");
	}
	
	public void closeFile(String name) {
		for(File_ f : root.catalog) {
			if(f.name.equals(name)) {
				if(f.openedBy.equals(Main.S.runningProcess.name)) {
					f.openedBy = "";
				}
				else if (f.openedBy.equals("")) {
					System.out.println("Plik nie jest otwarty");
				}
				else {
					System.out.println("Plik jest uzywany przez inny proces");
				}
				return;
			}
		}
		System.out.println("Plik o podanej nazwie nie istnieje");	
	}

	public void writeFile(String name, String content) {
		for (File_ f : root.catalog) {
			if (f.name.equals(name)) {
				if(f.openedBy.equals("")) {
					System.out.println("Plik nie jest otwarty");
					return;
				}
				if(!f.openedBy.equals(Main.S.runningProcess.name)) {
					System.out.println("Plik jest otwarty przez inny proces");
					return;
				}
				int lastUsedBlock = -1;
				int index = 0;
				for(int i=f.blockIndex; i < f.blockIndex + nrOfBlocks; i++) {
					if(disc[i] != emptySign) {
						lastUsedBlock = (int) disc[i];
					}
				}
				if(lastUsedBlock != -1) {
					for(int i = lastUsedBlock; i<lastUsedBlock + nrOfBlocks; i++) {
						if(disc[i] ==  emptySign) {
							if(index < content.length()) {
								disc[i] = content.charAt(index++);
							}
						}
					}	
				}
				int neededBlocks;
				if(content.length() <= index) {
					neededBlocks = 0;
				}
				else {
					neededBlocks = (content.length() - index) / nrOfBlocks + 1;
				}
				//System.out.println("need: " + neededBlocks);
				for (int i = 0; i < neededBlocks; i++) {
					int tempIndex = assignIndex();
					if (tempIndex != -1) {
						for (int j = f.blockIndex; j < f.blockIndex + nrOfBlocks; j++) {
							if (disc[j] == emptySign) {
								disc[j] = (char) tempIndex;
								for (int k = tempIndex; k < tempIndex + nrOfBlocks; k++) {
									if (index < content.length()) {
										disc[k] = content.charAt(index++);
									}
								}
								break;
							}
						}
					} else {
						System.out.println("Brak wolnego bloku!");
						return;
					}
				}
				return;
			}
		}
		System.out.println("Plik o podanej nazwie nie istnieje!");
	}

	public void readFile(String name) {
		for (File_ f : root.catalog) {
			if (f.name.equals(name)) {
				if(f.openedBy.equals("")) {
					System.out.println("Plik nie jest otwarty");
					return;
				}
				if(!f.openedBy.equals(Main.S.runningProcess.name)) {
					System.out.println("Plik jest otwarty przez inny proces");
					return;
				}
				String data = "";
				for (int i = f.blockIndex; i < f.blockIndex + nrOfBlocks; i++) {
					if (disc[i] != emptySign) {
						int currentBlockNr = (int) disc[i];
						for (int j = currentBlockNr; j < currentBlockNr + nrOfBlocks; j++) {
							if (disc[j] != emptySign) {
								data += disc[j];
							}
						}
					}
				}
				System.out.println(data);
				return;
			}
		}
		System.out.println("Plik o podanej nazwie nie istnieje");
	}

	public void deleteFile(String name) {
		for (File_ f : root.catalog) {
			if (f.name.equals(name)) {
				if(f.openedBy.equals("")) {
					System.out.println("Plik nie jest otwarty");
					return;
				}
				if(!f.openedBy.equals(Main.S.runningProcess.name)) {
					System.out.println("Plik jest otwarty przez inny proces");
					return;
				}
				for (int i = f.blockIndex; i < f.blockIndex + nrOfBlocks; i++) {
					if (disc[i] != emptySign) {
						int currentBlockNr = (int) disc[i];
						for (int j = currentBlockNr; j < currentBlockNr + 32; j++) {
							disc[j] = emptySign;
						}
						bitVector[currentBlockNr / nrOfBlocks] = false;
						disc[i] = emptySign;
					}
				}
				bitVector[f.blockIndex / nrOfBlocks] = false;
				root.catalog.remove(f);
				return;
			}
		}
		System.out.println("Plik o podanej nazwie nie istnieje");
	}

	public void listAllFiles() {
		for(File_ f : root.catalog) {
			System.out.println(f.name + " " + f.blockIndex);
		}
	}
	
	public void printFileData(String name) {
		for (File_ f : root.catalog) {
			if (f.name.equals(name)) {
				if(f.openedBy.equals("")) {
					System.out.println("Plik nie jest otwarty");
					return;
				}
				if(!f.openedBy.equals(Main.S.runningProcess.name)) {
					System.out.println("Plik jest otwarty przez inny proces");
					return;
				}
				System.out.println(f.name + " " + f.blockIndex);
				return;
			}
		}
		System.out.println("Plik o podanej nazwie nie istnieje");
	}

	private int assignIndex() {
		for (int i = 0; i < bitVectorSize; i++) {
			if (!bitVector[i]) {
				bitVector[i] = true;
				return i * nrOfBlocks;
			}
		}
		return -1;
	}

	public void printDisc() {
		for (int i = 0; i < disc.length; i++) {
			if ((i + 1) % 64 == 0) {
				System.out.println(disc[i]);
			} else {
				System.out.print(disc[i]);
			}
		}
	}
	
	public void printSector(int blockI) {
		int blockRange = blockI*nrOfBlocks;
		for (int i = blockRange; i < blockRange+32; i++) {
			if ((i + 1) % 8 == 0) {
				System.out.println((int)disc[i]);
			} else {
				System.out.print((int)disc[i]+" ");
			}
		}
	}
	
	public void executePingFromFile(String name ) {
		for (File_ f : root.catalog) {
			if (f.name.equals(name)) {
				String data = "";
				for (int i = f.blockIndex; i < f.blockIndex + nrOfBlocks; i++) {
					if (disc[i] != emptySign) {
						int currentBlockNr = (int) disc[i];
						for (int j = currentBlockNr; j < currentBlockNr + nrOfBlocks; j++) {
							if (disc[j] != emptySign) {
								data += disc[j];
							}
						}
					}
				}
				String ip = data;
		        String pingResult = "";

		        String pingCmd = "ping " + ip;
		        try {
		            Runtime r = Runtime.getRuntime();
		            Process p = r.exec(pingCmd);

		            BufferedReader in = new BufferedReader(new
		            InputStreamReader(p.getInputStream()));
		            String inputLine;
		            while ((inputLine = in.readLine()) != null) {
		                System.out.println(inputLine);
		                pingResult += inputLine;
		            }
		            in.close();

		        } catch (IOException e) {
		            System.out.println(e);
		        }
		        System.out.println(pingResult);
		        return;
			}
		}
		System.out.println("Plik o podanej nazwie nie istnieje");
	}
}
