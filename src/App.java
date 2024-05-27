import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class App extends JFrame {
    private Vector<Contact> contacts;
    private int nextID = 0;
    private JTable table;
    private final String FILENAME = ".informazioni.txt";

    App() {
        readContactsFromFile();
        this.setLayout(new BorderLayout());

        String[] columnNames = { "Nome", "Cognome", "Numero di telefono" };
        TableModel dtb = new DefaultTableModel(parsePeople(contacts), columnNames);
        table = new JTable(dtb) {
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane sp = new JScrollPane(table);
        this.add(sp);

        JToolBar bottomToolBar = new JToolBar();
        JPanel bottomPanel = new JPanel();
        JButton modifyButton = new JButton("Modifica");
        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int currentRow = table.getSelectedRow();
                if (currentRow == -1) {
                    openErrorWindow("Prima è necessario selezionare una persona dall'elenco per modificarla!");
                    return;
                }
                openEditWindow(currentRow);
                readContactsFromFile();
            }
        });
        JButton newButton = new JButton("Nuovo");
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                table.clearSelection();
                openEditWindow(-1);
                readContactsFromFile();
            }
        });
        JButton deleteButton = new JButton("Elimina");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int currentRow = table.getSelectedRow();
                if (currentRow == -1) {
                    openErrorWindow("Prima è necessario selezionare una persona dall'elenco per eliminarla!");
                    return;
                }
                openDeleteWindow(contacts.get(currentRow));
                readContactsFromFile();
            }
        });

        bottomPanel.add(modifyButton);
        bottomPanel.add(newButton);
        bottomPanel.add(deleteButton);

        bottomToolBar.add(bottomPanel);

        this.add(bottomToolBar, BorderLayout.SOUTH);

        this.setSize(getPreferredSize());
        this.setTitle("Applicazione Rubrica");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }

    private void openDeleteWindow(Contact c) {
        int reply = JOptionPane.showConfirmDialog(this, "Eliminare la persona " + c.name + " " + c.surname);
        if (reply == JOptionPane.NO_OPTION) {
            return;
        }
        int row = table.getSelectedRow();
        // Delete element to table
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(row);
        // Delete element to this.contacts
        this.contacts.remove(row);
        // Delete element to informazioni.txt
        updateFile();
        // Clear selected row
        table.clearSelection();
    }

    private void openErrorWindow(String str) {
        JOptionPane.showMessageDialog(this, str);
    }

    private void openEditWindow(int row) {
        Contact currentcContact;
        if (row == -1) {
            currentcContact = new Contact("", "", "", "", -1, nextID);
            nextID++;
        } else {
            Contact c = contacts.get(row);
            currentcContact = new Contact(c.name, c.surname, c.address, c.phoneNumber, c.age, c.id);
        }
        JDialog dialog = new JDialog(this, "Scheda contatto");
        dialog.setVisible(rootPaneCheckingEnabled);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        JLabel labelName = new JLabel("Nome: ");
        JLabel labelSurname = new JLabel("Cognome: ");
        JLabel labelAddress = new JLabel("Indirizzo: ");
        JLabel labelPhoneNumber = new JLabel("Numero di telefono: ");
        JLabel labelAge = new JLabel("Età: ");
        JTextField textFieldName = new JTextField(currentcContact.name);
        JTextField textFieldSurname = new JTextField(currentcContact.surname);
        JTextField textFieldAddress = new JTextField(currentcContact.address);
        JTextField textFieldPhoneNumber = new JTextField(currentcContact.phoneNumber);
        JTextField textFieldAge;
        if (currentcContact.age == -1) {
            textFieldAge = new JTextField("");
        } else {
            textFieldAge = new JTextField(String.valueOf(currentcContact.age));
        }

        int x, y;
        int gap = 3;

        // Name
        x = 0;
        y = 0;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(labelName, gbc);

        x = 1;
        y = 0;
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(textFieldName, gbc);

        // Surname
        x = 0;
        y = 1;
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(labelSurname, gbc);
        
        x = 1;
        y = 1;
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(textFieldSurname, gbc);

        // Address
        x = 0;
        y = 2;
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(labelAddress, gbc);

        x = 1;
        y = 2;
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(textFieldAddress, gbc);
        
        // Phone Number
        x = 0;
        y = 3;
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.ipadx = 100;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(labelPhoneNumber, gbc);

        x = 1;
        y = 3;
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.ipadx = 100;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(textFieldPhoneNumber, gbc);

        // Age
        x = 0;
        y = 4;
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(labelAge, gbc);

        x = 1;
        y = 4;
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(gap, gap + 2 * gap * x, gap, gap);
        mainPanel.add(textFieldAge, gbc);

        dialog.add(mainPanel);

        JToolBar bottomToolBar = new JToolBar();
        JPanel bottomPanel = new JPanel();
        JButton cancelButton = new JButton("Annulla");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        JButton saveButton = new JButton("Salva");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Remove selected element
                table.clearSelection();
                // Check Name
                if (textFieldName.getText().contains(";")) {
                    openErrorWindow("Il nome non deve contenere il carattere ;");
                    return;
                }
                // Check Surname
                if (textFieldSurname.getText().contains(";")) {
                    openErrorWindow("Il cognome non deve contenere il carattere ;");
                    return;
                }
                // Check Address
                if (textFieldAddress.getText().contains(";")) {
                    openErrorWindow("L'indirizzo non deve contenere il carattere ;");
                    return;
                }
                // Check Phone Number
                if (!textFieldPhoneNumber.getText().matches("^(\\+)?[\\d ]+$")) {
                    openErrorWindow("Il numero di telefono non è formattato correttamente!");
                    return;
                }
                // Check Age
                if (!textFieldAge.getText().matches("^\\d+$")) {
                    openErrorWindow("L'età deve essere un numero intero positivo!");
                    return;
                }
                // Close dialog only if no exception
                dialog.dispose();
                // Generate new contact
                Contact newContact = new Contact(textFieldName.getText(), textFieldSurname.getText(),
                        textFieldAddress.getText(), textFieldPhoneNumber.getText(),
                        Integer.parseInt(textFieldAge.getText()), -1);
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                if (row == -1) {
                    // Insert correct id in new contact
                    newContact.id = nextID;
                    nextID++;
                    // Add element to table
                    String[] s = { newContact.name, newContact.surname, newContact.phoneNumber };
                    model.addRow(s);
                    // Add element to this.contacts
                    contacts.add(newContact);
                    // Add element to informazioni.txt
                    updateFile();
                } else {
                    // Insert correct id in new contact
                    newContact.id = row;
                    // Add element to table
                    model.setValueAt(newContact.name, row, 0);
                    model.setValueAt(newContact.surname, row, 1);
                    model.setValueAt(newContact.phoneNumber, row, 2);
                    // Edit element to this.contacts
                    contacts.set(row, newContact);
                    // Add element to informazioni.txt
                    updateFile();
                }
            }
        });

        bottomPanel.add(cancelButton);
        bottomPanel.add(saveButton);
        bottomToolBar.add(bottomPanel);
        dialog.add(bottomToolBar, BorderLayout.SOUTH);
        dialog.pack();
    }

    private void readContactsFromFile() {
        Vector<Contact> v = new Vector<Contact>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line = reader.readLine();
            while (line != null) {
                String[] splitLine = line.split(";");
                if (splitLine.length != 5) {
                    throw new Exception("informazioni.txt with errors");
                }
                Contact p = new Contact(splitLine[0], splitLine[1], splitLine[2], splitLine[3],
                        Integer.parseInt(splitLine[4]), nextID);
                nextID++;
                v.add(p);
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("[LOG] No input file, starting with empty book...");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.contacts = v;
    }

    private static String[][] parsePeople(Vector<Contact> peopleVector) {
        ArrayList<String[]> data = new ArrayList<String[]>();
        for (Contact p : peopleVector) {
            String[] row = { p.name, p.surname, p.phoneNumber };
            data.add(row);
        }
        String[][] newData = data.toArray(new String[0][0]);
        return newData;
    }

    private void updateFile() {
        // Write all contacts information to the file, from scratch
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, false))) {
            for (Contact c : contacts) {
                writer.write(c.toCSVString());
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
