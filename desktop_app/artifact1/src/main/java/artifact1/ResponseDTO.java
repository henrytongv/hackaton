package artifact1;

public class ResponseDTO {
    public ResponseDTO() {
		super();
	}

	private String hashscanURL;
	private String accountId;
    private String evmAddress;
    private String privateKey;
    private String publicKey;

    public ResponseDTO(String accountId, String evmAddress, String hashscanURL, String privateKey, String publicKey) {
        this.accountId = accountId;
        this.evmAddress = evmAddress;
        this.hashscanURL = hashscanURL;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
    
    public String getHashscanURL() {
		return hashscanURL;
	}

	public void setHashscanURL(String hashscanURL) {
		this.hashscanURL = hashscanURL;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getEvmAddress() {
		return evmAddress;
	}

	public void setEvmAddress(String evmAddress) {
		this.evmAddress = evmAddress;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}   
}
