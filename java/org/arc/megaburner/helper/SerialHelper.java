/**
 * Serial communication helper
 * @author mingzo@gmail.com
 * @since 2017/11/10
 */
package org.arc.megaburner.helper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.arc.megaburner.Chips;
import org.arc.megaburner.CommException;
import org.arc.megaburner.Console;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialHelper {

	public static char SIGNAL_OP_BEGIN = '&';
	public static char SIGNAL_OP_END = '%';
	
	public static String CHECK_COMMAND = "C";
	public static String READ_COMMAND  = "R";
	public static String ERASE_COMMAND = "E";
	public static String WRITE_COMMAND = "W";
	

	private int selected = 0;
	
	private static String[] availablePortNames;
	
	private Map<String, CommPortIdentifier> availablePorts = new HashMap<String, CommPortIdentifier>();;
	
	private SerialPort serialPort;
	
	private InputStream in;
	
	private OutputStream out;
	
	private boolean connected = false;
	
    public String[] getAvailableSerialPorts() {
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            if (com.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	availablePorts.put(com.getName(), com);
            }
        }
        
        String[] portNames = new String[availablePorts.size()];
        int i=0;
        for (String key : availablePorts.keySet()) {
        	portNames[i] = key;
        	i++;
        }
        availablePortNames = portNames;
        
        return portNames;
    }

	public void connect() throws CommException {
		int speed = 115200;

		try {
			if (availablePortNames.length < 1)
				throw new CommException("No serial port exists.");
			
			CommPortIdentifier portIdentifier = availablePorts.get(availablePortNames[selected]);
			if (portIdentifier.isCurrentlyOwned()) {
				throw new CommException("Port is currently in use.");
			} else {
				CommPort commPort = portIdentifier.open("MegaBurner", 2000);
				if (commPort instanceof SerialPort) {
					serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(speed, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					serialPort.enableReceiveThreshold(1);
					serialPort.enableReceiveTimeout(2000);
					in = serialPort.getInputStream();
					out = serialPort.getOutputStream();
				}
				
				Thread.sleep(1000);
				connected = true;
				
				Console.log("Serial port \""+ availablePortNames[selected] +"\" connected.", false);
			}
		} catch (PortInUseException e) {
			//e.printStackTrace();
			throw new CommException("Port " + availablePortNames[selected] + " is in use.");
		} catch (UnsupportedCommOperationException e) {
			//e.printStackTrace();
			throw new CommException(e.getMessage());
		} catch (IOException e) {
			//e.printStackTrace();
			throw new CommException(e.getMessage());
		} catch (InterruptedException e) {
			//e.printStackTrace();
			throw new CommException(e.getMessage());
		}
	}

    public void disconnect() throws IOException {
        if (connected) {
            in.close();
            out.close();
            serialPort.close();
            
            connected = false;
        }
    }
    
    public boolean isConnected() {
        return connected;
    }

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	
	public String check() throws CommException {
		long start = System.currentTimeMillis();
		
		try {
			if (!isConnected())
				connect();

			in.skip(in.available());

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			bw.write(CHECK_COMMAND);
			bw.flush();
			
			byte[] code = new byte[4];
			int size = 0;
			int word = 0;
			while (size < 4) {
				word = in.read();
				if (word == -1)
					break;
				code[size++] = (byte) word;
			}

			String chip = new String(code);
			Console.log("Chip code is \""+chip+"\".", false);
			
			return chip;
		} catch (IOException e) {
			//e.printStackTrace();
			throw new CommException("Connection reset. Read operation aborted.");
		}
	}
	
	public byte[] read() throws CommException {
		Console.log("READ operation started...", false);
		
		long start = System.currentTimeMillis();
		try {
			in.skip(in.available());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			
			Console.progressReset(Chips.getCapacity());
			
			byte[] data = new byte[Chips.getCapacity()];
			for (int block = 0; block < Chips.getReadBlockCount(); block++) {
				//read command format is R[block],[page]
				bw.write(READ_COMMAND + block + "," + Chips.getReadBlockSize());
				bw.flush();
                
				int i = 0, byte_ = 0;
				while (i < Chips.getReadBlockSize()) {
					byte_ = in.read();
					if (byte_ == -1) {
						break;
					}
					data[block*Chips.getReadBlockSize() + i] = (byte) byte_;
					
					i++;
				}
				
				Console.progressUpdate((block+1)*Chips.getReadBlockSize());
			}

			Console.log("completed!", start, System.currentTimeMillis());
			
			return data;
		} catch (IOException e) {
			Console.progressUpdate(0);
			//e.printStackTrace();
			throw new CommException("Connection reset. Read operation aborted.");
		}
	}
	
	public boolean erase() throws CommException {
		Console.log("ERASE operation started...", false);
		long start = System.currentTimeMillis();
		
		try {
			in.skip(in.available());

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			//read command format is R[block],[page]
			bw.write(ERASE_COMMAND);
			bw.flush();
			
			wait4Signal(SIGNAL_OP_END);
				
			Console.log("completed!", start,System.currentTimeMillis());
			
			return true;
		} catch (IOException e) {
			//e.printStackTrace();
			throw new CommException("Connection reset. Read operation aborted.");
		}
	}
	
	public byte[] verify(byte[] daa) throws CommException {
		Console.log("VERIFY operation started...", false);
		
		long start = System.currentTimeMillis();
		try {
			int blank = 0;
			
			in.skip(in.available());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			
			Console.progressReset(Chips.getCapacity());
			
			byte[] comingData = new byte[daa.length];
			for (int block = 0; block < Chips.getReadBlockCount(); block++) {
				int beginIndex = block*Chips.getReadBlockSize();
				int endIndex = beginIndex + Chips.getReadBlockSize();
				
				int readSize = 0;
				if (daa.length < endIndex) {
					readSize = daa.length - beginIndex;
				}
				else {
					readSize = Chips.getReadBlockSize();
				}
				
				bw.write(READ_COMMAND + block + "," + readSize);
				bw.flush();
                
				if (readSize > 0) {
					int i = 0, byte_ = 0;
					while (i < readSize) {
						byte_ = in.read();
						if (byte_ == -1) {
							break;
						}
						comingData[beginIndex + i] = (byte) byte_;
						i++;
					}
					
					for (int x=0; x<readSize; x++) {
						if (comingData[beginIndex+x] != daa[beginIndex+x]) {
							System.out.println(x+"===="+(block*Chips.getReadBlockSize()+x));
							blank++;
						}
					}
					
					if (blank > 0) {
						String from = String.format("%08X", (block*Chips.getReadBlockSize() & 0xFFFFFFFF));
						String to   = String.format("%08X", (((block+1)*Chips.getReadBlockSize()-1) & 0xFFFFFFFF));
						
						Console.log("Address "+from+" to "+to+" compared different, verify failed!", false);
						break;
					}
					else {
						
						Console.progressUpdate((block+1)*Chips.getReadBlockSize());
					}
				}
			}

			if (blank == 0)
				Console.log("completed!", start, System.currentTimeMillis());
			
			return comingData;
		} catch (IOException e) {
			Console.progressUpdate(0);
			//e.printStackTrace();
			throw new CommException("Connection reset. Read operation aborted.");
		}
	}
	
	public void write(byte[] data) throws CommException {
		Console.log("WRITE operation started...", false);
		
		long start = System.currentTimeMillis();
		
		int totalLength = data.length;
		
		try {
			in.skip(in.available());
			
			Console.progressReset(totalLength);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			
			for (int i = 0; i < totalLength; i += Chips.getWriteBlockSize()) {
				int writeCount = 0;
				
				int nextBlock = i + Chips.getWriteBlockSize();
				if ( nextBlock <= totalLength) {
					writeCount = Chips.getWriteBlockSize();
				}
				else  {
					writeCount = totalLength - i;
				}
					
				//System.out.println(WRITE_COMMAND + i + "," + Chips.getPageSize()  + "," + writeCount);
				
				// write command format is W[page]
				bw.write(WRITE_COMMAND + i + "," + Chips.getPageSize()  + "," + writeCount);
				bw.flush();
				
				wait4Signal(SIGNAL_OP_BEGIN);
				
				out.write(data, i, writeCount);
				out.flush();

				wait4Signal(SIGNAL_OP_END);
				
				Console.progressUpdate(i);
			}
		} catch (IOException e) {
			Console.progressUpdate(0);
			//e.printStackTrace();
			throw new CommException("Connection reset. Write operation aborted.");
		}
		
		Console.log("completed.", start, System.currentTimeMillis());
	}
	
//	public static String getSelectedPortName() {
//		return selectedPortName;
//	}
//
//	public static void setSelectedPortName(String selectedPortName) {
//		Serial.selectedPortName = selectedPortName;
//	}
	
	private void wait4Signal(char s) throws IOException {
		int c;
        do {
            c = in.read();
//            if (c == -1) 
//            	break;
        } while (c != s);
	}

}
