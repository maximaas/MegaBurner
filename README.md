# MegaBurner
A Flash Programmer
Copyright (C) 2017 Maximaas<br/>
Version 1.0

When making SFC/SNES repro cartridges, a flash programmer is needed to burner the rom file binaries into the flash(eeprom) memory chips.

A TL866 universal programmer can handle most of the chips which can be found in the market. And I successfully made a Super FX2 repro with Star Fox 2 running on it.My next target is to make an SA-1 repro to run Mario RPG which is a 32Mbit game. 

MX29L3211 is a easy to find and cheap 4MB flash chip model with 3.3v power supply, which can not be supported by TL866. 

So I decided to make a DIY programmer by my own based on Arduino Mega2560. And here's my design:

![image](https://github.com/maximaas/MegaBurner/blob/master/pics/megaburner_hw.png)

1. A 48pin SOP adapter for holding the chip and turns SOP to DIP interface.
2. A mega2560 expansion shield wiring the 48pin DIP to arduino ports.
3. Currently it can only program MX29L3211. Various chip models can be supported in the future by supplying another expansion shield with different wiring.
4. EPROMs are not supported because the 12v program voltage needs additional power supply circuit, and I want to keep the programmer simple.

There's several succeded project on th internet, like:<br/>
Cartridge Reader Shield for Arduino Mega 2560 (https://github.com/sanni/cartreader)<br/>
MEEPROMMER (https://github.com/mkeller0815/MEEPROMMER)

Cartreader have a full 16-bit flash program feature, and tested exactly on MX29L3211 while MEEPROMMER handles read/write operations between PC and Mega2560 via serial port.

I copied and used the codes of these two features above, and wrote a window program using SWT.

![image](https://github.com/maximaas/MegaBurner/blob/master/pics/megaburner_window.png)

The wiring of expansion shield is the same as cartreader:

| Mega2560 Pins  | MX29L2311 Pins   |
|:---------------|:-----------------|
|A0              |A0                |
|A1              |A1                |
|A2              |A2                |
|A3              |A3                |
|A4              |A4                |
|A5              |A5                |
|A6              |A6                |
|A7              |A7                |
|A8              |A8                |
|A9              |A9                |
|A10             |A10               |
|A11             |A11               |
|A12             |A12               |
|A13             |A13               |
|A14             |A14               |
|A15             |A15               |
|D0              |                  |
|D1              |                  |
|D2              |                  |
|D3              |                  |
|D4              |                  |
|D5              |                  |
|D6              |BYTE (VCC)        |
|D7              |WE                |
|D8              |                  |
|D9              |CE                |
|D10             |                  |
|D11             |                  |
|D12             |                  |
|D13             |                  |
|D14             |                  |
|D15             |                  |
|D16             |OE                |
|D17             |                  |
|D18             |                  |
|D19             |                  |
|D20             |                  |
|D21             |                  |
|D22             |D8                |
|D23             |D9                |
|D24             |D10               |
|D25             |D11               |
|D26             |D12               |
|D27             |D13               |
|D28             |D14               |
|D29             |D15               |
|D30             |D7                |
|D31             |D6                |
|D32             |D5                |
|D33             |D4                |
|D34             |D3                |
|D35             |D2                |
|D36             |D1                |
|D37             |D0                |
|D38             |                  |
|D39             |                  |
|D40             |                  |
|D41             |                  |
|D42             |                  |
|D43             |                  |
|D44             |                  |
|D45             |A20               |
|D46             |A19               |
|D47             |A18               |
|D48             |A17               |
|D49             |A16               |
|D50             |                  |
|D51             |                  |
|D52             |                  |
|D53             |                  |

