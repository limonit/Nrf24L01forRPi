## Synopsis

Library for interfacing chip Nrf24L01(+) with RaspberryPi. Project is written in Java and uses Pi4J library. Provides some basic functions as well as some more advanced like interrupts. See docs for details.

## Motivation

Nrf24L01 modules are cheap and give great possibilities, but they are not very easy to handle. This library makes posiible to start using them in just few lines of code which is the main purpose. 

## Installation

It is possible to include jar file in project or just java files containing classes. You also have to include Pi4J library.
While using Java below 1.8, some minor changes in code have to be made.
See pom.xml in repository https://github.com/limonit/Nrf24L01Examples for example.

## API Reference

Almost complete API Reference can be found here: http://elektrofanklub.pl/javadocs/Nrf24L01Doc/

## Limitations

There are some limitations to library. At this moment only pipes 0 and 1 are available, You cannot also change using CRC, setting transmission retry count and some other minor functions. Feel free to implement them.

## Contributors

Inspiration and also some code for this project comes from Michał's Rajwa library, which can be found on his www: http://uhex.blogspot.com/2014/12/obsuga-moduu-radiowego-nrf24l01p-avr.html. 

## License

Standard MIT License

Copyright (c) 2016 Łukasz Trojanowski

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
