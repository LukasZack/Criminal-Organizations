package police;

import criminals.Member;
import criminals.Organization;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import interfaces.List;
import interfaces.MemberLambda;
import lists.SinglyLinkedList;

public class PoliceDepartment {
	
	private String captain;
	private  List<Organization> organizations; 
	private int digiroot;
	int arrestcount;
	String reportName;
	
	/* Here we define our class named PoliceDepartment in this constructor. --i
	*
	* @param captain the name of the captain in charge of the police department
	*
	*/
	
	public PoliceDepartment(String captain) {
		this.captain = captain;
		this.organizations = new SinglyLinkedList(); 
	}
	
	/*
	*
	* Returns a list of criminal organizations.
	* 
	* @return a list of criminal organizations
	*
	*/
	
	public List<Organization> getCriminalOrganizations() {
		return organizations;
		
	}

	/* 
	* Here the method creates a "File" object that represents the 
	* "CriminalOrganizations" directory within the "caseFolder".
	*
	* Then, it uses the "listFiles()" method to obtain a list of "File"
	* objects that represent the files in that directory.
	*
	* Next, it sets the value of "reportName" to the name of the "caseFolder".
    * 
	* Finally, the method iterates over the sorted list of files, checks if each file is a regular file 
	* using the "isFile()" method, and if it is, creates an "Organization" object by 
	* passing the path to the file to its constructor. This "Organization" object is then added
	* to a list called "organizations". 
	* 
	* @param caseFolder the path of the folder containing the criminal organizations' files
	*/
	
	public void setUpOrganizations(String caseFolder) {	
		
		File folder = new File(caseFolder+"/CriminalOrganizations");
		File[] files = folder.listFiles();
		reportName = new File(caseFolder).getName();
		Arrays.sort(files);
		for (File file : files) {
		    if (file.isFile()) {
		        String fileName = file.getName();
		        try {
					Organization OrganizationCriminal = new Organization(caseFolder+"/CriminalOrganizations/"+fileName);
					this.organizations.add(OrganizationCriminal);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
		    }
		}
		

	}
	
	/*
	* Here this method uses the "BufferedReader" object to read the file containing the message. 
	*
	* Then, the method reads the first line of the message file and extracts a key from it 
	* by calling the "getDigiroot" method which is then stored in "digiroot". 
	*
	* Next, the method uses the extracted key to find the key of the leader 
	* of the criminal organization associated with the key, and assigns it to "key".
	* 
	* Then for each line, the method splits it into individual members using a space as a delimiter
	* and checks if the "key" is within the range of valid indices for the "members" array.
	* If it is not, the method appends a space character to "leader".
	* If it is, the method appends the first character of the "key" -th member's name to "leader".
	* 
	* Finally, the method returns the deciphered message stored in leader.
	* 
	* @param caseFolder the path of the folder containing the message to decipher
	* 
	*/
	
	public String decipherMessage(String caseFolder) {

		BufferedReader reader;
		String[] members;
		String leader = "";

		try {
			
			reader = new BufferedReader(new FileReader(caseFolder));
			String line = reader.readLine();
			int key = getDigiroot(line.replace("#", ""));
			this.digiroot = key;
			
			key = this.getCriminalOrganizations().get(key-1).getLeaderKey();
			
			line = reader.readLine();
			line = reader.readLine();
			while (!line.equals("--")) {
				
				
				members = line.split(" ");
				
				if(key-1 >= members.length) {
					leader = leader+" ";
				}
				
				else {
					leader = leader+members[key-1].substring(0,1); 					
				}
				
				line = reader.readLine();

			}

		} catch (IOException e) {
			e.printStackTrace();
			
		}

		return leader;
		
	}
	
	/*
	 * Here the method first converts the string numbers to an integer and assigns it to "num".
	 * Finally, the method returns the calculated digital root.
	 * 
	 * @param numbers the number whose digital root is to be calculated
	 * @return the digital root of the given number
	 * 
	 */

	public int getDigiroot(String numbers) {
		
		
		    int num = Integer.parseInt(numbers);
		 
		    while (num >= 10) {
		        int sum = 0;
		        while (num > 0) {
		            sum = sum + num % 10;
		            num = num / 10;
		        }
		        num = sum;
		    }
		    
		    return num;
			
		}

	
	/* Here the "arrest()" method takes in the name of the leader of the criminal organization to be arrested.
	* It then uses a lambda function to find the leader within the
	* organization and sets the arrested status of the leader to true.
	*
	* Then the method also calls "arrestMembersRec()" to recursively arrest all the underlings of the leader.
	* 
	* Here the "arrestMembersRec()" method takes in a "Member" object and recursively arrests all the underlings of the member.
	* Finally the number of arrests made is kept track of in the "arrestcount" variable.
	* 
	* @param leader the nickname of the leader to be arrested
	* @param m the member whose underlings are to be arrested
	* 
	*/
	
	public void arrest(String leader) {
		
		// HERE WE ARE ARRESTING THE LEADER // 
		
		List<Member> criminals = new SinglyLinkedList();
		MemberLambda findLeader = (leaderx) -> leaderx.getNickname().toLowerCase().equals(leader.toLowerCase()); {
		Organization organizationCriminal = this.getCriminalOrganizations().get(digiroot-1);
		criminals = organizationCriminal.organizationTraversal(findLeader);


		if (!criminals.get(0).isArrested()) {
			criminals.get(0).setArrested(true);
			arrestcount++;
		}
		arrestMembersRec(criminals.get(0));
		
		} 
		
	 } 
	
		// HERE WE ARE ARRESTING THE UNDERLINGS // 
	
		void arrestMembersRec(Member m) {
			
			if (m.getUnderlings().isEmpty()) {
				return;
			}
			
			int size = m.getUnderlings().get(0).getUnderlings().size();;
			Member biggestMember = m.getUnderlings().get(0);
			
				for(int i = 0; i < m.getUnderlings().size(); i++) {
					
					if (!m.getUnderlings().get(i).isArrested()) {
						m.getUnderlings().get(i).setArrested(true);
						arrestcount++;
					}
					
					if (size < m.getUnderlings().get(i).getUnderlings().size()) {
						size = m.getUnderlings().get(i).getUnderlings().size();
						biggestMember = m.getUnderlings().get(i);
					}
					size = m.getUnderlings().get(i).getUnderlings().size();
				}
			arrestMembersRec(biggestMember);
			
		}
	
	
	/* Here this method first creates a new "File" object representing the report
	* file in the specified directory, and a "FileWriter" object to write to the file.
	* 
	* After writing all the necessary details to the file, the method resets the value of "arrestcount" to 0.
	* 
	* Finally, the method flushes and closes the "FileWriter" object
	* to ensure that all data is written to the file and that the file is closed properly.
	* 
	* @param filePath the file path where the report will be saved
	* 
	* */
		
	public void policeReport(String filePath) {
		
		try {
			File file = new File (filePath + "/" + reportName + "Report.txt");
			FileWriter writer = new FileWriter(file);
			writer.write("CASE REPORT " + "\n\n");
			writer.write("In charge of Operation: " + captain + "\n\n");
			writer.write("Total arrests made: " + arrestcount + "\n\n");
			writer.write("Current Status of Criminal Organizations:\n\n");
			
			for (int i = 0; i < this.getCriminalOrganizations().size(); i++) {
				if (organizations.get(i).getBoss().isArrested()) {
					writer.write("DISSOLVED \n");
				}
				writer.write(organizations.get(i).toString());
				writer.write("--- \n");
			}
			
			writer.write("END OF REPORT");
			
			arrestcount = 0;
			
			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
