package artifact1;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import com.jthemedetecor.OsThemeDetector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyFirstHederaAccount extends JFrame {

	static boolean useThemeLight = false;
	
	static String accountId= "";
	static String publicKey= "";
	static String balance= "";
	static String hashcanURL= "";
	
	// Logger for handling exceptions silently
    private static final Logger LOGGER = Logger.getLogger(MyFirstHederaAccount.class.getName());

	public static void main(String[] args) {
		try {
			// Create a new Account in Hedera testnet 
			ObjectMapper mapper = new ObjectMapper();
			RequestDTO requestData = new RequestDTO("justreturndemoaccount");
			
			// Convert Java object to JSON string
            String jsonBody = mapper.writeValueAsString(requestData);
            System.out.println("Outgoing JSON Body: " + jsonBody);

            // 3. Build the HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // 4. Build the HttpRequest
            String url = "https://*****************************************.us-east1.run.app/process";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();

            // 5. Send the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 6. Print status and raw response body
            System.out.println("\nResponse Status Code: " + response.statusCode());
            String responseBody = response.body();
            System.out.println("Raw Response Body: " + responseBody);


            ResponseDTO responseObject = mapper.readValue(responseBody, ResponseDTO.class);
            
            // 8. Output the parsed Java Object
            System.out.println("\nParsed Result:");
            System.out.println("hurl      : " + responseObject.getHashscanURL());
            System.out.println("account id: " + responseObject.getAccountId());
            System.out.println("evm a     : " + responseObject.getEvmAddress());
            System.out.println("private   : " + responseObject.getPrivateKey());
            System.out.println("public    : " + responseObject.getPublicKey());
            
            accountId = responseObject.getAccountId();
            publicKey= responseObject.getPublicKey();
            hashcanURL= responseObject.getHashscanURL();
            

			// get balance
			HttpClient mirrorClient = HttpClient.newHttpClient();

			// Configure query parameters
			String getBalances_limit = "2"; // Fill in the number of items to return
			String getBalances_order = "desc"; // Fill in the result ordering
			String accountIdToQuery=accountId;

			String getBalances_url = String.format(
					"https://testnet.mirrornode.hedera.com/api/v1/balances?account.id=%s&limit=%s&order=%s", accountIdToQuery,getBalances_limit,
					getBalances_order);

			HttpRequest getBalances_request = HttpRequest.newBuilder().uri(URI.create(getBalances_url)).GET().build();

			HttpResponse<String> getBalances_response = mirrorClient.send(getBalances_request,
					HttpResponse.BodyHandlers.ofString());

			if (getBalances_response.statusCode() != 200) {
				throw new RuntimeException("HTTP error! status: " + getBalances_response.statusCode());
			}

			String jsonResult = getBalances_response.body();
			System.out.println("------------------------------ Get Balances ------------------------------ ");
			System.out.println("Response status         : " + getBalances_response.statusCode());
			System.out.println("Balances                : " + jsonResult);
			
			


	        ResponseData data = mapper.readValue(jsonResult, ResponseData.class);

	        // double firstBalance = data.balances.get(0).balance;
	        balance = String.valueOf(data.balances.get(0).balance / 100000000);
	        
	        
	        
			

			// show information in UI
			final OsThemeDetector detector = OsThemeDetector.getDetector();
			final boolean isDarkThemeUsed = detector.isDark();

			if (isDarkThemeUsed) {
				System.out.println("somos dark");
			} else {
				useThemeLight = true;
				System.out.println("luzzzz");
			}

			SwingUtilities.invokeLater(() -> {
				try {
					new MyFirstHederaAccount().createAndShowGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public MyFirstHederaAccount() {
        // Set up the main window (JFrame)
        super("Accessible Data Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Use a JPanel with GridBagLayout for flexible and accessible layout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        // Configure general constraints for labels and fields
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // field 1
        JLabel nameLabel = new JLabel("Your Account Id:");
        JTextField nameField = createOutputField(accountId);
        nameLabel.setLabelFor(nameField); // Crucial for screen reader association
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; panel.add(nameField, gbc);

        // field 2
        JLabel versionLabel = new JLabel("Balance in Tiny hbars");
        JTextField versionField = createOutputField(balance);
        versionLabel.setLabelFor(versionField);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; panel.add(versionLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; panel.add(versionField, gbc);

        // field 3
        JLabel statusLabel = new JLabel("URL to verify account in Testnet web viewer:");
        JTextField statusField = createOutputField(hashcanURL);
        statusLabel.setLabelFor(statusField);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; panel.add(statusLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; panel.add(statusField, gbc);

        // field 4 is URL to account in testnet web dashboard
        JLabel linkLabel = new JLabel("Blockchain web:");
        JLabel linkField = createClickableLink("Account in testent", hashcanURL);
        linkLabel.setLabelFor(linkField); // Crucial for screen reader association

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; panel.add(linkLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; panel.add(linkField, gbc);

        // Final setup
        add(panel);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    /**
     * Helper method to create a non-editable JTextField for displaying output data.
     * @param text The initial text content.
     * @return A styled, non-editable JTextField.
     */
    private JTextField createOutputField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false); // Make it an output field
        field.setBackground(Color.WHITE); // Ensure high contrast
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setColumns(20); // Give it a preferred size
        
        // Setting AccessibleDescription for fields might be useful, but standard
        // JLabel.setLabelFor association is the primary mechanism for screen readers.
        // field.getAccessibleContext().setAccessibleDescription("The current value of " + labelText);
        
        return field;
    }

    /**
     * Helper method to create a clickable JLabel that acts as a hyperlink.
     * @param text The display text of the link.
     * @param url The URL to open when clicked.
     * @return A JLabel styled and equipped to act as a hyperlink.
     */
    private JLabel createClickableLink(String text, String url) {
        JLabel link = new JLabel("<html><a href=''>" + text + "</a></html>");
        link.setForeground(Color.BLUE.darker()); // Traditional link color
        link.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Indicate it's clickable
        link.setToolTipText("Click to open " + url + " in your browser"); // Tooltip for visual/hover accessibility

        // Add the click listener
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if the Desktop environment is supported before trying to browse
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Could not open URL: " + url, ex);
                        // Optionally show a message to the user if the link fails
                        JOptionPane.showMessageDialog(MyFirstHederaAccount.this, 
                            "Failed to open link: " + url, "Link Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MyFirstHederaAccount.this, 
                        "Desktop browsing is not supported on this system.", "System Error", JOptionPane.WARNING_MESSAGE);
                }
            }
            
            // Optional: Change appearance on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                link.setText("<html><a href=''>" + text + "</a></html>"); // Keep it underlined
            }

            @Override
            public void mouseExited(MouseEvent e) {
                link.setText("<html><a href=''>" + text + "</a></html>");
            }
        });
        
        // Ensure the link text is visible even if the text field logic is applied
        link.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Accessibility note: Using a JLabel styled as a link, rather than a JHyperlink 
        // library (which may not exist), is the standard way. The setLabelFor on the 
        // associated JLabel is key for screen readers.
        link.getAccessibleContext().setAccessibleName(text);
        link.getAccessibleContext().setAccessibleDescription("Clickable link to account information in testnet");
        
        return link;
    }
    
	private void createAndShowGUI() throws Exception {
		if (useThemeLight)
			UIManager.setLookAndFeel(new FlatLightLaf());
		else
			UIManager.setLookAndFeel(new FlatDarkLaf());


		
		//  show information
		
		
	}

}
