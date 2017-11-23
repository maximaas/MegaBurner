# MegaBurner
A Flash Programmer

When making SFC/SNES repro cartridges, a flash programmer is needed to burner the rom file binaries into the flash(eeprom) memory chips.

A TL866 universal programmer can handle most of the chips which can be found in the market. And I successfully made a Super FX2 repro with Star Fox 2 running on it.My next target is to make an SA-1 repro to run Mario RPG which is a 32Mbit game. 

MX29L3211 is a easy to find and cheap 4MB flash chip model with 3.3v power supply, which can not be supported by TL866. 

So I decided to make a DIY programmer by my own based on Arduino Mega2560. And here's my design:

<MegaBurner>

1. A 48pin SOP adapter for holding the chip and turns SOP to DIP interface.
2. A mega2560 expansion shield wiring the 48pin DIP to arduino ports.
3. Currently it can only program MX29L3211. Various chip models can be supported in the future by supplying another expansion shield with different wiring.
4. EPROMs are not supported because the 12v program voltage needs additional power supply circuit, and I want to keep the programmer simple.

There's several succeded project on th internet, like:
Cartridge Reader Shield for Arduino Mega 2560 (https://github.com/sanni/cartreader)
MEEPROMMER (https://github.com/mkeller0815/MEEPROMMER)

Cartreader have a full 16-bit flash program feature, and tested exactly on MX29L3211 while MEEPROMMER handles read/write operations between PC and Mega2560 via serial port.

I copied and used the codes of these two features above, and wrote a window program using SWT.

<Main>

