package de.uniks.pfp.testing;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class FactoryId {

	/* This class manages factory ids
	 * 
	 * 13-A-M
	 * 
	 * bricknumber - port - medium/large
	 * 
	 */
	
	StringBuffer id;
	InetAddress ip = null;
	
	private static final int IDLEN = 12;
	private static final String IPMASK = "192.168.0.1"; 
	
	public FactoryId() {
		id = new StringBuffer();
		id.setLength(IDLEN);
		id.replace(0, 6, "00-0-0");
		
	}
	
	public FactoryId(int brickNumber, String port) {
		this();
		this.setBrickNumber(brickNumber);
		this.setPort(port);
	}
	
	public FactoryId(int brickNumber, String port, Character type) {
		this(brickNumber, port);
		this.setType(type);
	}
	
	// Returns 00 or old bricknumber for this id
	public String setBrickNumber(int brickNumber) {
		String currentBrick = id.substring(0, 1);
		
		id.replace(0, 2, String.valueOf(brickNumber));
		
		setIp(brickNumber);
		return currentBrick;
	}

	public void setIp(int brickNumber) {
		String brick = id.substring(0, 2);
		if (!"00".equals(brick)) {
			System.out.println(Integer.parseInt(brick));
			try {
				ip = InetAddress.getByName(IPMASK + brickNumber);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String setPort(String port) {
		String currentPort = id.substring(3, 3);
		id.replace(3, 4, port);
		return currentPort;
	}
	
	public String setType(Character type) {
		String currentType = id.substring(5, 5);
		id.replace(5, 6, String.valueOf(type));
		return currentType;
	}
	
	public String getBrickNumber() {
		return id.substring(0, 2).toString();
	}
	
	public String getIp() {
		// String result = null;
		String brick = id.substring(0, 2);	
		String result = "00".equals(brick) ? null : IPMASK + brick;		
		return result;
	}
	
	public String getPort() {
		return id.substring(3, 4).toString();
	}
	
	public String getType() {
		return id.substring(5,6).toString();
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
	
	public static void main(String[] args) {
		
		FactoryId id = new FactoryId(13, "A", 'L');
		id.setBrickNumber(15);
		// test.replace(29, 31, "13-");
		System.out.println(id);
		try {
			System.out.println(id.ip);
			System.out.println(id.ip.isReachable(100));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
