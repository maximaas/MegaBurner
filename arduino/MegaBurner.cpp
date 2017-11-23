/**
 * Mega Burner
 * A EEPROM programmer based on Arduino Mega 2560.
 *
 * Version 1.0
 * Date 2017.11.11
 * Contact: mingzo@gmail.com
 */
#include "MegaBurner.h"
#include "MX29L3211.h"

MX29L3211 mx29l3211 = MX29L3211();

/*
 * Check the id of the chip
 *
 * Command is "C"
 */
void check() {
	mx29l3211.readId();
	mx29l3211.reset();
}

/*
 * Read operation
 *
 * the total capacity is divided into several blocks, client reads one block at a time.
 * Command example: R0,4096 (indicates the 1st block, from 0 to 4095 byte )
 *
 */
void read(String param) {
	long block_id  = split(param, ',', 0).toInt();
	long block_size = split(param, ',', 1).toInt();

	mx29l3211.reset();
	mx29l3211.read16(block_id, block_size);
}

/*
 * Erase operation
 *
 * Command is "E"
 */
void erase() {
	mx29l3211.reset();
	mx29l3211.erase();

	Serial.println('%');
}

/*
 * Write operation
 *
 * The EEPROM should support page programming.
 * Command example: W1048576,128,4096 (means write 4096bytes start at 1048576, 128bytes per page)
 */
void write(String param) {
	long offset = split(param, ',', 0).toInt();
	long page_size = split(param, ',', 1).toInt();
	long block_size = split(param, ',', 2).toInt();

	//receive incoming data to write
	byte buffer[block_size];

	Serial.println('&');
	long idx = 0;
    while(idx < block_size) {
      if(Serial.available()) {
    	  buffer[idx++] = Serial.read();
      }
    }

	if (block_size < page_size)
		page_size = block_size;

    mx29l3211.write16(offset, page_size, block_size, buffer);
    Serial.println('%');

	delay(100);
	mx29l3211.reset();
}


void setup() {
	Serial.begin(115200);

	mx29l3211.init();
}

void loop() {

	String COMMAND_DATA = "";

	while (Serial.available()) {
		COMMAND_DATA += (char)Serial.read();
	    delay(10);
	}

	if (COMMAND_DATA.length() > 0) {
		switch (COMMAND_DATA[0]) {
			case 'C':
				check();
				break;
			case 'R':
				read(COMMAND_DATA.substring(1));
				break;
			case 'E':
				erase();
				break;
			case 'W':
				write(COMMAND_DATA.substring(1));
				break;
			default:
				break;
		}
	}

}
