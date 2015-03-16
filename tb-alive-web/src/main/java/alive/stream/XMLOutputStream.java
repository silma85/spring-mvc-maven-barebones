/**
 * 
 */
package alive.stream;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Wrapper for XML byte array output stream
 */
public class XMLOutputStream extends ByteArrayOutputStream {

	private DataOutputStream outchannel;

	public XMLOutputStream(OutputStream outchannel) {
		super();
		this.outchannel = new DataOutputStream(outchannel);
	}

	public void send() throws IOException {
		byte[] data = toByteArray();
		outchannel.writeInt(data.length);
		outchannel.write(data);
		reset();
	}
}
