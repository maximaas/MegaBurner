/**
 * MX29L3211
 *
 * Version 1.0
 * Date 2017.11.11
 * Contact: mingzo@gmail.com
 */
#include "MX29L3211.h"

//A4, A5, A3
MX29L3211::MX29L3211()
{
}

void MX29L3211::init()
{
	  // Set Address Pins to Output
	  //A0-A7
	  DDRF = 0xFF;
	  //A8-A15
	  DDRK = 0xFF;
	  //A16-A23
	  DDRL = 0xFF;

	  // Set Control Pins to Output OE(PH1) BYTE(PH3) WE(PH4) CE(PH6)
	  DDRH |=  (1 << 1) | (1 << 3) | (1 << 4) | (1 << 6);

	  // Set Data Pins (D0-D15) to Input
	  DDRC = 0x00;
	  DDRA = 0x00;
	  // Disable Internal Pullups
	  PORTC = 0x00;
	  PORTA = 0x00;

	  // Setting OE(PH1) BYTE(PH3) WE(PH4) HIGH
	  PORTH |= (1 << 1) | (1 << 3) | (1 << 4);
	  // Setting CE(PH6) LOW
	  PORTH &= ~(1 << 6);

	  delay(100);

	  // ID flash
	  readId();
	  reset();
}

void MX29L3211::readId() {
  // Set data pins to output
  dataOut();

  // ID command sequence
  writeWord(0x5555, 0xaa);
  writeWord(0x2aaa, 0x55);
  writeWord(0x5555, 0x90);

  // Set data pins to input again
  dataIn();

  for (int i=0; i<2; i++) {
	  Serial.print(readByte(i), HEX);
  }
}

void MX29L3211::reset() {
  // Set data pins to output
  dataOut();

  // Reset command sequence
  writeWord(0x5555, 0xaa);
  writeWord(0x2aaa, 0x55);
  writeWord(0x5555, 0xf0);

  // Set data pins to input again
  dataIn();

  delay(500);
}

void MX29L3211::read8(long block_id, long block_size) {

	long start = (block_id * block_size)/2;

	//less than 2MB, use 8bit mode
	for (long i = start; i < (start+block_size); i++) {
		byte b = readByte(i);
		Serial.write(b);
	}

}

void MX29L3211::read16(long block_id, long block_size) {

	long start = (block_id * block_size)/2;

	//larger than 2MB, use 16bit mode
	for (long i = start; i < start+block_size/2; i++) {
	    word aword = readWord(i);

	    Serial.write( aword & 0xFF );
	    Serial.write( ( aword >> 8 ) & 0xFF );
	}
}

void MX29L3211::erase() {
  // Set data pins to output
  dataOut();

  // Erase command sequence
  writeWord(0x5555, 0xaa);
  writeWord(0x2aaa, 0x55);
  writeWord(0x5555, 0x80);
  writeWord(0x5555, 0xaa);
  writeWord(0x2aaa, 0x55);
  writeWord(0x5555, 0x10);

  // Set data pins to input again
  dataIn();

  busyCheck();
}

void MX29L3211::write8(long offset, long page_size, long block_size, byte data[]) {

	// Set data pins to output
	dataOut();

	//less than 2MB, use 8bit mode
	for (long bi = 0; bi < block_size; bi+=page_size) {
		// Check if write is complete
		delayMicroseconds(100);
		busyCheck();

		// Write command sequence
		writeWord(0x5555, 0xaa);
		writeWord(0x2aaa, 0x55);
		writeWord(0x5555, 0xa0);

		// Write one full page at a time
		for (long pi = 0; pi < page_size; pi++) {
			long a = offset + bi + pi;
			long b = bi + pi;
			//byte b = data[i] & 0xFF;
			writeByte(a, data[b]);
		}
	}

	// Check if write is complete
	delayMicroseconds(100);
	busyCheck();
	// Set data pins to input again
	dataIn();
}

void MX29L3211::write16(long offset, long page_size, long block_size, byte data[]) {

	// Set data pins to output
	dataOut();

	//larger than 2MB, use 16bit mode
	for (long bi = 0; bi < block_size/2; bi+=page_size/2) {
		// Check if write is complete
		delayMicroseconds(100);
		busyCheck();

		// Write command sequence
		writeWord(0x5555, 0xaa);
		writeWord(0x2aaa, 0x55);
		writeWord(0x5555, 0xa0);

		for (long pi = 0; pi < page_size/2; pi++) {
			long address = offset/2 +bi + pi;
			long a = (bi + pi)*2;
			long b = a + 1;

			word currWord = ((data[b] & 0xFF) << 8) | (data[a] & 0xFF);

			writeWord(address, currWord);
		}
	}

	// Check if write is complete
	delayMicroseconds(100);
	busyCheck();
	// Set data pins to input again
	dataIn();
}
