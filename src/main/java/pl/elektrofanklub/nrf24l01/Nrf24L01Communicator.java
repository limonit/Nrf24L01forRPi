package pl.elektrofanklub.nrf24l01;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Spi;

import java.util.concurrent.TimeUnit;


/**
 * The Nrf24L01Communicator class implements basic communication
 * using modules basing on nrf24L01(+).
 *
 * @author: Łukasz Trojanowski
 * @email: limonit@wp.pl
 * @www: www.elektrofanklub.pl
 * @version: 1.0
 */
public class Nrf24L01Communicator {
    private GpioPinDigitalOutput chipEnable;
    private GpioPinDigitalInput interrupt;

    public enum DataRate {
        DR250K, DR1M, DR2M;
    }

    public enum TxPower {
        DBM0, DBM6, DBM12, DBM18;
    }

    public Nrf24L01Communicator() {
    }

    private short readRegister(short register) {
        short[] buffer = new short[]{register, 0xFF};
        Spi.wiringPiSPIDataRW(0, buffer);
        return buffer[1];
    }

    private short writeRegister(short reg, short val) {
        short[] buffer = new short[]{(short) (Nrf24L01Registers.WRITE_REG | reg), val};
        Spi.wiringPiSPIDataRW(0, buffer);
        return buffer[0];
    }

    private short writeCommand(short command) {
        short[] buffer = new short[]{(short) (Nrf24L01Registers.WRITE_REG | command)}; // command NOP, FLUSH_TX, FLUSH_RX, REUSE_TX_PL
        Spi.wiringPiSPIDataRW(0, buffer);
        return buffer[0];
    }

    private int multibyteReadRegister(int reg, short[] pbuf) {
        int length;
        short[] buffer = new short[1];
        switch (reg) {
            case Nrf24L01Registers.RX_ADDR_P0:
            case Nrf24L01Registers.RX_ADDR_P1:
            case Nrf24L01Registers.TX_ADDR:
                length = (readRegister(Nrf24L01Registers.SETUP_AW) + 2); //RX/TX Address field width
                buffer = new short[++length];
                buffer[0] = (short) (reg);
                break;
            case Nrf24L01Registers.R_RX_PAYLOAD:
                if ((reg = ((writeRegister((short) (Nrf24L01Registers.NOP), (short) 0) & 0x0E) >> 1)) < 7) {    //read Data pipe number for the payload available for reading from  RX_FIFO
                    length = readRegister(Nrf24L01Registers.R_RX_PL_WID);
                    buffer = new short[++length];
                    buffer[0] = (short) (Nrf24L01Registers.R_RX_PAYLOAD);
                } else length = 0;
                break;
            default:
                return 0;
        }
        if (length != 0) {
            Spi.wiringPiSPIDataRW(0, buffer, length);
            System.arraycopy(buffer, 1, pbuf, 0, (pbuf.length <= length - 1) ? pbuf.length : length - 1);
        }
        return (((int) reg << 8) | length - 1);
    }

    private void multibyteWriteRegister(int reg, short pbuf[], int pipe) {
        short[] buffer = new short[pbuf.length + 1];
        switch (reg) {
            case Nrf24L01Registers.W_TX_PAYLOADD:
                buffer[0] = (short) (Nrf24L01Registers.W_TX_PAYLOADD);
                break;

            case Nrf24L01Registers.W_ACK_PAYLOAD:
                buffer[0] = (short) (Nrf24L01Registers.W_ACK_PAYLOAD | pipe);
                break;
            default:
                break;
        }
        int i = 0;
        System.arraycopy(pbuf, 0, buffer, 1, pbuf.length);
        Spi.wiringPiSPIDataRW(0, buffer);
    }

    private void multibyteWriteRegister(int reg, short pbuf[]) {
        if (reg == Nrf24L01Registers.RX_ADDR_P0 || reg == Nrf24L01Registers.RX_ADDR_P1 || reg == Nrf24L01Registers.TX_ADDR) {
            int length = (readRegister(Nrf24L01Registers.SETUP_AW) + 2); //RX/TX Address field width
            short[] buffer = new short[++length];
            buffer[0] = (short) (Nrf24L01Registers.WRITE_REG + reg);
            System.arraycopy(pbuf, 0, buffer, 1, length - 1);
            Spi.wiringPiSPIDataRW(0, buffer);
        }
    }

    /**
     * Initializes SPI with provided parameters.
     * Initializes Nrf24L01:
     * - enables CRC,
     * - activates features, (on Nrf24L01+ are already active)
     * - flushes buffers,
     * - sets Auto Retry Count to 15 (max.)
     * - sets power to max,
     *
     * @param channel
     * @param speed
     */
    public void init(int channel, int speed) {
        Spi.wiringPiSPISetup(channel, speed);
        softReset();
        writeRegister(Nrf24L01Registers.CONFIG, (short) (readRegister(Nrf24L01Registers.CONFIG) & ~(1 << Nrf24L01Registers.CRCO) | 1 << Nrf24L01Registers.EN_CRC)); //enable 1 byte CRC -- default TODO: try comment
        writeRegister(Nrf24L01Registers.SETUP_RETR, (short) (Nrf24L01Registers.ARD_1500US | Nrf24L01Registers.ARC_3)); //auto retransmit delay=1500us (250kbps), auto retransmit count=3
        setTxPower(Nrf24L01Communicator.TxPower.DBM0);
        writeRegister(Nrf24L01Registers.EN_AA, (short) (1 << Nrf24L01Registers.HAL_NRF_PIPE0 | 1 << Nrf24L01Registers.HAL_NRF_PIPE1)); //enable ACK data pipe 0
        writeRegister(Nrf24L01Registers.EN_RXADDR, (short)(1 << (Nrf24L01Registers.HAL_NRF_PIPE0)));
    }

    private void softReset() {
        powerDown();
        writeRegister(Nrf24L01Registers.STATUS, (short) 0xFF); //clear interrupt flags
        writeCommand(Nrf24L01Registers.FLUSH_TX); //remove any data
        writeCommand(Nrf24L01Registers.FLUSH_RX); //remove any data
        writeRegister(Nrf24L01Registers.EN_RXADDR, (short) 0); //disable all pipes
        writeRegister(Nrf24L01Registers.EN_AA, (short) 0); //disable all ACK pipes
        activate();
    }

    /**
     * Sets power up.
     * * If chip enable pin was set and RX mode is set,
     * chip enalbe is set high.
     */
    public void powerUp() {
        writeRegister(Nrf24L01Registers.CONFIG, (short) (readRegister(Nrf24L01Registers.CONFIG) | 1 << Nrf24L01Registers.PWR_UP));
        if (chipEnable != null && (readRegister(Nrf24L01Registers.CONFIG) & 1 << Nrf24L01Registers.PRIM_RX) != 0)
            chipEnable.high();
    }

    /**
     * Sets power down.
     * If chip enable pin was set it is also set low.
     */
    public void powerDown() {
        if (chipEnable != null) chipEnable.low();
        writeRegister(Nrf24L01Registers.CONFIG, (short) (readRegister(Nrf24L01Registers.CONFIG) & ~(1 << Nrf24L01Registers.PWR_UP)));
    }

    /**
     * Enables RX Mode.
     * If chip enable pin was set it is also set high.
     */
    public void enableRX() {
        boolean isPowerUp = isPowerUp();
        if (isPowerUp) powerDown();
        writeRegister(Nrf24L01Registers.CONFIG, (short) (readRegister(Nrf24L01Registers.CONFIG) | 1 << Nrf24L01Registers.PRIM_RX));
        if (isPowerUp) {
            powerUp();
            if (chipEnable != null) chipEnable.high();
        }
    }

    /**
     * Enables TX mode.
     */
    public void enableTX() {
        boolean isPowerUp = isPowerUp();
        if (isPowerUp) powerDown();
        if (chipEnable != null) chipEnable.low();
        writeRegister(Nrf24L01Registers.CONFIG, (short) (readRegister(Nrf24L01Registers.CONFIG) & ~(1 << Nrf24L01Registers.PRIM_RX)));
        if (isPowerUp) powerUp();
    }

    private boolean isPowerUp() {
        return ((readRegister(Nrf24L01Registers.CONFIG) & 1 << Nrf24L01Registers.PWR_UP) == 0) ? false : true;
    }

    private void activate() {
        writeRegister(Nrf24L01Registers.FEATURE, (short) (1 << Nrf24L01Registers.EN_ACK_PAY | 1 << Nrf24L01Registers.EN_DPL));
        Spi.wiringPiSPIDataRW(0, new short[]{Nrf24L01Registers.LOCK_UNLOCK, 0x73});
        if (readRegister(Nrf24L01Registers.FEATURE) == 0x00) { //TODO: looks little weird
            Spi.wiringPiSPIDataRW(0, new short[]{Nrf24L01Registers.LOCK_UNLOCK, 0x73});
            writeRegister(Nrf24L01Registers.FEATURE, (short) (1 << Nrf24L01Registers.EN_ACK_PAY | 1 << Nrf24L01Registers.EN_DPL)); //enable payload with ACK and dynamic packet length
        }
        writeRegister(Nrf24L01Registers.DYNPD, (short) 0x3F); //enable dynamic payload length for all data pipes
    }

    /**
     * Sets TX address
     *
     * @param address 3 to 5 byte addres
     */
    public void setupTXAddress(short[] address) {
        if (address.length < 2) throw new IllegalArgumentException();
        if (address.length > 5) {
            short[] address_tmp = new short[5];
            System.arraycopy(address, 0, address_tmp, 0, 5);
            address = address_tmp;
        }
        writeRegister(Nrf24L01Registers.SETUP_AW, (short) (address.length - 2));
        multibyteWriteRegister(Nrf24L01Registers.TX_ADDR, address);
    }

    /**
     * Sets RX address of device
     * and enables RX pipe.
     *
     * @param address 3 to 5 byte addres
     * @param pipe    pipe number. Currently only pipes 0 and 1 are implemented
     */
    public void setupRXAddress(short[] address, int pipe) {
        if (address.length < 2 || pipe > 1) throw new IllegalArgumentException();
        if (address.length > 5) {
            short[] address_tmp = new short[5];
            System.arraycopy(address, 0, address_tmp, 0, 5);
            address = address_tmp;
        }

        writeRegister(Nrf24L01Registers.SETUP_AW, (short) (address.length - 2));
        multibyteWriteRegister(Nrf24L01Registers.RX_ADDR_P0 + pipe, address);

        writeRegister(Nrf24L01Registers.EN_RXADDR, (short)(readRegister(Nrf24L01Registers.EN_RXADDR) | (1 << (Nrf24L01Registers.HAL_NRF_PIPE0 + pipe)))); //enable data pipe

    }

    /**
     * Sets RF channel.
     * Frequency of operation is 2400MHz + channel.
     * Range of operation is 2400-2525MHz.
     * Check your local regulations and limitation on using radio devices.
     * When using datarate 2Mbps channel is 2MHz wide.
     *
     * @param channel channel number as integer
     */
    public void setChannel(int channel) {
        if (channel < 0 || channel > 125) throw new IllegalArgumentException();
        writeRegister(Nrf24L01Registers.RF_CH, (short) channel);
    }

    /**
     * Checks if there is any unsent data
     *
     * @return
     */
    public boolean isTxFifoFull() {
        if ((readRegister(Nrf24L01Registers.FIFO_STATUS) & (1 << Nrf24L01Registers.TX_FIFO_FULL)) == 0) return false;
        else return true;
    }

    /**
     * Checks if there is any pending data in RX Fifo.
     *
     * @return
     */
    public boolean isRxFifoEmpty() {
        if ((readRegister(Nrf24L01Registers.FIFO_STATUS) & (1 << Nrf24L01Registers.RX_EMPTY)) == 0) return false;
        else return true;
    }

    /**
     * Reads single packet from RX Fifo.
     * In case packet is longer than passed databuffer data is truncated to fit into buffer.
     *
     * @param databuffer array to which packet is read
     */
    public void readPacket(short[] databuffer) {
        multibyteReadRegister(Nrf24L01Registers.R_RX_PAYLOAD, databuffer);
    }

    /**
     * Sets chip enable.
     * If chip enable is not set - pin has to be pulled high externally
     * or it has to be controlled externally by application.
     * If set - class provides basic operation.
     * In TX mode pin is set high by transmitPacket method.
     * In RX mode pin is held high as long as device is powered up.
     *
     * @param chipEnable null for disabling using chip enable pin
     */
    public void setChipEnable(GpioPinDigitalOutput chipEnable) {
        this.chipEnable = chipEnable;
        if (chipEnable != null) {
            chipEnable.low();
            chipEnable.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        }
    }

    /**
     * Transmits packet.
     * If chip enable is set it also genrates pulse on chip enalbe pin to start transmission.
     * If flushTx is false and Tx Fifo buffer is full no transmission is performed.
     * In this case flush buffer first.
     *
     * @param databuffer data to be sent. Up to 32 bytes.
     * @param flushTx    if true buffer is flushed before transmission
     * @return false in case if transmission was not performed due to full TX Fifo. True otherwise.
     */
    public boolean transmitPacket(short[] databuffer, boolean flushTx) {
        if (flushTx) writeCommand(Nrf24L01Registers.FLUSH_TX);
        if (isTxFifoFull()) return false;
        else {
            multibyteWriteRegister(Nrf24L01Registers.W_TX_PAYLOADD, databuffer, 0);
            if (chipEnable != null) {
                chipEnable.high();
                try {
                    TimeUnit.NANOSECONDS.sleep(10000);
                } catch (InterruptedException e) {
                    chipEnable.low();
                }
                chipEnable.low();
            }
            return true;
        }
    }

    /**
     * Sets interrupts handler generated by Nrf24L01
     *
     * @param interruptPin pin to which INT pin of Nrf24L01 is connected
     * @param handler      object implementing Nrf24l01interruptHandler interface
     */
    public void setInterrupt(GpioPinDigitalInput interruptPin, Nrf24L01InterruptHandler handler) {
        this.interrupt = interruptPin;
        interruptPin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                ;
                if (event.getEdge() == PinEdge.FALLING) {
                    switch (writeRegister(Nrf24L01Registers.STATUS, (short) (1 << Nrf24L01Registers.MAX_RT |
                            1 << Nrf24L01Registers.TX_DS | 1 << Nrf24L01Registers.RX_DR)) & (1 << Nrf24L01Registers.MAX_RT |
                            1 << Nrf24L01Registers.TX_DS | 1 << Nrf24L01Registers.RX_DR)) //read status register and clear interrupt flags
                    {
                        case (1 << Nrf24L01Registers.HAL_NRF_MAX_RT): // Max retries reached
                            handler.maxRT();
                            break;
                        case (1 << Nrf24L01Registers.HAL_NRF_TX_DS): // Packet sent
                            handler.txDS();
                            break;
                        case (1 << Nrf24L01Registers.HAL_NRF_RX_DR): // Packet received
                            handler.rxDR();
                            break;
                        case ((1 << Nrf24L01Registers.HAL_NRF_RX_DR) | (1 << Nrf24L01Registers.HAL_NRF_TX_DS)): // Ack payload recieved
                            handler.rxDR();
                            break;
                        default:
                            break;
                    }

                }
            }
        });
    }

    /**
     * Sets data rate.
     * Data rate of 250kbps is available only for Nrf24L01+
     *
     * @param dataRate Datarate
     */
    public void setDataRate(DataRate dataRate) {
        short rfSetup = readRegister(Nrf24L01Registers.RF_SETUP);
        if (dataRate == DataRate.DR250K) {
            writeRegister(Nrf24L01Registers.RF_SETUP, (short) ((rfSetup | 1 << Nrf24L01Registers.RF_DR_LOW) & ~(1 << Nrf24L01Registers.RF_DR)));
        }
        if (dataRate == DataRate.DR1M) {
            writeRegister(Nrf24L01Registers.RF_SETUP, (short) (rfSetup & ~(1 << Nrf24L01Registers.RF_DR_LOW | 1 << Nrf24L01Registers.RF_DR)));
        }
        if (dataRate == DataRate.DR2M) {
            writeRegister(Nrf24L01Registers.RF_SETUP, (short) ((rfSetup | 1 << Nrf24L01Registers.RF_DR) & ~(1 << Nrf24L01Registers.RF_DR_LOW)));
        }
    }

    public void flushTX() {
        writeCommand(Nrf24L01Registers.FLUSH_TX);
    }

    public void flushRX() {
        writeCommand(Nrf24L01Registers.FLUSH_RX);
    }

    public void setTxPower(TxPower power) {
        short rf_setup = readRegister(Nrf24L01Registers.RF_SETUP);

        if (power == TxPower.DBM0)
            writeRegister(Nrf24L01Registers.RF_SETUP, (short) ((rf_setup | 1 << Nrf24L01Registers.RF_PWR0) | 1 << Nrf24L01Registers.RF_PWR1)); //data rate=250kbps, power=0dBm

        if (power == TxPower.DBM6)
            writeRegister(Nrf24L01Registers.RF_SETUP, (short) ((rf_setup | 1 << Nrf24L01Registers.RF_PWR1) & ~(1 << Nrf24L01Registers.RF_PWR0))); //data rate=250kbps, power=0dBm

        if (power == TxPower.DBM12)
            writeRegister(Nrf24L01Registers.RF_SETUP, (short) ((rf_setup | 1 << Nrf24L01Registers.RF_PWR0) & ~(1 << Nrf24L01Registers.RF_PWR1)));

        if (power == TxPower.DBM18)
            writeRegister(Nrf24L01Registers.RF_SETUP, (short) (rf_setup & ~(1 << Nrf24L01Registers.RF_PWR0 | 1 << Nrf24L01Registers.RF_PWR1)));

    }
}
