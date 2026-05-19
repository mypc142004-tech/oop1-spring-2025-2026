package officemanagementsystem.fileio;

import officemanagementsystem.entity.Employee;

import java.io.*;


public class EmployeeFileIO {

    private static final String FILE_NAME = "Employees.txt";

    private static final String TEMP_FILE = "temp.txt";

    public static void createFileIfNotExists() throws IOException {
        File file = new File(FILE_NAME);

        if (!file.exists())
            file.createNewFile(); 
    }

    public static boolean idExists(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                Employee e = Employee.fromLine(line); 

                // If parsing succeeded and the ID matches, the ID already exists
                if (e != null && e.getId().equals(id))
                    return true;
            }
        } catch (IOException ignored) {
        }
        return false;
    }

    public static int countRecords() {
        int count = 0; 
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (Employee.fromLine(line) != null)
                    count++;
            }
        } catch (IOException ignored) {

        }
        return count; 
    }

    public static void addEmployee(Employee e) throws IOException {
        
        try (PrintWriter pw = new PrintWriter(
                new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            pw.println(e.toLine());
            
        }
    }

   
    public static boolean updateEmployee(Employee e) throws IOException {
        File inputFile = new File(FILE_NAME); 
        File tempFile = new File(TEMP_FILE);
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                Employee existing = Employee.fromLine(line);
                
                if (existing != null && existing.getId().equals(e.getId())) {
                    bw.write(e.toLine()); 
                    found = true;
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }
        }

        if (found) {
        
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                throw new IOException("Could not finalize update.");
            }
        } else {
            tempFile.delete();
        }
        return found;
    }

    public static boolean deleteEmployee(String id) throws IOException {
        File inputFile = new File(FILE_NAME);
        File tempFile = new File(TEMP_FILE);
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                Employee existing = Employee.fromLine(line);

                if (existing != null && existing.getId().equals(id)) {
                    found = true;
                    continue;
                }

                bw.write(line);
                bw.newLine();
            }
        }

        if (found) {
          
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                throw new IOException("Could not finalize delete.");
            }
        } else {
           
            tempFile.delete();
        }
        return found;
    }

    public static Object[][] getAllEmployees() {
        int total = countRecords();
        Object[][] rows = new Object[total][4];
        int idx = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null && idx < total) {
                Employee s = Employee.fromLine(line);

                if (s != null) {
                    Object[] row = s.toRow();
                    rows[idx][0] = row[0];
                    rows[idx][1] = row[1];
                    rows[idx][2] = row[2];
                    rows[idx][3] = row[3];
                    idx++;
                }
            }
        } catch (IOException ignored) {
          
        }
        return rows;
    }

    public static Object[][] searchEmployees(String keyword) {
      
        String kw = keyword.toLowerCase();

        int matchCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Employee s = Employee.fromLine(line);

                if (s != null && (s.getId().toLowerCase().contains(kw)
                        || s.getName().toLowerCase().contains(kw))) {
                    matchCount++;
                }
            }
        } catch (IOException ignored) {

        }

        Object[][] results = new Object[matchCount][4];
        int idx = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null && idx < matchCount) {
                Employee s = Employee.fromLine(line);

                if (s != null && (s.getId().toLowerCase().contains(kw)
                        || s.getName().toLowerCase().contains(kw))) {
                    Object[] row = s.toRow();
                    results[idx][0] = row[0];
                    results[idx][1] = row[1];
                    results[idx][2] = row[2];
                    results[idx][3] = row[3];
                    idx++; // Advance to the next result slot
                }
            }
        } catch (IOException ignored) {
           
        }
        return results;
    }
}
