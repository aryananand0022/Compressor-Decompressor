import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.zip.*;

public class CompressionApp extends JFrame {
    private JButton compressButton;
    private JButton decompressButton;
    private JTextArea logTextArea;

    public CompressionApp() {
        setTitle("Compression App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        compressButton = new JButton("Compress");
        decompressButton = new JButton("Decompress");
        logTextArea = new JTextArea();

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(compressButton);
        buttonPanel.add(decompressButton);

        JScrollPane logScrollPane = new JScrollPane(logTextArea);

        add(buttonPanel, BorderLayout.NORTH);
        add(logScrollPane, BorderLayout.CENTER);

        compressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(CompressionApp.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    compressFile(selectedFile);
                }
            }
        });

        decompressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(CompressionApp.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    decompressFile(selectedFile);
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void compressFile(File file) {
        try {
            String compressedFileName = file.getName() + ".zip";
            FileOutputStream fos = new FileOutputStream(compressedFileName);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            FileInputStream fis = new FileInputStream(file);

            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }

            zipOut.close();
            fis.close();
            fos.close();

            logTextArea.append("File compressed: " + compressedFileName + "\n");
        } catch (IOException ex) {
            logTextArea.append("Error compressing file: " + ex.getMessage() + "\n");
        }
    }

    private void decompressFile(File file) {
        try {
            String outputFileName = file.getName().replace(".zip", "");
            FileOutputStream fos = new FileOutputStream(outputFileName);
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(file));

            ZipEntry zipEntry = zipIn.getNextEntry();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = zipIn.read(bytes)) >= 0) {
                fos.write(bytes, 0, length);
            }

            zipIn.close();
            fos.close();

            logTextArea.append("File decompressed: " + outputFileName + "\n");
        } catch (IOException ex) {
            logTextArea.append("Error decompressing file: " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CompressionApp().setVisible(true);
            }
        });
    }
}
