/**
 * HEX Helper
 * @author mingzo@gmail.com
 * @since 2017/11/20
 */
package org.arc.megaburner.helper;

public class HexHelper {
    
    private static final char[] HEX_CHARS = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

	public static char[] bytes2Hex(byte[] buf) {
		char[] chars = new char[3 * buf.length];
		
		for (int i = 0; i < buf.length; i++) {
			chars[3 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
			chars[3 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
			chars[3 * i + 2] = ' ';
		}
		
		return chars;
	}

	public static String byte2Hex(int b) {
		String hex = Integer.toHexString(b);
		
		int len = hex.length();
		
		switch (len) {
		case 1:
			hex = "0000000" + hex;
			break;
		case 2:
			hex = "000000" + hex;
			break;
		case 3:
			hex = "00000" + hex;
			break;
		case 4:
			hex = "0000" + hex;
			break;
		case 5:
			hex = "000" + hex;
			break;
		case 6:
			hex = "00" + hex;
			break;
		case 7:
			hex = "0" + hex;
			break;
		default:
			break;
		}
		
		return hex;
	}
	
	public static String convertToHex(byte[] data) {
		int bytesCounter = 0;
		
		byte[] tmpHex = new byte[16];
		char[] tmpText = new char[16];
		
		StringBuilder result = new StringBuilder();

		long s = System.currentTimeMillis();
		for (int i=0; i<data.length; i++) {
			byte value = data[i];
			
			// convert to hex value with "X" formatter
			//sbHex.append(String.format("%02X", value & 0xFF));
			tmpHex[bytesCounter] = value;
			
			// If the chacater is not convertable, just print a dot symbol "."
			if (!Character.isISOControl(value)) {
				tmpText[bytesCounter] = (char) value;
			} else {
				tmpText[bytesCounter] = '.';
			}

			// if 16 bytes are read, reset the counter,
			// clear the StringBuilder for formatting purpose only.
			if (bytesCounter == 15) { 
				//String.format("%08X", (i-15) & 0xFFFFFFFF)
				result.append(byte2Hex(i-15)+"h: ").append(bytes2Hex(tmpHex)).append("; ").append(tmpText).append("\n");
				bytesCounter = 0;
			} else {
				bytesCounter++;
			}
		}
		long e = System.currentTimeMillis() - s;
		System.out.println("Hex converter time:"+e);
		
		// if still got content
		if (bytesCounter != 0) {
			String hexStr = new String(tmpHex);
			// add spaces more formatting purpose only
			for (; bytesCounter < 16; bytesCounter++) {
				// 1 character 3 spaces
				hexStr += "   ";
				
			}
			result.append(hexStr).append("      ").append(tmpText).append("\n");
		}

		return result.toString();
	}
	
//	public static void main(String[] args) {
//		
//		String rom = "C:\\Users\\only1\\Desktop\\Super Mario RPG.sfc";
//
//        try {
//        	File file = new File(rom);
//        	long fileSize = file.length();
//        	byte[] fileData = new byte[(int)fileSize];
//        	
//        	BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file));
//            for (int i = 0; i < fileSize; i++) {
//            	fileData[i] = (byte) bi.read();
//            }
//            bi.close();
//
//            HexHelper.convertToHex(fileData);
//        } catch (IOException e) {
//        	Console.log(e.getMessage(), false);
//        }
//		
//	}
}
