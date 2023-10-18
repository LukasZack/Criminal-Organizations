package police;

import java.io.File;

/**
 * Class whose main method will follow the steps needed for arresting 
 * members of criminal organizations. The step should be followed as established in the 
 * project's document.
 * @author Gretchen
 *
 */



public class ArrestOperation {

	public static void main(String[] args) {

		PoliceDepartment policeDepartment = new PoliceDepartment("Captain Morgan");
		policeDepartment.setUpOrganizations("inputFiles/case1");

		File directory = new File("inputFiles/case1/Flyers");
		File[] files = directory.listFiles();
		int i = 0;

		while (i < files.length) {
			File file = files[i];
			if (file != null && file.isFile()) {
				String leader = policeDepartment.decipherMessage("inputFiles/case1/Flyers/" + file.getName());
				policeDepartment.arrest(leader);
			}

			i++;
		}

		policeDepartment.policeReport("results");

		policeDepartment = new PoliceDepartment("Captain Morgan");
		policeDepartment.setUpOrganizations("inputFiles/case2");

		directory = new File("inputFiles/case2/Flyers");
		files = directory.listFiles();
		i = 0;

		while (i < files.length) {
			File file = files[i];
			if (file != null && file.isFile()) {
				String leader = policeDepartment.decipherMessage("inputFiles/case2/Flyers/" + file.getName());
				policeDepartment.arrest(leader);
			}
			i++;
		}
		policeDepartment.policeReport("results");


	}
}
