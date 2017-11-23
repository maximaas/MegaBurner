#ifndef _MegaBurner_H_
#define _MegaBurner_H_
#include "Arduino.h"

String split(String data, char separator, int index)
{
	String result = "";
	String remain = data;

	for (int i=0; i<=index; i++) {
		int idx = remain.indexOf(separator);

		result = remain.substring(0, idx);

		remain = remain.substring(idx+1);
	}
	return result;
}

#endif /* _MegaBurner_H_ */
